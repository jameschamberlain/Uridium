package net.uridium.game.server.msg;

import java.io.Serializable;

public class PlayerDeathData implements Serializable {
    public int ID;
    public int position;

    public PlayerDeathData(int ID, int position) {
        this.ID = ID;
        this.position = position;
    }
}