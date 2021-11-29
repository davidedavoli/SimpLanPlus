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
    import semantic.SimplanPlusException;

public class VarNode extends MetaNode {

    private IdNode id;
    private TypeNode type;
    private Node exp;

    public VarNode (IdNode i, TypeNode t, Node v) {
        id=i;
        type=t;
        exp=v;
    }


    //  @Override
    //  public ArrayList<SemanticError> delTypeCheck(DelEnv env, int nl) {
    //
    //	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
    //
    //	  if (!(type instanceof PointerTypeNode)) {
    //		HashSet<Pair<String, Integer>> tmp = new HashSet<Pair<String, Integer>>();
    //		tmp.add(new Pair<String, Integer>(id, nl));
    //		env.add(new AliasingDomain(tmp, new DelList()));
    //		res.addAll(exp.delTypeCheck(env, nl));
    //	  }
    //	  else {
    //		HashSet<Pair<String, Integer>> tmp = new HashSet<Pair<String, Integer>>();
    //		tmp.add(new Pair<String, Integer>(id, nl));
    //
    //		DelList l = new DelList(((PointerTypeNode)type).getDerefLevel());
    //
    //		if ((exp instanceof NewNode)) {
    //			l.put(0,new DelEffType(DelEffType.DelT.NIL));// statement del tipo ^type = exp dovrebbero essere consentiti solo se exp è new ^type.
    //		}
    //		else {
    //			res.add(new SemanticError("Errore grave! non dovremmo nemmeno entrare in questo ramo"));
    //		}
    //	  }
    //
    //	  return res;
    //  }

    public String toPrint(String s) throws SimplanPlusException {
        return s+"Var:" + id.getID() +"\n"
            +type.toPrint(s+"  ")
            +((exp==null)?"":exp.toPrint(s+"  "));
    }

    public HasReturn retTypeCheck() {
    return new HasReturn(HasReturn.hasReturnType.ABS);
    }


    //valore di ritorno non utilizzato
    public TypeNode typeCheck () throws SimplanPlusException {
        if (exp != null && ! (TypeUtils.isSubtype(exp.typeCheck(),type)) ){
            System.out.println("incompatible value for variable "+id.getID());
            System.exit(0);
        }
        return type;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
        StringBuilder cgen = new StringBuilder();
        if(exp != null){
            cgen.append(exp.codeGeneration(labelManager)).append("\n");
            cgen.append("push $a0\n");
        }
        else{
            /**
            * Decidere se pushare 0 per dire che non c'è nulla o alzare solamente lo stack pointer
            */
            cgen.append("subi $sp $sp 1 // non assegnato nulla\n");
            //cgen.append("push 0\n");
        }
        return cgen.toString();
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
        //create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        //env.offset = -2;
        HashMap<String, STentry> hm = env.getCurrentST();

        //return offset and decrement it by 1
        int new_offset = env.decOffset();
        STentry entry = new STentry(env.getNestingLevel(), type, new_offset); //separo introducendo "entry"
        id.setEntry(entry);

        if (exp != null){
            res.addAll(exp.checkSemantics(env));
            //  dereferenceLevel = 0 because we set the status of a variable
            id.setIdStatus(new Effect(Effect.READWRITE), 0);
        }

        if ( hm.put(id.getID(),entry) != null )
            res.add(new SemanticError("Var id '"+id+"' already declared"));

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