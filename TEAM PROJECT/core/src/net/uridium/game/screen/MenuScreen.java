package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.uridium.game.gameplay.Level;
import net.uridium.game.gameplay.LevelFactory;
import net.uridium.game.ui.HealthBar;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class MenuScreen extends UridiumScreen {
    OrthographicCamera camera;
    SpriteBatch batch;

    Texture bgTexture;
    TextureRegion bg;

    TextureRegion title;


    public MenuScreen() {
        init();
    }

    @Override
    public void init() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        bgTexture = new Texture(Gdx.files.internal("ground_01.png"));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        title = new TextureRegion(new Texture(Gdx.files.internal("temptitle.png")));
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            getUSMInstance().push(new TempScreen());
        }
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bg, 0, 0, GAME_WIDTH, GAME_WIDTH);
        batch.draw(title, 0, 0, 1280, 720);
        batch.end();
    }
}
