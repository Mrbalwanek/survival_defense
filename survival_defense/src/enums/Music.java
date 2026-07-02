package enums;

import utils.SoundUtils;

public enum Music{
    // muzyka w tle
    MENU(0),
    FIGHTING(1),
    FIGHTING2(2),
    UPGRADE(3),

    // efekty
    UPGRADE_SHUFFLE(4),
    GOT_UPGRADE(5),
    SLASH(6),
    BUTTON(7);

    public final int id;

    Music(int id){
        this.id = id;
    }
}