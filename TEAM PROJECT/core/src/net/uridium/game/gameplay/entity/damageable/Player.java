package net.uridium.game.gameplay.entity.damageable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.util.Assets;

import static net.uridium.game.res.Textures.*;

/**
 * The player class, controlled by the user
 */
public class Player extends DamageableEntity implements Comparable<Player> {

    /**
     * Four different colours for the four different characters in the game
     */
    public enum Colour {
        GREEN,
        PINK,
        YELLOW,
        BLUE
    }

    /**
     * Different types of powerups the user can have active
     */
    public enum POWERUP {
        NONE(""),
        FASTER_SHOOTING("DECREASED RELOAD TIME"),
        FASTER_MOVING("INCREASED SPEED");

        /**
         * Text to be shown to the user when the powerup is first acquired
         */
        String text;

        POWERUP(String text) {
            this.text = text;
        }

        /**
         * @return Returns the text of the powerup
         */
        public String getText() {
            return text;
        }
    }

    /**
     * Colour of the player
     */
    private Colour colour;

    /**
     * Score of the player
     */
    private int score = 0;

    /**
     * Time the player last shot a bullet
     */
    private long lastShot = 0;

    /**
     * Minimum time between one shot and the next
     */
    private long reloadTime = 250;

    /**
     * Speed the player moves at
     */
    private float speed = 200;

    /**
     * Boolean to track if the score has been changed (in order to send scores updates to clients)
     */
    private boolean scoreChanged = false;

    /**
     * Rank of the player out of all the players in the game(1st to 4th), defaults to -1 if unset
     */
    private int rank = -1;

    /**
     * Walking up animation
     */
    private transient Animation<TextureRegion> animUp;

    /**
     * Walking down animation
     */
    private transient Animation<TextureRegion> animDown;

    /**
     * Walking left animation
     */
    private transient Animation<TextureRegion> animLeft;

    /**
     * Walkig right animation
     */
    private transient Animation<TextureRegion> animRight;

    /**
     * Used to set up the animations
     */
    private transient TextureRegion[][] frames;

    /**
     * Used to track what part of an animation is currently rendered
     */
    private transient float stateTime;

    /**
     * Level of the player
     */
    private int level = 1;

    /**
     * Current xp
     */
    private float xp;

    /**
     * Xp needed to reach before level up
     */
    private float xpToLevelUp = 5;

    /**
     * Set to true on level up (in order to send updates to clients, is set back to false after this is checked)
     */
    private boolean isLevelledUp = false;

    /**
     * Current powerup of the player
     */
    private POWERUP powerup = POWERUP.NONE;

    /**
     * Remaining duration of a powerup
     */
    private float powerupDuration;

    /**
     * Stripped down constructor of player, with health and maxhealth defaulting to 100, see {@link Player#Player(int, Vector2, int, int, Colour)}
     */
    public Player(int ID, Vector2 spawn, Colour colour) {
        this(ID, spawn, 100, 100, colour);
    }

    /**
     * Player constructor
     * @param ID The entity id
     * @param spawn The spawn position
     * @param maxHealth The max health
     * @param health The health
     * @param colour The colour of the player (what character)
     */
    public Player(int ID, Vector2 spawn, int maxHealth, int health, Colour colour) {
        super(ID, new Rectangle(spawn.x, spawn.y, 40, 55), new Vector2(), "yellow", maxHealth, health);

        this.colour = colour;
    }

    /**
     * Updates the last show time to now
     */
    public void shoot() {
        lastShot = System.currentTimeMillis();
    }

    /**
     * Returns true if the player can shoot
     * @return <code>true</code> if the time between now and the last show is greater than the reload time, <code>false</code> otherwise
     */
    public boolean canShoot() {
        return System.currentTimeMillis() - lastShot > getReloadTime();
    }

    /**
     * @return The current reload time of the player
     */
    public long getReloadTime() {
        if (powerup == POWERUP.FASTER_SHOOTING)
            return 100;

        return reloadTime;
    }

