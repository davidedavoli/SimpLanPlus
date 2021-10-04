package ast.node.types;

import java.util.ArrayList;

import ast.node.Node;
import util.Environment;
import util.Label;
import util.SemanticError;

public interface TypeNode extends Node{
   
  String toPrint(String indent);

  //fa il type checking e ritorna: 
  //  per una espressione, il suo tipo (oggetto BoolTypeNode o IntTypeNode)
  //  per una dichiarazione, "null"
  TypeNode typeCheck();
  
  TypeNode dereference();
  
  String codeGeneration(Label labelManager);
  
  ArrayList<SemanticError> checkSemantics(Environment env);
  
}  