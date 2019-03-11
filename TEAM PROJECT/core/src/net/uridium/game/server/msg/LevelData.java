package net.uridium.game.server.msg;

import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.tile.Tile;

import java.io.Serializable;
import java.util.HashMap;

public class LevelData implements Serializable {
    public Tile[][] grid;
    public int gridWidth;
    public int gridHeight;
    public HashMap<Integer, Entity> entities;
    public int playerID;

    public LevelData(Tile[][] grid, int gridWidth, int gridHeight, HashMap<Integer, Entity> entities, int playerID) {
        this.grid = grid;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.entities = entities;
        this.playerID = playerID;
    }
}
