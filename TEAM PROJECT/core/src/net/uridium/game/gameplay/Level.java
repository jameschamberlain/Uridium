package net.uridium.game.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.DamageableEntity;
import net.uridium.game.gameplay.entity.damageable.enemy.Boss;
import net.uridium.game.gameplay.entity.damageable.enemy.Enemy;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.entity.projectile.Bullet;
import net.uridium.game.gameplay.tile.DoorTile;
import net.uridium.game.gameplay.tile.Tile;
import net.uridium.game.server.msg.*;
import net.uridium.game.util.Audio;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;
import static net.uridium.game.util.Audio.SOUND.ENEMY_DEAD;

/**
 * The client side version of the level class, handles all rendering and receives updates from the server
 */
public class Level {

    /**
     * The id of the level
     */
    int id;

    /**
     * The 2d array of tiles which form the grid
     */
    Tile[][] grid;

    /**
     * The width of the grid in tiles
     */
    public int gridWidth;

    /**
     * The height of the grid in tiles
     */
    public int gridHeight;

    /**
     * Width of each tile
     */
    public static final float TILE_WIDTH = 48;

    /**
     * Height of each tile
     */
    public static final float TILE_HEIGHT = 48;

    /**
     * The xOffset the grid needs to be drawn at in order for it to be centered on screen
     */
    public float xOffset;

    /**
     * The yOffset the grid needs to be drawn at in order for it to be centered on screen
     */
    public float yOffset;

    /**
     * HashMap of entities in the level, the key is the entity id
     */
    ConcurrentHashMap<Integer, Entity> entities;

    /**
     * ID of this client's player in the level
     */
    int playerID;

    /**
     * Pool of particle effects for levelling up
     */
    ParticleEffectPool levelUpEffectPool;

    /**
     * Pool of particle effects for the player taking damage
     */
    ParticleEffectPool damageEffectPool;

    /**
     * Pool of particles effects for the playing being healed
     */
    ParticleEffectPool healEffectPool;

    /**
     * Array of all particle effects to be updated and rendered
     */
    Array<ParticleEffectPool.PooledEffect> particleEffects;

