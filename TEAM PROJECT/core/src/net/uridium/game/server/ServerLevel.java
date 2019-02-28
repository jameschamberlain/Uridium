package net.uridium.game.server;

import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.entity.projectile.Bullet;
import net.uridium.game.gameplay.tile.Tile;
import net.uridium.game.server.msg.EntityUpdateData;
import net.uridium.game.server.msg.LevelData;
import net.uridium.game.server.msg.Msg;
import net.uridium.game.server.msg.PlayerMoveData;

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
        for(Integer i : entityIDsToRemove)
            entities.remove(i);

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

    public void update(float delta) {
        for(Entity e : entities.values()) {
            e.update(delta);

            if(e.checkChanged())
                msgs.add(new Msg(Msg.MsgType.ENTITY_UPDATE, new EntityUpdateData(e.getID(), e.getPosition(new Vector2()), e.getVelocity(new Vector2()))));
        }

        purgeEntities();
    }
}
