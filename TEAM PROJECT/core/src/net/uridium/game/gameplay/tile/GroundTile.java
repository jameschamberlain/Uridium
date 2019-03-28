package net.uridium.game.gameplay.tile;

/**
 * The 'empty' tile
 */
public class GroundTile extends Tile {

    /**
     * GroundTile constructor
     * @param gridX The x coordinate of the tile in the grid
     * @param gridY The y coordinate of the tile in the grid
     */
    public GroundTile(int gridX, int gridY) {
        super(gridX, gridY, "graphics/tile/tundraCenter.png", false);
    }
}