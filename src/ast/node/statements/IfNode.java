package ast.node.statements;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.exp.ExpNode;
import ast.node.types.BoolTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class IfNode extends MetaNode {

    private final ExpNode condition;
    private final Node thenBranch;
    private final Node elseBranch;

    public IfNode (ExpNode c, Node t, Node e) {
        condition = c;
        thenBranch = t;
        elseBranch = e;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        //create the result
        ArrayList<SemanticError> res = new ArrayList<>();

        //check semantics in the condition
        res.addAll(condition.checkSemantics(env));
        //check semantics in the then and in the else exp
        res.addAll(thenBranch.checkSemantics(env));
        if (elseBranch!=null)
            res.addAll(elseBranch.checkSemantics(env));
        return res;
    }

    public TypeNode typeCheck() {
        if ( !(TypeUtils.isSubtype(condition.typeCheck(),new BoolTypeNode()))) {
            System.err.println("Non boolean condition inside if: "+ condition.toPrint(""));
            System.exit(0);
        }

        TypeNode t = thenBranch.typeCheck();
        TypeNode e = elseBranch.typeCheck();
        if(t == null){
            System.err.println("Empty then.");
            System.exit(0);
        }
        if(e == null){
            return t;
        }
        else{
            if (TypeUtils.isSubtype(t,e))
                return e;
            if (TypeUtils.isSubtype(e,t))
                return t;
        }
        return null;
    }
    public HasReturn retTypeCheck() {
        HasReturn th_v= thenBranch.retTypeCheck();
        HasReturn el_v=(elseBranch!=null)?elseBranch.retTypeCheck():new HasReturn(HasReturn.hasReturnType.ABS);
        return HasReturn.min(th_v, el_v);
    }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {

        ArrayList<EffectError> errors = new ArrayList<>(condition.checkEffects(env));

        if (elseBranch != null) {
            var thenEnv = new Environment(env);
            errors.addAll(thenBranch.checkEffects(thenEnv));

            var elseEnv = new Environment(env);
            errors.addAll(elseBranch.checkEffects(elseEnv));

            env.replaceWithNewEnvironment(Environment.maxEnvironment(thenEnv, elseEnv));
        } else {
            errors.addAll(thenBranch.checkEffects(env));
        }

        return errors;
    }

    public String codeGeneration(Label labelManager) {

        StringBuilder codeGenerated = new StringBuilder();
        String then_branch = labelManager.freshLabel("then");
        String end_label = labelManager.freshLabel("endIf");
        /**
         * Code generation condition
         */
        String loaded_cond = condition.codeGeneration(labelManager);
        codeGenerated.append(loaded_cond).append("\n");
        codeGenerated.append("bc $a0 ").append(then_branch).append("\n");

        /**
         * Code generation else
         */
        if(elseBranch != null){
            String loaded_el = elseBranch.codeGeneration(labelManager);
            codeGenerated.append(loaded_el);
        }
        codeGenerated.append("b ").append(end_label).append("\n");


        /**
         * Code generation then
         */
        codeGenerated.append(then_branch).append(":\n");
        String loaded_th = thenBranch.codeGeneration(labelManager);
        codeGenerated.append(loaded_th).append("\n");

        /**
         * Append end_if_label_count
         */
        codeGenerated.append(end_label).append(":\n");


        return codeGenerated.toString();
    }

    @Override
    public List<Dereferences> variables() {
        List<Dereferences> c = condition.variables();
        List<Dereferences> t = ((MetaNode) thenBranch).variables();
        List<Dereferences> e = null;
        if(elseBranch!=null)
             e = ((MetaNode) elseBranch).variables();

        c.addAll(t);
        if(e!=null)
            c.addAll(e);
        return c;
    }


    public String toPrint(String s) {
        String print = s+"If\n" + condition.toPrint(s+"  ")
                + thenBranch.toPrint(s+"  ");
        if (elseBranch != null)
            print = print + elseBranch.toPrint(s+"  ");
        return print;

    }
}  