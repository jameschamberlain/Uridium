package net.uridium.game.server;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Enemy;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.entity.projectile.Bullet;
import net.uridium.game.gameplay.entity.projectile.Projectile;
import net.uridium.game.gameplay.tile.BreakableTile;
import net.uridium.game.gameplay.tile.Tile;
import net.uridium.game.server.msg.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerLevel {
    Tile[][] grid;
    int gridWidth;
    int gridHeight;

    ConcurrentHashMap<Integer, Entity> entities;
    ArrayList<Integer> entityIDsToRemove;
    ArrayList<Integer> playerIDs;

    ArrayList<Vector2> playerSpawnLocations;

    BlockingQueue<Msg> msgs;

    int nextEntityID;

    public ServerLevel(Tile[][] grid, int gridWidth, int gridHeight, ArrayList<Vector2> playerSpawnLocations) {
        this.grid = grid;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.playerSpawnLocations = playerSpawnLocations;

        entityIDsToRemove = new ArrayList<>();
        playerIDs = new ArrayList<>();

        entities = new ConcurrentHashMap<>();
        msgs = new LinkedBlockingQueue<>();

        addStartEnemies();
    }

    /**
     * @Deprecated just for testing
     */
    public void addStartEnemies() {
        Enemy e = new Enemy(getNextEntityID(), new Rectangle(700, 450, 40, 40), 1, 1);
        addEntity(e);
    }

    public Vector2 getNewPlayerSpawn() {
        return playerSpawnLocations.get(playerIDs.size());
    }

    public int getNextEntityID() {
        return nextEntityID++;
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getID(), entity);
        if(entity instanceof Player)
            playerIDs.add(entity.getID());

        msgs.add(new Msg(Msg.MsgType.NEW_ENTITY, entity));
    }

    public Player getPlayer(int playerID) {
        return (Player) entities.get(playerID);
    }

    public void purgeEntities() {
        for(Integer i : entityIDsToRemove) {
            entities.remove(i);
            msgs.add(new Msg(Msg.MsgType.REMOVE_ENTITY, new RemoveEntityData(i)));
        }

        entityIDsToRemove.clear();
    }

    public BlockingQueue<Msg> getMsgs() {
        return msgs;
    }

    public LevelData getLevelData() {
        return new LevelData(grid, gridWidth, gridHeight, new HashMap<Integer, Entity>(entities), -1);
    }

    public void createBullet(int playerID, PlayerMoveData.Dir dir) {
        float shootAngle;
        switch(dir) {
            case UP:
                shootAngle = 0;
                break;
            case DOWN:
                shootAngle = 180;
                break;
            case LEFT:
                shootAngle = 270;
                break;
            case RIGHT:
                shootAngle = 90;
                break;
            default:
                shootAngle = 0;
                break;
        }

        Bullet bullet = new Bullet(getNextEntityID(), getPlayer(playerID).getCenter(new Vector2()), shootAngle, playerID);
        addEntity(bullet);
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

//        for (Entity e : entities.values()) {
//            if(e instanceof Player) continue;
//
//            if(e instanceof Enemy) {
//                Enemy enemy = (Enemy) e;
//
//                if (Intersector.intersectRectangles(playerBody, enemy.getBody(), overlap)) {
//
//                    return true;
//                }
//            }
//        }

        return false;
    }

    public boolean checkCollisionsForProjectile(Projectile p) {
        Rectangle projectileBody = p.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for(int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if(tile.isObstacle()) {
                    obstacle = tile.getBody();

                    if(Intersector.intersectRectangles(projectileBody, obstacle, overlap)) {
                        entityIDsToRemove.add(p.getID());

                        if(tile instanceof BreakableTile) {
                            BreakableTile bt = (BreakableTile) tile;
                            bt.health--;

                            if(bt.health == 0) {
                                grid[i][j] = bt.getReplacementTile();
                                msgs.add(new Msg(Msg.MsgType.REPLACE_TILE, new ReplaceTileData(i, j, grid[i][j])));
                            }
                        }

                        return true;
                    }
                }
            }
        }

        for(Entity e : entities.values()) {
            Rectangle entityBody = e.getBody();

            if(Intersector.intersectRectangles(projectileBody, entityBody, overlap)) {
                if(e instanceof Enemy) {
                    entityIDsToRemove.add(p.getID());
                    entityIDsToRemove.add(e.getID());

                    getPlayer(p.getOwnerID()).addScore(100);
                }

                return true;
            }
        }

//        Rectangle playerBody = player.getBody();
//        if(Intersector.intersectRectangles(bulletBody, playerBody, overlap)) {
//            if (bullet.getEnemyBullet() == true){
//                bulletsToRemove.add(bullet);
//                player.setHealth(player.getHealth() - 1);
//                if (player.getHealth() <= 0){
//                    player.setIsDead(true);
//                }
//
//            }
//            return true;
//        }

        return false;
    }

    public void update(float delta) {
        for(Entity e : entities.values()) {
            e.update(delta);

            if(e instanceof Player)
                checkCollisionsForPlayer((Player) e);
            else if(e instanceof Projectile)
                checkCollisionsForProjectile((Projectile) e);

            if(e.checkChanged())
                msgs.add(new Msg(Msg.MsgType.ENTITY_UPDATE, new EntityUpdateData(e.getID(), e.getPosition(new Vector2()), e.getVelocity(new Vector2()))));
        }

        purgeEntities();
    }
}
