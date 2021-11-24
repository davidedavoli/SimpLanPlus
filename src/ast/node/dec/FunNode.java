package ast.node.dec;
import java.util.*;

import ast.FuncBodyUtils;
import ast.Label;
import ast.STentry;
import ast.node.ArgNode;
import ast.node.Node;
import ast.node.statements.BlockNode;
import ast.node.types.*;
import semantic.Effect;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class FunNode implements Node {

  private final String id;
  private final TypeNode type;
  private ArrowTypeNode functionType; //just for ST
  private ArrayList<TypeNode> partypes;
  private final ArrayList<Node> parlist = new ArrayList<>();
  //private ArrayList<Node> declist;
  private BlockNode body;
  private String beginFuncLabel = "";
  private String endFuncLabel = "";
  private int nestingLevel;

  public FunNode (String i, TypeNode t) {
    id=i;
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

  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
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

		RetEffType abs = new RetEffType(RetEffType.RetT.ABS);
		//RetEffType pres = new RetEffType(RetEffType.RetT.PRES);

		if ( body.retTypeCheck(this).leq(abs) && !(type instanceof VoidTypeNode)) {
			res.add(new SemanticError("Possible absence of return value"));
		}
      /*if ((type instanceof VoidTypeNode) && pres.leq(body.retTypeCheck())) {
    	  res.add(new SemanticError("Return statement in void function"));
      }*/


		return res;
	}

	/**
	 * @param env Environment
	 * @return ArrayList<SemanticError>
	 */
	@Override
	public ArrayList<SemanticError> checkEffects(Environment env){
	  ArrayList<SemanticError> errors = new ArrayList<>();

	  // create a new entry in STable  with the function id
		// env.addEntry(funId.getIdentifier(), funId.getSTEntry());
		STentry entry = env.createFunDec(beginFuncLabel,endFuncLabel,functionType);
	  	env.addEntry(this.id, entry);

		// put all the argNode to RW
		List<List<Effect>> startingEffect = new ArrayList<>();

		for (Node par : parlist) {
			ArgNode argNode = (ArgNode) par;
			List<Effect> argEffect = new ArrayList<>();

			// put all the pointed var in RW
			int maxDerefLvl  = argNode.getIdNode().getEntry().getMaxDereferenceLevel();
			for(int derefLvl = 0; derefLvl < maxDerefLvl; derefLvl++){
				argEffect.add(new Effect(Effect.READWRITE));
				//argEffect.add(new Effect(Effect.INITIALIZED));
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
	private ArrayList<SemanticError> fixPointCheckEffect(Environment env, List<List<Effect>> effects){
	  ArrayList<SemanticError> errors = new ArrayList<>();

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
			//env.addEntry(arg.getId(), arg.getId().getSTentry());
			//var argEntry = arg.getId().getSTentry();

			// update the status
			for (int derefLvl = 0; derefLvl < argEntry.getMaxDereferenceLevel(); derefLvl++) {
				// effects.get(argIndex).get(derefLvl) is the status of the argIndex-th argument at the dereference level derefLvl
				// given as this method parameter.
				argEntry.setDereferenceLevelVariableStatus(new Effect(effects.get(argIndex).get(derefLvl)), derefLvl);
			}
		}

		// Adding the function to the current scope for non-mutual recursive calls.
		STentry effectsFunEntry = env.createFunDecEffects(id,functionType);
		System.out.println(effectsFunEntry.getType());
		// add this node to recall this checkEffects
		effectsFunEntry.setFunctionNode(this);
		// impedisce la creazione di blocchi nelle successive iterazioni
		// controllare se serve, nella visita dell'albero viene fatto ma
		// in teoria serve rifarlo. Comunque metterlo a true non dovrebbe causare danni <=========
		body.setIsFunction(true);


		// keep the old effect
		Environment decFunEnv = new Environment(env);
		List<List<Effect>> effectsCopy = new ArrayList<>();

		for (var status : effectsFunEntry.getFunctionStatusList()) {
			effectsCopy.add(new ArrayList<>(status));
		}

		// =====================================================================
		// 3. Single execution of the function's body
		errors.addAll(checkBodyAndUpdateArgs(env, effectsFunEntry)); // env is updated after this call.

		// =====================================================================
		// 4. check if effects are changed after the body
		boolean different_funType = !effectsFunEntry.getFunctionStatusList().equals(effectsCopy);

		while (different_funType){
			//effect are changed!
			// replace the env and update status with the new effects

			env.update(decFunEnv);

			// lookUp should work??
			var funEntry = env.effectsLookUp(id);

			for (int argIndex = 0; argIndex < parlist.size(); argIndex++) {

				var argNode = (ArgNode) parlist.get(argIndex);
				var argEntry = env.effectsLookUp(argNode.getIdNode().getID());

				var argStatusList = effectsFunEntry.getFunctionStatusList().get(argIndex);

				for (int derefLvl = 0; derefLvl < argEntry.getMaxDereferenceLevel(); derefLvl++) {
					funEntry.setParameterStatus(argIndex, argStatusList.get(derefLvl), derefLvl);
				}
			}

			// they are the new starting effect
			effectsCopy = new ArrayList<>(effectsFunEntry.getFunctionStatusList());

			errors.addAll(checkBodyAndUpdateArgs(env, effectsFunEntry));

			// repeat until the effects are not modified
			different_funType = !effectsFunEntry.getFunctionStatusList().equals(effectsCopy);
		}

		// =====================================================================
		// 5. popScope and update the original env with the computed effect after fixpoint
		env.popBlockScope();

		// Setting the computed statuses in the function arguments and saving them in the Symbol Table entry of the function funId.
		var idEntry = env.effectsLookUp(id);
		for (int argIndex = 0; argIndex < parlist.size(); argIndex++) {
			//Update ID in previous scope
			var argStatuses = effectsFunEntry.getFunctionStatusList().get(argIndex);

			for (int derefLvl = 0; derefLvl < argStatuses.size(); derefLvl++) {
				idEntry.setParameterStatus(argIndex, argStatuses.get(derefLvl), derefLvl);
			}
		}

		return errors;
	}

	private ArrayList<SemanticError> checkBodyAndUpdateArgs(Environment env, STentry innerFunEntry){
		ArrayList<SemanticError> errors = new ArrayList<>();
		errors.addAll(body.checkEffects(env));

		for(int argIndex = 0; argIndex < parlist.size(); argIndex++){
			ArgNode arg = (ArgNode) parlist.get(argIndex);

			var argEntry = env.effectsLookUp(arg.getIdNode().getID());
			STentry functionEntry = env.effectsLookUp(id);

			for (int derefLvl = 0; derefLvl < argEntry.getMaxDereferenceLevel(); derefLvl++) {

				functionEntry.setParameterStatus(argIndex, argEntry.getDereferenceLevelVariableStatus(derefLvl), derefLvl);
				innerFunEntry.setParameterStatus(argIndex, argEntry.getDereferenceLevelVariableStatus(derefLvl), derefLvl);

			}
			System.out.println("Setted function status list in env "+functionEntry.getFunctionStatusList());
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

	  cgen.append(body.codeGeneration(labelManager)).append("\n");

	  RetEffType pres = new RetEffType(RetEffType.RetT.PRES);
	  if ((type instanceof VoidTypeNode) && !pres.leq(body.retTypeCheck(this))) {
		  cgen.append("subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return \n");
		  cgen.append("lw $fp 0($fp) //Load old $fp pushed \n");
	  }

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

}