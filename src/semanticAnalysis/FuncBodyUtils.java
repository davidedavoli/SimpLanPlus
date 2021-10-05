package semanticAnalysis;


public class FuncBodyUtils {

  private static int funLabCount=0; 

  private static String funCode=""; 

  public static String freshFunLabel() { 
	return "function_"+(funLabCount++);
  } 
  
  public static void putCode(String c) { 
    funCode+="\n"+c; //aggiunge una linea vuota di separazione prima di funzione
  } 
  
  public static String getCode() { 
    return funCode;
  } 


}