package net.uridium.game.util;

import java.applet.Applet;
import java.applet.AudioClip;

import com.sun.org.apache.xpath.internal.operations.Bool;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Audio {
    private static Audio instance = new Audio();
    public static Audio getAudioInstance() { return instance; }
    private Audio() {}

    public void backgroundPlay()
    {
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;

        ContinuousAudioDataStream loop = null;

        try
        {
            InputStream fileToBePlayed = new FileInputStream("C:\\Users\\Rahul Kalia\\Downloads\\URIDIUM_AUDIO\\background.wav");
            BGM = new AudioStream(fileToBePlayed);
            AudioPlayer.player.start(BGM);
        }
        catch(FileNotFoundException e){
            System.out.print(e.toString());
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
        MGP.start(loop);
    }

    public void play(Boolean loopingAudio, String fileLocation){
        AudioStream BGM;
        AudioData MD;
        AudioPlayer MGP = AudioPlayer.player;

        ContinuousAudioDataStream loop = null;

        try
        {
                InputStream test = new FileInputStream(fileLocation);
                BGM = new AudioStream(test);
                AudioPlayer.player.start(BGM);

            }
        catch(FileNotFoundException e){
                System.out.print(e.toString());
            }
        catch(IOException error)
        {
                System.out.print(error.toString());
            }


        if (loopingAudio = true){
            MGP.start(loop);
        }

    }

}
