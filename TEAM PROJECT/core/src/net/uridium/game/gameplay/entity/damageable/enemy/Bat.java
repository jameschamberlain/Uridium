package net.uridium.game.gameplay.entity.damageable.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

/**
 * Bat enemy, appears in boss levels as a special attack
 */
public class Bat extends Enemy {

    /**
     * Bat Constructor, uses the given angle to calculate the x and y velocity
     * @param ID The entity id
     * @param pos The initial position
     * @param angle The angle at which the bat is moving
     */
    public Bat(int ID, Vector2 pos, float angle) {
        super(ID, new Rectangle(pos.x, pos.y, 52.8f, 25.2f), new Vector2(0, 0), 2, 2, 195);

        float xVel = -speed * (float) Math.cos(angle);
        float yVel = -speed * (float) Math.sin(angle);

        setVelocity(xVel, yVel);
    }

    @Override
    public void loadAnim() {
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(Assets.getTex(("graphics/entity/enemy/bat.png")));
        frames[1] = new TextureRegion(Assets.getTex(("graphics/entity/enemy/bat_fly.png")));

        anim = new Animation<TextureRegion>(0.12f, frames);
    }
}