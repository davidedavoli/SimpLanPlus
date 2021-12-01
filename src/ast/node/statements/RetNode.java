package ast.node.statements;

import java.util.ArrayList;
import java.util.List;

import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.exp.ExpNode;
import ast.node.types.*;
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

  public ExpNode getValNode(){
        return (ExpNode) this.val;

  }

  public TypeNode getEtype() {
	  return etype;
  }

  public String toPrint(String s) {
    return s+"Return\n" + (val!= null ? val.toPrint(s+"  ") : "");
  }

  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
	  ArrayList<SemanticError> res = new ArrayList<SemanticError> ();
      if(val != null){
          res.addAll(val.checkSemantics(env));
      }
      current_nl=env.getNestingLevel();

      FunNode f = new FunNode("foo", new VoidTypeNode(),null);//parent_f.getIdNode());
      List<Node> ancestor = this.getAncestorsInstanceOf(f.getClass());
      if(ancestor.size()==0){
          res.add(new SemanticError("Return expression out of a function"));
      }
      else{
          parent_f = (FunNode) ancestor.get(0);
      }

	  return res;
 	}

  
  public TypeNode typeCheck() {
        if(etype instanceof VoidTypeNode && val != null){
            System.err.println("Trying to return value in void function: "+parent_f.getId());
            System.exit(0);
        }
          //throw new SimplanPlusException("Returning val in void function");
        else if(etype.getClass() == PointerTypeNode.class){
          System.err.println("Trying to return pointer inside of function: "+parent_f.getId());
          System.exit(0);
        }
            //throw new SimplanPlusException("Trying to return pointer inside a function.");
        else if(val == null) {
          return new VoidTypeNode();
        }
        else if (TypeUtils.isSubtype(val.typeCheck(), etype)) {
          return etype;
        }
        else{
          System.err.println("Wrong return type for function "+parent_f.getId());
          System.exit(0);
        }
           // throw new SimplanPlusException("Wrong return type for function");
      return null;
  }  
  
  public HasReturn retTypeCheck() {
//        parent_f = funNode;
	    return new HasReturn(HasReturn.hasReturnType.PRES);
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