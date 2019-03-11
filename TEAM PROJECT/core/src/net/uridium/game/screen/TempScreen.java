package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Level;
import net.uridium.game.gameplay.entity.Bullet;
import net.uridium.game.gameplay.entity.Player;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class TempScreen extends UridiumScreen {
    OrthographicCamera camera;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    Player player;
    Level level;

    public TempScreen() {
        init();
    }

    @Override
    public void init() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        player = new Player(GAME_WIDTH / 2 - 27.5f, GAME_HEIGHT / 2 - 27.5f, 55, 55);
        level = new Level();
        level.init();
    }

    @Override
    public void update(float delta) {
        player.update(delta);
        level.update(delta);
        level.checkPlayerCollisions(player);

        Rectangle playerBody = player.getBody();

        Vector2 bulletSpawn = new Vector2();
        if(player.canShoot()) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                level.spawnBullet(new Bullet(playerBody.getCenter(bulletSpawn).add(0, playerBody.height / 2), 0));
                player.shoot();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                level.spawnBullet(new Bullet(playerBody.getCenter(bulletSpawn).sub(playerBody.width / 2, 0), 270));
                player.shoot();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                level.spawnBullet(new Bullet(playerBody.getCenter(bulletSpawn).sub(0, playerBody.height / 2), 180));
                player.shoot();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                level.spawnBullet(new Bullet(playerBody.getCenter(bulletSpawn).add(playerBody.width / 2, 0), 90));
                player.shoot();
            }
        }
    }

    @Override
    public void render() {
//        Matrix4 matrix = camera.combined.cpy();
//        matrix.translate(100, 0, 0);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        level.render(shapeRenderer);
        player.render(shapeRenderer);

        shapeRenderer.end();
    }
}
