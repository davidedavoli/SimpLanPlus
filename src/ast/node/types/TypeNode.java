package ast.node.types;

import java.util.ArrayList;

import GraphEffects.EffectsManager;
import ast.node.Node;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public interface TypeNode extends Node {
   
  String toPrint(String indent) throws SimplanPlusException;

  //fa il type checking e ritorna: 
  //  per una espressione, il suo tipo (oggetto BoolTypeNode o IntTypeNode)
  //  per una dichiarazione, "null"
  TypeNode typeCheck();
  
  TypeNode dereference() throws SimplanPlusException;
  
  String codeGeneration(Label labelManager);

  ArrayList<SemanticError> checkSemantics(Environment env);


}  