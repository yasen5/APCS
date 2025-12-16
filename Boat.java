
import util.Types.Contents;
import util.Types.Location;
import util.MyDLList;

public class Boat extends MovingObj {
    public Boat() {
        super(MovingObj.ObjType.BOAT);
    }

    public Boat(Location loc) {
        super(MovingObj.ObjType.BOAT, loc);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return (contents.contains(Contents.WATER));
    }
}
