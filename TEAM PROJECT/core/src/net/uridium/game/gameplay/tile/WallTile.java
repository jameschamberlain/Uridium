package net.uridium.game.gameplay.tile;

/**
 * The tile which acts as a barrier around each level
 */
public class WallTile extends Tile {

    /**
     * WallTile constructor
     * @param gridX The x coordinate of the tile in the grid
     * @param gridY The y coordinate of the tile in the grid
     */
    public WallTile(int gridX, int gridY) {
        super(gridX, gridY, "graphics/tile/igloo.png", true);
    }
}
