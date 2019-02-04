package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.graphics.Texture;

public abstract class BreakableTile extends Tile {
    Tile replacementTile;

    public BreakableTile(int gridX, int gridY, Texture texture, boolean isObstacle, boolean isDamageable, int health, Tile replacementTile) {
        super(gridX, gridY, texture, isObstacle, isDamageable, health);

        this.replacementTile = replacementTile;
    }

    public Tile getReplacementTile() {
        return replacementTile;
    }
}
