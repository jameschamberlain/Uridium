package net.uridium.game.gameplay.entity.item;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;
import net.uridium.game.gameplay.entity.damageable.Player;

public abstract class Item extends Entity {
    private boolean used = false;

    public Item(int ID, Rectangle body, String textureFile) {
        super(ID, body, new Vector2(0, 0), textureFile);
    }

    public void onPlayerCollision(Player player) {
        used = true;
    }

    public boolean isUsed() {
        return used;
    }
}
