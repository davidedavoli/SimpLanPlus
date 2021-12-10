package ast.node.dec;
import java.util.*;

import ast.FuncBodyUtils;
import ast.Label;
import ast.STentry;
import ast.node.ArgNode;
import ast.node.IdNode;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.statements.BlockNode;
import ast.node.types.*;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;

public class FunNode extends MetaNode {
	private final String id;
	private final IdNode functionIdNode;
	private final TypeNode type;
	private ArrowTypeNode functionType;
	private ArrayList<TypeNode> parameterTypes;
	private final ArrayList<Node> parameterList = new ArrayList<>();
	private final List<Effect> returnEffect = new ArrayList<>();
	private BlockNode body;
	private final String beginFuncLabel;
	private final String endFuncLabel;



	public FunNode (String i, TypeNode t, IdNode functionIdNode) {
    	id=i;
		this.functionIdNode = functionIdNode;
    	type=t;
		beginFuncLabel = FuncBodyUtils.freshFunLabel();
		endFuncLabel = FuncBodyUtils.endFreshFunLabel();
  	}
	public String getId() {
		return id;
	}
	public String get_end_fun_label(){
		return endFuncLabel;
	}

	public BlockNode getBody() {
		return body;
	}
	public List<Node> getPars() {
		return parameterList;
	}

  	public void addFunBlock(BlockNode b) {
		body=b;
		body.setIsFunction(true);
  	}
  	public void addPar (ArgNode p) {
    	parameterList.add(p);
  	}


	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {

		//create result list
		ArrayList<SemanticError> res = new ArrayList<>();

		ArrayList<TypeNode> parTypes = new ArrayList<>();
		for(Node a : parameterList){
			ArgNode arg = (ArgNode) a;
			parTypes.add(arg.getType());
		}
		//set func type
		parameterTypes =parTypes;

		functionType = new ArrowTypeNode(parameterTypes, type);

		//env.offset = -2;
		HashMap<String, STentry> hm = env.getCurrentST();
		STentry entry = env.createFunDec(beginFuncLabel,endFuncLabel,functionType);
		functionIdNode.setEntry(entry);
		functionIdNode.getEntry().setFunctionNode(this);
		functionIdNode.getEntry().setBeginLabel(beginFuncLabel);
		functionIdNode.getEntry().setEndLabel(endFuncLabel);

		if ( hm.put(id,entry) != null )
			res.add(new SemanticError("Fun id '"+id+"' already declared"));
		else{
			env.createVoidScope();
			int parameterOffset=1;
			//check args
			for(Node a : parameterList){
				ArgNode arg = (ArgNode) a;
				STentry oldEntry = env.newFunctionParameter(arg.getIdNode().getID(),arg.getType(),parameterOffset++);
				arg.getIdNode().setEntry(env.lookUp(arg.getIdNode().getID()));
				if(oldEntry != null)
					res.add(new SemanticError("Parameter id '"+arg.getIdNode().getID()+"' already declared"));
			}
			//set func type
			res.addAll(body.checkSemantics(env));

			//close scope
			env.popFunScope();
		}

		HasReturn noReturn = new HasReturn(HasReturn.hasReturnType.ABS);

		if ( body.retTypeCheck().leq(noReturn) && !(type instanceof VoidTypeNode)) {
			res.add(new SemanticError("Possible absence of return value"));
		}
		return res;
	}

	public TypeNode typeCheck() {
		body.typeCheck();
		return new ArrowTypeNode(parameterTypes, type);
	}
	public HasReturn retTypeCheck() {
		return new HasReturn(HasReturn.hasReturnType.ABS);
	}

