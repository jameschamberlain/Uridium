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

/**
 * The type Server level.
 */
public class ServerLevel {
    /**
     * The Id.
     */
    int id;

    /**
     * The Grid.
     */
    Tile[][] grid;

    /**
     * The Grid width.
     */
    int gridWidth;

    /**
     * The Grid height.
     */
    int gridHeight;

    /**
     * The Entities.
     */
    ConcurrentHashMap<Integer, Entity> entities;

    /**
     * The Entity i ds to remove.
     */
    ArrayList<Integer> entityIDsToRemove;

    /**
     * The Player i ds.
     */
    ArrayList<Integer> playerIDs;


    /**
     * The Entrances.
     */
    ArrayList<Vector2> entrances;

    /**
     * The Msgs.
     */
    BlockingQueue<Msg> msgs;

    /**
     * The Next entity id.
     */
    int nextEntityID = 4;

    /**
     * The R.
     */
    Random r;

    /**
     * Whether the server needs to change the level
     */
    private boolean shouldChangeLevel;

    /**
     * If the level needs to be changed, id of the new level
     */
    private int newLevelId;

    /**
     * If the level needs to be changed, id of the entrance to use
     */
    private int nextLevelEntrance;

    /**
     * Time the players entered this level
     */
    private long enteredTime = 0;

    /**
     * Whether the game has ended (won or all players dead)
     */
    private boolean gameEnded = false;

    /**
     * Whether doors have been unlocked/enabled
     */
    private boolean unlocked = false;

    /**
     * Instantiates a new Server level.
     *
     * @param id         the id
     * @param grid       the grid
     * @param gridWidth  the grid width
     * @param gridHeight the grid height
     * @param entrances  the entrances
     */
    public ServerLevel(int id, Tile[][] grid, int gridWidth, int gridHeight, ArrayList<Vector2> entrances) {
        this(id, grid, gridWidth, gridHeight, entrances, new ArrayList<>());
    }

    /**
     * Instantiates a new Server level.
     *
     * @param id         the id
     * @param grid       the grid
     * @param gridWidth  the grid width
     * @param gridHeight the grid height
     * @param entrances  the entrances
     * @param spawners   the spawners
     */
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

    /**
     * Spawn boss.
     */
    public void spawnBoss() {
        Boss b = new Boss(getNextEntityID(), new Vector2(gridWidth / 2 * TILE_WIDTH, gridHeight * 3 / 4 * TILE_HEIGHT), this);
        entities.put(b.getID(), b);
    }

    /**
     * Sets the time the user entered the level
     *
     * @param enteredTime the entered time
     */
    public void setEnteredTime(long enteredTime) {
        this.enteredTime = enteredTime;
    }

    /**
     * Gets entrance.
     *
     * @param entrance the entrance id
     * @return the entrance position
     */
    public Vector2 getEntrance(int entrance) {
        Vector2 pos = entrances.get(entrance - 1);

        return entrances.get(entrance - 1);
    }

    /**
     * Gets next available entity id.
     *
     * @return the next entity id
     */
    public int getNextEntityID() {
        int x = nextEntityID++;

        if (entities.containsKey(x))
            return getNextEntityID();

        return x;
    }

    /**
     * Add entity to the level
     *
     * @param entity the entity
     */
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

    /**
     * Retarget each enemy to the nearest player
     */
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

    /**
     * Gets a player.
     *
     * @param playerID the player id
     * @return the player
     */
    public Player getPlayer(int playerID) {
        return (Player) entities.get(playerID);
    }

    /**
     * Remove players from the level
     *
     * @return the removed players
     */
    public ArrayList<Player> removePlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (int id : playerIDs) {
            players.add((Player) entities.get(id));
            entities.remove(id);
        }

        playerIDs.clear();

