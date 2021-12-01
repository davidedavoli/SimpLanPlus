package ast.node.statements;

import ast.Label;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.*;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class RetNode extends MetaNode {

  private final Node val;
  private final TypeNode returnType;// expected type
  private FunNode parent_f;
  private int current_nl;

    public RetNode (Node v, TypeNode e) {
    val=v;
    returnType=e;
  }

    public String toPrint(String s) {
    return s+"Return\n" + (val!= null ? val.toPrint(s+"  ") : "");
  }

  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  ArrayList<SemanticError> res = new ArrayList<>();
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
        if(returnType instanceof VoidTypeNode && val != null){
            System.err.println("Trying to return value in void function: "+parent_f.getId());
            System.exit(0);
        }
        else if(returnType.getClass() == PointerTypeNode.class){
          System.err.println("Trying to return pointer inside of function: "+parent_f.getId());
          System.exit(0);
        }
        else if(val == null) {
          return new VoidTypeNode();
        }
        else if (TypeUtils.isSubtype(val.typeCheck(), returnType)) {
          return returnType;
        }
        else{
          System.err.println("Wrong return type for function "+parent_f.getId());
          System.exit(0);
        }
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


    public String codeGeneration(Label labelManager) {
        StringBuilder codeGenerated = new StringBuilder();
        if( val != null){
            codeGenerated.append(val.codeGeneration(labelManager)).append("\n");
        }

        codeGenerated.append("lw $fp 0($fp) //Load old $fp pushed \n".repeat(Math.max(0, current_nl - parent_f.getBody().getCurrent_nl())));

        codeGenerated.append("subi $sp $fp 1 //Restore stack pointer as before block creation in return \n");
        codeGenerated.append("lw $fp 0($fp) //Load old $fp pushed \n");

        codeGenerated.append("b ").append(parent_f.get_end_fun_label()).append("\n");
		return codeGenerated.toString();
  }
    
}  