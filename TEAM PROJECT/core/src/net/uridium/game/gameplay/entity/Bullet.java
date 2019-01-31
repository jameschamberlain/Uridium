package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Colors;

public class Bullet {
    public static final float BULLET_WIDTH = 15;
    public static final float BULLET_HEIGHT = 15;

    Rectangle body;

    float shootAngle;
    double velocity = 450;
    double xVel;
    double yVel;

    public Bullet(Vector2 spawnPos, float shootAngle) {
        body = new Rectangle(spawnPos.x - BULLET_WIDTH / 2, spawnPos.y - BULLET_HEIGHT / 2, BULLET_WIDTH, BULLET_HEIGHT);

        this.shootAngle = shootAngle;

        xVel = velocity * Math.sin(Math.toRadians(shootAngle));
        yVel = velocity * Math.cos(Math.toRadians(shootAngle));
    }

    public Rectangle getBody() {
        return body;
    }

    public void update(float delta) {
        body.x += xVel * delta;
        body.y += yVel * delta;
    }

    public void render(ShapeRenderer shapeRenderer) {
        Vector2 center = new Vector2();

        shapeRenderer.setColor(Colors.BULLET_OUTLINE);
        shapeRenderer.rect(body.x, body.y, body.width / 2, body.height / 2, body.width, body.height, 1, 1, shootAngle);
        shapeRenderer.setColor(Colors.BULLET_MAIN);
        shapeRenderer.rect(body.x + body.width / 4, body.y + body.height / 4, body.width / 2, body.height / 2);
    }

}
