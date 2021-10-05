package semanticAnalysis;

public class Pair<X, Y>  {
	public final X _1; 
	public final Y _2; 
	
	public Pair() { 
		this._1 = null; 
		this._2 = null; 
	} 
	
	public Pair(X x, Y y) { 
		this._1 = x; 
		this._2 = y; 
	} 
	
	public X _1() {
		return _1;
	}

	public Y _2() {
		return _2;
	}

}
