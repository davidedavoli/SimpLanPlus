package ast.node;

import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;

import java.util.ArrayList;

import util.Environment;
import util.Label;
import util.SemanticError;
import util.SimplanPlusException;

public interface Node {
  String toPrint(String indent) throws SimplanPlusException;

  //fa il type checking e ritorna: 
  //  per una espressione, il suo tipo (oggetto BoolTypeNode o IntTypeNode)
  //  per una dichiarazione, "null"
  TypeNode typeCheck() throws SimplanPlusException;
  
  String codeGeneration(Label labelManager) throws SimplanPlusException;
  
  ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException;
  
  //ArrayList<SemanticError> delTypeCheck(DelEnv env, int nl);

  RetEffType retTypeCheck(FunNode funNode);
}