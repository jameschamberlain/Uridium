package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class InstructionScreen extends UridiumScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SpriteBatch batch1;
    private SpriteBatch batch2;
    private BitmapFont font1;

    private Skin mySkin;
    private Stage stage;

    Texture bgTexture;
    TextureRegion bg;

    public InstructionScreen(){
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


        Label gameTitle = new Label("I N S T R U C T I O N S",mySkin,"big");
        gameTitle.setSize(1280, 360);
        gameTitle.setPosition(0, 400);
        gameTitle.setFontScale(1.4f);
        gameTitle.setAlignment(Align.center);

        Label rule1 = new Label(" ↑ - To move up.",mySkin,"big");
        rule1.setSize(1280, 360);
        rule1.setPosition(-100, 300);
        rule1.setFontScale(1.4f);
        rule1.setAlignment(Align.center);




        Button backBtn = new TextButton("BACK",mySkin,"small");
        backBtn.setSize(200, 80);
        backBtn.setPosition((GAME_WIDTH - 340) / 2 + 50,(GAME_HEIGHT - 80) / 2 - (300));
        ((TextButton) backBtn).getLabel().setFontScale(1.4f);
        //insBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        backBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Back Clicked");
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new SettingsScreen());
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(gameTitle);
        stage.addActor(backBtn);
        stage.addActor(rule1);
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
}
