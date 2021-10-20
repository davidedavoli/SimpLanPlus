package semantic;

import java.util.*;

import ast.STentry;
import ast.node.types.TypeNode;

public class Environment {
	
	//THESE VARIABLES SHOULDN'T BE PUBLIC
	//THIS CAN BE DONE MUCH BETTER
	
	private ArrayList<HashMap<String,STentry>>  symTable = new ArrayList<HashMap<String,STentry>>();
	private int nestingLevel = -1;
	private int offset = 0;
	//livello ambiente con dichiarazioni piu' esterno � 0 (prima posizione ArrayList) invece che 1 (slides)
	//il "fronte" della lista di tabelle � symTable.get(nestingLevel)

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
		System.err.println("ID " + id + " is not in the ST.");

		return null;
	}




	/**
	 * Push and pop scope function
	 *
	 */
	public void createVoidScope(){
		this.nestingLevel++;
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
			int offset = stEntry.map(STentry::getOffset).orElse(-1);
			//System.out.println("NEW OFFSET HAS TO BE " + offset);
			this.offset = offset ;
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
	public STentry createFunDec(String bFL, String eFL){
		STentry entry = new STentry(this.nestingLevel,this.offset--,bFL,eFL);
		return entry;
	}

	public void addEntry(HashMap<String, STentry> entry){
		this.symTable.add(entry);
	}

	/**
	 * End new parameter/declaration function
	 */

}
