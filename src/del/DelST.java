package del;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class DelST {
	
	private ArrayList<HashMap<String, DelEffType>> envl;
	
	public DelST() {
		envl= new ArrayList<HashMap<String, DelEffType>>();
	}
	
	public DelST push(HashMap<String, DelEffType> hm) {
		envl.add(hm);
		return this;
	}
	
	public DelEffType insert(String k, DelEffType v) {
		if (envl.size()==0) {
			envl.add(new HashMap<String, DelEffType>());
		}
		envl.get(0).put(k, v);
		return v;
	}

	public void update(HashMap<String, DelEffType> hm) {
		for (Entry<String, DelEffType> i: hm.entrySet()) {
			for (HashMap<String, DelEffType> j: envl) {
				if(j.containsKey(i.getKey())) {
					j.put(i.getKey(), i.getValue());
				}
			}
		}
	}

	public DelST max(HashMap<String, DelEffType> hm) {
		for  (Entry<String, DelEffType> i: hm.entrySet())
			for (HashMap<String, DelEffType> j: envl) {
				if (j.containsKey(i.getKey())) {
					j.put(i.getKey(), DelEffType.max(j.get(i.getKey()), hm.get(i.getKey())));
					break;
				}
			}
		return this;
	}

	public DelST seq(HashMap<String, DelEffType> hm) {
		for  (Entry<String, DelEffType> i: hm.entrySet())
			for (HashMap<String, DelEffType> j: envl) {
				if (j.containsKey(i.getKey())) {
					j.put(i.getKey(), j.get(i.getKey()).seq(hm.get(i.getKey())));
					break;
				}
			}
		return this;
	}


}
