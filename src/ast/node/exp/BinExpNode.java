package ast.node.exp;

import ast.Dereferenceable;
import ast.node.types.BoolTypeNode;
import ast.node.types.IntTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

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
                //throw new SimplanPlusException("Operands are of different type");


        /**
        * +, -, *, / <,<=,>,>=: solo int
        * ==,!= : solo o tra int o tra bool
        * &&, ||, ! : solo tra bool
        * */

        switch (operator) {
            /**
             * Math operator da cambiare
             */
            case "+":
            case "-":
            case "*":
            case "/":
                // Both must be integer type
                if (!(lhsType instanceof IntTypeNode)) {
                    System.err.println("Operands are not int in math operator ([ + | - | * | / ])");
                    System.exit(0);
                }
                    //throw new SimplanPlusException("Operands are not int in division");

                return new IntTypeNode();

            case "<":
            case "<=":
            case ">":
            case ">=":
                if (!(lhsType instanceof IntTypeNode)) {
                    System.err.println("Operands are not int in >=");
                    System.exit(0);
                    //throw new SimplanPlusException("Operands are not int in >=");
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
                    //throw new SimplanPlusException("Operands are not bool in division");
                }
                return new BoolTypeNode();
        }
        /**
         * Lo switch copre tutti i casi
         */
        return null;
    }

    @Override
    public String codeGeneration(Label labelManager) throws SimplanPlusException {

        StringBuilder cgen = new StringBuilder();

        cgen.append("//Start codegen of ").append(lhs.getClass().getName()).append(operator).append(rhs.getClass().getName()).append("\n");
        /**
         * Cgen for lhs and rhs to push them on the stack
         */
        String lhs_generated = lhs.codeGeneration(labelManager);
        cgen.append(lhs_generated);

        cgen.append("push $a0 // push e1\n");
        String rhs_generated = rhs.codeGeneration(labelManager);

        cgen.append(rhs_generated);

        cgen.append("lw $a2 0($sp) //take e2 and $a2 take e1\n");
        cgen.append("pop // remove e1 from the stack to preserve stack\n");

        /**
         * $a2(=e1) operation $a0(=e2)
         */

        switch (operator) {
            case "+":{
                cgen.append("add $a0 $a2 $a0 // a0 = t1+a0\n");

                break;
            }
            case "-": {
                cgen.append("sub $a0 $a2 $a0 // a0 = t1-a0\n");
                break;
            }
            case "*": {
                cgen.append("mult $a0 $a2 $a0 // a0 = t1+a0\n");
                break;
            }
            case "/": {
                cgen.append("div $a0 $a2 $a0 // a0 = t1/a0\n");
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
                cgen.append("le $a0 $a2 $a0 // $a0 = $a2 <= $a0\n");
                break;
            }
            case "<":{
                cgen.append("lt $a0 $a2 $a0 // $a0 = $a2 < $a0\n");
                break;
            }
            case ">":{
                cgen.append("gt $a0 $a2 $a0 // $a0 = $a2 > $a0\n");
                break;
            }
            case ">=":{
                cgen.append("ge $a0 $a2 $a0 // $a0 = $a2 >= $a0\n");
                break;
            }
            case "==":{
                cgen.append("eq $a0 $a2 $a0 // $a0 = $a2 == $a0\n");
                break;
            }
            case "!=":{
                cgen.append("eq $a0 $a2 $a0 // $a0 = $a2 == $a0\n");
                cgen.append("not $a0 $a0 // $a0 = !$a0\n");
                break;
            }
            case "&&":{
                cgen.append("and $a0 $a2 $a0 // $a0 = $a2 && $a0\n");
                //cgen.append("mult $a0 $a2 $a0 // $a0 = $a2 && $a0 aka $a0 = $a2 * $a0\n");
                break;
            }

            case "||":{
                cgen.append("or $a0 $a2 $a0 // $a0 = $a2 || $a0\n");
                break;
            }
           
            /**
             * Case of == and != to implement on boolean expression
             */
        }
        return cgen.toString();
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
        ArrayList<SemanticError> binExpNodeErrors = new ArrayList<>();

        binExpNodeErrors.addAll(lhs.checkSemantics(env));
        binExpNodeErrors.addAll(rhs.checkSemantics(env));

        return binExpNodeErrors;
    }
    @Override
    public List<Dereferenceable> variables() {
        List<Dereferenceable> variables = new ArrayList<>();

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
