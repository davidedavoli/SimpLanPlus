//package ast;
//
//import java.util.ArrayList;
//
//import util.Environment;
//import util.SemanticError;
//
//public class NewNode implements Node {
//	
//	  private TypeNode type;
//	  private String id;
//	  
//	  public NewNode (TypeNode t) {
//		    type=t;
//		  }
//	  public void setID(String i) {
//		  id = i;
//	  }
//	  
//	  @Override
//	  public ArrayList<SemanticError> checkSemantics(Environment env) {
//		  //create result list
//		  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
//		  
//		  if (id != "") {
//			  System.out.println(id);
//			  int j=env.nestingLevel;
//			  STentry tmp=null; //È impossibile che non si trovi id: se così fosse allora, controllando la semantica della lhs dell'assegnamento, l'errore sarebbe già sollevato
//			  while (j>=0 && tmp==null)
//				  tmp=(env.symTable.get(j--)).get(id);
//			  type=tmp.getType();
//		  }
//		  
//	  	  if (type == null)
//	  		  res.add(new SemanticError("new operator in compound expression"));
//	      return res;
//	  }
//	  
//	  public String toPrint(String s) {
//		return s+"New:" + type +"\n";
//	  }
//	  
//	  //valore di ritorno non utilizzato
//	  public TypeNode typeCheck () {
//	  	  if (!(type instanceof PointerTypeNode)) {
//	  		  System.out.println("Attempted allocation of non-pointer type");
//	  		  System.exit(0);
//	  	  }
//	  		  
//	    return type;
//	  }
//	  
//	  public RetEffType retTypeCheck() {
//		  return new RetEffType(RetEffType.RetT.ABS);
//	  }
//	  
//	  public String codeGeneration() {
//			return "";//TODO
//	  }  
//
//         
//}  


package ast;

import java.util.ArrayList;

import types.PointerTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class NewNode implements Node {
	
	  private TypeNode type;
	  
	  public NewNode (TypeNode t) {
		    type=t;
		  }
	  
	  @Override
	  public ArrayList<SemanticError> checkSemantics(Environment env) {
		  //create result list
		  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		  
	  	  if (type == null)
	  		  res.add(new SemanticError("new operator in compound expression"));
	      return res;
	  }
	  
	  public String toPrint(String s) {
		return s+"New:\n" + type.toPrint(s+"   ") +"\n";
	  }
	  
	  //valore di ritorno non utilizzato
	  public TypeNode typeCheck () {
	    return new PointerTypeNode(type);
	  }
	  
	  public RetEffType retTypeCheck() {
		  return new RetEffType(RetEffType.RetT.ABS);
	  }
	  
	  public String codeGeneration() {
			return "";//TODO
	  }  
}  