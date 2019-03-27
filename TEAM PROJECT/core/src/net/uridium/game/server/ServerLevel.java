package net.uridium.game.server;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.ai.Pathfinder;
import net.uridium.game.gameplay.entity.EnemySpawner;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.entity.damageable.enemy.Bat;
import net.uridium.game.gameplay.entity.damageable.enemy.Boss;
import net.uridium.game.gameplay.entity.damageable.enemy.Enemy;
import net.uridium.game.gameplay.entity.damageable.enemy.Slime;
import net.uridium.game.gameplay.entity.item.Heal;
import net.uridium.game.gameplay.entity.item.Item;
import net.uridium.game.gameplay.entity.item.MovementSteroid;
import net.uridium.game.gameplay.entity.item.ShootingSteroid;
import net.uridium.game.gameplay.entity.projectile.Bullet;
import net.uridium.game.gameplay.entity.projectile.Projectile;
import net.uridium.game.gameplay.tile.BreakableTile;
import net.uridium.game.gameplay.tile.DoorTile;
import net.uridium.game.gameplay.tile.Tile;
import net.uridium.game.server.msg.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;

public class ServerLevel {
    int id;
    Tile[][] grid;
    int gridWidth;
    int gridHeight;

    ConcurrentHashMap<Integer, Entity> entities;
    ArrayList<Integer> entityIDsToRemove;
    ArrayList<Integer> playerIDs;

    ArrayList<Vector2> entrances;

    BlockingQueue<Msg> msgs;

    int nextEntityID = 4;
    Random r;

    private boolean shouldChangeLevel;
    private int newLevelId;
    private int nextLevelEntrance;

    private long enteredTime = 0;

    private boolean gameEnded = false;
    private boolean unlocked = false;

    public ServerLevel(int id, Tile[][] grid, int gridWidth, int gridHeight, ArrayList<Vector2> entrances) {
        this(id, grid, gridWidth, gridHeight, entrances, new ArrayList<>());
    }

    public ServerLevel(int id, Tile[][] grid, int gridWidth, int gridHeight, ArrayList<Vector2> entrances, ArrayList<EnemySpawner> spawners) {
        this.id = id;
        this.grid = grid;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.entrances = entrances;

        entityIDsToRemove = new ArrayList<>();
        playerIDs = new ArrayList<>();

        entities = new ConcurrentHashMap<>();
        msgs = new LinkedBlockingQueue<>();

        r = new Random();

        for (EnemySpawner spawner : spawners) {
            int spawnerId = getNextEntityID();
            spawner.setID(spawnerId);
            addEntity(spawner);
        }

        if(id == -1)
            spawnBoss();

        enteredTime = System.currentTimeMillis();
    }

    public void spawnBoss() {
        Boss b = new Boss(getNextEntityID(), new Vector2(gridWidth / 2 * TILE_WIDTH, gridHeight * 3 / 4 * TILE_HEIGHT), this);
        entities.put(b.getID(), b);
    }

    public void setEnteredTime(long enteredTime) {
        this.enteredTime = enteredTime;
    }

    public Vector2 getEntrance(int entrance) {
        Vector2 pos = entrances.get(entrance - 1);

        return entrances.get(entrance - 1);
    }

    public int getNextEntityID() {
        int x = nextEntityID++;

        if (entities.containsKey(x))
            return getNextEntityID();

        return x;
    }

    public void addEntity(Entity entity) {
//        if(entities.containsKey(entity.getID())) entity.setID(getNextEntityID());
        entities.put(entity.getID(), entity);
        msgs.add(new Msg(Msg.MsgType.NEW_ENTITY, entity));

        if (entity instanceof Player) {
            playerIDs.add(entity.getID());

            for (Integer i : playerIDs) {
                Player p = (Player) entities.get(i);
                msgs.add(new Msg(Msg.MsgType.PLAYER_UPDATE, new PlayerUpdateData(p.getID(), p.getScore(), p.getLevel(), p.getXp(), p.getXpToLevelUp(), false)));
            }

            retargetEnemies();
        } else if (entity instanceof Enemy && !(entity instanceof Boss) && !(entity instanceof Bat)&& !(entity instanceof Slime)) {
            Enemy enemy = (Enemy) entity;
            enemy.setPathfinder(new Pathfinder(getObstacleGridPositionList(), gridWidth, gridHeight));

            enemy.setTarget(getClosestPlayerToEnemy(enemy));
        }
    }

