package graphics;

import java.awt.Rectangle;
import entities.Enemy;
import entities.Character;
import enums.EnemyType;
import enums.GameState;
import enums.Music;

public class GameEngine {

    public static void upGame(MyPanel panel) {

        if (panel.state != GameState.PLAYING) return; // jeśli stan gry nie jest playing to ma nic się nie dziać; freeze gry

        boolean isAnyoneAlive = false;

        if(panel.characters != null && !panel.characters.isEmpty()){
            for(Character c : panel.characters){
                if(c.getHp() > 0){
                    isAnyoneAlive = true;
                    break;
                }
            }
        }

        if(panel.beacon.getHp() <= 0 || !isAnyoneAlive){
            panel.state = GameState.LOST;
            panel.repaint();
            return;
        }

        if (panel.waveActive) {
            panel.frames++;
            if (panel.frames >= 90 && panel.maxEnemiesOnWave > panel.enemiesSpawned) { // co 1.5 sekundy
                panel.frames = 0;

                int spawnX = (int) (Math.random() * panel.getWidth());

                if (Math.random() < 0.5) { // rzut monetą 50/50
                    spawnX = -50; // lewa strona mapy
                } else {
                    spawnX = panel.getWidth() + 50; // prawa strona mapy
                }

                int spawnY = (int) (Math.random() * panel.getHeight());

                EnemyType type = EnemyType.NORMAL;
                double random = Math.random();

                if(panel.wave >= 100 && !panel.endless){

                    panel.state = GameState.WON;

                } else if (panel.wave >= 75) {
                    if (random < 0.65) { // 65% szans
                        type = EnemyType.CORRUPTED_BOSS;
                    } else if (random < 0.95) { // 30% szans
                        type = EnemyType.BOSS;
                    } else { // 5% szans
                        type = EnemyType.THEBEST;
                    }
                } else if (panel.wave >= 45) {
                    if (random < 0.3) {  // 30% szans
                        type = EnemyType.CORRUPTED_BOSS;
                    } else if (random < 0.6) {  // 30% szans
                        type = EnemyType.BOSS;
                    } else {  // 40% szans
                        type = EnemyType.THEBEST;
                    }
                } else if (panel.wave >= 35) {
                    if (random < 0.4) { // 40% szans
                        type = EnemyType.BOSS;
                    } else if (random < 0.8) { // 40% szans
                        type = EnemyType.THEBEST;
                    } else { // 20% szans
                        type = EnemyType.STRONGER;
                    }
                } else if (panel.wave >= 25) {
                    if (random < 0.2) { // 20% szans
                        type = EnemyType.BOSS;
                    } else if (random < 0.7) { // 50% szans
                        type = EnemyType.THEBEST;
                    } else { // 30% szans
                        type = EnemyType.STRONGER;
                    }
                } else if (panel.wave >= 15) {
                    if (random < 0.4) { // 40% szans
                        type = EnemyType.THEBEST;
                    } else { // 60% szans
                        type = EnemyType.STRONGER;
                    }
                } else if (panel.wave >= 9) {
                    if (random < 0.7) { // 70% szans
                        type = EnemyType.STRONGER;
                    }
                } else if (panel.wave >= 3) {
                    if (random < 0.3) { // 30% szans
                        type = EnemyType.STRONGER;
                    }
                }

                Enemy e1 = new Enemy(spawnX, spawnY, type);
                panel.enemies.add(e1);

                panel.enemiesSpawned++;
            }
        }

        panel.healingTimer++;

        if (panel.healingTimer >= 300) { // co 5 sekund
            panel.healingTimer = 0;

            for (Character c : panel.characters) {
                if (panel.upgradeManager.isNoRegen()) {
                    c.setRegen(0);
                    continue;
                }

                if (c.getHp() < c.getMaxHp()) {
                    c.setHp(c.getHp() + c.getRegen());

                    if (c.getHp() > c.getMaxHp()) {
                        c.setHp(c.getMaxHp());
                    }
                }
            }
        }

        if (panel.XP >= panel.XPProgression) {

            panel.playEffect(Music.UPGRADE_SHUFFLE);
            panel.pauseMusic();
            panel.playUpgradeMusic(Music.UPGRADE);

            panel.upgradeManager.randomizedUpgrades();
            panel.state = GameState.UPGRADE;

            panel.XP = 0;
            panel.XPProgression += 500;
            panel.level += 1;

            if(panel.XPProgression >= 8000){
                panel.XPProgression += 3500;
            } else if(panel.XPProgression >= 5000){
                panel.XPProgression += 2500;
            } else if (panel.XPProgression >= 2000) {
                panel.XPProgression += 1500;
            } else if (panel.XPProgression > 1000) {
                panel.XPProgression += 1000;
            }
        }

        if (panel.upgradeManager.isDrunk()) {
            panel.gamblerTimer++;

            if (panel.gamblerTimer >= 600 && panel.gamblerTimer <= 4200) { // 10 sekund (600 klatek) na start trybu i potem tryb działa przez minutę (3600 + 600)
                if (panel.gamblerTimer % 600 == 0) {
                    panel.confusionMode = (int) (Math.random() * 3);
                }
            } else if (panel.gamblerTimer > 4200) {
                panel.upgradeManager.setDrunk(false);
                panel.confusionMode = 0;
                panel.gamblerTimer = 0;
            }
        }

        for (int i = 0; i < panel.characters.size(); i++) {
            Character p = panel.characters.get(i);

            if (p.getHp() <= 0) {
                panel.characters.remove(i);
                i--;
                continue;
            }

            int speed = p.getSpeed();

            if(panel.upgradeManager.isStillStanding() && p.getHp() <= (p.getMaxHp() / 5)) {
                speed += 2;
            }

            boolean moveUp = false;
            boolean moveDown = false;
            boolean moveLeft = false;
            boolean moveRight = false;
            boolean isAttacking = false;

            Character otherPlayer = null;

            if(p.getId() == 1){
                moveUp = panel.input.p1Up;
                moveDown = panel.input.p1Down;
                moveLeft = panel.input.p1Left;
                moveRight = panel.input.p1Right;
                isAttacking = panel.input.p1Attack;

                for(Character c : panel.characters){
                    if(c.getId() == 2){
                        otherPlayer = c;
                    }
                }
            } else if(p.getId() == 2){
                moveUp = panel.input.p2Up;
                moveDown = panel.input.p2Down;
                moveLeft = panel.input.p2Left;
                moveRight = panel.input.p2Right;
                isAttacking = panel.input.p2Attack;

                for(Character c : panel.characters){
                    if(c.getId() == 1){
                        otherPlayer = c;
                    }
                }
            }

            int moveX = 0;
            int moveY = 0;

            if(panel.upgradeManager.isDrunk() && panel.gamblerTimer >= 600) {
                speed += 2;

                if(panel.confusionMode == 0){  // normalne
                    if (moveUp) moveY -= speed;
                    if (moveDown) moveY += speed;
                    if (moveLeft) moveX -= speed;
                    if (moveRight) moveX += speed;
                } else if (panel.confusionMode == 1) { // odwrotne (odbicie lustrzane)
                    if (moveUp) moveY += speed;
                    if (moveDown) moveY -= speed;
                    if (moveLeft) moveX += speed;
                    if (moveRight) moveX -= speed;
                } else if (panel.confusionMode == 2) { // obrócone
                    if (moveUp) moveX -= speed;
                    if (moveDown) moveX += speed;
                    if (moveLeft) moveY += speed;
                    if (moveRight) moveY -= speed;
                }
            } else {  // normalny ruch (gdy karta nie jest aktywna lub trwa 10s czekania)
                if (moveUp) moveY -= speed;
                if (moveDown) moveY += speed;
                if (moveLeft) moveX -= speed;
                if (moveRight) moveX += speed;
            }

            if (moveX > 0) {
                p.setLookingRight(true);
            } else if (moveX < 0) {
                p.setLookingRight(false);
            }

            int newX = p.getX() + moveX;// wpółrzędne w poziomie by później zmienić je jak bedzie klikniety odpowiedni przycisk
            int newY = p.getY() + moveY; // wpółrzędne w pionie by później zmienić je jak bedzie klikniety odpowiedni przycisk

            p.setAttackCooldown(p.getAttackCooldown() + 1);

            if (isAttacking && p.isCanAttack() && p.getAttackVisible() == 0){
                if (p.getAttackCooldown() >= 30) {
                    panel.playEffect(Music.SLASH);
                    p.setAttacking(true);
                    p.setAttackVisible(10);
                    p.setAttackCooldown(0);
                    p.setCanAttack(false);
                }

                int swordX = 0;
                if(p.isLookingRight()){
                    swordX = p.getX() + 70;
                } else{
                    swordX = p.getX() - 30;
                }
                Rectangle swordAttack = new Rectangle(swordX, p.getY(), 60, 50);

                if(panel.upgradeManager.isHealTouch() && panel.characters.size() > 1){
                    if(swordAttack.intersects(otherPlayer.getBoundaries()) && p.getHp() > 1 && otherPlayer.getHp() < otherPlayer.getMaxHp()) {
                        otherPlayer.setHp(otherPlayer.getHp() + 1);
                        p.setHp(p.getHp() - 1);
                    }
                }
                for(Enemy e : panel.enemies) {
                    if (swordAttack.intersects(e.getBoundaries())) {
                        int damageToSend = (int) p.getDamage();

                        if(panel.upgradeManager.isStillStanding() && p.getHp() <= (p.getMaxHp() / 5)) {
                            damageToSend = (int) (damageToSend * 1.75);
                        }

                        e.takeDmg(damageToSend);

                        if (panel.upgradeManager.isVampire()) {
                            if(p.getHp() < p.getMaxHp()) {
                                p.setHp(p.getHp() + 1);
                            }
                        }
                    }
                }
            }
            if(!isAttacking){
                p.setCanAttack(true);
            }

            int bouncePower = speed * 25;
            boolean canMove = true; // mozesz sie ruszyc

            Rectangle barrierInFuture = new Rectangle(newX + 37, newY + 12, 24, 24);  // 24px w rzeczywistosci = 25px

            if(otherPlayer != null) { // jeśli istnieje 2 gracz
                if (barrierInFuture.intersects(otherPlayer.getBoundaries())) {
                    canMove = false; // nie mozesz sie ruszyc
                }
            }

            if(barrierInFuture.intersects(panel.beacon.getBoundaries())){
                if (panel.upgradeManager.isBouncyB()) {
                    if (moveY < 0) newY += bouncePower; // gora -> odbij w doł
                    if (moveY > 0) newY -= bouncePower; // dol -> odbij w gore
                    if (moveX < 0) newX += bouncePower; // lewo -> odbij w prawo
                    if (moveX > 0) newX -= bouncePower; // prawo -> odbij w lewo
                    canMove = true;
                } else if(panel.upgradeManager.isBeaconPad()){
                    if (moveY < 0) newY -= bouncePower;
                    if (moveX < 0) newX -= bouncePower;
                    if (moveY > 0) newY += bouncePower;
                    if (moveX > 0) newX += bouncePower;
                } else {
                    canMove = false;
                }
            }
            if (canMove) { // jeśli możesz się ruszyć
                Rectangle bounds = p.getBoundaries();

                if (bounds.x + bounds.width < 0) newX = panel.screenW - (bounds.x - p.getX()); // lewo -> prawo
                if (bounds.y + bounds.height < 0) newY = panel.screenH - (bounds.y - p.getY()); //gora -> dol
                if (bounds.x > panel.screenW) newX = -bounds.width - (bounds.x - p.getX()); // prawo -> lewo
                if (bounds.y > panel.screenH) newY = -bounds.height - (bounds.y - p.getY()); // dol -> gora

                p.setX(newX);// ustaw współrzędne w poziomie na nowe współrzędne
                p.setY(newY); //ustaw współrzędne w pionie na nowe współrzędne
            }
        }

        for(Character c : panel.characters){
            if (c.getAttackVisible() > 0) {
                c.setAttackVisible(c.getAttackVisible() - 1);
            } else {
                c.setAttacking(false);
            }
        }

        for(int i = panel.enemies.size() - 1; i >= 0; i--){
            Enemy e = panel.enemies.get(i);

            e.update(panel.beacon, panel.characters, panel.upgradeManager);

            if(e.getBoundaries().intersects(panel.beacon.getBoundaries())){
                double directionX = e.getX() - panel.beacon.getX(); // na + to prawo na - lewo
                double directionY = e.getY() - panel.beacon.getY();  // na + to góra na - dół

                double distance = Math.sqrt(directionX * directionX + directionY * directionY); // pitagoras a² + b² = c² -> c sqrt

                int enemyBounce = 170;

                if(distance != 0){
                    e.setX(e.getX() + (int)((directionX / distance) * enemyBounce));
                    e.setY(e.getY() + (int)((directionY / distance) * enemyBounce));
                }
            }

            if(panel.enemies.get(i).getHp() <= 0){
                int XPFromEnemy = e.getEnemyXpValue();

                if(panel.upgradeManager.isExtraXP()){
                    XPFromEnemy += panel.upgradeManager.getXPValue();
                }

                if (panel.upgradeManager.isDrunk() && panel.gamblerTimer >= 600) {
                    XPFromEnemy *= 2;
                }

                panel.XP += XPFromEnemy;

                int reward = e.getEnemyGoldValue();

                if(panel.upgradeManager.isExtraGold()){
                    reward += 25;
                }

                Character p1 = null;
                Character p2 = null;
                for(Character c : panel.characters) {
                    if(c.getId() == 1) p1 = c;
                    if(c.getId() == 2) p2 = c;
                }

                if(panel.input.p1Attack && p1 != null){
                    p1.setEnemiesKilled(p1.getEnemiesKilled() + 1);
                } else if(panel.input.p2Attack && p2 != null){
                    p2.setEnemiesKilled(p2.getEnemiesKilled() + 1);
                } else{  // jeśli przeciwnik nie zginął od żadnego z graczy

                    int reduced = reward / 2; // połowa kasy bo nikt nie zabił

                    if (!panel.input.p1Attack && !panel.input.p2Attack) {
                        reward = reduced;
                    }
                }

                panel.bankAccount += reward;
                panel.enemies.remove(i);
            }

            if(panel.enemiesSpawned >= panel.maxEnemiesOnWave && panel.enemies.isEmpty()){
                panel.wave += 2;
                panel.enemiesSpawned = 0;
                double random = Math.random();
                if(random < 0.4){ // 40%
                    panel.maxEnemiesOnWave += 1;
                } else if (random < 0.6){ // 20%
                    panel.maxEnemiesOnWave += 2;
                }
                panel.frames = -120; // 2 sekundy przerwy między falami
            }
        }

        panel.repaint();
    }
}