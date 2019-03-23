package net.uridium.game.gameplay.entity.damageable.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Spider extends Enemy {
    public Spider(int ID, Vector2 pos) {
        super(ID, new Rectangle(pos.x, pos.y, 48, 36), new Vector2(0, 0), 1, 1, 240);
    }

    @Override
    public void loadAnim() {
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(new Texture(Gdx.files.internal("spider_walk1.png")));
        frames[1] = new TextureRegion(new Texture(Gdx.files.internal("spider_walk2.png")));

        anim = new Animation<TextureRegion>(0.12f, frames);
    }
}