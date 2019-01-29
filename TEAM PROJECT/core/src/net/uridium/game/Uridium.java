package net.uridium.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import net.uridium.game.screen.TempScreen;

import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class Uridium extends ApplicationAdapter {
	public static final int GAME_WIDTH = 1280;
	public static final int GAME_HEIGHT = 720;

	@Override
	public void create () {
		getUSMInstance().push(new TempScreen());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(149f / 255f, 165f / 255f, 166f / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		getUSMInstance().updateAndRender();
	}
	
	@Override
	public void dispose () {
		//LATERs
	}
}
