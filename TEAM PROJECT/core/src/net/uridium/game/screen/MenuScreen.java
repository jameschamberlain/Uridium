package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.Uridium;
import net.uridium.game.util.GameConstants;
import net.uridium.game.util.MyAssetManager;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class MenuScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;

    public MenuScreen(){
        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.queueAddSkin();
        myAssetManager.manager.finishLoading();
        mySkin = myAssetManager.manager.get(GameConstants.skin);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        Label gameTitle = new Label("GAME MENU",mySkin,"big");
        gameTitle.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,GameConstants.centerY + GameConstants.row_height);
        gameTitle.setAlignment(Align.center);

        Button startBtn = new TextButton("START GAME",mySkin,"small");
        startBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        startBtn.setPosition(GameConstants.centerX - startBtn.getWidth()/2,GameConstants.centerY);
        startBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                game.gotoGameScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Button settingsBtn = new TextButton("SETTINGS",mySkin,"small");
        settingsBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        settingsBtn.setPosition(GameConstants.centerX - settingsBtn.getWidth()/2,startBtn.getY() - GameConstants.row_height -15);
        settingsBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                game.gotoSettingsScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });


        stage.addActor(gameTitle);
        stage.addActor(startBtn);
        stage.addActor(settingsBtn);
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
        stage.act();
        stage.draw();
    }

    public void dispose() {
        mySkin.dispose();
        stage.dispose();
    }
}
