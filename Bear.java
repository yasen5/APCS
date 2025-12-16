
import util.Types.Contents;
import util.Types.Location;
import util.MyDLList;

public class Bear extends MovingObj {
    public Bear() {
        super(MovingObj.ObjType.BEAR);
    }

    public Bear(Location loc) {
        super(MovingObj.ObjType.BEAR, loc);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return !(contents.size() > 1 || contents.contains(Contents.WATER) || contents.contains(Contents.ROAD));
    }
}
