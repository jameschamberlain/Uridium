package net.uridium.game.server.msg;

import java.io.Serializable;

/**
 * The type Player update data.
 */
public class PlayerUpdateData implements Serializable {

    /**
     * The Player id.
     */
    public int playerID;
    /**
     * The Score.
     */
    public int score;
    /**
     * The Level.
     */
    public int level;
    /**
     * The Xp.
     */
    public float xp;
    /**
     * The Xp to level up.
     */
    public float xpToLevelUp;
    /**
     * The Levelled up.
     */
    public boolean levelledUp;

    /**
     * Instantiates a new Player update data.
     *
     * @param playerID    the player id
     * @param score       the score
     * @param level       the level
     * @param xp          the xp
     * @param xpToLevelUp the xp to level up
     * @param levelledUp  the levelled up
     */
    public PlayerUpdateData(int playerID, int score, int level, float xp, float xpToLevelUp, boolean levelledUp) {
        this.playerID = playerID;
        this.score = score;
        this.level = level;
        this.xp = xp;
        this.xpToLevelUp = xpToLevelUp;
        this.levelledUp = levelledUp;
    }
}