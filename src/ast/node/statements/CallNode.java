package ast.node.statements;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ast.Dereferenceable;
import ast.STentry;
import ast.node.*;
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

            effectErrors.addAll(functionNode.fixPointCheckEffect(env, startingEffect));
        }

        if (!effectErrors.isEmpty()) {
            return effectErrors;
        }
        List<List<Effect>> functionEffects = id.getEntry().getFunctionStatusList();

        /**
         * Non pointer parameters
         */
        List<Integer> indexOfNoPointerParameters = new ArrayList<>();
        for(int index=0; index< parameterlist.size(); index++){
            ExpNode parameter = parameterlist.get(index);
            if ( !( (parameter instanceof Dereferenceable) && ((Dereferenceable) parameter).isPointer() ) ) {
                indexOfNoPointerParameters.add(index);
            }
        }
        /**
         * Variable in the expression of parameter
         */
        List<Dereferenceable> indexOfExpressionParameter = new ArrayList<>();
        for (ExpNode parameter : parameterlist) {
            if ( !( (parameter instanceof Dereferenceable) && ((Dereferenceable) parameter).isPointer() ) ) {
                indexOfExpressionParameter.addAll(parameter.variables());
            }
        }

        /**
         * Checkin error on non pointer parameters
         */
        for (int index : indexOfNoPointerParameters) {
            List<Effect> actualEffectsList = functionEffects.get(index);
            for(Effect effect : actualEffectsList ){
                if(effect.equals(Effect.ERROR))
                    effectErrors.add(new EffectError("The parameter " + parameterlist.get(index) + " from function: " + id.getID() + " is in Error status."));
            }
        }

        // Setting all variables inside expressions to be read/write.
        Environment e1 = new Environment(env); // Creating a copy of the environment.
        // exp of the parameter
        for (var variable : indexOfExpressionParameter) {
            var entryInE1 = e1.effectsLookUp(variable.getID());
            entryInE1.setDereferenceLevelVariableStatus(Effect.sequenceEffect(entryInE1.getDereferenceLevelVariableStatus(0), Effect.READWRITE), 0);
        }

        // Creating env 2 by getting all pointer/dereference of var
        Environment e2 = new Environment();

        /**
         * Non pointer parameters
         */
        List<Integer> indexOfPointerParameters = new ArrayList<>();
        for(int index=0; index< parameterlist.size(); index++){
            ExpNode parameter = parameterlist.get(index);
            if( (parameter instanceof Dereferenceable) && ((Dereferenceable) parameter).isPointer() ){
                indexOfPointerParameters.add(index);
            }
        }

        List<Environment> parEnvironments = new ArrayList<>();

        for (var i : indexOfPointerParameters) {
            // [u1 |-> seq] par [u2 |-> seq] par ... par [um |-> seq]
            // {[u1 |-> seq], [u2 |-> seq], ..., [um |-> seq]}
            Environment tmpEnvironment = new Environment();
            tmpEnvironment.createVoidScope();

            Dereferenceable pointer = parameterlist.get(i).variables().get(0);

            STentry entry = tmpEnvironment.createNewDeclaration(pointer.getID(), pointer.getEntry().getType());

            /*if(pointer.getEntry().getMaxDereferenceLevel() > functionEffects.get(i).size()) {
                //Dereference from the back
                for (int parameterDereferenceLevel = 0; parameterDereferenceLevel < functionEffects.get(i).size(); parameterDereferenceLevel++) {
                    int offset = parameterDereferenceLevel + (pointer.getEntry().getMaxDereferenceLevel() - functionEffects.get(i).size());
                    if (offset > functionEffects.get(i).size()) {
                        break;
                    }
                    Effect u_iEffect = env.effectsLookUp(pointer.getID()).getDereferenceLevelVariableStatus(offset);
                    Effect x_iEffect = functionEffects.get(i).get(offset);
                    Effect seq = Effect.sequenceEffect(u_iEffect, x_iEffect);

                    entry.setDereferenceLevelVariableStatus(seq, offset);
                }
            }
            else{
                for (int dereferenceLevel = 0; dereferenceLevel < pointer.getEntry().getMaxDereferenceLevel(); dereferenceLevel++) {
                    Effect u_iEffect = env.effectsLookUp(pointer.getID()).getDereferenceLevelVariableStatus(dereferenceLevel);
                    Effect x_iEffect = functionEffects.get(i).get(dereferenceLevel);
                    Effect seq = Effect.sequenceEffect(u_iEffect, x_iEffect);

                    entry.setDereferenceLevelVariableStatus(seq, dereferenceLevel);
                }
            }*/
            int actualDereference = 0;
            /*if(pointer.getEntry().getMaxDereferenceLevel() > functionEffects.get(i).size()) {
                actualDereference = pointer.getEntry().getMaxDereferenceLevel() - functionEffects.get(i).size();
                System.out.println(actualDereference);
            }*/

            for (int dereferenceLevel = 0; dereferenceLevel < pointer.getEntry().getMaxDereferenceLevel()-actualDereference; dereferenceLevel++) {
                Effect u_iEffect = env.effectsLookUp(pointer.getID()).getDereferenceLevelVariableStatus(dereferenceLevel);
                Effect x_iEffect = functionEffects.get(i).get(dereferenceLevel);
                Effect seq = Effect.sequenceEffect(u_iEffect, x_iEffect);

                entry.setDereferenceLevelVariableStatus(seq, dereferenceLevel);
            }
            parEnvironments.add(tmpEnvironment);
        }

        if (parEnvironments.size() > 0) {
            e2 = parEnvironments.get(0);
            for (int i = 1; i < parEnvironments.size(); i++) {
                e2 = Environment.parallelEnvironment(e2, parEnvironments.get(i));
            }
        }

        Environment updatedEnv = Environment.updateEnvironment(e1, e2);
        env.replaceWithNewEnv(updatedEnv);
        effectErrors.addAll(env.getEffectErrors());

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