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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Dimensions;
import com.sun.jndi.toolkit.url.Uri;
import net.uridium.game.Uridium;
import net.uridium.game.util.Audio;
import net.uridium.game.util.MyAssetManager;
import net.uridium.game.screen.MenuScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static net.uridium.game.Uridium.*;
import static net.uridium.game.res.Textures.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.*;
import static net.uridium.game.util.Dimensions.BUTTON_HEIGHT;
import static net.uridium.game.util.Dimensions.BUTTON_WIDTH;
import net.uridium.game.util.Audio;

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


        Slider slider = new Slider(0f, 1f, 0.01f, false, mySkin);

        slider.setWidth(250);
        slider.setValue(1f);

        slider.setPosition((GAME_WIDTH - 340) / 2, ((GAME_HEIGHT - 80) / 2) + 75);


        slider.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //slider.getValue();

                Uridium.music.setVolume(slider.getValue());

                super.touchDown(event, x, y, pointer, button);
            }
        });







        Button pauseBtn = new TextButton(" | | ", mySkin, "small");
        pauseBtn.setSize(80, 40);
        pauseBtn.setPosition((GAME_WIDTH - 100)/2 , (GAME_HEIGHT +200 )/2);
        ((TextButton) pauseBtn).getLabel().setFontScale(1.4f);
        // plusBtn.addAction(sequence(alpha(1), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        pauseBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Pause Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Pause");
                Uridium.music.stop();
                super.touchDown(event, x, y, pointer, button);
            }
        });



        Button playBtn = new TextButton(" |> ", mySkin, "small");
        playBtn.setSize(80, 40);
        playBtn.setPosition((GAME_WIDTH - 300)/2 , (GAME_HEIGHT +200 )/2);
        ((TextButton) playBtn).getLabel().setFontScale(1.4f);
        // plusBtn.addAction(sequence(alpha(1), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        playBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Pause Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Play");
                Uridium.music.play();
                super.touchDown(event, x, y, pointer, button);
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
        stage.addActor(pauseBtn);
        stage.addActor(playBtn);
        stage.addActor(slider);
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
