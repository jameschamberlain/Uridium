package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.Msg;
import net.uridium.game.server.msg.PlayerHealthData;

import static net.uridium.game.res.Textures.HEAL_POWERUP;

/**
 * Item which heals the user when picked up
 */
public class Heal extends Item {

    /**
     * Heal constructor
     * @param ID The entity id of the item
     * @param body The body of the item
     */
    public Heal(int ID, Rectangle body) {
        super(ID, body, HEAL_POWERUP);
    }

    /**
     * Heals the player<br>
     * {@inheritDoc}
     */
    @Override
    public Msg onPlayerCollision(Player player) {
        super.onPlayerCollision(player);
        player.heal(20);
        return new Msg(Msg.MsgType.PLAYER_HEALTH, new PlayerHealthData(player.getID(), player.getHealth(), player.getMaxHealth()));
    }
}