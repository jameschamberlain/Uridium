package net.uridium.game.gameplay.entity.damageable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;

public class DamageableEntity extends Entity {
    int maxHealth;
    int health;

    public DamageableEntity(int ID, Rectangle body, Vector2 vel, String textureFile) {
        this(ID, body, vel, textureFile, 5, 5);
    }

    public DamageableEntity(int ID, Rectangle body, Vector2 vel, String textureFile, int maxHealth, int health) {
        super(ID, body, vel, textureFile);

        this.maxHealth = maxHealth;
        this.health = health;
    }

    public void setHealth(int health) {
        if(health < 0) health = 0;
        if(health > maxHealth) health = maxHealth;

        this.health = health;
    }

    public void damage(int damage) {
        setHealth(health - damage);
    }

    public void heal(int mod) {
        setHealth(health + mod);
    }

    public int getHealth() {
        return health;
    }

    public void setMaxHealth(int maxHealth) {
        if(maxHealth < 1) maxHealth = 1;
        this.maxHealth = maxHealth;
        if(health > maxHealth) health = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isDead() {
        return health == 0;
    }
}
