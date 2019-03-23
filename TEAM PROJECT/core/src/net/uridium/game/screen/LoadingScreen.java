package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Audio;

import static net.uridium.game.util.Dimensions.GAME_HEIGHT;
import static net.uridium.game.util.Dimensions.GAME_WIDTH;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.BACKGROUND;

public class LoadingScreen extends UridiumScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;

    Background background;

    BitmapFont font;
    BitmapFont titleFont;
    GlyphLayout gl;

    public LoadingScreen() {
        Assets.getAssets().loadAssets();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("graphics/ui/font/Big_Bottom_Typeface_Normal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 22;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);

        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        background = new Background();

        Audio.getAudioInstance().libPlayLoop("audio\\background.wav");

    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        background.update(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);

        if (!Assets.getAssets().getManager().update()) {
            int progress = Math.round(Assets.getAssets().getManager().getProgress() * 100);

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            background.render(batch);

            String text = progress + "%";
            gl = new GlyphLayout(font, text);
            font.draw(batch, text, (GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT + gl.height) / 2);

            text = "URIDIUM";
            gl = new GlyphLayout(titleFont, text);
            titleFont.draw(batch, text, (GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT * 3 / 4) + gl.height / 2);

            batch.end();
        } else {
            getUSMInstance().push(new MenuScreen(background));
        }
    }

    public void dispose() {

    }
}
