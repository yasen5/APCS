package util;

public abstract class HashCode {
    public static int realHashCode(Object o) {
        if (o.getClass().isEnum()) {
            Enum obj = (Enum) o;
            return obj.ordinal();
        } else {
            return o.hashCode();
        }
    }
}
