package net.uridium.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.uridium.game.screen.*;

import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class Uridium extends ApplicationAdapter {
	public static final String TITLE = "Uridium";
	public static final float VERSION = .8f;
	public static final int V_WIDTH = 540;
	public static final int V_HEIGHT = 480;

	public OrthographicCamera camera;
	public SpriteBatch batch;

	public BitmapFont font24;
	public BitmapFont font;
	public AssetManager assets;
	public LoadingScreen loadingScreen;
	public SplashScreen splashScreen;
	public MainMenuScreen mainMenuScreen;
	public PlayScreen playScreen;
	public OptionScreen optionScreen;

	@Override
	public void create () {

		assets = new AssetManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		batch = new SpriteBatch();
		font = new BitmapFont();
		//initFonts();

		loadingScreen = new LoadingScreen(this);
		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		playScreen = new PlayScreen(this);
		optionScreen = new OptionScreen(this);

		//this.setScreen(new LoadingScreen(this));
		getUSMInstance().push(this.loadingScreen);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(149f / 255f, 165f / 255f, 166f / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		getUSMInstance().updateAndRender();
	}
	
	@Override
	public void dispose () {
		// FOR LATER USE
	}
}
