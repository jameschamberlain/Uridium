package net.uridium.game.server.msg;

import java.io.Serializable;

public class PlayerScoreData implements Serializable {

    public int playerID;
    public int score;

    public PlayerScoreData(int playerID, int score) {
        this.playerID = playerID;
        this.score = score;
    }
}