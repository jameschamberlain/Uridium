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
    Boolean enemyBullet = false;

    float shootAngle;
    double velocity = 450;
    double xVel;
    double yVel;

    float rot;

    public Bullet(Vector2 spawnPos, float shootAngle) {
        //就是发射子弹的位置（比如按上就是头顶的位置）- 子弹的一半高度 = 中心点位置 我怀疑他X写错了
        body = new Rectangle(spawnPos.x - BULLET_WIDTH / 2, spawnPos.y - BULLET_HEIGHT / 2, BULLET_WIDTH, BULLET_HEIGHT);

        this.shootAngle = shootAngle;
        //这是给鼠标用的
        xVel = velocity * Math.sin(Math.toRadians(shootAngle));
        yVel = velocity * Math.cos(Math.toRadians(shootAngle));
        //给子弹做渲染的
        texture = new TextureRegion(new Texture(Gdx.files.internal("penguin.png")));
        rot = 0;
    }

    public Rectangle getBody() {
        return body;
    }

    public Boolean getEnemyBullet(){
        return enemyBullet;
    }

    public void setEnemyBullet(Boolean enemyBullet){
        this.enemyBullet = enemyBullet;
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
