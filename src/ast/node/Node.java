package ast.node;

import ast.node.types.HasReturn;
import ast.node.types.TypeNode;

import java.util.ArrayList;

import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public interface Node {

  String toPrint(String indent) throws SimplanPlusException;

  //fa il type checking e ritorna: 
  //  per una espressione, il suo tipo (oggetto BoolTypeNode o IntTypeNode)
  //  per una dichiarazione, "null"
  TypeNode typeCheck() throws SimplanPlusException;
  
  String codeGeneration(Label labelManager) throws SimplanPlusException;



  ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException;
  
  //ArrayList<SemanticError> delTypeCheck(DelEnv env, int nl);

  HasReturn retTypeCheck();

  ArrayList<EffectError> checkEffects(Environment env);

    default int getDereferenceLevel(){
      return 0;
    }

}