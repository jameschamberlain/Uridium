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
import net.uridium.game.gameplay.entity.damageable.enemy.Enemy;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.tile.Tile;
import net.uridium.game.server.msg.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Level {
    Tile[][] grid;
    public int gridWidth;
    public int gridHeight;

    public static final float TILE_WIDTH = 48;
    public static final float TILE_HEIGHT = 48;
    public float xOffset;
    public float yOffset;

    float enemyMoveSpeed = 30;

    ConcurrentHashMap<Integer, Entity> entities;
    int playerID;

    ParticleEffectPool levelUpEffectPool;
    ParticleEffectPool damageEffectPool;
    ParticleEffectPool healEffectPool;
    Array<ParticleEffectPool.PooledEffect> particleEffects;

//    String outputScore;
//    BitmapFont myFont = new BitmapFont(Gdx.files.internal("arial.fnt"));

    public Level(LevelData levelData) {
        entities = new ConcurrentHashMap<>();

        this.grid = levelData.grid;
        this.gridWidth = levelData.gridWidth;
        this.gridHeight = levelData.gridHeight;
        this.entities.putAll(levelData.entities);
        this.playerID = levelData.playerID;

        xOffset = GAME_WIDTH - (gridWidth * TILE_WIDTH);
        xOffset /= 2;
        yOffset = GAME_HEIGHT - (gridHeight * TILE_HEIGHT) - 80;
        yOffset /= 2;

        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j < gridHeight; j++) {
                grid[i][j].loadTexture();
            }
        }

        for(Entity e : entities.values())
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

    public Vector2 getOffsets(Vector2 offsets) {
        offsets.x = xOffset;
        offsets.y = yOffset;
        return offsets;
    }

    public void addEntity(Entity e) {
        entities.put(e.getID(), e);
        e.loadTexture();
    }

    public void updateEntity(EntityUpdateData entityUpdateData) {
        Entity e = entities.get(entityUpdateData.ID);
        if(e == null) return;

        e.setPosition(entityUpdateData.pos);
        e.setVelocity(entityUpdateData.vel);
        if(e instanceof Enemy) ((Enemy) e).setAngle(entityUpdateData.angle);
    }

    public void removeEntity(RemoveEntityData removeEntityData) {
        entities.remove(removeEntityData.entityID);
    }

    public void replaceTile(ReplaceTileData replaceTileData) {
        replaceTileData.t.loadTexture();
        grid[replaceTileData.x][replaceTileData.y] = replaceTileData.t;
    }

    public void updatePlayer(PlayerUpdateData playerUpdateData) {
        Player player = (Player) entities.get(playerUpdateData.playerID);
        player.setScore(playerUpdateData.score);
        player.setLevel(playerUpdateData.level);
        player.setXp(playerUpdateData.xp);
        player.setXpToLevelUp(playerUpdateData.xpToLevelUp);

        if(playerUpdateData.levelledUp) {
            Vector2 playerPos = player.getPosition(new Vector2());
            ParticleEffectPool.PooledEffect effect = levelUpEffectPool.obtain();
            effect.setPosition(playerPos.x + player.getBody().width * (3/4), playerPos.y + player.getBody().height * (3/4));
            effect.start();
            particleEffects.add(effect);
        }
    }

    public void killPlayer(PlayerDeathData playerDeathData) {
        Vector2 pos = new Vector2();
        entities.get(playerDeathData.ID).getPosition(pos);
    }

    public void updateHealth(PlayerHealthData playerHealthData) {
        Player player = (Player) entities.get(playerHealthData.playerID);

        if(damageEffectPool != null) {
            if (playerHealthData.health < player.getHealth()) {
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

    public int getPlayerID() {
        return playerID;
    }

    public boolean checkCollisionsForPlayer(Player player) {
        Rectangle playerBody = player.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for(int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if(tile.isObstacle()) {
                    obstacle = tile.getBody();

                    if(Intersector.intersectRectangles(playerBody, obstacle, overlap)) {
                        if(overlap.width > overlap.height)
                            player.setY(player.getLastPos().y);
                        else
                            player.setX(player.getLastPos().x);

                        return true;
                    }
                }
            }
        }

        if(playerBody.y + playerBody.height > gridHeight * TILE_HEIGHT || playerBody.y < 0)
            player.setY(player.getLastPos().y);
        else if(playerBody.x + playerBody.width > gridWidth * TILE_WIDTH || playerBody.x < 0)
            player.setX(player.getLastPos().x);

        return false;
    }

    public Player getPlayer() {
        return (Player) entities.get(playerID);
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for(Entity e : entities.values())
            if(e instanceof Player) players.add((Player) e);

        return players;
    }

    public void update(float delta) {
        for(Entity e : entities.values()) {
            e.update(delta);

            if (e instanceof Player)
                checkCollisionsForPlayer((Player) e);
        }

        for (int i = particleEffects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = particleEffects.get(i);
            effect.update(delta);
            if(effect.isComplete()) {
                effect.free();
                particleEffects.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        Matrix4 matrix4 = batch.getProjectionMatrix();
        matrix4.translate(xOffset, yOffset, 0);
        batch.setProjectionMatrix(matrix4);

        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j < gridHeight; j++) {
                grid[i][j].render(batch);
            }
        }

        for(Entity e : entities.values())
            e.render(batch);

        for(ParticleEffectPool.PooledEffect effect : particleEffects)
            effect.draw(batch);
    }
}