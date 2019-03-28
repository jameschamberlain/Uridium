package net.uridium.game.gameplay.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

import static net.uridium.game.res.Textures.BULLET;

/**
 * Bullet is the projectile fired by the player
 */
public class Bullet extends Projectile {
    /**
     * Width of bullets
     */
    public static final float BULLET_WIDTH = 25.5f;

    /**
     * Height of bullets
     */
    public static final float BULLET_HEIGHT = 32.5f;

    /**
     * The angle the bullet is travelling
     */
    float shootAngle;

    /**
     * Rotation used for rendering bullets at angles
     */
    float rot;

    /**
     * Stripped down version of the bullet constructor, with a default velocity and texture file used, see {@link Bullet#Bullet(int, Vector2, float, float, String, int)}
     */
    public Bullet(int ID, Vector2 spawnPos, float shootAngle, int ownerID) {
        this(ID, spawnPos, shootAngle, 350, BULLET, ownerID);
    }

    /**
     * Constructor for bullet
     * @param ID Entity ID of the bullet
     * @param spawnPos Initial location of the bullet
     * @param shootAngle The angle at which the bullet is travelling
     * @param velocity The velocity of the bullet
     * @param textureFile File name of the entity's texture
     * @param ownerID The entity id of the entity which fired the projectile
     * @param textureFile File name of the entity's texture
     */
    public Bullet(int ID, Vector2 spawnPos, float shootAngle, float velocity, String textureFile, int ownerID) {
        super(ID, new Rectangle(spawnPos.x - BULLET_WIDTH / 2, spawnPos.y - BULLET_HEIGHT / 2, BULLET_WIDTH, BULLET_HEIGHT), new Vector2(), textureFile, ownerID);

        vel.x = velocity * (float) Math.cos(Math.toRadians(shootAngle));
        vel.y = velocity * (float) Math.sin(Math.toRadians(shootAngle));
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
