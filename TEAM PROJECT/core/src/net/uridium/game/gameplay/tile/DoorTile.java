package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DoorTile extends Tile {

    private int dest;

    public DoorTile(int gridX, int gridY) {
        super(gridX, gridY, "ground_03.png", false);
    }

    public void setDest(int d) {
        dest = d;
    }

    public int getDest() {
        return dest;
    }
}
