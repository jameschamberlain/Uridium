package net.uridium.game.gameplay.entity;

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
import net.uridium.game.util.Colors;

public class Bullet {
    public static final float BULLET_WIDTH = 25;
    public static final float BULLET_HEIGHT = 25;

    Rectangle body;
    TextureRegion texture;

    float shootAngle;
    double velocity = 450;
    double xVel;//asdasdasdqe
    double yVel;

    float rot;

    public Bullet(Vector2 spawnPos, float shootAngle) {
        body = new Rectangle(spawnPos.x - BULLET_WIDTH / 2, spawnPos.y - BULLET_HEIGHT / 2, BULLET_WIDTH, BULLET_HEIGHT);

        this.shootAngle = shootAngle;

        xVel = velocity * Math.sin(Math.toRadians(shootAngle));
        yVel = velocity * Math.cos(Math.toRadians(shootAngle));

        texture = new TextureRegion(new Texture(Gdx.files.internal("penguin.png")));
        rot = 0;
    }

    public Rectangle getBody() {
        return body;
    }

    public void update(float delta) {
        body.x += xVel * delta;
        body.y += yVel * delta;
        rot += 540 * delta;
        rot %= 360;
    }

    public void render(SpriteBatch batch) {
//        batch.draw(texture, body.x, body.y ,body.width, body.height);
        batch.draw(texture, body.x, body.y, body.width / 2, body.height / 2, body.width, body.height, 1, 1, rot);
    }

}
