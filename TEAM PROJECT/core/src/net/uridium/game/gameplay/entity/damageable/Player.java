package net.uridium.game.gameplay.entity.damageable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

import static net.uridium.game.res.Textures.*;

public class Player extends DamageableEntity {
    public enum Colour {
        GREEN,
        PINK,
        YELLOW,
        BLUE
    }

    public enum POWERUP {
        NONE(""),
        FASTER_SHOOTING("DECREASED RELOAD TIME"),
        FASTER_MOVING("INCREASED SPEED");

        String text;

        POWERUP(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    private Colour colour;
    private int score = 0;

    private long lastShot = 0;
    private long reloadTime = 250;

    private float speed = 200;

    private boolean scoreChanged = false;

    private transient Animation<TextureRegion> animUp;
    private transient Animation<TextureRegion> animDown;
    private transient Animation<TextureRegion> animLeft;
    private transient Animation<TextureRegion> animRight;

    private transient TextureRegion[][] frames;
    private transient float stateTime;

    private int level = 1;
    private float xp;
    private float xpToLevelUp = 5;

    private boolean isLevelledUp = false;

    private POWERUP powerup = POWERUP.NONE;
    private float powerupDuration;

    public Player(int ID, Vector2 spawn, Colour colour) {
        this(ID, spawn, 100, 100, colour);
    }

    public Player(int ID, Vector2 spawn, int maxHealth, int health, Colour colour) {
        super(ID, new Rectangle(spawn.x, spawn.y, 30, 40), new Vector2(), "yellow", maxHealth, health);

        this.colour = colour;
    }

    public void shoot() {
        lastShot = System.currentTimeMillis();
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShot > getReloadTime();
    }

    public long getReloadTime() {
        if (powerup == POWERUP.FASTER_SHOOTING)
            return 100;

        return reloadTime;
}

    public float getSpeed() {
        if(powerup == POWERUP.FASTER_MOVING)
            return 300;

        return speed;
    }

    public Colour getColour() {
        return colour;
    }

    public void setMovementDir(float x, float y) {
        setVelocity(getSpeed() * x, getSpeed() * y);
    }

    public float getHealthPercentage() {
        return 100 * health / maxHealth;
    }

    public float getXpPercentage() {
        return 100 * xp / xpToLevelUp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void levelUp() {
        level++;
        xp -= xpToLevelUp;
        xpToLevelUp += 2.5f;
        isLevelledUp = true;
    }

    public void setXp(float xp) {
        this.xp = xp;
        if (xp >= xpToLevelUp)
            levelUp();
    }

    public void setXpToLevelUp(float xpToLevelUp) {
        this.xpToLevelUp = xpToLevelUp;
    }

    public void addXp(float xp) {
        setXp(this.xp + xp);
    }

    public int getLevel() {
        return level;
    }

    public float getXp() {
        return xp;
    }

    public float getXpToLevelUp() {
        return xpToLevelUp;
    }

    public void addScore(int s) {
        score += s;
        scoreChanged = true;
    }

    public void setScore(int s) {
        score = s;
        scoreChanged = true;
    }

    @Override
    public void loadTexture() {
        Gdx.app.postRunnable(() -> {
            t = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_STANDING)));
            frames = new TextureRegion[4][2];

            frames[0][0] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_WALKING_1)));
            frames[0][0].flip(true, false);
            frames[0][1] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_WALKING_2)));
            frames[0][1].flip(true, false);

            frames[1][0] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_WALKING_1)));
            frames[1][1] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_WALKING_2)));

            frames[2][0] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_UP_1)));
            frames[2][1] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_UP_2)));

            frames[3][0] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_DOWN)));
            frames[3][1] = new TextureRegion(Assets.getTex((PLAYER_PATH + colour.toString() + PLAYER_STANDING)));
            frames[3][1].flip(true, false);

            animLeft = new Animation<TextureRegion>(0.1f, frames[0]);
            animRight = new Animation<TextureRegion>(0.1f, frames[1]);
            animUp = new Animation<TextureRegion>(0.1f, frames[2]);
            animDown = new Animation<TextureRegion>(0.1f, frames[3]);
        });

    }

    public boolean isScoreChanged() {
        return scoreChanged;
    }

    public void setScoreChangedFalse() {
        scoreChanged = false;
    }

    public boolean isLevelledUp() {
        return isLevelledUp;
    }

    public void setLevelledUpFalse() {
        isLevelledUp = false;
    }

    public int getScore() {
        return score;
    }

    public POWERUP getPowerup() {
        return powerup;
    }

    public boolean isPoweredUp() {
        return powerupDuration > 0;
    }

    public void setPowerup(POWERUP powerup, float powerupDuration) {
        this.powerup = powerup;
        this.powerupDuration = powerupDuration;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        stateTime += delta;

        if (powerup != POWERUP.NONE) {
            powerupDuration -= delta;

            if (powerupDuration <= 0) {
                powerup = POWERUP.NONE;
                System.out.println("powerup over");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (animRight != null) {
            if (vel.x > 0) {
                batch.draw(animRight.getKeyFrame(stateTime, true), body.x, body.y, body.width, body.height);
            } else if (vel.x < 0) {
                batch.draw(animLeft.getKeyFrame(stateTime, true), body.x, body.y, body.width, body.height);
            } else if (vel.y > 0) {
                batch.draw(animUp.getKeyFrame(stateTime, true), body.x, body.y, body.width, body.height);
            } else if (vel.y < 0) {
                batch.draw(animDown.getKeyFrame(stateTime, true), body.x, body.y, body.width, body.height);
            } else {
                batch.draw(t, body.x, body.y, body.width, body.height);
            }
        }
    }
}


