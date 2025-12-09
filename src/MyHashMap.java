package src;
public class MyHashMap<K, V> {
    private Object[] hashArray;
    private int size;
    private MyHashSet<K> keySet;

    public MyHashMap() {
        hashArray = new Object[676];
        size = 0;
        keySet = new MyHashSet<>();
    }

    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        if (keySet.add(key)) {
            size++;
        }
        int hash = -1;
        if (key.getClass().isEnum()) {
            Enum obj = (Enum) key;
            hash = obj.ordinal();
        }
        else {
            hash = key.hashCode(); 
        }
        V ret = (V) hashArray[hash];
        hashArray[hash] = value;
        return ret;
    }

    @SuppressWarnings("unchecked")
    public V get(Object o) {
        int hash = -1;
        if (o.getClass().isEnum()) {
            Enum obj = (Enum) o;
            hash = obj.ordinal();
        }
        else {
            hash = o.hashCode(); 
        }
        return (V) hashArray[hash];
    }

    @SuppressWarnings("unchecked")
    public V remove(Object o) {
        int hash = -1;
        if (o.getClass().isEnum()) {
            Enum obj = (Enum) o;
            hash = obj.ordinal();
        }
        else {
            hash = o.hashCode(); 
        }
        V ret = (V) hashArray[hash];
        hashArray[hash] = null;
        if (keySet.remove(o)) {
            size--;
        }
        return ret;
    }

    public int size() {
        return size;
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }

    public String toString() {
        String str = "[";
        for (K key : keySet.toDLList()) {
            int hash = -1;
            if (key.getClass().isEnum()) {
                Enum obj = (Enum) key;
                hash = obj.ordinal();
            }
            else {
                hash = key.hashCode(); 
            }
            str += key + " - " + hashArray[hash] + ",";
        }
        str += "]";
        return str;
    }
}
