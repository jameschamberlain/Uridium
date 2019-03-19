package net.uridium.game.server.msg;

import net.uridium.game.gameplay.entity.damageable.Player;

import java.io.Serializable;

public class PlayerPowerupData implements Serializable {
    public int playerID;
    public Player.POWERUP powerup;
    public float duration;

    public PlayerPowerupData(int playerID, Player.POWERUP powerup, float duration) {
        this.playerID = playerID;
        this.powerup = powerup;
        this.duration = duration;
    }
}
