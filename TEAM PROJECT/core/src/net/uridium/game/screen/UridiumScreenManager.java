package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;

import java.util.LinkedList;

public class UridiumScreenManager {
    private static UridiumScreenManager instance = new UridiumScreenManager();
    public static UridiumScreenManager getUSMInstance() { return instance; }

    private LinkedList<UridiumScreen> screens;

    private UridiumScreenManager() {
        screens = new LinkedList<UridiumScreen>();
    }

    public void push(UridiumScreen screen) {
        screens.push(screen);
    }

    public void pop() {
        screens.pop();
    }

    public void clearAndSet(UridiumScreen screen) {
        screens.clear();
        screens.push(screen);
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

    public UridiumScreen peek() {
        return screens.peek();
    }
}
