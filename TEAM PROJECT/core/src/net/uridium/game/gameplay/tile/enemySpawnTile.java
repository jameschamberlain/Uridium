package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class enemySpawnTile extends Tile {

    public enemySpawnTile(int gridX, int gridY) {
        super(gridX, gridY, "ground_05.png", true);
        setSpawnTile(true);
    }
}