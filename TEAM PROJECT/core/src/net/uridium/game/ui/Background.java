package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.uridium.game.util.Assets;

import static net.uridium.game.util.Dimensions.GAME_WIDTH;

public class Background {
    Texture background;
    Texture foreground;
    ParticleEffect fishEffect;

    public Background() {
        background = new Texture(Gdx.files.internal(Assets.SPLASH_BACKGROUND));
        foreground = new Texture(Gdx.files.internal(Assets.SPLASH_FOREGROUND));
        fishEffect = new ParticleEffect();
        fishEffect.load(Gdx.files.internal("graphics/particle/menu_fish.p"), Gdx.files.internal("graphics/particle"));
        fishEffect.setPosition(GAME_WIDTH / 2, 120);
        fishEffect.start();
    }

    public void update(float delta) {
        fishEffect.update(delta);
    }

    public void render(SpriteBatch batch) {
        batch.draw(background, 0, 0, 1280, 720);
        fishEffect.draw(batch);
        batch.draw(foreground, 0, 0, 1280, 720);
    }
}
