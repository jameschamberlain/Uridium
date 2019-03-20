package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.util.MyAssetManager;

import static net.uridium.game.Uridium.*;
import static net.uridium.game.res.Textures.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class SettingsScreen extends MenuScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;

    Texture bgTexture;
    TextureRegion bg;

    SettingsScreen() {
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

        Button volBtn = new TextButton("VOLUME", mySkin);
        volBtn.setSize(340, 80);
        volBtn.setPosition((GAME_WIDTH - 340) / 2, (GAME_HEIGHT - 80) / 2);
        //volBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        volBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new AudioScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });


        Button backBtn = new TextButton("BACK", mySkin);
        backBtn.setSize(340, 80);
        backBtn.setPosition((GAME_WIDTH - 340) / 2, (GAME_HEIGHT - 80) / 2 - (80 + 20));
        //backBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
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

        stage.addActor(backBtn);
        stage.addActor(volBtn);

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
        //Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

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
