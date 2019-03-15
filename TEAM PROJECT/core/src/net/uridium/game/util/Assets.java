package net.uridium.game.util;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    private static Assets assets = new Assets();
    public static Assets getInstance() {
        return assets;
    }
    private Assets() {}

    AssetManager manager;

    public void init() {
        manager = new AssetManager();
    }

    public AssetManager getManager() {
        return manager;
    }
}
