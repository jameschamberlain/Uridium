package net.uridium.game.gameplay.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.util.Assets;

import java.io.Serializable;

import static net.uridium.game.gameplay.Level.TILE_WIDTH;
import static net.uridium.game.gameplay.Level.TILE_HEIGHT;


public abstract class Tile implements Serializable {
    private Rectangle body;
    private int gridX;
    private int gridY;

    private boolean isObstacle;
    public boolean isDamageable;
    public boolean isEnemyTile = false;
    public int health;

    private String textureFile;
    private transient Texture t;

    public Tile(int gridX, int gridY, String textureFile, boolean isObstacle) {
        this(gridX, gridY, textureFile, isObstacle, false, 0);
    }

    public Tile(int gridX, int gridY, String textureFile, boolean isObstacle, boolean isDamageable, int health) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.textureFile = textureFile;
        this.isObstacle = isObstacle;
        this.isDamageable = isDamageable;
        this.health = health;

        body = new Rectangle(gridX * TILE_WIDTH, gridY * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);

    }

    public void loadTexture() {
        Gdx.app.postRunnable(() -> t = Assets.getTex((textureFile)));
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
        return t;
    }

    public String getTextureFile(){
        return textureFile;
    }

    public void setisEnemyTile(boolean isEnemyTile){
        this.isEnemyTile = isEnemyTile;
    }

    public void render(SpriteBatch batch) {
        if(t != null) batch.draw(t, body.x, body.y, body.width, body.height);
    }
}