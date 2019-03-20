package net.uridium.game.server.msg;

import java.io.Serializable;

public class PlayerUpdateData implements Serializable {

    public int playerID;
    public int score;
    public int level;
    public float xp;
    public float xpToLevelUp;
    public boolean levelledUp;

    public PlayerUpdateData(int playerID, int score, int level, float xp, float xpToLevelUp, boolean levelledUp) {
        this.playerID = playerID;
        this.score = score;
        this.level = level;
        this.xp = xp;
        this.xpToLevelUp = xpToLevelUp;
        this.levelledUp = levelledUp;
    }
}