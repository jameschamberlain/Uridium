package net.uridium.game.server.msg;

import net.uridium.game.gameplay.entity.damageable.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The type Game over data.
 */
public class GameOverData implements Serializable {
    /**
     * The Rankings.
     */
    public PlayerRankingData[] rankings;
    /**
     * The Won.
     */
    public boolean won;

    /**
     * Instantiates a new Game over data.
     *
     * @param players the players
     * @param won     the won
     */
    public GameOverData(ArrayList<Player> players, boolean won) {
        rankings = new PlayerRankingData[players.size()];

        for(Player p : players) {
            rankings[p.getRank()-1] = new PlayerRankingData(p.getColour(), "Player", p.getScore());
        }

        this.won = won;
    }

    /**
     * The type Player ranking data.
     */
    public class PlayerRankingData implements Serializable {
        /**
         * The Player colour.
         */
        public Player.Colour playerColour;
        /**
         * The Player name.
         */
        public String playerName;
        /**
         * The Player score.
         */
        public int playerScore;

        /**
         * Instantiates a new Player ranking data.
         *
         * @param playerColour the player colour
         * @param playerName   the player name
         * @param playerScore  the player score
         */
        public PlayerRankingData(Player.Colour playerColour, String playerName, int playerScore) {
            this.playerColour = playerColour;
            this.playerName = playerName;
            this.playerScore = playerScore;
        }
    }
}