	/**
	 * @param env Environment
	 * @return ArrayList<SemanticError>
	 */
	@Override
	public ArrayList<EffectError> checkEffects (Environment env){

		// create a new entry in STable  with the function id
	  	env.addEntry(this.id, functionIdNode.getEntry());
		functionIdNode.getEntry().setFunctionNode(this);
		functionIdNode.getEntry().setBeginLabel(beginFuncLabel);
		functionIdNode.getEntry().setEndLabel(endFuncLabel);

		// put all the argNode to RW
		List<List<Effect>> startingEffect = new ArrayList<>();

		for (Node par : parameterList) {
			ArgNode argNode = (ArgNode) par;
			List<Effect> argEffect = new ArrayList<>();

			// put all the pointed var in RW
			int maxDereferenceLevel  = argNode.getIdNode().getEntry().getMaxDereferenceLevel();
			for(int dereferenceLevel = 0; dereferenceLevel < maxDereferenceLevel; dereferenceLevel++){
				argEffect.add(new Effect(Effect.READWRITE));
			}
			startingEffect.add(argEffect);
		}

		return new ArrayList<>(fixPointCheckEffect(env, startingEffect));
	}
	/**
	 *
	 * @param env Environment
	 * @param effects List of effect
	 * @return  ArrayList<SemanticError>
	 */
	public ArrayList<EffectError> fixPointCheckEffect(Environment env, List<List<Effect>> effects){

		// 1. copy the old env, before analyze the body of the function
		env.createVoidScope();

		// =====================================================================
		int argOffset = 1;
		// 2. put at RW all formal parameters
		for(int i = 0; i < parameterList.size(); i++){
			var arg = (ArgNode) parameterList.get(i);
			env.newFunctionParameter(arg.getIdNode().getID(), arg.getType(), argOffset++);
			STentry argEntry = env.effectsLookUp(arg.getIdNode().getID());
			// update the status
			for (int dereferenceLevel = 0; dereferenceLevel < argEntry.getMaxDereferenceLevel(); dereferenceLevel++) {
				argEntry.setDereferenceLevelVariableStatus(new Effect(effects.get(i).get(dereferenceLevel)), dereferenceLevel);
			}
		}

		// Adding the function to the current scope for non-mutual recursive calls.
		STentry effectsFunEntry = env.createNewDeclaration(id,functionType);
		// add this node to recall this checkEffects
		effectsFunEntry.setFunctionNode(this);
		body.setIsFunction(true);

		Environment decFunEnv = new Environment(env);
		List<List<Effect>> effectsCopy = new ArrayList<>();

		for (var status : effectsFunEntry.getFunctionStatusList()) {
			effectsCopy.add(new ArrayList<>(status));
		}

		ArrayList<EffectError> errors = new ArrayList<>(checkInstructions(env, effectsFunEntry));

		while (effectsAreDifferent(effectsFunEntry, effectsCopy)){
			// effect are changed!
			// replace the env and update status with the new effects
			env.replaceWithNewEnvironment(decFunEnv);

			var funEntry = env.effectsLookUp(id);

			for (int parIndex = 0; parIndex < parameterList.size(); parIndex++) {
				var argNode = (ArgNode) parameterList.get(parIndex);
				var argEntry = env.effectsLookUp(argNode.getIdNode().getID());

				var argStatusList = effectsFunEntry.getFunctionStatusList().get(parIndex);

				for (int dereferenceLevel = 0; dereferenceLevel < argEntry.getMaxDereferenceLevel(); dereferenceLevel++) {
					funEntry.setParameterStatus(parIndex, argStatusList.get(dereferenceLevel), dereferenceLevel);
				}
			}
			effectsCopy = new ArrayList<>(effectsFunEntry.getFunctionStatusList());
			errors.addAll(checkInstructions(env, effectsFunEntry));
		}

		env.popBlockScope();
		// Update effects of arguments
		var idEntry = env.effectsLookUp(id);
		for (int parIndex = 0; parIndex < parameterList.size(); parIndex++) {
			var argStatuses = effectsFunEntry.getFunctionStatusList().get(parIndex);

			for (int dereferenceLevel = 0; dereferenceLevel < argStatuses.size(); dereferenceLevel++) {
				idEntry.setParameterStatus(parIndex, argStatuses.get(dereferenceLevel), dereferenceLevel);
			}
		}
		/*
		if(!(type instanceof VoidTypeNode)){
			List<Effect> maxReturnEffect = getMaxEffect();
			idEntry.setResultList(maxReturnEntry.getStatusList());
		}
		*/
		return errors;
	}
	/*public List<Effect> getMaxEffect(){
		List<RetNode> listOfReturnNodeFunction = returnNodeInBlock();
		List<List<Effect>> returnNodeEffect = new ArrayList<>();
		List<STentry> returnNodeEntry = new ArrayList<>();
		for (RetNode returnNode: listOfReturnNodeFunction){
			ExpNode returnValue = returnNode.getValNode();
			if(returnValue != null){

				if(returnValue instanceof  Dereferences){
					Dereferences returnValueDereference = (Dereferences) returnValue;
					returnNodeEffect.add(returnValueDereference.getEntry().getStatusList());
				}
				else{
					List<Effect> expNodeEffect = new ArrayList<>();
					expNodeEffect.add(Effect.READWRITE);
					returnNodeEffect.add(expNodeEffect);
				}
			}
		}

		List<Effect> maxReturnEffect = returnNodeEntry.get(0).getStatusList();


		for(int i=1;i<returnNodeEffect.size()-1;i++){

			for(int j=0;j<returnNodeEffect.get(i).size();j++){
				Effect maxEffect = Effect.maxEffectNoCopy(
						maxReturnEffect.get(j),
						returnNodeEffect.get(i).get(j)
				);
				maxReturnEffect.set(j,maxEffect);

			}
		}
		return  maxReturnEffect;
	}*/
	private boolean effectsAreDifferent(STentry effectsFunEntry, List<List<Effect>> effectsCopy) {
		return !effectsFunEntry.getFunctionStatusList().equals(effectsCopy);
	}
	private ArrayList<EffectError> checkInstructions(Environment env, STentry innerFunEntry){
		ArrayList<EffectError> errors = new ArrayList<>(body.checkEffects(env));

		/*
		List<Effect> maxReturnEffect = new ArrayList<>();
		if(!(type instanceof VoidTypeNode)){
			maxReturnEffect = getMaxEffect();
			innerFunEntry.setResultList(maxReturnEffect);
		}
		*/

		STentry functionEntry = env.effectsLookUp(id);

		for(int parIndex = 0; parIndex < parameterList.size(); parIndex++){
			ArgNode arg = (ArgNode) parameterList.get(parIndex);

			var argEntry = env.effectsLookUp(arg.getIdNode().getID());
			for (int dereferenceLevel = 0; dereferenceLevel < argEntry.getMaxDereferenceLevel(); dereferenceLevel++) {
				functionEntry.setParameterStatus(parIndex, argEntry.getDereferenceLevelVariableStatus(dereferenceLevel), dereferenceLevel);
				innerFunEntry.setParameterStatus(parIndex, argEntry.getDereferenceLevelVariableStatus(dereferenceLevel), dereferenceLevel);
			}
		}
		/*if(!(type instanceof VoidTypeNode)){
			maxReturnEffect = getMaxEffect(env,functionEntry);
			functionEntry.setResultList(maxReturnEffect);
		}*/
		return errors;
	}

