package semantic;

import java.util.*;
import java.util.function.BiFunction;

import ast.Dereferences;
import ast.STentry;
import ast.node.types.ArrowTypeNode;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;

public class Environment {

	private final ArrayList<HashMap<String,STentry>>  symTable;
	private int nestingLevel;
	private int offset;

/**
 * =====================================================
 * CONSTRUCTOR
 * =====================================================
 */

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

/**
 * =====================================================
 * GETTER
 * =====================================================
 */

	public int getOffset(){
		return this.offset;
	}
	public int getNestingLevel() {	return this.nestingLevel; }
	public HashMap<String, STentry> getCurrentST() {
		return this.symTable.get(this.nestingLevel);
	}

/**
 * =====================================================
 * SETTER
 * =====================================================
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

/**
 * =====================================================
 * Semantic
 * =====================================================
 */

	public STentry createFunDec(String bFL, String eFL,TypeNode type){
		return new STentry(this.nestingLevel, this.offset--,bFL,eFL,type);
	}
	public void popFunScope(){
		this.symTable.remove(this.nestingLevel--);
		if(this.nestingLevel >= 0){
			Optional<STentry> stEntry = this.symTable.get(this.nestingLevel).values().stream().min(Comparator.comparing(STentry::getOffset));
			this.offset = stEntry.map(STentry::getOffset).orElse(-1);
		}
	}


	/**
	 * Searches [id] in the Symbol Table and returns its entry, if present.
	 *
	 * @param id the identifier of the variable or function.
	 * @return the entry in the symbol table of the variable or function with that
	 * identifier.
	 */
	public STentry lookUp(final String id) {
		for (int i = nestingLevel; i >= 0; i--) {
			HashMap<String, STentry> scope = symTable.get(i);
			STentry stEntry = scope.get(id);
			if (stEntry != null) {
				return stEntry;
			}
		}
		return null;
	}

/**
 * =====================================================
 * End Semantic
 * =====================================================
 */

/**
 * =====================================================
 * Effect
 * =====================================================
 */


	public void addEntry(String id, STentry entry){
		HashMap<String, STentry> ST = this.symTable.get(nestingLevel);
		ST.put(id,entry);
	}

	/**
	 * Searches [id] in the Symbol Table and returns its entry. It must be present, otherwise
	 * Error "absence of ID" is raised.
	 *
	 * @param id the identifier of the variable or function.
	 * @return the entry in the symbol table of the variable or function with that
	 * identifier.
	 */
	public STentry effectsLookUp(String id) {
		for (int i = nestingLevel; i >= 0; i--) {
			var ithScope = symTable.get(i);
			var stEntry = ithScope.get(id);
			if (stEntry != null) {
				return stEntry;
			}
		}
		System.err.println("Unexpected absence of ID " + id + " in the Symbol Table.");
		return null; // Should not happen in effects
	}

