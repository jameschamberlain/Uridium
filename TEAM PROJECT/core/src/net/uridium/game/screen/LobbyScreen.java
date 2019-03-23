package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.uridium.game.server.Server;
import net.uridium.game.util.MyAssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class LobbyScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;
    private String input;
    private int myPort;
    Texture bgTexture;
    TextureRegion bg;

    // ROOM CODE!
    public String code = "";


    public LobbyScreen() {
        setCursor("cursor.png", 0, 0);
        myPort = 0;
        // Setup textures and background.
        bgTexture = new Texture(Gdx.files.internal("ground_01.png"));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        // Setup asset manager.
        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.queueAddSkin();
        myAssetManager.manager.finishLoading();
        mySkin = myAssetManager.manager.get("skin/glassy-ui.json");

        // Setup camera.
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        // Setup stage
        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        // Setup window.
        Label gameTitle = new Label("U R I D I U M", mySkin, "big");
        gameTitle.setSize(1280, 360);
        gameTitle.setPosition(0, 360);
        gameTitle.setFontScale(1.4f);
        gameTitle.setAlignment(Align.center);

        // Setup solo button.
        Button soloBtn = new TextButton("S O L O", mySkin, "small");
        soloBtn.setSize(340, 80);
        soloBtn.setPosition((GAME_WIDTH - 340) / 2.0f, (GAME_HEIGHT - 80) / 2.0f);
        ((TextButton) soloBtn).getLabel().setFontScale(1.4f);
//        soloBtn.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        // Listener for start button.
        soloBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    new Server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getUSMInstance().push(new GameScreen());
                super.touchDown(event, x, y, pointer, button);
            }
        });

        // Setup multiplayer button.
        Button multiplayerButton = new TextButton("M U L T I P L A Y E R", mySkin, "small");
        multiplayerButton.setSize(340, 80);
        multiplayerButton.setPosition((GAME_WIDTH - 340) / 2.0f, (GAME_HEIGHT - 80) / 2.0f - (80 + 20));
        ((TextButton) multiplayerButton).getLabel().setFontScale(1.4f);
//        multiplayerButton.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        // Listener for multiplayer button.
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

                Button confirmButton = new TextButton("C O N F I R M", mySkin, "small");

                confirmButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        getUSMInstance().push(new GameScreen(myPort));
                        super.touchDown(event, x, y, pointer, button);
                    }
                });

                Button cancelButton = new TextButton("C A N C E L", mySkin, "small");


                // Setup dialog to ask for room code
                Dialog dialog = new Dialog("Room code", mySkin) {
                    protected void result(Object object) {
                        if (object.equals(true) && !(textField.getText().isEmpty())) {
                            code = textField.getText();
                            System.out.println(code);
                            myPort=sendRequest(code);
                        }
                    }
                };
                dialog.getContentTable().add(label);
                dialog.getContentTable().add(textField);
                dialog.key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
                dialog.button(confirmButton, true);
                dialog.button(cancelButton, false);
                dialog.setSize(1000, GAME_HEIGHT / 2.0f);
                dialog.show(stage);

                super.touchUp(event, x, y, pointer, button);
            }
        });


        stage.addActor(gameTitle);
        stage.addActor(soloBtn);
        stage.addActor(multiplayerButton);
    }

    public int sendRequest(String roomCode){
        int port=0;
        try {
            Socket s = new Socket("127.0.0.1",9966);
            PrintStream ps = new PrintStream(s.getOutputStream());
            ps.println(roomCode);
            System.out.println("Sent!!!");
            BufferedReader bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
            port = Integer.valueOf(bfr.readLine());
            System.out.println("receive"+port);
            new Server(port);
            System.out.println("Server Starts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;

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
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

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
