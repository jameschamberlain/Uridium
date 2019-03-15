package net.uridium.game.server.msg;

import java.io.Serializable;

public class PlayerMoveData implements Serializable {
    public enum Dir {
        STOP,
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public Dir dir;

    public PlayerMoveData(Dir dir) {
        this.dir = dir;
    }
}
