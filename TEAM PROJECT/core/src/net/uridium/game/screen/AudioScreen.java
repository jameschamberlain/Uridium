package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.Uridium;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Dimensions;

import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.MENU_CURSOR;
import static net.uridium.game.util.Assets.SKIN;
import static net.uridium.game.util.Dimensions.BUTTON_HEIGHT;
import static net.uridium.game.util.Dimensions.BUTTON_WIDTH;
import static net.uridium.game.util.Dimensions.GAME_HEIGHT;
import static net.uridium.game.util.Dimensions.GAME_WIDTH;

public class AudioScreen extends UridiumScreen {


    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin skin;
    private Stage stage;

    Background background;
    BitmapFont titleFont;
    GlyphLayout gl;

    public AudioScreen(Background background) {
        setCursor(MENU_CURSOR, 0, 0);

        this.background = background;
        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");
        gl = new GlyphLayout(titleFont, "URIDIUM");

        skin = Assets.getAssets().getManager().get(SKIN);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        Slider slider = new Slider(0f, 1f, 0.01f, false, skin);

        slider.setWidth(250);
        slider.setValue(1f);

        slider.setPosition((GAME_WIDTH - 340) / 2, ((GAME_HEIGHT - 80) / 2) +50);


        slider.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                Uridium.music.setVolume(slider.getValue());

                super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });







        Button pauseBtn = new TextButton(" PAUSE", skin);
        pauseBtn.setSize(250, 80);
        pauseBtn.setPosition((GAME_WIDTH - 100)/2 , (GAME_HEIGHT +200 )/2 - 50);
        ((TextButton) pauseBtn).getLabel().setFontScale(1.4f);
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



        Button playBtn = new TextButton(" PLAY ", skin);
        playBtn.setSize(250, 80);
        playBtn.setPosition((GAME_WIDTH - 300)/2 - 100 , (GAME_HEIGHT +200 )/2 - 50);
        ((TextButton) playBtn).getLabel().setFontScale(1.4f);
        // plusBtn.addAction(sequence(alpha(1), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        playBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Uridium.music.play();
                super.touchDown(event, x, y, pointer, button);
            }
        });






        Button backBtn = new TextButton("BACK", skin);
        backBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        backBtn.setPosition((Dimensions.GAME_WIDTH - BUTTON_WIDTH) / 2, (Dimensions.GAME_HEIGHT - BUTTON_HEIGHT) / 2 - 10 - BUTTON_HEIGHT);
        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
