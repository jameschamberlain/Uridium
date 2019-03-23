package net.uridium.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Audio {
    private static Audio instance = new Audio();
    public static Audio getAudioInstance() { return instance; }
    private Audio() {}

    public void libPlayLoop(String fileLocation){
        Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        wavSound.loop(masterVolume);
    }

    public void libPlay(String fileLocation){
        Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        wavSound.play(masterVolume);
    }

    public static float masterVolume = 0.1f;

    public float getMasterVolume(){return masterVolume;}

    public void setMasterVolume(float newVolume){
        if (!(newVolume<0f || newVolume>1.0f)){
            masterVolume = newVolume;

        }
    }

    public void lowerMasterVolume(){
        float newVolume = getMasterVolume() -0.1f;
        setMasterVolume(newVolume);
    }

    public void increaseMasterVolume(){
        float newVolume = getMasterVolume() +0.1f;
        setMasterVolume(newVolume);
    }

    public void muteMasterVolume(){setMasterVolume(0f);}

}
