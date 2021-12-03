package GraphEffects;

import javax.swing.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetWithIdentity<T extends WithId> implements Set<T> {

    private HashSet<T> set= new HashSet<>();

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (set.size()==0)
            return false;
        if (!(o instanceof WithId))
            return false;
        for (T t: set){
            if(t.getId()==((T)o).getId()){
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(T t) {
        if (this.contains(t))
            return false;
        else
            return set.add(t);
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof WithId))
            return false;
        for (T t: set){
            if(t.getId()==((T)o).getId()){
                return set.remove(t);
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean res=true;
        boolean res1;
        for(Object o: c){
            res1 = this.contains(o);
            res = res1 && res;
        }
        return res;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean b = true;
        boolean b1;
        for (T t: c){
             b1 = this.add(t);
             b = b && b1;
        }
        return b;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        HashSet<T> new_set = new HashSet<>();
        for (Object o: c) {
            if(!(o instanceof WithId))
                continue;
            for (T t : set) {
                if (t.getId()== ((T) o).getId())
                    new_set.add(t);
            }
        }
        this.set=new_set;
        return new_set.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean b = false;
        boolean b1= false;
        for (Object i: c)
            b1 = this.remove(c);
            b = b || b1;
        return b;
    }

    @Override
    public void clear() {
        set.clear();
    }

    public T get(T a){
        for (T t: set){
            if (t.getId()==a.getId()){
                return t;
            }
        }
        return null;
    }

    public SetWithIdentity<T> union(SetWithIdentity<T> a){
        SetWithIdentity<T> new_set = new SetWithIdentity<T>();
        new_set.addAll(this);
        new_set.addAll(a);
        return new_set;
    }

    public SetWithIdentity<T> difference(SetWithIdentity<T> a){
        SetWithIdentity<T> new_set = new SetWithIdentity<T>();
        new_set.addAll(this);
        new_set.removeAll(a);
        return new_set;
    }

    @Override
    public String toString(){
        return set.toString();
    }
}
