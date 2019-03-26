package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.damageable.enemy.*;

import java.util.ArrayList;
import java.util.Random;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;
import static net.uridium.game.res.Textures.SPAWN_TILE;

public class EnemySpawner extends Entity {
    private int gridX;
    private int gridY;
    private Enemy.Type[] types;
    private Random r;

    int numEnemies;
    long lastSpawn;
    long spawnRate;

    public EnemySpawner(int gridX, int gridY, ArrayList<Enemy.Type> types, int numEnemies, long spawnRate) {
        super(-1, new Rectangle(gridX * TILE_WIDTH, gridY * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT), new Vector2(0, 0), SPAWN_TILE);

        this.gridX = gridX;
        this.gridY = gridY;
        this.types = new Enemy.Type[types.size()];
        types.toArray(this.types);
        this.numEnemies = numEnemies;
        this.spawnRate = spawnRate;
        r = new Random();
        setEnemyTile(true);

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

        Enemy.Type type = types[r.nextInt(types.length)];

        switch(type) {
            case BLUE_FISH:
                return new BlueFish(id, pos);
            case GREEN_FISH:
                return new GreenFish(id, pos);
            case PINK_FISH:
                return new PinkFish(id, pos);
            case SPIDER:
                return new Spider(id, pos);
            default:
                return new BlueFish(id, pos);
        }

    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getNumEnemies() {
        return numEnemies;
    }
}
