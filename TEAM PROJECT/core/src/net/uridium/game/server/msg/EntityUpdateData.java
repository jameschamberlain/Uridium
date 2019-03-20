package net.uridium.game.server.msg;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class EntityUpdateData implements Serializable {
    public int ID;
    public Vector2 pos;
    public Vector2 vel;
    public float angle;

    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel) {
        this(ID, pos, vel, 0);
    }

    public EntityUpdateData(int ID, Vector2 pos, Vector2 vel, float angle) {
        this.ID = ID;
        this.pos = pos;
        this.vel = vel;
        this.angle = angle;
    }
}
