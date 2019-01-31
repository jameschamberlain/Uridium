package net.uridium.game.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.gameplay.entity.Bullet;
import net.uridium.game.gameplay.entity.Player;
import net.uridium.game.gameplay.entity.Enemy;
import net.uridium.game.util.Colors;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Level {
    Rectangle[] walls;
    Rectangle[] obstacles;
    Rectangle[] doors;

    ArrayList<Bullet> bullets;
    ArrayList<Bullet> bulletsToRemove;
    ArrayList<Rectangle> enemies;
    ArrayList<Rectangle> enemiesToRemove;

    public Level() {
        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesToRemove = new ArrayList<>();
    }

    public void init() {
        initWalls();
        initDoors();
        initObstacles();
        initEnemies();
    }

    public void initWalls() {
        walls = new Rectangle[6];
        walls[0] = new Rectangle(40, 40, 60, 620);
        walls[1] = new Rectangle(1180, 40, 60, 280);
        walls[2] = new Rectangle(1180, 400, 60, 280);
        walls[3] = new Rectangle(40, 40, 1200, 60);
        walls[4] = new Rectangle(40, 620, 560, 60);
        walls[5] = new Rectangle(680, 620, 560, 60);
    }

    public void initDoors() {
        doors = new Rectangle[2];
        doors[0] = new Rectangle(605, 640, 70, 70);
        doors[1] = new Rectangle(1200, 325, 70, 70);
    }

    public void initObstacles() {
        obstacles = new Rectangle[2];
        obstacles[0] = new Rectangle(180, 110, 70, 70);
        obstacles[1] = new Rectangle(450, 400, 70, 70);
    }

    public void initEnemies() {
        enemies.add(new Rectangle(180, 250, 40, 40));
        enemies.add(new Rectangle(220, 330, 40, 40));
    }

    public boolean checkPlayerCollisions(Player player) {
        Rectangle playerBody = player.getBody();
        Rectangle overlap = new Rectangle();

        for(Rectangle door : doors) {
            if (Intersector.intersectRectangles(playerBody, door, overlap)) {

            }
        }

        for(Rectangle wall : walls) {
            if(Intersector.intersectRectangles(playerBody, wall, overlap)) {
                Rectangle playerBodyOldX = new Rectangle(player.lastPos.x, playerBody.y, playerBody.width, playerBody.height);

                if(!overlap.overlaps(playerBodyOldX))
                    player.goToLastXPos();
                else
                    player.goToLastYPos();
            }

            continue;
        }

        for(Rectangle obstacle : obstacles) {
            if(Intersector.intersectRectangles(playerBody, obstacle, overlap)) {
                Rectangle playerBodyOldX = new Rectangle(player.lastPos.x, playerBody.y, playerBody.width, playerBody.height);

                if(!overlap.overlaps(playerBodyOldX))
                    player.goToLastXPos();
                else
                    player.goToLastYPos();
            }

            continue;
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

        for(Rectangle wall : walls) {
            if (Intersector.intersectRectangles(bulletBody, wall, overlap)) {
                bulletsToRemove.add(bullet);
                return true;
            }
        }

        for(Rectangle obstacle : obstacles) {
            if(Intersector.intersectRectangles(bulletBody, obstacle, overlap)) {
                bulletsToRemove.add(bullet);
                return true;
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

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Colors.LEVEL_BG);
        shapeRenderer.rect(40, 40, 1200, 640);

        for(Rectangle wall : walls) {
            shapeRenderer.setColor(Colors.WALL_OUTLINE);
            shapeRenderer.rect(wall.x, wall.y, wall.width, wall.height);
            shapeRenderer.setColor(Colors.WALL_MAIN);
            shapeRenderer.rect(wall.x + 5, wall.y + 5, wall.width - 10, wall.height - 10);
        }

        for(Rectangle door : doors) {
            shapeRenderer.setColor(Colors.DOOR_OUTLINE);
            shapeRenderer.rect(door.x, door.y, door.width, door.height);
            shapeRenderer.setColor(Colors.DOOR_MAIN);
            shapeRenderer.rect(door.x + 4, door.y + 4, door.width - 8, door.height - 8);
        }

        for(Rectangle obstacle : obstacles) {
            shapeRenderer.setColor(Colors.OBST_OUTLINE);
            shapeRenderer.rect(obstacle.x - 4, obstacle.y - 4, obstacle.width + 8, obstacle.height + 8);
            shapeRenderer.setColor(Colors.OBST_MAIN);
            shapeRenderer.rect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        for(Bullet bullet : bullets) {
            bullet.render(shapeRenderer);
        }

        for (Rectangle enemy : enemies) {
            shapeRenderer.setColor(Color.OLIVE);
            shapeRenderer.rect(enemy.x, enemy.y, enemy.width, enemy.height);
        }
    }
}