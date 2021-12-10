package ast;


public class FuncBodyUtils {

  private static int funLabCount=0; 

  public static String freshFunLabel() { 
	return "Function"+(funLabCount);
  }

  public static String endFreshFunLabel() {
    String endLabel = "endFunction"+funLabCount;
    funLabCount = funLabCount + 1;
    return endLabel;

  }

}