package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import src.util.MyDLList;
import src.util.MyHashMap;
import src.util.MyHashTable;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Screen extends JPanel implements KeyListener {
    public enum Contents {
        NONE,
        TREE,
        HOUSE,
        CACTUS,
        SANTA_FE_NATIONAL_FOREST,
        RIO_GRANDE_DEL_NORTE_NATIONAL_MONUMENT,
        RIO_GRANDE_GORGE_BRIDGE,
        ASSISI_BASILICA,
        GILA_NATIONAL_FOREST,
        WATER,
        ROAD,
        DIRT,
        GRASS,
        DESERT,
        BORDER
    }

    public static MyHashMap<Contents, BufferedImage> contentImages;

    static {
        contentImages = new MyHashMap<>();
        Contents[] contentValues = Contents.values();
        for (int i = 1; i < 8; i++) {
            try {
                contentImages.put(contentValues[i],
                        ImageIO.read(new File("/Users/yasen/AdvCSQ2Proj/src/images/"
                                + contentValues[i].name().toLowerCase() + ".png")));
            } catch (IOException e) {
                System.out.println("Error for reading " + contentValues[i].name() + " " + e);
            }
        }
    }

    public static record Location(int row, int col) implements Serializable {
        @Override
        public int hashCode() {
            return 100 * row + col;
        }
    }

    public static MyHashTable<Location, Contents> map;
    public static int viewportX = 0, viewportY = 0;
    public final static int viewportWidth = 10, viewportHeight = 10;
    public final static int gridBoxSize = 1000 / viewportWidth;
    public static MyDLList<MovingObj> movingObjs = null;
    
        public Screen() {
            setLayout(null);
            setFocusable(true);
            try {
                FileInputStream fis = new FileInputStream("src/map.txt");
    
                ObjectInputStream in = new ObjectInputStream(fis);
    
                map = (MyHashTable<Location, Contents>) in.readObject();
    
                fis.close();
                in.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Failed to read!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                FileInputStream fis = new FileInputStream("src/player.txt");
    
                ObjectInputStream in = new ObjectInputStream(fis);
    
                viewportX = in.readInt();
                viewportY = in.readInt();
    
                fis.close();
                in.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Using default list");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                FileInputStream fis = new FileInputStream("src/moving_objs.txt");
    
                ObjectInputStream in = new ObjectInputStream(fis);
    
                movingObjs = (MyDLList<MovingObj>) in.readObject();

            fis.close();
            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Failed to read moving objs");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (movingObjs == null) {
            movingObjs = new MyDLList<>();
            for (int i = 0; i < 100; i++) {
                switch ((int) (Math.random() * 4)) {
                    case 0 -> movingObjs.add(new Car());
                    case 1 -> movingObjs.add(new Boat());
                    case 2 -> movingObjs.add(new Mule());
                    case 3 -> movingObjs.add(new Bear());
                }
            }
        }
        addKeyListener(this);
        for (MovingObj obj : movingObjs) {
            new Thread(obj).start();
        }
        new Thread(new Animator(this)).start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = viewportY; row < viewportY + viewportHeight; row++) {
            for (int col = viewportX; col < viewportX + viewportWidth; col++) {
                for (Contents content : map.get(new Location(row, col))) {
                    switch (content) {
                        case WATER -> {
                            g.setColor(Color.BLUE);
                            g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                                    gridBoxSize);
                        }
                        case ROAD -> {
                            g.setColor(Color.BLACK);
                            g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                                    gridBoxSize);
                        }
                        case DIRT -> {
                            g.setColor(new Color(107, 70, 11));
                            g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                                    gridBoxSize);
                        }
                        case GRASS -> {
                            g.setColor(Color.GREEN);
                            g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                                    gridBoxSize);
                        }
                        case DESERT -> {
                            g.setColor(Color.YELLOW);
                            g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                                    gridBoxSize);
                        }
                        case BORDER -> {
                            g.setColor(Color.RED);
                            g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                                    gridBoxSize);
                        }
                        default -> {
                            g.drawImage(contentImages.get(content), (col - viewportX) * gridBoxSize,
                                    (row - viewportY) * gridBoxSize, gridBoxSize,
                                    gridBoxSize, this);
                        }
                    }
                }
            }
        }
        // Tourist
        g.setColor(new Color(107, 70, 11));
        g.fillRect((int) (5.25 * gridBoxSize), (int) (5.25 * gridBoxSize), gridBoxSize / 2, gridBoxSize / 2);
        g.setColor(Color.PINK);
        g.fillOval((int) ((5.5 - 0.2) * gridBoxSize), (int) ((5) * gridBoxSize - 0.2),
                (int) (0.4 * gridBoxSize), (int) (0.4 * gridBoxSize));
        g.fillRect((int) ((5.25 - 0.1) * gridBoxSize), (int) ((5.3) * gridBoxSize), (int) (0.1 * gridBoxSize),
                (int) (gridBoxSize / 3));
        g.fillRect((int) ((5.75) * gridBoxSize), (int) ((5.3) * gridBoxSize), (int) (0.1 * gridBoxSize),
                (int) (gridBoxSize / 3));
        for (MovingObj obj : movingObjs) {
            obj.drawMe(g);
        }
    }

    public void onClose() {
        try {
            FileOutputStream out;
            ObjectOutputStream outObj;
            out = new FileOutputStream("src/player.txt");
            outObj = new ObjectOutputStream(out);
            outObj.writeInt(viewportX);
            outObj.writeInt(viewportY);
            outObj.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Didn't write player coordinates");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            FileOutputStream out;
            ObjectOutputStream outObj;
            out = new FileOutputStream("src/moving_objs.txt");
            outObj = new ObjectOutputStream(out);
            outObj.writeObject(movingObjs);
            outObj.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Didn't write moving objs position");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void moveViewport(int xDiff, int yDiff) {
        viewportX += xDiff;
        viewportY += yDiff;
        if (viewportY < 0 || viewportY > 100 - viewportHeight || viewportX < 0 || viewportX > 100 - viewportWidth) {
            viewportY -= yDiff;
            viewportX -= xDiff;
            return;
        }
        Location newLoc = new Location(viewportY + viewportHeight / 2, viewportX + viewportWidth / 2);
        MyDLList<Contents> contents = map.get(newLoc);
        boolean invalid = contents.size() > 1;
        for (Contents content : contents) {
            if (invalid || content == Contents.BORDER || content == Contents.WATER) {
                invalid = true;
                break;
            }
        }
        for (MovingObj obj : movingObjs) {
            if (obj.getLoc().equals(newLoc)) {
                invalid = true;
            }
        }
        if (invalid) {
            viewportY -= yDiff;
            viewportX -= xDiff;
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> moveViewport(0, -1);
            case KeyEvent.VK_A -> moveViewport(-1, 0);
            case KeyEvent.VK_S -> moveViewport(0, 1);
            case KeyEvent.VK_D -> moveViewport(1, 0);
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}