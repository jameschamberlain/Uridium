package net.uridium.game.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Dewa on 3/7/2017.
 */
public class MyAssetManager {
    public final AssetManager manager = new AssetManager();

    public void queueAddSkin(){
        SkinParameter parameter = new SkinParameter("skin/glassy-ui.atlas");
        manager.load("skin/glassy-ui.json" ,Skin.class,parameter);
    }
}
