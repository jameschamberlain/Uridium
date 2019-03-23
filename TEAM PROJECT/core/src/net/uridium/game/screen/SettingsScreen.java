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
import net.uridium.game.util.MyAssetManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class SettingsScreen extends MenuScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;

    Texture bgTexture;
    TextureRegion bg;

    public boolean isPlaying = false;

    public SettingsScreen(){

        MenuScreen menuScreen = new MenuScreen();

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

        //if(!music.isPlaying())
          //  music.play();

        //sound.play(1);

        //sound.stop();

        Button scrBtn = new TextButton("S C R E E N",mySkin,"small");
        scrBtn.setSize(340,80);
        scrBtn.setPosition((GAME_WIDTH - 340) / 2,(GAME_HEIGHT - 80) / 2 + (100));
        ((TextButton) scrBtn).getLabel().setFontScale(1.4f);
        //volBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        scrBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Screen Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Screen");
                getUSMInstance().push(new ScreenSetting());
                super.touchDown(event, x, y, pointer, button);
            }
        });

        Button volBtn = new TextButton("A U D I O",mySkin,"small");
        volBtn.setSize(340,80);
        volBtn.setPosition((GAME_WIDTH - 340) / 2,(GAME_HEIGHT - 80) / 2);
        ((TextButton) volBtn).getLabel().setFontScale(1.4f);
        //volBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        volBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Volume Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Vol");
                getUSMInstance().push(new AudioScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });



        Button insBtn = new TextButton("I N S T R U C T I O N S",mySkin,"small");
        insBtn.setSize(340, 80);
        insBtn.setPosition((GAME_WIDTH - 340) / 2,(GAME_HEIGHT - 80) / 2 - (80 + 20));
        ((TextButton) insBtn).getLabel().setFontScale(1.4f);
        //backBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        insBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Instructions Clicked");
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new InstructionScreen());
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Button backBtn = new TextButton("B A C K",mySkin,"small");
        backBtn.setSize(340, 80);
        backBtn.setPosition((GAME_WIDTH - 340) / 2,(GAME_HEIGHT - 80) / 2 - (200));
        ((TextButton) backBtn).getLabel().setFontScale(1.4f);
        //insBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        backBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Back Clicked");
                //music.pause();
                //sound.pause();
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
        stage.addActor(insBtn);
        stage.addActor(scrBtn);

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
