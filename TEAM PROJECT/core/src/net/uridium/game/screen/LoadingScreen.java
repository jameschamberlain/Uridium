package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;


public class LoadingScreen implements Screen {

    private final UserInterface app;

    private ShapeRenderer shapeRenderer;
    private float progress;

    public LoadingScreen(final UserInterface app) {
        this.app = app;
        this.shapeRenderer = new ShapeRenderer();
    }

    private void queueAssets() {
        app.assets.load("img/Picture.png", Texture.class);
        app.assets.load("ui/uiskin.atlas", TextureAtlas.class);
    }

    @Override
    public void show() {
        System.out.println("LOADING");
        shapeRenderer.setProjectionMatrix(app.camera.combined);
        this.progress = 0f;
        queueAssets();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, app.assets.getProgress(), .1f);
        if (app.assets.update() && progress >= app.assets.getProgress() - .001f) {
            app.setScreen(app.splashScreen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32, app.camera.viewportHeight / 2 - 8, app.camera.viewportWidth - 100, 16);

        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(32, app.camera.viewportHeight / 2 - 8, progress * (app.camera.viewportWidth - 100), 16);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}