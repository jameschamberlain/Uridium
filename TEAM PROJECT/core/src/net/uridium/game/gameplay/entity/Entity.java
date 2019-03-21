package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    int ID;

    public Rectangle body;
    public Vector2 lastPos;
    public Vector2 vel;
    public String textureFile;
    public transient TextureRegion t;

    boolean changed;

    public Entity(int ID, Rectangle body, Vector2 vel, String textureFile) {
        this.ID = ID;
        this.body = body;
        this.vel = vel;
        this.textureFile = textureFile;

        lastPos = new Vector2();
        body.getPosition(lastPos);
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Rectangle getBody() {
        return body;
    }

    public void setX(float x) {
        body.setX(x);
    }

    public void setY(float y) {
        body.setY(y);
    }

    public void setPosition(Vector2 pos) {
        body.setPosition(pos);
        changed = true;
    }

    public Vector2 getPosition(Vector2 pos) {
        return body.getPosition(pos);
    }

    public Vector2 getCenter(Vector2 center) {
        return body.getCenter(center);
    }

    public Vector2 getLastPos() {
        return lastPos;
    }

    public void setVelocity(float x, float y) {
        setVelocity(new Vector2(x, y));
    }

    public void setVelocity(Vector2 vel) {
        this.vel = vel;
        changed = true;
    }

    public Vector2 getVelocity(Vector2 vel) {
        vel.x = this.vel.x;
        vel.y = this.vel.y;

        return vel;
    }

    public void loadTexture() {
        Gdx.app.postRunnable(() -> t = new TextureRegion(Assets.getTex((textureFile))));
    }

    public boolean checkChanged() {
        if(changed) {
            changed = false;
            return true;
        }

        return false;
    }

    public void update(float delta) {
        getPosition(lastPos);

        body.x += vel.x * delta;
        body.y += vel.y * delta;

        if(vel.len2() != 0)
            changed = true;
    }

    public void render(SpriteBatch batch) {
        if(t != null) batch.draw(t, body.getX(), body.getY(), body.getWidth(), body.getHeight());
    }

    public int getID() {
        return ID;
    }
}