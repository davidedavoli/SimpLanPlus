package ast.node.exp;

import ast.node.Node;
import ast.node.types.BoolTypeNode;
import ast.node.types.IntTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;
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
        
        if (lhsType != rhsType) {
            System.out.println("OPERANDI DI TIPO DIVERSO, LANCIARE ECCEZIONE");
            return null;
        }
        
        
        /**
        * +, -, *, / <,<=,>,>=: solo int
        * ==,!= : solo o tra int o tra bool
        * &&, ||, ! : solo tra bool
        * */
        
        switch (operator) {
            /**
             * Math operator
             */
            case "+":
            case "-":
            case "*":
            case "/":
            case "<":
            case "<=":
            case ">":
            case ">=":
                // Both must be integer type
                if (!(lhsType instanceof IntTypeNode)) {
                    System.out.println("Gli operandi non sono int, lanciare un'eccezione");
                }
                return new IntTypeNode();
            /**
             * Bool operator
             * These can be done with bool and Int
             * 
             */
            case "==":
            case "!=":
                if (!(lhsType instanceof IntTypeNode)) {
                    return new BoolTypeNode();
                }
                else {
                    return new IntTypeNode();
                }
                

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
        cgen.append("//Strart codegen of ").append(lhs.toString()).append(operator).append(rhs.toString())
                .append("\n");
        /**
         * Cgen for lhs and rhs to push them on the stack
         */
        String lhs_generated = lhs.codeGeneration(labelManager);
        cgen.append(lhs_generated);
        cgen.append("push $a0 // push e1\n");
        String rhs_generated = rhs.codeGeneration(labelManager);
        cgen.append(rhs_generated);
        cgen.append("lw $t1 0($sp) take e2 and $t1 take e1\n");
        cgen.append("pop // remove e1 from the stack to preserve stack\n");

        /**
         * $t1(=e1) operation $a0(=e2)
         */

        switch (operator) {
            case "+":{
                cgen.append("add $a0 $t1 $a0 // a0 = t1+a0\n");
            }
            case "-": {
                cgen.append("sub $a0 $t1 $a0 // a0 = t1-a0\n");
                break;
            }
            case "*": {
                cgen.append("mult $a0 $t1 $a0 // a0 = t1+a0\n");
                break;
            }
            case "/": {
                cgen.append("div $a0 $t1 $a0 // a0 = t1/a0\n");
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
                cgen.append("le $a0 $t1 $a0 // $a0 = $t1 <= $a0");
                break;
            }
            case "<":{
                cgen.append("lt $a0 $t1 $a0 // $a0 = $t1 < $a0");
                break;
            }
            case ">":{
                cgen.append("gt $a0 $t1 $a0 // $a0 = $t1 > $a0");
                break;
            }
            case ">=":{
                cgen.append("ge $a0 $t1 $a0 // $a0 = $t1 >= $a0");
                break;
            }
            case "==":{
                cgen.append("eq $a0 $t1 $a0 // $a0 = $t1 == $a0");
                break;
            }
            case "!=":{
                cgen.append("eq $a0 $t1 $a0 // $a0 = $t1 == $a0");
                cgen.append("not $a0 $a0 // $a0 = !$a0");
                break;
            }
            case "&&":{
                cgen.append("and $a0 $t1 $a0 // $a0 = $t1 && $a0");
                break;
            }

            case "||":{
                cgen.append("or $a0 $t1 $a0 // $a0 = $t1 || $a0");
                break;
            }
           /* case "<":{
                String true_label = labelManager.freshLabel("is_less_branch");
                String end_true_label = "end_of_" + true_label;

                String boolCondition = "blr $t1 $a0 ";
                String comment = "// jump to label if $t1 < $a0 \n";

                makeBoolExp(cgen, boolCondition, true_label, end_true_label, comment);
                
                break;
            }
            case ">":{
                String true_label = labelManager.freshLabel("is_high_branch");
                String end_true_label = "end_of_" + true_label;

                String boolCondition = "brr $t1 $a0 ";
                String comment = "// jump to label if $t1 > $a0 \n";

                makeBoolExp(cgen, boolCondition, true_label, end_true_label, comment);
                
                break;
            }
            case ">=":{
                String true_label = labelManager.freshLabel("is_high_equal_branch");
                String end_true_label = "end_of_" + true_label;

                String boolCondition = "breq $t1 $a0 ";
                String comment = "// jump to label if $t1 >= $a0 \n";

                makeBoolExp(cgen, boolCondition, true_label, end_true_label, comment);
                break;
            }
            case "==": {
                String true_label = labelManager.freshLabel("is_equal_branch");
                String end_true_label = "end_of_" + true_label;

                String boolCondition = "beq $t1 $a0 ";
                String comment = "// jump to label if $t1 == $a0 \n";

                makeBoolExp(cgen, boolCondition, true_label, end_true_label, comment);

                break;
            }
            case "!=": {
                String true_label = labelManager.freshLabel("is_not_equal_branch");
                String end_true_label = "end_of_" + true_label;

                String boolCondition = "bneq $t1 $a0 ";
                String comment = "// jump to label if $t1 != $a0 \n";

                makeBoolExp(cgen, boolCondition, true_label, end_true_label, comment);

                break;
            }

            case "&&": {
                String true_label = labelManager.freshLabel("true_and_branch");
                String end_true_label = "end_of_" + true_label;

                String boolCondition = "and $t1 $a0 ";
                String comment = "// jump to label if $t1 && $a0 \n";

                makeBoolExp(cgen, boolCondition, true_label, end_true_label, comment);

                break;
            }

            case "||": {
                String true_label = labelManager.freshLabel("true_or_branch");
                String end_true_label = "end_of_" + true_label;

                String boolCondition = "or $t1 $a0 ";
                String comment = "// jump to label if $t1 || $a0 \n";

                makeBoolExp(cgen, boolCondition, true_label, end_true_label, comment);

                break;
            }*/

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
        ArrayList<SemanticError> errors = new ArrayList<>();

        errors.addAll(this.lhs.checkSemantics(env));
        errors.addAll(this.rhs.checkSemantics(env));

        return errors;
    }

    @Override
    public RetEffType retTypeCheck() {
        return null;
    }


    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return null;
    }
}
