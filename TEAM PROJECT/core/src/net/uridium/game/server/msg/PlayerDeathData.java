package net.uridium.game.server.msg;

import java.io.Serializable;

/**
 * The type Player death data.
 */
public class PlayerDeathData implements Serializable {
    /**
     * The Id.
     */
    public int ID;
    /**
     * The Position.
     */
    public int position;

    /**
     * Instantiates a new Player death data.
     *
     * @param ID       the id
     * @param position the position
     */
    public PlayerDeathData(int ID, int position) {
        this.ID = ID;
        this.position = position;
    }
}