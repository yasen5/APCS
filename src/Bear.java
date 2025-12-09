package src;

import src.Screen.Contents;

public class Bear extends MovingObj {
    public Bear() {
        super(MovingObj.ObjType.BEAR);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return !(contents.size() > 1 || contents.contains(Contents.WATER) || contents.contains(Contents.ROAD));
    }
}
