
import java.awt.Graphics;
import java.awt.Color;

public class Tourist {
    public void drawMe(Graphics g) {
        g.setColor(new Color(107, 70, 11));
        g.fillRect((int) (5.25 * Screen.gridBoxSize), (int) (5.25 * Screen.gridBoxSize), Screen.gridBoxSize / 2, Screen.gridBoxSize / 2);
        g.setColor(Color.PINK);
        g.fillOval((int) ((5.5 - 0.2) * Screen.gridBoxSize), (int) ((5) * Screen.gridBoxSize - 0.2),
                (int) (0.4 * Screen.gridBoxSize), (int) (0.4 * Screen.gridBoxSize));
        g.fillRect((int) ((5.25 - 0.1) * Screen.gridBoxSize), (int) ((5.3) * Screen.gridBoxSize), (int) (0.1 * Screen.gridBoxSize),
                (int) (Screen.gridBoxSize / 3));
        g.fillRect((int) ((5.75) * Screen.gridBoxSize), (int) ((5.3) * Screen.gridBoxSize), (int) (0.1 * Screen.gridBoxSize),
                (int) (Screen.gridBoxSize / 3));
    }
}
