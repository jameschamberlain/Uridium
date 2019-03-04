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
import java.util.HashMap;

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
     * The next coord for the enemy to travel to (pathfinding).
     */
    private Vector2 nextPoint;
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

    public Vector2 pixelToGrid(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        x = ((x-76) / 64) + 1;
        y = ((y-76) / 64) + 1;
        return new Vector2(x, y);
    }

    public Vector2 gridToPixel(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        x = ((x-1) * 64) + 76;
        y = ((y-1) * 64) + 76;
        return new Vector2(x, y);
    }

    public Vector2 convertCoord(Vector2 coord) {
        float x = coord.x;
        float y = coord.y;
        if (x <= 44) {
            x = 0.0f;
        }
        else if (x <= 108) {
            x = 1.0f;
        }
        else if (x <= 172) {
            x = 2.0f;
        }
        else if (x <= 236) {
            x = 3.0f;
        }
        else if (x <= 300) {
            x = 4.0f;
        }
        else if (x <= 364) {
            x = 5.0f;
        }
        else if (x <= 428) {
            x = 5.0f;
        }
        else if (x <= 492) {
            x = 6.0f;
        }
        else if (x <= 556) {
            x = 7.0f;
        }
        else if (x <= 620) {
            x = 8.0f;
        }
        else if (x <= 684) {
            x = 9.0f;
        }
        else if (x <= 748) {
            x = 10.0f;
        }
        else if (x <= 812) {
            x = 11.0f;
        }
        else if (x <= 876) {
            x = 12.0f;
        }
        else if (x <= 940) {
            x = 13.0f;
        }
        else {
            x = 14.0f;
        }

        if (y <= 44) {
            y = 0.0f;
        }
        else if (y <= 108) {
            y = 1.0f;
        }
        else if (y <= 172) {
            y = 2.0f;
        }
        else if (y <= 236) {
            y = 3.0f;
        }
        else if (y <= 300) {
            y = 4.0f;
        }
        else if (y <= 364) {
            y = 5.0f;
        }
        else if (y <= 428) {
            y = 5.0f;
        }
        else if (y <= 492) {
            y = 6.0f;
        }
        else if (y <= 556) {
            y = 7.0f;
        }
        else if (y <= 620) {
            y = 8.0f;
        }
        else if (y <= 684) {
            y = 9.0f;
        }
        else {
            y = 10.0f;
        }
        return new Vector2(x, y);
    }

}
