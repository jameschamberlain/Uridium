package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.PlayerPowerupData;
import net.uridium.game.util.Assets;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

/**
 * UI rendered in game, such as health bar and xp bar
 */
public class InGameUI {
    /**
     * Texture for the background of the ui at the top of the screen
     */
    Texture bg;

    /**
     * Border texture of  ui at the top of the screen
     */
    Texture border;

    /**
     * Background of the status bars (hp, xp, etc.)
     */
    Texture hpBg;

    /**
     * Texture of the hp bar
     */
    Texture hp;

    /**
     * Texture of the xp bar
     */
    Texture xp;

    /**
     * Texture of the powerup bar
     */
    Texture powerup;

    /**
     * Font used to draw text to screen
     */
    BitmapFont font;

    /**
     * GlyphLayout used for positioning text correctly
     */
    GlyphLayout gl;

    /**
     * Large font used for drawing bigger text
     */
    BitmapFont largeFont;

    /**
     * How long to show big text for
     */
    float textDuration = 0;

    /**
     * Max duration text is shown for
     */
    float maxTextDuration = 0;

    /**
     * Text to show on screen
     */
    String text = "INCREASED SPEED";

    /**
     * Whether the ui is currently drawing text on the screen
     */
    boolean showingText = false;

    /**
     * Whether the text being drawn on the screen should stay there indefinitely
     */
    boolean infiniteText = false;

    /**
     * Horizontal distance between edge of the screen and the start of the ui elements
     */
    float defaultXOffset = 160;

    /**
     * Used to progressively draw elements of UI at the correct offset
     */
    float xOffset;

    /**
     * Used to draw the powerup bar in the correct place, the yOffset of the grid
     */
    float gridYOffset;

    /**
     * Max duration of current powerup (used to workout percentage of the powerup bar)
     */
    float powerupDuration;

    /**
     * Powerup duration remaining
     */
    float powerupRemaining;

    /**
     * Whether the current level in the game is a boss level
     */
    boolean isBossLevel;

    /**
     * Stores the percentage of the boss's hp to render the boss's hp bar
     */
    float bossHpPercent;

    public InGameUI(float gridYOffset) {
        this(gridYOffset, false);
    }

