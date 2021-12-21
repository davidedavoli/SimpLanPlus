package ast.node.statements;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ast.Dereferences;
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

public class CallNode extends MetaNode {

    private final IdNode id;
    private STentry entry;
    private final ArrayList<ExpNode> parameterList;
    private int nestingLevel;
    private Boolean isAlreadyCalled;

    public CallNode(IdNode id, ArrayList<ExpNode> args) {
        this.id=id;
        parameterList = args;
        isAlreadyCalled = false;
    }

    public ArrayList<ExpNode> getParameterList() {
        return parameterList;
    }

    public ArrayList<SemanticError> checkSemantics(Environment env) {
        //create the result
        ArrayList<SemanticError> res = new ArrayList<>();

        entry = env.lookUp(id.getID());
        if (entry == null)
            res.add(new SemanticError("Fun Id "+id.getID()+" not declared.", id.getID()));
        else{
            nestingLevel = env.getNestingLevel();
            for(ExpNode arg : parameterList)
                res.addAll(arg.checkSemantics(env));
        }

        res.addAll(checkAncestorCall());

        return res;
    }
    private ArrayList<SemanticError> checkAncestorCall(){
        ArrayList<SemanticError> res = new ArrayList<>();
        FunNode f = new FunNode("foo", new VoidTypeNode(),id);
        FunNode g;
        ArrayList<Node> path = this.getAncestorsInstanceOf(f.getClass());
        System.out.println(path);

        if(!path.isEmpty()){
            FunNode par = (FunNode) path.get(0);
            if(Objects.equals(par.getId(), this.id.getID()))
                entry.getFunctionNode().setIsRecursive(true);
            path.remove(0);//plain recursive functions are ok
        }

        for (Node parF: path) {
            g = (FunNode) parF;
            if(g.getId().equals(id.getID())){
                res.add(new SemanticError("call of ancestor function in (grand-)child.", id.getID()));
            }
        }
        return res;
    }

    public TypeNode typeCheck() {  //
        ArrowTypeNode t=null;
        if (entry.getType() instanceof ArrowTypeNode)
            t=(ArrowTypeNode) entry.getType();
        else {
            System.err.println("Trying to invoke "+id.getID()+". But it is not a function.");
            System.exit(0);
        }

        List<TypeNode> p = t.getParList();
        if ( !(p.size() == parameterList.size()) ){
            System.err.println("Wrong number of parameters in the invocation of "+id.getID()+".");
            System.exit(0);
        }

        for (int i = 0; i< parameterList.size(); i++)
            if ( !(TypeUtils.isSubtype( (parameterList.get(i)).typeCheck(), p.get(i)) ) ){
                System.err.println("Wrong type for "+(i+1)+"-th parameter in the invocation of "+id.getID()+".");
                System.exit(0);
            }

        return t.getRet();
    }
    public HasReturn retTypeCheck() {
        return new HasReturn(HasReturn.hasReturnType.ABS);
    }

