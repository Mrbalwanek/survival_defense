package graphics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import entities.Character;
import entities.Enemy;
import entities.Beacon;
import utils.ImageUtils;


public class MyWindow extends JFrame {
    private final int windowWidth, windowHeight;
    public int getWindowWidth() {return windowWidth;}
    public int getWindowHeight() {return windowHeight;}
    Image img = ImageUtils.createImageIconFromName("mr");
    public MyWindow(int w, int h, ArrayList<Character> characters, ArrayList<Enemy> enemies, Beacon beacon) {
        this.setSize(w, h);
        windowWidth = w;
        windowHeight = h;
        this.add(new MyPanel(characters, enemies, beacon, w, h)); // tworzy się panel gry + przekazanie do niego danych (graczy, enemy i beaconu)
        this.setVisible(true); // okno pojawia się na ekranie
        this.setTitle("Survival Defense"); // zmienia tytuł
        this.setIconImage(img); // zmienia ikonę na bałwanka :D
        this.setResizable(false); // nie można zmienić rozmiaru ekranu myszką
    }
}

