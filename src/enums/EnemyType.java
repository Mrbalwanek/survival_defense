package enums;

import utils.ImageUtils;
import java.awt.*;

public enum EnemyType {
    NORMAL(50, 2,5,10,250,"enemy"),
    STRONGER(150, 3,10,25,300,"strongerenemy"),
    THEBEST(250,4,20,50,500,"thebestenemy"),
    BOSS(550,2,50,200,1500,"boss"),
    CORRUPTED_BOSS(1000,3,80,500,3500,"corruptedboss");

    public final int hp, speed, money, xp;
    public final double damage;
    public final Image image;

    EnemyType(int hp, int speed, double damage, int money, int xp, String fileName){
        this.hp = hp;
        this.damage = damage;
        this.speed = speed;
        this.money = money;
        this.xp = xp;
        this.image = ImageUtils.createImageIconFromName(fileName);
    }
}