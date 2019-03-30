package net.uridium.game.server.msg;

import java.io.Serializable;

/**
 * The type Remove entity data.
 */
public class RemoveEntityData implements Serializable {
    /**
     * The Entity id.
     */
    public int entityID;

    /**
     * Instantiates a new Remove entity data.
     *
     * @param entityID the entity id
     */
    public RemoveEntityData(int entityID) {
        this.entityID = entityID;
    }
}
