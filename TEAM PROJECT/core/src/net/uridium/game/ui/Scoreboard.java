package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.util.Assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

/**
 * The type Scoreboard.
 */
public class Scoreboard {
    /**
     * The Datas.
     */
    CopyOnWriteArrayList<ScoreboardData> datas;

    /**
     * The Font.
     */
    BitmapFont font;
    /**
     * The Tex.
     */
    Texture tex;
    /**
     * The Glyph layout.
     */
    GlyphLayout glyphLayout;
    /**
     * The Renderer.
     */
    ShapeRenderer renderer;

    /**
     * The Width.
     */
    float width = 1000;
    /**
     * The Height.
     */
    float height = 400;

    /**
     * The X offset.
     */
    float xOffset;
    /**
     * The Y offset.
     */
    float yOffset;

    /**
     * The Blue icon.
     */
    Texture blueIcon;
    /**
     * The Green icon.
     */
    Texture greenIcon;
    /**
     * The Yellow icon.
     */
    Texture yellowIcon;
    /**
     * The Pink icon.
     */
    Texture pinkIcon;

    /**
     * The Hp.
     */
    Texture hp;
    /**
     * The Hp bg.
     */
    Texture hpBg;

    /**
     * Instantiates a new Scoreboard.
     */
    public Scoreboard() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("newFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();
        tex = Assets.getTex("graphics/ui/scoreboard_bg.png");
        hpBg = Assets.getTex("graphics/ui/hpBg.png");
        hp = Assets.getTex("graphics/ui/hp.png");

        renderer = new ShapeRenderer();

        xOffset = (GAME_WIDTH - width) / 2;
        yOffset = (GAME_HEIGHT - height) / 2;

        datas = new CopyOnWriteArrayList<>();
        blueIcon = Assets.getTex(("graphics/entity/player/BLUE/icon.png"));
        pinkIcon = Assets.getTex(("graphics/entity/player/PINK/icon.png"));
        greenIcon = Assets.getTex(("graphics/entity/player/GREEN/icon.png"));
        yellowIcon = Assets.getTex(("graphics/entity/player/YELLOW/icon.png"));
    }

    /**
     * Update scoreboard.
     *
     * @param players the players
     */
    public void updateScoreboard(ArrayList<Player> players) {
        for(int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            ScoreboardData data = new ScoreboardData(p.getColour(), "Player " + (i+1), p.getHealthPercentage(), p.getLevel(), p.getScore());

            if(i >=  datas.size())
                datas.add(data);
            else
                datas.set(i, data);
        }
    }

    /**
     * Render.
     *
     * @param batch the batch
     */
    public void render(SpriteBatch batch) {
        batch.draw(tex, xOffset, yOffset, width, height);

        int i = 1;
        for(ScoreboardData data : datas) {
            float y = yOffset + height - (100 * i);
            float xOffsetOffset = 0;

            xOffsetOffset += 50;

            batch.draw(getIcon(data.playerColour), xOffset + xOffsetOffset, y + 25, 50, 50);

            xOffsetOffset += 50 + 50;
            glyphLayout = new GlyphLayout(font, data.playerName);
            font.draw(batch, data.playerName, xOffset + xOffsetOffset, y + 50 + glyphLayout.height / 2);

            xOffsetOffset += 200 + 50;
            drawHealthBar(batch, data.healthPercent, xOffset + xOffsetOffset, y + 36);

            glyphLayout = new GlyphLayout(font, "" + data.score);
            font.draw(batch, "" + data.score, xOffset + width - 50 - glyphLayout.width, y + 50 + glyphLayout.height / 2);
            i++;

            batch.end();
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.BLUE);
            renderer.rect(xOffset, y, width, 100);
            renderer.end();
            batch.begin();
        }
    }

    private void drawHealthBar(SpriteBatch batch, float percentage, float x, float y) {
        batch.draw(hpBg, x, y, 200, 28);
        batch.draw(hp, x + 4, y + 4, (200f - 8f) * (percentage / 100), 20);
    }

    private Texture getIcon(Player.Colour colour) {
        switch(colour) {
            case GREEN:
                return greenIcon;
            case PINK:
                return pinkIcon;
            case YELLOW:
                return yellowIcon;
            case BLUE:
                return blueIcon;
            default:
                return null;
        }
    }

    /**
     * The type Scoreboard data.
     */
    class ScoreboardData {
        /**
         * The Player colour.
         */
        Player.Colour playerColour;
        /**
         * The Player name.
         */
        String playerName;
        /**
         * The Health percent.
         */
        float healthPercent;
        /**
         * The Level.
         */
        int level;
        /**
         * The Score.
         */
        int score;

        /**
         * Instantiates a new Scoreboard data.
         *
         * @param playerColour  the player colour
         * @param playerName    the player name
         * @param healthPercent the health percent
         * @param level         the level
         * @param score         the score
         */
        public ScoreboardData(Player.Colour playerColour, String playerName, float healthPercent, int level, int score) {
            this.playerColour = playerColour;
            this.playerName = playerName;
            this.healthPercent = healthPercent;
            this.level = level;
            this.score = score;
        }
    }
}
