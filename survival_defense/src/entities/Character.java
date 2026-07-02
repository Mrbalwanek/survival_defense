package entities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import upgrades.UpgradeManager;
import utils.ImageUtils;

public class Character extends Entity{
    private Image img = ImageUtils.createImageIconFromName("player");
    private Image attackImgL = ImageUtils.createImageIconFromName("slashL");
    private Image attackImgR = ImageUtils.createImageIconFromName("slashR");
    private int money;
    private int level;
    private boolean isAttacking;
    private int attackVisible;
    private boolean lookingRight;
    private int attackCooldown;
    private int extraDmgFromHitMeHard;
    private boolean canAttack;
    private int id;
    private int regen;
    private int enemiesKilled;

    public Character (int x, int y, int hp, int maxHp, double damage, int speed, int money, int level, boolean isAttacking, int attackVisible, boolean lookingRight, int attackCooldown, int extraDmgFromHitMeHard, boolean canAttack, int id, int enemiesKilled){
        super(x, y, hp, maxHp, (int)damage, speed);
        this.money = money;
        this.level = level;
        this.isAttacking = isAttacking;
        this.attackVisible = attackVisible;
        this.lookingRight = lookingRight;
        this.attackCooldown = attackCooldown;
        this.extraDmgFromHitMeHard = extraDmgFromHitMeHard;
        this. canAttack = canAttack;
        this.id = id;
        this.regen = 2;
        this.enemiesKilled = enemiesKilled;
    }

    public Character(int x, int y, boolean lookingRight, int id){
        this(x, y, 100, 100, 25.0, 5, 0, 1, false, 0, lookingRight, 0, 0,true, id, 0);
        this.id = id;
    }

    @Override
    public void update(Beacon beacon, ArrayList<Character> characters, UpgradeManager upgradeManager) {
        //  puste bo gracz porusza się za pomocą PlayerInput
    }

    public int getMoney() {
        return money;
    }

    public int getLevel() {
        return level;
    }

    public void setMoney(int money) {
        if (money >= 0) {
            this.money = money;
        }
    }
    public void setMoney(String money) {
        this.money = Integer.parseInt(money);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public int getAttackVisible() {
        return attackVisible;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public Image getAttackImgL() {
        return attackImgL;
    }

    public Image getAttackImgR() {
        return attackImgR;
    }

    public boolean isLookingRight() {
        return lookingRight;
    }

    public void setLookingRight(boolean lookingRight) {
        this.lookingRight = lookingRight;
    }

    public void setAttackVisible(int attackVisible) {
        this.attackVisible = attackVisible;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public int getExtraDmgFromHitMeHard() {
        return extraDmgFromHitMeHard;
    }

    public void setExtraDmgFromHitMeHard(int extraDmgFromHitMeHard) {this.extraDmgFromHitMeHard = extraDmgFromHitMeHard;}

    public boolean isCanAttack() {return canAttack;}

    public void setCanAttack(boolean canAttack) {this.canAttack = canAttack;}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public int getRegen() {
        return regen;
    }

    public void setRegen(int regen) {
        this.regen = regen;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void setEnemiesKilled(int enemiesKilled) {
        this.enemiesKilled = enemiesKilled;
    }
}

