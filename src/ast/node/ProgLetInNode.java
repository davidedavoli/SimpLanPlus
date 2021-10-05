package ast.node;
import java.util.ArrayList;
import java.util.HashMap;

import ast.STentry;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semanticAnalysis.Label;
import semanticAnalysis.FuncBodyUtils;
import semanticAnalysis.Environment;
import semanticAnalysis.SemanticError;

public class ProgLetInNode implements Node {

  private ArrayList<Node> declist;
  private Node exp;
  
  public ProgLetInNode (ArrayList<Node> d, Node e) {
    declist=d;
    exp=e;
  }
  
  public String toPrint(String s) {
	String declstr="";
    for (Node dec:declist)
      declstr += dec.toPrint(s+"  ");
	return s+"ProgLetIn\n" + declstr + exp.toPrint(s+"  ") ; 
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  env.nestingLevel++;
      HashMap<String, STentry> hm = new HashMap<String,STentry> ();
      env.symTable.add(hm);
      
      //declare resulting list
      ArrayList<SemanticError> res = new ArrayList<SemanticError>();
      
      //check semantics in the dec list
      if(declist.size() > 0){
    	  env.offset = -2;
    	  //if there are children then check semantics for every child and save the results
    	  for(Node n : declist)
    		  res.addAll(n.checkSemantics(env));
      }
      
      //check semantics in the exp body
      res.addAll(exp.checkSemantics(env));
      
      //clean the scope, we are leaving a let scope
      env.symTable.remove(env.nestingLevel--);
      
      //return the result
      return res;
	}
  
  public TypeNode typeCheck () {
    for (Node dec:declist)
      dec.typeCheck();
    return exp.typeCheck();
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) {
	  String declCode="";
	  for (Node dec:declist)
		    declCode+=dec.codeGeneration(labelManager);
	  return  "push 0\n"+
			  declCode+
			  exp.codeGeneration(labelManager)+"halt\n"+
			  FuncBodyUtils.getCode();
  } 
  
  
    
}  