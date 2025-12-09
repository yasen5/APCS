package src;

import src.Screen.Contents;

public class Boat extends MovingObj {
    public Boat() {
        super(MovingObj.ObjType.BOAT);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return (contents.contains(Contents.WATER));
    }
}