    @Override
    public ArrayList<EffectError> checkEffects(Environment env) {
        ArrayList<EffectError> effectErrors = new ArrayList<>(id.checkEffects(env));
        //parameterList.forEach((p) -> effectErrors.addAll(p.checkEffects(env)));
        //Get all actual parameter status
        Environment envCopy = new Environment(env);
        if(!isAlreadyCalled){

            isAlreadyCalled = true;
            FunNode functionNode = id.getEntry().getFunctionNode();

            List<List<Effect>> startingEffect = new ArrayList<>();

            for (ExpNode par : parameterList) {
                List<Effect> parameterEffect = new ArrayList<>();

                // put all the pointed var in RW
                if (par instanceof LhsExpNode) {
                    Dereferences parameter = par.variables().get(0);
                    int maxDereferenceLevel = parameter.getEntry().getMaxDereferenceLevel();

                    for (int dereferenceLevel = 0; dereferenceLevel < maxDereferenceLevel; dereferenceLevel++) {
                        parameterEffect.add(new Effect(parameter.getEntry().getDereferenceLevelVariableStatus(dereferenceLevel)));
                    }

                } else {
                    parameterEffect.add(new Effect(Effect.INITIALIZED));
                }
                startingEffect.add(parameterEffect);
            }

            effectErrors.addAll(functionNode.fixPointCheckEffect(env, startingEffect));
        }
        System.out.println("env before fixpoint: ");
        System.out.println(env);

        if (!effectErrors.isEmpty()) {
            return effectErrors;
        }
        id.setEntry(env.effectsLookUp(id.getID()));
        List<List<Effect>> functionEffects = envCopy.effectsLookUp(id.getID()).getFunctionStatusList();
        System.out.println(functionEffects);
        /**
         * All variables/expressions that are not pointer
         */
        List<Integer> indexOfNoPointerParameters = new ArrayList<>();
        for(int index = 0; index< parameterList.size(); index++){
            ExpNode parameter = parameterList.get(index);
            if ( !( (parameter instanceof Dereferences) && ((Dereferences) parameter).isPointer() ) ) {
                indexOfNoPointerParameters.add(index);
            }

        }
        /**
         * Variable in the expression of parameter
         */
        List<Dereferences> indexOfExpressionParameter = new ArrayList<>();
        for (ExpNode parameter : parameterList) {
            if ( !( (parameter instanceof Dereferences) && ((Dereferences) parameter).isPointer() ) ) {
                indexOfExpressionParameter.addAll(parameter.variables());
            }
        }

        /**
         * Checking error on non pointer parameters
         * Maybe is useless after the last commit
         */
        for (int index : indexOfNoPointerParameters) {
            List<Effect> actualEffectsList = functionEffects.get(index);
            for(Effect effect : actualEffectsList ){
                if(effect.equals(Effect.ERROR))
                    effectErrors.add(new EffectError("The parameter " + parameterList.get(index) + " from function: " + id.getID() + " is in Error status."));
            }
        }

        // Setting all variables inside expressions to be read/write.
        //Environment e1 = new Environment(env); // Creating a copy of the environment.
        Environment e1 = Environment.maxEnvironment(env,envCopy);// new Environment(envCopy); // Creating a copy of the environment.
        // exp of the parameter
        for (var variable : indexOfExpressionParameter) {
            var entryInE1 = e1.effectsLookUp(variable.getID());
            entryInE1.setDereferenceLevelVariableStatus(Effect.sequenceEffect(entryInE1.getDereferenceLevelVariableStatus(0), Effect.READWRITE), 0);
        }

        System.out.println(e1);

        // Creating env 2 by getting all pointer/dereference of var
        Environment e2 = new Environment();

        /**
         * Pointer parameters
         */
        List<Integer> indexOfPointerParameters = new ArrayList<>();
        for(int index = 0; index< parameterList.size(); index++){
            ExpNode parameter = parameterList.get(index);
            if( (parameter instanceof Dereferences) && ((Dereferences) parameter).isPointer() ){
                indexOfPointerParameters.add(index);
            }
        }

        List<Environment> parEnvironments = new ArrayList<>();

        for (var i : indexOfPointerParameters) {
            // [u1 |-> seq] par [u2 |-> seq] par ... par [um |-> seq]
            // {[u1 |-> seq], [u2 |-> seq], ..., [um |-> seq]}
            Environment tmpEnvironment = new Environment();
            tmpEnvironment.createVoidScope();

            Dereferences pointer = parameterList.get(i).variables().get(0);
            System.out.println("TMP");
            STentry entry = tmpEnvironment.createNewDeclaration(pointer.getID(), pointer.getEntry().getType(), pointer.getEntry().getisPar());
            for (int j =0; j<entry.getMaxDereferenceLevel(); j++)
                entry.setDereferenceLevelVariableStatus(e1.effectsLookUp(pointer.getID()).getDereferenceLevelVariableStatus(j), j);


            int actualDereference = 0;
            if(pointer.getEntry().getMaxDereferenceLevel() > functionEffects.get(i).size()) {
                actualDereference = pointer.getEntry().getMaxDereferenceLevel() - functionEffects.get(i).size();
            }
            /**
             * fun(^int x);
             * ^^int y;
             * fun(y^);
             * actualDereference = 5-2 = 3
             [ [0:c0,1:c1] ]

             0:c0->1:c1, 1:c1->2:c2

             [0:c0,1:c1,2:c2]

             */
            System.out.println("functionEffects: ");
            System.out.println(functionEffects);
            for (int dereferenceLevel = 0; dereferenceLevel < functionEffects.get(i).size(); dereferenceLevel++) {
                Effect u_iEffect = e1.effectsLookUp(pointer.getID()).getDereferenceLevelVariableStatus(dereferenceLevel+actualDereference);
                Effect x_iEffect = functionEffects.get(i).get(dereferenceLevel);
                Effect seq;

                System.out.println((pointer.getID()));
                System.out.println(isAccessToGlobal(pointer.getID()));
                if (isAccessToGlobal(pointer.getID()))
                    seq = Effect.parallelEffect(u_iEffect, x_iEffect);
                else
                    seq = Effect.sequenceEffect(u_iEffect, x_iEffect);

                entry.setDereferenceLevelVariableStatus(seq, dereferenceLevel+actualDereference);
            }
            parEnvironments.add(tmpEnvironment);
        }

        if (parEnvironments.size() > 0) {
            e2 = parEnvironments.get(0);
            for (int i = 1; i < parEnvironments.size(); i++) {
                e2 = Environment.parallelEnvironment(e2, parEnvironments.get(i));
            }
        }
        //List<Effect> returned = env.getCurrentST().get(id.getID()).getReturnList();

        Environment updatedEnv = Environment.updateEnvironment(e1, e2);
        env.replaceWithNewEnvironment(updatedEnv);
        //env.getCurrentST().get(id.getID()).setResultList(returned);


        effectErrors.addAll(env.getEffectErrors());

        return effectErrors;
    }

    private boolean isAccessToGlobal(String id){
//        System.out.println(entry.getFunctionNode().variables());
        return entry.getFunctionNode().variables().stream().map((x)->x.getID()).collect(Collectors.toList()).contains(id);
    }

    public String codeGeneration(Label labelManager) {
        StringBuilder codeGenerated = new StringBuilder();

        codeGenerated.append("push $fp\n");

        for (int i = parameterList.size()-1; i>=0; i--){
            codeGenerated.append(parameterList.get(i).codeGeneration(labelManager)).append("\n");
            codeGenerated.append("push $a0\n");
        }

        codeGenerated.append("mv $fp $al //put in $al actual fp\n");


        codeGenerated.append("lw $al 0($al) //go up to chain\n".repeat(Math.max(0, nestingLevel - entry.getNestingLevel())));

        codeGenerated.append("push $al\n");
        codeGenerated.append("jal  ").append(entry.getBeginFuncLabel()).append("// jump to start of function and put in $ra next instruction\n");

        return codeGenerated.toString();
    }

    public List<Dereferences> variables(){
        return parameterList.stream().flatMap((x)->x.variables().stream()).collect(Collectors.toList());
    }

    public String toPrint(String s) {  //
        StringBuilder parameterString= new StringBuilder();
        for (ExpNode par: parameterList)
            parameterString.append(par.toPrint(s + "  "));
        return s+"Call:" + id + " at nesting level " + nestingLevel +"\n"
                +entry.toPrint(s+"  ")
                +parameterString;
    }
}