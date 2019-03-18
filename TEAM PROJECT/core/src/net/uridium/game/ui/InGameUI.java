package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.PlayerPowerupData;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class InGameUI {
    Texture bg;
    Texture border;

    Texture hpBg;
    Texture hp;
    Texture xp;
    Texture powerup;

    BitmapFont font;
    GlyphLayout gl;

    BitmapFont largeFont;
    float textDuration = 0;
    float maxTextDuration = 0;
    String text = "INCREASED SPEED";
    boolean showingText = false;

    float defaultXOffset = 160;
    float xOffset;

    float gridYOffset;

    float powerupDuration;
    float powerupRemaining;

    public InGameUI(float gridYOffset) {
        bg = new Texture(Gdx.files.internal("scoreboard_bg.png"));
        border = new Texture(Gdx.files.internal("border.png"));

        hpBg = new Texture(Gdx.files.internal("hpBg.png"));
        hp = new Texture(Gdx.files.internal("hp.png"));
        xp = new Texture(Gdx.files.internal("xp.png"));
        powerup = new Texture(Gdx.files.internal("powerup.png"));

        this.gridYOffset = gridYOffset;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("newFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;
        font = generator.generateFont(parameter);
        parameter.size = 50;
        largeFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public void update(float delta) {
        if(powerupRemaining > 0) powerupRemaining -= delta;
        if(showingText) {
            textDuration -= delta;
            float scale = (maxTextDuration - textDuration) * 5;
            largeFont.getData().setScale(scale > 1 ? 1 : scale);

            if(textDuration < 0) showingText = false;
        }
    }

    public void updatePowerup(PlayerPowerupData playerPowerupData) {
        this.powerupDuration = playerPowerupData.duration;
        this.powerupRemaining = playerPowerupData.duration;

        showExpandingText(playerPowerupData.powerup.getText(), 0.8f);
    }

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
        if(showingText) drawExpandingText(batch);
    }

    public void showExpandingText(String text, float textDuration) {
        showingText = true;
        this.text = text;
        this.textDuration = textDuration;
        this.maxTextDuration = textDuration;
    }

    public void drawHealthBar(SpriteBatch batch, float percentage, float xOffset) {
        batch.draw(hpBg, xOffset, 645 + (75 - 24) / 2, 200, 24);
        batch.draw(hp, xOffset + 4, 645 + (75 - 20) / 2, (200 - 8) * (percentage / 100), 20);
    }

    public void drawXpBar(SpriteBatch batch, float percentage, float xOffset) {
        batch.draw(hpBg, xOffset, 645 + (75 - 24) / 2, 200, 24);
        batch.draw(xp, xOffset + 4, 645 + (75 - 20) / 2, (200 - 8) * (percentage / 100), 20);
    }

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

    public void drawPowerupBar(SpriteBatch batch) {
        if(powerupRemaining > 0) {
            batch.draw(hpBg, (GAME_WIDTH - 360) / 2, (gridYOffset - 48) / 2, 360, 48);
            batch.draw(powerup, (GAME_WIDTH - 360) / 2 + 8, (gridYOffset - 48) / 2 + 8, 344 * (powerupRemaining / powerupDuration), 32);
        }
    }

    public void drawExpandingText(SpriteBatch batch) {
        gl = new GlyphLayout(largeFont, text);
        largeFont.draw(batch, text, (GAME_WIDTH - gl.width) / 2, (GAME_HEIGHT - 80 + gl.height) / 2);
    }
}