    /**
     * InGameUI constructor
     * @param gridYOffset The yOffset of the grid in the level
     * @param isBossLevel Whether the current level is a boss level
     */
    public InGameUI(float gridYOffset, boolean isBossLevel) {
        bg = Assets.getTex("graphics/ui/scoreboard_bg.png");
        border = Assets.getTex("graphics/ui/border.png");

        hpBg = Assets.getTex("graphics/ui/hpBg.png");
        hp = Assets.getTex("graphics/ui/hp.png");
        xp = Assets.getTex("graphics/ui/xp.png");
        powerup = Assets.getTex("graphics/ui/powerup.png");

        this.gridYOffset = gridYOffset;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("graphics/ui/font/Big_Bottom_Typeface_Normal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        font = generator.generateFont(parameter);
        parameter.size = 38;
        largeFont = generator.generateFont(parameter);
        generator.dispose();

        this.isBossLevel = isBossLevel;
    }

    /**
     * @param isBossLevel Whether the game is currently in a boss level or not
     */
    public void setBossLevel(boolean isBossLevel) {
        this.isBossLevel = isBossLevel;
    }

    /**
     * @param bossHpPercent The current hp percentage of the boss
     */
    public void setBossHpPercent(float bossHpPercent) {
        this.bossHpPercent = bossHpPercent;
    }

    /**
     * Update UI durations
     * @param delta The time between this update and the last one
     */
    public void update(float delta) {
        if (powerupRemaining > 0) powerupRemaining -= delta;
        if (showingText) {
            if (textDuration > 0) {
                textDuration -= delta;
                float scale = (maxTextDuration - textDuration) * 5;
                largeFont.getData().setScale(scale > 1 ? 1 : scale);
            } else if (textDuration < 0 && !infiniteText) {
                showingText = false;
            }
        }
    }

    /**
     * Updates the current powerup
     * @param playerPowerupData PlayerPowerupData object received from the server
     */
    public void updatePowerup(PlayerPowerupData playerPowerupData) {
        this.powerupDuration = playerPowerupData.duration;
        this.powerupRemaining = playerPowerupData.duration;

        showExpandingText(playerPowerupData.powerup.getText(), 0.8f, false);
    }

    /**
     * Render the ui
     * @param batch The SpriteBatch to render the ui with
     * @param player The player to get data from to render on the ui
     */
    public void render(SpriteBatch batch, Player player) {
        xOffset = defaultXOffset;

        batch.draw(bg, 0, 645, 1280, 75);
        batch.draw(border, 0, 640, 1280, 5);

        drawRightSide(batch, player, xOffset);

        gl = new GlyphLayout(font, "HP:");
        font.draw(batch, "HP:", xOffset, 645 + (75 + gl.height) / 2);
        xOffset += gl.width + 15;

        drawHealthBar(batch, player.getHealthPercentage(), xOffset);
        xOffset += 230;

        drawPowerupBar(batch);
        if (showingText) drawExpandingText(batch);

        if(isBossLevel)
            drawBossHpBar(batch);
    }

    /**
     * Shows some text in the center of the screen which expands from nothing, stays for textDuration unless infiniteText is true
     * @param text The text to show
     * @param textDuration How long to show the text for
     * @param infiniteText Whether the text should be shown indefinitely
     */
    public void showExpandingText(String text, float textDuration, boolean infiniteText) {
        showingText = true;
        this.text = text;
        this.textDuration = textDuration;
        this.maxTextDuration = textDuration;
        this.infiniteText = infiniteText;
    }

    /**
     * Draw the health bar to the screen
     * @param batch The SpriteBatch to render with
     * @param percentage The hp percentage of the player
     * @param xOffset The xOffset to draw the bar at
     */
    public void drawHealthBar(SpriteBatch batch, float percentage, float xOffset) {
        batch.draw(hpBg, xOffset, 645 + (75 - 24) / 2f, 200, 24);
        batch.draw(hp, xOffset + 4, 645 + (75 - 20) / 2f, (200 - 8) * (percentage / 100), 20);
    }

    /**
     * Draw the xp bar to the screen
     * @param batch The SpriteBatch to render with
     * @param percentage The hp percentage of the player
     * @param xOffset The xOffset to draw the bar at
     */
    public void drawXpBar(SpriteBatch batch, float percentage, float xOffset) {
        batch.draw(hpBg, xOffset, 645 + (75 - 24) / 2, 200, 24);
        batch.draw(xp, xOffset + 4, 645 + (75 - 20) / 2, (200 - 8) * (percentage / 100), 20);
    }

    /**
     * Draw the boss hp bar to the screen
     * @param batch The SpriteBatch to render with
     */
    public void drawBossHpBar(SpriteBatch batch) {
        batch.draw(hpBg, 20, (GAME_HEIGHT - 300) / 2f, 60, 300);
        batch.draw(hp, 25, (GAME_HEIGHT - 300) / 2f + 5, 50, 290 * bossHpPercent);
    }

    /**
     * Draws the right side of the top ui
     * @param batch The SpriteBatch to render with
     * @param player The player to get the data from to render the ui
     * @param xOffset The xOffset of the ui
     */
    public void drawRightSide(SpriteBatch batch, Player player, float xOffset) {
        xOffset = GAME_WIDTH - xOffset;

        // DRAW BAR
        xOffset -= 200;
        drawXpBar(batch, player.getXpPercentage(), xOffset);

        // DRAW XP LABEL
//        gl = new GlyphLayout(font, "XP:");
//        xOffset -= (gl.width + 15);
//        font.draw(batch, "XP:", xOffset, 645 + (75 + gl.height) /2 );

        // DRAW PLAYER LEVEL
        String text = "LVL " + player.getLevel() + ":";
        gl = new GlyphLayout(font, text);
        xOffset -= (gl.width + 15);
        font.draw(batch, text, xOffset, 645 + (75 + gl.height) / 2);
    }

    /**
     * Draws the powerup bar
     * @param batch The SpriteBatch to render the bar with
     */
    public void drawPowerupBar(SpriteBatch batch) {
        if (powerupRemaining > 0) {
            batch.draw(hpBg, (GAME_WIDTH - 360) / 2, (gridYOffset - 48) / 2, 360, 48);
            batch.draw(powerup, (GAME_WIDTH - 360) / 2 + 8, (gridYOffset - 48) / 2 + 8, 344 * (powerupRemaining / powerupDuration), 32);
        }
    }

    /**
     * If showing text, renders the current text to screen
     * @param batch The SpriteBatch to render the text with
     */
    public void drawExpandingText(SpriteBatch batch) {
        gl = new GlyphLayout(largeFont, text);
        largeFont.draw(batch, text, (GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT - 80 + gl.height) / 2);
    }
}