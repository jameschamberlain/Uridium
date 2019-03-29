package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.damageable.enemy.*;

import java.util.ArrayList;
import java.util.Random;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;
import static net.uridium.game.res.Textures.SPAWN_TILE;

/**
 * Static entity which spawns enemies adjacent to itself, into the level it is in
 */
public class EnemySpawner extends Entity {
    /**
     * The grid X coordinate of the spawner
     */
    private int gridX;

    /**
     * The grid Y coordinate of the spawner
     */
    private int gridY;

    /**
     * A list of enemy types the spawner can spawn
     */
    private Enemy.Type[] types;

    /**
     * Random used for generating random numbers
     */
    private Random r;

    /**
     * The number of enemies the spawner can spawn in its lifetime
     */
    int numEnemies;

    /**
     * The time an enemy was last spawned from the spawner
     */
    long lastSpawn;

    /**
     * The time between spawning enemies
     */
    long spawnRate;

    /**
     * Constructor for EnemySpawner
     * @param gridX The grid X coordinate
     * @param gridY The grid Y coordinate
     * @param types The list of types able to spawn
     * @param numEnemies The number of enemies to spawn
     * @param spawnRate The gap between enemy spawns
     */
    public EnemySpawner(int gridX, int gridY, ArrayList<Enemy.Type> types, int numEnemies, long spawnRate) {
        super(-1, new Rectangle(gridX * TILE_WIDTH, gridY * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT), new Vector2(0, 0), SPAWN_TILE);

        this.gridX = gridX;
        this.gridY = gridY;
        this.types = new Enemy.Type[types.size()];
        types.toArray(this.types);
        this.numEnemies = numEnemies;
        this.spawnRate = spawnRate;
        r = new Random();
    }

    /**
     * @param ID The new ID for the entity, avoid using if possible
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return True if a new enemy can be spawned, false otherwise
     */
    public boolean canSpawn() {
        return numEnemies > 0 && System.currentTimeMillis() - lastSpawn > spawnRate;
    }

    /**
     * @return The number of enemies still to be spawned
     */
    public int getNumEnemies() {
        return numEnemies;
    }

    /**
     * Generates a new enemy to spawn and returns it
     * @param id The id to be given to the enemy
     * @param pos The position to spawn the entity
     * @return The entity to be added to the level
     */
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

    /**
     * @return The grid X coordinate
     */
    public int getGridX() {
        return gridX;
    }

    /**
     * @return The grid Y coordinate
     */
    public int getGridY() {
        return gridY;
    }
}
