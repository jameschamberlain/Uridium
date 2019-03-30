package net.uridium.game.server.msg;

import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.tile.Tile;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The type Level data.
 */
public class LevelData implements Serializable {
    /**
     * The Id.
     */
    public int id;
    /**
     * The Grid.
     */
    public Tile[][] grid;
    /**
     * The Grid width.
     */
    public int gridWidth;
    /**
     * The Grid height.
     */
    public int gridHeight;
    /**
     * The Entities.
     */
    public HashMap<Integer, Entity> entities;
    /**
     * The Player id.
     */
    public int playerID;

    /**
     * Instantiates a new Level data.
     *
     * @param id         the id
     * @param grid       the grid
     * @param gridWidth  the grid width
     * @param gridHeight the grid height
     * @param entities   the entities
     * @param playerID   the player id
     */
    public LevelData(int id, Tile[][] grid, int gridWidth, int gridHeight, HashMap<Integer, Entity> entities, int playerID) {
        this.id = id;
        this.grid = grid;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.entities = entities;
        this.playerID = playerID;
    }
}