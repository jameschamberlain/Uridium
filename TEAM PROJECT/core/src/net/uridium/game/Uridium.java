package net.uridium.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.uridium.game.screen.MenuScreen;


import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;


public class Uridium extends Game {
	public static int GAME_WIDTH = 1280;
	public static int GAME_HEIGHT = 720;

	public static Sound sound;

	public static Music music;

	@Override
	public void create () {

		music = Gdx.audio.newMusic(Gdx.files.internal("audio\\background.wav"));
		music.play();

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
