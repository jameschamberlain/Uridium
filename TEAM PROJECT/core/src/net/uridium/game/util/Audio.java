package net.uridium.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Audio {
    public static Audio instance = new Audio();
    public static Audio getAudioInstance() { return instance; }
    public Audio() {}

    public void libPlayLoop(String fileLocation){
        Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        wavSound.loop(masterVolume);
    }

    public void libPlay(String fileLocation){
        Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        wavSound.play(masterVolume);
    }


    public static float masterVolume = 0.1f;

    public void setMasterVolume(float newVolume, String fileLocation){
       // Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        masterVolume = newVolume;
        //wavSound.pause();
    }

    public void muteMasterVolume(float mute, String fileLocation){
        Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        setMasterVolume(0f, fileLocation);
        wavSound.pause();
    }

}
