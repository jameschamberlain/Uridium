package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.Uridium;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Audio;

import static net.uridium.game.Uridium.setCursor;
import static net.uridium.game.util.Dimensions.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.*;

/**
 * The type Menu screen.
 */
public class MenuScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin skin;
    private Stage stage;

    /**
     * The Background.
     */
    Background background;
    /**
     * The Title font.
     */
    BitmapFont titleFont;
    /**
     * The Gl.
     */
    GlyphLayout gl;

//    Texture bgTexture;
//    TextureRegion bg;

    /**
     * Instantiates a new Menu screen.
     *
     * @param backgroud the backgroud
     */
    public MenuScreen(Background backgroud) {
        setCursor(MENU_CURSOR, 0, 0);
        Audio.getAudio().playTheme();

        this.background = backgroud;
        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");
        gl = new GlyphLayout(titleFont, "URIDIUM");

        skin = Assets.getAssets().getManager().get(SKIN);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);

        // Setup play button.
        Button playBtn = setupPlayButton();

        // Setup the settings button.
        Button settingsBtn = setupSettingsButton();

        // Add title and buttons to the screen.
        stage.addActor(playBtn);
        stage.addActor(settingsBtn);
    }

    /**
     * Setup the play button.
     * Enters into the game selection screen.
     *
     * @return The play button.
     */
    private Button setupPlayButton() {
        Button playBtn = new TextButton("PLAY", skin);
        playBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        playBtn.setPosition((GAME_WIDTH - BUTTON_WIDTH) / 2, (GAME_HEIGHT - BUTTON_HEIGHT) / 2);
        ((TextButton.TextButtonStyle) playBtn.getStyle()).fontColor = Color.WHITE;
        // Listener for click events.
        playBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                getUSMInstance().push(new GameSelectionScreen(background));
                super.touchDown(event, x, y, pointer, button);
            }
        });
        return playBtn;
    }

    /**
     * Setup the settings button.
     * Enters into the settings screen.
     *
     * @return The settings button.
     */
    private Button setupSettingsButton() {
        Button settingsBtn = new TextButton("SETTINGS", skin);
        settingsBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsBtn.setPosition((GAME_WIDTH - BUTTON_WIDTH) / 2, (GAME_HEIGHT - BUTTON_HEIGHT) / 2 - 10 - BUTTON_HEIGHT);
        // Listener for click events.
        settingsBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                getUSMInstance().push(new SettingsScreen(background));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        return settingsBtn;
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
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        background.render(batch);

        titleFont.draw(batch, "URIDIUM", (GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT * 3 / 4) + gl.height / 2);

        batch.end();

        stage.act();
        stage.draw();
    }

    /**
     * Dispose.
     */
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}