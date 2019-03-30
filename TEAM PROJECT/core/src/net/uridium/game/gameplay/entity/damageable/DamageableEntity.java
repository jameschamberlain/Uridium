package net.uridium.game.gameplay.entity.damageable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;

/**
 * An entity with health which can take damage and be killed
 */
public class DamageableEntity extends Entity {

    /**
     * Max health of the entity
     */
    float maxHealth;

    /**
     * Current health of the entity
     */
    float health;

    /**
     * DamageableEntity Constructor
     * @param ID The entity id
     * @param body The entity body
     * @param vel The initial entity velocity
     * @param textureFile String used to access the entity texture on the client side
     * @param maxHealth The max health of the entity
     * @param health The health of the entity
     */
    public DamageableEntity(int ID, Rectangle body, Vector2 vel, String textureFile, int maxHealth, int health) {
        super(ID, body, vel, textureFile);

        this.maxHealth = maxHealth;
        this.health = health;
    }

    /**
     * @param health The new health of the entity
     */
    public void setHealth(float health) {
        if(health < 0) health = 0;
        if(health > maxHealth) health = maxHealth;

        this.health = health;
        System.out.println(this.health);
    }

    /**
     * Damage the entity by a specific amount
     * @param damage The amount to damage the entity
     */
    public void damage(float damage) {
        setHealth(health - damage);
    }

    /**
     * Heal the entity by a specific amount
     * @param mod The amount to heal the entity
     */
    public void heal(float mod) {
        setHealth(health + mod);
    }

    /**
     * @return The current health of the entity
     */
    public float getHealth() {
        return health;
    }

    /**
     * @param maxHealth The new max health of the entity
     */
    public void setMaxHealth(float maxHealth) {
        if(maxHealth < 1) maxHealth = 1;
        this.maxHealth = maxHealth;
        if(health > maxHealth) health = maxHealth;
    }

    /**
     * @return The max health of the entity
     */
    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return <code>true</code> if the health of the entity is 0, <code>false</code> otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }
}
