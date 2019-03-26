package net.uridium.game.util;

public final class Dimensions {
    // Game dimensions.
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;

    // Title dimensions.
    public static final float TITLE_WIDTH = 1280;
    public static final float TITLE_HEIGHT = 360;
    public static final float TITLE_X = 0;
    public static final float TITLE_Y = 360;
    public static final float TITLE_FONT_SCALE = 1.4f;

    // Button dimensions.
    public static final float BUTTON_WIDTH = 340;
    public static final float BUTTON_HEIGHT = 80;
    public static final float BUTTON_X = (GAME_WIDTH - 340) / 2.0f;
    public static final float BUTTON_Y = (GAME_HEIGHT - 80) / 2.0f;
    public static final float BUTTON_GAP = 100;

    // Side button dimensions.
    public static final float SIDE_BUTTON_WIDTH = 255;
    public static final float SIDE_BUTTON_HEIGHT = 80;
    public static final float SIDE_BUTTON_X = (GAME_WIDTH - 170) / 5.0f;
    public static final float SIDE_BUTTON_Y = (GAME_HEIGHT - 10) / 2.0f;
    public static final float SIDE_BUTTON_GAP = 100;

    // Dialog dimensions.
    public static final float DIALOG_WIDTH = GAME_WIDTH / 1.2f;
    public static final float DIALOG_HEIGHT = GAME_HEIGHT / 2.0f;
    public static final float DIALOG_BUTTON_FONT_SCALE = 0.8f;
    public static final float DIALOG_PADDING = 20;
}
