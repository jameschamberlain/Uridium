package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.util.Assets;

import java.io.Serializable;

import static net.uridium.game.gameplay.Level.TILE_WIDTH;
import static net.uridium.game.gameplay.Level.TILE_HEIGHT;

/**
 * Base tile class, levels consist of 2d arrays of tiles
 */
public abstract class Tile implements Serializable {

    /**
     * Rectangle used for collision with entities
     */
    Rectangle body;

    /**
     * X coordinate of the tile on the grid
     */
    private int gridX;

    /**
     * Y coordinate of the tile on the grid
     */
    private int gridY;

    /**
     * Whether the tile can be passed through or not
     */
    private boolean isObstacle;

    /**
     * Whether the tile can take damage/be destroyed
     */
    public boolean isDamageable;

    /**
     * Health of the tile - if it is damageable
     */
    public int health;

    /**
     * The string used to access the texture of the tile on the client side
     */
    private String textureFile;

    /**
     * Texture of the tile, loaded using the {@link Tile#textureFile}
     */
    transient Texture t;

    /**
     * Stripped down version of the tile constructor for non damageable tiles, see {@link Tile#Tile(int, int, String, boolean, boolean, int)}
     */
    public Tile(int gridX, int gridY, String textureFile, boolean isObstacle) {
        this(gridX, gridY, textureFile, isObstacle, false, 0);
    }

    /**
     * Tile constructor
     * @param gridX The x coordinate of the tile in the grid
     * @param gridY The y coordinate of the tile in the grid
     * @param textureFile
     * @param isObstacle Whether the tile is an obstacle or not
     * @param isDamageable Whether the tile can take damage/be destroyed
     * @param health The health of the tile - if it is damageable
     */
    public Tile(int gridX, int gridY, String textureFile, boolean isObstacle, boolean isDamageable, int health) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.textureFile = textureFile;
        this.isObstacle = isObstacle;
        this.isDamageable = isDamageable;
        this.health = health;

        body = new Rectangle(gridX * TILE_WIDTH, gridY * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);

    }

    /**
     * Used to load the texture from {@link Tile#textureFile} on the client side
     */
    public void loadTexture() {
        Gdx.app.postRunnable(() -> t = Assets.getTex((textureFile)));
    }

    /**
     * @return <code>true</code> if the tile is an obstacle, <code>false</code> otherwise
     */
    public boolean isObstacle() {
        return isObstacle;
    }

    /**
     * @return The body of the tile
     */
    public Rectangle getBody() {
        return body;
    }

    /**
     * @return The grid x coordinate of the tile
     */
    public int getGridX() {
        return gridX;
    }

    /**
     * @return The grid y coordinate of the tile
     */
    public int getGridY() {
        return gridY;
    }

    /**
     * @return The texture of the tile
     */
    public Texture getTexture() {
        return t;
    }

    /**
     * Renders the tile using the given SpriteBatch
     * @param batch The SpriteBatch used to render the tile
     */
    public void render(SpriteBatch batch) {
        if(t != null) batch.draw(t, body.x, body.y, body.width, body.height);
    }
}