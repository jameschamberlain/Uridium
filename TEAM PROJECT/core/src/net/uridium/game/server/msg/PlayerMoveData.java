package net.uridium.game.server.msg;

import java.io.Serializable;

/**
 * The type Player move data.
 */
public class PlayerMoveData implements Serializable {
    /**
     * The enum Dir.
     */
    public enum Dir {
        /**
         * Stop dir.
         */
        STOP,
        /**
         * Up dir.
         */
        UP,
        /**
         * Down dir.
         */
        DOWN,
        /**
         * Left dir.
         */
        LEFT,
        /**
         * Right dir.
         */
        RIGHT;
    }

    /**
     * The Dir.
     */
    public Dir dir;

    /**
     * Instantiates a new Player move data.
     *
     * @param dir the dir
     */
    public PlayerMoveData(Dir dir) {
        this.dir = dir;
    }
}
