package ast.node.types;

public class HasReturn {
	
	public enum hasReturnType {ABS, PRES}
	private final hasReturnType val;
	
	public hasReturnType getVal() {
		return val;
	}
	
	public HasReturn(hasReturnType t) {
		this.val=t;
	}
	
	public static HasReturn min(HasReturn a, HasReturn b) {
			if (a.leq(b))
				return a;
			else
				return b;		
		}
	
	public static HasReturn max(HasReturn a, HasReturn b) {
		if (a.leq(b))
			return b;
		else
			return a;		
	}
	
	public boolean leq(HasReturn t ) {
		switch (this.val) {
			case ABS:
				return true;
			case PRES:
				return t.getVal()== hasReturnType.PRES;
		}
		return false;
	}
  
}  