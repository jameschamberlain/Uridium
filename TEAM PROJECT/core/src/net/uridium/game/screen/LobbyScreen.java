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
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import static net.uridium.game.Uridium.setCursor;
import static net.uridium.game.util.Dimensions.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.*;

public class LobbyScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;
    private int myPort;
    private TextureRegion bg;

    private Background background;

    // ROOM CODE!
    private String code = "";

    /**
     * Constructor for a new lobby screen.
     */
    public LobbyScreen(Background background) {
        setCursor(MENU_CURSOR, 0, 0);
        myPort = 0;
        // Setup textures and background.
        Texture bgTexture = Assets.getTex((BACKGROUND));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        mySkin = Assets.getAssets().getManager().get(SKIN);

        this.background = background;

        // Setup camera.
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        // Setup stage
        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);


        // Setup game title label.
        Label gameTitle = setupGameTitle();
        // Setup create button.
        Button createBtn = setupCreateButton();
        // Setup refresh button.
        Button refreshBtn = setupRefreshButton();
        // Setup random button.
        Button randomBtn = setupRandomButton();
        // Setup back button.
        Button backBtn = setupBackButton();
        // Setup room list.
        ScrollPane roomList = setupRoomList();

        // Add title, buttons and room list to the screen.
        stage.addActor(gameTitle);
        stage.addActor(createBtn);
        stage.addActor(refreshBtn);
        stage.addActor(randomBtn);
        stage.addActor(backBtn);
        stage.addActor(roomList);
    }


    /**
     * Setup the game title label.
     *
     * @return The game title label.
     */
    private Label setupGameTitle() {
        Label gameTitle = new Label("U R I D I U M", mySkin, "title");
        gameTitle.setSize(TITLE_WIDTH, TITLE_HEIGHT);
        gameTitle.setPosition(TITLE_X, TITLE_Y);
        gameTitle.setFontScale(TITLE_FONT_SCALE);
        gameTitle.setAlignment(Align.center);
        return gameTitle;
    }


    /**
     * Setup the create button.
     * Creates a new room.
     *
     * @return The create button.
     */
    private Button setupCreateButton() {
        Button createBtn = new TextButton("C R E A T E", mySkin);
        createBtn.setSize(SIDE_BUTTON_WIDTH, SIDE_BUTTON_HEIGHT);
        createBtn.setPosition(SIDE_BUTTON_X, SIDE_BUTTON_Y);
        // Listener for click events.
        createBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Label to describe dialog.
                Label descriptionLabel = new Label("Enter room ID", mySkin);
                descriptionLabel.setAlignment(Align.center);
                descriptionLabel.setColor(Color.BLACK);

                // Text field for user input.
                TextField textField = new TextField("", mySkin);

                // Setup cancel button.
                Label cancelLabel = new Label("CANCEL", mySkin, "button");
                cancelLabel.setFontScale(DIALOG_BUTTON_FONT_SCALE);
                Button cancelButton = new Button(mySkin);
                cancelButton.add(cancelLabel);

                // Setup confirm button.
                Label confirmLabel = new Label("CONFIRM", mySkin, "button");
                confirmLabel.setFontScale(DIALOG_BUTTON_FONT_SCALE);
                Button confirmButton = new Button(mySkin);
                confirmButton.add(confirmLabel);
                // Listener for click events.
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

                // Setup dialog to ask for room code
                Dialog dialog = new Dialog("Room ID", mySkin) {
                    protected void result(Object object) {
                        if (object.equals(true) && !(textField.getText().isEmpty())) {
                            code = textField.getText();
                            System.out.println(code);
                            myPort = sendRequest(code);
                        }
                    }
                };
                dialog.getContentTable().add(descriptionLabel);
                dialog.getContentTable().add(textField);
                dialog.getContentTable().pad(DIALOG_PADDING);
                dialog.getButtonTable().pad(DIALOG_PADDING);
                dialog.key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
                dialog.button(confirmButton, true);
                dialog.button(cancelButton, false);
                dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
                dialog.setMovable(false);
                dialog.show(stage);

                super.touchUp(event, x, y, pointer, button);
            }
        });
        return createBtn;
    }


    /**
     * Setup the refresh button.
     * Refreshes the list of rooms.
     *
     * @return The refresh button.
     */
    private Button setupRefreshButton() {
        Button refreshBtn = new TextButton("R E F R E S H", mySkin);
        refreshBtn.setSize(SIDE_BUTTON_WIDTH, SIDE_BUTTON_HEIGHT);
        refreshBtn.setPosition(SIDE_BUTTON_X, SIDE_BUTTON_Y - SIDE_BUTTON_GAP);
        // Listener for click events.
        refreshBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        return refreshBtn;
    }


    /**
     * Setup the random button.
     * Joins a random room.
     *
     * @return The random button.
     */
    private Button setupRandomButton() {
        Button randomBtn = new TextButton("R A N D O M", mySkin);
        randomBtn.setSize(SIDE_BUTTON_WIDTH, SIDE_BUTTON_HEIGHT);
        randomBtn.setPosition(SIDE_BUTTON_X, SIDE_BUTTON_Y - SIDE_BUTTON_GAP * 2);
        // Listener for click events.
        randomBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        return randomBtn;
    }


    /**
     * Setup the back button.
     * Returns the user to the game selection screen.
     *
     * @return The back button.
     */
    private Button setupBackButton() {
        Button backBtn = new TextButton("B A C K", mySkin);
        backBtn.setSize(SIDE_BUTTON_WIDTH, SIDE_BUTTON_HEIGHT);
        backBtn.setPosition(SIDE_BUTTON_X, SIDE_BUTTON_Y - SIDE_BUTTON_GAP * 3);
        // Listener for click events.
        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getUSMInstance().push(new GameSelectionScreen(background));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        return backBtn;
    }


    /**
     * Setup the room list scroll pane.
     * Used for displaying the list of rooms.
     *
     * @return The room list scroll pane.
     */
    private ScrollPane setupRoomList() {
        List<String> roomList = new List<>(mySkin);
        String[] strings = new String[20];
        for (int i = 0, k = 0; i < 20; i++) {
            strings[k++] = "String: " + i;
        }
        roomList.setItems(strings);
        ScrollPane scrollPane = new ScrollPane(roomList);
        scrollPane.setBounds(0, 0, GAME_WIDTH / 2.0f - 100, GAME_HEIGHT / 2.0f);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition((GAME_WIDTH - 170) / 2.0f, (GAME_HEIGHT - 10) / 2.0f - 290);
        scrollPane.setTransform(true);
        return scrollPane;
    }

    /**
     * Sends a request to the server to join a specific room.
     *
     * @param roomCode The code of the selected room.
     * @return The port of the destination room.
     */
    private int sendRequest(String roomCode) {
        int port = 0;
        try {
            Socket s = new Socket("127.0.0.1", 9966);
            PrintStream ps = new PrintStream(s.getOutputStream());
            ps.println(roomCode);
            System.out.println("Sent!!!");
            BufferedReader bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
            port = Integer.valueOf(bfr.readLine());
            System.out.println("receive" + port);
            new Server(port);
            System.out.println("Server Starts");
        }
        catch (IOException e) {
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
