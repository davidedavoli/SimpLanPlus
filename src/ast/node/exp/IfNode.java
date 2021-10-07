package ast.node.exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.types.BoolTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import util.Environment;
import util.Label;
import util.SemanticError;
import util.FuncBodyUtils;

public class IfNode implements Node {

  private Node cond;
  private Node th;
  private Node el;
  
  public IfNode (Node c, Node t, Node e) {
    cond=c;
    th=t;
    el=e;
  }
  
  public String toPrint(String s) {
    return s+"If\n" + cond.toPrint(s+"  ") 
                    + th.toPrint(s+"  ")   
                    + el.toPrint(s+"  "); 
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
	  //create the result
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  //check semantics in the condition
	  res.addAll(cond.checkSemantics(env));
	 	  
	  //check semantics in the then and in the else exp
	  res.addAll(th.checkSemantics(env));
	  if (el!=null)
		  res.addAll(el.checkSemantics(env));
	  
	  return res;
  }
  
  public RetEffType retTypeCheck() {
	  if (el!=null)
		  return RetEffType.min(th.retTypeCheck(), el.retTypeCheck());
	  else
		  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public TypeNode typeCheck() {
    if (!(TypeUtils.isSubtype(cond.typeCheck(),new BoolTypeNode()))) {
      System.out.println("non boolean condition in if");
      System.exit(0);
    }
    TypeNode t = th.typeCheck();
    TypeNode e = el.typeCheck();
    
    if (TypeUtils.isSubtype(t,e))
      return e;
    if (TypeUtils.isSubtype(e,t))
      return t;
//	non più necessari dal momento che l'if-then-esle non è più un'espressione
    
//    System.out.println("Incompatible types in then else branches");
//    System.exit(0);
    return null;
  }
  
  public String codeGeneration(Label labelManager) {

      StringBuilder cgen = new StringBuilder();
      String then_branch = labelManager.freshLabel("then");
      String end_label = labelManager.freshLabel("end_if");
      /**
       * Cgen condizione
       */
      String loaded_cond = cond.codeGeneration(labelManager);
      cgen.append(loaded_cond);
      cgen.append("bc $a0 ").append(then_branch).append("\n");

      /**
       * Cgen else
       */
      if(el != null){
          String loaded_el = el.codeGeneration(labelManager);
          cgen.append(loaded_el);
      }
      cgen.append("b ").append(end_label);


      /**
       * Cgen then
       */
      cgen.append(then_branch).append(":\n");
      String loaded_th = th.codeGeneration(labelManager);
      cgen.append(loaded_th).append("\n");

      /**
       * Append end_if_label_count
       */
      cgen.append(end_label);


      return cgen.toString();
  }
  
}  