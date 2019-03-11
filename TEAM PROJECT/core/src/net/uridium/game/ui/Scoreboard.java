package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Scoreboard {
    HashMap<Integer, Integer> scores;

    BitmapFont myFont;
    Texture tex;
    GlyphLayout glyphLayout;

    float width = 1000;
    float height = 400;

    float xOffset;
    float yOffset;

    public Scoreboard() {
        myFont = new BitmapFont(Gdx.files.internal("arial.fnt"));
        tex = new Texture(Gdx.files.internal("scoreboard_bg.png"));

        xOffset = (GAME_WIDTH - width) / 2;
        yOffset = (GAME_HEIGHT - height) / 2;

        scores = new HashMap<>();
    }

    public void setScore(int player, int score) {
        scores.put(player, score);
    }

    public void render(SpriteBatch batch) {
        batch.draw(tex, xOffset, yOffset, width, height);

        int i = 1;
        for(Map.Entry<Integer, Integer> playerScore : scores.entrySet()) {
            String text = "" + playerScore.getValue();
            glyphLayout = new GlyphLayout(myFont, text);
            float y = yOffset + height - (100 * i) + 50 + glyphLayout.height / 2;

            myFont.draw(batch, "Player " + playerScore.getKey(), xOffset + 50, y);
            myFont.draw(batch, text, xOffset + width - 50 - glyphLayout.width, y);
            i++;
        }

//        myFont.draw(batch, "YOU'RE DEAD BITCH",500,500);
    }
}
