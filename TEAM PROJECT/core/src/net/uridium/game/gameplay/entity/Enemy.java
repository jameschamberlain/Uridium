package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    final Rectangle body;


    public Vector2 lastPos;
    public long lastShot = 0;
    private long reloadTime = 200;

    public Enemy(float x, float y, float width, float height) {
        lastPos = new Vector2(x, y);
        body = new Rectangle(x, y, width, height);
    }

    public Rectangle getBody() {
        return body;
    }

    public Vector2 getCenter() {
        return new Vector2(body.x + body.width / 2, body.y + body.height / 2);
    }



    public void update(float delta) {
        body.getPosition(lastPos);


    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(body.x, body.y, body.width, body.height);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(body.x + 4, body.y + 4, body.width - 8, body.height - 8);
    }
}
