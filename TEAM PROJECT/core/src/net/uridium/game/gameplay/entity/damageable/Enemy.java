package net.uridium.game.gameplay.entity.damageable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Level;
import net.uridium.game.gameplay.ai.Pathfinder;
import net.uridium.game.gameplay.entity.projectile.Bullet;

import java.util.ArrayList;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;

public class Enemy extends DamageableEntity {
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

    private float speed = 125;

    private transient Player target;


    public Enemy(int ID, Rectangle body, int maxHealth, int health) {
        super(ID, body, new Vector2(0, 0), "chicken.png", maxHealth, health);
    }

    public Enemy(int ID, Rectangle body, Vector2 vel, String textureFile, int maxHealth, int health) {
        super(ID, body, vel, textureFile, maxHealth, health);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(t == null) updatePathfinding(delta);
    }

    public void updatePathfinding(float delta) {
        if(target == null) return;

        // Check whether the route is empty or the player has moved
        if (getRouteToPlayer().isEmpty() || target.getVelocity(new Vector2()).len2() > 0) {
//                || Math.abs(oldPlayerPos.x - currentPlayerPos.x) > 0
//                || Math.abs(oldPlayerPos.y - currentPlayerPos.y) > 0) {


            // Reset the pathfinding grid and set the start and end position

            getPathfinder().resetPaths();
            setPathfindingStart(new Vector2(getBody().x, getBody().y));
            setPathfindingEnd(new Vector2(target.getBody().x, target.getBody().y));

            // Check to see if the desired route is valid e.g. the start doesn't equal the end
            if (!(getPathfindingStart().equals(getPathfindingEnd()))) {
                setRouteToPlayer(getPathfinder().findPath(getPathfindingStart(), getPathfindingEnd()));
//                oldPlayerPos = convertCoord(new Vector2(player.getBody().x, player.getBody().y));
                setNextPoint(getRouteToPlayer().get(0));
                getRouteToPlayer().remove(0);

                setVelocityFromAngle(calculateAngleToNextPoint());
            }
        }

        // If the enemy has traveled to the desired coordinate then get the next coordinate in the list
        if (hasReachedNextPoint()) {
            // Only attempt to get the next point if the list is not empty
//            System.out.println("FUCK");
            if (!(getRouteToPlayer().isEmpty())) {
                setNextPoint(getRouteToPlayer().get(0));
                getRouteToPlayer().remove(0);

                setVelocityFromAngle(calculateAngleToNextPoint());
            } else {
                setVelocity(0, 0);
            }
        } else {
//            System.out.println("body.x -> " + Math.ceil(body.x) + " body.y -> " + Math.ceil(body.y) + " next.x -> " + nextPoint.x + " next.y" + nextPoint.y);
        }
    }

    // PATHFINDING SHIZZLE WIZZLE
    public void setTarget(Player target) {
        this.target = target;
    }

    public boolean hasReachedNextPoint() {
        double ceilX = Math.ceil(body.x);
        double floorX = Math.floor(body.x);
        double ceilY = Math.ceil(body.y);
        double floorY = Math.floor(body.y);

        if(ceilX == getNextPoint().x || floorX == getNextPoint().x)
            if(ceilY == getNextPoint().y || floorY == getNextPoint().y)
                return true;

        return false;
    }

    //Calculates the angle to the player is from the enemy
    public float calculateAngleToNextPoint(){
        float x = body.x;
        float nextX = nextPoint.x;
        float xDifference = x - nextX;

        float y = body.y;
        float nextY = nextPoint.y;
        float yDifference = y - nextY;

        double a = Math.atan2(yDifference, xDifference);

        return (float) a;
    }

    public void setVelocityFromAngle(float a) {
        float xVel = -speed * (float) Math.cos(a);
        float yVel = -speed * (float) Math.sin(a);

        setVelocity(xVel, yVel);
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
        this.pathfindingStart = convertCoord(pathfindingStart);
    }

    public Vector2 getPathfindingEnd() {
        return pathfindingEnd;
    }

    public void setPathfindingEnd(Vector2 pathfindingEnd) {
        this.pathfindingEnd = convertCoord(pathfindingEnd);
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public void setPathfinder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    public Vector2 getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(Vector2 nextPoint) {
        this.nextPoint = nextPoint;
    }

    /**
     *
     * Converts a pixel to a grid coordinate.
     *
     * @param coord The pixel point.
     *
     * @return The grid coordinate.
     */
    public Vector2 pixelToGrid(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        x = ((x-(2.5f*TILE_WIDTH)) / TILE_WIDTH) + 1;
        y = ((y-(2.5f*TILE_HEIGHT)) / TILE_HEIGHT) + 1;
        return new Vector2(x, y);
    }

    /**
     *
     * Converts a grid coordinate to a pixel point.
     *
     * @param coord The grid coordinate.
     *
     * @return The pixel point.
     */
    public static Vector2 gridToPixel(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        x = ((x-1) * TILE_WIDTH) + (2.5f*TILE_WIDTH);
        y = ((y-1) * TILE_HEIGHT) + (2.5f*TILE_HEIGHT);
        return new Vector2(x, y);
    }

    /**
     *
     * Classifies any pixel point into the grid position it is contained in.
     *
     * @param coord The pixel point.
     *
     * @return The grid coordinate.
     */
    public Vector2  convertCoord(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        x -= TILE_WIDTH * 1.5;
        x -= (TILE_WIDTH - body.width) / 2;
        x /= TILE_WIDTH;
        x -= (x%1);
        x += 1;

        y -= TILE_HEIGHT * 1.5;
        y -= (TILE_HEIGHT - body.height) / 2;
        y /= TILE_HEIGHT;
        y -= (y%1);
        y += 1;
//        if (x <= 76) {
//            x = 0.0f;
//        }
//        else if (x <= 172) {
//            x = 1.0f;
//        }
//        else if (x <= 236) {
//            x = 2.0f;
//        }
//        else if (x <= 300) {
//            x = 3.0f;
//        }
//        else if (x <= 364) {
//            x = 4.0f;
//        }
//        else if (x <= 428) {
//            x = 5.0f;
//        }
//        else if (x <= 492) {
//            x = 6.0f;
//        }
//        else if (x <= 556) {
//            x = 7.0f;
//        }
//        else if (x <= 620) {
//            x = 8.0f;
//        }
//        else if (x <= 684) {
//            x = 9.0f;
//        }
//        else if (x <= 748) {
//            x = 10.0f;
//        }
//        else if (x <= 812) {
//            x = 11.0f;
//        }
//        else if (x <= 876) {
//            x = 12.0f;
//        }
//        else {
//            x = 13.0f;
//        }

//        if (y <= 108) {
//            y = 0.0f;
//        }
//        else if (y <= 172) {
//            y = 1.0f;
//        }
//        else if (y <= 236) {
//            y = 2.0f;
//        }
//        else if (y <= 300) {
//            y = 3.0f;
//        }
//        else if (y <= 364) {
//            y = 4.0f;
//        }
//        else if (y <= 428) {
//            y = 5.0f;
//        }
//        else if (y <= 492) {
//            y = 6.0f;
//        }
//        else if (y <= 556) {
//            y = 7.0f;
//        }
//        else {
//            y = 8.0f;
//        }
        return new Vector2(x, y);
    }
}
