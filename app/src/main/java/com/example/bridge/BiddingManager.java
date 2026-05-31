package com.example.bridge;

import com.example.bridge.model.Card;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;

import java.util.ArrayList;
import java.util.List;

public class BiddingManager {

    private final List<Player> players;
    private final GameController.GameCallback callback;

    public BiddingManager(List<Player> players, GameController.GameCallback callback) {
        this.players = players;
        this.callback = callback;
    }

    public String determineBestContract() {
        switchCardsIfAreToWeaks();
        int totalHCP = getHPCFromSNPlayers();
        String contractColor = getContractColor();

        //if (totalHCP >= 25) return "3NT";
        //if (totalHCP >= 20) return "2NT";
        //if (totalHCP >= 15) return "1NT";
        return contractColor;
    }

    private String getContractColor() {
        Player north = players.get(0);
        Player south = players.get(2);

        // 1. Check Majors for 8+ fit (Spades, then Hearts)
        if (getCombinedCount(Suit.SPADES) >= 8) return "Spades";
        if (getCombinedCount(Suit.HEARTS) >= 8) return "Hearts";

        // 2. Check for NT if we have holds in all colors
        if (playersHaveHoldInAllSuits(north, south)) return "NT";

        // 3. Check Minors for 8+ fit (Clubs, then Diamonds)
        if (getCombinedCount(Suit.CLUBS) >= 8) return "Clubs";
        if (getCombinedCount(Suit.DIAMONDS) >= 8) return "Diamonds";

        // 4. Check for 7 card fits (Majors first)
        if (getCombinedCount(Suit.SPADES) == 7) return "Spades";
        if (getCombinedCount(Suit.HEARTS) == 7) return "Hearts";
        if (getCombinedCount(Suit.CLUBS) == 7) return "Clubs";
        if (getCombinedCount(Suit.DIAMONDS) == 7) return "Diamonds";

        return "NT";
    }

    private int getCombinedCount(Suit suit) {
        return players.get(0).countSuit(suit) + players.get(2).countSuit(suit);
    }

    private boolean playersHaveHoldInAllSuits(Player p1, Player p2) {
        for (Suit suit : Suit.values()) {
            if (!p1.hasHold(suit) && !p2.hasHold(suit)) {
                return false;
            }
        }
        return true;
    }


    private void switchCardsIfAreToWeaks() {
        int maxSuitS = numberLongestColor(players.get(2));
        int maxSuitN = numberLongestColor(players.get(0));
        int hcp = getHPCFromSNPlayers();

        boolean isStrongEnough = (maxSuitS > 6 && hcp >= 15) || (maxSuitN > 6 && hcp >= 15);

        if (!isStrongEnough && hcp < 20) {
            List<Card> h0 = new ArrayList<>(players.get(0).getHand());
            List<Card> h1 = new ArrayList<>(players.get(1).getHand());
            List<Card> h2 = new ArrayList<>(players.get(2).getHand());
            List<Card> h3 = new ArrayList<>(players.get(3).getHand());

            players.get(0).clearHand();
            players.get(1).clearHand();
            players.get(2).clearHand();
            players.get(3).clearHand();

            players.get(0).addCards(h1);
            players.get(1).addCards(h0);
            players.get(2).addCards(h3);
            players.get(3).addCards(h2);

            for (int i = 0; i < 4; i++) {
                callback.onHandUpdated(i);
            }
        }
    }

    private int numberLongestColor(Player player) {
        int maxCount = 0;
        for (Suit s : Suit.values()) {
            int count = player.countSuit(s);
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    private int getHPCFromSNPlayers() {
        int hcpSouth = players.get(2).calculateHCP();
        int hcpNorth = players.get(0).calculateHCP();
        return hcpSouth + hcpNorth;
    }
}
