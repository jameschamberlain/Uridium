package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.uridium.game.util.Assets;

import static net.uridium.game.util.Dimensions.GAME_WIDTH;

/**
 * The animated background rendered in menus
 */
public class Background {
    /**
     * Background texture
     */
    Texture background;

    /**
     * Foreground texture
     */
    Texture foreground;

    /**
     * Background texture if won game
     */
    Texture background2;

    /**
     * Foreground texture if won game
     */
    Texture foreground2;

    /**
     * Moving fish effect
     */
    ParticleEffect fishEffect;

    /**
     * If true, the alternate victory background is shown
     */
    boolean showAlt = false;

    /**
     * Background constructor
     */
    public Background() {
        background = new Texture(Gdx.files.internal(Assets.SPLASH_BACKGROUND));
        foreground = new Texture(Gdx.files.internal(Assets.SPLASH_FOREGROUND));

        background2 = new Texture(Gdx.files.internal(Assets.SPLASH_BACKGROUND2));
        foreground2 = new Texture(Gdx.files.internal(Assets.SPLASH_FOREGROUND2));

        fishEffect = new ParticleEffect();
        fishEffect.load(Gdx.files.internal("graphics/particle/menu_fish.p"), Gdx.files.internal("graphics/particle"));
        fishEffect.setPosition(GAME_WIDTH / 2, 120);
        fishEffect.start();
    }

    /**
     * @param showAlt New value of show alt
     */
    public void setShowAlt(boolean showAlt) {
        this.showAlt = showAlt;
    }

    /**
     * Updates the background
     * @param delta The time between this update and the last
     */
    public void update(float delta) {
        fishEffect.update(delta);
    }

    /**
     * Renders the background
     * @param batch The SpriteBatch to render the background with
     */
    public void render(SpriteBatch batch) {
        if(!showAlt) {
            batch.draw(background, 0, 0, 1280, 720);
            fishEffect.draw(batch);
            batch.draw(foreground, 0, 0, 1280, 720);
        } else {
            batch.draw(background2, 0, 0, 1280, 720);
            fishEffect.draw(batch);
            batch.draw(foreground2, 0, 0, 1280, 720);
        }
    }
}
