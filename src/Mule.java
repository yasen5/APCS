package src;

import src.Screen.Contents;
import src.util.MyDLList;

public class Mule extends MovingObj {
    public Mule() {
        super(MovingObj.ObjType.MULE);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return !(contents.size() > 1 || contents.contains(Contents.WATER) || contents.contains(Contents.ROAD));
    }
}
