package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import net.uridium.game.gameplay.entity.damageable.Player;

import static net.uridium.game.Uridium.GAME_WIDTH;

public class InGameUI {
    Texture bg;
    Texture border;

    Texture hpBg;
    Texture hp;
    Texture xp;

    BitmapFont font;
    GlyphLayout gl;

    float defaultXOffset = 160;
    float xOffset;

    public InGameUI() {
        bg = new Texture(Gdx.files.internal("scoreboard_bg.png"));
        border = new Texture(Gdx.files.internal("border.png"));

        hpBg = new Texture(Gdx.files.internal("hpBg.png"));
        hp = new Texture(Gdx.files.internal("hp.png"));
        xp = new Texture(Gdx.files.internal("xp.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("newFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    public void render(SpriteBatch batch, Player player) {
        xOffset = defaultXOffset;

        batch.draw(bg, 0, 645, 1280, 75);
        batch.draw(border, 0, 640, 1280, 5);

        drawRightSide(batch, player, xOffset);

        gl = new GlyphLayout(font, "HP:");
        font.draw(batch, "HP:", xOffset, 645 + (75 + gl.height) / 2);
        xOffset += gl.width + 15;

        drawHealthBar(batch, (player.getHealth() * 100) / player.getMaxHealth(), xOffset);
        xOffset += 230;
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
        drawXpBar(batch, 60, xOffset);

        // DRAW XP LABEL
//        gl = new GlyphLayout(font, "XP:");
//        xOffset -= (gl.width + 15);
//        font.draw(batch, "XP:", xOffset, 645 + (75 + gl.height) /2 );

        // DRAW PLAYER LEVEL
        gl = new GlyphLayout(font, "LVL 1:");
        xOffset -= (gl.width + 15);
        font.draw(batch, "LVL 1:", xOffset, 645 + (75 + gl.height) / 2);
    }
}
