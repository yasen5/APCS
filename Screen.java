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
    private enum Background {
        WATER,
        ROAD,
        DIRT,
        GRASS,
        BORDER
    }

    private enum Contents {
        NONE,
        TREE,
        HOUSE,
        BANDELIER,
        RIO_GRANDE_BRIDGE,
        ALBUQUERQUE_OLD_TOWN,
        ASSISI_BASILICA,
        SANTA_FE_PLAZA;
    }

    private static record GridObject(Background bg, Contents contents) implements Serializable {
    }

    private static MyHashMap<Contents, BufferedImage> contentImages;

    static {
        contentImages = new MyHashMap<>();
        for (Contents content : Contents.values()) {
            if (content == Contents.NONE) {
                continue;
            }
            try {
                contentImages.put(content, ImageIO.read(new File("images/" + content.name().toLowerCase() + ".png")));
            } catch (IOException e) {
                System.out.println("Error for reading " + content.name() + " " + e);
            }
        }
    }

    private static record Location(int row, int col) implements Serializable {
        @Override
        public int hashCode() {
            return 100 * row + col;
        }
    }

    public static MyHashTable<Location, GridObject> map;

    private int viewportX = 0, viewportY = 0;
    private final int viewportWidth = 10, viewportHeight = 10;

    private final int gridBoxSize = 1000 / viewportWidth;

    public Screen() {
        setLayout(null);
        setFocusable(true);
        try {
            FileInputStream fis = new FileInputStream("map.txt");

            ObjectInputStream in = new ObjectInputStream(fis);

            map = (MyHashTable<Location, GridObject>) in.readObject();

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
        Background[] backgroundOptions = Background.values();
        Contents[] contentOptions = Contents.values();
        if (!(map instanceof MyHashTable<Location, GridObject>)) {
            map = new MyHashTable<>();
            for (int row = 0; row < 100; row++) {
                for (int col = 0; col < 100; col++) {
                    boolean isRoad = row % 5 == 0 || col % 5 == 0;
                    boolean isGrass = col % 10 < 5 && row % 10 < 5;
                    boolean isWater = col % 16 < 3 && row % 13 < 3;
                    boolean isBorder = col == 0 || row == 0 || (col == 99 && row < 84) || (row == 99 && col < 10)
                            || (col == 10 && row > 89) || (row == 89 && col >= 10 && col < 30)
                            || (col == 30 && row >= 84 && row < 90) || (row == 84 && col > 30);
                    Background bg = Background.DIRT;
                    if (isBorder) {
                        bg = Background.BORDER;
                    } else if (isRoad) {
                        bg = Background.ROAD;
                    } else if (isWater) {
                        bg = Background.WATER;
                    } else if (isGrass) {
                        bg = Background.GRASS;
                    }
                    map.put(new Location(row, col), new GridObject(bg, contentOptions[(int) (Math.random() * 3)]));
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
                for (GridObject obj : map.get(new Location(row, col))) {
                    switch (obj.bg) {
                        case WATER -> g.setColor(Color.BLUE);
                        case ROAD -> g.setColor(Color.BLACK);
                        case DIRT -> g.setColor(new Color(102, 70, 11));
                        case GRASS -> g.setColor(Color.GREEN);
                        case BORDER -> g.setColor(Color.RED);
                    }
                    g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                            gridBoxSize);
                    if (obj.contents != Contents.NONE) {
                        // System.out.println("Drawing");
                        System.out.println("Val is null: " + (contentImages.get(obj.contents) == null));
                        g.drawImage(contentImages.get(obj.contents), (col - viewportX) * gridBoxSize,
                                (row - viewportY) * gridBoxSize, gridBoxSize,
                                gridBoxSize, this);
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
        Background bg = map.get(new Location(viewportY + viewportHeight / 2, viewportX + viewportWidth / 2)).get(0).bg;
        if (bg == Background.BORDER || bg == Background.WATER) {
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