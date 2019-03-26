package net.uridium.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

import static net.uridium.game.util.Audio.SOUND.*;

public class Audio {
    enum SOUND {
        BUTTON_CLICK,
        PLAYER_SHOOT,
        PLAYER_DAMAGE,
        PLAYER_DEAD,
        ENEMY_DEAD,
        CHANGE_ROOM,
        GAME_OVER,
        VICTORY
    }

    private static Audio instance = new Audio();
    public static Audio getAudio() { return instance; }
    private Audio() {}

    private boolean muted = false;
    private float volume = 0.5f;

    private Music theme;

    private HashMap<SOUND, Sound> sounds;

    public void init() {
        theme = Assets.get("audio/background.wav", Music.class);
        theme.setLooping(true);

        sounds = new HashMap<>();
//        sounds.put(BUTTON_CLICK, Assets.get("audio/BUTTON_CLICK.wav", Sound.class));
//        sounds.put(PLAYER_SHOOT, Assets.get("audio/PLAYER_SHOOT.wav", Sound.class));
//        sounds.put(PLAYER_DAMAGE, Assets.get("audio/PLAYER_DAMAGE.wav", Sound.class));
//        sounds.put(PLAYER_DEAD, Assets.get("audio/PLAYER_DEAD.wav", Sound.class));
//        sounds.put(ENEMY_DEAD, Assets.get("audio/ENEMY_DEAD.wav", Sound.class));
//        sounds.put(CHANGE_ROOM, Assets.get("audio/CHANGE_ROOM.wav", Sound.class));
//        sounds.put(GAME_OVER, Assets.get("audio/GAME_OVER.wav", Sound.class));
//        sounds.put(VICTORY, Assets.get("audio/VICTORY.wav", Sound.class));
    }

    public void playTheme() {
        if(!muted) theme.play();
    }

    public void playSound(SOUND sound) {
        this.playSound(sound, false);
    }

    public void playSound(SOUND sound, boolean loop){
        if(muted) return;

//        Sound s = sounds.get(sound);
//
//        if(loop) s.loop(volume);
//        else s.play(volume);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void toggleMute() {
        muted = !muted;

        if(muted) {
            for (Sound s : sounds.values()) s.stop();
            theme.stop();
        } else {
            playTheme();
        }
    }

}
