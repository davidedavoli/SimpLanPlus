package ast.node.statements;

import java.util.ArrayList;

//import org.stringtemplate.v4.compiler.STParser.notConditional_return;

import ast.STentry;
import ast.node.IdNode;
import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Effect;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class DeletionNode implements Node {

  private IdNode id;
  private TypeNode type;
  private STentry entry;
  
  public DeletionNode (IdNode i) {
    id=i;
    type=null;
    entry=null;
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
	  //create result list
      ArrayList<SemanticError> res = new ArrayList<SemanticError>();
      res = id.checkSemantics(env);
      type = id.typeCheck();
      entry = id.getEntry();
      return res;
  }
  
  public String toPrint(String s) {
	return s+"Delete:" + id +"\n";
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () throws SimplanPlusException {
	  if (! (type instanceof PointerTypeNode))
          throw new SimplanPlusException("attempted deletion of a non-pointer variable");
    return id.typeCheck();
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
      ArrayList<SemanticError> errors = new ArrayList<>();

      errors.addAll(id.checkEffects(env));

      STentry idEntry = id.getEntry();
      System.out.println(idEntry.getStatusList());
      /*System.out.println("ENTRY TO DELETE");
      System.out.println(idEntry);
      System.out.println(idEntry.getStatus(0));*/

      System.out.println(idEntry);
      if (
              idEntry.getStatus(1).equals(new Effect(Effect.DEL))
              ||
              idEntry.getStatus(1).equals(new Effect(Effect.ERR))
      ) {
        errors.add(new SemanticError(id.getID() + " already deleted."));
      } else {

        errors.addAll(env.checkStmStatus(
                new LhsNode(id),
                Effect::sequenceEffect,
                Effect.DEL)
        );
      }
      return errors;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
      StringBuilder cgen = new StringBuilder();
      System.out.println("DELETE NODE "+id.getID()+" NODO_ "+id);
      cgen.append(id.codeGeneration(labelManager)).append("\n");
      cgen.append("free $a0 //free address in $a0\n");

      return cgen.toString();
  }  
    
}  