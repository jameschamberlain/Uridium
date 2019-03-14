package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.damageable.Enemy;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;

public class EnemySpawner extends Entity {
    private int gridX;
    private int gridY;

    int numEnemies = 15;
    long lastSpawn = 0;
    long spawnRate = 4000;

    public EnemySpawner(int gridX, int gridY) {
        super(-1, new Rectangle(gridX * TILE_WIDTH, gridY * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT), new Vector2(0, 0), "crate_01.png");

        this.gridX = gridX;
        this.gridY = gridY;

    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean canSpawn() {
        return numEnemies > 0 && System.currentTimeMillis() - lastSpawn > spawnRate;
    }

    public Enemy spawn(int id, Vector2 pos) {
        numEnemies--;
        lastSpawn = System.currentTimeMillis();
        return new Enemy(id, new Rectangle(pos.x, pos.y, 40, 40), Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
