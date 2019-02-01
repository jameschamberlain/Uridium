package net.uridium.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.uridium.game.Uridium;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//这里就是在造框
		config.width = GAME_WIDTH;
		config.height = GAME_HEIGHT;
		config.resizable = false;
		config.title = "Uridium";
		//把这个框体传进去
		new LwjglApplication(new Uridium(), config);
	}
}