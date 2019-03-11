package net.uridium.game.server.msg;

import java.io.Serializable;

public class PlayerShootData implements Serializable {
    public double angle;

    public PlayerShootData(double angle) {
        this.angle = angle;
    }
}
