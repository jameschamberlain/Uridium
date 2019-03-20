package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.Msg;
import net.uridium.game.server.msg.PlayerPowerupData;

import static net.uridium.game.res.Textures.MOVEMENT_POWERUP;

public class MovementSteroid extends Item {
    public MovementSteroid(int ID, Rectangle body) {
        super(ID, body, MOVEMENT_POWERUP);
    }

    @Override
    public Msg onPlayerCollision(Player player) {
        super.onPlayerCollision(player);
        player.setPowerup(Player.POWERUP.FASTER_MOVING, 5);
        return new Msg(Msg.MsgType.PLAYER_POWERUP, new PlayerPowerupData(player.getID(), Player.POWERUP.FASTER_MOVING, 5));
    }
}