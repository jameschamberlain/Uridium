package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.Msg;
import net.uridium.game.server.msg.PlayerPowerupData;

public class MovementSteroid extends Item {
    public MovementSteroid(int ID, Rectangle body) {
        super(ID, body, "coffee.png");
    }

    @Override
    public Msg onPlayerCollision(Player player) {
        super.onPlayerCollision(player);
        player.setPowerup(Player.POWERUP.FASTER_MOVING, 5);
        return new Msg(Msg.MsgType.PLAYER_POWERUP, new PlayerPowerupData(player.getID(), Player.POWERUP.FASTER_MOVING, 5));
    }
}