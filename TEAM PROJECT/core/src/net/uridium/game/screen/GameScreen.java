package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Level;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.ServerConstants;
import net.uridium.game.server.msg.*;
import net.uridium.game.server.msg.PlayerMoveData.Dir;
import net.uridium.game.ui.InGameUI;
import net.uridium.game.ui.Scoreboard;
import net.uridium.game.util.Assets;
import net.uridium.game.util.Audio;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

import static net.uridium.game.Uridium.*;
import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;
import static net.uridium.game.util.Assets.BACKGROUND;
import static net.uridium.game.util.Assets.GAME_CURSOR;

/**
 * The type Game screen.
 */
public class GameScreen extends UridiumScreen {
    /**
     * The S.
     */
    Socket s;
    /**
     * The Oos.
     */
    ObjectOutputStream oos;
    /**
     * The Ois.
     */
    ObjectInputStream ois;

    /**
     * The Camera.
     */
    OrthographicCamera camera;
    /**
     * The Batch.
     */
    SpriteBatch batch;

    /**
     * The Level.
     */
    Level level;
    /**
     * The Bg texture.
     */
    Texture bgTexture;
    /**
     * The Bg.
     */
    TextureRegion bg;

    /**
     * The Ui.
     */
    InGameUI ui;
    /**
     * The Scoreboard.
     */
    Scoreboard scoreboard;
    /**
     * The Last dir.
     */
    Dir lastDir;

    /**
     * The Changing level.
     */
    boolean changingLevel = false;

    /**
     * Instantiates a new Game screen.
     */
    public GameScreen() {
        init();
    }

    /**
     * Instantiates a new Game screen.
     *
     * @param port the port
     */
    public GameScreen(int port){init(port,false);}

    @Override
    public void init(){init(6666,true);}


