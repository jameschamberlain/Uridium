package net.uridium.game.server.msg;

import java.io.Serializable;

public class RemoveEntityData implements Serializable {
    public int entityID;

    public RemoveEntityData(int entityID) {
        this.entityID = entityID;
    }
}
