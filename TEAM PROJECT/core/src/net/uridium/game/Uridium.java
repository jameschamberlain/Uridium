package net.uridium.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.uridium.game.screen.LoadingScreen;
import net.uridium.game.util.Assets;

import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

/**
 * Main game class
 */
public class Uridium extends ApplicationAdapter {

	/**
	 * The width of the game window
	 */
	public static final int GAME_WIDTH = 1280;

	/**
	 * The height of the game window
	 */
	public static final int GAME_HEIGHT = 720;

	@Override
	public void create () {
		getUSMInstance().push(new LoadingScreen());
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

	/**
	 * Sets the cursor image of the game
	 * @param cursor The file name of the cursor image
	 * @param xHotspot See {@link com.badlogic.gdx.Graphics#newCursor(Pixmap, int, int)}
	 * @param yHotspot See {@link com.badlogic.gdx.Graphics#newCursor(Pixmap, int, int)}
	 */
	public static void setCursor(String cursor, int xHotspot, int yHotspot) {
		TextureRegion cursorImage = new TextureRegion(Assets.getAssets().getManager().get(cursor, Texture.class));
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

