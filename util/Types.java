package util;

import java.io.Serializable;

public class Types {
    public enum Contents {
        NONE,
        TREE,
        HOUSE,
        CACTUS,
        SANTA_FE_NATIONAL_FOREST,
        RIO_GRANDE_DEL_NORTE_NATIONAL_MONUMENT,
        RIO_GRANDE_GORGE_BRIDGE,
        ASSISI_BASILICA,
        GILA_NATIONAL_FOREST,
        WATER,
        ROAD,
        DIRT,
        GRASS,
        DESERT,
        BORDER
    } 

    public static record Location(int row, int col) implements Serializable {
        @Override
        public int hashCode() {
            return 100 * row + col;
        }
    }
}
