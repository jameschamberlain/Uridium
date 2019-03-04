package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.ui.Button;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class TestMenuScreen extends UridiumScreen {
    OrthographicCamera camera;
    SpriteBatch batch;

    Texture bgTexture;
    TextureRegion bg;

//    TextureRegion title;

    Texture title;
    Button menu;

    public TestMenuScreen() {
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

        title = new Texture(Gdx.files.internal("title.png"));
        menu = new Button((1280 - 320) / 2, (720 - 76) / 2, 320, 76, new Texture(Gdx.files.internal("buttonPlay.png")), new Texture(Gdx.files.internal("buttonPlay_pressed.png")));

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Rectangle mouse = new Rectangle(Gdx.input.getX(), 720 - Gdx.input.getY(), 1, 1);
                if(mouse.overlaps(menu.getRectangle())) {
                    getUSMInstance().push(new GameScreen());
                    return true;
                }

                return super.touchDown(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    public void update(float delta) {
//        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//            getUSMInstance().push(new GameScreen());
//        }

        menu.update(delta);
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bg, 0, 0, GAME_WIDTH, GAME_WIDTH);

        batch.draw(title, 0, 440, 1280, 200);
        menu.render(batch);

//        batch.draw(title, 0, 0, 1280, 720);
        batch.end();
    }
}
