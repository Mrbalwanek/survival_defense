package controls;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import entities.Character;

public class PlayerInput extends KeyAdapter{

    public boolean p1Up, p1Left, p1Down, p1Right, p1Attack; // dla gracza 1
    public boolean p2Up, p2Left, p2Down, p2Right, p2Attack; // dla gracza 2
    public Character character1, character2;

    public PlayerInput(Character character1,Character character2){
        this.character1 = character1;
        this.character2 = character2;
    }

    // metoda odświeżająca by dało się resetować gre
    public void updateCharacters(Character c1, Character c2) {
        this.character1 = c1;
        this.character2 = c2;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> p1Up = true;
            case KeyEvent.VK_A -> p1Left = true;
            case KeyEvent.VK_S -> p1Down = true;
            case KeyEvent.VK_D -> p1Right = true;
            case KeyEvent.VK_SPACE -> p1Attack = true;

            case KeyEvent.VK_UP -> p2Up = true;
            case KeyEvent.VK_LEFT -> p2Left = true;
            case KeyEvent.VK_DOWN -> p2Down = true;
            case KeyEvent.VK_RIGHT -> p2Right = true;
            case KeyEvent.VK_ENTER -> p2Attack = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> p1Up = false;
            case KeyEvent.VK_A -> p1Left = false;
            case KeyEvent.VK_S -> p1Down = false;
            case KeyEvent.VK_D -> p1Right = false;
            case KeyEvent.VK_SPACE -> p1Attack = false;

            case KeyEvent.VK_UP -> p2Up = false;
            case KeyEvent.VK_LEFT -> p2Left = false;
            case KeyEvent.VK_DOWN -> p2Down = false;
            case KeyEvent.VK_RIGHT -> p2Right = false;
            case KeyEvent.VK_ENTER -> p2Attack = false;
        }
    }
}