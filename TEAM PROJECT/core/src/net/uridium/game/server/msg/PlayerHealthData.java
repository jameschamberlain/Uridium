package net.uridium.game.server.msg;

import java.io.Serializable;

public class PlayerHealthData implements Serializable {
    public int playerID;
    public int health;
    public int maxHealth;

    public PlayerHealthData(int playerID, int health, int maxHealth) {
        this.playerID = playerID;
        this.health = health;
        this.maxHealth = maxHealth;
    }
}