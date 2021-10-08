package ast.node.dec;

import ast.STentry;
import ast.node.Node;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;

import java.util.ArrayList;
import java.util.HashMap;

import ast.node.types.TypeUtils;
import util.Environment;
import util.Label;
import util.SemanticError;
import util.FuncBodyUtils;

public class VarNode implements Node {

  private String id;
  private TypeNode type;
  private Node exp;
  
  public VarNode (String i, TypeNode t, Node v) {
    id=i;
    type=t;
    exp=v;
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
  		//create result list
  		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  	  
  		//env.offset = -2;
  		HashMap<String, STentry> hm = env.symTable.get(env.nestingLevel);
          int new_offset = env.offset--;
          System.out.println("NEW OFFSET "+new_offset);
        STentry entry = new STentry(env.nestingLevel,type, new_offset); //separo introducendo "entry"
        
        if (exp!=null)
        	res.addAll(exp.checkSemantics(env));

        if ( hm.put(id,entry) != null )
        		res.add(new SemanticError("Var id "+id+" already declared"));
                
        return res;
  }

//  @Override
//  public ArrayList<SemanticError> delTypeCheck(DelEnv env, int nl) {
//	  
//	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
//	  
//	  if (!(type instanceof PointerTypeNode)) {
//		HashSet<Pair<String, Integer>> tmp = new HashSet<Pair<String, Integer>>();
//		tmp.add(new Pair<String, Integer>(id, nl));
//		env.add(new AliasingDomain(tmp, new DelList()));
//		res.addAll(exp.delTypeCheck(env, nl));
//	  }
//	  else {
//		HashSet<Pair<String, Integer>> tmp = new HashSet<Pair<String, Integer>>();
//		tmp.add(new Pair<String, Integer>(id, nl));
//		
//		DelList l = new DelList(((PointerTypeNode)type).getDerefLevel());
//		
//		if ((exp instanceof NewNode)) {
//			l.put(0,new DelEffType(DelEffType.DelT.NIL));// statement del tipo ^type = exp dovrebbero essere consentiti solo se exp è new ^type.
//		}
//		else {
//			res.add(new SemanticError("Errore grave! non dovremmo nemmeno entrare in questo ramo"));
//		}
//	  }
//	  
//	  return res;
//  }

  public String toPrint(String s) {
	return s+"Var:" + id +"\n"
	  	   +type.toPrint(s+"  ")
         +((exp==null)?"":exp.toPrint(s+"  ")); 
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () {
    if (exp != null && ! (TypeUtils.isSubtype(exp.typeCheck(),type)) ){
      System.out.println("incompatible value for variable "+id);
      System.exit(0);
    }     
    return type;
  }
  
  public String codeGeneration(Label labelManager) {
      StringBuilder cgen = new StringBuilder();
      if(exp != null){
          cgen.append(exp.codeGeneration(labelManager)).append("\n");
          cgen.append("push $a0\n");

      }
      else{
          /**
           * Decidere se pushare 0 per dire che non c'è nulla o alzare solamente lo stack pointer
           */
          cgen.append("subi $sp $sp 1 // non assegnato nulla\n");
          //cgen.append("push 0\n");
      }

	  return cgen.toString();
  }  
    
}  