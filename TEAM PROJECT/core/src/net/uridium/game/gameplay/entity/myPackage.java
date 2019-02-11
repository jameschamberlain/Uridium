package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class myPackage implements Serializable {
    HashMap<Integer, Vector2> players;

    public myPackage(){
        players = new HashMap<Integer, Vector2>();
    }

    public HashMap<Integer,Vector2> getPlayers(){
        return players;
    }

    public void setPlayers(int i,Vector2 v2){
        players.put(i,v2);
    }

}
