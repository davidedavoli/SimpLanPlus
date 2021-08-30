package ast;

import java.util.ArrayList;

import types.BoolTypeNode;
import types.IntTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;
import util.SimpLanlib;

public class BinExpNode implements Node {

  private Node left;
  private String op;
  private Node right;
  
  public BinExpNode (Node l, String o,  Node r) {
    left=l;
    op = o;
    right=r;
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  //create the result
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  //check semantics in the left and in the right exp
	  
	  res.addAll(left.checkSemantics(env));
	  res.addAll(right.checkSemantics(env));
	  
 	  return res;
 	}
  
  public String toPrint(String s) {
    return s+op+"\n" + left.toPrint(s+"  ")  
                      + right.toPrint(s+"  ") ; 
  }
  
  public TypeNode typeCheck() {
	  TypeNode toret;
	  switch (op) {
	  	case "+":
	  		if (! ( SimpLanlib.isSubtype(left.typeCheck(),new IntTypeNode()) &&
	  				SimpLanlib.isSubtype(right.typeCheck(),new IntTypeNode()) ) ) {
	  			System.out.println("Non integers in sum");
	  			System.exit(0);
	  		}
  			toret =new IntTypeNode();
  			break;
	  	case "-":
	  		if (! ( SimpLanlib.isSubtype(left.typeCheck(),new IntTypeNode()) &&
	  				SimpLanlib.isSubtype(right.typeCheck(),new IntTypeNode()) ) ) {
	  			System.out.println("Non integers in difference");
	  			System.exit(0);
	  		}
  			toret =new IntTypeNode();
  			break;
	  	case "*":
	  		if (! ( SimpLanlib.isSubtype(left.typeCheck(),new IntTypeNode()) &&
	  				SimpLanlib.isSubtype(right.typeCheck(),new IntTypeNode()) ) ) {
	  			System.out.println("Non integers in multiplication");
	  			System.exit(0);
	  		}
  			toret =new IntTypeNode();
  			break;

	  	case "/":
	  		if (! ( SimpLanlib.isSubtype(left.typeCheck(),new IntTypeNode()) &&
	  				SimpLanlib.isSubtype(right.typeCheck(),new IntTypeNode()) ) ) {
	  			System.out.println("Non integers in division");
	  			System.exit(0);
	  		}
  			toret =new IntTypeNode();
  			break;
	  	case "<": case "<=": case ">": case ">=": case "==": case "!=":
	  		if (! ( SimpLanlib.isSubtype(left.typeCheck(),new IntTypeNode()) &&
	  				SimpLanlib.isSubtype(right.typeCheck(),new IntTypeNode()) ) ) {
	  			System.out.println("Non integers in comparison");
	  			System.exit(0);
	  		}
  			toret =new BoolTypeNode();
  			break;
	  	case "&&": case "||":
	  		if (! ( SimpLanlib.isSubtype(left.typeCheck(),new BoolTypeNode()) &&
	  				SimpLanlib.isSubtype(right.typeCheck(),new BoolTypeNode()) ) ) {
	  			System.out.println("Non integers in comparison");
	  			System.exit(0);
	  		}
  			toret =new BoolTypeNode();
  			break;

	  	default:
	  		//TODO darci un senso
	  		toret= null;
	  }
	  return toret;
		  
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

  
  public String codeGeneration() {
//		return left.codeGeneration()+right.codeGeneration()+"add\n";
	  String toret=left.codeGeneration()+right.codeGeneration();
	  String l1 = SimpLanlib.freshLabel(); 
	  String l2 = SimpLanlib.freshLabel();
	  switch (op) {
	  	case "+":
			toret +="add\n";
			break;
	  	case "-":
			toret +="sub\n";
			break;
	  	case "*":
			toret +="mult\n";
			break;
	  	case "/":
			toret += "div\n";
			break;
	  	case "==": //Copiato da EqualNode
	  	    toret +=
				   "beq "+ l1 +"\n"+
				   "push 0\n"+
				   "b " + l2 + "\n" +
				   l1 + ":\n"+
				   "push 1\n" +
				   l2 + ":\n";
			break;
	  	case "!=" :
  	    toret +=
			   "beq "+ l1 +"\n"+
			   "push 1\n"+
			   "b " + l2 + "\n" +
			   l1 + ":\n"+
			   "push 0\n" +
			   l2 + ":\n";
  	    break;
	  	case "<=": //cambiato beq in bleq
	  	    toret +=
				   "bleq "+ l1 +"\n"+
				   "push 0\n"+
				   "b " + l2 + "\n" +
				   l1 + ":\n"+
				   "push 1\n" +
				   l2 + ":\n";
	  	break;
	  	case ">"://negazione di de Morgan del caso precedente
	  	    toret +=
				   "bleq "+ l1 +"\n"+
				   "push 1\n"+
				   "b " + l2 + "\n" +
				   l1 + ":\n"+
				   "push 0\n" +
				   l2 + ":\n";
	  	break;
	  	case ">=":
	  	    toret +="sub\n"+ // a>=b sse a-b >=0 sse 0 <=b pere definizione
	  	    		"push 0 \n"+
				   "bleq "+ l1 +"\n"+
				   "push 0\n"+
				   "b " + l2 + "\n" +
				   l1 + ":\n"+
				   "push 1\n" +
				   l2 + ":\n";
	  	break;
	  	case "<": //negazione di de Morgan del caso precedente
	  	    toret +="sub\n"+ // a>=b sse a-b >=0 sse 0 <=b per definizione
	  	    		"push 0 \n"+
				   "bleq "+ l1 +"\n"+
				   "push 1\n"+
				   "b " + l2 + "\n" +
				   l1 + ":\n"+
				   "push 0\n" +
				   l2 + ":\n";
	  	break;
	  	case "&&":  //a && b == tt sse a+b == 2
	  		toret+="add\n"+
	  				"push 2\n"+
					"beq "+ l1 +"\n"+
					"push 0\n"+
					"b " + l2 + "\n" +
					l1 + ":\n"+
					"push 1\n" +
					l2 + ":\n";
	  	break;
	  	case "||": // a || b == tt sse 1 <= a+b
			toret +="add\n"+
					"push 1\n"+
					"bleq "+ l1 +"\n"+
					"push 0\n"+
					"b " + l2 + "\n" +
					l1 + ":\n"+
					"push 1\n" +
					l2 + ":\n";
	  	break;
	  	default:
	  		toret= "";
	  		break;
	  }
	  return toret;
  }
}  