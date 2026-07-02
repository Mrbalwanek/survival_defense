package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import entities.Beacon;
import entities.Character;
import entities.Enemy;
import entities.RandomUpgrade;
import controls.PlayerInput;
import enums.*;
import upgrades.UpgradeManager;
import utils.SoundUtils;


public class MyPanel extends JPanel {

    public ArrayList<Character> characters;
    public ArrayList<Enemy> enemies;

    public Beacon beacon;
    public PlayerInput input;

    public UpgradeManager upgradeManager = new UpgradeManager();

    public GameState state = GameState.MENU; // na start gra w trybie menu

    //okno
    public int screenW;
    public int screenH;

    //timery
    public int frames = 0;
    public int healingTimer = 0;

    //wartości gry
    public int XP;
    public int level;
    public int XPProgression;
    public int bankAccount;

    // upgrade zmienne
    public int gamblerTimer = 0;
    public int confusionMode = 0;

    // beacon upgrade
    public int beaconLvl;
    public int lvlReqMoney;
    public int maxHpBonus;

    //beacon heal
    public int healValue;
    public int healReqMoney;

    //menu opcje
    public int gamemodeOption = 0;
    public int bgOption = 0;

    //waves
    public int wave;
    public int maxEnemiesOnWave = 3;
    public int enemiesSpawned;
    public boolean waveActive = true;
    public boolean endless;

    //muzyka
    SoundUtils music = new SoundUtils();
    SoundUtils sfx = new SoundUtils();
    SoundUtils upgradeMusic = new SoundUtils();
    FightingMusic fMusic = FightingMusic.MUTE;

    //guziki
    Rectangle startBtn = new Rectangle(0, 0, 200, 80); // guzik startu

    Rectangle playerBtn = new Rectangle(0, 0, 180, 60);
    Rectangle twoPlayersBtn = new Rectangle(0, 0, 180, 60);

    Rectangle greenBg = new Rectangle(0, 0, 80, 80);
    Rectangle greyBg = new Rectangle(0, 0, 80, 80);
    Rectangle brownBg = new Rectangle(0, 0, 80, 80);

    Rectangle musicBtn = new Rectangle(0, 0, 140, 40);

    Rectangle beaconUpBtn = new Rectangle(0, 0, 230, 40);
    Rectangle beaconHealBtn = new Rectangle(0, 0, 100, 40);

    Rectangle playagain = new Rectangle (0, 0, 200, 80);

    Rectangle newgame = new Rectangle (0, 0, 200, 80);
    Rectangle endlessmode = new Rectangle (0, 0, 200, 80);

