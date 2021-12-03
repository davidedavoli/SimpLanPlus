package ast.node.statements;

import java.util.ArrayList;

import GraphEffects.EffectsManager;
import GraphEffects.Graph;
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
import semantic.SimplanPlusException;

public class IfNode extends MetaNode {

  private ExpNode cond;
  private Node th;
  private Node el;
  
  public IfNode (ExpNode c, Node t, Node e) {
    cond=c;
    th=t;
    el=e;

  }
  
  public String toPrint(String s) throws SimplanPlusException {
      String print = s+"If\n" + cond.toPrint(s+"  ")
              + th.toPrint(s+"  ");
      if (el != null)
          print = print + el.toPrint(s+"  ");
      return print;

  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
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
  
  public HasReturn retTypeCheck() {
      HasReturn th_v=th.retTypeCheck();
      HasReturn el_v=(el!=null)?el.retTypeCheck():new HasReturn(HasReturn.hasReturnType.ABS);
      //do not remove these lines: retTypeCheck has a side-effect;
	  if (el!=null)
		  return HasReturn.min(th_v, el_v);
	  else
		  return new HasReturn(th_v.getVal());//RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();

        errors.addAll(cond.checkEffects(env));

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

    public TypeNode typeCheck() throws SimplanPlusException {
    if (
            !(TypeUtils.isSubtype(cond.typeCheck(),new BoolTypeNode()))

    ) {
        throw new SimplanPlusException("non boolean condition in if");
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

//	non più necessari dal momento che l'if-then-esle non è più un'espressione
    
//    System.out.println("Incompatible types in then else branches");
//    System.exit(0);
    return null;
  }
  
  public String codeGeneration(Label labelManager) throws SimplanPlusException {

      StringBuilder cgen = new StringBuilder();
      String then_branch = labelManager.freshLabel("then");
      String end_label = labelManager.freshLabel("endIf");
      /**
       * Cgen condizione
       */
      String loaded_cond = cond.codeGeneration(labelManager);
      cgen.append(loaded_cond).append("\n");
      cgen.append("bc $a0 ").append(then_branch).append("\n");

      /**
       * Cgen else
       */
      if(el != null){
          String loaded_el = el.codeGeneration(labelManager);
          cgen.append(loaded_el);
      }
      cgen.append("b ").append(end_label).append("\n");


      /**
       * Cgen then
       */
      cgen.append(then_branch).append(":\n");
      String loaded_th = th.codeGeneration(labelManager);
      cgen.append(loaded_th).append("\n");

      /**
       * Append end_if_label_count
       */
      cgen.append(end_label).append(":\n");


      return cgen.toString();
  }

    @Override
    public void checkGraphEffects(EffectsManager m) {
        EffectsManager m1;
        EffectsManager m2;

        Graph new_graph;

        cond.checkGraphEffects(m);

        m1 = new EffectsManager(m.getNl(), m.getG().copy());
        m2 = new EffectsManager(m.getNl(), m.getG().copy());

        th.checkGraphEffects(m1);

        new_graph= Graph.sup(m.getG(), m1.getG());

        if (el!=null) {
            th.checkGraphEffects(m2);
            new_graph = Graph.sup(new_graph, m2.getG());
        }


        m.setG(new_graph);
    }
}