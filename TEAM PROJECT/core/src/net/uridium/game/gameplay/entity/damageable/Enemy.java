package net.uridium.game.gameplay.entity.damageable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Level;
import net.uridium.game.gameplay.entity.projectile.Bullet;

public class Enemy extends DamageableEntity {
    public long lastShot = 0;
    private long reloadTime = 1000;

    public Enemy(int ID, Rectangle body, int maxHealth, int health) {
        super(ID, body, new Vector2(0, 0), "chicken.png", maxHealth, health);
    }

    public Enemy(int ID, Rectangle body, Vector2 vel, String textureFile, int maxHealth, int health) {
        super(ID, body, vel, textureFile, maxHealth, health);
    }

    public void shoot(float shootAngle) {
//        level.spawnBullet(new Bullet(getCenter(), shootAngle), true);
//        lastShot = System.currentTimeMillis();
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShot > reloadTime;
    }
}
