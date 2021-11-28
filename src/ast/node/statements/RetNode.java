package ast.node.statements;

import java.util.ArrayList;

import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import ast.node.types.VoidTypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class RetNode extends MetaNode {

  private Node val;
  private TypeNode etype;// expected type
  private FunNode parent_f;
  private int current_nl;

    public RetNode (Node v, TypeNode e) {
    val=v;
    etype=e;
  }
  
  
  public TypeNode getEtype() {
	  return etype;
  }

  public String toPrint(String s) throws SimplanPlusException {
    return s+"Return\n" + (val!= null ? val.toPrint(s+"  ") : "");
  }

  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
	  ArrayList<SemanticError> res = new ArrayList<SemanticError> ();

	  if (! (etype instanceof TypeNode))
          res.add(new SemanticError("Return expression out of a function"));
      if(val != null){
          res.addAll(val.checkSemantics(env));
      }
      current_nl=env.getNestingLevel();

      FunNode f = new FunNode("foo", new VoidTypeNode(),null);//parent_f.getIdNode());
      parent_f = (FunNode) this.getAncestorsInstanceOf(f.getClass()).get(0);
	  return res;
 	}

  
  public TypeNode typeCheck() throws SimplanPlusException {
        if(val == null){
            return new VoidTypeNode();
        }
        else if (TypeUtils.isSubtype(val.typeCheck(), etype))
            return etype;
        else
            throw new SimplanPlusException("Wrong return type for function");
  }  
  
  public RetEffType retTypeCheck() {
//        parent_f = funNode;
	    return new RetEffType(RetEffType.RetT.PRES);
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();
        if (val != null) {
            errors.addAll(val.checkEffects(env));
        }

        return errors;
    }


    public String codeGeneration(Label labelManager) throws SimplanPlusException {
        StringBuilder cgen = new StringBuilder();
        if( val != null){
            cgen.append(val.codeGeneration(labelManager)).append("\n");
        }

        for (int i = 0; i < current_nl-parent_f.getBody().getCurrent_nl(); i++)
            cgen.append("lw $fp 0($fp) //Load old $fp pushed \n");

        cgen.append("subi $sp $fp 1 //Restore stackpointer as before block creation in return \n");
        cgen.append("lw $fp 0($fp) //Load old $fp pushed \n");

        cgen.append("b ").append(parent_f.get_end_fun_label()).append("\n");
		return cgen.toString();
  }
    
}  