package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.util.MyAssetManager;

import static net.uridium.game.res.Dimens.*;
import static net.uridium.game.res.Textures.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class NewSplashScreen extends UridiumScreen {

    long startTime;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;

    Texture bgTexture;
    TextureRegion bg;


    public NewSplashScreen() {
        startTime = TimeUtils.millis();

        bgTexture = new Texture(Gdx.files.internal(SPLASH_SCREEN));
        bg = new TextureRegion(bgTexture);
        //bg.setRegion(0, 0, 300, 50);

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

        // Add title and buttons to the screen.
        stage.addActor(gameTitle);


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

    @Override
    public void init() {
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        if(!(TimeUtils.millis() - startTime >= 7000)) {
            // 10 seconds haven't passed yet
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            // Creates a white background
            Gdx.gl.glClearColor(1f, 1f, 1f, 1);
            batch.draw(bg, 0, 0, 1280, 720);
            // Draw your animation here
            batch.end();
            stage.act();
            stage.draw();
        } else {
            getUSMInstance().push(new MenuScreen());
        }
    }

    public void dispose() {
        mySkin.dispose();
        stage.dispose();
    }
}
