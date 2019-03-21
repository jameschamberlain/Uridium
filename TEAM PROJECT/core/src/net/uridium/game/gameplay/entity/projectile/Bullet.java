package net.uridium.game.gameplay.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Colors;

import static net.uridium.game.res.Textures.BULLET;

public class Bullet extends Projectile {
    public static final float BULLET_WIDTH = 25.5f;
    public static final float BULLET_HEIGHT = 32.5f;

    float shootAngle;

    float rot;

    public Bullet(int ID, Vector2 spawnPos, float shootAngle, int ownerID) {
        this(ID, spawnPos, shootAngle, 350, BULLET, ownerID);
    }

    public Bullet(int ID, Vector2 spawnPos, float shootAngle, float velocity, int ownerID) {
        this(ID, spawnPos, shootAngle, velocity, BULLET, ownerID);
    }

    public Bullet(int ID, Vector2 spawnPos, float shootAngle, float velocity, String textureFile, int ownerID) {
        super(ID, new Rectangle(spawnPos.x - BULLET_WIDTH / 2, spawnPos.y - BULLET_HEIGHT / 2, BULLET_WIDTH, BULLET_HEIGHT), new Vector2(), textureFile, ownerID);

        vel.x = velocity * (float) Math.cos(Math.toRadians(shootAngle));
        vel.y = velocity * (float) Math.sin(Math.toRadians(shootAngle));
    }

    public Bullet(int ID, Vector2 spawnPos, Vector2 vel, int ownerID) {
        super(ID, new Rectangle(spawnPos.x, spawnPos.y, BULLET_WIDTH, BULLET_HEIGHT), vel, BULLET, ownerID);

        rot = 0;
    }

    @Override
    public void loadTexture() {
        Gdx.app.postRunnable(() -> {
            t = new TextureRegion(Assets.getTex((textureFile)));
        });
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        rot += 720 * delta;
        rot %= 360;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(t != null) batch.draw(t, body.x, body.y, body.width / 2, body.height / 2, body.width, body.height, 1, 1, rot);
    }

}
