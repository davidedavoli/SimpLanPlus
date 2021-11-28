package ast.node.statements;
import java.util.ArrayList;
import java.util.List;

import ast.STentry;
import ast.node.ArgNode;
import ast.node.IdNode;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.exp.ExpNode;
import ast.node.exp.LhsExpNode;
import ast.node.types.*;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class CallNode extends MetaNode {

  private IdNode id;
  private STentry entry;
  private ArrayList<ExpNode> parameterlist;
  private int nestinglevel;
  private String endFunction;
  private Boolean isAlreadyCalled;


  
  public CallNode(IdNode id, ArrayList<ExpNode> args) {
	this.id=id;
    parameterlist = args;
    isAlreadyCalled = false;
}

public String toPrint(String s) throws SimplanPlusException {  //
    String parlstr="";
	for (ExpNode par:parameterlist)
	  parlstr+=par.toPrint(s+"  ");		
	return s+"Call:" + id + " at nestlev " + nestinglevel +"\n" 
           +entry.toPrint(s+"  ")
           +parlstr;        
  }

public RetEffType retTypeCheck() {

	  return new RetEffType(RetEffType.RetT.ABS);
}

    @Override
    public ArrayList<EffectError> checkEffects(Environment env) {
        ArrayList<EffectError> effectErrors = new ArrayList<>();

        effectErrors.addAll(id.checkEffects(env));
        parameterlist.forEach((p) -> effectErrors.addAll(p.checkEffects(env)));
        //Get all actual parameter status
        if(!isAlreadyCalled){

            isAlreadyCalled = true;
            FunNode functionNode = id.getEntry().getFunctionNode();

            List<List<Effect>> startingEffect = new ArrayList<>();

            for (ExpNode par : parameterlist) {
                List<Effect> parameterEffect = new ArrayList<>();

                // put all the pointed var in RW
                if (par instanceof LhsExpNode) {
                    int maxDereferenceLevel = par.variables().get(0).getEntry().getMaxDereferenceLevel();
                    for (int dereferenceLevel = 0; dereferenceLevel < maxDereferenceLevel; dereferenceLevel++) {
                        parameterEffect.add(new Effect(par.variables().get(0).getEntry().getDereferenceLevelVariableStatus(dereferenceLevel)));
                    }
                } else {
                    parameterEffect.add(new Effect(Effect.READWRITE));
                }
                startingEffect.add(parameterEffect);
            }

            System.out.println("ID FUN ENTRY "+id.getEntry());
            effectErrors.addAll(functionNode.fixPointCheckEffect(env, startingEffect));
        }

        if (!effectErrors.isEmpty()) {
            return effectErrors;
        }

        //All exp to rw

        //fix point



        // Check if error list is empty

        // Check error inside not pointer parameter of fun

        // Creating env 1 putting RW to all variables inside the
        // exp of the parameter


        // Creating env 2 by getting all pointer/dereference of var
        // from param and doing the seq with env and effects list
        // of the stentry function

        // Updating e1,e2
        // Replace with new env

        return effectErrors;
    }

    public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
		//create the result
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();


        entry = env.lookUp(id.getID());
        if (entry == null)
            res.add(new SemanticError("Id "+id+" not declared"));
        else{
            nestinglevel = env.getNestingLevel();
            for(ExpNode arg : parameterlist)
                res.addAll(arg.checkSemantics(env));
        }

        res.addAll(checkAncestorCall());

		return res;
  }
  
  public TypeNode typeCheck () throws SimplanPlusException {  //
	 ArrowTypeNode t=null;
     if (entry.getType() instanceof ArrowTypeNode)
         t=(ArrowTypeNode) entry.getType();
     else 
         throw new SimplanPlusException("Invocation of a non-function "+id);
     
     List<TypeNode> p = t.getParList();
     if ( !(p.size() == parameterlist.size()) )
         throw new SimplanPlusException("Wrong number of parameters in the invocation of "+id);
    
     for (int i=0; i<parameterlist.size(); i++)
       if ( !(TypeUtils.isSubtype( (parameterlist.get(i)).typeCheck(), p.get(i)) ) )
           throw new SimplanPlusException("Wrong type for "+(i+1)+"-th parameter in the invocation of "+id);
       
     return t.getRet();
  }
  
  public String codeGeneration(Label labelManager) throws SimplanPlusException {
      StringBuilder cgen = new StringBuilder();

      cgen.append("push $fp\n");

      for (int i=parameterlist.size()-1; i>=0; i--){
          cgen.append(parameterlist.get(i).codeGeneration(labelManager)).append("\n");
          cgen.append("push $a0\n");
      }

      cgen.append("mv $fp $al //put in $al actual fp\n");


      for (int i = 0; i<nestinglevel-entry.getNestingLevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      cgen.append("push $al\n");
      cgen.append("jal  ").append(entry.getBeginFuncLabel()).append("// jump to start of function and put in $ra next istruction\n");

      return cgen.toString();
  }


    public ArrayList<ExpNode> getParlist() {
        return parameterlist;
    }

    private ArrayList<SemanticError> checkAncestorCall(){
      ArrayList<SemanticError> res = new ArrayList<>();
        FunNode f = new FunNode("foo", new VoidTypeNode(),id);
        FunNode g;
        ArrayList<Node> path = this.getAncestorsInstanceOf(f.getClass());
        if(!path.isEmpty())
            path.remove(0);//plain recursive functions are ok
        for (Node parF: path) {
            g = (FunNode) parF;
            if(g.getId().equals(id.getID())){
                res.add(new SemanticError("call of ancestor function in (grand-)child "));
            }
        }
        return res;
    }

}