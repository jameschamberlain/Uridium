package net.uridium.game.gameplay.entity.damageable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends DamageableEntity {
    int score = 0;

    public long lastShot = 0;
    private long reloadTime = 350;

    public Player(int ID, Vector2 spawn) {
        this(ID, spawn, 5, 5);
    }

    public Player(int ID, Vector2 spawn, int maxHealth, int health) {
        this(ID, spawn, "penguin_square.png", maxHealth, health);
    }

    public Player(int ID, Vector2 spawn, String textureFile, int maxHealth, int health) {
        super(ID, new Rectangle(spawn.x, spawn.y, 40, 40), new Vector2(), textureFile, maxHealth, health);
    }

    private void shoot(Vector2 bulletSpawn, float shootAngle) {
//        level.spawnBullet(new Bullet(bulletSpawn, shootAngle), false);
//        lastShot = System.currentTimeMillis();
    }

    private boolean canShoot() {
        return System.currentTimeMillis() - lastShot > reloadTime;
    }

    public void addScore(int s) {
        score += s;
    }

    public void setScore(int s){
        score = s;
    }

    public int getScore(){
        return score;
    }

//    @Override
//    public boolean keyDown(int keycode) {
//        Vector2 bulletSpawn = new Vector2();
//
//        if(canShoot()) {
//            switch(keycode) {
//                case Input.Keys.UP:
//                    bulletSpawn = body.getCenter(bulletSpawn).add(0, body.height / 2);
//                    shoot(bulletSpawn, 0);
//                    return true;
//                case Input.Keys.LEFT:
//                    bulletSpawn = body.getCenter(bulletSpawn).sub(body.width / 2, 0);
//                    shoot(bulletSpawn, 270);
//                    return true;
//                case Input.Keys.DOWN:
//                    bulletSpawn = body.getCenter(bulletSpawn).sub(0, body.height / 2);
//                    shoot(bulletSpawn, 180);
//                    return true;
//                case Input.Keys.RIGHT:
//                    bulletSpawn = body.getCenter(bulletSpawn).add(body.width / 2, 0);
//                    shoot(bulletSpawn, 90);
//                    return true;
//            }
//        }
//
//        return super.keyDown(keycode);
//    }
}


