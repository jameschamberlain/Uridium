package net.uridium.game.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.gameplay.entity.projectile.Bullet;
import net.uridium.game.gameplay.tile.Tile;
import net.uridium.game.server.msg.EntityUpdateData;
import net.uridium.game.server.msg.LevelData;
import net.uridium.game.server.msg.PlayerMoveData;
import net.uridium.game.server.msg.PlayerMoveData.Dir;

import java.util.HashMap;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Level {
    Tile[][] grid;
    public int gridWidth;
    public int gridHeight;

    public static final float TILE_WIDTH = 64;
    public static final float TILE_HEIGHT = 64;
    float xOffset;
    float yOffset;

    float enemyMoveSpeed = 30;

    HashMap<Integer, Entity> entities;
    int playerID;

//    String outputScore;
//    BitmapFont myFont = new BitmapFont(Gdx.files.internal("arial.fnt"));

    public Level(LevelData levelData) {
        entities = new HashMap<>();

        this.grid = levelData.grid;
        this.gridWidth = levelData.gridWidth;
        this.gridHeight = levelData.gridHeight;
        this.entities.putAll(levelData.entities);
        this.playerID = levelData.playerID;

        xOffset = GAME_WIDTH - (gridWidth * TILE_WIDTH);
        xOffset /= 2;
        yOffset = GAME_HEIGHT - (gridHeight * TILE_HEIGHT);
        yOffset /= 2;

        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j < gridHeight; j++) {
                grid[i][j].loadTexture();
            }
        }

        for(Entity e : entities.values())
            e.loadTexture();

        /*try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public void addEntity(Entity e) {
        entities.put(e.getID(), e);
        e.loadTexture();
    }

    public void updateEntity(EntityUpdateData entityUpdateData) {
        Entity e = entities.get(entityUpdateData.ID);
        e.setPosition(entityUpdateData.pos);
        e.setVelocity(entityUpdateData.vel);
    }

    public Player getPlayer() {
        return (Player) entities.get(playerID);
    }

    public void update(float delta) {
        for(Entity e : entities.values())
            e.update(delta);

//        checkPlayerCollisions();

        //bottom two values are temporary
//        float newX = 200;
//        float newY = 300;
//        float shootAngle;
//        for(Enemy enemy : enemies) {
//            shootAngle = calculateAngleToPlayer(enemy);
//            if(enemy.canShoot()){
//                enemy.shoot(shootAngle);
//            }
//            //call to james' function to provide update to newx and newy
//            moveEnemy(enemy, newX, newY, delta);
//        }

//        for(Bullet b : bullets) {
//            b.update(delta);
//        }

//        checkBulletCollisions();
//        purgeBullets();
//        purgeEnemies();
//        outputScore = String.valueOf(player.getScore());
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

        for(Entity e : entities.values())
            e.render(batch);

//        matrix4 = batch.getProjectionMatrix();
//        matrix4.translate(-xOffset, -yOffset, 0);
//        batch.setProjectionMatrix(matrix4);
//        if (player.getIsDead()){
//            myFont.draw(batch, "YOU'RE DEAD BITCH",500,500);
//        }
//        myFont.draw(batch, "Score \n  " + outputScore,1130,670);
    }

//    public void initEnemies() {
//        enemies.add(new Enemy(700, 450, 40, 40, this));
//        enemies.add(new Enemy(220, 330, 40, 40, this));
//    }

    /*public boolean checkPlayerCollisions() {
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
    }*/

    /*public void checkBulletCollisions() {
        for(Bullet bullet : bullets)
            checkBullet(bullet);
    }*/

    /*public boolean checkBullet(Bullet bullet) {
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
            Rectangle enemyBody = enemy.getBody();
            if(Intersector.intersectRectangles(bulletBody, enemyBody, overlap)) {
                if (bullet.getEnemyBullet() == false){
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    player.setScore(player.getScore() + 100);
                }
                return true;
            }
        }

        Rectangle playerBody = player.getBody();
        if(Intersector.intersectRectangles(bulletBody, playerBody, overlap)) {
            if (bullet.getEnemyBullet() == true){
                bulletsToRemove.add(bullet);
                player.setHealth(player.getHealth() - 1);
                if (player.getHealth() <= 0){
                    player.setIsDead(true);
                }

            }
            return true;
        }

        return false;
    }*/

    /*private void purgeBullets() {
        bullets.removeAll(bulletsToRemove);
        bulletsToRemove.clear();
    }*/

    /*private void purgeEnemies() {
        enemies.removeAll(enemiesToRemove);
        enemiesToRemove.clear();
    }*/

    /*public void spawnBullet(Bullet bullet, Boolean enemyBullet) {
        bullet.setEnemyBullet(enemyBullet);
        bullets.add(bullet);
    }*/

    //Calculates the angle to the player is from the enemy
    /*public float calculateAngleToPlayer(Enemy enemy){
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
        //System.out.println(angle);

        return angle;
    }*/

    //Moves the enemy to a new X and Y coordinates
   /* public void moveEnemy(Enemy enemy, float newX, float newY, float delta){
        if (enemy.getBody().x != newX || enemy.getBody().y != newY){
            if (enemy.getBody().x < newX){
                enemy.getBody().x += enemyMoveSpeed * delta;
            }
            if (enemy.getBody().x > newX){
                enemy.getBody().x -= enemyMoveSpeed * delta;
            }
            if (enemy.getBody().y < newY){
                enemy.getBody().y += enemyMoveSpeed * delta;
            }
            if (enemy.getBody().y > newY){
                enemy.getBody().y -= enemyMoveSpeed * delta;
            }
        }
    }*/
}