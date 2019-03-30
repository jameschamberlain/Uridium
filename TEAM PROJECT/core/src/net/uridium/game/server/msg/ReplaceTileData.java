package net.uridium.game.server.msg;

import net.uridium.game.gameplay.tile.Tile;

import java.io.Serializable;

/**
 * The type Replace tile data.
 */
public class ReplaceTileData implements Serializable {
    /**
     * The X.
     */
    public int x;
    /**
     * The Y.
     */
    public int y;
    /**
     * The T.
     */
    public Tile t;

    /**
     * Instantiates a new Replace tile data.
     *
     * @param x the x
     * @param y the y
     * @param t the t
     */
    public ReplaceTileData(int x, int y, Tile t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }
}