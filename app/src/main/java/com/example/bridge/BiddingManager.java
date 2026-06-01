package com.example.bridge;

import com.example.bridge.model.Card;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BiddingManager {

    private final Map<String, Player> players;
    private final GameController.GameCallback callback;

    public BiddingManager(Map<String, Player> players, GameController.GameCallback callback) {
        this.players = players;
        this.callback = callback;
    }

    public String determineBestContract() {
        switchCardsIfAreToWeaks();
        int totalHCP = getHPCFromSNPlayers();
        String contractColor = getContractColor();
        int contractCount = getContractCount(contractColor, totalHCP);

        swapNorthSouthIfSouthHasLongerTrump(contractColor);
        sortHandsByContract(contractColor);

        callback.onHandUpdated("North");
        callback.onHandUpdated("South");

        return contractCount + " " + contractColor;
    }

    private void swapNorthSouthIfSouthHasLongerTrump(String contractColor) {
        Suit trumpSuit = null;
        switch (contractColor) {
            case "Spades": trumpSuit = Suit.SPADES; break;
            case "Hearts": trumpSuit = Suit.HEARTS; break;
            case "Diamonds": trumpSuit = Suit.DIAMONDS; break;
            case "Clubs": trumpSuit = Suit.CLUBS; break;
        }

        if (trumpSuit != null) {
            int southCount = players.get("South").countSuit(trumpSuit);
            int northCount = players.get("North").countSuit(trumpSuit);
            
            // If South has more trump cards than North, swap them
            if (southCount < northCount) {
                List<Card> southHand = new ArrayList<>(players.get("South").getHand());
                List<Card> northHand = new ArrayList<>(players.get("North").getHand());

                players.get("South").clearHand();
                players.get("South").addCards(northHand);
                players.get("North").clearHand();
                players.get("North").addCards(southHand);
            }
        }
    }

    private void sortHandsByContract(String contractColor) {
        Suit trumpSuit = null;
        if (contractColor != null) {
            switch (contractColor) {
                case "Spades": trumpSuit = Suit.SPADES; break;
                case "Hearts": trumpSuit = Suit.HEARTS; break;
                case "Diamonds": trumpSuit = Suit.DIAMONDS; break;
                case "Clubs": trumpSuit = Suit.CLUBS; break;
            }
        }

        for (Player player : players.values()) {
            player.resortHand(trumpSuit);
        }
    }

    private int getContractCount(String contractColor, int totalHCP) {
        int acesAndKings = getAcesAndKingsCount();
        boolean hasSecondColor = hasSecondColorFit();

        if (contractColor.equals("Spades") || contractColor.equals("Hearts")) {
            if (numberLongestColor(players.get("South")) < 7 && numberLongestColor(players.get("North")) < 7) {
                if (totalHCP >= 37) return 7;
                if (totalHCP >= 33) return 6;
                if (totalHCP >= 29 && hasSecondColor && acesAndKings >= 7) return 6;
                if (totalHCP >= 25) return 4;
                if (totalHCP >= 24 && hasSecondColor) return 4;
                if (totalHCP >= 20) return 2;
                return 1;
            } else { // Long suit (7+)
                if (totalHCP >= 37) return 7;
                if (totalHCP >= 33) return 6;
                if (totalHCP >= 25) return 4;
                return 3;
            }
        } else if (contractColor.equals("Diamonds") || contractColor.equals("Clubs")) {
            if (numberLongestColor(players.get("South")) < 7 && numberLongestColor(players.get("North")) < 7) {
                if (totalHCP >= 37) return 7;
                if (totalHCP >= 33) return 6;
                if (totalHCP >= 29 && hasSecondColor && acesAndKings >= 7) return 6;
                if (totalHCP >= 26) return 5;
                if (totalHCP >= 20) return 3;
                return 1;
            } else { // Long suit (7+)
                if (totalHCP >= 37) return 7;
                if (totalHCP >= 33) return 6;
                if (totalHCP >= 25) return 5;
                return 3;
            }
        } else { // NT
            if (totalHCP >= 37) return 7;
            if (totalHCP >= 33) return 6;
            if (totalHCP >= 25) return 3;
            return 1;
        }
    }

    private int getAcesAndKingsCount() {
        return players.get("North").countAcesAndKings() + players.get("South").countAcesAndKings();
    }

    private boolean hasSecondColorFit() {
        for (Suit suit : Suit.values()) {
            if (getCombinedCount(suit) >= 8) {
                int fits = 0;
                for (Suit s : Suit.values()) {
                    if (getCombinedCount(s) >= 8) fits++;
                }
                return fits >= 2;
            }
        }
        return false;
    }

    private String getContractColor() {

        // 1. Check Majors for 8+ fit (Spades, then Hearts)
        if (getCombinedCount(Suit.SPADES) >= 8) return "Spades";
        if (getCombinedCount(Suit.HEARTS) >= 8) return "Hearts";

        // 2. Check for NT if we have holds in all colors
        if (playersHaveHoldInAllSuits(players.get("North"), players.get("South"))) return "NT";

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
        return players.get("North").countSuit(suit) + players.get("South").countSuit(suit);
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
        int maxSuitS = numberLongestColor(players.get("South"));
        int maxSuitN = numberLongestColor(players.get("North"));
        int hcp = getHPCFromSNPlayers();

        boolean isStrongEnough = (maxSuitS > 6 && hcp >= 15) || (maxSuitN > 6 && hcp >= 15);

        if (!isStrongEnough && hcp < 20) {
            List<Card> h0 = new ArrayList<>(players.get("North").getHand());
            List<Card> h1 = new ArrayList<>(players.get("East").getHand());
            List<Card> h2 = new ArrayList<>(players.get("South").getHand());
            List<Card> h3 = new ArrayList<>(players.get("West").getHand());

            players.get("North").clearHand();
            players.get("East").clearHand();
            players.get("South").clearHand();
            players.get("West").clearHand();

            players.get("North").addCards(h1);
            players.get("East").addCards(h0);
            players.get("South").addCards(h3);
            players.get("West").addCards(h2);

            callback.onHandUpdated("North");
            callback.onHandUpdated("East");
            callback.onHandUpdated("South");
            callback.onHandUpdated("West");
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
        int hcpSouth = players.get("South").calculateHCP();
        int hcpNorth = players.get("North").calculateHCP();
        return hcpSouth + hcpNorth;
    }
}
