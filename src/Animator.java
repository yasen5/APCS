package src;

public class Animator implements Runnable {
    Screen screen;
    public Animator(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            screen.repaint();
        }
    }
}
