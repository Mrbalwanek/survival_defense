package entities;

import java.awt.*;

import enums.UpgradeType;

public class RandomUpgrade {
    private String name;
    private int value;
    private Color color;
    private Rectangle option;
    private UpgradeType type;


    public RandomUpgrade(String name, int value, Color color, Rectangle option, UpgradeType type) {
        this.name = name;
        this.value = value;
        this.color = color;
        this.option = option;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public Rectangle getOption() {
        return option;
    }

    public UpgradeType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setOption(Rectangle option) {
        this.option = option;
    }

    public void setType(UpgradeType type) {
        this.type = type;
    }
}
