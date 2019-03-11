package net.uridium.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UserInterface extends Game {

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


		this.setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();

	}

	@Override
	public void dispose(){
		batch.dispose();
		font.dispose();
		assets.dispose();
		this.getScreen().dispose();
		loadingScreen.dispose();
		splashScreen.dispose();
		mainMenuScreen.dispose();
		playScreen.dispose();

	}

	/*private void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 24;
		params.color = Color.BLACK;
		font24 = generator.generateFont(params);
	}*/
}
