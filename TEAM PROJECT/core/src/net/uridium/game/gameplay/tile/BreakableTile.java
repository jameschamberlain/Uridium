package net.uridium.game.gameplay.tile;

/**
 * Base class for tiles which can be destroyed
 */
public abstract class BreakableTile extends Tile {

    /**
     * The tile to put in the grid in place of this tile when it is destroyed
     */
    Tile replacementTile;

    /**
     * BreakableTile constructor<br>
     * @param replacementTile The tile to replace this tile when it is destroyed<br>
     * For all other params see {@link Tile#Tile(int, int, String, boolean, boolean, int)}
     */
    public BreakableTile(int gridX, int gridY, String textureFile, boolean isObstacle, boolean isDamageable, int health, Tile replacementTile) {
        super(gridX, gridY, textureFile, isObstacle, isDamageable, health);

        this.replacementTile = replacementTile;
    }

    /**
     * @return The replacement tile
     */
    public Tile getReplacementTile() {
        return replacementTile;
    }
}
