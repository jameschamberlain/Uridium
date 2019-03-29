package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
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
import net.uridium.game.server.Server;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Audio;
import net.uridium.game.util.Dimensions;

import java.io.IOException;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;
import static net.uridium.game.Uridium.setCursor;
import static net.uridium.game.util.Dimensions.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.*;

/**
 * The type Game selection screen.
 */
public class GameSelectionScreen extends UridiumScreen {

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


    /**
     * Instantiates a new Game selection screen.
     *
     * @param background the background
     */
    public GameSelectionScreen(Background background) {
        Texture bgTexture = Assets.getTex((BACKGROUND));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        skin = Assets.getAssets().getManager().get(SKIN);

        this.background = background;
        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");
        gl = new GlyphLayout(titleFont, "URIDIUM");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        // Setup stage
        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);

        // Setup solo button.
        Button soloBtn = setupSoloButton();

        // Setup multiplayer button.
        Button multiplayerButton = setupMultiplayerButton();

        // Setup back button.
        Button backBtn = setupBackButton();

        stage.addActor(soloBtn);
        stage.addActor(multiplayerButton);
        stage.addActor(backBtn);
    }

    /**
     * Setup the solo button.
     * Enters into solo mode.
     *
     * @return The solo button.
     */
    private Button setupSoloButton() {
        Button soloBtn = new TextButton("SOLO", skin);
        soloBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        soloBtn.setPosition(Dimensions.GAME_WIDTH / 2 - BUTTON_WIDTH - 5, (Dimensions.GAME_HEIGHT - BUTTON_HEIGHT) / 2);
        // Listener for click events.
        soloBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                try {
                    new Server();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                getUSMInstance().push(new GameScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });
        return soloBtn;
    }

    /**
     * Setup the multiplayer button.
     * Enters into the multiplayer screen.
     *
     * @return The multiplayer button.
     */
    private Button setupMultiplayerButton() {
        Button multiplayerButton = new TextButton("MULTIPLAYER", skin);
        multiplayerButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        multiplayerButton.setPosition(Dimensions.GAME_WIDTH / 2 + 5, (Dimensions.GAME_HEIGHT - BUTTON_HEIGHT) / 2);
        // Listener for click events.
        multiplayerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                getUSMInstance().push(new LobbyScreen(background));
            }
        });
        return multiplayerButton;
    }

    /**
     * Setup the back button.
     * Returns the user to the game selection screen.
     *
     * @return The back button.
     */
    private Button setupBackButton() {
        Button backBtn = new TextButton("back", skin);
        backBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        backBtn.setPosition((Dimensions.GAME_WIDTH - BUTTON_WIDTH) / 2, (Dimensions.GAME_HEIGHT - BUTTON_HEIGHT) / 2 - 10 - BUTTON_HEIGHT);
        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                getUSMInstance().push(new MenuScreen(background));
            }
        });
        return backBtn;
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
        //Gdx.gl.glClearColor(1,0,0,0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        background.render(batch);
        titleFont.draw(batch, "URIDIUM", (Dimensions.GAME_WIDTH - gl.width) / 2, (Dimensions.GAME_HEIGHT * 3 / 4) + gl.height / 2);

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
