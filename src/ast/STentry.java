package ast;

import ast.node.types.TypeNode;
import util.SimplanPlusException;

public class STentry {
 
  private final int nl;
  private TypeNode type;
  private final int offset;

  public String getBeginFuncLabel() {
    return beginFuncLabel;
  }

  public String getEndFuncLabel() {
    return endFuncLabel;
  }

  private String beginFuncLabel = "";
  private String endFuncLabel = "";
  
  public STentry (int n, int os)
  {nl=n;
  offset=os;} 
   
  public STentry (int n, TypeNode t, int os)
  {nl=n;
   type=t;
   offset=os;}

  public STentry (int n, int os, String bFL, String eFL) {
    nl=n;
    offset=os;
    beginFuncLabel= bFL;
    endFuncLabel = eFL;
  }
  
  public void addType (TypeNode t)
  {type=t;}
  
  public TypeNode getType ()
  {return type;}

  public int getOffset ()
  {return offset;}
  
  public int getNestinglevel ()
  {return nl;}
  
  public String toPrint(String s) throws SimplanPlusException { //
	   return s+"STentry: nestlev " + Integer.toString(nl) +"\n"+
			  s+"STentry: type\n" + 
			  type.toPrint(s+"  ") + 
		      s+"STentry: offset " + Integer.toString(offset) + "\n";
  }
}  