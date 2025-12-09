package src;
import java.io.Serializable;

public class MyHashSet<E> implements Serializable {
    private Object[] hashArray;
    private int size;

    public MyHashSet() {
        hashArray = new Object[10000];
        size = 0;
    }

    public boolean add(E obj) {
        if (contains(obj)) {
            return false;
        } else {
            int hash = -1;
            if (obj.getClass().isEnum()) {
                Enum en = (Enum) obj;
                hash = en.ordinal();
            } else {
                hash = obj.hashCode();
            }
            hashArray[hash] = obj;
            size++;
        }
        return true;
    }

    public void clear() {
        size = 0;
        hashArray = new Object[128];
    }

    public boolean contains(Object obj) {
        int hash = -1;
        if (obj.getClass().isEnum()) {
            Enum en = (Enum) obj;
            hash = en.ordinal();
        } else {
            hash = obj.hashCode();
        }
        if (hashArray[hash] != null && obj.equals(hashArray[hash])) {
            return true;
        }
        return false;
    }

    public boolean remove(Object obj) {
        if (contains(obj)) {
            int hash = -1;
            if (obj.getClass().isEnum()) {
                Enum en = (Enum) obj;
                hash = en.ordinal();
            } else {
                hash = obj.hashCode();
            }
            hashArray[hash] = null;
            size--;
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public MyDLList<E> toDLList() {
        MyDLList<E> dll = new MyDLList<E>();
        for (int i = 0; i < hashArray.length; i++) {
            if (hashArray[i] != null) {
                dll.add((E) hashArray[i]);
            }
        }
        return dll;
    }
}