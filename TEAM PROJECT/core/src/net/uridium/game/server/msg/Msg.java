package net.uridium.game.server.msg;

import java.io.Serializable;

public class Msg implements Serializable {
    MsgType type;
    Object data;

    public Msg(MsgType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public MsgType getType() {
        return type;
    }

    public enum MsgType {
        NEW_LEVEL,
        NEW_ENTITY,
        ENTITY_UPDATE,
        REMOVE_ENTITY,
        REPLACE_TILE,
        PLAYER_MOVE,
        PLAYER_SHOOT,
        PLAYER_UPDATE,
        PLAYER_HEALTH
    }
}