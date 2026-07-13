package com.example.bridge.ui.history;

import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.bidding.Tools.Hand;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.game.GameActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private GameActivity gameActivity;

    public PbnExporter(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        this.date = new SimpleDateFormat("yyyy.MM.dd", Locale.US).format(new Date());
    }

    public void initNewGame(Map<String, List<Card>> hands, String dealer, String vulnerable) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        this.event = timestamp;
        this.site = "Bridge App";
        this.board = "1";
        this.dealer = dealer;
        this.vulnerable = vulnerable;

        // Create a deep copy of the hands because they will be cleared during play
        this.initialHands = new java.util.HashMap<>();
        if (hands != null) {
            for (Map.Entry<String, List<Card>> entry : hands.entrySet()) {
                this.initialHands.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
        }

        this.auction.clear();
        this.playHistory = new ArrayList<>();
        this.contract = null;
        this.declarer = null;
    }
    public void setContract(Contract contract, String declarer) {
        this.contract = contract;
        this.declarer = declarer;
    }

    public String getDeclarer() {
        return declarer;
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

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public void setVulnerable(String vulnerable) {
        this.vulnerable = vulnerable;
    }

    public void setPlayerNames(String west, String north, String east, String south) {
        this.west = west;
        this.north = north;
        this.east = east;
        this.south = south;
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
            sb.append(String.format(Locale.US, "[Auction \"%s\"]\n", formatDirection(dealer)));
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
        if (initialHands == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(dealer).append(":");

        String[] directions = {"West", "North", "East", "South"};
        int startIdx = 0;
        if ("N".equals(dealer)) startIdx = 1;
        else if ("E".equals(dealer)) startIdx = 2;
        else if ("S".equals(dealer)) startIdx = 3;

        for (int i = 0; i < 4; i++) {
            int currentIdx = (startIdx + i) % 4;
            sb.append(formatHand(initialHands.get(directions[currentIdx])));
            if (i < 3) sb.append(" ");
        }
        return sb.toString();
    }

    private String formatHand(List<Card> hand) {
        if (hand == null) return "";
        StringBuilder sb = new StringBuilder();
        Suit[] suits = {Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS};
        for (int i = 0; i < 4; i++) {
            Suit currentSuit = suits[i];
            List<Card> suitCards = new ArrayList<>();
            for (Card card : hand) {
                if (card.getSuit() == currentSuit) {
                    suitCards.add(card);
                }
            }
            // Sort ranks descending for PBN (A, K, Q, J, T, 9, ...)
            suitCards.sort((c1, c2) -> Integer.compare(c2.getRank().ordinal(), c1.getRank().ordinal()));

            for (Card card : suitCards) {
                sb.append(formatRank(card.getRank()));
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
            case "North":
                order = new String[]{"North", "East", "South", "West"};
                break;
            case "East":
                order = new String[]{"East", "South", "West", "North"};
                break;
            case "South":
                order = new String[]{"South", "West", "North", "East"};
                break;
            default:
                order = new String[]{"West", "North", "East", "South"};
                break;
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

    public void todoBiding() {
        Game game = new Game();

        if (gameActivity == null || gameActivity.getGameController() == null) return;
        Map<String, com.example.bridge.model.Player> players = gameActivity.getGameController().getPlayers();
        if (players == null) return;

        com.example.bridge.model.Player playerN = players.get("North");
        com.example.bridge.model.Player playerS = players.get("South");

        if (playerN != null) {
            game.getDeal().put(Direction.N, Hand.parse(formatHand(playerN.getHand())));
        }
        if (playerS != null) {
            game.getDeal().put(Direction.S, Hand.parse(formatHand(playerS.getHand())));
        }


        game.dealer = Direction.N;
        game.bidSystemNS = "NatC";
        game.bidSystemEW = "NatC";

        BiddingState state = new BiddingState(game);

        System.out.println("plesik AI North trzyma rękę: " + game.getDeal().get(Direction.N));
        System.out.println("plesik AI South trzyma rękę: " + game.getDeal().get(Direction.S));
        System.out.println("plesik --- Rozpoczynamy licytację ---\n");

        // 4. Pętla licytacji aż do końca (3 pasy)
        while (!state.getContract().isAuctionComplete()) {
            Direction turn = state.getNextToAct().getDirection();

            if (turn == Direction.N || turn == Direction.S) {
                PositionCalls choices = state.getCallChoices();
                CallDetails best = choices.getBestCall();

                if (best == null) {
                    System.err.println("plesik BŁĄD: AI " + turn + " nie wie co zalicytować!");
                    break;
                }

                System.out.println(turn + "plesik  licytuje: " + best.getCall());
                String ruleId = best.getMatchedLogID(state.getNextToAct());
                if (ruleId != null) {
                    System.out.println("plesik    [ID: " + ruleId + "]");
                }
                System.out.println("plesik    [Uzasadnienie: " + best.getDescription(state.getNextToAct()) + "]");
                state.makeCall(best);
                //printPublicKnowledge(state);
            } else {
                state.makeCall(Call.PASS);
            }
        }
    }
}