    /**
     * @return The current speed of the player
     */
    public float getSpeed() {
        if(powerup == POWERUP.FASTER_MOVING)
            return 300;

        return speed;
    }

    /**
     * @return The colour of the player
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Sets the velocity of the player, with the x velocity being set to the given x value multiplied by the speed, and the y velocity being set to the given y value multiplied by the speed
     * @param x The x value to multiply the speed by
     * @param y The y value to multiply the speed b
     */
    public void setMovementDir(float x, float y) {
        setVelocity(getSpeed() * x, getSpeed() * y);
    }

    /**
     * @return The percentage of health the player currently has
     */
    public float getHealthPercentage() {
        return 100 * health / maxHealth;
    }

    /**
     * @return The percentage of xp gained towards the next level up
     */
    public float getXpPercentage() {
        return 100 * xp / xpToLevelUp;
    }

    /**
     * @param level The level to set the player
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Called when the play has enough xp to level up, levels up the player stats
     */
    public void levelUp() {
        level++;
        xp -= xpToLevelUp;
        xpToLevelUp += 2.5f;
        isLevelledUp = true;

        float addedHealth = maxHealth * 0.2f;
        maxHealth += addedHealth;
        heal(addedHealth);
    }

    /**
     * @param xp The new xp of the player
     */
    public void setXp(float xp) {
        this.xp = xp;
        if (xp >= xpToLevelUp)
            levelUp();
    }

    /**
     * @param xpToLevelUp The new amount of xp needed to level up
     */
    public void setXpToLevelUp(float xpToLevelUp) {
        this.xpToLevelUp = xpToLevelUp;
    }

    /**
     * Adds xp to the player
     * @param xp The amount of xp to add
     */
    public void addXp(float xp) {
        setXp(this.xp + xp);
    }

    /**
     * @return The level of the player
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return The amount of xp the player has
     */
    public float getXp() {
        return xp;
    }

    /**
     * @return The amount of xp needed to get from 0xp on the current level, to the next level
     */
    public float getXpToLevelUp() {
        return xpToLevelUp;
    }

    /**
     * Adds score to the player
     * @param s The amount of score to add to the player
     */
    public void addScore(int s) {
        score += s;
        scoreChanged = true;
    }

    /**
     * @param s The new score of the player
     */
    public void setScore(int s) {
        score = s;
        scoreChanged = true;
    }

    /**
     * Loads the textures and creates animations for the player using the player's colour
     */
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

    /**
     * @deprecated not used anymore, remains just in case
     * @return Whether the score has been changed
     */
    public boolean isScoreChanged() {
        return scoreChanged;
    }

    /**
     * @deprecated not used anymore, remains just in case
     * Sets the score changed boolean to false
     */
    public void setScoreChangedFalse() {
        scoreChanged = false;
    }

    /**
     * @return <code>true</code> if the player has levelled up recently, <code>false</code> otherwise
     */
    public boolean isLevelledUp() {
        return isLevelledUp;
    }

    /**
     * Sets the levelled up boolean to false
     */
    public void setLevelledUpFalse() {
        isLevelledUp = false;
    }

    /**
     * @return The score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the current powerup of the player
     * @param powerup The powerup type
     * @param powerupDuration The powerup duration
     */
    public void setPowerup(POWERUP powerup, float powerupDuration) {
        this.powerup = powerup;
        this.powerupDuration = powerupDuration;
    }

    /**
     * @return The rank of the player
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank The new rank of the player
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        stateTime += delta;

        if (powerup != POWERUP.NONE) {
            powerupDuration -= delta;

            if (powerupDuration <= 0)
                powerup = POWERUP.NONE;
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

    /**
     * Compares the score of one player to another
     * @param o The player to compare this player to
     * @return Greater than 0 if this player has a higher score, less than 0 if the other player has a higher score, and 0 if the scores are even
     */
    @Override
    public int compareTo(Player o) {
        return score - o.getScore();
    }
}


