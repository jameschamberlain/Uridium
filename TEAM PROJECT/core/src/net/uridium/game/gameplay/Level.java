package net.uridium.game.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Bullet;
import net.uridium.game.gameplay.entity.Player;
import net.uridium.game.gameplay.entity.Enemy;
import net.uridium.game.gameplay.tile.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.math.*;
import java.lang.Number.*;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Level {
    public int gridWidth;
    public int gridHeight;

    public static final float TILE_WIDTH = 64;
    public static final float TILE_HEIGHT = 64;

    public Tile[][] grid;

    float xOffset;
    float yOffset;

    private Player player;

    ArrayList<Bullet> bullets;
    ArrayList<Bullet> bulletsToRemove;
    ArrayList<Enemy> enemies;
    ArrayList<Enemy> enemiesToRemove;

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

        initEnemies();
        player = new Player(playerSpawnCenter.x - 27.5f, playerSpawnCenter.y - 27.5f, 55, 55, this);
        Gdx.input.setInputProcessor(player);

    }

    public void initEnemies() {
        enemies.add(new Enemy(280, 450, 40, 40, this));
        enemies.add(new Enemy(220, 330, 40, 40, this));
    }

    public boolean checkPlayerCollisions() {
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

        return false;
    }

    public void checkBulletCollisions() {
        for(Bullet bullet : bullets)
            checkBullet(bullet);
    }

    public boolean checkBullet(Bullet bullet) {
        Rectangle bulletBody = bullet.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for(int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if(tile.isObstacle()) {
                    obstacle = tile.getBody();

                    if(Intersector.intersectRectangles(bulletBody, obstacle, overlap)) {
                        bulletsToRemove.add(bullet);
                        return true;
                    }
                }
            }
        }

//        for (Enemy enemy : enemies){
//            Rectangle enemyBody = enemy.getBody();
//            if(Intersector.intersectRectangles(bulletBody, enemyBody, overlap)) {
//                bulletsToRemove.add(bullet);
//                enemiesToRemove.add(enemy);
//                return true;
//            }
//        }

        return false;
    }

    public void update(float delta) {
        player.update(delta);
        checkPlayerCollisions();

        float shootAngle;
        for(Enemy enemy : enemies) {
            shootAngle = calculateAngleToPlayer(enemy);
            if(enemy.canShoot())
                enemy.shoot(shootAngle);
        }

        for(Bullet b : bullets) {
            b.update(delta);
        }

        checkBulletCollisions();
        purgeBullets();
        purgeEnemies();
    }

    private void purgeBullets() {
        bullets.removeAll(bulletsToRemove);
        bulletsToRemove.clear();
    }

    public void purgeEnemies() {
        for(Enemy enemy : enemiesToRemove)
            enemies.remove(enemy);
    }

    public void spawnBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void render(SpriteBatch batch) {
        Matrix4 matrix4 = batch.getProjectionMatrix();
        matrix4.translate(xOffset, yOffset, 0);
        batch.setProjectionMatrix(matrix4);

        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j < gridHeight; j++) {
                grid[i][j].render(batch);
            }
        }

        for(Bullet bullet : bullets)
            bullet.render(batch);

        for (Enemy enemy : enemies)
            enemy.render(batch);

        player.render(batch);
    }

    //Calculates the angle to the player is from the enemy
    public float calculateAngleToPlayer(Enemy enemy){
        float playerX = player.lastPos.x;
        float enemyX =  enemy.getBody().getX();
        float xDifference = playerX - enemyX;

        float playerY = player.lastPos.y;
        float enemyY = enemy.getBody().getY();
        float yDifference = playerY - enemyY;

        double x = xDifference;
        double y = yDifference;

        float distanceToPlayer = ((float)x * (float)x) + ((float)y*(float)y);

        double a = Math.atan2(x,y);
        float angle = (float)Math.toDegrees(a);

        if (angle <= 0){
            angle= angle + 360;
        }
        System.out.println(angle);

        return angle;
    }


}