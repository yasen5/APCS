package src;

import src.Screen.Contents;
import src.util.MyDLList;

public class Car extends MovingObj {
    public Car() {
        super(MovingObj.ObjType.CAR);
    }

    @Override
    public boolean canMove(MyDLList<Contents> contents) {
        return contents.size() == 1 && contents.contains(Contents.ROAD);
    }
}
