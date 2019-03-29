package net.uridium.game.server.msg;

import java.io.Serializable;

/**
 * The type Player health data.
 */
public class PlayerHealthData implements Serializable {
    /**
     * The Player id.
     */
    public int playerID;
    /**
     * The Health.
     */
    public float health;
    /**
     * The Max health.
     */
    public float maxHealth;

    /**
     * Instantiates a new Player health data.
     *
     * @param playerID  the player id
     * @param health    the health
     * @param maxHealth the max health
     */
    public PlayerHealthData(int playerID, float health, float maxHealth) {
        this.playerID = playerID;
        this.health = health;
        this.maxHealth = maxHealth;
    }
}