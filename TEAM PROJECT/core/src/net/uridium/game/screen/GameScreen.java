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
import net.uridium.game.server.msg.*;
import net.uridium.game.server.msg.PlayerMoveData.Dir;
import net.uridium.game.ui.HealthBar;
import net.uridium.game.ui.InGameUI;
import net.uridium.game.ui.Scoreboard;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

import static net.uridium.game.Uridium.*;

public class GameScreen extends UridiumScreen {
    Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    OrthographicCamera camera;
    SpriteBatch batch;

    Level level;
    Texture bgTexture;
    TextureRegion bg;

    InGameUI ui;
    HealthBar healthBar;
    Scoreboard scoreboard;
    Dir lastDir;

    boolean changingLevel = false;

    public GameScreen() {
        init();
    }

    @Override
    public void init() {
        setCursor("crossair_white.png", 32, 32);

        try {
            s = new Socket("localhost",9988);
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

        ui = new InGameUI();

        healthBar = new HealthBar(level.getPlayer().getHealth(), level.getPlayer().getMaxHealth());
        scoreboard = new Scoreboard();

        bgTexture = new Texture(Gdx.files.internal("ground_01.png"));
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
//                    case Input.Keys.UP:
//                        sendShootMsg(Dir.UP);
//                        return true;
//                    case Input.Keys.LEFT:
//                        sendShootMsg(Dir.LEFT);
//                        return true;
//                    case Input.Keys.DOWN:
//                        sendShootMsg(Dir.DOWN);
//                        return true;
//                    case Input.Keys.RIGHT:
//                        sendShootMsg(Dir.RIGHT);
//                        return true;
                }

                return super.keyDown(keycode);
            }

            @Override
            public boolean keyUp(int keycode) {
                switch(keycode){
                    case Input.Keys.W:
                        if(lastDir == Dir.UP) sendDirMsg(Dir.STOP);
                        return true;
                    case Input.Keys.A:
                        if(lastDir == Dir.LEFT) sendDirMsg(Dir.STOP);
                        return true;
                    case Input.Keys.S:
                        if(lastDir == Dir.DOWN) sendDirMsg(Dir.STOP);
                        return true;
                    case Input.Keys.D:
                        if(lastDir == Dir.RIGHT) sendDirMsg(Dir.STOP);
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

    private void processMsg(Msg msg) {
        switch(msg.getType()) {
            case NEW_LEVEL:
                changeLevel((LevelData) msg.getData());
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
            case PLAYER_SCORE:
                PlayerScoreData data = (PlayerScoreData) msg.getData();
                level.updateScore(data);
                scoreboard.setScore(data.playerID, data.score);
                break;
            case PLAYER_HEALTH:
                level.updateHealth((PlayerHealthData) msg.getData());
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
            healthBar.update(level.getPlayer().getHealth());
        }
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(bg, 0, 0, GAME_WIDTH, GAME_WIDTH);
        if(!changingLevel) {
            level.render(batch);

            batch.setProjectionMatrix(camera.combined);
//            healthBar.render(batch);
            ui.render(batch, level.getPlayer());
            if(Gdx.input.isKeyPressed(Input.Keys.TAB)) scoreboard.render(batch);
        }

        batch.end();
    }

    public void changeLevel(LevelData levelData) {
        changingLevel = true;
        Level newLevel = new Level(levelData);
        level = newLevel;
        changingLevel = false;
    }
}
