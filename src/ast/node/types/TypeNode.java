package ast.node.types;

import java.util.ArrayList;

import ast.node.Node;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public interface TypeNode extends Node {
   
  String toPrint(String indent);

  TypeNode typeCheck();
  
  TypeNode dereference();
  
  String codeGeneration(Label labelManager);
  
  ArrayList<SemanticError> checkSemantics(Environment env);
  
}  