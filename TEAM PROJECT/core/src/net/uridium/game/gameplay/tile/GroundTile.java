package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GroundTile extends Tile {

    public GroundTile(int gridX, int gridY) {
        super(gridX, gridY, new Texture(Gdx.files.internal("ground_06.png")), false);
    }
}