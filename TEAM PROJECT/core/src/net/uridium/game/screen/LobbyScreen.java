package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import net.uridium.game.server.Server;
import net.uridium.game.server.ServerConstants;
import net.uridium.game.ui.Background;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Audio;
import net.uridium.game.util.Dimensions;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import static net.uridium.game.Uridium.setCursor;
import static net.uridium.game.util.Dimensions.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.*;

/**
 * The type Lobby screen.
 */
public class LobbyScreen extends UridiumScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Skin mySkin;
    private Stage stage;
    private int myPort;
    private TextureRegion bg;

    private List<String> roomList;
    private HashMap<String,int[]> roomData;

    private Background background;
    /**
     * The Title font.
     */
    BitmapFont titleFont;
    /**
     * The Gl.
     */
    GlyphLayout gl;

    // ROOM CODE!
    private String roomCode = "";
    private String tempRoomCode = "";

    private PrintStream ps;
    private Socket s;
    private ObjectInputStream oi;

    private String choice;


    /**
     * Constructor for a new lobby screen.
     *
     * @param background the background
     */
    public LobbyScreen(Background background) {
        roomData = new HashMap<>();

        try {
            s = new Socket(ServerConstants.SERVER_IP, ServerConstants.LOBBY_SERVER_PORT);
            ps = new PrintStream(s.getOutputStream());
            oi = new ObjectInputStream(s.getInputStream());
            roomData = (HashMap<String, int[]>) oi.readObject();
            System.out.println("You have got the package:"+roomData.size());
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            while(true){
                try {
                    ObjectInputStream myoi =oi;
                    System.out.println("Starts to accept");
                    roomData = (HashMap<String,int[]>)myoi.readObject();
                    System.out.println("You have entered the thread");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }).start();


        setCursor(MENU_CURSOR, 0, 0);
        myPort = 0;
        // Setup textures and background.
        Texture bgTexture = Assets.getTex((BACKGROUND));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        mySkin = Assets.getAssets().getManager().get(SKIN);

        this.background = background;
        titleFont = Assets.getAssets().getManager().get("bigFont.ttf");
        gl = new GlyphLayout(titleFont, "URIDIUM");

        // Setup camera.
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();

        // Setup stage
        stage = new Stage(new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera), batch);
        Gdx.input.setInputProcessor(stage);

        // Setup create button.
        Button createBtn = setupCreateButton();
        // Setup refresh button.
        Button refreshBtn = setupRefreshButton();
        // Setup back button.
        Button backBtn = setupBackButton();
        // Setup room list.
        ScrollPane roomList = setupRoomList();

        stage.addActor(createBtn);
        stage.addActor(refreshBtn);
        stage.addActor(backBtn);
        stage.addActor(roomList);
        stage.unfocus(roomList);
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
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);

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
                        //getUSMInstance().push(new GameScreen(myPort));
                        //setupRoomList();
                        super.touchDown(event, x, y, pointer, button);
                    }
                });

                // Setup dialog to ask for room roomCode
                Dialog dialog = new Dialog("Room ID", mySkin) {
                    protected void result(Object object) {
                        if (object.equals(true) && !(textField.getText().isEmpty())) {
                            roomCode = textField.getText();
                            String roomCodeCopy = roomCode;
                            roomCode = "create "+roomCode;
                            System.out.println(roomCode);
                            ps.println(roomCode);
                            System.out.println("Room code "+roomCode+" has been sent");
                            try{
                                while(true){
                                    int[] data = roomData.get(roomCodeCopy);
                                    Thread.sleep(100);
                                    if(null==data) continue;;
                                    myPort = roomData.get(roomCodeCopy)[0];
                                    break;

                                }
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            getUSMInstance().push(new GameScreen(myPort));
//                            try {
//                                roomData = (HashMap<String,int[]>)oi.readObject();
//                                System.out.println("Data size is "+roomData.size());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
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
        Button refreshBtn = new TextButton("J O I N", mySkin);
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
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                System.out.println("You have Choosed "+choice);
                choice = choice.split(" ")[1];
                for(String s : roomData.keySet()){
                    System.out.println(s);
                }
                String sendToken = "join "+choice;
                System.out.println(roomCode);
                ps.println(sendToken);
                System.out.println(roomData.get(choice)[0]);

                //System.out.println(roomData.get(choice)[0]);
                int portNum = roomData.get(choice)[0];

                getUSMInstance().push(new GameScreen(portNum));

                super.touchUp(event, x, y, pointer, button);
            }
        });
        return refreshBtn;
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
                Audio.getAudio().playSound(Audio.SOUND.BUTTON_CLICK);
                getUSMInstance().push(new GameSelectionScreen(background));
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
        roomList = new List<>(mySkin);

        new Thread(()->{

            while(true){
                String[] strings = new String[roomData.size()+1];
                strings[0] = "Rooms";

                int k=1;
                for(String name:roomData.keySet()){
                    strings[k]= "Room "+name+"   ("+roomData.get(name)[1]+"/4)";
                    k++;
                    //System.out.println(name);
                }

//                for (int i = 0, k = 1; i < 4; i++) {
//                    strings[k++] = "String: " +  Math.random();
//                }

                roomList.setItems(strings);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScrollPane scrollPane = new ScrollPane(roomList);
        scrollPane.setBounds(0, 0, GAME_WIDTH / 2.0f - 100, GAME_HEIGHT / 2.0f);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition((GAME_WIDTH - 170) / 2.0f, (GAME_HEIGHT - 10) / 2.0f - 290);
        scrollPane.setTransform(true);
        roomList.getSelected();
        return scrollPane;
    }

    /**
     * Sends a request to the server to join a specific room.
     *
     * @return The port of the destination room.
     */
//    private void sendRequest(String roomCode) {
//        int port = 0;
//
//        try {
//            Socket s = new Socket(ServerConstants.SERVER_IP, ServerConstants.LOBBY_SERVER_PORT);
//            PrintStream ps = new PrintStream(s.getOutputStream());
//            ObjectInputStream oi = new ObjectInputStream(s.getInputStream());
//            ps.println(roomCode);
//            System.out.println("Sent!!!");
//            roomData = (HashMap<String,int[]>)oi.readObject();
//            System.out.println("Get the list");
//
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (roomList != null) {
            if (!(roomList.getSelected().equals(" "))) {
                tempRoomCode = roomList.getSelected();
                if (!(tempRoomCode.equals(roomCode))) {
                    roomCode = tempRoomCode;
                    //Print room
                    choice = roomList.getSelected();
                    System.out.println(roomList.getSelected());
                }
            }

        }

        background.update(delta);
    }

    @Override
    public void render() {
        //Gdx.gl.glClearColor(1,0,0,0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        background.render(batch);
        titleFont.draw(batch, "URIDIUM", (Dimensions.GAME_WIDTH - gl.width) / 2, (Dimensions.GAME_HEIGHT * 3 / 4) + gl.height / 2);

        batch.end();

        stage.act();
        stage.draw();
    }

    /**
     * Dispose.
     */
    public void dispose() {
        mySkin.dispose();
        stage.dispose();
    }
}
