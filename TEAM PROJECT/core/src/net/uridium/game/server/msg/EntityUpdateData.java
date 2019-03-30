package net.uridium.game.server.msg;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * The type Entity update data.
 */
public class EntityUpdateData implements Serializable {
    /**
     * The Id.
     */
    public int ID;
    /**
     * The Pos.
     */
    public Vector2 pos;
    /**
     * The Vel.
     */
    public Vector2 vel;
    /**
     * The Angle.
     */
    public float angle;
    /**
     * The Health.
     */
    public float health;
    /**
     * The Max health.
     */
    public float maxHealth;

    /**
     * Instantiates a new Entity update data.
     *
     * @param ID  the id
     * @param pos the pos
     * @param vel the vel
     */
    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel) {
        this(ID, pos, vel, 0, 0);
    }

    /**
     * Instantiates a new Entity update data.
     *
     * @param ID        the id
     * @param pos       the pos
     * @param vel       the vel
     * @param health    the health
     * @param maxHealth the max health
     */
    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel, float health, float maxHealth) {
        this(ID, pos, vel, 0, health, maxHealth);
    }

    /**
     * Instantiates a new Entity update data.
     *
     * @param ID        the id
     * @param pos       the pos
     * @param vel       the vel
     * @param angle     the angle
     * @param health    the health
     * @param maxHealth the max health
     */
    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel, float angle, float health, float maxHealth) {
        this.ID = ID;
        this.pos = pos;
        this.vel = vel;
        this.angle = angle;
        this.health = health;
        this.maxHealth = maxHealth;
    }
}
