package del;

import java.util.HashSet;
import java.util.Set;
import semantic.Pair;

public class AliasingDomain {
	
	private Set<Pair<String, Integer>> set;
	private DelList value;

	public AliasingDomain() {
		set = new HashSet<>();
		value = new DelList();		
	}

	public AliasingDomain(DelList v) {
		set = new HashSet<>();
		value = v;		
	}
	
	public AliasingDomain(Set<Pair<String, Integer>> s) {
		set = s;
		value = new DelList();		
	}

	public AliasingDomain(Set<Pair<String, Integer>> s, DelList v) {
		set = s;
		value = v;		
	}
	
	public void setValue(DelList v) {
		value = v;
	}
	
	public void addName(Pair<String, Integer> p) {
		set.add(p);
	}

	public void remName(Pair<String, Integer> p) {
		set.remove(p);
	}
	
	public DelList getValue() {
		return value;
	}
	
	public void remNestingLevel(int nl) {
		Set<Pair<String, Integer>> tmp = new HashSet<Pair<String, Integer>>();
		
		for (Pair<String, Integer> i: set) {
			if (i._2() >= nl)
				tmp.add(i);
		}
		
		set.removeAll(tmp);
	}
	
	public boolean contains(Pair<String, Integer> p) {
		for (Pair<String, Integer> t: set) {
			if (p._1() == t._1() && p._1() == t._1())
				return true;
		}
		return false;
	}
}
