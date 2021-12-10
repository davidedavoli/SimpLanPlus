package effect;

public class EffectError {

	public final String msg;

	public EffectError(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		
		return msg;
	}
}
