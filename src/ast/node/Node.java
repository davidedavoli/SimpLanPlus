package ast.node;

import ast.node.types.RetEffType;
import ast.node.types.TypeNode;

import java.util.ArrayList;

import util.Environment;
import util.Label;
import util.SemanticError;

public interface Node {
  String toPrint(String indent);

  //fa il type checking e ritorna: 
  //  per una espressione, il suo tipo (oggetto BoolTypeNode o IntTypeNode)
  //  per una dichiarazione, "null"
  TypeNode typeCheck();
  
  String codeGeneration(Label labelManager);
  
  ArrayList<SemanticError> checkSemantics(Environment env);
  
  //ArrayList<SemanticError> delTypeCheck(DelEnv env, int nl);
  
  RetEffType retTypeCheck();
  
}  