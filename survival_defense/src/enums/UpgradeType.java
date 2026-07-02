package enums;

public enum UpgradeType {
    HEAL("NORMAL","Gain some health back", true),
    MAX_HP("NORMAL","Gain some max health", true),
    REGEN("NORMAL","Get +2 health regeneration", false),
    MONEY("NORMAL","Gain some money", true),
    DAMAGE("NORMAL","Gain some damage", true),
    REROLL("NORMAL","Rerolls upgrades",false),

    XP_BOOST("SPECIAL","Gain +250 XP every time u defeat an enemy", false),
    TANK("SPECIAL","More max health but slower speed",false),
    VAMPIRE("SPECIAL","Attacking enemies heals but u have lower max health and enemies deal more damage (doesn't stack)",false),
    GAMBLER("SPECIAL","Gamble your life: get random amount of money from 1 to 1250 but lose random amount of health from 1 to 75 (random values for both players)",false),
    IM_STILL_STANDING("SPECIAL","When health is beneath 20% of max amount gain +2 speed and 1.5 damage multiplier",false),
    GOLD_GOLD_GOLD("SPECIAL","Every defeated enemy drops +25 more money",false),
    BERSERK("SPECIAL","Gain 1.75 damage multiplier but never regenerate health again and lose 20% of max health",false),
    DRUNK_GAMBLER("SPECIAL","Randomized control keys for 1 minute but in that time XP gain is multiplied by 2",false),
    HEALING_TOUCH("SPECIAL","Attacking another player gives him 1hp but takes 1hp from you",false),
    SACRIFICE("SPECIAL","Sacrifice random amount of health to maxhp of the beacon",false),
    SPIKEY_BEACON("SPECIAL","If an enemy attacks the beacon it loses 25hp",false),
    HIT_ME_HARD("SPECIAL","When the player is below 25% of max health when an enemy attacks him he gains +1 damage (limit +50 damage)",false),
    BOUNCY_BEACON("SPECIAL","When a player touches the beacon he bounces from it",false),
    SPEED("SPECIAL","Gain +1 speed",false),
    HUH("SPECIAL","Beacon now becomes a speed pad for players",false);


    public final String type, description;
    public final boolean hasValue;

    UpgradeType(String type, String description, boolean hasValue) {
        this.type = type;
        this.description = description;
        this.hasValue = hasValue;
    }
}