	/**
	 * Checks the status of the variable and updates it with the given rule {@code effectFun}
	 * If the new status is Effect::ERROR then returns a SemanticError:"ID used after deleting".
	 * If the variable is not declared an Exception is raised:"ID is not declared".
	 *
	 * @param variable {LhsNode} variable is the variable that will receive the new effect
	 * @param effectFun {BiFunction} function from effect that has to be invoked
	 * @param effect {Effect} effect to be applied
	 */
	public ArrayList<EffectError> checkStmStatus(
			Dereferences variable,
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
				errors.add(new EffectError(variable.getID() + " used after deleting."));
			}
		} catch (Exception exception) {
			errors.add(new EffectError("Id "+variable.getID() + " not declared."));
		}
		return errors;
	}



	/**
	 * =====================================================
	 * Effect operation on Environment
	 * =====================================================
	 **/
		/**
		 * Returns a new environment which has, for each identifier, the maximum
		 * effect set in the two environments. Assumes dom(env2) is a subset of
		 * dom(env1).
		 *
		 * @param firstEnv first environment
		 * @param secondEnv second environment
		 * @return {environment} the maximum environment of the two
		 */
		public static Environment maxEnvironment(Environment firstEnv, Environment secondEnv) {
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
						boolean isPar = entryFirstEnv.getisPar();
						var entry = new STentry(nestingLevel,type,offset, isPar);
						entry.setFunctionNode(entryFirstEnv.getFunctionNode());
						var maxDeference = entry.getMaxDereferenceLevel();
						for (int deferenceLevel = 0; deferenceLevel < maxDeference; deferenceLevel++){
							var firstEffect = entryFirstEnv.getDereferenceLevelVariableStatus(deferenceLevel);
							var secondEffect = entrySecondEnv.getDereferenceLevelVariableStatus(deferenceLevel);
							entry.setDereferenceLevelVariableStatus(Effect.maxEffect(firstEffect,secondEffect),deferenceLevel);
						}
						maxScope.put(varId,entry);
					}
				}
				maxEnvironment.symTable.add(maxScope);
			}

			return maxEnvironment;
		}

		/**
		 * Returns the par environment applied to the head of {@code firstEnv} and
		 * {@code secondEnv}.
		 *
		 * @param firstEnv environment which has at least one scope
		 * @param secondEnv environment which has at least one scope
		 * @return the par environment
		 */
		public static Environment parallelEnvironment(Environment firstEnv, Environment secondEnv) {
			Environment resultingEnvironment = new Environment();
			resultingEnvironment.createVoidScope();

			Map<String, STentry> scope1 = firstEnv.symTable.get(firstEnv.symTable.size() - 1);
			Map<String, STentry> scope2 = secondEnv.symTable.get(secondEnv.symTable.size() - 1);

			// CASE 1: Exists x in scope1 that not belongs to scope2
			checkIfEntryIsInUniqueScope(scope1, scope2, resultingEnvironment);
			// CASE 2: Exists x in scope2 that non belongs to scope1
			checkIfEntryIsInUniqueScope(scope2, scope1, resultingEnvironment);
			// CASE 3: The scopes contains the same variables, parallel operation occurs
			parallelOperationOnEnvironment(scope1, scope2, resultingEnvironment);

			return resultingEnvironment;
		}
		/**
		 * Given two Map of <String, STEntry> {@code scope1} and {@code scope2} which contains the same variables,
		 * execute Parallel effect operation between {@code scope1} and {@code scope2} and store the result in
		 * {@code resultingEnvironment}
		 *
		 * @param scope1 environment which has at least one scope
		 * @param scope2 environment which has at least one scope
		 * @param resultingEnvironment environment resulting after the parallel effect operation
		 */
		private static void parallelOperationOnEnvironment(Map<String, STentry> scope1, Map<String, STentry> scope2, Environment resultingEnvironment){
			for (var variableIdScope1 : scope1.entrySet()) {
				for (var variableIdScope2 : scope2.entrySet()) {

					if (variableIdScope1.getKey().equals(variableIdScope2.getKey())) {
						STentry entry = resultingEnvironment.createNewDeclaration(
								variableIdScope1.getKey(),
								variableIdScope1.getValue().getType(),
								variableIdScope1.getValue().getisPar()
						);
						entry.setFunctionNode(variableIdScope1.getValue().getFunctionNode());

						for (int j = 0; j < variableIdScope2.getValue().getMaxDereferenceLevel(); j++) {
							entry.setDereferenceLevelVariableStatus(
									Effect.parallelEffect(
											variableIdScope1.getValue().getDereferenceLevelVariableStatus(j),
											variableIdScope2.getValue().getDereferenceLevelVariableStatus(j)
									),
									j);
						}
					}
				}
			}
		}

		/**
		 * Creates a new environment updating {@code env1} with the effects applied in
		 * {@code env2}.Consider invoking this function with clones of the environments
		 * since this function performs side effects on the arguments.
		 *
		 * @param env1 environment to update (multi-scope)
		 * @param env2 environment with updates (single-scope)
		 * @return the updated environment
		 */
		public static Environment updateEnvironment(Environment env1, Environment env2) {
			Environment returnedEnvironment;

			if(env2.symTable.size() == 0){
				return new Environment(env1);
			}
			else if(env1.symTable.size() == 0){
				return new Environment(env2);
			}
			HashMap<String, STentry> topScopeFirstEnvironment = env1.symTable.get(env1.symTable.size() - 1);
			HashMap<String, STentry> topScopeSecondEnvironment = env2.symTable.get(env2.symTable.size() - 1);

			// CASE 3: \sigma' = \emptySet
			if (topScopeSecondEnvironment.keySet().isEmpty()) {
				return new Environment(env1);
			}

			var nameIdSecondEnvironment = topScopeSecondEnvironment.entrySet().stream().findFirst().get().getKey();
			var valueIdSecondEnvironment = topScopeSecondEnvironment.entrySet().stream().findFirst().get().getValue();
			var typeIdSecondEnvironment = valueIdSecondEnvironment.getType();
			var parIdSecondEnvironment = valueIdSecondEnvironment.getisPar();
			env2.removeFirstIdentifier(nameIdSecondEnvironment);

			// CASE 1
			if (topScopeFirstEnvironment.containsKey(nameIdSecondEnvironment)) {
				topScopeFirstEnvironment.put(nameIdSecondEnvironment, valueIdSecondEnvironment);
				returnedEnvironment = updateEnvironment(env1, env2);
			}
			else {
				// CASE 2
				Environment envWithOnlyU = new Environment();
				envWithOnlyU.createVoidScope();

				STentry tmpEntry = envWithOnlyU.createNewDeclaration(nameIdSecondEnvironment, typeIdSecondEnvironment, parIdSecondEnvironment);
				tmpEntry.setFunctionNode(valueIdSecondEnvironment.getFunctionNode());
				int maxDereferenceIdSecondEnvironment = valueIdSecondEnvironment.getMaxDereferenceLevel();

				for (int dereferenceLevel = 0; dereferenceLevel < maxDereferenceIdSecondEnvironment; dereferenceLevel++) {
					tmpEntry.setDereferenceLevelVariableStatus(valueIdSecondEnvironment.getDereferenceLevelVariableStatus(dereferenceLevel), dereferenceLevel);
				}

				env1.popBlockScope();
				Environment environmentUpdated = updateEnvironment(env1, envWithOnlyU);
				environmentUpdated.createScope(topScopeFirstEnvironment);

				returnedEnvironment = updateEnvironment(environmentUpdated, env2);
			}
			return returnedEnvironment;
		}
	/**
	 * =====================================================
	 * End Effect operation on Environment
	 * =====================================================
	 **/

	/**
	 * =====================================================
	 * Effect Utils
	 * =====================================================
	 **/
		/**
		 * Given two Map of <String, STEntry> {@code scope1} and {@code scope2}, check if there are some variables is {@code scope1},
		 * that not belongs to {@code scope2}. If it is true, the variables are added to {@code resultingEnvironment}
		 *
		 * @param scope1 environment which has at least one scope
		 * @param scope2 environment which has at least one scope
		 * @param resultingEnvironment environment resulting after the check
		 */
		private static void checkIfEntryIsInUniqueScope(Map<String, STentry> scope1, Map<String, STentry> scope2, Environment resultingEnvironment) {
			for (var variableIdScope1 : scope1.entrySet()) {

				if (!scope2.containsKey(variableIdScope1.getKey())) {
					STentry entry = resultingEnvironment.createNewDeclaration(
							variableIdScope1.getKey(),
							variableIdScope1.getValue().getType(),
							variableIdScope1.getValue().getisPar()
					);
					entry.setFunctionNode(variableIdScope1.getValue().getFunctionNode());

					int variableMaxDereferenceLevel = variableIdScope1.getValue().getMaxDereferenceLevel();
					for (int variableDereferenceLevel = 0; variableDereferenceLevel < variableMaxDereferenceLevel; variableDereferenceLevel++) {
						entry.setDereferenceLevelVariableStatus(variableIdScope1.getValue().getDereferenceLevelVariableStatus(variableDereferenceLevel), variableDereferenceLevel);
					}
				}
			}
		}

		/**
		 * Given an {@code id} remove the first occurrence from symbolTable if present.
		 * @param id to be removed
		 */
		private void removeFirstIdentifier(String id) {
			for (int i = symTable.size() - 1; i >= 0; i--) {
				if (symTable.get(i).containsKey(id)) {
					symTable.get(i).remove(id);
					return;
				}
			}
		}

		public void replaceWithNewEnvironment(Environment newEnvironment) {
			this.symTable.clear();
			this.nestingLevel = newEnvironment.getNestingLevel();
			this.offset = newEnvironment.getOffset();

			for (var scope : newEnvironment.symTable) {
				final HashMap<String, STentry> copiedScope = new HashMap<>();
				for (var id : scope.keySet()) {
					//copiedScope.put(id, scope.get(id));
					copiedScope.put(id, new STentry(scope.get(id)));
				}
				this.symTable.add(copiedScope);
			}
		}

		/**
		 * Adds a new variable named [id] of type [type] into the current scope. The
		 * caller is sure that [id] does not exist in the current scope: if it does,
		 * then unexpected behavior could occur.
		 *
		 * @param id   the identifier of the variable or function.
		 * @param type the type of the variable or function.
		 */
		public STentry createNewDeclaration(final String id, final TypeNode type, boolean isPar) {
			STentry stEntry;
			if (type instanceof ArrowTypeNode) {
				stEntry = new STentry(nestingLevel, type, 0, isPar);
			} else {
				if(offset == 0)
					offset = -1;
				stEntry = new STentry(nestingLevel, type, --offset, isPar);
			}
			STentry declaration = getCurrentST().put(id, stEntry);
			if (declaration != null) {
				System.err.println("Unexpected multiple assignment for ID: " + id + ". It was previously defined of type: "
						+ declaration.getType() + ".");
			}
			declaration = getCurrentST().get(id);
			return declaration;

		}

		public ArrayList<EffectError> getEffectErrors() {
			ArrayList<EffectError> errors = new ArrayList<>();
			for (var scope : symTable) {
				for (var entry : scope.entrySet()) {
					int actualMaxDereference = entry.getValue().getMaxDereferenceLevel();
					for (int dereferenceLevel = 0; dereferenceLevel < actualMaxDereference; dereferenceLevel++) {
						if (entry.getValue().getDereferenceLevelVariableStatus(dereferenceLevel).equals(Effect.ERROR)) {
							errors.add(new EffectError(entry.getKey() + " used after deleting."));
						}
					}
				}
			}
			return errors;
		}
	/**
	 * =====================================================
	 * End Effect Utils
	 * =====================================================
	 **/

