package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DoorTile extends Tile {

    public DoorTile(int gridX, int gridY) {
        super(gridX, gridY, new Texture(Gdx.files.internal("ground_03.png")), false);
    }
}
