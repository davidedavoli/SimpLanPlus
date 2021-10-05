package ast.node.statements;
import java.util.ArrayList;

import ast.STentry;
import ast.node.Node;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class CallNode implements Node {

  private String id;
  private STentry entry;
  private ArrayList<Node> parlist; 
  private int nestinglevel;

  
  public CallNode (String i, STentry e, ArrayList<Node> p, int nl) {
    id=i;
    entry=e;
    parlist = p;
    nestinglevel=nl;
  }
  
  public CallNode(String text, ArrayList<Node> args) {
	id=text;
    parlist = args;
}

public String toPrint(String s) {  //
    String parlstr="";
	for (Node par:parlist)
	  parlstr+=par.toPrint(s+"  ");		
	return s+"Call:" + id + " at nestlev " + nestinglevel +"\n" 
           +entry.toPrint(s+"  ")
           +parlstr;        
  }

public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
}

  public ArrayList<SemanticError> checkSemantics(Environment env) {
		//create the result
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		
		 int j=env.nestingLevel;
		 STentry tmp=null; 
		 while (j>=0 && tmp==null)
		     tmp=(env.symTable.get(j--)).get(id);
		 if (tmp==null)
			 res.add(new SemanticError("Id "+id+" not declared"));
		 
		 else{
			 this.entry = tmp;
			 this.nestinglevel = env.nestingLevel;
			 
			 for(Node arg : parlist)
				 res.addAll(arg.checkSemantics(env));
		 }
		 return res;
  }
  
  public TypeNode typeCheck () {  //                           
	 ArrowTypeNode t=null;
     if (entry.getType() instanceof ArrowTypeNode) t=(ArrowTypeNode) entry.getType(); 
     else {
       System.out.println("Invocation of a non-function "+id);
       System.exit(0);
     }
     ArrayList<TypeNode> p = t.getParList();
     if ( !(p.size() == parlist.size()) ) {
       System.out.println("Wrong number of parameters in the invocation of "+id);
       System.exit(0);
     } 
     for (int i=0; i<parlist.size(); i++) 
       if ( !(TypeUtils.isSubtype( (parlist.get(i)).typeCheck(), p.get(i)) ) ) {
         System.out.println("Wrong type for "+(i+1)+"-th parameter in the invocation of "+id);
         System.exit(0);
       } 
     return t.getRet();
  }
  
  public String codeGeneration(Label labelManager) {
	    String parCode="";
	    for (int i=parlist.size()-1; i>=0; i--)
	    	parCode+=parlist.get(i).codeGeneration(labelManager);
	    
	    String getAR="";
		  for (int i=0; i<nestinglevel-entry.getNestinglevel(); i++) 
		    	 getAR+="lw\n";
		  					// formato AR: control_link+parameters+access_link+dich_locali
		return "lfp\n"+ 				// CL
               parCode+
               "lfp\n"+getAR+ 		// setto AL risalendo la catena statica
               						// ora recupero l'indirizzo a cui saltare e lo metto sullo stack
               "push "+entry.getOffset()+"\n"+ // metto offset sullo stack
		       "lfp\n"+getAR+ 		// risalgo la catena statica
			   "add\n"+ 
               "lw\n"+ 				// carico sullo stack il valore all'indirizzo ottenuto
		       "js\n";
  }

    
}  