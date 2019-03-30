package net.uridium.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

import static net.uridium.game.util.Audio.SOUND.*;

/**
 * Class used to manage audio throughout the game, uses a singleton pattern to prevent multiple instances being created
 */
public class Audio {

    /**
     * Different sound effect identifiers
     */
    public enum SOUND {
        BUTTON_CLICK,
        PLAYER_SHOOT,
        PLAYER_DAMAGE,
        PLAYER_DEAD,
        ENEMY_DEAD,
        CHANGE_ROOM,
        GAME_OVER,
        VICTORY,
        POWERUP
    }


    private static Audio instance = new Audio();
    public static Audio getAudio() { return instance; }
    private Audio() {}

    /**
     * Whether the game is muted
     */
    private boolean muted = false;

    /**
     * The volume of the game, from 0 to 1
     */
    private float volume = 0;

    /**
     * The theme music to play
     */
    private Music theme;

    /**
     * A hashmap of sounds which links sounds to their enum identifies, see {@link SOUND}
     */
    private HashMap<SOUND, Sound> sounds;

    /**
     * Initialises the Audio instance
     */
    public void init() {
        theme = Assets.get("audio/background.wav", Music.class);
        theme.setLooping(true);
        theme.setVolume(volume * 0.6f);

        sounds = new HashMap<>();
        sounds.put(BUTTON_CLICK, Assets.get("audio/BUTTON_CLICK.wav", Sound.class));
        sounds.put(PLAYER_SHOOT, Assets.get("audio/PLAYER_SHOOT.wav", Sound.class));
        sounds.put(PLAYER_DAMAGE, Assets.get("audio/PLAYER_DAMAGE.wav", Sound.class));
        sounds.put(PLAYER_DEAD, Assets.get("audio/PLAYER_DEAD.wav", Sound.class));
        sounds.put(ENEMY_DEAD, Assets.get("audio/ENEMY_DEAD.wav", Sound.class));
        sounds.put(CHANGE_ROOM, Assets.get("audio/CHANGE_ROOM.ogg", Sound.class));
        sounds.put(GAME_OVER, Assets.get("audio/GAME_OVER.wav", Sound.class));
//        sounds.put(VICTORY, Assets.get("audio/VICTORY.wav", Sound.class));
        sounds.put(POWERUP, Assets.get("audio/POWERUP.wav", Sound.class));
    }

    /**
     * Play the theme tune, starts the theme tune if the game is not muted
     */
    public void playTheme() {
        if(!muted) theme.play();
    }

    /**
     * Play a sound, no looping
     * @param sound Sound to play
     */
    public void playSound(SOUND sound) {
        this.playSound(sound, false);
    }

    /**
     * Play a sound
     * @param sound Sound to play
     * @param loop Whether to loop the sound or not
     */
    public void playSound(SOUND sound, boolean loop){
        if(muted) return;

        Sound s = sounds.get(sound);

        try {
            if (loop) s.loop(volume);
            else s.play(volume);
        } catch (Exception e) {}
    }

    /**
     * @return The volume of the game
     */
    public float getVolume() {
        return volume;
    }

    /**
     * @param volume The new volume of the game
     */
    public void setVolume(float volume) {
        this.volume = volume;
        theme.setVolume(volume * 0.8f);
    }

    /**
     * Toggle mute in the game, when muting stops all sound effects and the theme tune
     */
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
