package net.uridium.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static net.uridium.game.Uridium.GAME_HEIGHT;


public class HealthBar {

    Texture heart;
    Texture emptyHeart;
    float health;
    float maxHealth = 5;

    public HealthBar(float health, float maxHealth) {
        heart = new Texture(Gdx.files.internal("heart.png"));
        emptyHeart = new Texture(Gdx.files.internal("heartOutline.png"));

        this.health = health;
        this.maxHealth = maxHealth;
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < maxHealth; i++) {
            if(i < health) batch.draw(heart, 20, GAME_HEIGHT - ((20 + 24 * 4/3) * (i+1)), 28 * 4/3, 24 * 4/3);
            else batch.draw(emptyHeart, 20, GAME_HEIGHT - ((20 + 24 * 4/3) * (i+1)), 28 * 4/3, 24 * 4/3);
        }
    }

    public void update(int health){
        this.health = health;
    }

}
