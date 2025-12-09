package src;

public class CustomEnum {
    private Enum<?> e_num;

    public CustomEnum(Enum<?> e_num) {
        this.e_num = e_num;
    }
    
    @Override
    public int hashCode() {
        return e_num.ordinal();
    }

    public String name() {
        return e_num.name();
    }
}
