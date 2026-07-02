package entities;

import combat.Damageable;

import java.awt.Rectangle;

public abstract  class Entity implements Damageable {
    protected int x, y, hp, maxHp, speed;
    protected double damage;

    public Entity() {}

    public Entity(int x, int y, int hp, int maxHp, int damage, int speed) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.maxHp = maxHp;
        this.damage = damage;
        this.speed = speed;
    }

    public abstract void update(Beacon beacon, java.util.ArrayList<Character> characters, upgrades.UpgradeManager upgradeManager);

    public int getX() {
        return x;
    }

    public int getY() {return y;}

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public double getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Rectangle getBoundaries(){
        return new Rectangle(x + 37, y + 12, 24, 24); // 24px w rzeczywistosci = 25px
    }

    @Override
    public void takeDmg(double damage) {
        this.hp -= (int)damage;
    }
}
