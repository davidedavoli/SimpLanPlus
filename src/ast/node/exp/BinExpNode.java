package ast.node.exp;

import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.BoolTypeNode;
import ast.node.types.IntTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;
import java.util.ArrayList;

/**
 * Class to handle math expression. These operations are permitted only on Integers value
 */

public class BinExpNode implements Node {
    private final Node lhs;
    private final String operator;
    private final Node rhs;

    public BinExpNode(Node lhs,String operator, Node rhs) {
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

        if(!(lhsType instanceof IntTypeNode && rhsType instanceof IntTypeNode)){
            System.out.println("NOT INT OPERANT");
            if(!(lhsType instanceof BoolTypeNode && rhsType instanceof BoolTypeNode)){
                System.out.println("OPERANDI DI TIPO DIVERSO, LANCIARE ECCEZIONE");
                return null;
            }
            else
                System.out.println("BOOL OPERAND");
        }
        else
            System.out.println("INT OPERAND");


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
                    System.out.println("Gli operandi non sono int, lanciare un'eccezione");
                }
                return new IntTypeNode();

            case "<":
            case "<=":
            case ">":
            case ">=":
                if (!(lhsType instanceof IntTypeNode)) {
                    System.out.println("Gli operandi non sono int, lanciare un'eccezione");
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
                    System.out.println("Gli operandi non sono bool, lanciare un'eccezione");
                }
                return new BoolTypeNode();
        }
        /**
         * Lo switch copre tutti i casi
         */
        return null;
    }

    @Override
    public String codeGeneration(Label labelManager) {

        StringBuilder cgen = new StringBuilder();

        cgen.append("//Start codegen of ").append(lhs.typeCheck().getClass().getName()).append(operator).append(rhs.typeCheck().getClass().getName())
                .append("\n");
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

    /*private void makeBoolExp(StringBuilder cgen, String boolCondition, String trueLabel, String endLabel, String comment){

        cgen.append(boolCondition).append(trueLabel).append(comment); 
        // eq $a0 $t1 $a0 //a0 = t1==a0
        // minus $a0 $t1 $a0 //a0 = t1<a0


        //CORPO FALSE BRANCH
        cgen.append("li $a0 0 // load false result in $a0 \n");
        cgen.append("b ").append(endLabel).append("\n");

        //CORPO TRUE BRANCH
        cgen.append(trueLabel).append(":\n");
        cgen.append("li $a0 1 // load true result in $a0 \n");
        cgen.append(endLabel).append(":\n");

    }
    */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        ArrayList<SemanticError> binExpNodeErrors = new ArrayList<>();

        binExpNodeErrors.addAll(lhs.checkSemantics(env));
        binExpNodeErrors.addAll(rhs.checkSemantics(env));

        return binExpNodeErrors;
    }

    @Override
    public RetEffType retTypeCheck(FunNode funNode) {
        return null;
    }
}
