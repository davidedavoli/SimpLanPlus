package del;

import java.util.HashSet;
import java.util.Set;

import util.Pair;

public class DelEnv {
	
	private Set<AliasingDomain> set;
	
	public DelEnv() {
		set = new HashSet<AliasingDomain>();
	}
	
	public AliasingDomain find(Pair<String, Integer> p) {
		for (AliasingDomain i: set) {
			if (i.contains(p))
				return i;
		}
		return null;
	}
	
	public void add(AliasingDomain a) {
		set.add(a);
	}

}
