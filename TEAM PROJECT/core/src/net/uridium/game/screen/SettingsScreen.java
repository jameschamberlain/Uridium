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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Dimensions;

import static net.uridium.game.Uridium.*;
import static net.uridium.game.res.Textures.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.*;
import static net.uridium.game.util.Dimensions.BUTTON_HEIGHT;
import static net.uridium.game.util.Dimensions.BUTTON_WIDTH;

public class SettingsScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin skin;
    private Stage stage;

    Background background;

    public SettingsScreen(Background background) {
        setCursor(MENU_CURSOR, 0, 0);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();
        this.background = background;

        skin = Assets.getAssets().getManager().get(SKIN);

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);

        Button insBtn = new TextButton("INSTRUCTIONS", skin);
        insBtn.setSize(340, 80);
        insBtn.setPosition((GAME_WIDTH - 340) / 2, (GAME_HEIGHT - 80) / 2 + 100);
        insBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new InstructionScreen(background));
                super.touchDown(event, x, y, pointer, button);
            }
        });



        Button audioBtn = new TextButton("AUDIO", skin);
        audioBtn.setSize(340, 80);
        audioBtn.setPosition((GAME_WIDTH - 340) / 2, (GAME_HEIGHT - 80) / 2);
        //volBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        audioBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new AudioScreen(background));
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
                getUSMInstance().push(new MenuScreen(background));
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(backBtn);
        stage.addActor(audioBtn);
        stage.addActor(insBtn);

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