/**
 * =====================================================
 * End Effect
 * =====================================================
 */



/**
 * =====================================================
 * Push & Pop Scope
 * =====================================================
 **/

	/**
	 * Pushes new scope in the Symbol Table stack. Increments the nesting level.
	 * Sets the offset to 0.
	 */
	public void createVoidScope(){
		this.nestingLevel++;
		offset = 0;
		this.symTable.add(new HashMap<>());
	}

	/**
	 * Adds scope passed to the environment.
	 *
	 * @param newScope the scope to add to the Symbol Table stack
	 */
	public void createScope(HashMap<String, STentry> newScope){
		this.nestingLevel++;
		this.symTable.add(newScope);
	}

	public void popBlockScope(){
		this.symTable.remove(this.nestingLevel--);

	}

/**
 * =====================================================
 * End Push & Pop Scope
 * =====================================================
 **/

/**
 * =====================================================
 * New parameter declaration function
 * =====================================================
 **/

	public STentry newFunctionParameter(final String varId, final TypeNode varType, final int offset){
		HashMap<String, STentry> ST = this.symTable.get(this.nestingLevel);
		STentry newEntry = new STentry(this.nestingLevel,varType,offset,true);
		return ST.put(varId,newEntry);
	}

/**
 * =====================================================
 * End New parameter declaration function
 * =====================================================
 **/


	@Override
	public String toString() {

		return "Environment{" +
				"\n\tsymTable=\n\t\t" + symTable +
				"\tnestingLevel=" + nestingLevel +
				", \n\toffset=" + offset +
				"\n\t}";
	}
}
