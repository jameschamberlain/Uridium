package net.uridium.game.server.msg;

import net.uridium.game.gameplay.entity.damageable.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class GameOverData implements Serializable {
    public PlayerRankingData[] rankings;
    public boolean won;

    public GameOverData(ArrayList<Player> players, boolean won) {
        rankings = new PlayerRankingData[players.size()];

        for(Player p : players) {
            rankings[p.getRank()-1] = new PlayerRankingData(p.getColour(), "Player", p.getScore());
        }

        this.won = won;
    }

    public class PlayerRankingData implements Serializable {
        public Player.Colour playerColour;
        public String playerName;
        public int playerScore;

        public PlayerRankingData(Player.Colour playerColour, String playerName, int playerScore) {
            this.playerColour = playerColour;
            this.playerName = playerName;
            this.playerScore = playerScore;
        }
    }
}
