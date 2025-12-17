
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import util.MyDLList;
import util.MyHashMap;
import util.MyHashTable;
import util.Types.Contents;
import util.Types.Location;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Screen extends JPanel implements KeyListener {
    public static MyHashMap<Contents, BufferedImage> contentImages;

    public static MyHashTable<Location, Contents> map;
    public static int viewportX = 0, viewportY = 0;
    public final static int viewportWidth = 10, viewportHeight = 10;
    public final static int gridBoxSize = 1000 / viewportWidth;
    public static MyDLList<MovingObj> movingObjs = null;
    public static MyHashMap<Contents, Color> colorMap = new MyHashMap<>();
    private Tourist tourist;

    static {
        contentImages = new MyHashMap<>();
        Contents[] contentValues = Contents.values();
        for (int i = 1; i <= 8; i++) {
            try {
                contentImages.put(contentValues[i],
                        ImageIO.read(new File("images/"
                                + contentValues[i].name().toLowerCase() + ".png")));
            } catch (IOException e) {
                System.out.println("Error for reading " + contentValues[i].name() + " " + e);
            }
        }
        colorMap.put(Contents.WATER, Color.BLUE);
        colorMap.put(Contents.ROAD, Color.BLACK);
        colorMap.put(Contents.DIRT, new Color(107, 70, 11));
        colorMap.put(Contents.BORDER, Color.RED);
        colorMap.put(Contents.GRASS, Color.GREEN);
        colorMap.put(Contents.DESERT, Color.YELLOW);
    }

    public Screen() {
        setLayout(null);
        setFocusable(true);
        this.tourist = new Tourist();
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
        try {
            FileInputStream fis = new FileInputStream("moving_objs.txt");

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
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(1000, 0, 100, 100);
        saveButton.addActionListener((ActionEvent e) -> onClose());
        saveButton.setFocusable(false);
        this.add(saveButton);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1100, 1000);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = viewportY; row < viewportY + viewportHeight; row++) {
            for (int col = viewportX; col < viewportX + viewportWidth; col++) {
                for (Contents content : map.get(new Location(row, col))) {
                    Color bgColor = colorMap.get(content);
                    if (bgColor != null) {
                        g.setColor(bgColor);
                        g.fillRect((col - viewportX) * gridBoxSize, (row - viewportY) * gridBoxSize, gridBoxSize,
                                gridBoxSize);
                    } else {
                        g.drawImage(contentImages.get(content), (col - viewportX) * gridBoxSize,
                                (row - viewportY) * gridBoxSize, gridBoxSize,
                                gridBoxSize, this);
                    }
                }
            }
        }
        for (MovingObj obj : movingObjs) {
            obj.drawMe(g);
        }
        tourist.drawMe(g);
        displayLandmarks(g, 0, 1);
        displayLandmarks(g, 1, 0);
        displayLandmarks(g, 1, 1);
        displayLandmarks(g, 0, 0);
        displayLandmarks(g, 0, -1);
        displayLandmarks(g, -1, 0);
        displayLandmarks(g, -1, -1);
        displayLandmarks(g, -1, 1);
        displayLandmarks(g, 1, -1);
    }

    public void displayLandmarks(Graphics g, int xDiff, int yDiff) {
        BufferedImage img = null;
        String name = null;
        String caption = null;
        for (Contents content : map
                .get(new Location(viewportY + viewportHeight / 2 + xDiff, viewportX + viewportWidth / 2 + yDiff))) {
            if (content == Contents.CACTUS || content == Contents.HOUSE || content == Contents.TREE) {
                continue;
            }
            if (contentImages.get(content) != null) {
                img = contentImages.get(content);
                name = content.name();
                switch (content) {
                    case SANTA_FE_NATIONAL_FOREST -> {
                        caption = "Covers 1.6 million acres of mountains, valleys and mesas ranging from 5,000 to 13,000 feet in elevation";
                    }
                    case RIO_GRANDE_DEL_NORTE_NATIONAL_MONUMENT -> {
                        caption = "Has the Taos Plateau volcanic field. Contains many grazing animals";
                    }
                    case RIO_GRANDE_GORGE_BRIDGE -> {
                        caption = "7th highest bridge in U.S., completed 1965";
                    }
                    case ASSISI_BASILICA -> {
                        caption = "Replaced an adobe church that was destroyed in the Pueblo Revolt of 1680";
                    }
                    case GILA_NATIONAL_FOREST -> {
                        caption = "6th largest national forest in the U.S. Houses the Gila monster species";
                    }
                    default -> {
                        System.out.println("Got an image from invalid source");
                        System.exit(0);
                    }
                }
                break;
            }
        }
        if (img == null)
            return;
        g.drawImage(img, 200, 200, 600, 600, this);
        g.setColor(Color.WHITE);
        g.fillRect(200, 800, 600, 100);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Comic Sans", 0, 15));
        g.drawString(name.replaceAll("_", " "), 200, 850);
        g.drawString(caption, 200, 890);
    }

    public void onClose() {
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
        try {
            FileOutputStream out;
            ObjectOutputStream outObj;
            out = new FileOutputStream("moving_objs.txt");
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