package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.Uridium;
import net.uridium.game.server.msg.GameOverData;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Audio;
import net.uridium.game.util.Dimensions;

import java.util.ArrayList;

import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.MENU_CURSOR;
import static net.uridium.game.util.Assets.SKIN;

public class GameOverScreen extends UridiumScreen {
    OrthographicCamera camera;
    SpriteBatch batch;

    private Skin skin;
    private Stage stage;

    Background background;
    BitmapFont titleFont;
    BitmapFont smallFont;
    GlyphLayout gl;

    GameOverData gameOverData;
    private float lockout = 3;

    public GameOverScreen(GameOverData gameOverData) {
        setCursor(MENU_CURSOR, 0, 0);

        skin = Assets.getAssets().getManager().get(SKIN);

        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");
        smallFont = Assets.get("smallFont.ttf", BitmapFont.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        // Setup stage
        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);

        background = new Background();

        this.gameOverData = gameOverData;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(lockout <= 0) {
                    Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                    getUSMInstance().set(new MenuScreen(background));
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        background.update(delta);
        lockout -= delta;
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        background.render(batch);
        gl = new GlyphLayout(titleFont, "GAME OVER");
        titleFont.draw(batch, "GAME OVER", (Dimensions.GAME_WIDTH - gl.width) / 2, (Dimensions.GAME_HEIGHT * 3 / 4) + gl.height / 2);

        float y = GAME_HEIGHT / 2;
        smallFont.getData().setScale(1.4f);
        smallFont.setColor(Color.BLACK);
        for(int i = 0; i < gameOverData.rankings.length; i++) {
            GameOverData.PlayerRankingData prd = gameOverData.rankings[i];
            String text = GameScreen.positionToString(i + 1) + " ~ " + prd.playerName + " " + prd.playerScore;
            gl = new GlyphLayout(smallFont, text);
            smallFont.draw(batch, text, (Dimensions.GAME_WIDTH - gl.width) / 2, y + gl.height / 2);
            y -= (gl.height + 25);
        }

        smallFont.getData().setScale(0.815f);
        gl = new GlyphLayout(smallFont, "Click anywhere to go to menu");
        smallFont.draw(batch, "Click anywhere to go to menu", (GAME_WIDTH - gl.width) / 2, 25 + gl.height);

        smallFont.getData().setScale(0.8f);
        smallFont.setColor(Color.WHITE);
        gl = new GlyphLayout(smallFont, "Click anywhere to go to menu");
        smallFont.draw(batch, "Click anywhere to go to menu", (GAME_WIDTH - gl.width) / 2, 25 + gl.height);

        smallFont.getData().setScale(1);
        batch.end();

        stage.act();
        stage.draw();
    }
}
