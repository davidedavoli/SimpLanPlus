package ast.node.dec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ast.STentry;
import ast.node.ArgNode;
import ast.node.Node;
import ast.node.statements.BlockNode;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.VoidTypeNode;
import util.Label;
import util.FuncBodyUtils;
import util.Environment;
import util.SemanticError;

public class FunNode implements Node {

  private String id;
  private TypeNode type; 
  private ArrayList<TypeNode> partypes;
  private ArrayList<Node> parlist = new ArrayList<Node>(); 
  private ArrayList<Node> declist; 
  private BlockNode body;
  private String beginFuncLabel = "";
  private String endFuncLabel = "";

  public FunNode (String i, TypeNode t) {
    id=i;
    type=t;
	beginFuncLabel = FuncBodyUtils.freshFunLabel();
	endFuncLabel = FuncBodyUtils.endFreshFunLabel();
	System.out.println("CREO FUNZIONE " +id);
  }
  
  public void addDecBody (ArrayList<Node> d, BlockNode b) {
    declist=d;
    body=b;
	body.setIsFunction(true);

  }
  public String get_start_fun_label(){
	  return beginFuncLabel;
  }

	public String get_end_fun_label(){
		return endFuncLabel;
	}

  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  
	  //create result list
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  //env.offset = -2;
	  HashMap<String, STentry> hm = env.symTable.get(env.nestingLevel);
	  int new_offset = env.offset--;
      STentry entry = new STentry(env.nestingLevel,new_offset,beginFuncLabel,endFuncLabel); //separo introducendo "entry"
      
      if ( hm.put(id,entry) != null )
        res.add(new SemanticError("Fun id '"+id+"' already declared"));
      else{
    	  //creare una nuova hashmap per la symTable
	      env.nestingLevel++;
	      HashMap<String,STentry> hmn = new HashMap<String,STentry> ();

		  env.symTable.add(hmn);

	      ArrayList<TypeNode> parTypes = new ArrayList<TypeNode>();
	      int paroffset=1;
	      
	      //check args
	      for(Node a : parlist){
	    	  ArgNode arg = (ArgNode) a;
	    	  parTypes.add(arg.getType());

				  if ( hmn.put(arg.getId(),new STentry(env.nestingLevel,arg.getType(),paroffset++)) != null  )
	    		  	System.out.println("Parameter id '"+arg.getId()+"' already declared");
	      }

	      //set func type
	      
	      partypes= parTypes;
	      
	      entry.addType( new ArrowTypeNode(parTypes, type) );
	      

		  res.addAll(body.checkSemantics(env));

		  //close scope
	      env.symTable.remove(env.nestingLevel--);
	      
      }
      
      RetEffType abs = new RetEffType(RetEffType.RetT.ABS);
      RetEffType pres = new RetEffType(RetEffType.RetT.PRES);
      
      
      if (!(type instanceof VoidTypeNode) && body.retTypeCheck(this).leq(abs)) {
    	  res.add(new SemanticError("Possible absence of return value"));
      }
      /*if ((type instanceof VoidTypeNode) && pres.leq(body.retTypeCheck())) {
    	  res.add(new SemanticError("Return statement in void function"));
      }*/

      
      return res;
	}
  
  public void addPar (Node p) {
    parlist.add(p);
  }  
  
  public String toPrint(String s) {
	String parlstr="";
	for (Node par:parlist)
	  parlstr+=par.toPrint(s+"  ");
	String declstr="";
	if (declist!=null) 
	  for (Node dec:declist)
	    declstr+=dec.toPrint(s+"  ");
    return s+"Fun:" + id +"\n"
		   +type.toPrint(s+"  ")
		   +parlstr
	   	   +declstr
           +body.toPrint(s+"  ") ; 
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () {
	if (declist!=null) 
	  for (Node dec:declist)
		dec.typeCheck();
	body.typeCheck();
    return new ArrowTypeNode(partypes, type);
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) {
	  int declaration_size = 0;
	  int parameter_size = parlist.size();


	  StringBuilder cgen = new StringBuilder();

	  cgen.append("//BEGIN FUNCTION ").append(beginFuncLabel).append("\n");
	  cgen.append(beginFuncLabel).append(":\n");
	  //cgen.append("sw $fp 0($sp)\n");
	  cgen.append("mv $sp $fp\n");
	  cgen.append("push $ra\n");

	  /*if (declist!=null) {
		  declaration_size = declist.size();
		  for (Node dec:declist)
			  cgen.append(dec.codeGeneration(labelManager));
	  }
	  cgen.append(body.codeGeneration(labelManager)).append("\n");*/
	  cgen.append(body.codeGeneration(labelManager)).append("\n");
	  cgen.append(endFuncLabel).append(":\n");

	  cgen.append("lw $ra 0($sp)\n");
	  cgen.append("pop\n");


	  cgen.append("addi $sp $sp ").append(declaration_size).append("//pop declaration ").append(declaration_size).append("\n");
	  cgen.append("addi $sp $sp ").append(parameter_size).append("// pop parameters").append(parameter_size).append("\n");
	  cgen.append("pop\n");
	  cgen.append("lw $fp 0($sp)\n");


	  //cgen.append("sw $fp 0($fp)\n");

	  cgen.append("jr $ra\n");
	  /*
	  cgen.append("sw $ra -1($bsp) //save ra before old sp\n");

	  if (declist!=null) {
			declaration_size = declist.size();
			for (Node dec:declist)
				cgen.append(dec.codeGeneration(labelManager));
	  }


	  cgen.append(body.codeGeneration(labelManager)).append("\n");


	  //FINE FUNZIONE
	  cgen.append(endFuncLabel).append(":\n");
	  //Here in $a0 = Return value
	  cgen.append("addi $sp $sp ").append(declaration_size).append("\n");
	  cgen.append("addi $sp $sp ").append(parameter_size).append("\n");
	  cgen.append("lw $ra -1($bsp) // load ra\n");

	  cgen.append("lw $fp 1($bsp)\n");
	  cgen.append("lw $sp 0($bsp)\n"); // Restore old stack pointer.
	  cgen.append("addi $bsp $fp 2\n"); // Restore address of old base stack pointer.
	  cgen.append("jr $ra\n");*/
	  cgen.append("// END OF ").append(id).append("\n");

	  

	  return cgen.toString();
/*
		String body_fun = funl+":\n"+
				"cfp\n"+ 		// setta $fp a $sp
				"lra\n"+ 		// inserimento return address
				declCode+ 		// inserimento dichiarazioni locali
				body.codeGeneration(labelManager)+
				"srv\n"+ 		// pop del return value
				popDecl+
				"sra\n"+ 		// pop del return address
				"pop\n"+ 		// pop di AL
				popParl+
				"sfp\n"+  		// setto $fp a valore del CL
				"lrv\n"+ 		// risultato della funzione sullo stack
				"lra\n"+"js\n";  // salta a $ra
	    FuncBodyUtils.putCode(funl+":\n"+
	        "cfp\n"+ 		// setta $fp a $sp				
	        "lra\n"+ 		// inserimento return address
	    		declCode+ 		// inserimento dichiarazioni locali
	    		body.codeGeneration(labelManager)+
	    		"srv\n"+ 		// pop del return value
	    		popDecl+
	    		"sra\n"+ 		// pop del return address
	    		"pop\n"+ 		// pop di AL
	    		popParl+
	    		"sfp\n"+  		// setto $fp a valore del CL
	    		"lrv\n"+ 		// risultato della funzione sullo stack
	    		"lra\n"+"js\n"  // salta a $ra
	    		);
	    
		return "push "+ funl +"\n";*/
  }
  
}  