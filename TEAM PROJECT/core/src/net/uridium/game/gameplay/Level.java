package net.uridium.game.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.ai.Pathfinder;
import net.uridium.game.gameplay.entity.*;
import net.uridium.game.gameplay.entity.Enemy;
import net.uridium.game.gameplay.tile.BreakableTile;
import net.uridium.game.gameplay.tile.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.math.*;
import java.lang.Number.*;
import java.util.Vector;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Level {
    public int gridWidth;
    public int gridHeight;

    public static final float TILE_WIDTH = 64;
    public static final float TILE_HEIGHT = 64;

    /**
     * Current coordinate of the player
     */
    private Vector2 currentPlayerPos;
    /**
     * Last coordinate of the player
     */
    private Vector2 oldPlayerPos;

    public Tile[][] grid;

    float xOffset;
    float yOffset;

    public Player getPlayer() {
        return player;
    }

    private Player player;

    float enemyMoveSpeed = 50;

    ArrayList<Bullet> bullets;
    ArrayList<Bullet> bulletsToRemove;
    ArrayList<Enemy> enemies;
    ArrayList<Enemy> enemiesToRemove;

    String outputScore;
    BitmapFont myFont = new BitmapFont(Gdx.files.internal("arial.fnt"));

    public Level(Tile[][] grid, int gridWidth, int gridHeight, Vector2 playerSpawnCenter) {

        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesToRemove = new ArrayList<>();

        this.grid = grid;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        xOffset = GAME_WIDTH - (gridWidth * TILE_WIDTH);
        xOffset /= 2;
        yOffset = GAME_HEIGHT - (gridHeight * TILE_HEIGHT);
        yOffset /= 2;


        player = new Player(playerSpawnCenter.x - 27.5f, playerSpawnCenter.y - 27.5f, 55, 55, this);
        // Setup the current player coordinate
        currentPlayerPos = new Vector2(player.getBody().x, player.getBody().y);
        initEnemies();
        Gdx.input.setInputProcessor(player);

    }

    public void initEnemies() {
        enemies.add(new Enemy(652, 460, 40, 40, this));
        enemies.add(new Enemy(204, 332, 40, 40, this));
        // The list of obstacles in the world.
        ArrayList<Vector2> obstacles = new ArrayList<>();
        obstacles.add(new Vector2(10, 1));
        obstacles.add(new Vector2(10, 2));
        obstacles.add(new Vector2(9, 3));
        obstacles.add(new Vector2(10, 3));
        obstacles.add(new Vector2(10, 5));
        obstacles.add(new Vector2(4, 6));
        obstacles.add(new Vector2(5, 6));
        obstacles.add(new Vector2(6, 6));
        obstacles.add(new Vector2(10, 6));
        obstacles.add(new Vector2(11, 6));
        obstacles.add(new Vector2(4, 7));
        obstacles.add(new Vector2(5, 7));
        obstacles.add(new Vector2(6, 7));
        for (Enemy enemy : enemies) {
            // Setup the pathfinder for the enemies to use.
            enemy.setPathfinder(new Pathfinder(obstacles));
            enemy.setPathfindingStart(new Vector2(enemy.getBody().x, enemy.getBody().y));
            enemy.setPathfindingEnd(new Vector2(getPlayer().getBody().x, getPlayer().getBody().y));
            enemy.setRouteToPlayer(enemy.getPathfinder().findPath(enemy.getPathfindingStart(), enemy.getPathfindingEnd()));
            enemy.setNextPoint(enemy.gridToPixel(enemy.getPathfindingStart()));
            oldPlayerPos = enemy.convertCoord(new Vector2(player.getBody().x, player.getBody().y));
        }
    }

    public boolean checkPlayerCollisions() {
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
                        Rectangle playerBodyOldX = new Rectangle(player.lastPos.x, playerBody.y, playerBody.width, playerBody.height);
                        if (!overlap.overlaps(playerBodyOldX))
                            player.goToLastXPos();

                        Rectangle playerBodyOldY = new Rectangle(playerBody.x, player.lastPos.y, playerBody.width, playerBody.height);
                        if (!overlap.overlaps(playerBodyOldY))
                            player.goToLastYPos();

                        return true;
                    }
                }
            }
        }

        for (Enemy enemy : enemies) {
            if (Intersector.intersectRectangles(playerBody, enemy.getBody(), overlap)) {
                Rectangle playerBodyOldX = new Rectangle(player.lastPos.x, playerBody.y, playerBody.width, playerBody.height);
                if (!overlap.overlaps(playerBodyOldX))
                    player.goToLastXPos();

                Rectangle playerBodyOldY = new Rectangle(playerBody.x, player.lastPos.y, playerBody.width, playerBody.height);
                if (!overlap.overlaps(playerBodyOldY))
                    player.goToLastYPos();

                return true;
            }
        }

        return false;
    }

    public void checkBulletCollisions() {
        for (Bullet bullet : bullets)
            checkBullet(bullet);
    }

    public boolean checkBullet(Bullet bullet) {
        Rectangle bulletBody = bullet.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if (tile.isObstacle()) {
                    obstacle = tile.getBody();

                    if (Intersector.intersectRectangles(bulletBody, obstacle, overlap)) {
                        bulletsToRemove.add(bullet);

                        if (tile instanceof BreakableTile) {
                            BreakableTile bt = (BreakableTile) tile;
                            bt.health--;

                            if (bt.health == 0) {
                                grid[i][j] = bt.getReplacementTile();
                            }
                        }

                        return true;
                    }
                }
            }
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBody = enemy.getBody();
            if (Intersector.intersectRectangles(bulletBody, enemyBody, overlap)) {
                if (bullet.getEnemyBullet() == false) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    player.setScore(player.getScore() + 100);
                }
                return true;
            }
        }

        Rectangle playerBody = player.getBody();
        if (Intersector.intersectRectangles(bulletBody, playerBody, overlap)) {
            if (bullet.getEnemyBullet() == true) {
                bulletsToRemove.add(bullet);
                player.setHealth(player.getHealth() - 1);
                if (player.getHealth() <= 0) {
                    player.setIsDead(true);
                }

            }
            return true;
        }

        return false;
    }

    public void update(float delta) {
        player.update(delta);
        checkPlayerCollisions();
        float shootAngle;
        for (Enemy enemy : enemies) {
            currentPlayerPos = enemy.convertCoord(new Vector2(player.getBody().x, player.getBody().y));
            shootAngle = calculateAngleToPlayer(enemy);
            if (enemy.canShoot()) {
                enemy.shoot(shootAngle);
            }
            // Check whether the enemy is in the same position as the player
            if (!(enemy.convertCoord(enemy.getCenter()).equals(currentPlayerPos))) {
                // Check whether the route is empty or the player has moved
                if (enemy.getRouteToPlayer().isEmpty()
                        || Math.abs(oldPlayerPos.x - currentPlayerPos.x) > 0
                        || Math.abs(oldPlayerPos.y - currentPlayerPos.y) > 0) {
                    // Reset the pathfinding grid and set the start and end position
                    enemy.getPathfinder().resetPaths();
                    enemy.setPathfindingStart(new Vector2(enemy.getBody().x, enemy.getBody().y));
                    enemy.setPathfindingEnd(new Vector2(getPlayer().getBody().x, getPlayer().getBody().y));
                    // Check to see if the desired route is valid e.g. the start doesn't equal the end
                    if (!(enemy.getPathfindingStart().equals(enemy.getPathfindingEnd()))) {
                        enemy.setRouteToPlayer(enemy.getPathfinder().findPath(enemy.getPathfindingStart(), enemy.getPathfindingEnd()));
                        oldPlayerPos = enemy.convertCoord(new Vector2(player.getBody().x, player.getBody().y));
                        enemy.setNextPoint(enemy.getRouteToPlayer().get(0));
                        enemy.getRouteToPlayer().remove(0);
                    }
                }
            }
            // If the enemy has traveled to the desired coordinate then get the next coordinate in the list
            if ((((float) Math.ceil(enemy.getBody().x)) == enemy.getNextPoint().x) && (((float) Math.ceil(enemy.getBody().y)) == enemy.getNextPoint().y)) {
                // Only attempt to get the next point if the list is not empty
                if (!(enemy.getRouteToPlayer().isEmpty())) {
                    enemy.setNextPoint(enemy.getRouteToPlayer().get(0));
                    enemy.getRouteToPlayer().remove(0);
                }
            }
            moveEnemy(enemy, enemy.getNextPoint().x, enemy.getNextPoint().y, delta);

        }

        for (Bullet b : bullets) {
            b.update(delta);
        }

        checkBulletCollisions();
        purgeBullets();
        purgeEnemies();
        outputScore = String.valueOf(player.getScore());
    }

    private void purgeBullets() {
        bullets.removeAll(bulletsToRemove);
        bulletsToRemove.clear();
    }

    private void purgeEnemies() {
        enemies.removeAll(enemiesToRemove);
        enemiesToRemove.clear();
    }

    public void spawnBullet(Bullet bullet, Boolean enemyBullet) {
        bullet.setEnemyBullet(enemyBullet);
        bullets.add(bullet);
    }

    public void render(SpriteBatch batch) {
        Matrix4 matrix4 = batch.getProjectionMatrix();
        matrix4.translate(xOffset, yOffset, 0);
        batch.setProjectionMatrix(matrix4);

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j].render(batch);
            }
        }

        for (Bullet bullet : bullets)
            bullet.render(batch);

        for (Enemy enemy : enemies)
            enemy.render(batch);

        player.render(batch);

        matrix4 = batch.getProjectionMatrix();
        matrix4.translate(-xOffset, -yOffset, 0);
        batch.setProjectionMatrix(matrix4);
        if (player.getIsDead()) {
            myFont.draw(batch, "YOU'RE DEAD BITCH", 500, 500);

        }
        myFont.draw(batch, "Score \n  " + outputScore, 1130, 670);
    }

    //Calculates the angle to the player is from the enemy
    public float calculateAngleToPlayer(Enemy enemy) {
        float playerX = player.lastPos.x;
        float enemyX = enemy.getBody().getX();
        float xDifference = playerX - enemyX;

        float playerY = player.lastPos.y;
        float enemyY = enemy.getBody().getY();
        float yDifference = playerY - enemyY;

        double x = xDifference;
        double y = yDifference;

        float distanceToPlayer = ((float) x * (float) x) + ((float) y * (float) y);

        double a = Math.atan2(x, y);
        float angle = (float) Math.toDegrees(a);

        if (angle <= 0) {
            angle = angle + 360;
        }
        //System.out.println(angle);

        return angle;
    }

    //Moves the enemy to a new X and Y coordinates
    public void moveEnemy(Enemy enemy, float newX, float newY, float delta) {
        if (enemy.getBody().x != newX || enemy.getBody().y != newY) {
            if (enemy.getBody().x < newX) {
                enemy.getBody().x += enemyMoveSpeed * delta;
            }
            if (enemy.getBody().x > newX) {
                enemy.getBody().x -= enemyMoveSpeed * delta;
            }
            if (enemy.getBody().y < newY) {
                enemy.getBody().y += enemyMoveSpeed * delta;
            }
            if (enemy.getBody().y > newY) {
                enemy.getBody().y -= enemyMoveSpeed * delta;
            }
        }
    }

}