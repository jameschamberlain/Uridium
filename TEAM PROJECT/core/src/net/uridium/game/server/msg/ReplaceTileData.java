package net.uridium.game.server.msg;

import net.uridium.game.gameplay.tile.Tile;

import java.io.Serializable;

public class ReplaceTileData implements Serializable {
    public int x;
    public int y;
    public Tile t;

    public ReplaceTileData(int x, int y, Tile t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }
}