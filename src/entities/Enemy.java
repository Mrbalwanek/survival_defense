package entities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import graphics.MyWindow;
import upgrades.UpgradeManager;
import utils.ImageUtils;
import enums.EnemyType;

public class Enemy extends Entity{

    private final Image imgHit = ImageUtils.createImageIconFromName("hit");
    private Image currentImg;
    private EnemyType type;

    private int cooldown ;
    private int hitTimer;

    public Enemy(int x, int y, EnemyType type){
        super(x, y, type.hp, type.hp, (int)type.damage, type.speed); // te 2 type.hp to maxHp
        this.type = type;
        this.currentImg = type.image;
        this.cooldown = 0;
        this.hitTimer = 0;
    }

    @Override
    public void takeDmg(double dmg) {
        this.hp -= (int) dmg;
        this.hitTimer = 8;
        this.currentImg = imgHit;
    }

    @Override
    public void update(Beacon beacon, ArrayList<Character> characters, UpgradeManager upgradeManager){

        if(hitTimer > 0) {
            hitTimer--;
            if(hitTimer == 0) {
                restoreBaseImg();
            }
        }


        double beaconCenterX = beacon.getX() + 20;
        double beaconCenterY = beacon.getY() + 20;

        double currentEnemyX = x + 25;
        double currentEnemyY = y + 25;

        // jak daleko do celu
        double diffX = beaconCenterX  - currentEnemyX;
        double diffY = beaconCenterY - currentEnemyY;

        // Pitagoras
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        if(distance > speed) {
            x += (int) ((diffX / distance) * speed);
            y += (int) ((diffY / distance) * speed);
        }

        if(cooldown > 0){
            cooldown--;
        }

        // kolizja z beaconem
        if(getBoundaries().intersects(beacon.getBoundaries()) && cooldown == 0){
            cooldown = 60;
            beacon.takeDmg(this.damage);
            if(upgradeManager.isThornBeacon()){
                takeDmg(25);
            }
        }

        // kolizja z graczami
        for(Character c : characters){
            if(getBoundaries().intersects(c.getBoundaries()) && cooldown == 0){
                cooldown = 60;
                c.takeDmg(this.damage);

                if(upgradeManager.isLowHpDmgBuff() && c.getHp() < (c.getMaxHp() / 4)){
                    if(c.getExtraDmgFromHitMeHard() < 50) {
                        c.setDamage(c.getDamage() + 1);
                        c.setExtraDmgFromHitMeHard(c.getExtraDmgFromHitMeHard() + 1);
                    }
                }
            }
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public Image getImg() {
        return currentImg;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getHitTimer() {
        return hitTimer;
    }

    public void setHitTimer(int hitTimer) {
        this.hitTimer = hitTimer;
    }

    public Image getCurrentImg() {
        return currentImg;
    }

    public void restoreBaseImg() {
        this.currentImg = type.image;
    }

    public int getEnemyGoldValue() {return type.money;}

    public int getEnemyXpValue() {return type.xp;}

}
