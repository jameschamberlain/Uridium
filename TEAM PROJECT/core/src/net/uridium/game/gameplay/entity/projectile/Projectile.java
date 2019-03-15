package net.uridium.game.gameplay.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Entity;

public class Projectile extends Entity {
    int ownerID;

    public Projectile(int ID, Rectangle body, Vector2 vel, String textureFile, int ownerID) {
        super(ID, body, vel, textureFile);

        this.ownerID = ownerID;
    }

    public int getOwnerID() {
        return ownerID;
    }
}
