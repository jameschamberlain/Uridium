package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import net.uridium.game.gameplay.entity.damageable.Player;

public class Heal extends Item {
    public Heal(int ID, Rectangle body) {
        super(ID, body, "heal.png");
    }

    @Override
    public void onPlayerCollision(Player player) {
        super.onPlayerCollision(player);
        player.heal(20);
    }
}