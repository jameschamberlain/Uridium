package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    final Rectangle body;
    Color c = new Color(10673872);
    Color c2 = new Color();

    float moveSpeed = 300;

    public Vector2 lastPos;
    public long lastShot = 0;
    private long reloadTime = 200;

    public Player(float x, float y, float width, float height) {
        lastPos = new Vector2(x, y);
        body = new Rectangle(x, y, width, height);
    }

    public Rectangle getBody() {
        return body;
    }

    public void shoot() {
        lastShot = System.currentTimeMillis();
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShot > reloadTime;
    }

    public Vector2 getCenter() {
        return new Vector2(body.x + body.width / 2, body.y + body.height / 2);
    }

    public void goToLastXPos() {
        body.x = lastPos.x;
    }

    public void goToLastYPos() {
        body.y = lastPos.y;
    }

    public void update(float delta) {
        body.getPosition(lastPos);

        boolean moved = false;
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D))
            moved = true;

        if(moved) System.out.println(body.y);

        if(Gdx.input.isKeyPressed(Input.Keys.W))
            body.y += moveSpeed * delta;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            body.x -= moveSpeed * delta;
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            body.y -= moveSpeed * delta;
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            body.x += moveSpeed * delta;

        if(moved) {
            System.out.println(body.y);
            System.out.println("--");
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(c2);
        shapeRenderer.rect(body.x, body.y, body.width, body.height);
        shapeRenderer.setColor(c);
        shapeRenderer.rect(body.x + 4, body.y + 4, body.width - 8, body.height - 8);
    }
}
