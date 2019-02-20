package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Level;
import net.uridium.game.gameplay.ai.Pathfinder;

import java.util.ArrayList;

public class Enemy {
    final Rectangle body;
    /**
     * A list of Vector2 coords that map the route to the player.
     */
    private ArrayList<Vector2> routeToPlayer = new ArrayList<>();
    /**
     * The starting coord of the enemy (pathfinding).
     */
    private Vector2 pathfindingStart;
    /**
     * The goal coord of the enemy (pathfinding).
     */
    private Pathfinder pathfinder;
    private Vector2 pathfindingEnd;
    public Vector2 lastPos;
    public long lastShot = 0;
    private long reloadTime = 1000;
    private Level level;
    private Texture texture = new Texture(Gdx.files.internal("chicken.png"));

    public Enemy(float x, float y, float width, float height, Level level) {
        lastPos = new Vector2(x, y);
        body = new Rectangle(x, y, width, height);
        this.level = level;
    }

    public Rectangle getBody() {
        return body;
    }

    public Vector2 getCenter() {
        return new Vector2(body.x + body.width / 2, body.y + body.height / 2);
    }

    //stolen from player for now, will update in future
    public void shoot(float shootAngle) {
        level.spawnBullet(new Bullet(getCenter(), shootAngle), true);
        lastShot = System.currentTimeMillis();
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShot > reloadTime;
    }

    public void update(float delta) {
//        body.getPosition(lastPos);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, body.x, body.y, body.width, body.height);
    }

    public void setRouteToPlayer(ArrayList<Vector2> routeToPlayer) {
        this.routeToPlayer = routeToPlayer;
    }

    public ArrayList<Vector2> getRouteToPlayer() {
        return routeToPlayer;
    }

    public Vector2 getPathfindingStart() {
        return pathfindingStart;
    }

    public void setPathfindingStart(Vector2 pathfindingStart) {
        this.pathfindingStart = pathfindingStart;
    }

    public Vector2 getPathfindingEnd() {
        return pathfindingEnd;
    }

    public void setPathfindingEnd(Vector2 pathfindingEnd) {
        this.pathfindingEnd = pathfindingEnd;
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public void setPathfinder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }
}
