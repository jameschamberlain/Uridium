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
import com.sun.jndi.toolkit.url.Uri;
import net.uridium.game.Uridium;
import net.uridium.game.util.Audio;
import net.uridium.game.util.MyAssetManager;
import net.uridium.game.screen.MenuScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import net.uridium.game.util.Audio;

public class AudioScreen extends SettingsScreen {


    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;

    Texture bgTexture;
    TextureRegion bg;

    public AudioScreen() {
        setCursor("cursor.png", 0, 0);

        bgTexture = new Texture(Gdx.files.internal("ground_01.png"));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);


        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.queueAddSkin();
        myAssetManager.manager.finishLoading();
        mySkin = myAssetManager.manager.get("skin/glassy-ui.json");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        Button plusBtn = new TextButton("  +  ", mySkin, "small");
        plusBtn.setSize(80, 40);
        plusBtn.setPosition((GAME_WIDTH + 100)/2 , (GAME_HEIGHT +50 )/2);
        ((TextButton) plusBtn).getLabel().setFontScale(1.4f);
       // plusBtn.addAction(sequence(alpha(1), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        plusBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Plus Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Plus");
                //getUSMInstance().push(new SettingsScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });


        Button minusBtn = new TextButton("  -  ", mySkin, "small");
        minusBtn.setSize(80, 40);
        minusBtn.setPosition((GAME_WIDTH - 100)/2 , (GAME_HEIGHT +50 )/2);
        ((TextButton) minusBtn).getLabel().setFontScale(1.4f);
        // plusBtn.addAction(sequence(alpha(1), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        minusBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Minus Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Minus");
                //Uridium.music.setVolume(1.0f);
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




        Button backBtn = new TextButton("BACK", mySkin, "small");
        backBtn.setSize(250, 80);
        backBtn.setPosition((GAME_WIDTH - 340) / 2, (GAME_HEIGHT - 80) / 2);
        ((TextButton) backBtn).getLabel().setFontScale(1.4f);
        backBtn.addAction(sequence(alpha(1), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Back Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Back");
                getUSMInstance().push(new SettingsScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(backBtn);
        stage.addActor(plusBtn);
        stage.addActor(minusBtn);
        stage.addActor(pauseBtn);
        stage.addActor(playBtn);
    }
    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1,0,0,0);
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
