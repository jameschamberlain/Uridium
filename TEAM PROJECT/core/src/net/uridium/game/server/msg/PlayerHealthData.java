package net.uridium.game.server.msg;

import java.io.Serializable;

public class PlayerHealthData implements Serializable {
    public int playerID;
    public float health;
    public float maxHealth;

    public PlayerHealthData(int playerID, float health, float maxHealth) {
        this.playerID = playerID;
        this.health = health;
        this.maxHealth = maxHealth;
    }
}