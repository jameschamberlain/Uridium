package net.uridium.game.screen;

import com.badlogic.gdx.Gdx;

import java.util.LinkedList;

/**
 * The type Uridium screen manager.
 */
public class UridiumScreenManager {
    private static UridiumScreenManager instance = new UridiumScreenManager();

    /**
     * Gets usm instance.
     *
     * @return the usm instance
     */
    public static UridiumScreenManager getUSMInstance() { return instance; }

    private LinkedList<UridiumScreen> screens;

    private UridiumScreenManager() {
        screens = new LinkedList<UridiumScreen>();
    }

    /**
     * Push.
     *
     * @param screen the screen
     */
    public void push(UridiumScreen screen) {
        screens.push(screen);
    }

    /**
     * Pop.
     */
    public void pop() {
        screens.pop();
    }

    /**
     * Clear and set.
     *
     * @param screen the screen
     */
    public void clearAndSet(UridiumScreen screen) {
        screens.clear();
        screens.push(screen);
    }

    /**
     * Set.
     *
     * @param screen the screen
     */
    public void set(UridiumScreen screen) {
        pop();
        push(screen);
    }

    /**
     * Update and render.
     */
    public void updateAndRender() {
        if(Gdx.graphics.getDeltaTime() < 1)
            peek().update(Gdx.graphics.getDeltaTime());

        peek().render();
    }

    /**
     * Peek uridium screen.
     *
     * @return the uridium screen
     */
    public UridiumScreen peek() {
        return screens.peek();
    }
}
