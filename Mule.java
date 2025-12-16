
import util.Types.Contents;
import util.Types.Location;
import util.MyDLList;

public class Mule extends MovingObj {
    public Mule() {
        super(MovingObj.ObjType.MULE);
    }

    public Mule(Location loc) {
        super(MovingObj.ObjType.MULE, loc);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return !(contents.size() > 1 || contents.contains(Contents.WATER) || contents.contains(Contents.ROAD));
    }
}
