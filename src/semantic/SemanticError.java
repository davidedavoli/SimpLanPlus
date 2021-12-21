package semantic;

public class SemanticError {
	
	public final String msg;
	public final String id;
	
	public SemanticError(String msg, String id) {
		this.msg = msg;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return msg;
	}
}
