package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Audio;
import net.uridium.game.util.Dimensions;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static net.uridium.game.Uridium.*;
import static net.uridium.game.res.Textures.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.*;
import static net.uridium.game.util.Dimensions.BUTTON_HEIGHT;
import static net.uridium.game.util.Dimensions.BUTTON_WIDTH;

public class AudioScreen extends UridiumScreen {


    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin skin;
    private Stage stage;

    Background background;

    public AudioScreen(Background background) {
        setCursor(MENU_CURSOR, 0, 0);

        this.background = background;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        skin = Assets.getAssets().getManager().get(SKIN);
        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);

        Button plusBtn = new TextButton("  +  ", skin);
        plusBtn.setSize(80, 40);
        plusBtn.setPosition((GAME_WIDTH + 100)/2 , (GAME_HEIGHT +50 )/2);
        plusBtn.addListener(new InputListener() {public void clicked (InputEvent event, float x, float y) {
                Audio.getAudioInstance().increaseMasterVolume();
            }});


        Button minusBtn = new TextButton("  -  ", skin);
        minusBtn.setSize(80, 40);
        minusBtn.setPosition((GAME_WIDTH - 100)/2 , (GAME_HEIGHT +50 )/2);
        minusBtn.addListener(new InputListener() {
            public void clicked (InputEvent event, float x, float y) {
                Audio.getAudioInstance().lowerMasterVolume();
            }
        });

        Button pauseBtn = new TextButton(" ||  ", skin);
        pauseBtn.setSize(80, 40);
        pauseBtn.setPosition((GAME_WIDTH - 100)/2 , (GAME_HEIGHT +200 )/2);
        pauseBtn.addListener(new InputListener() {
            public void clicked (InputEvent event, float x, float y) {
                Audio.getAudioInstance().muteMasterVolume();
            }
        });

        Button backBtn = new TextButton("back", skin);
        backBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        backBtn.setPosition((Dimensions.GAME_WIDTH - BUTTON_WIDTH) / 2, (Dimensions.GAME_HEIGHT - BUTTON_HEIGHT) / 2 - 10 - BUTTON_HEIGHT);
        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Back Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Back");
                getUSMInstance().push(new SettingsScreen(background));
                super.touchDown(event, x, y, pointer, button);
            }
        });

        stage.addActor(backBtn);
        stage.addActor(plusBtn);
        stage.addActor(minusBtn);
        stage.addActor(pauseBtn);
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
        Gdx.gl.glClearColor(1,0,0,0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.render(batch);
        batch.end();

        stage.act();
        stage.draw();
    }
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
