package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.Msg;
import net.uridium.game.server.msg.PlayerPowerupData;

import static net.uridium.game.res.Textures.SHOOTING_POWERUP;

public class ShootingSteroid extends Item {
    public ShootingSteroid(int ID, Rectangle body) {
        super(ID, body, SHOOTING_POWERUP);
    }

    @Override
    public Msg onPlayerCollision(Player player) {
        super.onPlayerCollision(player);
        player.setPowerup(Player.POWERUP.FASTER_SHOOTING, 5);
        return new Msg(Msg.MsgType.PLAYER_POWERUP, new PlayerPowerupData(player.getID(), Player.POWERUP.FASTER_SHOOTING, 5));
    }
}