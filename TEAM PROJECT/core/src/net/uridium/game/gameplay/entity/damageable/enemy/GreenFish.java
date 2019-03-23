package net.uridium.game.gameplay.entity.damageable.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GreenFish extends Enemy {
    public GreenFish(int ID, Vector2 pos) {
        super(ID, new Rectangle(pos.x, pos.y, 48, 36), new Vector2(0, 0), 1, 1, 180);
    }

    @Override
    public void loadAnim() {
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(new Texture(Gdx.files.internal("fishGreen.png")));
        frames[1] = new TextureRegion(new Texture(Gdx.files.internal("fishGreen_swim.png")));

        anim = new Animation<TextureRegion>(0.2f, frames);
    }
}