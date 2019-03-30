package net.uridium.game.gameplay.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;

/**
 * Base projectile class
 */
public abstract class Projectile extends Entity {
    /**
     * The entity ID of the entity which fired the projectile
     */
    int ownerID;

    /**
     * Constructor for Projectile
     * @param ID The entity ID
     * @param body The body of the entity
     * @param vel Initial velocity
     * @param textureFile File name of the entity's texture
     * @param ownerID The entity id of the entity which fired the projectile
     */
    public Projectile(int ID, Rectangle body, Vector2 vel, String textureFile, int ownerID) {
        super(ID, body, vel, textureFile);

        this.ownerID = ownerID;
    }

    /**
     * @return The enity ID of the entity which fired the projectile
     */
    public int getOwnerID() {
        return ownerID;
    }
}
