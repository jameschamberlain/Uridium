package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class WallTile extends Tile {

    public WallTile(int gridX, int gridY) {
        super(gridX, gridY, new Texture(Gdx.files.internal("block_08.png")), true);
    }
}
