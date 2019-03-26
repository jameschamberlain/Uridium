package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.uridium.game.util.Assets;

public class DoorTile extends Tile {

    private int dest;
    private int entrance;
    private transient TextureRegion tr;
    private float rot;

    public DoorTile(int gridX, int gridY) {
        super(gridX, gridY, "graphics/tile/DOORBOI.png", false);
    }

    public void setDest(int d) {
        dest = d;
    }

    public int getDest() {
        return dest;
    }

    public void setEntrance(int e) {
        entrance = e;
    }

    public int getEntrance() {
        return entrance;
    }

    public void setRot(float rot) {
        this.rot = rot;
    }

    @Override
    public void loadTexture() {
        super.loadTexture();
        Gdx.app.postRunnable(() -> tr = new TextureRegion(t));

    }

    @Override
    public void render(SpriteBatch batch) {
        if(tr != null) batch.draw(tr, body.x ,body.y, body.width / 2, body.height / 2, body.width, body.height, 1, 1, rot);
    }
}
