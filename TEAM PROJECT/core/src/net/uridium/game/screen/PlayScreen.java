package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

//import com.cnnranderson.slidez.Application;
//import com.cnnranderson.slidez.actors.SlideButton;

public class PlayScreen implements Screen {

    // App reference
    private final UserInterface app;

    // Stage vars
    private Stage stage;
    private Skin skin;

    // Game Grid
    private int boardSize = 4;
    private int holeX, holeY;
    //private SlideButton[][] buttonGrid;

    // Nav-Buttons
    private TextButton buttonBack, Player1, Player2;


    public PlayScreen(final UserInterface app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(UserInterface.V_WIDTH, UserInterface.V_HEIGHT, app.camera));
    }

    @Override
    public void show() {
        System.out.println("PLAY");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        NavigationButtons();

    }


    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    // Initialize the back button
    private void NavigationButtons() {
        buttonBack = new TextButton("Back", skin, "default");
        buttonBack.setPosition(200, app.camera.viewportHeight - 310);
        buttonBack.setSize(100, 50);
        buttonBack.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));

        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
                System.out.println("BackButton Clicked");
            }
        });

        Player1 = new TextButton("1 Player", skin, "default");
        Player1.setPosition(200, app.camera.viewportHeight - 150);
        Player1.setSize(100, 50);
        Player1.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));

        Player1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
                System.out.println("1Player Clicked");
            }
        });

        Player2 = new TextButton("2 Players", skin, "default");
        Player2.setPosition(200, app.camera.viewportHeight - 230);
        Player2.setSize(100, 50);
        Player2.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));

        Player2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
                System.out.println("2Player Clicked");
            }
        });

        stage.addActor(buttonBack);
        stage.addActor(Player1);
        stage.addActor(Player2);
    }


}



