package net.uridium.game.server.msg;

import net.uridium.game.gameplay.entity.damageable.Player;

import java.io.Serializable;

/**
 * The type Player powerup data.
 */
public class PlayerPowerupData implements Serializable {
    /**
     * The Player id.
     */
    public int playerID;
    /**
     * The Powerup.
     */
    public Player.POWERUP powerup;
    /**
     * The Duration.
     */
    public float duration;

    /**
     * Instantiates a new Player powerup data.
     *
     * @param playerID the player id
     * @param powerup  the powerup
     * @param duration the duration
     */
    public PlayerPowerupData(int playerID, Player.POWERUP powerup, float duration) {
        this.playerID = playerID;
        this.powerup = powerup;
        this.duration = duration;
    }
}
