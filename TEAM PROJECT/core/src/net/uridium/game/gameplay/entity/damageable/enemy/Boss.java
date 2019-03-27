package net.uridium.game.gameplay.entity.damageable.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.ServerLevel;
import net.uridium.game.util.Assets;

import java.util.Timer;
import java.util.TimerTask;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;

public class Boss extends Enemy {

    private float actionTimer = 3;
    private float spiralAttackTimer = 0;
    private float slimeSpitTimer = 0;

    private float minX;
    private float maxX;
    private boolean movingLeft = true;

    private float minY;
    private float maxY;

    public transient ServerLevel level;

    public Boss(int ID, Vector2 pos, ServerLevel level) {
        super(ID, new Rectangle(pos.x - 27.5f, pos.y - 40, 55, 80), 50, 50, 80);

        minX = level.getGridWidth() * TILE_WIDTH / 5;
        maxX = minX * 4;
        vel.x = -speed;

        minY = pos.y - TILE_HEIGHT;
        maxY = pos.y + TILE_HEIGHT;
        vel.y = -(speed * 1/2);

        this.level = level;
    }

    @Override
    public void loadAnim() {
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(Assets.getTex(("graphics/entity/enemy/ghost.png")));
        frames[1] = new TextureRegion(Assets.getTex(("graphics/entity/enemy/ghost_normal.png")));

        anim = new Animation<>(0.6f, frames);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (level != null) {
            float updateTime = delta * (1 + 0.5f * (getHealth() / getMaxHealth()));

            if (vel.x < 0 && body.x <= minX)
                vel.x = speed;
            else if (vel.x > 0 && body.x + body.width >= maxX)
                vel.x = -speed;

            if (vel.y < 0 && body.y <= minY)
                vel.y = speed * 1/2;
            else if (vel.y > 0 && body.y + body.height >= maxY)
                vel.y = -speed * 1/2;

            actionTimer -= updateTime;
            spiralAttackTimer -= updateTime;
            slimeSpitTimer -= updateTime;

            if (actionTimer <= 0)
                decideAction();
        } else {
            if (vel.x < 0 && !movingLeft) {
                anim.getKeyFrames()[0].flip(true, false);
                anim.getKeyFrames()[1].flip(true, false);
                movingLeft = true;
            } else if (vel.x > 0 && movingLeft) {
                anim.getKeyFrames()[0].flip(true, false);
                anim.getKeyFrames()[1].flip(true, false);
                movingLeft = false;
            }
        }
    }

    private void decideAction() {
        if (canSpiralAttack())
            spiralAttack();
        else if (canSlimeSpit())
            slimeSpit();
    }

    private boolean canSpiralAttack() {
        return spiralAttackTimer <= 0;
    }

    private void spiralAttack() {
        for (int i = 0; i < 18; i++) {
            float angle = 20 * i;

            Bat bat = new Bat(level.getNextEntityID(), body.getPosition(new Vector2()), angle);
            level.addEntity(bat);
        }

        spiralAttackTimer = 12;
        actionTimer = 2;
    }

    private boolean canSlimeSpit() {
        return slimeSpitTimer <= 0;
    }

    private void slimeSpit() {
        for (Player player : level.getPlayers()) {
            if(player.isDead()) continue;

            int numSlimes = getHealth() < getMaxHealth() / 4 ? 4 : (getHealth() < getMaxHealth() / 2 ? 3 : 2);
            for (int i = 0; i < numSlimes; i++) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Vector2 pos = new Vector2();
                        player.getPosition(pos);

                        float angle = angleToPosition(pos);

                        Slime slime = new Slime(level.getNextEntityID(), body.getPosition(new Vector2()), angle);
                        level.addEntity(slime);
                    }
                }, i * 250);

            }
        }

        slimeSpitTimer = 3;
        actionTimer = 2;
    }

    private float angleToPosition(Vector2 pos) {
        float x = body.x;
        float pX = pos.x;
        float xDifference = x - pX;

        float y = body.y;
        float pY = pos.y;
        float yDifference = y - pY;

        double a = Math.atan2(yDifference, xDifference);

        return (float) a;
    }
}