package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

public class InstructionScreen extends UridiumScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SpriteBatch batch1;
    private SpriteBatch batch2;
    private BitmapFont font1;

    private Skin skin;
    private Stage stage;
    Background background;
    BitmapFont titleFont;
    GlyphLayout gl;

    Texture bgTexture;
    TextureRegion bg;

    public InstructionScreen(Background background){
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


        Label gameTitle = new Label("I N S T R U C T I O N S",skin);
        gameTitle.setSize(1280, 360);
        gameTitle.setPosition(0, 400);
        gameTitle.setFontScale(1.4f);
        gameTitle.setAlignment(Align.center);

        Label rule1 = new Label(" 'W' - To move up.",skin);
        rule1.setSize(1280, 360);
        rule1.setPosition(-100, 300);
        rule1.setFontScale(1.4f);
        rule1.setAlignment(Align.center);

        Label rule2 = new Label(" 'A' - To move left.",skin);
        rule2.setSize(1280, 360);
        rule2.setPosition(-100, 200);
        rule2.setFontScale(1.4f);
        rule2.setAlignment(Align.center);

        Label rule3 = new Label(" 'S' - To move down.\n 'D' - To move right.",skin);
        rule3.setSize(1280, 360);
        rule3.setPosition(-50, 100);
        rule3.setFontScale(1.4f);
        rule3.setAlignment(Align.center);

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

        stage.addActor(gameTitle);
        stage.addActor(backBtn);
        stage.addActor(rule1);
        stage.addActor(rule2);
        stage.addActor(rule3);
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

        titleFont.draw(batch, "URIDIUM", (GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT * 3 / 4) + gl.height / 2);

        batch.end();

        stage.act();
        stage.draw();
    }
}
