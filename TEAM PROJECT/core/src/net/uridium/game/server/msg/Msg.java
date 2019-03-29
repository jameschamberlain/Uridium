package net.uridium.game.server.msg;

import java.io.Serializable;

/**
 * The type Msg.
 */
public class Msg implements Serializable {
    /**
     * The Type.
     */
    MsgType type;
    /**
     * The Data.
     */
    Object data;

    /**
     * Instantiates a new Msg.
     *
     * @param type the type
     * @param data the data
     */
    public Msg(MsgType type, Object data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public MsgType getType() {
        return type;
    }

    /**
     * The enum Msg type.
     */
    public enum MsgType {
        /**
         * New level msg type.
         */
        NEW_LEVEL,
        /**
         * New entity msg type.
         */
        NEW_ENTITY,
        /**
         * Entity update msg type.
         */
        ENTITY_UPDATE,
        /**
         * Remove entity msg type.
         */
        REMOVE_ENTITY,
        /**
         * Replace tile msg type.
         */
        REPLACE_TILE,
        /**
         * Player move msg type.
         */
        PLAYER_MOVE,
        /**
         * Player shoot msg type.
         */
        PLAYER_SHOOT,
        /**
         * Player update msg type.
         */
        PLAYER_UPDATE,
        /**
         * Player health msg type.
         */
        PLAYER_HEALTH,
        /**
         * Player death msg type.
         */
        PLAYER_DEATH,
        /**
         * Game over msg type.
         */
        GAME_OVER,
        /**
         * Player powerup msg type.
         */
        PLAYER_POWERUP,
        /**
         * Unlock doors msg type.
         */
        UNLOCK_DOORS
    }
}