    /**
     * Init.
     *
     * @param port         the port
     * @param singlePlayer the single player
     */
    public void init(int port, boolean singlePlayer) {
        setCursor(GAME_CURSOR, 32, 32);

        try {
            if(singlePlayer){
               s = new Socket("127.0.0.1",port);
            }
            else{
                s = new Socket(ServerConstants.SERVER_IP,port);
            }
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());

            Msg msg = (Msg) ois.readObject();
            if(msg.getType() == Msg.MsgType.NEW_LEVEL)
                level = new Level((LevelData) msg.getData());
            else
                throw new Exception("Expected MsgType of NEW_LEVEL, received MsgType of " + msg.getType().name());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        batch = new SpriteBatch();

        ui = new InGameUI(level.getOffsets(new Vector2()).y);

//        healthBar = new HealthBar(level.getPlayer().getHealth(), level.getPlayer().getMaxHealth());
        scoreboard = new Scoreboard();

        bgTexture = Assets.getTex((BACKGROUND));
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bg = new TextureRegion(bgTexture);
        bg.setRegion(0, 0, 640, 640);

        new Thread(()-> {
            while(true){
                try {
                    Collection<Msg> msgs = (Collection<Msg>) ois.readObject();
                    for(Msg msg : msgs)
                        processMsg(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        lastDir = Dir.STOP;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if(level.getPlayer().getHealth() == 0) return super.keyDown(keycode);

                switch(keycode){
                    case Input.Keys.W:
                        sendDirMsg(Dir.UP);
                        lastDir = Dir.UP;
                        return true;
                    case Input.Keys.A:
                        sendDirMsg(Dir.LEFT);
                        lastDir = Dir.LEFT;
                        return true;
                    case Input.Keys.S:
                        sendDirMsg(Dir.DOWN);
                        lastDir = Dir.DOWN;
                        return true;
                    case Input.Keys.D:
                        sendDirMsg(Dir.RIGHT);
                        lastDir = Dir.RIGHT;
                        return true;
                }

                return super.keyDown(keycode);
            }

            @Override
            public boolean keyUp(int keycode) {
                if(level.getPlayer().getHealth() == 0) return super.keyUp(keycode);

                switch(keycode){
                    case Input.Keys.W:
                        if(lastDir == Dir.UP) sendDirMsg(getNewDir());
                        return true;
                    case Input.Keys.A:
                        if(lastDir == Dir.LEFT) sendDirMsg(getNewDir());
                        return true;
                    case Input.Keys.S:
                        if(lastDir == Dir.DOWN) sendDirMsg(getNewDir());
                        return true;
                    case Input.Keys.D:
                        if(lastDir == Dir.RIGHT) sendDirMsg(getNewDir());
                        return true;
                }

                return super.keyUp(keycode);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                screenY = GAME_HEIGHT - screenY;
                shoot(screenX, screenY);

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                screenY = GAME_HEIGHT - screenY;
                shoot(screenX, screenY);
                return true;
            }
        });
    }

    private Dir getNewDir() {
        if(Gdx.input.isKeyPressed(Input.Keys.W)) return Dir.UP;
        else if(Gdx.input.isKeyPressed(Input.Keys.A)) return Dir.LEFT;
        else if(Gdx.input.isKeyPressed(Input.Keys.S)) return Dir.DOWN;
        else if(Gdx.input.isKeyPressed(Input.Keys.D)) return Dir.RIGHT;

        return Dir.STOP;
    }

    private void processMsg(Msg msg) {
        switch(msg.getType()) {
            case NEW_LEVEL:
                changeLevel((LevelData) msg.getData());
                Audio.getAudio().playSound(Audio.SOUND.CHANGE_ROOM);
                break;
            case NEW_ENTITY:
                level.addEntity((Entity) msg.getData());
                break;
            case ENTITY_UPDATE:
                level.updateEntity((EntityUpdateData) msg.getData());
                break;
            case REMOVE_ENTITY:
                level.removeEntity((RemoveEntityData) msg.getData());
                break;
            case REPLACE_TILE:
                level.replaceTile((ReplaceTileData) msg.getData());
                break;
            case UNLOCK_DOORS:
                level.unlockDoors();
                break;
            case PLAYER_UPDATE:
                PlayerUpdateData data = (PlayerUpdateData) msg.getData();
                level.updatePlayer(data);
                scoreboard.updateScoreboard(level.getPlayers());
                break;
            case PLAYER_HEALTH:
                level.updateHealth((PlayerHealthData) msg.getData());
                scoreboard.updateScoreboard(level.getPlayers());
                break;
            case PLAYER_DEATH:
                PlayerDeathData pdd = (PlayerDeathData) msg.getData();
                Audio.getAudio().playSound(Audio.SOUND.PLAYER_DEAD);
                level.killPlayer(pdd);
                if(level.getPlayerID() == pdd.ID) ui.showExpandingText("You came " + positionToString(pdd.position) + "!", 0.8f, true);
                break;
            case PLAYER_POWERUP:
                PlayerPowerupData ppd = (PlayerPowerupData) msg.getData();
                if(ppd.playerID == level.getPlayerID()) ui.updatePowerup(ppd);
                break;
            case GAME_OVER:
                Audio.getAudio().playSound(Audio.SOUND.GAME_OVER);
                Gdx.app.postRunnable(() -> getUSMInstance().clearAndSet(new GameOverScreen((GameOverData) msg.getData())));
                break;
        }
    }

    private void sendDirMsg(Dir dir) {
        try {
            oos.writeObject(new Msg(Msg.MsgType.PLAYER_MOVE, new PlayerMoveData(dir)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendShootMsg(double angle) {
        try {
            oos.writeObject(new Msg(Msg.MsgType.PLAYER_SHOOT, new PlayerShootData(angle)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shoot(float screenX, float screenY) {
        Player player = level.getPlayer();
        Vector2 playerCenter = player.getCenter(new Vector2());
        float diffX = screenX - playerCenter.x - level.xOffset;
        float diffY = screenY - playerCenter.y - level.yOffset;

        double x = Math.atan2(diffY, diffX);
        x *= 180 / Math.PI;
        sendShootMsg(x);
    }

    @Override
    public void update(float delta) {
        if(!changingLevel) {
            level.update(delta);
            ui.update(delta);

            if(level.getId() == -1)
                ui.setBossHpPercent(level.getBoss().getHealth() / level.getBoss().getMaxHealth());
        }
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(bg, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        if(!changingLevel) {
            level.render(batch);

            batch.setProjectionMatrix(camera.combined);
            ui.render(batch, level.getPlayer());
            if(Gdx.input.isKeyPressed(Input.Keys.TAB)) scoreboard.render(batch);
        }
        batch.end();
    }

    /**
     * Change level.
     *
     * @param levelData the level data
     */
    public void changeLevel(LevelData levelData) {
        changingLevel = true;
        level = new Level(levelData);
        changingLevel = false;

        if(levelData.id == -1)
            ui.setBossLevel(true);
    }

    /**
     * Position to string string.
     *
     * @param position the position
     * @return the string
     */
    public static String positionToString(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append(position);

        switch(position) {
            case 1:
                builder.append("st");
                break;
            case 2:
                builder.append("nd");
                break;
            case 3:
                builder.append("rd");
                break;
            case 4:
                builder.append("th");
                break;
        }

        return builder.toString();
    }
}
