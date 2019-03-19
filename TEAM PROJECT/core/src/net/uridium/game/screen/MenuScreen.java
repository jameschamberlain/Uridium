package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import net.uridium.game.util.MyAssetManager;

import static net.uridium.game.Uridium.setCursor;
import static net.uridium.game.res.Textures.*;
import static net.uridium.game.res.Dimens.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class MenuScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;

    Texture bgTexture;
    TextureRegion bg;


    public MenuScreen() {
        setCursor(MENU_CURSOR, 0, 0);

        bgTexture = new Texture(Gdx.files.internal(BACKGROUND));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.queueAddSkin();
        myAssetManager.manager.finishLoading();
        mySkin = myAssetManager.manager.get(SKIN);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        // Setup game title label.
        Label gameTitle = setupGameTitle();
        // Setup play button.
        Button playBtn = setupPlayButton();
        // Setup the settings button.
        Button settingsBtn = setupSettingsButton();
        // Setup the exit button.
        Button exitBtn = setupExitButton();

        // Add title and buttons to the screen.
        stage.addActor(gameTitle);
        stage.addActor(playBtn);
        stage.addActor(settingsBtn);
        stage.addActor(exitBtn);
    }

    /**
     * Setup the game title label.
     *
     * @return The game title label.
     */
    private Label setupGameTitle() {
        Label gameTitle = new Label("U R I D I U M", mySkin, "title");
        gameTitle.setSize(TITLE_WIDTH, TITLE_HEIGHT);
        gameTitle.setPosition(TITLE_X, TITLE_Y);
        gameTitle.setFontScale(TITLE_FONT_SCALE);
        gameTitle.setAlignment(Align.center);
        return gameTitle;
    }

    /**
     * Setup the play button.
     * Enters into the game selection screen.
     *
     * @return The play button.
     */
    private Button setupPlayButton() {
        Button playBtn = new TextButton("P L A Y", mySkin);
        playBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        playBtn.setPosition(BUTTON_X, BUTTON_Y);
        // Listener for click events.
        playBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new GameSelectionScreen());
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
        Button settingsBtn = new TextButton("S E T T I N G S", mySkin);
        settingsBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsBtn.setPosition(BUTTON_X, BUTTON_Y - BUTTON_GAP);
        // Listener for click events.
        settingsBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new SettingsScreen());
                super.touchUp(event, x, y, pointer, button);
            }
        });
        return settingsBtn;
    }


    /**
     * Setup the exits button.
     * Exits the game.
     *
     * @return The exit button.
     */
    private Button setupExitButton() {
        Button exitBtn = new TextButton("E X I T", mySkin);
        exitBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitBtn.setPosition(BUTTON_X, BUTTON_Y - BUTTON_GAP * 2);
        // Listener for click events.
        exitBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        return exitBtn;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bg, 0, 0, GAME_WIDTH, GAME_WIDTH);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void dispose() {
        mySkin.dispose();
        stage.dispose();
    }
}
