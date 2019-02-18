package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class myPackage implements Serializable {
    HashMap<Integer, Vector2> players;
    HashMap<Integer, Vector2> playerBullets;
    HashMap<Integer, Integer> healthBar;
    HashMap<Integer, Integer> scores;
    ArrayList<Vector2> enemyBullets;


    public myPackage(){
        players = new HashMap<Integer, Vector2>();
        playerBullets = new HashMap<Integer, Vector2>();
        healthBar = new HashMap<Integer, Integer>();
        enemyBullets = new ArrayList<Vector2>();
        scores = new HashMap<Integer, Integer>();

    }


    public HashMap<Integer,Vector2> getPlayers(){
        return players;
    }

    public void setPlayers(int port,Vector2 v2){
        players.put(port,v2);
    }

    public HashMap<Integer, Vector2> getPlayerBullets (){
        return playerBullets;
    }

    public void setPlayerBullets(int port,Vector2 v2) {
        playerBullets.put(port,v2);
    }

    public HashMap<Integer, Integer> getHealthBar(){
        return healthBar;
    }

    public void setHealthBar(int port,Integer health){
        healthBar.put(port,health);
    }

    public ArrayList<Vector2> getEnemyBullets(){
        return enemyBullets;
    }




}
