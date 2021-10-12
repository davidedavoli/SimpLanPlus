package ast.node.statements;
import java.util.ArrayList;

import ast.STentry;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import util.Environment;
import util.Label;
import util.SemanticError;

public class CallNode implements Node {

  private String id;
  private STentry entry;
  private ArrayList<Node> parlist; 
  private int nestinglevel;
  private String endFunction;

  
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

public RetEffType retTypeCheck(FunNode funNode) {

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
      StringBuilder cgen = new StringBuilder();

/*
      cgen.append("push $fp // save fp before call function\n");
      cgen.append("push $sp // save sp before call function\n");
      cgen.append("mv $sp $bsp\n");
      cgen.append("addi $a0 $bsp 2\n");
      cgen.append("sw $a0 0($bsp)\n"); // link al bsp il punto dello stack prima di push $fp

      cgen.append("subi $sp $sp 1 //RA\n");

      cgen.append("mv $fp $al //put in $al actual fp\n");
      for (int i=0; i<nestinglevel-entry.getNestinglevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      for (Node par:parlist){
          cgen.append(par.codeGeneration(labelManager)).append("\n");
          cgen.append("push $a0\n");

      }

      cgen.append("push $al // save access link\n");

      //cgen.append("mv $sp $fp //update $fp\n");
      cgen.append("addi $fp $sp ").append(parlist.size()).append(" // $fp at beginning of param\n");*/

      cgen.append("push $fp\n");

     // cgen.append("mv $sp $fp //put in $fp parameter frame\n");
      for (int i=parlist.size()-1; i>=0; i--){
          cgen.append(parlist.get(i).codeGeneration(labelManager)).append("\n");
          cgen.append("push $a0\n");
      }
       /*   for (Node par:parlist) {
          cgen.append(par.codeGeneration(labelManager)).append("\n");
          cgen.append("push $a0\n");
      }*/

      cgen.append("mv $fp $al //put in $al actual fp\n");


      for (int i=0; i<nestinglevel-entry.getNestinglevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      cgen.append("push $al\n");
      cgen.append("jal  ").append(entry.getBeginFuncLabel()).append("// jump to start of function and put in $ra next istruction\n");

      return cgen.toString();
      // formato AR: control_link+parameters+access_link+dich_locali
	/*	return "lfp\n"+ 				// CL
               parCode+
               "lfp\n"+getAR+ 		// setto AL risalendo la catena statica
               						// ora recupero l'indirizzo a cui saltare e lo metto sullo stack
               "push "+entry.getOffset()+"\n"+ // metto offset sullo stack
		       "lfp\n"+getAR+ 		// risalgo la catena statica
			   "add\n"+ 
               "lw\n"+ 				// carico sullo stack il valore all'indirizzo ottenuto
		       "js\n";
*/
  }

    
}  