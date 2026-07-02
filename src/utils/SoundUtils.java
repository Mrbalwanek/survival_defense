package utils;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundUtils {
    Clip clip;
    long currentTime = 0;
    URL[] sounds = new URL[10];

    public SoundUtils(){
        sounds[0] = getClass().getResource("/sounds/menu.wav");
        sounds[1] = getClass().getResource("/sounds/game_bg1.wav");
        sounds[2] = getClass().getResource("/sounds/game_bg2.wav");
        sounds[3] = getClass().getResource("/sounds/upgrades_menu.wav");
        sounds[4] = getClass().getResource("/sounds/start_for_cards.wav");
        sounds[5] = getClass().getResource("/sounds/got_upgrade.wav");
        sounds[6] = getClass().getResource("/sounds/slash.wav");
        sounds[7] = getClass().getResource("/sounds/button_click.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(sounds[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch(Exception e){
            System.out.println("Błąd ładowania dźwięku: " + e.getMessage());
        }
    }

    public void play() {
        if (this.clip != null) {
            clip.start();
        }
    }

    public void loop() {
        if (this.clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if(clip != null) {
            clip.stop();
        }
    }

    public void pause() {
        if(clip != null && clip.isRunning()){
            currentTime = clip.getMicrosecondPosition(); // zapamietanie czasu na jakim sie skonzczyla piosenka
            clip.stop();
        }
    }

    public void resume() {
        if(clip != null){
            clip.setMicrosecondPosition(currentTime);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
