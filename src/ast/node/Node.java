package ast.node;

import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import java.util.ArrayList;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public interface Node {

  String toPrint(String indent);

  //type checking return:
  //  for an expression his type
  //  for a declaration null
  TypeNode typeCheck();
  
  String codeGeneration(Label labelManager);

  ArrayList<SemanticError> checkSemantics(Environment env);

  HasReturn retTypeCheck();

  ArrayList<EffectError> checkEffects(Environment env);

    default int getDereferenceLevel(){
      return 0;
    }

}