    public void retargetEnemies() {
        for (Entity e : entities.values()) {
            if (e instanceof Enemy && !(e instanceof Boss) && !(e instanceof Bat)&& !(e instanceof Slime)) {
                Enemy enemy = (Enemy) e;

                Player player;
                if ((player = getClosestPlayerToEnemy(enemy)) != null)
                    enemy.setTarget(player);
            }
        }
    }

    public void addEntities(ArrayList<? extends Entity> entities) {
        for (Entity entity : entities)
            addEntity(entity);
    }


    public Player getPlayer(int playerID) {
        return (Player) entities.get(playerID);
    }

    public ArrayList<Player> removePlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (int id : playerIDs) {
            players.add((Player) entities.get(id));
            entities.remove(id);
        }

        playerIDs.clear();

        return players;
    }

    public void purgeEntities() {
        for (Integer i : entityIDsToRemove) {
            entities.remove(i);
            msgs.add(new Msg(Msg.MsgType.REMOVE_ENTITY, new RemoveEntityData(i)));
        }

        entityIDsToRemove.clear();
    }

    public BlockingQueue<Msg> getMsgs() {
        return msgs;
    }

    public LevelData getLevelData() {
        return new LevelData(id, grid, gridWidth, gridHeight, new HashMap<Integer, Entity>(entities), -1);
    }

    public int getNumPlayers() {
        return playerIDs.size();
    }

    public int getID() {
        return id;
    }

    public void createBullet(int playerID, double shootAngle) {
        Bullet bullet = new Bullet(getNextEntityID(), getPlayer(playerID).getCenter(new Vector2()), (float) shootAngle, playerID);
        addEntity(bullet);
    }

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
                        else if (overlap.width < overlap.height)
                            player.setX(player.getLastPos().x);
                        else {
                            player.setPosition(player.getLastPos());
                        }

                        return;
                    }
                } else if (tile instanceof DoorTile) {
                    DoorTile door = (DoorTile) tile;
                    obstacle = door.getBody();
                    int dest = door.getDest();
                    int entrance = door.getEntrance();

                    if (Intersector.intersectRectangles(playerBody, obstacle, overlap) && canChangeLevel() && door.isEnabled()) {
                        newLevelId = dest;
                        shouldChangeLevel = true;
                        nextLevelEntrance = entrance;

                        return;
                    }
                }
            }
        }

        for (Entity e : entities.values()) {
            if (e instanceof Enemy && !(e instanceof Boss)) {
                Enemy enemy = (Enemy) e;

                if (Intersector.intersectRectangles(playerBody, enemy.getBody(), overlap)) {
                    entityIDsToRemove.add(enemy.getID());
                    player.damage(10 + 10 * new Random().nextInt(4));
                    msgs.add(new Msg(Msg.MsgType.PLAYER_HEALTH, new PlayerHealthData(player.getID(), player.getHealth(), player.getMaxHealth())));

                    if (player.getHealth() == 0) {
                        player.setPosition(new Vector2(-1000, -1000));
                        player.setVelocity(0, 0);
                        int rank = getNumPlayers() - getNumPlayersDead() + 1;
                        player.setRank(rank);
                        msgs.add(new Msg(Msg.MsgType.PLAYER_DEATH, new PlayerDeathData(player.getID(), rank)));
                        if (getNumPlayers() - getNumPlayersDead() == 0) {
                            gameOver();
                        }
                        retargetEnemies();
                    }

                    return;
                }
            } else if (e instanceof Item) {
                Item i = (Item) e;

                if (Intersector.intersectRectangles(playerBody, i.getBody(), overlap)) {
                    Msg m = i.onPlayerCollision(player);
                    if (m != null) msgs.add(m);
                    return;
                }
            }
        }

        if (playerBody.y + playerBody.height > gridHeight * TILE_HEIGHT || playerBody.y < 0)
            player.setY(player.getLastPos().y);
        else if (playerBody.x + playerBody.width > gridWidth * TILE_WIDTH || playerBody.x < 0)
            player.setX(player.getLastPos().x);
    }

    public void checkCollisionsForEnemy(Enemy enemy) {
        Rectangle enemyBody = enemy.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if (tile.isObstacle() || tile instanceof DoorTile) {
                    obstacle = tile.getBody();

                    if (Intersector.intersectRectangles(enemyBody, obstacle, overlap)) {
                        if(enemy instanceof Bat || enemy instanceof Slime) {
                            entityIDsToRemove.add(enemy.getID());
                            return;
                        }

                        if (overlap.width > overlap.height)
                            enemy.setY(enemy.getLastPos().y);
                        else if (overlap.width < overlap.height)
                            enemy.setX(enemy.getLastPos().x);
                        else {
                            enemy.setPosition(enemy.getLastPos());
                        }

                        return;
                    }
                }
            }
        }

        if (enemyBody.y + enemyBody.height > gridHeight * TILE_HEIGHT || enemyBody.y < 0)
            entityIDsToRemove.add(enemy.getID());
        else if (enemyBody.x + enemyBody.width > gridWidth * TILE_WIDTH || enemyBody.x < 0)
            entityIDsToRemove.add(enemy.getID());
    }

    public void checkCollisionsForProjectile(Projectile p) {
        Rectangle projectileBody = p.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if (tile.isObstacle()) {
                    obstacle = tile.getBody();

                    if (Intersector.intersectRectangles(projectileBody, obstacle, overlap)) {
                        entityIDsToRemove.add(p.getID());

                        if (tile instanceof BreakableTile) {
                            BreakableTile bt = (BreakableTile) tile;
                            bt.health--;

                            if (bt.health == 0) {
                                grid[i][j] = bt.getReplacementTile();
                                msgs.add(new Msg(Msg.MsgType.REPLACE_TILE, new ReplaceTileData(i, j, grid[i][j])));
                            }
                        }

                        return;
                    }
                }
            }
        }

        for (Entity e : entities.values()) {
            Rectangle entityBody = e.getBody();

            if (Intersector.intersectRectangles(projectileBody, entityBody, overlap)) {
                if (e instanceof Enemy) {
                    Enemy enemy = (Enemy) e;
                    enemy.damage(1);
                    entityIDsToRemove.add(p.getID());

                    if(enemy.isDead() && !(enemy instanceof Boss)) {
                        entityIDsToRemove.add(e.getID());

                        Player killer = getPlayer(p.getOwnerID());
                        killer.addScore(100);
                        killer.addXp(2.5f);
                        msgs.add(new Msg(Msg.MsgType.PLAYER_UPDATE, new PlayerUpdateData(killer.getID(), killer.getScore(), killer.getLevel(), killer.getXp(), killer.getXpToLevelUp(), killer.isLevelledUp())));
                        killer.setLevelledUpFalse();

                        dropItem(enemy.getPosition(new Vector2()));
                    }
                }

                return;
            }
        }
    }

    public ArrayList<Vector2> getObstacleGridPositionList() {
        ArrayList<Vector2> positions = new ArrayList<>();

        for (int i = 1; i < gridWidth - 1; i++) {
            for (int j = 1; j < gridHeight - 1; j++) {
                Tile tile = grid[i][j];

                if (tile.isObstacle())
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
        for (Integer playerID : playerIDs) {
            Player player = (Player) entities.get(playerID);
            if (player.getHealth() == 0) continue;
            player.getPosition(playerPos);

            float xDif = playerPos.x - enemyPos.x;
            float yDif = playerPos.y - enemyPos.y;
            float distance = (float) Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));

            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestPlayer = player;
            }
        }

        return closestPlayer;
    }

    public void update(float delta) {
        if (playerIDs.size() == 0) return;

        boolean noEnemies = canChangeLevel();

        for (Entity e : entities.values()) {
            e.update(delta);

            if (e instanceof Player) {
                checkCollisionsForPlayer((Player) e);
            } else if (e instanceof Projectile) {
                checkCollisionsForProjectile((Projectile) e);
            } else if (e instanceof Boss) {
                handleBoss((Boss) e);
            } else if (e instanceof Enemy) {
                noEnemies = false;
                checkCollisionsForEnemy((Enemy) e);
            } else if (e instanceof EnemySpawner) {
                EnemySpawner es = (EnemySpawner) e;
                if(es.getNumEnemies() != 0)
                    noEnemies = false;

                handleEnemySpawner(es);
            } else if (e instanceof Item) {
                if (((Item) e).isUsed())
                    entityIDsToRemove.add(e.getID());
            }

            if (e.checkChanged()) {
                if (e instanceof Enemy) {
                    msgs.add(new Msg(Msg.MsgType.ENTITY_UPDATE, new EntityUpdateData(e.getID(), e.getPosition(new Vector2()), e.getVelocity(new Vector2()), ((Enemy) e).getAngle(), ((Enemy) e).getHealth(), ((Enemy) e).getMaxHealth())));
                } else {
                    msgs.add(new Msg(Msg.MsgType.ENTITY_UPDATE, new EntityUpdateData(e.getID(), e.getPosition(new Vector2()), e.getVelocity(new Vector2()))));
                }
            }
        }

        if(!unlocked && noEnemies) {
            msgs.add(new Msg(Msg.MsgType.UNLOCK_DOORS, 0));
            unlocked = true;
            unlockDoors();
        }

        purgeEntities();
    }

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

    public void handleBoss(Boss boss) {
        if(boss.isDead() && !gameEnded)
            gameOver();
    }

    public void handleEnemySpawner(EnemySpawner spawner) {
        if (!spawner.canSpawn()) return;

        int x = spawner.getGridX();
        int y = spawner.getGridY();
        ArrayList<Vector2> availableTiles = new ArrayList<>();

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i != x || j != y) {
                    Tile t = grid[i][j];
                    if (!t.isObstacle()) availableTiles.add(new Vector2(t.getBody().getX(), t.getBody().getY()));
                }
            }
        }

        Vector2 spawnPos = availableTiles.get(r.nextInt(availableTiles.size()));
        Enemy e = spawner.spawn(getNextEntityID(), spawnPos);
        e.setPosition(e.getPosition(new Vector2()).add(new Vector2((TILE_WIDTH - e.getBody().width) / 2, (TILE_HEIGHT - e.getBody().height) / 2)));
        addEntity(e);
    }

    public void dropItem(Vector2 pos) {
        switch (r.nextInt(24)) {
            case 0:
            case 1:
                Heal h = new Heal(getNextEntityID(), new Rectangle(pos.x, pos.y, 40, 40));
                addEntity(h);
                break;
            case 2:
                ShootingSteroid ss = new ShootingSteroid(getNextEntityID(), new Rectangle(pos.x, pos.y, 40, 40));
                addEntity(ss);
                break;
            case 3:
                MovementSteroid ms = new MovementSteroid(getNextEntityID(), new Rectangle(pos.x, pos.y, 40, 40));
                addEntity(ms);
                break;
        }
    }

    public int shouldChangeLevel() {
        return shouldChangeLevel ? newLevelId : 0;
    }

    public int getNextLevelEntrance() {
        return nextLevelEntrance;
    }

    public void changedLevel() {
        shouldChangeLevel = false;
    }

    public boolean canChangeLevel() {
        return System.currentTimeMillis() - enteredTime > 3000 && enteredTime != 0;
    }

    public Player.Colour getAvailColour() {
        return Player.Colour.values()[playerIDs.size()];
    }

    public int getNumPlayersDead() {
        int dead = 0;

        for (Player p : getPlayers())
            if (p.getHealth() == 0) dead++;

        return dead;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (Entity e : entities.values())
            if (e instanceof Player) players.add((Player) e);

        return players;
    }

    public void gameOver() {
        gameEnded = true;

        ArrayList<Player> players = getPlayers();
        Collections.sort(players, Collections.reverseOrder());

        for(int i = 0; i < players.size(); i++)
            if(players.get(i).getRank() == -1)
                players.get(i).setRank(i + 1);

        msgs.add(new Msg(Msg.MsgType.GAME_OVER, new GameOverData(getPlayers())));
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }
}
