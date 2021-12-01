package ast.node.statements;

import java.util.ArrayList;

import ast.node.MetaNode;
import ast.node.Node;
import ast.node.exp.ExpNode;
import ast.node.types.BoolTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class IfNode extends MetaNode {

  private final ExpNode cond;
  private final Node th;
  private final Node el;
  
  public IfNode (ExpNode c, Node t, Node e) {
    cond=c;
    th=t;
    el=e;

  }
  
  public String toPrint(String s) {
      String print = s+"If\n" + cond.toPrint(s+"  ")
              + th.toPrint(s+"  ");
      if (el != null)
          print = print + el.toPrint(s+"  ");
      return print;

  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
	  //create the result
	  ArrayList<SemanticError> res = new ArrayList<>();
	  
	  //check semantics in the condition
	  res.addAll(cond.checkSemantics(env));
	 	  
	  //check semantics in the then and in the else exp
	  res.addAll(th.checkSemantics(env));
	  if (el!=null)
		  res.addAll(el.checkSemantics(env));
	  
	  return res;
  }
  
  public HasReturn retTypeCheck() {
      HasReturn th_v=th.retTypeCheck();
      HasReturn el_v=(el!=null)?el.retTypeCheck():new HasReturn(HasReturn.hasReturnType.ABS);
	  if (el!=null)
		  return HasReturn.min(th_v, el_v);
	  else
		  return new HasReturn(th_v.getVal());
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {

        ArrayList<EffectError> errors = new ArrayList<>(cond.checkEffects(env));

        if (el != null) {
            var thenEnv = new Environment(env);
            errors.addAll(th.checkEffects(thenEnv));

            var elseEnv = new Environment(env);
            errors.addAll(el.checkEffects(elseEnv));

            env.replaceWithNewEnv(Environment.max(thenEnv, elseEnv));
        } else {
            errors.addAll(th.checkEffects(env));
        }

        return errors;
    }

    public TypeNode typeCheck() {
    if ( !(TypeUtils.isSubtype(cond.typeCheck(),new BoolTypeNode()))) {
        System.err.println("Non boolean condition in if: "+cond.toPrint(""));
        System.exit(0);
    }
    TypeNode t = th.typeCheck();
    if(el == null){
        return t;
    }
    else{
        TypeNode e = el.typeCheck();

        if (TypeUtils.isSubtype(t,e))
            return e;
        if (TypeUtils.isSubtype(e,t))
            return t;
    }
    return null;
  }
  
  public String codeGeneration(Label labelManager) {

      StringBuilder codeGenerated = new StringBuilder();
      String then_branch = labelManager.freshLabel("then");
      String end_label = labelManager.freshLabel("endIf");
      /**
       * Code generation condition
       */
      String loaded_cond = cond.codeGeneration(labelManager);
      codeGenerated.append(loaded_cond).append("\n");
      codeGenerated.append("bc $a0 ").append(then_branch).append("\n");

      /**
       * Code generation else
       */
      if(el != null){
          String loaded_el = el.codeGeneration(labelManager);
          codeGenerated.append(loaded_el);
      }
      codeGenerated.append("b ").append(end_label).append("\n");


      /**
       * Code generation then
       */
      codeGenerated.append(then_branch).append(":\n");
      String loaded_th = th.codeGeneration(labelManager);
      codeGenerated.append(loaded_th).append("\n");

      /**
       * Append end_if_label_count
       */
      codeGenerated.append(end_label).append(":\n");


      return codeGenerated.toString();
  }
  
}  