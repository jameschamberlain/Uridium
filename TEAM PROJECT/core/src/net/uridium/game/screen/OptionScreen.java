package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class OptionScreen implements Screen {
    private final UserInterface app;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Skin skin;
    private TextButton soundOptions, settingsOptions;
    private TextButton backButton;


    public OptionScreen(UserInterface app){

        this.app = app;
        this.stage = new Stage(new FitViewport(UserInterface.V_WIDTH, UserInterface.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
    }
    @Override
    public void show() {
        System.out.println("Options");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", app.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));



        initButtons();
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
        shapeRenderer.dispose();
    }

    private void initButtons() {
        soundOptions = new TextButton("Audio", skin, "default");
        soundOptions.setPosition(110, 260);
        soundOptions.setSize(200, 50);
        soundOptions.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        soundOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //app.setScreen(app.playScreen);
            }
        });

        settingsOptions = new TextButton("Back", skin, "default");
        settingsOptions.setPosition(110, 120);
        settingsOptions.setSize(200, 50);
        settingsOptions.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        settingsOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
            }
        });

        backButton = new TextButton("Settings", skin, "default");
        backButton.setPosition(110, 190);
        backButton.setSize(200, 50);
        backButton.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
            }
        });



        stage.addActor(soundOptions);
        stage.addActor(settingsOptions);
        stage.addActor(backButton);
    }
}
