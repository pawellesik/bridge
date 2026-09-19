package com.example.bridge.ui.biddings;

import com.example.bridge.DdsSolver;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.ui.game.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuickGameBidding {

    private final Map<String, Player> players;
    private final GameController.GameCallback callback;
    private final DdsSolver ddsSolver;

    public QuickGameBidding(Map<String, Player> players, GameController.GameCallback callback, DdsSolver ddsSolver) {
        this.players = players;
        this.callback = callback;
        this.ddsSolver = ddsSolver;
    }

    public Contract determineBestContract() {
        switchCardsIfAreToWeaks();
        int totalHCP = getHPCFromSNPlayers();
        String contractColorStr = getContractColor();
        int contractCount = getContractCount(contractColorStr, totalHCP);


        if (getPlayer("S").countSuit(Suit.getSuit(contractColorStr)) != getPlayer("N").countSuit(Suit.getSuit(contractColorStr))) {
            swapNorthSouthIfSouthHasLongerTrump(contractColorStr);
        } else {
            swapNorthSouthIfSouthHaveMoreHpc();
        }

        // Resort hands AFTER potential swaps, because addCards inside swaps resets sorting to Natural
        sortHandsByContract(Suit.getSuit(contractColorStr));

        int possibleTricks = simulateMaxTricks(contractColorStr);
        if (contractCount == 1 && !contractColorStr.equals("NT") && possibleTricks >= 8) {
            contractCount = 2;
        }
        if (contractCount == 2 && possibleTricks >= 10) {
            contractCount = 4;
        }
        if (contractCount == 3 && !contractColorStr.equals("NT") && !contractColorStr.equals("Spades") && !contractColorStr.equals("Hearts") && possibleTricks >= 11) {
            contractCount = 5;
        }
        if (contractCount == 3 && !contractColorStr.equals("NT") && !contractColorStr.equals("Diamonds") && !contractColorStr.equals("Clubs") && possibleTricks >= 10) {
            contractCount = 4;
        } else if (contractCount == 4 && possibleTricks == 13) {
            contractCount = 6;
        } else if (contractCount == 5 && possibleTricks == 13) {
            contractCount = 6;
        }

        // Notify all players that their hands might have changed/resorted
        for (String playerName : players.keySet()) {
            callback.onHandUpdated(playerName);
        }

        Suit contractSuit = null;
        if (!contractColorStr.equals("NT")) {
            for (Suit s : Suit.values()) {
                if (s.name().equalsIgnoreCase(contractColorStr)) {
                    contractSuit = s;
                    break;
                }
            }
        }

        return new Contract(contractCount, contractSuit);
    }

    private int simulateMaxTricks(String contractColor) {
        int[] ddsCards = new int[16];
        String[] handNames = {"North", "East", "South", "West"};
        String[] shortHandNames = {"N", "E", "S", "W"};
        for (int h = 0; h < 4; h++) {
            Player p = players.get(handNames[h]);
            if (p == null) {
                p = players.get(shortHandNames[h]);
            }
            if (p != null) {
                for (Card c : p.getHand()) {
                    int suitIdx = mapSuitToDdsIndex(c.getSuit());
                    ddsCards[h * 4 + suitIdx] |= (1 << (c.getRank().ordinal() + 2));
                }
            }
        }

        int[] results = ddsSolver.calcFullDDTable(ddsCards);
        if (results == null || results.length < 20) return 7; // Fallback

        int trumpIdx = getTrumpDdsIndex(contractColor);
        // We check what South (idx 2) or North (idx 0) can make as leader
        int tricksNorth = results[trumpIdx * 4];
        int tricksSouth = results[trumpIdx * 4 + 2];

        return Math.max(tricksNorth, tricksSouth);
    }

    private int mapSuitToDdsIndex(Suit suit) {
        switch (suit) {
            case SPADES:
                return 0;
            case HEARTS:
                return 1;
            case DIAMONDS:
                return 2;
            case CLUBS:
                return 3;
            default:
                return 0;
        }
    }

    private int getTrumpDdsIndex(String color) {
        switch (color) {
            case "Spades":
                return 0;
            case "Hearts":
                return 1;
            case "Diamonds":
                return 2;
            case "Clubs":
                return 3;
            case "NT":
                return 4;
            default:
                return 4;
        }
    }

    private Player getPlayer(String dir) {
        if (dir == null) return null;
        Player p = players.get(dir);
        if (p != null) return p;
        switch (dir.toUpperCase()) {
            case "S": case "SOUTH": return players.get("South");
            case "N": case "NORTH": return players.get("North");
            case "E": case "EAST": return players.get("East");
            case "W": case "WEST": return players.get("West");
            default: return null;
        }
    }

    private void swapNorthSouthIfSouthHasLongerTrump(String contractColorStr) {
        Suit trumpSuit = null;
        switch (contractColorStr) {
            case "Spades":
                trumpSuit = Suit.SPADES;
                break;
            case "Hearts":
                trumpSuit = Suit.HEARTS;
                break;
            case "Diamonds":
                trumpSuit = Suit.DIAMONDS;
                break;
            case "Clubs":
                trumpSuit = Suit.CLUBS;
                break;
        }

        if (trumpSuit != null) {
            Player south = getPlayer("S");
            Player north = getPlayer("N");
            if (south != null && north != null) {
                int southCount = south.countSuit(trumpSuit);
                int northCount = north.countSuit(trumpSuit);

                if (southCount < northCount) {
                    List<Card> southHand = new ArrayList<>(south.getHand());
                    List<Card> northHand = new ArrayList<>(north.getHand());

                    south.clearHand();
                    south.addCards(northHand);
                    north.clearHand();
                    north.addCards(southHand);
                }
            }
        }
    }

    private void swapNorthSouthIfSouthHaveMoreHpc() {
        Player south = getPlayer("S");
        Player north = getPlayer("N");
        if (south != null && north != null && south.calculateHCP() < north.calculateHCP()) {
            List<Card> southHand = new ArrayList<>(south.getHand());
            List<Card> northHand = new ArrayList<>(north.getHand());

            south.clearHand();
            south.addCards(northHand);
            north.clearHand();
            north.addCards(southHand);
        }
    }

    public void sortHandsByContract(Suit trumpSuit) {
        for (Player player : players.values()) {
            player.resortHand(trumpSuit);
        }
    }

    private int getContractCount(String contractColor, int totalHCP) {
        int acesAndKings = getAcesAndKingsCount();
        boolean hasSecondColor = hasSecondColorFit();

        if (contractColor.equals("Spades") || contractColor.equals("Hearts")) {
            if (numberLongestColor(getPlayer("S")) < 7 && numberLongestColor(getPlayer("N")) < 7) {
                if (totalHCP >= 37) return 7;
                if (totalHCP >= 34 && hasSecondColor && acesAndKings >= 8) return 7;
                if (totalHCP >= 34 && hasSecondColor && acesAndKings >= 7 && hasRenonsInOtherColor(contractColor))
                    return 7;
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
            if (numberLongestColor(getPlayer("S")) < 7 && numberLongestColor(getPlayer("N")) < 7) {
                if (totalHCP >= 37) return 7;
                if (totalHCP >= 34 && hasSecondColor && acesAndKings >= 8) return 7;
                if (totalHCP >= 34 && hasSecondColor && acesAndKings >= 7 && hasRenonsInOtherColor(contractColor))
                    return 7;
                if (totalHCP >= 33) return 6;
                if (totalHCP >= 29 && hasSecondColor && acesAndKings >= 7) return 6;
                if (totalHCP >= 26) return 5;
                if (totalHCP >= 20) return 3;
                return 1;
            } else { // Long suit (7+)
                if (totalHCP >= 37) return 7;
                if (totalHCP >= 35 && acesAndKings >= 8) return 7;
                if (totalHCP >= 34 && hasSecondColor && acesAndKings >= 8) return 7;
                if (totalHCP >= 33) return 6;
                if (totalHCP >= 25) return 5;
                return 3;
            }
        } else { // NT
            if (totalHCP >= 37) return 7;
            if (totalHCP >= 35 && acesAndKings >= 8) return 7;
            if (totalHCP >= 35 && hasSecondColor && acesAndKings >= 8) return 7;
            if (totalHCP >= 33) return 6;
            if (totalHCP >= 25) return 3;
            return 1;
        }
    }

    private int getAcesAndKingsCount() {
        Player north = getPlayer("N");
        Player south = getPlayer("S");
        if (north == null || south == null) return 0;
        return north.countAcesAndKings() + south.countAcesAndKings();
    }

    private boolean hasRenonsInOtherColor(String contractColor) {
        Suit trumpSuit = null;
        if (contractColor != null) {
            switch (contractColor) {
                case "Spades":
                    trumpSuit = Suit.SPADES;
                    break;
                case "Hearts":
                    trumpSuit = Suit.HEARTS;
                    break;
                case "Diamonds":
                    trumpSuit = Suit.DIAMONDS;
                    break;
                case "Clubs":
                    trumpSuit = Suit.CLUBS;
                    break;
            }
        }

        Player north = getPlayer("N");
        Player south = getPlayer("S");
        if (north == null || south == null) return false;

        for (Suit suit : Suit.values()) {
            if (suit == trumpSuit) continue;
            if (north.countSuit(suit) == 0 || south.countSuit(suit) == 0) {
                return true;
            }
        }
        return false;
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

        // 3. Check Minors for 8+ fit (Clubs, then Diamonds)
        if (getCombinedCount(Suit.CLUBS) >= 8) return "Clubs";
        if (getCombinedCount(Suit.DIAMONDS) >= 8) return "Diamonds";

        // 2. Check for NT if we have holds in all colors
        if (playersHaveHoldInAllSuits(getPlayer("N"), getPlayer("S"))) return "NT";

        // 4. Check for 7 card fits (Majors first)
        if (getCombinedCount(Suit.SPADES) == 7) return "Spades";
        if (getCombinedCount(Suit.HEARTS) == 7) return "Hearts";
        if (getCombinedCount(Suit.CLUBS) == 7) return "Clubs";
        if (getCombinedCount(Suit.DIAMONDS) == 7) return "Diamonds";

        return "NT";
    }

    private int getCombinedCount(Suit suit) {
        Player north = getPlayer("N");
        Player south = getPlayer("S");
        if (north == null || south == null) return 0;
        return north.countSuit(suit) + south.countSuit(suit);
    }

    private boolean playersHaveHoldInAllSuits(Player p1, Player p2) {
        if (p1 == null || p2 == null) return false;
        for (Suit suit : Suit.values()) {
            if (!p1.hasHold(suit) && !p2.hasHold(suit)) {
                return false;
            }
        }
        return true;
    }


    private void switchCardsIfAreToWeaks() {
        Player south = getPlayer("S");
        Player north = getPlayer("N");
        Player east = getPlayer("E");
        Player west = getPlayer("W");
        if (south == null || north == null || east == null || west == null) return;

        int maxSuitS = numberLongestColor(south);
        int maxSuitN = numberLongestColor(north);
        int hcp = getHPCFromSNPlayers();

        boolean isStrongEnough = (maxSuitS > 6 && hcp >= 15) || (maxSuitN > 6 && hcp >= 15);

        if (!isStrongEnough && hcp < 20) {
            List<Card> h0 = new ArrayList<>(north.getHand());
            List<Card> h1 = new ArrayList<>(east.getHand());
            List<Card> h2 = new ArrayList<>(south.getHand());
            List<Card> h3 = new ArrayList<>(west.getHand());

            north.clearHand();
            east.clearHand();
            south.clearHand();
            west.clearHand();

            north.addCards(h1);
            east.addCards(h0);
            south.addCards(h3);
            west.addCards(h2);
        }
    }

    private int numberLongestColor(Player player) {
        if (player == null) return 0;
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
        Player south = getPlayer("S");
        Player north = getPlayer("N");
        if (south == null || north == null) return 0;
        return south.calculateHCP() + north.calculateHCP();
    }
}