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
import net.uridium.game.server.Server;
import net.uridium.game.util.MyAssetManager;

import java.io.IOException;

import static net.uridium.game.Uridium.setCursor;
import static net.uridium.game.res.Textures.*;
import static net.uridium.game.res.Dimens.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class GameSelectionScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;
    private TextureRegion bg;


    GameSelectionScreen() {
        setCursor(MENU_CURSOR, 0, 0);
        // Setup textures and background.
        Texture bgTexture = new Texture(Gdx.files.internal(BACKGROUND));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        // Setup asset manager.
        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.queueAddSkin();
        myAssetManager.manager.finishLoading();
        mySkin = myAssetManager.manager.get(SKIN);

        // Setup camera.
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        // Setup stage
        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        // Setup game title label.
        Label gameTitle = setupGameTitle();
        // Setup solo button.
        Button soloBtn = setupSoloButton();
        // Setup multiplayer button.
        Button multiplayerButton = setupMultiplayerButton();
        // Setup back button.
        Button backBtn = setupBackButton();

        // Add title and buttons to the screen.
        stage.addActor(gameTitle);
        stage.addActor(soloBtn);
        stage.addActor(multiplayerButton);
        stage.addActor(backBtn);
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
     * Setup the solo button.
     * Enters into solo mode.
     *
     * @return The solo button.
     */
    private Button setupSoloButton() {
        Button soloBtn = new TextButton("S O L O", mySkin);
        soloBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        soloBtn.setPosition(BUTTON_X, BUTTON_Y);
        // Listener for click events.
        soloBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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
        Button multiplayerButton = new TextButton("M U L T I P L A Y E R", mySkin);
        multiplayerButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        multiplayerButton.setPosition(BUTTON_X, BUTTON_Y - BUTTON_GAP);
        // Listener for click events.
        multiplayerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new LobbyScreen());
                super.touchDown(event, x, y, pointer, button);
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
        Button backBtn = new TextButton("B A C K", mySkin);
        backBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        backBtn.setPosition(BUTTON_X, BUTTON_Y - BUTTON_GAP * 2);
        // Listener for click events.
        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new MenuScreen());
                super.touchUp(event, x, y, pointer, button);
            }
        });
        return backBtn;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        //Gdx.gl.glClearColor(1,0,0,0);
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
