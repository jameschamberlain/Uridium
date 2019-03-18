/*package net.uridium.game.screen;

public class LoadingScreen extends UridiumScreen {
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public AssetManager assets;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private float progress;

    public LoadingScreen() {
        //this.app = app;
        this.shapeRenderer = new ShapeRenderer();
    }

    private void queueAssets() {
        assets.load("img/Picture.png", Texture.class);
        assets.load("ui/uiskin.atlas", TextureAtlas.class);
    }

    @Override
    public void show() {
        System.out.println("LOADING");
        shapeRenderer.setProjectionMatrix(camera.combined);
        this.progress = 0f;
        queueAssets();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, assets.getProgress(), .1f);
        if (assets.update() && progress >= assets.getProgress() - .001f) {
            //setScreen(app.splashScreen);
            getUSMInstance().push(new MenuScreen());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32, camera.viewportHeight / 2 - 8, camera.viewportWidth - 100, 16);

        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(32, camera.viewportHeight / 2 - 8, progress * (camera.viewportWidth - 100), 16);
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
    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {

    }
}*/
