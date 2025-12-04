import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Screen extends JPanel implements KeyListener {
    private enum Contents {
        NONE,
        TREE,
        HOUSE,
        SANTA_FE_NATIONAL_FOREST,
        RIO_GRANDE_DEL_NORTE_NATIONAL_MONUMENT,
        RIO_GRANDE_GORGE_BRIDGE,
        ASSISI_BASILICA,
        GILA_NATIONAL_FOREST,
        WATER,
        ROAD,
        DIRT,
        GRASS,
        BORDER
    }

    private static MyHashMap<Contents, BufferedImage> contentImages;

    static {
        contentImages = new MyHashMap<>();
        Contents[] contentValues = Contents.values();
        for (int i = 1; i < 8; i++) {
            try {
                contentImages.put(contentValues[i],
                        ImageIO.read(new File("images/" + contentValues[i].name().toLowerCase() + ".png")));
            } catch (IOException e) {
                System.out.println("Error for reading " + contentValues[i].name() + " " + e);
            }
        }
    }

    private static record Location(int row, int col) implements Serializable {
        @Override
        public int hashCode() {
            return 100 * row + col;
        }
    }

    public static MyHashTable<Location, Contents> map;

    private int viewportX = 0, viewportY = 0;
    private final int viewportWidth = 10, viewportHeight = 10;

    private final int gridBoxSize = 1000 / viewportWidth;

    public Screen() {
        setLayout(null);
        setFocusable(true);
        try {
            FileInputStream fis = new FileInputStream("map.txt");

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
            FileInputStream fis = new FileInputStream("player.txt");

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
        Contents[] options = Contents.values();
        if (!(map instanceof MyHashTable<Location, Contents>)) {
            map = new MyHashTable<>();
            for (int row = 0; row < 100; row++) {
                for (int col = 0; col < 100; col++) {
                    boolean isRoad = row % 5 == 0 || col % 5 == 0;
                    boolean isGrass = col % 10 < 5 && row % 10 < 5;
                    boolean isWater = col % 16 < 3 && row % 13 < 3;
                    boolean isBorder = col == 0 || row == 0 || (col == 99 && row < 84) || (row == 99 && col < 10)
                            || (col == 10 && row > 89) || (row == 89 && col >= 10 && col < 30)
                            || (col == 30 && row >= 84 && row < 90) || (row == 84 && col > 30);
                    Contents bg = Contents.DIRT;
                    if (isBorder) {
                        bg = Contents.BORDER;
                    } else if (isRoad) {
                        bg = Contents.ROAD;
                    } else if (isWater) {
                        bg = Contents.WATER;
                    } else if (isGrass) {
                        bg = Contents.GRASS;
                    }
                    Contents thingOnTop = Contents.NONE;
                    if (col == 49 && row == 4) {
                        thingOnTop = Contents.RIO_GRANDE_DEL_NORTE_NATIONAL_MONUMENT;
                    } else if (col == 49 && row == 14) {
                        thingOnTop = Contents.RIO_GRANDE_GORGE_BRIDGE;
                    } else if (col == 44 && row == 24) {
                        thingOnTop = Contents.SANTA_FE_NATIONAL_FOREST;
                    } else if (col == 49 && row == 34) {
                        thingOnTop = Contents.ASSISI_BASILICA;
                    } else if (col == 14 && row == 69) {
                        thingOnTop = Contents.GILA_NATIONAL_FOREST;
                    } else if (bg == Contents.GRASS) {
                        if ((int) (Math.random() * 4) == 0) {
                            thingOnTop = Contents.TREE;
                        }
                    } else if (bg == Contents.DIRT) {
                        if ((int) (Math.random() * 16) == 0) {
                            thingOnTop = Contents.HOUSE;
                        }
                    }
                    Location loc = new Location(row, col);
                    map.put(loc, bg);
                    map.put(loc, thingOnTop);
                }
            }
            try {
                FileOutputStream out;
                ObjectOutputStream outObj;
                out = new FileOutputStream("map.txt");
                outObj = new ObjectOutputStream(out);
                // Create a data stream to read in
                outObj.writeObject(map);
                outObj.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Didn't write");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        addKeyListener(this);
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
        g.setColor(Color.pink);
        g.drawRect(5 * gridBoxSize, 5 * gridBoxSize, gridBoxSize, gridBoxSize);
    }

    public void moveViewport(int xDiff, int yDiff) {
        viewportX += xDiff;
        viewportY += yDiff;
        if (viewportY < 0 || viewportY > 100 - viewportHeight || viewportX < 0 || viewportX > 100 - viewportWidth) {
            viewportY -= yDiff;
            viewportX -= xDiff;
            return;
        }
        for (Contents content : map.get(new Location(viewportY + viewportHeight / 2, viewportX + viewportWidth / 2))) {
            if (content == Contents.BORDER || content == Contents.WATER || content == Contents.ASSISI_BASILICA
                    || content == Contents.GILA_NATIONAL_FOREST || content == Contents.HOUSE
                    || content == Contents.RIO_GRANDE_DEL_NORTE_NATIONAL_MONUMENT
                    || content == Contents.RIO_GRANDE_GORGE_BRIDGE || content == Contents.SANTA_FE_NATIONAL_FOREST
                    || content == Contents.TREE) {
                viewportY -= yDiff;
                viewportX -= xDiff;
            }
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
        try {
            FileOutputStream out;
            ObjectOutputStream outObj;
            out = new FileOutputStream("player.txt");
            outObj = new ObjectOutputStream(out);
            outObj.writeInt(viewportX);
            outObj.writeInt(viewportY);
            outObj.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Didn't write player coordinates");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}