    public MyPanel(ArrayList<Character> characters, ArrayList<Enemy> enemies, Beacon beacon, int w, int h) {
        this.characters = characters;
        this.enemies = enemies;
        this.beacon = beacon;
        this.screenW = w;
        this.screenH = h;

        this.input = new PlayerInput(null, null);

        setFocusable(true);
        setOpaque(true);

        playMusic(Music.MENU);

        // GAME LOOP ~60 FPS
        Timer gameLoop = new Timer(15, e -> updateGame());
        gameLoop.start();

        addKeyListener(input);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int btnx = e.getX(); // x myszki
                int btny = e.getY(); // y myszki

                if(bgOption == 1){
                    setBackground(Color.decode("#4D6620"));
                } else if(bgOption == 2){
                    setBackground(Color.decode("#7D7D7D"));
                } else if(bgOption == 3){
                    setBackground(Color.decode("#4A2F1B"));
                }

                if(playerBtn.contains(btnx,btny)){
                    gamemodeOption = 1;
                    playEffect(Music.BUTTON);
                    repaint();
                }

                if(twoPlayersBtn.contains(btnx,btny)){
                    gamemodeOption = 2;
                    playEffect(Music.BUTTON);
                    repaint();
                }

                if(greenBg.contains(btnx,btny)){
                    bgOption = 1;
                    playEffect(Music.BUTTON);
                    repaint();
                }

                if(greyBg.contains(btnx,btny)){
                    bgOption = 2;
                    playEffect(Music.BUTTON);
                    repaint();
                }

                if(brownBg.contains(btnx,btny)){
                    bgOption = 3;
                    playEffect(Music.BUTTON);
                    repaint();
                }

                if (state == GameState.MENU && gamemodeOption > 0 && bgOption > 0) {
                    if(startBtn.contains(btnx, btny)){

                        playEffect(Music.BUTTON);
                        stopMusic();

                        characters.clear();
                        enemies.clear();

                        wave = 0;
                        XP = 0;
                        level = 1;
                        XPProgression = 500;
                        enemiesSpawned = 0;
                        bankAccount = 0;

                        healValue = 1;
                        healReqMoney = 1;

                        endless = false;

                        //beacon
                        beaconLvl = 1;
                        lvlReqMoney = 100;
                        maxHpBonus = 25;
                        beacon.hpReset();

                        upgradeManager.resetUpgrades();

                        Character p1 = new Character(screenW / 2 - 250, screenH / 2 - 100, false,1);
                        characters.add(p1);

                        Character p2 = null;
                        if(gamemodeOption == 2) {
                            p2 = new Character(screenW / 2 + 100, screenH / 2 - 100, true,2);
                            characters.add(p2);
                        }

                        input.updateCharacters(p1, p2);

                        state = GameState.PLAYING;
                        repaint();
                        fMusic = FightingMusic.MUTE;
                    }
                }
                if (state == GameState.PLAYING) {
                    if (musicBtn.contains(btnx, btny)) {
                        playEffect(Music.BUTTON);
                        stopMusic();

                        if (fMusic == FightingMusic.FIRST) { // jeśli jest pierwsza piosenka
                            fMusic = FightingMusic.SECOND; // po kliknięciu ma być druga
                            playMusic(Music.FIGHTING2);
                        } else if (fMusic == FightingMusic.SECOND) { // jeśli jest druga piosenka
                            fMusic = FightingMusic.MUTE;  // po kliknięciu ma nie być żadnej
                        } else if (fMusic == FightingMusic.MUTE) { // jeśli jest cisza
                            fMusic = FightingMusic.FIRST; // po kliknięciu ma być 1 piosenka
                            playMusic(Music.FIGHTING);
                        }
                        repaint();
                    }
                    if (beaconUpBtn.contains(btnx, btny)){

                        playEffect(Music.BUTTON);

                        if(bankAccount >= lvlReqMoney){
                            bankAccount -= lvlReqMoney;
                            beacon.setMaxHp(beacon.getMaxHp() + maxHpBonus);
                            beaconLvl++;

                            if(beaconLvl == 2){
                                lvlReqMoney = 500;
                                maxHpBonus = 25;
                            } else if(beaconLvl == 3) {
                                lvlReqMoney = 2000;
                                maxHpBonus = 50;
                            } else if(beaconLvl == 4){
                                lvlReqMoney = 5000;
                                maxHpBonus = 100;
                            } else if(beaconLvl == 5){
                                lvlReqMoney = 10000;
                                maxHpBonus = 200;
                            }
                        }
                    }

                    if(beaconHealBtn.contains(btnx, btny)){
                        if (bankAccount >= healReqMoney) {
                            if (beacon.getHp() < beacon.getMaxHp()){
                                bankAccount -= healReqMoney;
                                beacon.setHp(beacon.getHp() + healValue);
                            }
                        }
                    }

                }


                if (state == GameState.UPGRADE) {
                    for (RandomUpgrade u : upgradeManager.getUpgrades()) { // przejście przez tablice ulepszeń

                        if (u.getOption().contains(btnx, btny)) { // jeśli myszką naciśnie się opcję
                            upgradeManager.applyUpgrade(u, characters, enemies, beacon);

                            if (u.getType() != UpgradeType.REROLL){
                                playEffect(Music.GOT_UPGRADE);
                                stopUpgradeMusic();
                                state = GameState.PLAYING;
                                if(fMusic != FightingMusic.MUTE){
                                    resumeMusic();
                                }
                            } else{
                                playEffect(Music.UPGRADE_SHUFFLE);
                            }

                            repaint();
                            break;
                        }
                    }
                }

                if(state == GameState.WON){
                    if(newgame.contains(btnx, btny)){
                        playEffect(Music.BUTTON);

                        if(music != null && upgradeMusic != null){
                            stopMusic();
                            stopUpgradeMusic();
                        }

                        state = GameState.MENU;
                        playMusic(Music.MENU);
                        repaint();
                    } else if(endlessmode.contains(btnx, btny)){
                        playEffect(Music.BUTTON);
                        endless = true;
                        state = GameState.PLAYING;
                    }
                }

                if (state == GameState.LOST){
                    if(playagain.contains(btnx, btny)){
                        playEffect(Music.BUTTON);

                        if(music != null && upgradeMusic != null){
                            stopMusic();
                            stopUpgradeMusic();
                        }

                        state = GameState.MENU;
                        playMusic(Music.MENU);
                        repaint();
                    }
                }
            }
        });
    }

    private void updateGame() {GameEngine.upGame(this);}

    public void playMusic(Music m){
        music.setFile(m.id);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playEffect(Music m) {
        sfx.setFile(m.id);
        sfx.play();
    }

    public void pauseMusic(){
        music.pause();
    }

    public void resumeMusic(){
        music.resume();
    }

    public void playUpgradeMusic(Music m) {
        upgradeMusic.setFile(m.id);
        upgradeMusic.play();
        upgradeMusic.loop();
    }

    public void stopUpgradeMusic() {
        if(upgradeMusic != null){
            upgradeMusic.stop();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // czyści ekran przed rysowaniem
        Graphics2D g2 = (Graphics2D) g; // ulepszone graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // ladniejsze napisy bez pixelów widocznych

        startBtn.x = getWidth() / 2 - 130;
        startBtn.y = getHeight() / 2;

        switch (state) {

            case MENU:

                // tło
                GradientPaint bgGradient = new GradientPaint(0, 0, Color.decode("#4A4747"), 0, getHeight(), Color.decode("#2B2828"));
                g2.setPaint(bgGradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                int centerx = getWidth() / 2;
                int centery = getHeight() / 2;
                int gap = 200;
                int totalWidth = playerBtn.width + gap + twoPlayersBtn.width;
                int startX = centerx - (totalWidth / 2);

                // napis gry
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 35));
                g2.drawString("Survival Defense", (getWidth() - g2.getFontMetrics().stringWidth("Survival Defense")) / 2, getHeight() / 4);

                startBtn.setBounds(centerx - (startBtn.width / 2), centery - 100, startBtn.width, startBtn.height);

                // guzik startu
                g2.setColor(Color.decode("#1F0404"));
                g2.fill(startBtn);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.decode("#140202"));
                g2.draw(startBtn);
                // napis "START" na guziku
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 30));
                g2.drawString("START", startBtn.x + 50, startBtn.y + 50);

                //2 guziki trybów
                twoPlayersBtn.setBounds(startX  + playerBtn.width + gap, centery + 80, twoPlayersBtn.width, twoPlayersBtn.height);
                playerBtn.setBounds(startX, centery + 80, playerBtn.width, playerBtn.height);

                g2.setColor(Color.decode("#1F0404"));
                g2.fill(playerBtn);
                g2.fill(twoPlayersBtn);

                if(gamemodeOption == 1){
                    g2.setColor(Color.decode("#BD3C3C"));
                    g2.fill(playerBtn);
                } else if(gamemodeOption == 2){
                    g2.setColor(Color.decode("#BD3C3C"));
                    g2.fill(twoPlayersBtn);
                }

                // napisy guzików
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 15));
                g2.drawString("1 Player", playerBtn.x + (playerBtn.width - g2.getFontMetrics().stringWidth("1 Player")) / 2, playerBtn.y + 40);
                g2.drawString("2 Players", twoPlayersBtn.x + (twoPlayersBtn.width - g2.getFontMetrics().stringWidth("2 Players")) / 2, twoPlayersBtn.y + 40);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.decode("#140202"));
                g2.draw(playerBtn);
                g2.draw(twoPlayersBtn);

                //kolor mapy guziki
                greenBg.setBounds(centerx - (80 / 2) - 80 - gap, centery + 250, 80, 80);
                greyBg.setBounds(centerx - (80 / 2), centery + 250, 80, 80);
                brownBg.setBounds(centerx + (80 / 2) + gap, centery + 250, 80, 80);

                g2.setColor(Color.WHITE);
                g2.drawString("Map color:", greyBg.x, greyBg.y - 20);
                g2.setColor(Color.decode("#4D6620"));
                g2.fill(greenBg);
                g2.setColor(Color.decode("#7D7D7D"));
                g2.fill(greyBg);
                g2.setColor(Color.decode("#4A2F1B"));
                g2.fill(brownBg);

                if(bgOption == 1){
                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.RED);
                    g2.draw(greenBg);
                } else if(bgOption == 2){
                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.RED);
                    g2.draw(greyBg);
                } else if(bgOption == 3){
                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.RED);
                    g2.draw(brownBg);
                }

                break;

            case PLAYING:

                if (characters != null) { // jeśli lista istnieje
                    for (Character c : characters) {
                        g2.drawImage(c.getImg(), c.getX(), c.getY(), 100, 50, null);
                    }
                }

                if (enemies != null) { // jeśli lista istnieje
                    for (Enemy e : enemies) {
                        g2.drawImage(e.getImg(), e.getX(), e.getY(), 100, 50, null);
                    }
                }

                g2.drawImage(beacon.getImg(), beacon.getX(), beacon.getY(), 40, 40, null);
                g2.setColor(Color.RED);
                g2.drawString(beacon.getHp() + "/" + beacon.getMaxHp() + " hp",  beacon.getX() - 10, beacon.getY() + 60);


                g2.setColor(Color.LIGHT_GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 30));
                g2.drawString("WAVE: " + wave, getWidth() - g2.getFontMetrics().stringWidth("WAVE: ") - 80, getHeight() - 20);

                // in game info
                for(Character c : characters){
                    if(c.getId() == 1){ // gracz 1
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Arial", Font.PLAIN, 20));
                        g2.drawString("P1:", 20, 50);
                        g2.setColor(Color.RED);
                        g2.drawString(c.getHp() + "/" + c.getMaxHp() + " hp", 20, 80);
                        g2.setColor(Color.decode("#17E33C"));
                        g2.drawString(bankAccount + "$", 20, 110);
                        g2.setColor(Color.WHITE);
                        g2.drawString("Killed: "+ c.getEnemiesKilled(), 20, 140);

                    } else if (c.getId() == 2){ // gracz 2
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Arial", Font.PLAIN, 20));

                        int rightPanelX = getWidth() - 120;

                        g2.drawString("P2:", rightPanelX, 50);
                        g2.setColor(Color.RED);
                        g2.drawString(c.getHp() + "/" + c.getMaxHp() + " hp", rightPanelX, 80);
                        g2.setColor(Color.decode("#17E33C"));
                        g2.drawString(bankAccount + "$", rightPanelX, 110);
                        g2.setColor(Color.WHITE);
                        g2.drawString("Killed: " + c.getEnemiesKilled(), rightPanelX, 140);
                    }
                }

                //wspolne xp
                int fillWidth = (int)((double) XP / XPProgression * 200);
                Color col = Color.decode("#8DA6A8");
                g2.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 110));
                g2.fillRect(getWidth() / 2 - 100, 80,200, 30);
                g2.setColor(Color.WHITE);
                g2.drawRect(getWidth() / 2 - 100, 80,200, 30);
                g2.setColor(Color.decode("#6D95BF"));
                g2.fillRect(getWidth() / 2 - 100, 80, fillWidth, 30);
                g2.drawString(XP + " / " + XPProgression, getWidth() / 2 - 30, 60);
                g2.drawString("lvl: " + level, getWidth() / 2 - 20, 140);

                // guzik z muzyką
                musicBtn.setBounds(15, getHeight() - 50, 140, 40);

                g2.setColor (Color.decode("#1F0404"));
                g2.fill(musicBtn);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.draw(musicBtn);

                g2.setFont(new Font("Verdana", Font.BOLD, 12));
                String txt = "CURRENT: ";
                switch(fMusic){
                    case FIRST:
                        txt += "SONG 1";
                        break;
                    case SECOND:
                        txt += "SONG 2";
                        break;
                    case MUTE:
                        txt += "NONE";
                        break;
                }
                g2.drawString(txt, musicBtn.x + 10, musicBtn.y + 25);

                // beacon upgrade
                beaconUpBtn.setBounds(175, getHeight() - 50, 230, 40);

                g2.setColor(Color.decode("#6A8A94"));
                g2.fill(beaconUpBtn);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.draw(beaconUpBtn);
                g2.setFont(new Font("Verdana", Font.BOLD, 12));
                String upTxt = "Max hp (lvl: " + beaconLvl + ", + " + maxHpBonus + "hp) " + lvlReqMoney + "$";
                if(beaconLvl > 5){
                    upTxt = "MAX";
                }
                g2.drawString(upTxt, beaconUpBtn.x + 10, beaconUpBtn.y + 25);

                // beacon heal
                beaconHealBtn.setBounds(420, getHeight() - 50, 100, 40);

                g2.setColor(Color.decode("#649E5A"));
                g2.fill(beaconHealBtn);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.draw(beaconHealBtn);
                g2.setFont(new Font("Verdana", Font.BOLD, 12));
                g2.drawString("Heal Beacon", beaconHealBtn.x + 10, beaconHealBtn.y + 25);

                for(Character c : characters){
                    if(c.isAttacking()){
                        Image imgDirection;
                        int xAlign;

                        if(c.isLookingRight()){
                            imgDirection = c.getAttackImgR();
                            xAlign = 60;
                        }else {
                            imgDirection = c.getAttackImgL();
                            xAlign = -40;
                        }

                        g2.drawImage(imgDirection, c.getX() + xAlign, c.getY(), 80, 50, null);
                    }
                    /*
                    // hitbox check
                    if (c.isAttacking()) {
                        int hitboxX;
                        if (c.isLookingRight()) {
                            hitboxX = c.getX() + 70;
                        } else {
                            hitboxX = c.getX() - 30;
                        }
                        g2.setColor(Color.RED);
                        g2.drawRect(hitboxX, c.getY(), 60, 50);
                    }*/
                }

                break;

            case UPGRADE:

                g2.setColor(Color.decode("#242E30"));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 40));
                g2.drawString("UPGRADES", getWidth() / 2 - 110, 80);

                int cardWidth = 250;
                int cardHeight = 400;
                int gg = 20;

                int tWidth = (cardWidth * 3) + (gg * 2);
                int starttX = (getWidth() - tWidth) / 2;

                int index = 0;
                // karty
                for(RandomUpgrade u : upgradeManager.getUpgrades()){

                    int xx = starttX + (index * (cardWidth + gg));
                    int yy = 170;

                    Rectangle card = new Rectangle(xx, yy, cardWidth, cardHeight);

                    u.setOption(card);

                    UpgradeType type = u.getType();
                    // tło
                    g2.setColor(Color.decode("#303A45"));
                    g2.fill(card);

                    // tło pod tytuł
                    g2.setColor(Color.decode("#56676E"));
                    g2.fillRect(card.x, card.y, card.width, 60);

                    // ramka (rzadkość)
                    g2.setColor(u.getColor());
                    g2.setStroke(new BasicStroke(4));
                    g2.draw(card);

                    // ramka na tytuł
                    g2.setStroke(new BasicStroke(4));
                    g2.drawLine(card.x, card.y + 60, card.x + card.width, card.y + 60);

                    // teksty
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 20));
                    g2.drawString(u.getName(), card.x + 20, card.y + 40);

                    g2.setFont(new Font("Arial", Font.PLAIN, 18));
                    FontMetrics fm = g2.getFontMetrics();
                    String text = type.description;
                    String oneLine = "";
                    int y = card.y + 100;
                    int maxWidth = card.width - 40;
                    for(String word : text.split(" ")){
                        String whole = oneLine + word + " ";
                        if(fm.stringWidth(whole) > maxWidth){
                            g2.drawString(oneLine, card.x + 20, y);
                            y += fm.getHeight();
                            oneLine = word + " ";
                        } else{
                            oneLine = whole;
                        }
                    }
                    g2.drawString(oneLine, card.x + 20, y);

                    if(type.hasValue) {
                        g2.drawString("Amount: " + u.getValue(), card.x + 20, card.height - 30);
                    }

                    index++;
                }

                g2.setColor(Color.decode("#1F0404"));
                g2.fillRect(0,0,250, getHeight());
                g2.fillRect(getWidth() - 250,0,250, getHeight());


                for(Character c : characters) {
                    // gracz 1 info
                    if(c.getId() == 1) {
                        g2.setColor(Color.RED);
                        g2.setFont(new Font("Arial", Font.PLAIN, 20));
                        g2.drawString("P1:", 20, 50);
                        g2.setColor(Color.WHITE);
                        g2.drawString("Current hp: " + c.getHp(), 20, 110);
                        g2.drawString("Max hp: " + c.getMaxHp(), 20, 170);
                        g2.drawString("Current money: " + c.getMoney() + "$", 20, 230);
                        g2.drawString("Current damage: " + c.getDamage(), 20, 290);
                        g2.drawString("Current speed: " + c.getSpeed(), 20, 350);
                        g2.drawString("Regen value: " + c.getRegen(), 20, 410);

                    // gracz 2 info
                    } else if(c.getId() == 2){
                        g2.setColor(Color.RED);
                        g2.setFont(new Font("Arial", Font.PLAIN, 20));
                        int statsX = getWidth() - 230;

                        g2.drawString("P2:", statsX, 50);
                        g2.setColor(Color.WHITE);
                        g2.drawString("Current hp: " + c.getHp(), statsX, 110);
                        g2.drawString("Max hp: " + c.getMaxHp(), statsX, 170);
                        g2.drawString("Current money: " + c.getMoney() + "$", statsX, 230);
                        g2.drawString("Current damage: " + c.getDamage(), statsX, 290);
                        g2.drawString("Current speed: " + c.getSpeed(), statsX, 350);
                        g2.drawString("Regen value: " + c.getRegen(), statsX, 410);
                    }
                }
                break;

            case WON:
                // tło
                bgGradient = new GradientPaint(0, 0, Color.decode("#4A4747"), 0, getHeight(), Color.decode("#2B2828"));
                g2.setPaint(bgGradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                int wx = getWidth() / 2;
                int wy = getHeight() / 2;

                // napis gry
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 35));
                g2.drawString("YOU WON", (getWidth() - g2.getFontMetrics().stringWidth("YOU WON")) / 2, getHeight() / 4);

                newgame.setBounds(wx - (newgame.width / 2), wy - 100, newgame.width, newgame.height);
                endlessmode.setBounds(wx - (endlessmode.width / 2), wy + 100, endlessmode.width, endlessmode.height);

                // guzik nowej gry
                g2.setColor(Color.decode("#1F0404"));
                g2.fill(newgame);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.decode("#140202"));
                g2.draw(newgame);
                // napis
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 20));
                int nStringWidth = g2.getFontMetrics().stringWidth("NEW GAME");
                g2.drawString("NEW GAME", newgame.x + (newgame.width - nStringWidth) / 2, newgame.y + (newgame.height / 2) + 10);

                // guzik endless mode
                g2.setColor(Color.decode("#1F0404"));
                g2.fill(endlessmode);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.decode("#140202"));
                g2.draw(endlessmode);
                // napis
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 20));
                int eStringWidth = g2.getFontMetrics().stringWidth("ENDLESS");
                g2.drawString("ENDLESS", endlessmode.x + (endlessmode.width - eStringWidth) / 2, endlessmode.y + (endlessmode.height / 2) + 10);

                break;


            case LOST:
                // tło
                bgGradient = new GradientPaint(0, 0, Color.decode("#4A4747"), 0, getHeight(), Color.decode("#2B2828"));
                g2.setPaint(bgGradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                // napis gry
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 35));
                g2.drawString("GAME OVER", (getWidth() - g2.getFontMetrics().stringWidth("GAME OVER")) / 2, getHeight() / 4);

                playagain.setBounds(cx - (playagain.width / 2), cy - 100, playagain.width, playagain.height);

                // guzik startu
                g2.setColor(Color.decode("#1F0404"));
                g2.fill(playagain);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.decode("#140202"));
                g2.draw(playagain);
                // napis "PLAY AGAIN" na guziku
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Verdana", Font.BOLD, 20));
                int stringWidth = g2.getFontMetrics().stringWidth("PLAY AGAIN");
                g2.drawString("PLAY AGAIN", playagain.x + (playagain.width - stringWidth) / 2, playagain.y + (playagain.height / 2) + 10);

                break;

        }
    }
}