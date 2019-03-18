/*package net.uridium.game.screen;*/

/*import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.Uridium;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class SplashScreen extends UridiumScreen {

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public AssetManager assets;
    private Stage stage;
    private Texture splashTex;

    private Image splashImg;

    public SplashScreen(){
        assets = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Uridium.GAME_WIDTH, Uridium.GAME_HEIGHT);
        batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Uridium.GAME_WIDTH, Uridium.GAME_HEIGHT, camera));

        System.out.println("SPLASH");
        Gdx.input.setInputProcessor(stage);

        Runnable transitionRunnable;
        transitionRunnable = new Runnable() {
            @Override
            public void run() {
                //app.setScreen(app.mainMenuScreen);
            }
        };

        splashTex = new Texture("img/Picture.png");
        splashImg = new Image(splashTex);
        splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
        splashImg.setPosition(stage.getWidth() / 2 - 32, stage.getHeight() + 32);
        splashImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 32, 2f, Interpolation.swing)),
                delay(1.5f), fadeOut(1.25f), run(transitionRunnable)));

        stage.addActor(splashImg);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render() {

    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }



    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}*/
