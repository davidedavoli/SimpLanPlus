package del;

import java.util.ArrayList;

public class DelList {
	
	private ArrayList<DelEffType> list;
	
	public DelList() {
		list = new ArrayList<DelEffType>();
	}
	
	public DelList(int n) {
		list = new ArrayList<DelEffType>();
		for (int i=0; i< n; i++) {
			list.add(new DelEffType(DelEffType.DelT.DEL));
		}
	}
	
	public DelList(ArrayList<DelEffType> l) {
		list=l;
	}

	static DelList max(DelList a, DelList b) {
		DelList tmp=new DelList();
		for (int i=0; i< Integer.max(a.size(), b.size()); i++) {
			tmp.put(i, DelEffType.max(a.get(i), b.get(i)));
		}
		return tmp;
	}

	public DelEffType get(int n) {
		if(n <= list.size()) {
			return list.get(n);
		}
		else return new DelEffType(DelEffType.DelT.DEL);
	}
	
	public int size() {
		return list.size();
	}
	
	public DelEffType put(int n, DelEffType d) {
		DelEffType tmp;
		if(n <= list.size()) {
			tmp= list.get(n);
			list.add(n, d);
			return tmp;
		}
		else
			return null;
	}
	
	public DelList dereference() {
		ArrayList<DelEffType> list2 = (ArrayList<DelEffType>) list.clone();
		list2.remove(0);
		return new DelList(list2);
	}
}
