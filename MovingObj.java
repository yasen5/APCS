
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import util.Types.Contents;
import util.Types.Location;
import util.MyDLList;
import util.MyHashMap;

public abstract class MovingObj implements Runnable, Serializable {
    protected static MyHashMap<ObjType, BufferedImage> images;

    static {
        images = new MyHashMap<>();
        ObjType[] contentValues = ObjType.values();
        for (int i = 0; i < contentValues.length; i++) {
            try {
                images.put(contentValues[i],
                        ImageIO.read(new File("images/"
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
    private Location loc;

    public MovingObj(ObjType type) {
        this.type = type;
        do {
            loc = new Location((int) (Math.random() * 100), (int) (Math.random() * 100));
        } while (!canMove(Screen.map.get(loc)));
    }

    public MovingObj(ObjType type, Location loc) {
        this.loc = loc;
    }

    public void drawMe(Graphics g) {
        int relativeX = loc.col() - Screen.viewportX;
        int relativeY = loc.row() - Screen.viewportY;
        if (relativeX >= 0 && relativeX < Screen.viewportWidth && relativeY >= 0 && relativeY < Screen.viewportHeight) {
            g.drawImage(images.get(type), relativeX * Screen.gridBoxSize, relativeY * Screen.gridBoxSize,
                    Screen.gridBoxSize, Screen.gridBoxSize, null);
        }
    }

    protected abstract boolean canMove(MyDLList<Contents> contents);

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int count = 0;
            for (int i = 0; i < 1000; i++) {
                count++;
                int xDiff = (int) (Math.random() * 3) - 1;
                int yDiff = (int) (Math.random() * 3) - 1;
                Location newLoc = new Location(loc.row() + yDiff, loc.col() + xDiff);
                for (MovingObj obj : Screen.movingObjs) {
                    if (obj.getLoc().equals(loc)) {
                        continue;
                    }
                }
                if (newLoc.col() == Screen.viewportX + Screen.viewportWidth / 2
                        && newLoc.row() == Screen.viewportY + Screen.viewportHeight / 2) {
                    continue;
                }
                if (newLoc.col() > 0 && newLoc.row() > 0 && newLoc.col() < 100 && newLoc.row() < 100
                        && canMove(Screen.map.get(newLoc))) {
                    this.loc = newLoc;
                    break;
                }
            }
            if (count == 1000) {
                // System.out.println("Failed");
            }
        }
    }

    public Location getLoc() {
        return loc;
    }
}
