package main;

import java.util.ArrayList;

import entities.Beacon;
import graphics.MyWindow;
import entities.Character;
import entities.Enemy;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {
        Dimension monitorSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) monitorSize.getWidth();
        int screenHeight = (int) monitorSize.getHeight();

        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Enemy> enemies = new ArrayList<>();

        Beacon beacon = new Beacon(screenWidth / 2 - 40, screenHeight / 2 - 40);

        new MyWindow(screenWidth, screenHeight, characters, enemies, beacon);
    }
}
