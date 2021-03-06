package net.uridium.game.gameplay.entity.damageable.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

/**
 * Pink fish enemy, appears throughout the game
 */
public class PinkFish extends Enemy {

    /**
     * PinkFish constructor
     * @param ID The entity id
     * @param pos The initial position
     */
    public PinkFish(int ID, Vector2 pos) {
        super(ID, new Rectangle(pos.x, pos.y, 48, 36), new Vector2(0, 0), 1, 1, 180);
    }

    @Override
    public void loadAnim() {
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(Assets.getTex(("graphics/entity/enemy/fishPink.png")));
        frames[1] = new TextureRegion(Assets.getTex(("graphics/entity/enemy/fishPink_swim.png")));

        anim = new Animation<>(0.2f, frames);
    }
}