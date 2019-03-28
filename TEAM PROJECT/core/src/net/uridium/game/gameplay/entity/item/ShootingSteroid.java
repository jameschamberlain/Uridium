package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.Msg;
import net.uridium.game.server.msg.PlayerPowerupData;

import static net.uridium.game.res.Textures.SHOOTING_POWERUP;

/**
 * Item which temporarily increases the shooting rate of the player
 */
public class ShootingSteroid extends Item {

    /**
     * ShootingSteroid constructor
     * @param ID The entity id of the item
     * @param body The body of the item
     */
    public ShootingSteroid(int ID, Rectangle body) {
        super(ID, body, SHOOTING_POWERUP);
    }

    /**
     * Temporarily gives the player a faster shooting powerup<br>
     * {@inheritDoc}
     */
    @Override
    public Msg onPlayerCollision(Player player) {
        super.onPlayerCollision(player);
        player.setPowerup(Player.POWERUP.FASTER_SHOOTING, 5);
        return new Msg(Msg.MsgType.PLAYER_POWERUP, new PlayerPowerupData(player.getID(), Player.POWERUP.FASTER_SHOOTING, 5));
    }
}