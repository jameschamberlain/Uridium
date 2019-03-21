package net.uridium.game.gameplay.entity.damageable.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Level;
import net.uridium.game.gameplay.ai.Pathfinder;
import net.uridium.game.gameplay.entity.damageable.DamageableEntity;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.entity.projectile.Bullet;

import java.util.ArrayList;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;

public abstract class Enemy extends DamageableEntity {
    public enum Type {
        BLUE_FISH,
        PINK_FISH,
        GREEN_FISH,
        SPIDER
    }

    /**
     * A list of Vector2 coords that map the route to the player.
     */
    private transient ArrayList<Vector2> routeToPlayer = new ArrayList<>();

    /**
     * The starting coord of the enemy (pathfinding).
     */
    private transient Vector2 pathfindingStart;

    /**
     * The starting coord of the enemy (pathfinding).
     */
    private transient Vector2 pathfindingEnd;

    /**
     * The next coord for the enemy to travel to (pathfinding).
     */
    private transient Vector2 nextPoint;

    /**
     * The pathfinder object (pathfinding).
     */
    private transient Pathfinder pathfinder;

    /**
     * The speed the enemy travels.
     */
    private float speed = 100;
    private float angle = 0;

    private transient Player target;

    transient Animation<TextureRegion> anim;
    private transient float stateTime;

    public Enemy(int ID, Rectangle body, int maxHealth, int health, float speed) {
        this(ID, body, new Vector2(0, 0), maxHealth, health, speed);
    }

    public Enemy(int ID, Rectangle body, Vector2 vel, int maxHealth, int health, float speed) {
        super(ID, body, vel, "", maxHealth, health);

        this.speed = speed;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (t == null && target != null && target.getHealth() > 0) updatePathfinding(delta);
        stateTime += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(anim != null) batch.draw(anim.getKeyFrame(stateTime, true), body.x, body.y, body.width / 2, body.height / 2, body.width, body.height, 1, 1, angle);
    }

    @Override
    public void loadTexture() {
        Gdx.app.postRunnable(this::loadAnim);
    }

    public abstract void loadAnim();

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    /**
     *
     * Updates the pathfinding for the enemy.
     *
     * @param delta Delta.
     */
    private void updatePathfinding(float delta) {
        if (target == null) return;

        // Check whether the route is empty or the player has moved.
        if (routeToPlayer.isEmpty() || target.getVelocity(new Vector2()).len2() > 0) {

            // Reset the pathfinding grid and set the start and end position (converting from pixel to grid).
            pathfinder.resetPaths();
            pathfindingStart = convertCoord(new Vector2(getBody().x, getBody().y));
            pathfindingEnd = convertCoord(new Vector2(target.getBody().x, target.getBody().y));

            // Check to see if the desired route is valid e.g. the start doesn't equal the end.
            if (!(pathfindingStart.equals(pathfindingEnd))) {
                try {
                    routeToPlayer = pathfinder.findPath(pathfindingStart, pathfindingEnd);
                    nextPoint = routeToPlayer.get(0);
                    routeToPlayer.remove(0);

                    setVelocityFromAngle(calculateAngleToNextPoint());
                } catch (Exception e) {}
            }
        }

        // If the enemy has traveled to the desired coordinate then get the next coordinate in the list.
        if (hasReachedNextPoint()) {
            // Attempt to get the next point if the list is not empty.
            if (!(routeToPlayer.isEmpty())) {
                nextPoint = routeToPlayer.get(0);
                routeToPlayer.remove(0);

                setVelocityFromAngle(calculateAngleToNextPoint());
            }
            else {
                setVelocity(0, 0);
            }
        }
    }

    /**
     *
     * Sets the target (one of the players).
     *
     * @param target The chosen player.
     */
    public void setTarget(Player target) {
        this.target = target;
    }

    /**
     *
     * Checks whether the enemey has reached te next point in the route.
     *
     * @return A boolean denoting whether the next point was reached.
     */
    private boolean hasReachedNextPoint() {
        double ceilX = Math.ceil(body.x);
        double floorX = Math.floor(body.x);
        double ceilY = Math.ceil(body.y);
        double floorY = Math.floor(body.y);

        try {
            if (ceilX == nextPoint.x || floorX == nextPoint.x)
                if (ceilY == nextPoint.y || floorY == nextPoint.y)
                    return true;
        } catch (Exception e) {}

        return false;
    }

    /**
     *
     * Calculates the angle to the player from the enemy.
     *
     * @return The angle to the player.
     */
    private float calculateAngleToNextPoint() {
        float x = body.x;
        float nextX = nextPoint.x;
        float xDifference = x - nextX;

        float y = body.y;
        float nextY = nextPoint.y;
        float yDifference = y - nextY;

        double a = Math.atan2(yDifference, xDifference);

        return (float) a;
    }

    /**
     *
     * Sets the velocity from a given angle.
     *
     * @param a The angle to the player from the enemy.
     */
    private void setVelocityFromAngle(float a) {
        angle = (float) Math.toDegrees(a);
        float xVel = -speed * (float) Math.cos(a);
        float yVel = -speed * (float) Math.sin(a);

        setVelocity(xVel, yVel);
    }

    /**
     *
     * Sets the pathfinding object.
     *
     * @param pathfinder The new pathfinder object.
     */
    public void setPathfinder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    /**
     * Converts a pixel to a grid coordinate.
     *
     * @param coord The pixel point.
     * @return The grid coordinate.
     */
    private Vector2 pixelToGrid(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        float a1 = TILE_WIDTH * 2 + ((TILE_WIDTH - body.width) / 2);
        x = ((x - a1) / TILE_WIDTH) + 1;
        y = ((y - a1) / TILE_WIDTH) + 1;
        return new Vector2(x, y);
    }

    /**
     * Converts a grid coordinate to a pixel point.
     *
     * @param coord The grid coordinate.
     * @return The pixel point.
     */
    public static Vector2 gridToPixel(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        // USING CONSTANT 40 INSTEAD OF BODY.WIDTH AS REFERENCED FROM STATIC CONTEXT
        float a1 = TILE_WIDTH * 2 + ((TILE_WIDTH - 40) / 2);
        x = ((x - 1) * TILE_WIDTH) + a1;
        y = ((y - 1) * TILE_WIDTH) + a1;
        return new Vector2(x, y);
    }

    /**
     * Classifies any pixel point into the grid position it is contained in.
     *
     * @param coord The pixel point.
     * @return The grid coordinate.
     */
    private Vector2 convertCoord(Vector2 coord) {
        coord = pixelToGrid(coord);
        coord.x = Math.round(coord.x);
        coord.y = Math.round(coord.y);
        float x = coord.x;
        float y = coord.y;
        return coord;
    }
}
