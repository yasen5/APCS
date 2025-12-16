
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import util.Types.Contents;
import util.Types.Location;
import util.MyHashMap;
import util.MyHashTable;

public class MapReader {
    public static void main(String[] args) {
        MyHashTable<Location, Contents> map = new MyHashTable<>();
        MyHashMap<String, Contents> contentMap = new MyHashMap<>();
        contentMap.put("1", Contents.DESERT);
        contentMap.put("2", Contents.DIRT);
        contentMap.put("3", Contents.WATER);
        contentMap.put("4", Contents.BORDER);
        contentMap.put("5", Contents.GRASS);
        contentMap.put("6", Contents.ROAD);
        try {
            FileInputStream fis = new FileInputStream("/Users/yasen/AdvCSQ2Proj/map.txt");

            ObjectInputStream in = new ObjectInputStream(fis);

            Scanner sc = new Scanner(new File("/Users/yasen/AdvCSQ2Proj/MapExportFile.txt"));

            int row = 0;
            while (sc.hasNextLine()) {
                if (row == 100) {
                    break;
                }
                int col = 0;
                String line = sc.nextLine();
                for (String num : line.split(" ")) {
                    Contents bg = contentMap.get(num);
                    map.put(new Location(row, col), bg);
                    Contents thingOnTop = null;
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
                    } else if (bg == Contents.DESERT) {
                        if ((int) (Math.random() * 16) == 0) {
                            thingOnTop = Contents.CACTUS;
                        }
                    }
                    Location loc = new Location(row, col);
                    if (thingOnTop != null) {
                        map.put(loc, thingOnTop);
                    }
                    col++;
                }
                row++;
            }

            fis.close();
            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Failed to read! ");
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
