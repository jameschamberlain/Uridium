package net.uridium.game.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import net.uridium.game.gameplay.entity.damageable.Player;

import static net.uridium.game.util.Audio.SOUND.*;
import static net.uridium.game.util.Audio.SOUND.VICTORY;

public class Assets {
    /**
     * Image used on the splash screen.
     */
    public static final String SPLASH_BACKGROUND = "graphics/ui/splash/background.png";
    public static final String SPLASH_BACKGROUND2 = "graphics/ui/splash/background2.png";
    /**
     * Image used on the splash screen.
     */
    public static final String SPLASH_FOREGROUND = "graphics/ui/splash/foreground.png";
    public static final String SPLASH_FOREGROUND2 = "graphics/ui/splash/foreground2.png";

    /**
     * Background tile used in the menus and in-game.
     */
    public static final String BACKGROUND = "graphics/tile/iceWaterDeepStars.png";

    /**
     * Default skin in the game.
     */
    public static final String SKIN = "graphics/ui/skin/freezing-ui.json";

    /**
     * Cursor
     */
    public static final String MENU_CURSOR = "graphics/ui/cursor.png";

    /**
     * Cursor used in-game.
     */
    public static final String GAME_CURSOR = "graphics/ui/crosshair_white.png";


    private static Assets assets = new Assets();
    public static Assets getAssets() {
        return assets;
    }
    private Assets() {}

    AssetManager manager;

    public void init() {
        manager = new AssetManager();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public AssetManager getManager() {
        return manager;
    }

    public static Texture getTex(String asset) {
        return get(asset, Texture.class);
    }

    public static <T> T get(String asset, Class<T> t) {
        return getAssets().getManager().get(asset, t);
    }

    public void loadAssets() {
        Assets.getAssets().init();

        // FONT
        FreetypeFontLoader.FreeTypeFontLoaderParameter bigFontParam = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        bigFontParam.fontFileName = "graphics/ui/font/Big_Bottom_Typeface_Normal.ttf";
        bigFontParam.fontParameters.size = 60;
        Assets.getAssets().getManager().load("bigFont.ttf", BitmapFont.class, bigFontParam);

        FreetypeFontLoader.FreeTypeFontLoaderParameter smallFontParam = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallFontParam.fontFileName = "graphics/ui/font/Big_Bottom_Typeface_Normal.ttf";
        smallFontParam.fontParameters.size = 20;
        smallFontParam.fontParameters.color = Color.WHITE;
        Assets.getAssets().getManager().load("smallFont.ttf", BitmapFont.class, smallFontParam);

        Assets.getAssets().getManager().finishLoadingAsset("bigFont.ttf");
        Assets.getAssets().getManager().finishLoadingAsset("smallFont.ttf");

        // SKIN
        ObjectMap<String, Object> fonts = new ObjectMap<String, Object>();
        fonts.put("bigFont", Assets.getAssets().getManager().get("bigFont.ttf"));
        fonts.put("smallFont", Assets.getAssets().getManager().get("smallFont.ttf"));
        SkinLoader.SkinParameter parameter = new SkinLoader.SkinParameter("graphics/ui/skin/freezing-ui.atlas", fonts);
        Assets.getAssets().getManager().load("graphics/ui/skin/freezing-ui.json" , Skin.class, parameter);

        // LOAD TEXTURES
        // TILE
        Assets.getAssets().getManager().load("graphics/tile/tundraCenter.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/tile/igloo.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/tile/DOORBOI.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/tile/iceBlockAlt.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/tile/iceBlock.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/tile/obstacle.png", Texture.class);

        // ENTITY
        Assets.getAssets().getManager().load("graphics/entity/spawner.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/projectile/wrench.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/item/heal.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/item/coffee.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/item/orange_beaker.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/piranha.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/fishGreen.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/fishGreen_swim.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/fishPink.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/fishPink_swim.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/spider_walk1.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/spider_walk2.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/ghost.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/ghost_normal.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/bat.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/bat_fly.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/entity/enemy/slimeBlue_squashed.png", Texture.class);

        for(Player.Colour c : Player.Colour.values()) {
            Assets.getAssets().getManager().load("graphics/entity/player/" + c.toString() + "/walk1.png", Texture.class);
            Assets.getAssets().getManager().load("graphics/entity/player/" + c.toString() + "/walk2.png", Texture.class);
            Assets.getAssets().getManager().load("graphics/entity/player/" + c.toString() + "/up1.png", Texture.class);
            Assets.getAssets().getManager().load("graphics/entity/player/" + c.toString() + "/up2.png", Texture.class);
            Assets.getAssets().getManager().load("graphics/entity/player/" + c.toString() + "/down.png", Texture.class);
            Assets.getAssets().getManager().load("graphics/entity/player/" + c.toString() + "/stand.png", Texture.class);
            Assets.getAssets().getManager().load("graphics/entity/player/" + c.toString() + "/icon.png", Texture.class);
        }

        // UI
        Assets.getAssets().getManager().load(BACKGROUND, Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/crosshair_white.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/cursor.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/hp.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/xp.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/hpBg.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/scoreboard_bg.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/powerup.png", Texture.class);
        Assets.getAssets().getManager().load("graphics/ui/border.png", Texture.class);

        // AUDIO
        Assets.getAssets().getManager().load("audio/background.wav", Music.class);
        Assets.getAssets().getManager().load("audio/BUTTON_CLICK.wav", Sound.class);
        Assets.getAssets().getManager().load("audio/PLAYER_SHOOT.wav", Sound.class);
        Assets.getAssets().getManager().load("audio/PLAYER_DAMAGE.wav", Sound.class);
        Assets.getAssets().getManager().load("audio/PLAYER_DEAD.wav", Sound.class);
        Assets.getAssets().getManager().load("audio/ENEMY_DEAD.wav", Sound.class);
        Assets.getAssets().getManager().load("audio/CHANGE_ROOM.ogg", Sound.class);
        Assets.getAssets().getManager().load("audio/GAME_OVER.wav", Sound.class);
//        Assets.getAssets().getManager().load("audio/VICTORY.wav", Sound.class);
        Assets.getAssets().getManager().load("audio/POWERUP.wav", Sound.class);
    }
}
