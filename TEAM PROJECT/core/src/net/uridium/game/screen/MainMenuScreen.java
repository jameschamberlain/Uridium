package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
public class MainMenuScreen implements Screen {
    private final UserInterface userInterface;


    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;

    private TextButton buttonPlay, buttonExit, buttonSettings;

    private ShapeRenderer shapeRenderer;
    private TextButton buttonOptions;
    private Texture texture;
    private Image background;
    private TextureRegion bgtex;

    public MainMenuScreen(final UserInterface userInterface) {
        this.userInterface = userInterface;
        this.stage = new Stage(new FitViewport(UserInterface.V_WIDTH, UserInterface.V_HEIGHT, userInterface.camera));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        System.out.println("MENU");
        Gdx.input.setInputProcessor(stage);
        stage.clear();



        this.skin = new Skin();
        this.skin.addRegions(userInterface.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", userInterface.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));




        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("Picture.png"));
        /*bgtex = new TextureRegion(texture,800,600);
        background = new Image(bgtex);
        stage.addActor(background);*/

        initButtons();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.graphics.setTitle("Uridium");

        batch.begin();
        batch.draw(texture, 150, 500);
        batch.end();
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
        batch.dispose();
        texture.dispose();
        stage.dispose();
        shapeRenderer.dispose();
    }

    private void initButtons() {
        buttonPlay = new TextButton("Play", skin, "default");
        buttonPlay.setPosition(130, 260);
        buttonPlay.setSize(150, 50);
        buttonPlay.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                userInterface.setScreen(userInterface.playScreen);
            }
        });

        buttonOptions = new TextButton("Options", skin, "default");
        buttonOptions.setPosition(130, 190);
        buttonOptions.setSize(150, 50);
        buttonOptions.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                userInterface.setScreen(userInterface.optionScreen);
            }
        });

        buttonExit = new TextButton("Exit", skin, "default");
        buttonExit.setPosition(130, 120);
        buttonExit.setSize(150, 50);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(buttonPlay);
        stage.addActor(buttonOptions);
        stage.addActor(buttonExit);
    }
}