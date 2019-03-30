package net.uridium.game.server.msg;

import java.io.Serializable;

/**
 * The type Player shoot data.
 */
public class PlayerShootData implements Serializable {
    /**
     * The Angle.
     */
    public double angle;

    /**
     * Instantiates a new Player shoot data.
     *
     * @param angle the angle
     */
    public PlayerShootData(double angle) {
        this.angle = angle;
    }
}
