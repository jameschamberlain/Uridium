package net.uridium.game.screen;

/**
 * The type Uridium screen.
 */
public abstract class UridiumScreen {
    /**
     * Init.
     */
    public abstract void init();

    /**
     * Update.
     *
     * @param delta the delta
     */
    public abstract void update(float delta);

    /**
     * Render.
     */
    public abstract void render();
}