package upgrades;

import entities.Character;
import entities.Enemy;
import entities.Beacon;
import entities.RandomUpgrade;
import enums.UpgradeType;
import java.awt.*;
import java.util.ArrayList;

public class UpgradeManager {

    ArrayList<RandomUpgrade> upgrades = new ArrayList<>();

    boolean noRegen;
    boolean vampire;
    boolean healTouch;
    boolean extraGold;
    boolean thornBeacon;
    boolean lowHpDmgBuff;
    boolean stillStanding;
    boolean drunk;
    boolean bouncyB;
    boolean extraXP;
    boolean beaconPad;

    int XPValue = 250;

    public void applyUpgrade(RandomUpgrade u, ArrayList<Character> characters, ArrayList<Enemy> enemies, Beacon beacon){

        switch (u.getType()) {

            case HEAL:
                for (Character c : characters) {
                    c.setHp(c.getHp() + u.getValue());

                    if (c.getHp() > c.getMaxHp()) {
                        c.setHp(c.getMaxHp());
                    }
                }
                break;

            case MAX_HP:
                for (Character c : characters) {
                    c.setMaxHp(c.getMaxHp() + u.getValue());
                }
                break;

            case REGEN:
                for (Character c : characters) {
                    c.setRegen(c.getRegen() + 2);
                }
                break;

            case MONEY:
                for (Character c : characters) {
                    c.setMoney(c.getMoney() + u.getValue());
                }
                break;

            case DAMAGE:
                for (Character c : characters) {
                    c.setDamage(c.getDamage() + u.getValue());
                }
                break;

            case XP_BOOST:
                XPValue += 250;
                break;

            case SPIKEY_BEACON:
                thornBeacon = true;
                break;

            case REROLL:
                randomizedUpgrades();
                break;

            //golden

            case TANK:
                for(Character c : characters){
                    c.setMaxHp(c.getMaxHp() + 50);
                    c.setSpeed(c.getSpeed() - 1);
                    if(c.getSpeed() < 2){
                        c.setSpeed(2);
                    }
                }
                break;

            case VAMPIRE:
                vampire = true;
                for(Character c : characters){
                    c.setMaxHp(c.getMaxHp() - 20);
                    c.setHp(c.getMaxHp());
                }
                for(Enemy en :enemies){
                    en.setDamage((int)(en.getDamage() / 1.5));
                }
                break;

            case GAMBLER:
                for (Character c : characters) {
                    c.setMoney(c.getMoney() + u.getValue());

                    int lostHP = (int)(Math.random() * 75) + 1; // los straty HP
                    c.setMaxHp(c.getMaxHp() - lostHP);

                    if(c.getMaxHp() < 1){
                        c.setMaxHp(1);
                    }
                    if(c.getHp() > c.getMaxHp()){
                        c.setHp(c.getMaxHp());
                    }
                }
                break;

            case IM_STILL_STANDING:
                stillStanding = true;
                break;

            case GOLD_GOLD_GOLD:
                extraGold = true;
                break;

            case BERSERK:
                for(Character c : characters){
                    c.setDamage((int)(c.getDamage() * 1.75));
                    noRegen = true;
                    c.setMaxHp(c.getMaxHp() - (c.getMaxHp() / 5));
                    if(c.getHp() > c.getMaxHp()){
                        c.setHp(c.getMaxHp());
                    }
                }
                break;

            case DRUNK_GAMBLER:
                drunk = true;
                break;

            case HEALING_TOUCH:
                healTouch = true;
                break;

            case SACRIFICE:
                for(Character c : characters){
                    c.setHp(c.getHp() - u.getValue());
                    beacon.setMaxHp(beacon.getMaxHp() + u.getValue());
                    if(c.getHp() <= 0){
                        c.setHp(1);
                    }
                }
                break;

            case HIT_ME_HARD:
                lowHpDmgBuff = true;
                break;

            case BOUNCY_BEACON:
                bouncyB = true;
                break;

            case SPEED:
                for(Character c : characters){
                    c.setSpeed(c.getSpeed() + 1);
                }
                break;

            case HUH:
                beaconPad = true;
                break;
        }

    }

