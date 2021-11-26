package ast.node.statements;
import java.util.ArrayList;
import java.util.List;

import ast.STentry;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.exp.ExpNode;
import ast.node.types.*;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class CallNode extends MetaNode {

  private String id;
  private STentry entry;
  private ArrayList<ExpNode> parlist;
  private int nestinglevel;
  private String endFunction;

  
  public CallNode (String i, STentry e, ArrayList<ExpNode> p, int nl) {
    id=i;
    entry=e;
    parlist = p;
    nestinglevel=nl;
  }
  
  public CallNode(String text, ArrayList<ExpNode> args) {
	id=text;
    parlist = args;
}

public String toPrint(String s) throws SimplanPlusException {  //
    String parlstr="";
	for (ExpNode par:parlist)
	  parlstr+=par.toPrint(s+"  ");		
	return s+"Call:" + id + " at nestlev " + nestinglevel +"\n" 
           +entry.toPrint(s+"  ")
           +parlstr;        
  }

public RetEffType retTypeCheck() {

	  return new RetEffType(RetEffType.RetT.ABS);
}

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {





        return new ArrayList<>();
    }

    public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
		//create the result
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();


        entry = env.lookUp(id);
        if (entry == null)
            res.add(new SemanticError("Id "+id+" not declared"));
        else{
            nestinglevel = env.getNestingLevel();
            for(ExpNode arg : parlist)
                res.addAll(arg.checkSemantics(env));
        }

        FunNode f = new FunNode("foo", new VoidTypeNode());
        FunNode g;
        ArrayList<Node> path = this.getAncestorsInstanceOf(f.getClass());
        if(!path.isEmpty())
            path.remove(0);//plain recursive functions are ok
        for (Node parF: path) {
            g = (FunNode) parF;
            if(g.getId().equals(id)){
                res.add(new SemanticError("call of ancestor function in (grand-)child "));
            }
        }

		return res;
  }
  
  public TypeNode typeCheck () throws SimplanPlusException {  //
	 ArrowTypeNode t=null;
     if (entry.getType() instanceof ArrowTypeNode) t=(ArrowTypeNode) entry.getType(); 
     else 
         throw new SimplanPlusException("Invocation of a non-function "+id);
     
     List<TypeNode> p = t.getParList();
     if ( !(p.size() == parlist.size()) )
         throw new SimplanPlusException("Wrong number of parameters in the invocation of "+id);
    
     for (int i=0; i<parlist.size(); i++) 
       if ( !(TypeUtils.isSubtype( (parlist.get(i)).typeCheck(), p.get(i)) ) )
           throw new SimplanPlusException("Wrong type for "+(i+1)+"-th parameter in the invocation of "+id);
       
     return t.getRet();
  }
  
  public String codeGeneration(Label labelManager) throws SimplanPlusException {
      StringBuilder cgen = new StringBuilder();

      cgen.append("push $fp\n");

      for (int i=parlist.size()-1; i>=0; i--){
          cgen.append(parlist.get(i).codeGeneration(labelManager)).append("\n");
          cgen.append("push $a0\n");
      }
       /*   for (Node par:parlist) {
          cgen.append(par.codeGeneration(labelManager)).append("\n");
          cgen.append("push $a0\n");
      }*/

      cgen.append("mv $fp $al //put in $al actual fp\n");


      for (int i = 0; i<nestinglevel-entry.getNestingLevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      cgen.append("push $al\n");
      cgen.append("jal  ").append(entry.getBeginFuncLabel()).append("// jump to start of function and put in $ra next istruction\n");

      return cgen.toString();
  }


    public ArrayList<ExpNode> getParlist() {
        return parlist;
    }
}