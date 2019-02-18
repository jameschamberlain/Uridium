package net.uridium.game.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Bullet;
import net.uridium.game.gameplay.entity.Player;
import net.uridium.game.gameplay.entity.Enemy;
import net.uridium.game.gameplay.entity.myPackage;
import net.uridium.game.util.Colors;

import javax.sound.sampled.Clip;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Level {
    private myPackage aPackage;

    public int gridWidth;
    public int gridHeight;

    public static final float TILE_WIDTH = 64;
    public static final float TILE_HEIGHT = 64;

    ArrayList<String> rows;
    public Tile[][] grid;

    float xOffset;
    float yOffset;

    Player player;
    Vector2 playerSpawn;

    Texture enemyTexture;

    Socket s;
    PrintStream ps;
    ObjectInputStream oi;

    HashMap<Integer,Vector2> players;
    ArrayList<Bullet> bullets;
    ArrayList<Bullet> bulletsToRemove;
    ArrayList<Rectangle> enemies;
    ArrayList<Rectangle> enemiesToRemove;

    public Level(FileHandle fileHandle) {
        //Client Service starts
        try {
            s = new Socket("127.0.0.1",9988);
            ps = new PrintStream(s.getOutputStream());
            oi = new ObjectInputStream(s.getInputStream());
            aPackage = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesToRemove = new ArrayList<>();

        rows = new ArrayList<>();

        try {
            String line;
            BufferedReader reader = fileHandle.reader(2048);
            while((line = reader.readLine()) != null){
                rows.add(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        players = new HashMap<Integer, Vector2>();
    }

    public void init() {

        enemyTexture = new Texture(Gdx.files.internal("chicken.png"));

        gridHeight = rows.size();
        gridWidth = rows.get(0).length();

        grid = new Tile[gridWidth][gridHeight];

        xOffset = GAME_WIDTH - (gridWidth * TILE_WIDTH);
        xOffset /= 2;
        yOffset = GAME_HEIGHT - (gridHeight * TILE_HEIGHT);
        yOffset /= 2;

        initEnemies();

        Texture tileTexture;
        String row;
        boolean isObstacle = false;
        for(int j = 0; j < gridHeight; j++) {
            row = rows.get(j);
            for(int i = 0; i < gridWidth; i++) {
                char c = row.charAt(i);
                isObstacle = false;

                switch(c) {
                    case 'W':
                        tileTexture = new Texture(Gdx.files.internal("block_04.png"));
                        isObstacle = true;
                        break;
                    case 'O':
                        tileTexture = new Texture(Gdx.files.internal("crate_08.png"));
                        isObstacle = true;
                        break;
                    case 'D':
                        tileTexture = new Texture(Gdx.files.internal("ground_03.png"));
                        break;
                    case 'P':
                        playerSpawn = new Vector2(xOffset + i * TILE_WIDTH, yOffset + j * TILE_HEIGHT);
                        System.out.println("Before Send");
                        System.out.println(playerSpawn.x+" "+playerSpawn.y);
                        ps.println(playerSpawn.x+" "+playerSpawn.y);
                        System.out.println("After Send");
                    default:
                        tileTexture = new Texture(Gdx.files.internal("ground_06.png"));
                        break;
                }

                grid[i][gridHeight - 1 - j] = new Tile(i, gridHeight - 1 - j, tileTexture, xOffset, yOffset, isObstacle);
            }
        }

        //Get Init data
        new Thread(()-> {
            while(true){
                try {
                    aPackage = (myPackage) oi.readObject();
                    players = aPackage.getPlayers();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The size is "+players.size());
        players = aPackage.getPlayers();
        for(Vector2 p : players.values()){
            player = new Player(p.x, p.y, 55, 55, this);
            //Gdx.input.setInputProcessor(player);
        }

    }

    public void initEnemies() {
        enemies.add(new Rectangle(180, 250, 40, 40));
        enemies.add(new Rectangle(220, 330, 40, 40));
    }

    public boolean checkPlayerCollisions() {
        Rectangle playerBody = player.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for(int i = 0; i < gridWidth; i++) {
                    for (int j = 0; j < gridHeight; j++) {
                        tile = grid[i][j];

                if(tile.isObstacle) {
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

                if(tile.isObstacle) {
                    obstacle = tile.getBody();

                    if(Intersector.intersectRectangles(bulletBody, obstacle, overlap)) {
                        bulletsToRemove.add(bullet);
                        return true;
                    }
                }
            }
        }

        for (Rectangle enemy : enemies){
            if(Intersector.intersectRectangles(bulletBody, enemy, overlap)) {
                bulletsToRemove.add(bullet);
                enemiesToRemove.add(enemy);
                return true;
            }
        }

        return false;
    }

    public void update(float delta) {
        //player.getBody().getPosition(player.getLastPos());

        //Rectangle body = player.getBody();
        float moveSpeed = player.getMoveSpeed();
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            player.setLastPosY(player.getLastPos().y += moveSpeed * delta);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            player.setLastPosX(player.getLastPos().x -= moveSpeed * delta);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            player.setLastPosY(player.getLastPos().y -= moveSpeed * delta);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            player.setLastPosX(player.getLastPos().x += moveSpeed * delta);


        System.out.println("InputProcessor sent "+player.getLastPos().x+"-----"+player.getLastPos().y);
        //Instead of doing change here, Sending the location to Server
        //Ensuer Collosion happenes, do not go to first Position.

        checkPlayerCollisions();
        ps.println(player.getLastPos().x+" "+player.getLastPos().y);
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        for(Bullet b : bullets) {
            b.update(delta);
        }

        checkBulletCollisions();
        purgeBullets();
        purgeEnemies();
    }

    public void purgeBullets() {
        for(Bullet bullet : bulletsToRemove)
            bullets.remove(bullet);
    }

    public void purgeEnemies() {
        for(Rectangle enemy : enemiesToRemove)
            enemies.remove(enemy);
    }

    public void spawnBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void render(SpriteBatch batch) {

        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j < gridHeight; j++) {
                grid[i][j].render(batch);
            }
        }

        for(Bullet bullet : bullets)
            bullet.render(batch);

        for (Rectangle enemy : enemies)
            batch.draw(enemyTexture, enemy.x, enemy.y, enemy.width, enemy.height);


        for(Vector2 p:players.values()){
            player.setBody(p);
            System.out.println("The body has been set to "+p.x+"---"+p.y);
            player.render(batch);
        }
    }
}