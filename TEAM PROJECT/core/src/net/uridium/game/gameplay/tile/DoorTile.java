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
    private transient Texture wall;
    private float rot;

    private boolean enabled;

    public DoorTile(int gridX, int gridY) {
        super(gridX, gridY, "graphics/tile/DOORBOI.png", false);

        enabled = false;
        System.out.println(enabled);
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

    public void enable() {
        System.out.println("enable()");
        enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadTexture() {
        super.loadTexture();
        Gdx.app.postRunnable(() -> {
            tr = new TextureRegion(t);
            wall = Assets.getTex("graphics/tile/igloo.png");
        });
    }

    @Override
    public void render(SpriteBatch batch) {
        if(wall != null) {
            if(isEnabled()) batch.draw(tr, body.x ,body.y, body.width / 2, body.height / 2, body.width, body.height, 1, 1, rot);
            else batch.draw(wall, body.x, body.y, body.width, body.height);
        }
    }
}
