package ast.node.exp;

import ast.Dereferences;
import ast.node.types.BoolTypeNode;
import ast.node.types.IntTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle math expression. These operations are permitted only on Integers value
 */

public class BinExpNode extends ExpNode {
    private final ExpNode lhs;
    private final String operator;
    private final ExpNode rhs;

    public BinExpNode(ExpNode lhs, String operator, ExpNode rhs) {
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }

    @Override
    public String toPrint(String indent) {
        return indent + lhs + " " + operator + " " + rhs.toPrint(indent);
    }

    @Override
    public TypeNode typeCheck() {
        TypeNode lhsType = lhs.typeCheck();
        TypeNode rhsType = rhs.typeCheck();

        if(!(lhsType instanceof IntTypeNode && rhsType instanceof IntTypeNode))
            if(!(lhsType instanceof BoolTypeNode && rhsType instanceof BoolTypeNode)){
                System.err.println("Operands are of different type");
                System.exit(0);
            }

        switch (operator) {
            case "+":
            case "-":
            case "*":
            case "/":
                // Both must be integer type
                if (!(lhsType instanceof IntTypeNode)) {
                    System.err.println("Operands are not int in math operator ([ + | - | * | / ])");
                    System.exit(0);
                }
                return new IntTypeNode();

            case "<":
            case "<=":
            case ">":
            case ">=":
                if (!(lhsType instanceof IntTypeNode)) {
                    System.err.println("Operands are not int in ([ >= | > | < | <= ])");
                    System.exit(0);
                }
                return new BoolTypeNode();
            /**
             * Bool operator
             * These can be done with bool and Int
             *
             */
            case "==":
            case "!=":
                return new BoolTypeNode();

            case "&&":
            case "||":
                if(!(lhsType instanceof  BoolTypeNode)){
                    System.err.println("Operands are not bool in [ or(||) | and[&&] ");
                    System.exit(0);
                }
                return new BoolTypeNode();
        }
        /**
         * Should not arrive here.
         */
        return null;
    }

    @Override
    public String codeGeneration(Label labelManager) {

        StringBuilder codeGenerated = new StringBuilder();

        codeGenerated.append("//Start codegen of ").append(lhs.getClass().getName()).append(operator).append(rhs.getClass().getName()).append("\n");
        /**
         * Cgen for lhs and rhs to push them on the stack
         */
        String lhs_generated = lhs.codeGeneration(labelManager);
        codeGenerated.append(lhs_generated);

        codeGenerated.append("push $a0 // push e1\n");
        String rhs_generated = rhs.codeGeneration(labelManager);

        codeGenerated.append(rhs_generated);

        codeGenerated.append("lw $a2 0($sp) //take e2 and $a2 take e1\n");
        codeGenerated.append("pop // remove e1 from the stack to preserve stack\n");

        /**
         * $a2(=e1) operation $a0(=e2)
         */

        switch (operator) {
            case "+":{
                codeGenerated.append("add $a0 $a2 $a0 // a0 = t1+a0\n");

                break;
            }
            case "-": {
                codeGenerated.append("sub $a0 $a2 $a0 // a0 = t1-a0\n");
                break;
            }
            case "*": {
                codeGenerated.append("mult $a0 $a2 $a0 // a0 = t1+a0\n");
                break;
            }
            case "/": {
                codeGenerated.append("div $a0 $a2 $a0 // a0 = t1/a0\n");
                break;
            }
            /*
            * le
            * lt
            * gt
            * ge
            * eq
            * */
            case "<=":{
                codeGenerated.append("le $a0 $a2 $a0 // $a0 = $a2 <= $a0\n");
                break;
            }
            case "<":{
                codeGenerated.append("lt $a0 $a2 $a0 // $a0 = $a2 < $a0\n");
                break;
            }
            case ">":{
                codeGenerated.append("gt $a0 $a2 $a0 // $a0 = $a2 > $a0\n");
                break;
            }
            case ">=":{
                codeGenerated.append("ge $a0 $a2 $a0 // $a0 = $a2 >= $a0\n");
                break;
            }
            case "==":{
                codeGenerated.append("eq $a0 $a2 $a0 // $a0 = $a2 == $a0\n");
                break;
            }
            case "!=":{
                codeGenerated.append("eq $a0 $a2 $a0 // $a0 = $a2 == $a0\n");
                codeGenerated.append("not $a0 $a0 // $a0 = !$a0\n");
                break;
            }
            case "&&":{
                codeGenerated.append("and $a0 $a2 $a0 // $a0 = $a2 && $a0\n");
                //codeGenerated.append("mult $a0 $a2 $a0 // $a0 = $a2 && $a0 aka $a0 = $a2 * $a0\n");
                break;
            }

            case "||":{
                codeGenerated.append("or $a0 $a2 $a0 // $a0 = $a2 || $a0\n");
                break;
            }
           
            /**
             * Case of == and != to implement on boolean expression
             */
        }
        return codeGenerated.toString();
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        ArrayList<SemanticError> binExpNodeErrors = new ArrayList<>();

        binExpNodeErrors.addAll(lhs.checkSemantics(env));
        binExpNodeErrors.addAll(rhs.checkSemantics(env));

        return binExpNodeErrors;
    }
    @Override
    public List<Dereferences> variables() {
        List<Dereferences> variables = new ArrayList<>();

        variables.addAll(lhs.variables());
        variables.addAll(rhs.variables());

        return variables;
    }
    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();

        errors.addAll(lhs.checkEffects(env));
        errors.addAll(rhs.checkEffects(env));

        errors.addAll(checkExpStatus(env));

        return errors;
    }

    @Override
    public HasReturn retTypeCheck() {
        return null;
    }
}
