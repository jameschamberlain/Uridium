package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.uridium.game.util.Assets;

/**
 * The tile used to transport players from one level to the next
 */
public class DoorTile extends Tile {

    /**
     * The id of the destination level
     */
    private int dest;

    /**
     * The id of the entrance used when entering the destination level
     */
    private int entrance;

    /**
     * The texture of the tile to render after the door is enabled
     */
    private transient TextureRegion tr;

    /**
     * The texture of the tile to render before the door is enabled
     */
    private transient Texture wall;

    /**
     * The rotation of the door
     */
    private float rot;

    /**
     * Whether the door can be used or not
     */
    private boolean enabled;

    /**
     * DoorTile constructor
     * @param gridX The x coordinate of the tile in the grid
     * @param gridY The y coordinate of the tile in the grid
     */
    public DoorTile(int gridX, int gridY) {
        super(gridX, gridY, "graphics/tile/DOORBOI.png", false);

        enabled = false;
        System.out.println(enabled);
    }

    /**
     * @param d The new destination id of the door
     */
    public void setDest(int d) {
        dest = d;
    }

    /**
     * @return The id of the destination level
     */
    public int getDest() {
        return dest;
    }

    /**
     * @param e The new entrance id of the door
     */
    public void setEntrance(int e) {
        entrance = e;
    }

    /**
     * @return The entrance id of the entrance to use when going to the destination level
     */
    public int getEntrance() {
        return entrance;
    }

    /**
     * @param rot The new rotation of the door
     */
    public void setRot(float rot) {
        this.rot = rot;
    }

    /**
     * Enable the door to be used
     */
    public void enable() {
        enabled = true;
    }

    /**
     * @return <code>true</code> if the door is enabled, <code>false</code> otherwise
     */
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
