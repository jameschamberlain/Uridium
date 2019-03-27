package net.uridium.game.server.msg;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class EntityUpdateData implements Serializable {
    public int ID;
    public Vector2 pos;
    public Vector2 vel;
    public float angle;
    public float health;
    public float maxHealth;

    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel) {
        this(ID, pos, vel, 0, 0);
    }
    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel, float health, float maxHealth) {
        this(ID, pos, vel, 0, health, maxHealth);
    }

    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel, float angle, float health, float maxHealth) {
        this.ID = ID;
        this.pos = pos;
        this.vel = vel;
        this.angle = angle;
        this.health = health;
        this.maxHealth = maxHealth;
    }
}
