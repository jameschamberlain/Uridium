package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static net.uridium.game.gameplay.Level.TILE_WIDTH;
import static net.uridium.game.gameplay.Level.TILE_HEIGHT;


public abstract class Tile {
    private Rectangle body;
    private int gridX;
    private int gridY;

    private boolean isObstacle;
    private boolean isDamageable;
    protected int health;

    private Texture texture;

    public Tile(int gridX, int gridY, Texture texture, boolean isObstacle) {
        this(gridX, gridY, texture, isObstacle, false, 0);
    }

    public Tile(int gridX, int gridY, Texture texture, boolean isObstacle, boolean isDamageable, int health) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.texture = texture;
        this.isObstacle = isObstacle;
        this.isDamageable = isDamageable;
        this.health = health;

        body = new Rectangle(gridX * TILE_WIDTH, gridY * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public Rectangle getBody() {
        return body;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public Texture getTexture() {
        return texture;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, body.x, body.y, body.width, body.height);
    }
}