package ast.node.dec;

import ast.STentry;
import ast.node.IdNode;
import ast.node.MetaNode;
import ast.node.Node;
    import ast.node.types.HasReturn;
    import ast.node.types.TypeNode;

    import java.util.ArrayList;
    import java.util.HashMap;

    import ast.node.types.TypeUtils;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
    import ast.Label;
    import semantic.SemanticError;

public class VarNode extends MetaNode {

    private final IdNode id;
    private final TypeNode type;
    private final Node exp;

    public VarNode (IdNode i, TypeNode t, Node v) {
        id=i;
        type=t;
        exp=v;
    }


    public String toPrint(String s) {
        return s+"Var:" + id.getID() +"\n"
            +type.toPrint(s+"  ")
            +((exp==null)?"":exp.toPrint(s+"  "));
    }

    public HasReturn retTypeCheck() {
    return new HasReturn(HasReturn.hasReturnType.ABS);
    }

    public TypeNode typeCheck() {
        if (exp != null && ! (TypeUtils.isSubtype(exp.typeCheck(),type)) ){
            System.err.println("Incompatible value in assignment for variable "+id.getID() + " of type: "+id.typeCheck().toPrint("") + " when exp is of type: "+exp.typeCheck().toPrint(""));
            //System.err.println("Incompatible value for variable "+id.getID());
            System.exit(0);
        }
        return type;
    }

    public String codeGeneration(Label labelManager) {
        StringBuilder codeGenerated = new StringBuilder();
        if(exp != null){
            codeGenerated.append(exp.codeGeneration(labelManager)).append("\n");
            codeGenerated.append("push $a0\n");
        }
        else{
            codeGenerated.append("subi $sp $sp 1 // No value assigned\n");
        }
        return codeGenerated.toString();
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        ArrayList<SemanticError> res = new ArrayList<>();

        HashMap<String, STentry> hm = env.getCurrentST();

        //return offset and decrement it by 1
        int new_offset = env.decOffset();
        STentry entry = new STentry(env.getNestingLevel(), type, new_offset);
        id.setEntry(entry);

        if (exp != null){
            res.addAll(exp.checkSemantics(env));
            //  dereferenceLevel = 0 because we set the status of a variable
            id.setIdStatus(new Effect(Effect.READWRITE), 0);
        }

        if ( hm.put(id.getID(),entry) != null )
            res.add(new SemanticError("Var id '"+id.getID()+"' already declared"));

        return res;
    }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();
        if(exp != null){
            errors.addAll(exp.checkEffects(env));
        }
        env.addEntry(id.getID(), id.getEntry());
        return errors;
    }

}