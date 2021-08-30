package ast;

import del.*;
import types.RetEffType;
import types.TypeNode;

import java.util.ArrayList;
import java.util.HashMap;

import util.SimpLanlib;
import util.Environment;
import util.SemanticError;

public class BlockNode implements Node {

  private ArrayList<Node> declarations;
  private ArrayList<Node> statements;
  
  public BlockNode (ArrayList<Node> d, ArrayList<Node> e) {
    declarations=d;
    statements=e;
  }
  
  public String toPrint(String s) {
	String declstr="";
	String statstr="";
    for (Node dec:declarations)
      declstr += dec.toPrint(s+"  ");
    for (Node stat:statements)
      statstr += stat.toPrint(s+"  ");
	return s+"Block\n" + declstr + statstr ; 
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  env.nestingLevel++;
      HashMap<String,STentry> hm = new HashMap<String,STentry> ();
      env.symTable.add(hm);
      
      //declare resulting list
      ArrayList<SemanticError> res = new ArrayList<SemanticError>();
      
      //check semantics in the dec list
      if(declarations.size() > 0){
    	  //FIXME offset messo completamente a caso
    	  env.offset = -2;
    	  //if there are children then check semantics for every child and save the results
    	  for(Node n : declarations)
    		  res.addAll(n.checkSemantics(env));
      }
      
      if(statements.size() > 0){
    	  //FIXME offset messo completamente a caso
    	  env.offset = -2;
    	  //if there are children then check semantics for every child and save the results
    	  for(Node n : statements)
    		  res.addAll(n.checkSemantics(env));
      }

      //check semantics in the exp body      
      //clean the scope, we are leaving a let scope
      env.symTable.remove(env.nestingLevel--);
      
      //return the result
      return res;
	}
  
  public TypeNode typeCheck () {
	  //ne siamo sicuri?
	TypeNode last =null;
    for (Node dec:declarations)
      last = dec.typeCheck();
    for (Node stat:statements)
      last = stat.typeCheck();
    return last;
 }
  
  public ArrayList<TypeNode> getReturnList(){
      ArrayList<TypeNode> res = new ArrayList<TypeNode>();
      for (Node s: statements) {
    	  if (s instanceof BlockNode)
    		  res.addAll(((BlockNode) s).getReturnList());//Warning  Casting
    	  else if (s instanceof RetNode) {
    		  res.add(s.typeCheck());
    		  break;
    	  }
      }
      return res;
  }
  
  public RetEffType retTypeCheck() {
	  RetEffType tmp = new RetEffType(RetEffType.RetT.ABS);
	  for (Node s:statements) {
			  tmp= RetEffType.max(tmp, s.retTypeCheck());
	  }
	  return tmp;
  }

  public ArrayList<SemanticError> delTypeCheck(DelEnv env, int n){
	  ArrayList<SemanticError> err = new ArrayList<SemanticError>();
	  
	  for (Node i: declarations) {
		  err.addAll(i.delTypeCheck(env, n));
	  }
	  
	  for (Node i: statements) {
		  if (!(i instanceof BlockNode))
			  err.addAll(i.delTypeCheck(env, n+1));
		  else
			  err.addAll(i.delTypeCheck(env, n));
	  }

	  return err;
  }
  
  public String codeGeneration() {
	  String code="";
	  for (Node dec:declarations)
		    code+=dec.codeGeneration();
	  for (Node stat:statements)
		    code+=stat.codeGeneration();
	  return  "push 0\n"+
			  code+
			  SimpLanlib.getCode();
  } 
  
  
    
}  