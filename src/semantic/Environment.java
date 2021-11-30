package semantic;

import java.util.*;
import java.util.function.BiFunction;

import ast.Dereferenceable;
import ast.STentry;
import ast.node.types.ArrowTypeNode;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;

public class Environment {
	
	//THESE VARIABLES SHOULDN'T BE PUBLIC
	//THIS CAN BE DONE MUCH BETTER
	
	private ArrayList<HashMap<String,STentry>>  symTable = new ArrayList<HashMap<String,STentry>>();
	private int nestingLevel = -1;
	private int offset = 0;
	//livello ambiente con dichiarazioni piu' esterno � 0 (prima posizione ArrayList) invece che 1 (slides)
	//il "fronte" della lista di tabelle � symTable.get(nestingLevel)

	public Environment(int nestingLevel, int offset){
		this.symTable = new ArrayList<>();
		this.nestingLevel = nestingLevel;
		this.offset = offset;

	}
	public Environment(Environment e) {
		this(e.nestingLevel,e.offset);
		for (var scopeBlock : e.symTable) {
			final HashMap<String, STentry> copiedScope = new HashMap<>();
			for (var id : scopeBlock.keySet()) {
				STentry entry = scopeBlock.get(id);
				copiedScope.put(id, new STentry(entry));
			}
			this.symTable.add(copiedScope);
		}
	}

	public Environment() {
		this(-1,0);
	}

	public static Environment max(Environment firstEnv, Environment secondEnv) {
		var maxEnvironment = new Environment(firstEnv.nestingLevel, firstEnv.offset);
		for (int scopeIndex = 0, size = firstEnv.symTable.size(); scopeIndex < size; scopeIndex++) {
			var firstScope = firstEnv.symTable.get(scopeIndex);
			var secondScope = secondEnv.symTable.get(scopeIndex);

			final HashMap<String, STentry> maxScope = new HashMap<>();
			for (var varId : firstScope.keySet()) {
				var entryFirstEnv = firstScope.get(varId);
				var entrySecondEnv = secondScope.get(varId);
				if(entrySecondEnv == null){
					maxScope.put(varId,entryFirstEnv);
				}
				else{
					int nestingLevel = entryFirstEnv.getNestingLevel();
					TypeNode type = entryFirstEnv.getType();
					int offset = entryFirstEnv.getOffset();
					var entry = new STentry(nestingLevel,type,offset);
					entry.setFunctionNode(entryFirstEnv.getFunctionNode());
					var maxDeference = entry.getMaxDereferenceLevel();
					for (int deferenceLevel = 0; deferenceLevel < maxDeference; deferenceLevel++){
						var firstEffect = entryFirstEnv.getDereferenceLevelVariableStatus(deferenceLevel);
						var secondEffect = entrySecondEnv.getDereferenceLevelVariableStatus(deferenceLevel);
						//entry.setDereferenceLevelVariableStatus(Effect.maxEffect(firstEffect,secondEffect),deferenceLevel);
						entry.updatePointerStatusReference(Effect.maxEffectNoCopy(firstEffect,secondEffect),deferenceLevel);
					}
					maxScope.put(varId,entry);
				}
			}
			maxEnvironment.symTable.add(maxScope);
		}



		return maxEnvironment;
	}

	public static Environment parallelEnvironment(Environment firstEnv, Environment secondEnv) {
		Environment resultingEnvironment = new Environment();

		resultingEnvironment.createVoidScope();

		Map<String, STentry> scope1 = firstEnv.symTable.get(firstEnv.symTable.size() - 1);
		Map<String, STentry> scope2 = secondEnv.symTable.get(secondEnv.symTable.size() - 1);

		for (var xInE1 : scope1.entrySet()) {
			if (!scope2.containsKey(xInE1.getKey())) {

				STentry entry = resultingEnvironment.createNewDeclaration(xInE1.getKey(),
						xInE1.getValue().getType());


				entry.setFunctionNode(xInE1.getValue().getFunctionNode());
				for (int j = 0; j < xInE1.getValue().getMaxDereferenceLevel(); j++) {
					entry.setDereferenceLevelVariableStatus(xInE1.getValue().getDereferenceLevelVariableStatus(j), j);
				}
			}
		}

		for (var xInE2 : scope2.entrySet()) {
			if (!scope1.containsKey(xInE2.getKey())) {
				STentry entry = resultingEnvironment.createNewDeclaration(xInE2.getKey(),
						xInE2.getValue().getType());
				entry.setFunctionNode(xInE2.getValue().getFunctionNode());
				for (int j = 0; j < xInE2.getValue().getMaxDereferenceLevel(); j++) {
					entry.setDereferenceLevelVariableStatus(xInE2.getValue().getDereferenceLevelVariableStatus(j), j);
				}
			}
		}

		for (var xInE1 : scope1.entrySet()) {
			for (var xInE2 : scope2.entrySet()) {
				if (xInE1.getKey().equals(xInE2.getKey())) {
					STentry entry = resultingEnvironment.createNewDeclaration(xInE1.getKey(),
							xInE1.getValue().getType());
					entry.setFunctionNode(xInE1.getValue().getFunctionNode());
					for (int j = 0; j < xInE2.getValue().getMaxDereferenceLevel(); j++) {
						entry.setDereferenceLevelVariableStatus(Effect.parallelEffect(xInE1.getValue().getDereferenceLevelVariableStatus(j), xInE2.getValue().getDereferenceLevelVariableStatus(j)), j);
					}
				}
			}
		}

		return resultingEnvironment;
	}

