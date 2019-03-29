package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.Msg;

/**
 * Base class for all items, cannot be created itself
 */
public abstract class Item extends Entity {
    /**
     * If the item has been used or not
     */
    private boolean used = false;

    /**
     * Constructor for Item
     * @param ID The ID of the entity
     * @param body The body of the entity
     * @param textureFile File name of the entity's texture
     */
    public Item(int ID, Rectangle body, String textureFile) {
        super(ID, body, new Vector2(0, 0), textureFile);
    }

    /**
     * Called when the item is collided with by a player
     * @param player The player which collided with the item
     * @return A msg to send out to the clients
     */
    public Msg onPlayerCollision(Player player) {
        used = true;
        return null;
    }

    /**
     * Returns whether the item has been used or not
     * @return <code>true</code> if the item has been used, <code>false</code> otherwise
     */
    public boolean isUsed() {
        return used;
    }
}
