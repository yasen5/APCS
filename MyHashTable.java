import java.io.Serializable;

public class MyHashTable<K, V> implements Serializable {
    private MyDLList<V>[] table;
    private MyHashSet<K> keySet;

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        table = new MyDLList[10000];
        this.keySet = new MyHashSet<>();
    }

    public void put(K key, V value) {
        keySet.add(key);
        if (table[key.hashCode()] == null) {
            table[key.hashCode()] = new MyDLList<>();
        }
        table[key.hashCode()].add(value);
    }

    public MyDLList<V> get(Object o) {
        return (MyDLList<V>) table[o.hashCode()];
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }

    public String toString() {
        String str = "[";
        for (K key : keySet.toDLList()) {
            str += "[";
            for (V value : table[key.hashCode()]) {
                str += "#" + key.hashCode() + " | " + key + " | " + value + ",";
            }
            str += "]";
        }
        str += "]";
        return str;
    }

    public void remove(K k, V v) {
        MyDLList<V> dll = table[k.hashCode()];
        dll.remove(v);
        if (dll.size() == 0) {
            keySet.remove(k);
            table[k.hashCode()] = null;
        }
        keySet.remove(v);
    }

    public void remove(K k) {
        table[k.hashCode()] = null;
        keySet.remove(k);
    }
}