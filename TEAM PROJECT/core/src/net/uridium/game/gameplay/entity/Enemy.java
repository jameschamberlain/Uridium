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

public class Enemy {
    final Rectangle body;

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
}
