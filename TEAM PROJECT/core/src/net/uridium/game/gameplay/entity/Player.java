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
import net.uridium.game.util.UridiumInputProcessor;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Player extends UridiumInputProcessor {
    //这里是身体的矩形
    final Rectangle body;
    Color c = new Color(10673872);
    Color c2 = new Color();
    //移动速度
    float moveSpeed = 300;
    //当前关卡
    Level level;

    public Vector2 lastPos;
    public long lastShot = 0;
    private long reloadTime = 350;


    Texture texture;

    public Player(float x, float y, float width, float height, Level level) {


        lastPos = new Vector2(x, y);
        body = new Rectangle(x, y, width, height);

        this.level = level;
        texture = new Texture(Gdx.files.internal("penguin_square.png"));
    }

    public void update(float delta) {
        body.getPosition(lastPos);

        boolean moved = false;
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D))
            moved = true;
    }


    public Rectangle getBody() {
        return body;
    }

    public float getMoveSpeed(){
        return moveSpeed;
    }

    public Vector2 getLastPos(){
        return  lastPos;
    }

    public void setBody(Vector2 v2){
        body.x = v2.x;
        body.y = v2.y;
    }

    private void shoot(Vector2 bulletSpawn, float shootAngle) {
        level.spawnBullet(new Bullet(bulletSpawn, shootAngle));
        lastShot = System.currentTimeMillis();
    }

    private boolean canShoot() {
        return System.currentTimeMillis() - lastShot > reloadTime;
    }

    public void goToLastXPos() {
        body.x = lastPos.x;
    }

    public void goToLastYPos() {
        body.y = lastPos.y;
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, body.x, body.y, body.width, body.height);
    }

    @Override
    public boolean keyDown(int keycode) {
        //创建了一个(0,0)的Vector
        Vector2 bulletSpawn = new Vector2();

        if(canShoot()) {
            switch(keycode) {
                case Input.Keys.UP:
                    //获得企鹅当前位置的中心点然后加上一半身体的高度，就是企鹅rec的正上方，下面的同理
                    bulletSpawn = body.getCenter(bulletSpawn).add(0, body.height / 2);
                    //有了起始角度和起始坐标就可以发射了
                    shoot(bulletSpawn, 0);
                    return true;
                case Input.Keys.LEFT:
                    bulletSpawn = body.getCenter(bulletSpawn).sub(body.width / 2, 0);
                    shoot(bulletSpawn, 270);
                    return true;
                case Input.Keys.DOWN:
                    bulletSpawn = body.getCenter(bulletSpawn).sub(0, body.height / 2);
                    shoot(bulletSpawn, 180);
                    return true;
                case Input.Keys.RIGHT:
                    bulletSpawn = body.getCenter(bulletSpawn).add(body.width / 2, 0);
                    shoot(bulletSpawn, 90);
                    return true;
            }
        }

        return super.keyDown(keycode);
    }
}


