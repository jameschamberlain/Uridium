package net.uridium.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.uridium.game.screen.GameScreen;

import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;


public class Uridium extends ApplicationAdapter {
	public static final int GAME_WIDTH = 1280;
	public static final int GAME_HEIGHT = 720;

	@Override
	public void create () {
<<<<<<< HEAD

		TextureRegion cursorImage = new TextureRegion(new Texture(Gdx.files.internal("cursor.png")));
		cursorImage.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		if (!cursorImage.getTexture().getTextureData().isPrepared()) {
			cursorImage.getTexture().getTextureData().prepare();
		}
		Pixmap pixmap = cursorImage.getTexture().getTextureData().consumePixmap();

		Cursor c = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(c);
		pixmap.dispose();

		getUSMInstance().push(new GameScreen());
=======
		getUSMInstance().push(new MenuScreen());
>>>>>>> 4523ee12b167edf48ea973e7be25ed3990583e85
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(149f / 255f, 165f / 255f, 166f / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		getUSMInstance().updateAndRender();
	}

	@Override
	public void dispose () {

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