        return players;
    }

    /**
     * Purge entities which need to be removed
     */
    public void purgeEntities() {
        for (Integer i : entityIDsToRemove) {
            entities.remove(i);
            msgs.add(new Msg(Msg.MsgType.REMOVE_ENTITY, new RemoveEntityData(i)));
        }

        entityIDsToRemove.clear();
    }

    /**
     * Gets msgs which need to be sent
     *
     * @return the msgs
     */
    public BlockingQueue<Msg> getMsgs() {
        return msgs;
    }

    /**
     * Gets level data to send to client
     *
     * @return the level data
     */
    public LevelData getLevelData() {
        return new LevelData(id, grid, gridWidth, gridHeight, new HashMap<Integer, Entity>(entities), -1);
    }

    /**
     * Gets num players in the game
     *
     * @return the num players
     */
    public int getNumPlayers() {
        return playerIDs.size();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * Create bullet.
     *
     * @param playerID   the player id
     * @param shootAngle the shoot angle
     */
    public void createBullet(int playerID, double shootAngle) {
        Bullet bullet = new Bullet(getNextEntityID(), getPlayer(playerID).getCenter(new Vector2()), (float) shootAngle, playerID);
        addEntity(bullet);
    }

    /**
     * Check collisions for player.with walls, doors, enemies, and items
     *
     * @param player the player
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
                            gameOver(false);
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

    /**
     * Check collisions for an enemy with walls
     *
     * @param enemy the enemy
     */
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

    /**
     * Check collisions for projectile with breakable tiles and entities
     *
     * @param p the p
     */
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

    /**
     * Gets a list of all the grid coordinates which contain obstacles
     *
     * @return the obstacle grid position list
     */
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

    /**
     * Gets closest player to enemy.
     *
     * @param enemy the enemy
     * @return the closest player to enemy
     */
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

    /**
     * Update the level
     *
     * @param delta the delta time
     */
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

    /**
     * Unlock doors in the level
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
     * Handle boss updates
     *
     * @param boss the boss
     */
    public void handleBoss(Boss boss) {
        if(boss.isDead() && !gameEnded)
            gameOver(true);
    }

    /**
     * Handle enemy spawner updates
     *
     * @param spawner the spawner
     */
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

    /**
     * Drop item on ground on enemy death
     *
     * @param pos the pos
     */
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

    /**
     * Checks if the server should change the level
     *
     * @return Returns the id of the level to change to if it should change, 0 otherwise
     */
    public int shouldChangeLevel() {
        return shouldChangeLevel ? newLevelId : 0;
    }

    /**
     * Gets the entrance of the next level to use if needed
     *
     * @return the next level entrance
     */
    public int getNextLevelEntrance() {
        return nextLevelEntrance;
    }

    /**
     * After changing level, makes sure upon re-entering the level that it does not instantly change level back
     */
    public void changedLevel() {
        shouldChangeLevel = false;
    }

    /**
     * Can change level boolean.
     *
     * @return Whether the level can be changed
     */
    public boolean canChangeLevel() {
        return System.currentTimeMillis() - enteredTime > 3000 && enteredTime != 0;
    }

    /**
     * Gets next player colour available
     *
     * @return A player colour
     */
    public Player.Colour getAvailColour() {
        return Player.Colour.values()[playerIDs.size()];
    }

    /**
     * Gets num players dead.
     *
     * @return the num players dead
     */
    public int getNumPlayersDead() {
        int dead = 0;

        for (Player p : getPlayers())
            if (p.getHealth() == 0) dead++;

        return dead;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (Entity e : entities.values())
            if (e instanceof Player) players.add((Player) e);

        return players;
    }

    /**
     * Game over.
     *
     * @param won Whether the game was won or not
     */
    public void gameOver(boolean won) {
        gameEnded = true;

        ArrayList<Player> players = getPlayers();
        Collections.sort(players, Collections.reverseOrder());

        for(int i = 0; i < players.size(); i++)
            if(players.get(i).getRank() == -1)
                players.get(i).setRank(i + 1);

        msgs.add(new Msg(Msg.MsgType.GAME_OVER, new GameOverData(getPlayers(), won)));
    }

    /**
     * Is game ended boolean.
     *
     * @return the boolean
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Gets grid width.
     *
     * @return the grid width
     */
    public int getGridWidth() {
        return gridWidth;
    }

    /**
     * Gets grid height.
     *
     * @return the grid height
     */
    public int getGridHeight() {
        return gridHeight;
    }
}
