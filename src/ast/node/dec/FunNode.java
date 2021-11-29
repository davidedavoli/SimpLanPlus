package ast.node.dec;
import java.util.*;
import java.util.stream.Collectors;

import ast.Dereferenceable;
import ast.FuncBodyUtils;
import ast.Label;
import ast.STentry;
import ast.node.ArgNode;
import ast.node.IdNode;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.exp.ExpNode;
import ast.node.exp.IdExpNode;
import ast.node.exp.LhsExpNode;
import ast.node.statements.BlockNode;
import ast.node.statements.RetNode;
import ast.node.types.*;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class FunNode extends MetaNode {

	private final String id;
	private final IdNode functionIdNode;
  private final TypeNode type;
  private ArrowTypeNode functionType; //just for ST
  private ArrayList<TypeNode> partypes;
  private final ArrayList<Node> parlist = new ArrayList<>();
  private List<Effect> returnEffect = new ArrayList<>();
  private BlockNode body;
  private String beginFuncLabel = "";
  private String endFuncLabel = "";
  private int nestingLevel;

  public FunNode (String i, TypeNode t, IdNode functionIdNode) {
    id=i;
	this.functionIdNode = functionIdNode;
    type=t;
	beginFuncLabel = FuncBodyUtils.freshFunLabel();
	endFuncLabel = FuncBodyUtils.endFreshFunLabel();
	nestingLevel = -1;

  }

  public void addFunBlock(BlockNode b) {
    body=b;
	body.setIsFunction(true);

  }

  public String get_end_fun_label(){
		return endFuncLabel;
	}

  public void addPar (ArgNode p) {
    parlist.add(p);
  }

	public BlockNode getBody() {
		return body;
	}
	public List<Node> getPars() {
		return parlist;
	}

	public String toPrint(String s) throws SimplanPlusException {
	StringBuilder parlstr= new StringBuilder();
	for (Node par:parlist)
	  parlstr.append(par.toPrint(s + "  "));
    return s+"Fun:" + id +"\n"
		   +type.toPrint(s+"  ")
		   +parlstr
           +body.toPrint(s+"  ") ;
  }

  //valore di ritorno non utilizzato
  public TypeNode typeCheck () throws SimplanPlusException {
	body.typeCheck();
    return new ArrowTypeNode(partypes, type);
  }

  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }



	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {

		//create result list
		ArrayList<SemanticError> res = new ArrayList<>();

		ArrayList<TypeNode> parTypes = new ArrayList<>();
		for(Node a : parlist){
			ArgNode arg = (ArgNode) a;
			parTypes.add(arg.getType());
		}
		//set func type
		partypes=parTypes;

		functionType = new ArrowTypeNode(partypes, type);


		//env.offset = -2;
		HashMap<String, STentry> hm = env.getCurrentST();
		STentry entry = env.createFunDec(beginFuncLabel,endFuncLabel,functionType);
		functionIdNode.setEntry(entry);
		functionIdNode.getEntry().setFunctionNode(this);
		functionIdNode.getEntry().setBeginLabel(beginFuncLabel);
		functionIdNode.getEntry().setEndLabel(endFuncLabel);

		nestingLevel = env.getNestingLevel();
		if ( hm.put(id,entry) != null )
			res.add(new SemanticError("Fun id '"+id+"' already declared"));
		else{
			env.createVoidScope();
			int paroffset=1;
			//check args
			for(Node a : parlist){
				ArgNode arg = (ArgNode) a;
				STentry oldEntry = env.newFunctionParameter(arg.getIdNode().getID(),arg.getType(),paroffset++);
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
		//RetEffType isReturn = new RetEffType(RetEffType.RetT.PRES);

		if ( body.retTypeCheck().leq(noReturn) && !(type instanceof VoidTypeNode)) {
			res.add(new SemanticError("Possible absence of return value"));
		}
      /*if ((type instanceof VoidTypeNode) && isReturn.leq(body.retTypeCheck())) {
    	  res.add(new SemanticError("Return statement in void function"));
      }*/


		return res;
	}

	public List<RetNode> returnNodeInBlock(){

		return body.getGrandChildren().stream()
				.filter( metaNode -> metaNode instanceof RetNode)
				.map(metaNode -> (RetNode)metaNode)
				.collect(Collectors.toList());
	}


	/**
	 * @param env Environment
	 * @return ArrayList<SemanticError>
	 */
	@Override
	public ArrayList<EffectError> checkEffects (Environment env){
	  ArrayList<EffectError> errors = new ArrayList<>();

	  // create a new entry in STable  with the function id
	  	env.addEntry(this.id, functionIdNode.getEntry());
		functionIdNode.getEntry().setFunctionNode(this);
		functionIdNode.getEntry().setBeginLabel(beginFuncLabel);
		functionIdNode.getEntry().setEndLabel(endFuncLabel);

		// put all the argNode to RW
		List<List<Effect>> startingEffect = new ArrayList<>();

		for (Node par : parlist) {
			ArgNode argNode = (ArgNode) par;
			List<Effect> argEffect = new ArrayList<>();

			// put all the pointed var in RW
			int maxDereferenceLevel  = argNode.getIdNode().getEntry().getMaxDereferenceLevel();
			for(int dereferenceLevel = 0; dereferenceLevel < maxDereferenceLevel; dereferenceLevel++){
				argEffect.add(new Effect(Effect.READWRITE));
			}
			startingEffect.add(argEffect);
		}


		errors.addAll(fixPointCheckEffect(env, startingEffect));



	  return errors;
	}

	/**
	 *
	 * @param env Environment
	 * @param effects List of effect
	 * @return  ArrayList<SemanticError>
	 */
	public ArrayList<EffectError> fixPointCheckEffect(Environment env, List<List<Effect>> effects){
	  ArrayList<EffectError> errors = new ArrayList<>();
	  List<Effect> maxReturnEffect = new ArrayList<>();
				// =====================================================================
	  // 1. copy the old env, before analyze the body of the function
		env.createVoidScope();

		// =====================================================================
		int argOffset = 1;
		// 2. put at RW all the formal parameters
		for(int argIndex = 0; argIndex < parlist.size(); argIndex++){
			var arg = (ArgNode) parlist.get(argIndex);

			env.newFunctionParameter(arg.getIdNode().getID(), arg.getType(), argOffset++);

			STentry argEntry = env.effectsLookUp(arg.getIdNode().getID());

			// update the status
			for (int derefLvl = 0; derefLvl < argEntry.getMaxDereferenceLevel(); derefLvl++) {
				argEntry.setDereferenceLevelVariableStatus(new Effect(effects.get(argIndex).get(derefLvl)), derefLvl);
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

		errors.addAll(checkBodyAndUpdateArgs(env, effectsFunEntry));

		boolean different_funType = !effectsFunEntry.getFunctionStatusList().equals(effectsCopy);

		while (different_funType){
			// effect are changed!
			// replace the env and update status with the new effects

			env.replaceWithNewEnv(decFunEnv);

			var funEntry = env.effectsLookUp(id);

			for (int argIndex = 0; argIndex < parlist.size(); argIndex++) {

				var argNode = (ArgNode) parlist.get(argIndex);
				var argEntry = env.effectsLookUp(argNode.getIdNode().getID());

				var argStatusList = effectsFunEntry.getFunctionStatusList().get(argIndex);

				for (int derefLvl = 0; derefLvl < argEntry.getMaxDereferenceLevel(); derefLvl++) {
					funEntry.setParameterStatus(argIndex, argStatusList.get(derefLvl), derefLvl);
				}
			}

			effectsCopy = new ArrayList<>(effectsFunEntry.getFunctionStatusList());

			errors.addAll(checkBodyAndUpdateArgs(env, effectsFunEntry));

			different_funType = !effectsFunEntry.getFunctionStatusList().equals(effectsCopy);
		}

		env.popBlockScope();

		var idEntry = env.effectsLookUp(id);
		for (int argIndex = 0; argIndex < parlist.size(); argIndex++) {
			var argStatuses = effectsFunEntry.getFunctionStatusList().get(argIndex);

			for (int dereferenceLevel = 0; dereferenceLevel < argStatuses.size(); dereferenceLevel++) {
				idEntry.setParameterStatus(argIndex, argStatuses.get(dereferenceLevel), dereferenceLevel);
			}
		}
		if(!(type instanceof VoidTypeNode)){
			maxReturnEffect = getMaxEffect();

			idEntry.setResultList(maxReturnEffect);
		}


		return errors;
	}

	public List<Effect> getMaxEffect(){
		List<RetNode> listOfReturnNodeFunction = returnNodeInBlock();
		List<List<Effect>> returnNodeEffect= new ArrayList<>();
		for (RetNode returnNode: listOfReturnNodeFunction){
			ExpNode returnValue = returnNode.getValNode();
			if(returnValue != null){

				if(returnValue instanceof  Dereferenceable){
					Dereferenceable returnValueDereference = (Dereferenceable) returnValue;
					returnNodeEffect.add(returnValueDereference.getEntry().getStatusList());
				}
				else{
					List<Effect> expNodeEffect = new ArrayList<>();
					expNodeEffect.add(Effect.READWRITE);
					returnNodeEffect.add(expNodeEffect);
				}
			}
		}
		List<Effect> maxReturnEffect = returnNodeEffect.get(0);
		for(int i=1;i<returnNodeEffect.size()-1;i++){

			for(int j=0;j<returnNodeEffect.get(i).size();j++){
				Effect maxEffect = Effect.maxEffect(
						maxReturnEffect.get(j),
						returnNodeEffect.get(i).get(j)
				);
				maxReturnEffect.set(j,maxEffect);

			}
		}
		return  maxReturnEffect;
	}
	private ArrayList<EffectError> checkBodyAndUpdateArgs(Environment env, STentry innerFunEntry){
		ArrayList<EffectError> errors = new ArrayList<>();
		errors.addAll(body.checkEffects(env));
		List<Effect> maxReturnEffect = new ArrayList<>();
		if(!(type instanceof VoidTypeNode)){
			maxReturnEffect = getMaxEffect();

			innerFunEntry.setResultList(maxReturnEffect);
		}

		//System.out.println("max Effect returning from function check body "+innerFunEntry.getReturnList());

		for(int argIndex = 0; argIndex < parlist.size(); argIndex++){
			ArgNode arg = (ArgNode) parlist.get(argIndex);

			var argEntry = env.effectsLookUp(arg.getIdNode().getID());
			STentry functionEntry = env.effectsLookUp(id);

			for (int dereferenceLevel = 0; dereferenceLevel < argEntry.getMaxDereferenceLevel(); dereferenceLevel++) {

				functionEntry.setParameterStatus(argIndex, argEntry.getDereferenceLevelVariableStatus(dereferenceLevel), dereferenceLevel);
				innerFunEntry.setParameterStatus(argIndex, argEntry.getDereferenceLevelVariableStatus(dereferenceLevel), dereferenceLevel);

			}
			/*System.out.println("function " +functionEntry);
			System.out.println("max " +maxReturnEffect);*/
			if(!(type instanceof VoidTypeNode)){
				functionEntry.setResultList(maxReturnEffect);
			}

			//System.out.println("function " +functionEntry);

		}
		return errors;
	}


	public String codeGeneration(Label labelManager) throws SimplanPlusException {
	  int declaration_size = 0;
	  int parameter_size = parlist.size();

	  StringBuilder cgen = new StringBuilder();

	  cgen.append("//BEGIN FUNCTION ").append(beginFuncLabel).append("\n");
	  cgen.append(beginFuncLabel).append(":\n");

	  cgen.append("mv $sp $fp\n");
	  cgen.append("push $ra\n");


		HasReturn returnEffect = new HasReturn(HasReturn.hasReturnType.PRES);

		if ((type instanceof VoidTypeNode) && !returnEffect.leq(body.retTypeCheck())) {
			StringBuilder missingReturnCode = new StringBuilder();

			missingReturnCode.append("subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return \n");
			missingReturnCode.append("lw $fp 0($fp) //Load old $fp pushed \n");
			missingReturnCode.append("b ").append(endFuncLabel).append("\n");

			body.addMissingReturnFunctionCode(missingReturnCode.toString());
		}

	  cgen.append(body.codeGeneration(labelManager)).append("\n");

	  cgen.append(endFuncLabel).append(":\n");


	  cgen.append("lw $ra 0($sp)\n");

	  cgen.append("pop\n");

	  cgen.append("addi $sp $sp ").append(declaration_size).append("//pop declaration ").append(declaration_size).append("\n");
	  cgen.append("addi $sp $sp ").append(parameter_size).append("// pop parameters").append(parameter_size).append("\n");
	  cgen.append("pop\n");
	  cgen.append("lw $fp 0($sp)\n");
	  cgen.append("pop\n");

	  cgen.append("jr $ra\n");

	  cgen.append("// END OF ").append(id).append("\n");


	  return cgen.toString();
  }

	public String getId() {
		return id;
	}

	public IdNode getIdNode() {
		return functionIdNode;
	}
}