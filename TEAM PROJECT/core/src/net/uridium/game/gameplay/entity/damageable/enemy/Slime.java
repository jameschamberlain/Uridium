package net.uridium.game.gameplay.entity.damageable.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

/**
 * Slime enemy, appears in the boss level
 */
public class Slime extends Enemy {

    /**
     * Slime constructor, uses the given angle to calculate the x and y velocity
     * @param ID The entity id
     * @param pos The initial position
     * @param angle The angle the slime is moving
     */
    public Slime(int ID, Vector2 pos, float angle) {
        super(ID, new Rectangle(pos.x, pos.y, 42.75f, 15f), new Vector2(0, 0), 1, 2, 225);

        this.angle = (float) Math.toDegrees(angle);
        float xVel = -speed * (float) Math.cos(angle);
        float yVel = -speed * (float) Math.sin(angle);

        setVelocity(xVel, yVel);
    }

    @Override
    public void loadAnim() {
        TextureRegion[] frames = new TextureRegion[1];
        frames[0] = new TextureRegion(Assets.getTex(("graphics/entity/enemy/slimeBlue_squashed.png")));

        anim = new Animation<>(0.15f, frames);
    }
}