    public void randomizedUpgrades() {

        upgrades.clear();

        //losowanie 3 ulepszeń
        for (int i = 0; i < 3; i++) {
            /*UpgradeType[] types = UpgradeType.values();
            UpgradeType selected = types[(int) (Math.random() * types.length)]; // losowanie elementu; (int) - zamiana random z ułamka na całość
            */
            String rarityToRoll = (Math.random() < 0.20) ? "SPECIAL" : "NORMAL";

            // 2. Filtrowanie puli na podstawie pola 'type' z Twojego Enuma
            ArrayList<UpgradeType> pool = new ArrayList<>();
            for (UpgradeType t : UpgradeType.values()) {
                if (t.type.equals(rarityToRoll)) {
                    pool.add(t);
                }
            }

            // 3. Wybór losowej karty z pasującej grupy
            UpgradeType selected = pool.get((int) (Math.random() * pool.size()));

            String cardName = selected.toString();
            int randValue = 0;

            switch(selected){
                //  int zamienia Math.random z ułamka na liczbę całkowitą
                case HEAL -> randValue = (int)(Math.random() * 26) + 5; // 5 - 30 Heal
                case MAX_HP -> randValue = (int)(Math.random() * 26) + 5; // 5 - 30 MaxHp
                case MONEY -> randValue = (int)(Math.random() * 101) + 50; // 50 - 150 Money
                case DAMAGE -> randValue = (int)(Math.random() * 11) + 10;  // 10 - 20 DMG

                case GAMBLER -> randValue = (int)(Math.random() * 1250) + 1; // 1 - 1250 Money
                case SACRIFICE -> randValue = (int)(Math.random() * 60) + 10; // 10 - 70 MaxHp To Beacon
            }

            Color cardColor = Color.GREEN;

            switch (selected){

                case HEAL, MAX_HP:
                    if(randValue > 25){
                        cardColor = Color.RED;
                    } else if(randValue > 20){
                        cardColor = Color.MAGENTA;
                    } else if(randValue > 10){
                        cardColor = Color.BLUE;
                    }
                    break;

                case MONEY:
                    if(randValue > 130){
                        cardColor = Color.RED;
                    } else if(randValue > 100){
                        cardColor = Color.MAGENTA;
                    } else if(randValue > 70){
                        cardColor = Color.BLUE;
                    }
                    break;

                case DAMAGE:
                    if(randValue > 18){
                        cardColor = Color.RED;
                    } else if(randValue > 16){
                        cardColor = Color.MAGENTA;
                    } else if(randValue > 14){
                        cardColor = Color.BLUE;
                    }
                    break;

                case REROLL:
                    cardColor = Color.BLACK;
                    break;

                case REGEN, XP_BOOST, SPIKEY_BEACON, BOUNCY_BEACON, SPEED:
                    cardColor = Color.RED;
                    break;

                case TANK, VAMPIRE, GAMBLER, IM_STILL_STANDING, GOLD_GOLD_GOLD, BERSERK, DRUNK_GAMBLER, HEALING_TOUCH, SACRIFICE, HIT_ME_HARD, HUH:
                    cardColor = Color.ORANGE;
                    break;
            }


            int xPos = 390 + (i * 430); // zaczyna się od X: 390px i kazdy prostokat ma odstep od siebie 430px (430 bo szerokość + przerwa)
            Rectangle rect = new Rectangle(xPos, 200, 280, 450);

            RandomUpgrade gamblingOption = new RandomUpgrade(cardName, randValue, cardColor, rect, selected);
            upgrades.add(gamblingOption);
        }
    }

    public void resetUpgrades() {
        this.noRegen = false;
        this.vampire = false;
        this.healTouch = false;
        this.extraGold = false;
        this.thornBeacon = false;
        this.lowHpDmgBuff = false;
        this.stillStanding = false;
        this.drunk = false;
        this.bouncyB = false;
        this.extraXP = false;
        this.beaconPad = false;
    }


    public ArrayList<RandomUpgrade> getUpgrades() {
        return upgrades;
    }

    public boolean isNoRegen() {
        return noRegen;
    }

    public boolean isVampire() {
        return vampire;
    }

    public boolean isHealTouch() {
        return healTouch;
    }

    public boolean isExtraGold() {
        return extraGold;
    }

    public boolean isThornBeacon() {
        return thornBeacon;
    }

    public boolean isLowHpDmgBuff() {
        return lowHpDmgBuff;
    }

    public boolean isStillStanding() {
        return stillStanding;
    }

    public int getXPValue() {return XPValue;}

    public boolean isExtraXP() {return extraXP;}

    public boolean isDrunk() {
        return drunk;
    }

    public void setDrunk(boolean drunk) {
        this.drunk = drunk;
    }

    public boolean isBouncyB() {return bouncyB;}

    public boolean isBeaconPad() {return beaconPad;}

    public void setUpgrades(ArrayList<RandomUpgrade> upgrades) {
        this.upgrades = upgrades;
    }

    public void setNoRegen(boolean noRegen) {
        this.noRegen = noRegen;
    }

    public void setVampire(boolean vampire) {
        this.vampire = vampire;
    }

    public void setHealTouch(boolean healTouch) {
        this.healTouch = healTouch;
    }

    public void setExtraGold(boolean extraGold) {
        this.extraGold = extraGold;
    }

    public void setThornBeacon(boolean thornBeacon) {
        this.thornBeacon = thornBeacon;
    }

    public void setLowHpDmgBuff(boolean lowHpDmgBuff) {
        this.lowHpDmgBuff = lowHpDmgBuff;
    }

    public void setStillStanding(boolean stillStanding) {
        this.stillStanding = stillStanding;
    }

    public void setBouncyB(boolean bouncyB) {
        this.bouncyB = bouncyB;
    }

    public void setExtraXP(boolean extraXP) {
        this.extraXP = extraXP;
    }

    public void setBeaconPad(boolean beaconPad) {
        this.beaconPad = beaconPad;
    }

    public void setXPValue(int XPValue) {
        this.XPValue = XPValue;
    }
}
