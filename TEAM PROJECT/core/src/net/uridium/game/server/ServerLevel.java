package net.uridium.game.server;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.ai.Pathfinder;
import net.uridium.game.gameplay.entity.EnemySpawner;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Enemy;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.entity.projectile.Bullet;
import net.uridium.game.gameplay.entity.projectile.Projectile;
import net.uridium.game.gameplay.tile.BreakableTile;
import net.uridium.game.gameplay.tile.DoorTile;
import net.uridium.game.gameplay.tile.Tile;
import net.uridium.game.screen.GameOver;
import net.uridium.game.server.msg.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class ServerLevel {
    int id;
    Tile[][] grid;
    int gridWidth;
    int gridHeight;

    ConcurrentHashMap<Integer, Entity> entities;
    ArrayList<Integer> entityIDsToRemove;
    ArrayList<Integer> playerIDs;

    ArrayList<Vector2> playerSpawnLocations;

    BlockingQueue<Msg> msgs;

    int nextEntityID;
    Random r;

    private boolean shouldChangeLevel;
    private int newLevelId;

    public ServerLevel(int id, Tile[][] grid, int gridWidth, int gridHeight, ArrayList<Vector2> playerSpawnLocations) {
        this(id, grid, gridWidth, gridHeight, playerSpawnLocations, new ArrayList<>());
    }

    public ServerLevel(int id, Tile[][] grid, int gridWidth, int gridHeight, ArrayList<Vector2> playerSpawnLocations, ArrayList<EnemySpawner> spawners) {
        this.id = id;
        this.grid = grid;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.playerSpawnLocations = playerSpawnLocations;

        entityIDsToRemove = new ArrayList<>();
        playerIDs = new ArrayList<>();

        entities = new ConcurrentHashMap<>();
        msgs = new LinkedBlockingQueue<>();

        r = new Random();

        for(EnemySpawner spawner : spawners) {
            spawner.setID(getNextEntityID());
            addEntity(spawner);
        }
    }


    public Vector2 getNewPlayerSpawn() {
        return playerSpawnLocations.get(playerIDs.size());
    }

    public int getNextEntityID() {
        int x = nextEntityID++;

        if(entities.containsKey(x))
            return getNextEntityID();

        return x;
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getID(), entity);
        msgs.add(new Msg(Msg.MsgType.NEW_ENTITY, entity));

        if(entity instanceof Player) {
            playerIDs.add(entity.getID());

            for(Integer i : playerIDs) {
                Player p = (Player) entities.get(i);
                msgs.add(new Msg(Msg.MsgType.PLAYER_SCORE, new PlayerScoreData(p.getID(), p.getScore())));
            }

            retargetEnemies();
        } else if(entity instanceof Enemy) {
            Enemy enemy = (Enemy) entity;
            enemy.setPathfinder(new Pathfinder(getObstacleGridPositionList(), gridWidth, gridHeight));

            Player player;
            if((player = getClosestPlayerToEnemy(enemy)) != null)
                enemy.setTarget(player);
        }
    }

    public void retargetEnemies() {
        for(Entity e : entities.values()) {
            if(e instanceof Enemy) {
                Enemy enemy = (Enemy) e;

                enemy.setTarget(getClosestPlayerToEnemy(enemy));
            }
        }
    }

    public void addEntities(ArrayList<? extends Entity> entities) {
        for(Entity entity : entities)
            addEntity(entity);
    }


    public Player getPlayer(int playerID) {
        return (Player) entities.get(playerID);
    }

    public ArrayList<Player> removePlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for(int id : playerIDs) {
            players.add((Player) entities.get(id));
            entities.remove(id);
        }

        playerIDs.clear();

        return players;
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

    public int getID() {
        return id;
    }

    public void createBullet(int playerID, double shootAngle) {
        Bullet bullet = new Bullet(getNextEntityID(), getPlayer(playerID).getCenter(new Vector2()), (float) shootAngle, playerID);
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
                        else if(overlap.width < overlap.height)
                            player.setX(player.getLastPos().x);
                        else {
                            player.setPosition(player.getLastPos());
                        }

                        return true;
                    }
                } else if (tile instanceof DoorTile) {
                    obstacle = tile.getBody();
                    int dest = ((DoorTile) tile).getDest();

                    if(Intersector.intersectRectangles(playerBody, obstacle, overlap)) {
                        newLevelId = dest;
                        shouldChangeLevel = true;

                        return true;
                    }
                }
            }
        }

        for (Entity e : entities.values()) {
            if(e instanceof Enemy) {
                Enemy enemy = (Enemy) e;

                if (Intersector.intersectRectangles(playerBody, enemy.getBody(), overlap)) {
                    entityIDsToRemove.add(enemy.getID());
                    player.damage(10 + 10 * new Random().nextInt(4));
                    msgs.add(new Msg(Msg.MsgType.PLAYER_HEALTH, new PlayerHealthData(player.getID(), player.getHealth(), player.getMaxHealth())));
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkCollisionsForEnemy(Enemy enemy) {
        Rectangle enemyBody = enemy.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for(int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if(tile.isObstacle() || tile instanceof DoorTile) {
                    obstacle = tile.getBody();

                    if(Intersector.intersectRectangles(enemyBody, obstacle, overlap)) {
                        if(overlap.width > overlap.height)
                            enemy.setY(enemy.getLastPos().y);
                        else if(overlap.width < overlap.height)
                            enemy.setX(enemy.getLastPos().x);
                        else {
                            enemy.setPosition(enemy.getLastPos());
                        }

                        return true;
                    }
                }
            }
        }

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

                    Player killer = getPlayer(p.getOwnerID());
                    killer.addScore(100);
                    msgs.add(new Msg(Msg.MsgType.PLAYER_SCORE, new PlayerScoreData(killer.getID(), killer.getScore())));
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

    public ArrayList<Vector2> getObstacleGridPositionList() {
        ArrayList<Vector2> positions = new ArrayList<>();

        for(int i = 1; i < gridWidth-1; i++) {
            for (int j = 1; j < gridHeight-1; j++) {
                Tile tile = grid[i][j];

                if(tile.isObstacle())
                    positions.add(new Vector2(tile.getGridX() - 1, tile.getGridY() - 1));
            }
        }

        return positions;
    }

    public Player getClosestPlayerToEnemy(Enemy enemy) {
        Player closestPlayer = null;
        float shortestDistance = Float.MAX_VALUE;

        Vector2 enemyPos = new Vector2();
        enemy.getPosition(enemyPos);
        Vector2 playerPos = new Vector2();
        for(Integer playerID : playerIDs) {
            Player player = (Player) entities.get(playerID);
            player.getPosition(playerPos);

            float xDif = playerPos.x - enemyPos.x;
            float yDif = playerPos.y - enemyPos.y;
            float distance = (float) Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));

            if(distance < shortestDistance) {
                shortestDistance = distance;
                closestPlayer = player;
            }
        }

        return closestPlayer;
    }

    public void update(float delta) {
        for(Entity e : entities.values()) {
            e.update(delta);



            if(e instanceof Player)
                checkCollisionsForPlayer((Player) e);
            else if(e instanceof Projectile)
                checkCollisionsForProjectile((Projectile) e);
            else if(e instanceof Enemy)
                checkCollisionsForEnemy((Enemy) e);
            else if(e instanceof EnemySpawner)
                handleEnemySpawner((EnemySpawner) e);

            if(e.checkChanged()) {
                msgs.add(new Msg(Msg.MsgType.ENTITY_UPDATE, new EntityUpdateData(e.getID(), e.getPosition(new Vector2()), e.getVelocity(new Vector2()))));
            }
        }

        purgeEntities();
    }

    public void handleEnemySpawner(EnemySpawner spawner) {
        if(!spawner.canSpawn()) return;

        int x = spawner.getGridX();
        int y = spawner.getGridY();
        ArrayList<Vector2> availableTiles = new ArrayList<>();

        for(int i = x - 1; i <= x + 1; i++) {
            for(int j = y - 1; j <= y + 1; j++) {
                if(i != x || j != y) {
                    Tile t = grid[i][j];
                    if(!t.isObstacle()) availableTiles.add(new Vector2(t.getBody().getX(), t.getBody().getY()));
                }
            }
        }

        Vector2 spawnPos = availableTiles.get(r.nextInt(availableTiles.size()));
        Enemy e = spawner.spawn(getNextEntityID(), spawnPos);
        e.setPosition(e.getPosition(new Vector2()).add(new Vector2((TILE_WIDTH - e.getBody().width) / 2, (TILE_HEIGHT - e.getBody().height) / 2)));
        addEntity(e);
    }

    public int shouldChangeLevel() {
        return shouldChangeLevel ? newLevelId : -1;
    }

    public void changedLevel() {
        shouldChangeLevel = false;
    }
}
