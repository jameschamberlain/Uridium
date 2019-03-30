package net.uridium.game.gameplay.tile;

/**
 * Default obstacle tile in the game
 */
public class CrateTile extends BreakableTile {

    /**
     * CrateTile constructor
     * @param gridX The x coordinate of the tile in the grid
     * @param gridY The y coordinate of the tile in the grid
     */
    public CrateTile(int gridX, int gridY) {
        super(gridX, gridY, "graphics/tile/obstacle.png", true, true, 3, new GroundTile(gridX, gridY));
    }
}
