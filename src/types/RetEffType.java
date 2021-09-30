package types;

public class RetEffType  {
	
	public enum RetT {ABS, PRES};
	private RetT val;
	
	public RetT getVal() {
		return val;
	}
	
	public RetEffType(RetT t) {
		this.val=t;
	}
	
	public static RetEffType min(RetEffType a, RetEffType b) {
			if (a.leq(b))
				return a;
			else
				return b;		
		}
	
	public static RetEffType max(RetEffType a, RetEffType b) {
		if (a.leq(b))
			return b;
		else
			return a;		
	}
	
	public boolean leq(RetEffType t ) {
		switch (this.val) {
			case ABS:
				return true;
			case PRES:
				return t.getVal()==RetT.PRES;
		}
		return false;
	}
  
}  