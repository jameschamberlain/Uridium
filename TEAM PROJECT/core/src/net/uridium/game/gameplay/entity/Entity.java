package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

import java.io.Serializable;

/**
 * Base Entity Class
 */
public abstract class Entity implements Serializable {
    /**
     * Unique identifier of the entity
     */
    int ID;

    /**
     * Body of the entity, used for collisions
     */
    public Rectangle body;

    /**
     * Position of the entity in the last update, used for collision
     */
    public Vector2 lastPos;

    /**
     * Speed of the entity in each axis
     */
    public Vector2 vel;

    /**
     * File name of the entity's texture
     */
    public String textureFile;

    /**
     * Texture which is rendered to the screen on the client side
     */
    public transient TextureRegion t;

    boolean changed;

    /**
     * Default constructor for Entity
     * @param ID Unique ID of the entity
     * @param body Body of the entity
     * @param vel Initial Speed of the entity
     * @param textureFile String used to access the entity texture on the client side
     */
    public Entity(int ID, Rectangle body, Vector2 vel, String textureFile) {
        this.ID = ID;
        this.body = body;
        this.vel = vel;
        this.textureFile = textureFile;

        lastPos = new Vector2();
        body.getPosition(lastPos);
    }

    /**
     * @param ID The new ID for the entity, avoid using if possible
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return Body of the entity
     */
    public Rectangle getBody() {
        return body;
    }

    /**
     * @param x The new x value for the body of the entity
     */
    public void setX(float x) {
        body.setX(x);
    }

    /**
     * @param y The new y value for the body of the entity
     */
    public void setY(float y) {
        body.setY(y);
    }

    /**
     * @param pos The new position for the body of the entity
     */
    public void setPosition(Vector2 pos) {
        body.setPosition(pos);
        changed = true;
    }

    /**
     * Returns the position of the entity's body, copied to the given Vector2
     * @param pos The vector2 to copy the position of the body to
     * @return The given vector2 with modified position
     */
    public Vector2 getPosition(Vector2 pos) {
        return body.getPosition(pos);
    }

    /**
     * Returns the center of the entity's body, copied to the given Vector2
     * @param center The vector2 to copy the center of the body to
     * @return The given vector2 with modified position
     */
    public Vector2 getCenter(Vector2 center) {
        return body.getCenter(center);
    }

    /**
     * @return Returns the position of the entity last update
     */
    public Vector2 getLastPos() {
        return lastPos;
    }

    /**
     * Sets the velocity of the entity
     * @param x The new x value of the velocity
     * @param y The new y value of the velocity
     */
    public void setVelocity(float x, float y) {
        setVelocity(new Vector2(x, y));
    }

    /**
     * @param vel The new velocity of the entity
     */
    public void setVelocity(Vector2 vel) {
        this.vel = vel;
        changed = true;
    }

    /**
     * Returns the velocity of the entity, copied to the given Vector2
     * @param vel The vector2 to copy the velocity to
     * @return The given vector2 with modified values
     */
    public Vector2 getVelocity(Vector2 vel) {
        vel.x = this.vel.x;
        vel.y = this.vel.y;

        return vel;
    }

    /**
     * Loads the texture for the entity from the {@link Entity#textureFile}, used on the client side only
     */
    public void loadTexture() {
        Gdx.app.postRunnable(() -> t = new TextureRegion(Assets.getTex((textureFile))));
    }

    /**
     * Checks if the entity has been changed (either it's velocity or position has been changed)
     * @return Whether the entity has been changed or not
     */
    public boolean checkChanged() {
        if(changed) {
            changed = false;
            return true;
        }

        return false;
    }

    /**
     * Updates the entity
     * @param delta The delta time (time between this update and the last)
     */
    public void update(float delta) {
        getPosition(lastPos);

        body.x += vel.x * delta;
        body.y += vel.y * delta;

        if(vel.len2() != 0)
            changed = true;
    }

    /**
     * Renders the entity on the screen
     * @param batch SpriteBatch used to render the entity
     */
    public void render(SpriteBatch batch) {
        if(t != null) batch.draw(t, body.getX(), body.getY(), body.getWidth(), body.getHeight());
    }

    /**
     * @return The ID of the entity
     */
    public int getID() {
        return ID;
    }
}