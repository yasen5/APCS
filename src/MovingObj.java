package src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import src.Screen;
import src.Screen.Contents;

public abstract class MovingObj implements Runnable {
    protected static MyHashMap<ObjType, BufferedImage> images;

    static {
        images = new MyHashMap<>();
        ObjType[] contentValues = ObjType.values();
        for (int i = 0; i < contentValues.length; i++) {
            try {
                images.put(contentValues[i],
                        ImageIO.read(new File("/Users/yasen/AdvCSQ2Proj/src/images/"
                                + contentValues[i].name().toLowerCase() + ".png")));
            } catch (IOException e) {
                System.out.println("Error for reading " + contentValues[i].name() + " " + e);
            }
        }
    }

    public static enum ObjType {
        BOAT,
        CAR,
        BEAR,
        MULE
    }

    private ObjType type;
    private Screen.Location loc;

    public MovingObj(ObjType type) {
        this.type = type;
        int counter = 0;
        do {
            System.out.println(++counter);
            loc = new Screen.Location((int) (Math.random() * 100), (int) (Math.random() * 100));
        } while (canMove(Screen.map.get(loc)));
    }

    public void drawMe(Graphics g) {
        g.drawImage(images.get(type), loc.col() * Screen.gridBoxSize, loc.row() * Screen.gridBoxSize,
                Screen.gridBoxSize, Screen.gridBoxSize, null);
    }

    protected abstract boolean canMove(MyDLList<Screen.Contents> contents);

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 9; i++) {
                int xDiff = (int) (Math.random() * 2);
                int yDiff = (int) (Math.random() * 2);
                Screen.Location newLoc = new Screen.Location(loc.row() + yDiff, loc.col() + xDiff);
                if (loc.col() < 100 && loc.row() < 100 && canMove(Screen.map.get(newLoc))) {
                    this.loc = newLoc;
                    break;
                }
            }
        }
    }

    public Screen.Location getLoc() {
        return loc;
    }
}
