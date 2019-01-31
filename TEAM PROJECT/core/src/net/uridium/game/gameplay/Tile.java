package net.uridium.game.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static net.uridium.game.gameplay.Level.TILE_WIDTH;
import static net.uridium.game.gameplay.Level.TILE_HEIGHT;


public class Tile {
    Rectangle body;
    int x;
    int y;
    boolean isObstacle;

    Texture texture;

    public Tile(int x, int y, Texture texture, float xOffset, float yOffset) {
        this(x, y, texture, xOffset, yOffset, false);
    }

    public Tile(int x, int y, Texture texture, float xOffset, float yOffset, boolean isObstacle) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.isObstacle = isObstacle;

        body = new Rectangle(xOffset + x * TILE_WIDTH, yOffset + y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBody() {
        return body;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, body.x, body.y, body.width, body.height);
    }
}