package ast.node.statements;

import java.util.ArrayList;

//import org.stringtemplate.v4.compiler.STParser.notConditional_return;

import ast.STentry;
import ast.node.Node;
import ast.node.types.PointerTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class DeletionNode implements Node {

  private String id;
  private TypeNode type;
  
  public DeletionNode (String i) {
    id=i;
    type=null;
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
	  //create result list
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  	  int j = 0;
  		//env.offset = -2;
  	  STentry tmp=null;
  	  while (j>=0 && tmp==null)
  		  tmp=(env.symTable.get(j--)).get(id);
  	  
  	  if (tmp==null)
  		  res.add(new SemanticError("Id "+id+" not declared"));
  	  
  	  type = tmp.getType();
        
      return res;
  }
  
  public String toPrint(String s) {
	return s+"Delete:" + id +"\n";
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () {
	  if (! (type instanceof PointerTypeNode)){      
      System.out.println("attempted deletion of a non-pointer variable");
      System.exit(0);
    }     
    return null;
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) {
		return "";//TODO
  }  
    
}  