    /**
     * Level constructor, constructs a client side level based on the LevelData object received from the server
     * @param levelData The LevelData object received from the server
     */
    public Level(LevelData levelData) {
        entities = new ConcurrentHashMap<>();

        this.id = levelData.id;
        this.grid = levelData.grid;
        this.gridWidth = levelData.gridWidth;
        this.gridHeight = levelData.gridHeight;
        this.entities.putAll(levelData.entities);
        this.playerID = levelData.playerID;

        xOffset = GAME_WIDTH - (gridWidth * TILE_WIDTH);
        xOffset /= 2;
        yOffset = GAME_HEIGHT - (gridHeight * TILE_HEIGHT) - 80;
        yOffset /= 2;

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j].loadTexture();
            }
        }

        for (Entity e : entities.values())
            e.loadTexture();

        particleEffects = new Array<>();

        Gdx.app.postRunnable(() -> {
            ParticleEffect levelUpEffect = new ParticleEffect();
            levelUpEffect.load(Gdx.files.internal("graphics/particle/levelUp.p"), Gdx.files.internal("graphics/particle"));
            levelUpEffectPool = new ParticleEffectPool(levelUpEffect, 0, 4);

            ParticleEffect damageEffect = new ParticleEffect();
            damageEffect.load(Gdx.files.internal("graphics/particle/damage.p"), Gdx.files.internal("graphics/particle"));
            damageEffectPool = new ParticleEffectPool(damageEffect, 0, 12);

            ParticleEffect healEffect = new ParticleEffect();
            healEffect.load(Gdx.files.internal("graphics/particle/heal.p"), Gdx.files.internal("graphics/particle"));
            healEffectPool = new ParticleEffectPool(healEffect, 0, 12);
        });
    }

    /**
     * @param offsets The Vector2 to copy the render offsets too
     * @return The Vector2 containing the render offsets
     */
    public Vector2 getOffsets(Vector2 offsets) {
        offsets.x = xOffset;
        offsets.y = yOffset;
        return offsets;
    }

    /**
     * Add an entity to the level
     * @param e The entity to add to the level
     */
    public void addEntity(Entity e) {
        entities.put(e.getID(), e);
        e.loadTexture();

        if (e instanceof Bullet)
            Audio.getAudio().playSound(Audio.SOUND.PLAYER_SHOOT);
    }

    /**
     * Update an entity in the level
     * @param entityUpdateData EntityUpdateData object received from the server
     */
    public void updateEntity(EntityUpdateData entityUpdateData) {
        Entity e = entities.get(entityUpdateData.ID);
        if (e == null) return;

        e.setPosition(entityUpdateData.pos);
        e.setVelocity(entityUpdateData.vel);

        if(entityUpdateData.maxHealth > 0) {
            DamageableEntity de = (DamageableEntity) e;
            de.setHealth(entityUpdateData.health);
            de.setMaxHealth(entityUpdateData.maxHealth);
        }

        if (e instanceof Enemy) ((Enemy) e).setAngle(entityUpdateData.angle);
    }

    /**
     * Remove an entity from the server
     * @param removeEntityData RemoveEntityData object retrieved from the server
     */
    public void removeEntity(RemoveEntityData removeEntityData) {
        if (entities.get(removeEntityData.entityID) instanceof Enemy)
            Audio.getAudio().playSound(ENEMY_DEAD);

        entities.remove(removeEntityData.entityID);
    }

    /**
     * Replace a tile in the grid
     * @param replaceTileData ReplaceTileData object received from the server
     */
    public void replaceTile(ReplaceTileData replaceTileData) {
        replaceTileData.t.loadTexture();
        grid[replaceTileData.x][replaceTileData.y] = replaceTileData.t;
    }

    /**
     * Update a player in the level
     * @param playerUpdateData PlayerUpdateData object received from the server
     */
    public void updatePlayer(PlayerUpdateData playerUpdateData) {
        Player player = (Player) entities.get(playerUpdateData.playerID);
        player.setScore(playerUpdateData.score);
        player.setLevel(playerUpdateData.level);
        player.setXp(playerUpdateData.xp);
        player.setXpToLevelUp(playerUpdateData.xpToLevelUp);

        if (playerUpdateData.levelledUp) {
            Vector2 playerPos = player.getPosition(new Vector2());
            ParticleEffectPool.PooledEffect effect = levelUpEffectPool.obtain();
            effect.setPosition(playerPos.x + player.getBody().width * (3 / 4f), playerPos.y + player.getBody().height * (3 / 4f));
            effect.start();
            particleEffects.add(effect);
        }
    }

    /**
     * Called when a player dies on the level
     * @param playerDeathData PlayerDeathData object received from the server
     */
    public void killPlayer(PlayerDeathData playerDeathData) {
        Vector2 pos = new Vector2();
        entities.get(playerDeathData.ID).getPosition(pos);
    }

    /**
     * Updates the health of the player
     * @param playerHealthData PlayerHealthData object received from the server
     */
    public void updateHealth(PlayerHealthData playerHealthData) {
        Player player = (Player) entities.get(playerHealthData.playerID);

        if (damageEffectPool != null) {
            if (playerHealthData.health < player.getHealth()) {
                Audio.getAudio().playSound(Audio.SOUND.PLAYER_DAMAGE);
                Vector2 playerPos = player.getPosition(new Vector2());
                ParticleEffectPool.PooledEffect effect = damageEffectPool.obtain();
                effect.setPosition(playerPos.x + player.getBody().width / 2, playerPos.y + player.getBody().height / 2);
                effect.start();
                particleEffects.add(effect);
            } else if (playerHealthData.health > player.getHealth()) {
                Vector2 playerPos = player.getPosition(new Vector2());
                ParticleEffectPool.PooledEffect effect = healEffectPool.obtain();
                effect.setPosition(playerPos.x + player.getBody().width / 2, playerPos.y + player.getBody().height / 2);
                effect.start();
                particleEffects.add(effect);
            }
        }

        player.setHealth(playerHealthData.health);
        player.setMaxHealth(playerHealthData.maxHealth);
    }

    /**
     * Enable all the doors on the map
     */
    public void unlockDoors() {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Tile tile = grid[i][j];

                if (tile instanceof DoorTile) {
                    DoorTile door = (DoorTile) tile;
                    door.enable();
                }
            }
        }
    }

    /**
     * @return The id of this client's player
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Checks for collisions between the player and the walls to keep movement and collisions smooth
     * @param player The player to check collisinos with
     */
    public void checkCollisionsForPlayer(Player player) {
        Rectangle playerBody = player.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if (tile.isObstacle()) {
                    obstacle = tile.getBody();

                    if (Intersector.intersectRectangles(playerBody, obstacle, overlap)) {
                        if (overlap.width > overlap.height)
                            player.setY(player.getLastPos().y);
                        else
                            player.setX(player.getLastPos().x);

                        return;
                    }
                }
            }
        }

        if (playerBody.y + playerBody.height > gridHeight * TILE_HEIGHT || playerBody.y < 0)
            player.setY(player.getLastPos().y);
        else if (playerBody.x + playerBody.width > gridWidth * TILE_WIDTH || playerBody.x < 0)
            player.setX(player.getLastPos().x);
    }

    /**
     * @return This client's player
     */
    public Player getPlayer() {
        return (Player) entities.get(playerID);
    }

    /**
     * @return A list of players in the level
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (Entity e : entities.values())
            if (e instanceof Player) players.add((Player) e);

        return players;
    }

    /**
     * Updates the level and everything in it
     * @param delta The time between this update and the last
     */
    public void update(float delta) {
        for (Entity e : entities.values()) {
            e.update(delta);

            if (e instanceof Player)
                checkCollisionsForPlayer((Player) e);
        }

        for (int i = particleEffects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = particleEffects.get(i);
            effect.update(delta);
            if (effect.isComplete()) {
                effect.free();
                particleEffects.removeIndex(i);
            }
        }
    }

    /**
     * Renders the level to the screen
     * @param batch The SpriteBatch used to render the level to the screen
     */
    public void render(SpriteBatch batch) {
        Matrix4 matrix4 = batch.getProjectionMatrix();
        matrix4.translate(xOffset, yOffset, 0);
        batch.setProjectionMatrix(matrix4);

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j].render(batch);
            }
        }

        for (Entity e : entities.values())
            e.render(batch);

        for (ParticleEffectPool.PooledEffect effect : particleEffects)
            effect.draw(batch);
    }

    /**
     * @return The id of the level
     */
    public int getId() {
        return id;
    }

    /**
     * @return The boss instance if on the boss level, or <code>null</code> otherwise
     */
    public Boss getBoss() {
        if(id != -1)
            return null;

        for (Entity e : entities.values())
            if (e instanceof Boss)
                return (Boss) e;

        return null;
    }
}