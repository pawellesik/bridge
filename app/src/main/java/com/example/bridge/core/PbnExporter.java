package com.example.bridge.core;

import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Trick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Class for collecting game data and exporting it to PBN (Portable Bridge Notation) format.
 */
public class PbnExporter {

    private String event = "Casual Game";
    private String site = "Bridge App";
    private final String date;
    private String board = "1";
    private String west = "Robot";
    private String north = "Robot";
    private String east = "Robot";
    private String south = "User";
    private String dealer = "W";
    private String vulnerable = "None";
    
    private Map<String, List<Card>> initialHands;
    private Contract contract;
    private String declarer;
    private int resultTricks;
    private final List<String> auction = new ArrayList<>();
    private List<Trick> playHistory = new ArrayList<>();

    public PbnExporter() {
        this.date = new SimpleDateFormat("yyyy.MM.dd", Locale.US).format(new Date());
    }

    public void setInitialHands(Map<String, List<Card>> hands) {
        this.initialHands = hands;
    }

    public void setContract(Contract contract, String declarer) {
        this.contract = contract;
        this.declarer = declarer;
    }

    public void setResult(int tricksWonByDeclarerSide) {
        this.resultTricks = tricksWonByDeclarerSide;
    }

    public void setPlayHistory(List<Trick> history) {
        this.playHistory = history;
    }

    public void addBid(String bid) {
        this.auction.add(bid);
    }

    public List<String> getAuction() {
        return auction;
    }

    public void setMetadata(String event, String site, String board) {
        this.event = event;
        this.site = site;
        this.board = board;
    }

    public String generatePbn() {
        StringBuilder sb = new StringBuilder();

        // Standard Tags
        sb.append(String.format(Locale.US, "[Event \"%s\"]\n", event));
        sb.append(String.format(Locale.US, "[Site \"%s\"]\n", site));
        sb.append(String.format(Locale.US, "[Date \"%s\"]\n", date));
        sb.append(String.format(Locale.US, "[Board \"%s\"]\n", board));
        sb.append(String.format(Locale.US, "[West \"%s\"]\n", west));
        sb.append(String.format(Locale.US, "[North \"%s\"]\n", north));
        sb.append(String.format(Locale.US, "[East \"%s\"]\n", east));
        sb.append(String.format(Locale.US, "[South \"%s\"]\n", south));
        sb.append(String.format(Locale.US, "[Dealer \"%s\"]\n", dealer));
        sb.append(String.format(Locale.US, "[Vulnerable \"%s\"]\n", vulnerable));

        if (initialHands != null) {
            sb.append(String.format(Locale.US, "[Deal \"%s\"]\n", formatDeal()));
        }

        if (contract != null) {
            sb.append(String.format(Locale.US, "[Declarer \"%s\"]\n", formatDirection(declarer)));
            sb.append(String.format(Locale.US, "[Contract \"%s\"]\n", formatContract(contract)));
            sb.append(String.format(Locale.US, "[Result \"%d\"]\n", resultTricks));
        }

        // Auction
        if (!auction.isEmpty()) {
            sb.append("[Auction \"W\"]\n");
            for (int i = 0; i < auction.size(); i++) {
                sb.append(auction.get(i)).append(i % 4 == 3 ? "\n" : " ");
            }
            if (auction.size() % 4 != 0) sb.append("\n");
        }

        // Play
        if (!playHistory.isEmpty() && !playHistory.get(0).getCardsOnTable().isEmpty()) {
            String leadDirection = findLeadDirection(playHistory.get(0));
            sb.append(String.format(Locale.US, "[Play \"%s\"]\n", formatDirection(leadDirection)));
            
            for (Trick trick : playHistory) {
                if (trick.getCardsOnTable().size() == 4) {
                    sb.append(formatTrickPlay(trick)).append("\n");
                }
            }
        }

        return sb.toString();
    }

    private String formatDeal() {
        StringBuilder sb = new StringBuilder();
        sb.append(dealer).append(":");
        
        String[] directions = {"West", "North", "East", "South"};
        for (int i = 0; i < 4; i++) {
            sb.append(formatHand(initialHands.get(directions[i])));
            if (i < 3) sb.append(" ");
        }
        return sb.toString();
    }

    private String formatHand(List<Card> hand) {
        if (hand == null) return "";
        StringBuilder sb = new StringBuilder();
        Suit[] suits = {Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS};
        for (int i = 0; i < 4; i++) {
            for (Card card : hand) {
                if (card.getSuit() == suits[i]) {
                    sb.append(formatRank(card.getRank()));
                }
            }
            if (i < 3) sb.append(".");
        }
        return sb.toString();
    }

    private String formatRank(Rank rank) {
        if (rank == Rank.TEN) return "T";
        return rank.display;
    }

    private String formatContract(Contract contract) {
        if (contract.isPass()) return "Pass";
        String suit = contract.isNoTrump() ? "NT" : contract.getSuit().name().substring(0, 1).toUpperCase();
        return contract.getLevel() + suit;
    }

    private String formatDirection(String dir) {
        if (dir == null || dir.isEmpty()) return "";
        return dir.substring(0, 1).toUpperCase();
    }

    private String findLeadDirection(Trick trick) {
        if (trick.getCardsOnTableMap().isEmpty()) return "W";
        return trick.getCardsOnTableMap().keySet().iterator().next();
    }

    private String formatTrickPlay(Trick trick) {
        String leader = findLeadDirection(trick);
        String[] order;
        switch (leader) {
            case "North": order = new String[]{"North", "East", "South", "West"}; break;
            case "East":  order = new String[]{"East", "South", "West", "North"}; break;
            case "South": order = new String[]{"South", "West", "North", "East"}; break;
            default:      order = new String[]{"West", "North", "East", "South"}; break;
        }

        StringBuilder sb = new StringBuilder();
        Map<String, Card> cards = trick.getCardsOnTableMap();
        for (int i = 0; i < 4; i++) {
            Card c = cards.get(order[i]);
            if (c != null) {
                sb.append(c.getSuit().name().charAt(0)).append(formatRank(c.getRank()));
            } else {
                sb.append("- ");
            }
            if (i < 3) sb.append(" ");
        }
        return sb.toString();
    }
}
