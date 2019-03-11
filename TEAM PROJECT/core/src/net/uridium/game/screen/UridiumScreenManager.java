package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;
import net.uridium.game.Uridium;

import java.util.LinkedList;

public class UridiumScreenManager {
    private static UridiumScreenManager instance = new UridiumScreenManager();
    public static UridiumScreenManager getUSMInstance() { return instance; }

    private LinkedList<Uridium> screens;

    private UridiumScreenManager() {
        screens = new LinkedList<Uridium>();
    }

    public void push(Uridium screen) {
        screens.push(screen);
    }

    public void pop() {
        screens.pop();
    }

    public void set(UridiumScreen screen) {
        pop();
        push(screen);
    }

    public void updateAndRender() {
        if(Gdx.graphics.getDeltaTime() < 1)
            peek().update(Gdx.graphics.getDeltaTime());

        peek().render();
    }


}