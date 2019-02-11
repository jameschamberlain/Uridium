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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import net.uridium.game.gameplay.entity.Bullet;
import net.uridium.game.gameplay.entity.Player;
import net.uridium.game.gameplay.entity.Enemy;
import net.uridium.game.gameplay.tile.BreakableTile;
import net.uridium.game.gameplay.tile.Tile;
import java.util.concurrent.TimeUnit;


import javax.sound.sampled.Clip;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

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

    Texture enemyTexture;

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

        enemyTexture = new Texture(Gdx.files.internal("chicken.png"));
        initEnemies();

        player = new Player(playerSpawnCenter.x - 27.5f, playerSpawnCenter.y - 27.5f, 55, 55, this);
        Gdx.input.setInputProcessor(player);
    }

    public void initEnemies() {
        enemies.add(new Enemy(240, 250, 40, 40));
        enemies.add(new Enemy(600, 500, 40, 40));
    }

    public int updateHealthBar(){
        int health = player.getHealth();
        return health;
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

        for (Enemy enemy : enemies) {
            if (Intersector.intersectRectangles(playerBody, enemy.getBody(), overlap)) {
                System.out.println(player.getHealth());
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

                        if(tile instanceof BreakableTile) {
                            BreakableTile bt = (BreakableTile) tile;
                            bt.health--;

                            if(bt.health == 0) {
                                grid[i][j] = bt.getReplacementTile();
                            }
                        }

                        return true;
                    }
                }
            }
        }

        for (Enemy enemy : enemies){
            if(Intersector.intersectRectangles(bulletBody, enemy.getBody(), overlap)) {
                bulletsToRemove.add(bullet);
                enemiesToRemove.add(enemy);
                player.setScore(player.getScore() + 100);
                return true;
            }
        }

        return false;
    }

    public void update(float delta) {
        player.update(delta);
        checkPlayerCollisions();

        for(Bullet b : bullets) {
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
            batch.draw(enemyTexture, enemy.getBody().getX(), enemy.getBody().getY(), enemy.getBody().getWidth(), enemy.getBody().getHeight());

        player.render(batch);

        matrix4 = batch.getProjectionMatrix();
        matrix4.translate(-xOffset, -yOffset, 0);
        batch.setProjectionMatrix(matrix4);
        myFont.draw(batch, "Score \n  " + outputScore,1130,670);
    }
}