	public String codeGeneration(Label labelManager) {
	  int declaration_size = 0;
	  int parameter_size = parameterList.size();

	  StringBuilder codeGenerated = new StringBuilder();

	  codeGenerated.append("//BEGIN FUNCTION ").append(beginFuncLabel).append("\n");
	  codeGenerated.append(beginFuncLabel).append(":\n");

	  codeGenerated.append("mv $sp $fp\n");
	  codeGenerated.append("push $ra\n");


		HasReturn returnEffect = new HasReturn(HasReturn.hasReturnType.PRES);

		if ((type instanceof VoidTypeNode) && !returnEffect.leq(body.retTypeCheck())) {
			StringBuilder missingReturnCode = new StringBuilder();

			missingReturnCode.append("subi $sp $fp 1 //Restore stack pointer as before block creation in a void function without return \n");
			missingReturnCode.append("lw $fp 0($fp) //Load old $fp pushed \n");
			missingReturnCode.append("b ").append(endFuncLabel).append("\n");

			body.addMissingReturnFunctionCode(missingReturnCode.toString());
		}

	  codeGenerated.append(body.codeGeneration(labelManager)).append("\n");

	  codeGenerated.append(endFuncLabel).append(":\n");

	  codeGenerated.append("lw $ra 0($sp)\n");

	  codeGenerated.append("pop\n");

	  codeGenerated.append("addi $sp $sp ").append(declaration_size).append("//pop declaration ").append(declaration_size).append("\n");
	  codeGenerated.append("addi $sp $sp ").append(parameter_size).append("// pop parameters").append(parameter_size).append("\n");
	  codeGenerated.append("pop\n");
	  codeGenerated.append("lw $fp 0($sp)\n");
	  codeGenerated.append("pop\n");

	  codeGenerated.append("jr $ra\n");

	  codeGenerated.append("// END OF ").append(id).append("\n");

	  return codeGenerated.toString();
  }


	public String toPrint(String s) {
		StringBuilder parameterString= new StringBuilder();
		for (Node par: parameterList)
			parameterString.append(par.toPrint(s + "  "));
		return s+"Fun:" + id +"\n"
				+type.toPrint(s+"  ")
				+parameterString
				+body.toPrint(s+"  ") ;
	}
}