	public static Environment updateEnvironment(Environment env1, Environment env2) {
		Environment returnedEnvironment;

		if (env2.symTable.size() == 0 || env1.symTable.size() == 0) {
			return new Environment(env1);
		}

		HashMap<String, STentry> headScope1 = env1.symTable.get(env1.symTable.size() - 1);
		HashMap<String, STentry> headScope2 = env2.symTable.get(env2.symTable.size() - 1);

		if (headScope2.keySet().isEmpty()) {
			// \sigma' = \emptySet
			return new Environment(env1);
		}

		var u = headScope2.entrySet().stream().findFirst().get();
		env2.removeFirstIdentifier(u.getKey());
		//primo caso
		if (headScope1.containsKey(u.getKey())) {
			headScope1.put(u.getKey(), u.getValue());

			returnedEnvironment = updateEnvironment(env1, env2);
		} else {
			//secondo caso
			Environment envWithOnlyU = new Environment();
			envWithOnlyU.createVoidScope();
			STentry tmpEntry = envWithOnlyU.createNewDeclaration(u.getKey(), u.getValue().getType());
			tmpEntry.setFunctionNode(u.getValue().getFunctionNode());
			for (int j = 0; j < u.getValue().getMaxDereferenceLevel(); j++) {
				tmpEntry.setDereferenceLevelVariableStatus(u.getValue().getDereferenceLevelVariableStatus(j), j);
			}

			env1.popBlockScope();
			Environment tmpEnv = updateEnvironment(env1, envWithOnlyU);
			tmpEnv.createScope(headScope1);

			returnedEnvironment = updateEnvironment(tmpEnv, env2);
		}

		return returnedEnvironment;
	}

	private void removeFirstIdentifier(String id) {
		for (int i = symTable.size() - 1; i >= 0; i--) {
			if (symTable.get(i).containsKey(id)) {
				symTable.get(i).remove(id);
				return;
			}
		}
	}

	public ArrayList<EffectError> getEffectErrors() {
		ArrayList<EffectError> errors = new ArrayList<>();
		for (var scope : symTable) {
			for (var entry : scope.entrySet()) {
				for (int i = 0; i < entry.getValue().getMaxDereferenceLevel(); i++) {
					if (entry.getValue().getDereferenceLevelVariableStatus(i).equals(Effect.ERROR)) {
						errors.add(new EffectError("The pointer " + entry.getKey() + "^".repeat(i) + " is used after deletion."));
					}
				}
			}
		}

		return errors;
	}

	/**
	 * Getter
	 */
	public int getOffset(){
		return this.offset;
	}
	public int getNestingLevel() {
		return nestingLevel;
	}

	public HashMap<String, STentry> getCurrentST() {
		return this.symTable.get(this.nestingLevel);
	}
	public HashMap<String, STentry> getPrevCurrentST() {
		return this.symTable.get(this.nestingLevel-1);
	}
	/**
	 * End of Getter
	 */


	/**
	 * Setter
	 */
	public int decOffset(){
		return this.offset--;
	}

	public void functionOffset(){
		this.offset = -2;
	}
	public void blockOffset(){
		this.offset = -1;
	}


	public int setNestingLevel(int nestingLevel) {
		this.nestingLevel = nestingLevel;
		return nestingLevel;
	}
	/**
	 * End of Setter
	 */




	public STentry lookUp(final String id) {
		for (int i = nestingLevel; i >= 0; i--) {
			HashMap<String, STentry> scope = symTable.get(i);
			STentry stEntry = scope.get(id);
			if (stEntry != null) {
				return stEntry;
			}
		}
		//throw new SimplanPlusException("ID " + id + " is not in the ST.");
		//System.err.println("ID " + id + " is not in the ST.");
		return null;
	}






	/**
	 * Push and pop scope function
	 *
	 */
	public void createVoidScope(){
		this.nestingLevel++;
		offset = 0;
		this.symTable.add(new HashMap<String, STentry>());
	}

	public void createScope(HashMap<String, STentry> newScope){
		this.nestingLevel++;
		this.symTable.add(newScope);
	}


	public void popBlockScope(){
		this.symTable.remove(this.nestingLevel--);

	}

