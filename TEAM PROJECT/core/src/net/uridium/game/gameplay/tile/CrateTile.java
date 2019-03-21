package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class CrateTile extends BreakableTile {

    public CrateTile(int gridX, int gridY) {
        super(gridX, gridY, "graphics/tile/iceBlockAlt.png", true, true, 3, new GroundTile(gridX, gridY));
    }
}
