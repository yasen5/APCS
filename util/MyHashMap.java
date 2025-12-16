package util;

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
        V ret = (V) hashArray[HashCode.realHashCode(key)];
        hashArray[HashCode.realHashCode(key)] = value;
        return ret;
    }

    @SuppressWarnings("unchecked")
    public V get(Object o) {
        return (V) hashArray[HashCode.realHashCode(o)];
    }

    @SuppressWarnings("unchecked")
    public V remove(Object o) {
        V ret = (V) hashArray[HashCode.realHashCode(o)];
        hashArray[HashCode.realHashCode(o)] = null;
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
            str += key + " - " + hashArray[HashCode.realHashCode(key)] + ",";
        }
        str += "]";
        return str;
    }
}
