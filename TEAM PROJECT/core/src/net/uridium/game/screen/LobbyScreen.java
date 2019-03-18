package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.util.MyAssetManager;

import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class LobbyScreen extends MenuScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;

    Texture bgTexture;
    TextureRegion bg;

    // ROOM CODE!
    public String code = "";


    public LobbyScreen() {
        setCursor("cursor.png", 0, 0);

        // Setup background texture.
        bgTexture = new Texture(Gdx.files.internal("ice/iceWaterDeepAlt.png"));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        // Setup asset manager.
        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.queueAddSkin();
        myAssetManager.manager.finishLoading();
        mySkin = myAssetManager.manager.get("skin/freezing-ui.json");

        // Setup camera.
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        // Setup stage
        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        // Setup window.
        Label gameTitle = new Label("U R I D I U M", mySkin, "title");
        gameTitle.setSize(1280, 360);
        gameTitle.setPosition(0, 360);
        gameTitle.setFontScale(1.4f);
        gameTitle.setAlignment(Align.center);

        // Setup solo button.
        Button soloBtn = new TextButton("S O L O", mySkin);
        soloBtn.setSize(400, 80);
        soloBtn.setPosition((GAME_WIDTH - 400) / 2.0f, (GAME_HEIGHT - 80) / 2.0f);
        ((TextButton) soloBtn).getLabel().setFontScale(1.2f);
        soloBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new GameScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });

        // Setup multiplayer button.
        Button multiplayerButton = new TextButton("M U L T I P L A Y E R", mySkin);
        multiplayerButton.setSize(400, 80);
        multiplayerButton.setPosition((GAME_WIDTH - 400) / 2.0f, (GAME_HEIGHT - 80) / 2.0f - (80 + 20));
        ((TextButton) multiplayerButton).getLabel().setFontScale(1.2f);
        multiplayerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                Label label = new Label("Enter room ID", mySkin);
                label.setAlignment(Align.center);
                label.setColor(Color.BLACK);

                TextField textField = new TextField("", mySkin);


                Label label2 = new Label("CONFIRM", mySkin, "button");
                label.setAlignment(Align.center);
                label2.setFontScale(0.8f);
                Button confirmButton = new Button(mySkin);
                confirmButton.add(label2);

                Label label3 = new Label("CANCEL", mySkin, "button");
                label.setAlignment(Align.center);
                label3.setFontScale(0.8f);
                Button cancelButton = new Button(mySkin);
                cancelButton.add(label3);


                // Setup dialog to ask for room code
                Dialog dialog = new Dialog("Room ID", mySkin) {
                    protected void result(Object object) {
                        if (object.equals(true) && !(textField.getText().isEmpty())) {
                            code = textField.getText();
                            System.out.println(code);
                        }
                    }
                };
                dialog.getContentTable().add(label);
                dialog.getContentTable().add(textField);
                dialog.getContentTable().pad(20.0f);
                dialog.getButtonTable().pad(20.0f);
                dialog.key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
                dialog.button(confirmButton, true);
                dialog.button(cancelButton, false);
                dialog.setSize(GAME_WIDTH / 1.2f, GAME_HEIGHT / 2.0f);
                dialog.setMovable(false);
                dialog.show(stage);

                super.touchUp(event, x, y, pointer, button);
            }
        });


        // Setup back button.
        Button backBtn = new TextButton("B A C K", mySkin);
        backBtn.setSize(400, 80);
        backBtn.setPosition((GAME_WIDTH - 400) / 2.0f, (GAME_HEIGHT - 80) / 2.0f - (80 + 20) * 2);
        ((TextButton) backBtn).getLabel().setFontScale(1.2f);
        soloBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new MenuScreen());
                super.touchUp(event, x, y, pointer, button);
            }
        });


        stage.addActor(gameTitle);
        stage.addActor(soloBtn);
        stage.addActor(multiplayerButton);
        stage.addActor(backBtn);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        //Gdx.gl.glClearColor(1,0,0,0);
        //Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClear(GL30.GL_BLEND);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bg, 0, 0, GAME_WIDTH, GAME_WIDTH);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void dispose() {
        mySkin.dispose();
        stage.dispose();
    }
}
