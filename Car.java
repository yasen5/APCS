
import util.Types.Contents;
import util.Types.Location;
import util.MyDLList;

public class Car extends MovingObj {
    public Car() {
        super(MovingObj.ObjType.CAR);
    }

    public Car(Location loc) {
        super(MovingObj.ObjType.CAR, loc);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return contents.size() == 1 && contents.contains(Contents.ROAD);
    }
}
