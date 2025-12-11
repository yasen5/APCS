package src;

import src.Screen.Contents;
import src.util.MyDLList;

public class Boat extends MovingObj {
    public Boat() {
        super(MovingObj.ObjType.BOAT);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return (contents.contains(Contents.WATER));
    }
}
