package net.uridium.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.uridium.game.screen.GameScreen;
import net.uridium.game.screen.MenuScreen;
//import net.uridium.game.screen.SplashScreen;
//import net.uridium.game.screen;
import net.uridium.game.util.Audio;
import net.uridium.game.util.Audio.*;

import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;


public class Uridium extends Game {
	public static final int GAME_WIDTH = 1280;
	public static final int GAME_HEIGHT = 720;


	@Override
	public void create () {
		//
		getUSMInstance().push(new MenuScreen());
		//getUSMInstance().push(new SplashScreen());
		//this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(149f / 255f, 165f / 255f, 166f / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		getUSMInstance().updateAndRender();
	}

	@Override
	public void dispose () {
		// FOR LATER USE

	}

	public static void setCursor(String cursor, int xHotspot, int yHotspot) {
		TextureRegion cursorImage = new TextureRegion(new Texture(Gdx.files.internal(cursor)));
		cursorImage.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		if (!cursorImage.getTexture().getTextureData().isPrepared()) {
			cursorImage.getTexture().getTextureData().prepare();
		}
		Pixmap pixmap = cursorImage.getTexture().getTextureData().consumePixmap();

		Cursor c = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
		Gdx.graphics.setCursor(c);
		pixmap.dispose();
	}
}
