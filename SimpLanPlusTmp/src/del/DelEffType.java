package del;


public class DelEffType {

	public enum DelT {NIL, RW, DEL, ERR};
	private DelT val;


	public DelEffType(DelT t) {
		val=t;
	}

	public DelT getVal() {
		return val;
	}
	
	static DelEffType min(DelEffType a, DelEffType b) {
		if (a.leq(b))
			return a;
		else
			return b;		
	}

	static DelEffType max(DelEffType a, DelEffType b) {
		if (a.leq(b))
			return b;
		else
			return a;		
	}
	
	private int map(DelEffType v) {
		switch (v.getVal()) {
			case NIL:
				return 0;
			case RW:
				return 1;
			case DEL:
				return 2;
			case ERR:
				return 3;
		}
		return 4;
	}
	
	public boolean leq(DelEffType t) {
		return map(this)<=map(t);
	}
	
	public DelEffType seq(DelEffType t) {
		if (max(this, t).leq(new DelEffType(DelT.RW))) {
			return max(t, this);
		}
		else if((val==DelT.DEL && t.getVal()==DelT.NIL) || (this.leq(new DelEffType(DelT.RW)) && t.getVal()==DelT.DEL)) {
			return new DelEffType(DelT.DEL);
		}
		else return new DelEffType(DelT.ERR);
	}

	public DelEffType par(DelEffType t) {
		return max(this.seq(t), t.seq(this));
	}

}
