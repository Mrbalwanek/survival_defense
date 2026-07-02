package entities;

import java.awt.*;
import java.util.ArrayList;

import upgrades.UpgradeManager;
import utils.ImageUtils;

public class Beacon extends Entity{
    Image img = ImageUtils.createImageIconFromName("beacon");

    public Beacon (int x, int y) {
        super(x, y, 200, 200, 0, 0);
    }

    public Image getImg() {
        return img;
    }

    public void hpReset() {
        this.hp = 200;
        this.maxHp = 200;
    }

    @Override
    public void update(Beacon beacon, ArrayList<Character> characters, UpgradeManager upgradeManager) {
        // puste bo beacon tylko stoi w miejscu
    }

    @Override
    public Rectangle getBoundaries() {
        return new Rectangle(x, y + 1, 38, 38); // 38px w rzeczywistosci = 39px
    }
}