	public void popFunScope(){
		this.symTable.remove(this.nestingLevel--);
		if(this.nestingLevel >= 0){
			Optional<STentry> stEntry = this.symTable.get(this.nestingLevel).values().stream().min(Comparator.comparing(STentry::getOffset));
			this.offset = stEntry.map(STentry::getOffset).orElse(-1);
		}
	}

	/**
	 * End push and pop scope function
	 */



	/**
	 * New parameter/declaration function
	 */


	public STentry newFunctionParameter(final String varId, final TypeNode varType, final int offset){
		HashMap<String, STentry> ST = this.symTable.get(this.nestingLevel);
		STentry newEntry = new STentry(this.nestingLevel,varType,offset);
		STentry oldEntry = ST.put(varId,newEntry);
		return oldEntry;
	}
	public STentry createFunDec(String bFL, String eFL,TypeNode type){
		STentry entry = new STentry(this.nestingLevel,this.offset--,bFL,eFL,type);
		return entry;
	}

	public void addEntry(String id, STentry entry){
		HashMap<String, STentry> ST = this.symTable.get(nestingLevel);
		ST.put(id,entry);
	}
	public void removeEntry(String id){
		HashMap<String, STentry> ST = this.symTable.get(nestingLevel);
		ST.remove(id);
	}

	/**
	 * End new parameter/declaration function
	 */


	/**
	 *
	 * Check Stm status
	 * @param {LhsNode} variable is the variable that will receive the new effect
	 * @param {BiFunction} function from effect that has to be invoke
	 * @param {int} effect to be applayed
	 */
	public ArrayList<EffectError> checkStmStatus(
		Dereferenceable variable,
		BiFunction<Effect,Effect,Effect> effectFun,
		Effect effect
	) {

		ArrayList<EffectError> errors = new ArrayList<>();

		try {
			STentry idEntry = lookUp(variable.getID());

			Effect oldEffect = idEntry.getDereferenceLevelVariableStatus(variable.getDereferenceLevel());
			Effect newEffect = new Effect(effect);

			Effect newStatus = effectFun.apply(oldEffect,newEffect);
			idEntry.setDereferenceLevelVariableStatus(newStatus, variable.getDereferenceLevel());

			if (newStatus.equals(new Effect(Effect.ERROR))) {
				errors.add(new EffectError(variable.getID() + " used after delete(env)."));
			}
		} catch (Exception exception) {
			errors.add(new EffectError(variable.getID() + " not declared. Aborting."));
		}
		return errors;
	}

	public void replaceWithNewEnv(Environment newEnvironment) {
		this.symTable.clear();
		this.nestingLevel = newEnvironment.getNestingLevel();
		this.offset = newEnvironment.getOffset();

		for (var scope : newEnvironment.symTable) {
			final HashMap<String, STentry> copiedScope = new HashMap<>();
			for (var id : scope.keySet()) {
				//TODO chiedere laneve se a sto punto diamo errore solo a run time perchè
				// nel caso di assegnare due puntatori diversi nell'if la variabile rimane
				// referenziata al secondo
				copiedScope.put(id, scope.get(id));
				//copiedScope.put(id, new STentry(scope.get(id)));
			}
			this.symTable.add(copiedScope);
		}
	}

	public STentry effectsLookUp(String id) {
		for (int i = nestingLevel; i >= 0; i--) {
			var ithScope = symTable.get(i);
			var stEntry = ithScope.get(id);
			if (stEntry != null) {
				return stEntry;
			}
		}

		System.err.println("Unexpected absence of ID " + id + " in the Symbol Table.");

		return null; // Does not happen if preconditions are met.
	}

	public STentry createNewDeclaration(final String id, final TypeNode type) {
		STentry stEntry;
		if (type instanceof ArrowTypeNode) {
			//TODO This offset doesn't change anything?
			stEntry = new STentry(nestingLevel, type, 0);
		} else {
			stEntry = new STentry(nestingLevel, type, --offset);
		}
		STentry declaration = getCurrentST().put(id, stEntry);
		if (declaration != null) {
			System.err.println("Unexpected multiple assignment for ID: " + id + ". It was previously defined of type: "
					+ declaration.getType() + ".");
		}
		declaration = getCurrentST().get(id);
		return declaration;

	}

	@Override
	public String toString() {

		StringBuilder st = new StringBuilder();
		String hs="";
		for (HashMap<String, STentry> hm: symTable){
			for (String k: hm.keySet()){
				hs = "\t" + k + "->" + hm.get(k).toString() + "\n";
			}
			st.append(hs).append('\n');
		}

		return "Environment{" +
				"\n\tsymTable=\n\t\t" + symTable +
				"\tnestingLevel=" + nestingLevel +
				", \n\toffset=" + offset +
				"\n\t}";
	}

	/**
	 *
	 * End Stm status
	 *
	 */


}
