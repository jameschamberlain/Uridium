package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import net.uridium.game.Uridium;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Dimensions;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static net.uridium.game.Uridium.*;
import static net.uridium.game.Uridium.GAME_WIDTH;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.MENU_CURSOR;
import static net.uridium.game.util.Assets.SKIN;
import static net.uridium.game.util.Dimensions.GAME_HEIGHT;

public class ScreenSetting extends UridiumScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin skin;
    private Stage stage;

    Background background;
    BitmapFont titleFont;
    GlyphLayout gl;

    public ScreenSetting(Background background) {
        setCursor(MENU_CURSOR, 0, 0);

        this.background = background;
        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");
        gl = new GlyphLayout(titleFont, "URIDIUM");

        skin = Assets.getAssets().getManager().get(SKIN);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Dimensions.GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(Dimensions.GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        Button fulBtn = new TextButton("F U L L S C R E E N",skin);
        fulBtn.setSize(340,80);
        fulBtn.setPosition((GAME_WIDTH - 340) / 2,(GAME_HEIGHT - 80) / 2);
        ((TextButton) fulBtn).getLabel().setFontScale(1.4f);
        //volBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        fulBtn.addListener(new InputListener(){
            private OrthographicCamera camera1;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Full Clicked");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Full");
                //GAME_WIDTH = 3200;
                //GAME_HEIGHT = 1800;
                //getUSMInstance().push(new SettingsScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });


        Button minBtn = new TextButton("M I N I M I Z E D",skin);
        minBtn.setSize(340, 80);
        minBtn.setPosition((GAME_WIDTH - 340) / 2,(GAME_HEIGHT - 80) / 2 - (80 + 20));
        ((TextButton) minBtn).getLabel().setFontScale(1.4f);
        //backBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        minBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Minimized Clicked");
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //getUSMInstance().push(new SettingsScreen());
                //GAME_WIDTH = 3200;
                //GAME_HEIGHT = 1800;
                super.touchUp(event, x, y, pointer, button);
            }
        });






        Button backBtn = new TextButton("BACK", skin);
        backBtn.setSize(340, 80);
        backBtn.setPosition((GAME_WIDTH - 340) / 2, (GAME_HEIGHT - 80) / 2 - (175));
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
                getUSMInstance().push(new SettingsScreen(background));
                super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(backBtn);
        stage.addActor(minBtn);
        stage.addActor(fulBtn);
        //stage.addActor(pauseBtn);
        //stage.addActor(playBtn);
    }
    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        background.render(batch);

        titleFont.draw(batch, "URIDIUM", (Dimensions.GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT * 3 / 4) + gl.height / 2);

        batch.end();

        stage.act();
        stage.draw();
    }
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
