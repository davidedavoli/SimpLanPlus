package ast.node.dec;
import java.util.*;
import java.util.function.Consumer;

import ast.Dereferenceable;
import ast.FuncBodyUtils;
import ast.Label;
import ast.STentry;
import ast.node.ArgNode;
import ast.node.Node;
import ast.node.statements.BlockNode;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.VoidTypeNode;
import semantic.Effect;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class FunNode implements Node {

  private String id;
  private TypeNode type;
  private ArrayList<TypeNode> partypes;
  private ArrayList<ArgNode> parlist = new ArrayList<ArgNode>();
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
	//System.out.println("CREO FUNZIONE " +id);
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
	String parlstr="";
	for (Node par:parlist)
	  parlstr+=par.toPrint(s+"  ");
    return s+"Fun:" + id +"\n"
		   +type.toPrint(s+"  ")
		   +parlstr
	   	   //+declstr
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
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();

		//env.offset = -2;
		HashMap<String, STentry> hm = env.getCurrentST();
		STentry entry = env.createFunDec(beginFuncLabel,endFuncLabel);
		nestingLevel = env.getNestingLevel();
		if ( hm.put(id,entry) != null )
			res.add(new SemanticError("Fun id '"+id+"' already declared"));
		else{
			env.createVoidScope();

			ArrayList<TypeNode> parTypes = new ArrayList<TypeNode>();
			int paroffset=1;
			//check args
			for(Node a : parlist){
				ArgNode arg = (ArgNode) a;
				parTypes.add(arg.getType());
				STentry oldEntry = env.newFunctionParameter(arg.getId(),arg.getType(),paroffset++);
				if(oldEntry != null)
					res.add(new SemanticError("Parameter id '"+arg.getId()+"' already declared"));
			}
			//set func type
			partypes= parTypes;

			entry.addType( new ArrowTypeNode(parTypes, type) );

			res.addAll(body.checkSemantics(env));

			//close scope

			env.popFunScope();


		}

		RetEffType abs = new RetEffType(RetEffType.RetT.ABS);
		RetEffType pres = new RetEffType(RetEffType.RetT.PRES);

		if ( body.retTypeCheck(this).leq(abs) && !(type instanceof VoidTypeNode)) {
			res.add(new SemanticError("Possible absence of return value"));
		}
      /*if ((type instanceof VoidTypeNode) && pres.leq(body.retTypeCheck())) {
    	  res.add(new SemanticError("Return statement in void function"));
      }*/


		return res;
	}

	/**
	 * @param env
	 * @return
	 */
	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
	  ArrayList<SemanticError> errors = new ArrayList<>();

	  // create a new entry in STable  with the function id
		// env.addEntry(funId.getIdentifier(), funId.getSTEntry());
		STentry entry = env.createFunDec(beginFuncLabel,endFuncLabel);
	  	env.addEntry(this.id, entry);

		// put all the argNode to RW
		List<List<Effect>> startingEffect = new ArrayList<>();
		int argOffset = 1;


		for (ArgNode argNode : parlist) {
			List<Effect> argEffect = new ArrayList<>();
//			env.newFunctionParameter(argNode.getId(), argNode.getType(), argOffset++);

			// put all the pointed var in RW
			int maxDerefLvl  = env.effectsLookUp(argNode.getId()).getMaxDereferenceLevel();
			for(int derefLvl = 0; derefLvl < maxDerefLvl; derefLvl++){
				//argEffect.add(new Effect(Effect.READWRITE));
				argEffect.add(new Effect(Effect.INITIALIZED));
			}
			startingEffect.add(argEffect);
		}


		errors.addAll(fixPointCheckEffect(env, startingEffect));



	  return errors;
	}

	/**
	 *
	 * @param env
	 * @param effects
	 * @return
	 */
	private ArrayList<SemanticError> fixPointCheckEffect(Environment env, List<List<Effect>> effects) {
	  ArrayList<SemanticError> errors = new ArrayList<>();

	  // =====================================================================
	  // 1. copy the old env, before analyze the body of the function
		env.createVoidScope();

		// =====================================================================
		int argOffset = 1;
		// 2. put at RW all the formal parameters
		for(int argIndex = 0; argIndex < parlist.size(); argIndex++){
			var arg = parlist.get(argIndex);

			env.newFunctionParameter(arg.getId(), arg.getType(), argOffset++);

			STentry argEntry = env.effectsLookUp(arg.getId());
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
		STentry innerFunEntry = env.addUniqueNewDeclaration(funId.getIdentifier(), funType);
		// setup for the analysis of the function's body
		innerFunEntry.setFunctionNode(this);
		// impedisce la creazione di blocchi nelle successive iterazioni
		// controllare se serve, lo dovrebbe gia' fare nella visita dell'albero <=========
		body.setIsFunction(true);


		// keep the old effect
		Environment old_env = new Environment(env);
		List<List<Effect>> old_effects = new ArrayList<>();
		for (var status : innerFunEntry.getFunctionStatus()) {
			old_effects.add(new ArrayList<>(status));
		}

		// =====================================================================
		// 3. Single execution of the function's body
		errors.addAll(checkBodyAndUpdateArgs(env, innerFunEntry)); // env is updated after this call.

		// =====================================================================
		// 4. check if effects are changed after the body
		boolean different_funType = !innerFunEntry.getFunctionStatus().equals(old_effects);

		while (different_funType){
			//effect are changed!
			// replace the env and update status with the new effects

			env.replace(old_env);

			// lookUp should work??
			var funEntry = env.safeLookup(funId.getIdentifier());

			for (int argIndex = 0; argIndex < args.size(); argIndex++) {
				var argEntry = env.safeLookup(args.get(argIndex).getId().getIdentifier());
				var argStatuses = innerFunEntry.getFunctionStatus().get(argIndex);

				for (int derefLvl = 0; derefLvl < argEntry.getMaxDereferenceLevel(); derefLvl++) {
					funEntry.setParamStatus(argIndex, argStatuses.get(derefLvl), derefLvl);
				}
			}

			// they are the new starting effect
			old_effects = new ArrayList<>(innerFunEntry.getFunctionStatus());

			errors.addAll(checkBodyAndUpdateArgs(env, innerFunEntry));

			// repeat until the effects are not modified
			different_funType = !innerFunEntry.getFunctionStatus().equals(old_effects);
		}

		// =====================================================================
		// 5. popScope and update the original env with the computed effect after fixpoint
		env.popBlockScope();

		// Setting the computed statuses in the function arguments and saving them in the Symbol Table entry of the function funId.
		var idEntry = env.safeLookup(funId.getIdentifier());
		for (int argIndex = 0; argIndex < parlist.size(); argIndex++) {
			//Update ID in previous scope
			var argStatuses = innerFunEntry.getFunctionStatus().get(argIndex);

			for (int derefLvl = 0; derefLvl < argStatuses.size(); derefLvl++) {
				idEntry.setParamStatus(argIndex, argStatuses.get(derefLvl), derefLvl);
			}
		}

		return errors;
	}

	private ArrayList<SemanticError> checkBodyAndUpdateArgs(Environment env, STentry innerFunEntry) throws SimplanPlusException {
		ArrayList<SemanticError> errors = new ArrayList<>();

		errors.addAll(body.checkEffects(env));
		for(int argIndex = 0; argIndex < parlist.size(); argIndex++){
			var argEntry = env.lookUp(parlist.get(argIndex).getId());

			for (int derefLvl = 0; derefLvl < argEntry.getMaxDereferenceLevel(); derefLvl++) {
				funId.getSTEntry().setParamStatus(argIndex, argEntry.getVariableStatus(derefLvl), derefLvl);
				innerFunEntry.setParamStatus(argIndex, argEntry.getVariableStatus(derefLvl), derefLvl);
			}
		}
		return errors;
	};


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