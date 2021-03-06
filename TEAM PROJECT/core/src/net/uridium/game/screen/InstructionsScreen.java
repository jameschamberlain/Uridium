package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;

import static net.uridium.game.Uridium.setCursor;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.MENU_CURSOR;
import static net.uridium.game.util.Assets.SKIN;
import static net.uridium.game.util.Dimensions.GAME_HEIGHT;
import static net.uridium.game.util.Dimensions.GAME_WIDTH;

/**
 * The type Instructions screen.
 */
public class InstructionsScreen extends UridiumScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SpriteBatch batch1;
    private SpriteBatch batch2;
    private BitmapFont font1;
    private List list;

    private Skin skin;
    private Stage stage;
    /**
     * The Background.
     */
    Background background;
    /**
     * The Title font.
     */
    BitmapFont titleFont;
    /**
     * The Gl.
     */
    GlyphLayout gl;


    /**
     * Instantiates a new Instructions screen.
     *
     * @param background the background
     */
    public InstructionsScreen(Background background){
        setCursor(MENU_CURSOR, 0, 0);

        this.background = background;
        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");
        gl = new GlyphLayout(titleFont, "URIDIUM");
        skin = Assets.getAssets().getManager().get(SKIN);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);



        Label rules = new Label("'W' - To move up.\n 'A' - To move left.\n 'S' - To move down.\n 'D' - To move right.",skin);
        rules.setSize(1280, 360);
        rules.setPosition(0, 200);
        rules.setFontScale(1.4f);
        rules.setAlignment(Align.center);
        rules.setColor(Color.BLACK);

        Button backBtn = new TextButton("BACK",skin);
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
                getUSMInstance().push(new SettingsScreen(background));
                super.touchUp(event, x, y, pointer, button);
            }
        });


        stage.addActor(backBtn);
        stage.addActor(rules);
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
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        background.render(batch);

        titleFont.draw(batch, "URIDIUM", (GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT * 3 / 4) + gl.height / 2);

        batch.end();

        stage.act();
        stage.draw();
    }
}
