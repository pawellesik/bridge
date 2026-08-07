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
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Pbn {

    private String event = "Casual Game";
    private String site = "Bridge App";
    private String date;
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
    private int score = 0;
    private int imp = 0;

    public Pbn(GameActivity gameActivity, String board) {
        this.gameActivity = gameActivity;
        this.date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US).format(new Date());
        this.board = board;
    }

    public void initNewGame() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        this.event = timestamp;
        this.date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US).format(new Date());
        this.site = "Bridge App";

        this.initialHands = new java.util.HashMap<>();
        if (gameActivity.getGameController().getHandsMap() != null) {
            for (Map.Entry<String, List<Card>> entry : gameActivity.getGameController().getHandsMap().entrySet()) {
                this.initialHands.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
        }

        this.auction.clear();
        this.playHistory = new ArrayList<>();
        this.contract = null;
        this.declarer = null;
    }

    public Map<String, List<Card>> getInitialHands() {
        return initialHands;
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

    public String getNorth() {
        return north;
    }

    public String getSouth() {
        return south;
    }

    public String getWest() {
        return west;
    }

    public String getEast() {
        return east;
    }

    public String getBoard() {
        return board;
    }

    public Contract getContract() {
        return contract;
    }

    public List<Trick> getPlayHistory() {
        return playHistory;
    }

    public int getResultTricks() {
        return resultTricks;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setImp(int imp) {
        this.imp = imp;
    }

    public int getImp() {
        return imp;
    }

    public void calculateAndSetScore() {
        if (contract == null || contract.isPass()) {
            this.score = 0;
            return;
        }

        int level = contract.getLevel();
        int requiredTricks = level + 6;
        int diff = resultTricks - requiredTricks;

        if (diff < 0) {
            // Kontrakt wpadł (Wpadki przed partią: -50 za każdą lewę)
            this.score = diff * 50;
            return;
        }

        // Kontrakt ugrany
        int trickPoints;
        Suit suit = contract.getSuit();

        if (contract.isNoTrump()) {
            trickPoints = 40 + (level - 1) * 30;
        } else if (suit == Suit.SPADES || suit == Suit.HEARTS) {
            trickPoints = level * 30;
        } else { // DIAMONDS or CLUBS
            trickPoints = level * 20;
        }

        int currentScore = trickPoints;

        // Premia za ugraną końcówkę lub częściówkę
        if (trickPoints >= 100) {
            currentScore += 300; // Końcówka przed partią
        } else {
            currentScore += 50;  // Częściówka
        }

        // Nadróbki (Overtricks) przed partią
        if (diff > 0) {
            if (contract.isNoTrump() || suit == Suit.SPADES || suit == Suit.HEARTS) {
                currentScore += diff * 30;
            } else {
                currentScore += diff * 20;
            }
        }

        // Premie szlemowe (Slam bonuses) przed partią
        if (level == 6) currentScore += 500;      // Mały szlem
        else if (level == 7) currentScore += 1000; // Wielki szlem

        this.score = currentScore;
    }

    public void loadFromJsonObject(JSONObject json) {
        try {
            this.event = json.optString("Event", event);
            this.site = json.optString("Site", site);
            this.date = json.optString("Date", date);
            this.board = json.optString("Board", board);
            this.west = json.optString("West", west);
            this.north = json.optString("North", north);
            this.east = json.optString("East", east);
            this.south = json.optString("South", south);
            this.dealer = json.optString("Dealer", dealer);
            this.vulnerable = json.optString("Vulnerable", vulnerable);

            if (json.has("Deal")) {
                parseDealString(json.getString("Deal"));
            }

            if (json.has("Contract")) {
                this.contract = Contract.fromString(json.getString("Contract"));
                this.declarer = json.optString("Declarer", "");
                this.resultTricks = json.optInt("Result", 0);
                this.score = json.optInt("Score", 0);
                this.imp = json.optInt("Imp", 0);
            }

            if (json.has("Auction")) {
                this.auction.clear();
                JSONArray auctionArray = json.getJSONArray("Auction");
                for (int i = 0; i < auctionArray.length(); i++) {
                    this.auction.add(auctionArray.getString(i));
                }
            }

            if (json.has("Play")) {
                this.playHistory = new ArrayList<>();
                JSONArray playArray = json.getJSONArray("Play");
                for (int i = 0; i < playArray.length(); i++) {
                    this.playHistory.add(parseTrickString(playArray.getString(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDealString(String dealStr) {
        this.initialHands = new java.util.HashMap<>();
        if (dealStr == null || dealStr.isEmpty() || !dealStr.contains(":")) return;

        String[] mainParts = dealStr.split(":", 2);
        String dealerPart = mainParts[0].trim().toUpperCase();
        String handsPart = mainParts[1].trim();

        String[] handStrings = handsPart.split("\\s+");
        String[] directions = {"North", "East", "South", "West"};
        
        int startIdx = getDirectionIndex(dealerPart);

        for (int i = 0; i < handStrings.length && i < 4; i++) {
            int currentDirIdx = (startIdx + i) % 4;
            this.initialHands.put(directions[currentDirIdx], parsePbnHand(handStrings[i]));
        }
    }

    private int getDirectionIndex(String d) {
        if (d == null || d.isEmpty()) return 0;
        char firstChar = d.toUpperCase().charAt(0);
        switch (firstChar) {
            case 'E': return 1;
            case 'S': return 2;
            case 'W': return 3;
            default: return 0; // North
        }
    }

    private List<Card> parsePbnHand(String pbnHand) {
        List<Card> cards = new ArrayList<>();
        String[] suitParts = pbnHand.split("\\.");
        Suit[] suits = {Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS};

        for (int i = 0; i < suitParts.length && i < 4; i++) {
            for (char rankChar : suitParts[i].toCharArray()) {
                Rank rank = Rank.fromPbnLetter(rankChar);
                if (rank != null) {
                    cards.add(new Card(suits[i], rank));
                }
            }
        }
        return cards;
    }

    private Trick parseTrickString(String trickStr) {
        Trick trick = new Trick();
        String[] cardStrings = trickStr.split(" ");
        // Note: We don't know the leader easily here without more logic, 
        // but for now let's just parse the cards. 
        // The format is [Suit][Rank], e.g., "H7"
        
        // PBN Play order depends on the leader. 
        // For simplicity, let's assume we can map them back if needed.
        // Actually, we'd need to know who led to map to directions.
        return trick;
    }

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("Event", event);
            json.put("Site", site);
            json.put("Date", date);
            json.put("Board", board);
            json.put("West", west);
            json.put("North", north);
            json.put("East", east);
            json.put("South", south);
            json.put("Dealer", dealer);
            json.put("Vulnerable", vulnerable);

            if (initialHands != null) {
                json.put("Deal", formatDeal());
            }

            if (contract != null) {
                json.put("Declarer", formatDirection(declarer));
                json.put("Contract", formatContract(contract));
                json.put("Result", resultTricks);
                json.put("Score", score);
                json.put("Imp", imp);
            }

            if (!auction.isEmpty()) {
                json.put("Auction", new JSONArray(auction));
            }

            if (!playHistory.isEmpty()) {
                JSONArray playArray = new JSONArray();
                for (Trick trick : playHistory) {
                    if (trick.getCardsOnTable().size() == 4) {
                        playArray.put(formatTrickPlay(trick));
                    }
                }
                json.put("Play", playArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
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
            sb.append(String.format(Locale.US, "[Score \"%d\"]\n", score));
            sb.append(String.format(Locale.US, "[Imp \"%d\"]\n", imp));
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
        String d = (dealer != null && !dealer.isEmpty()) ? dealer.substring(0, 1).toUpperCase() : "N";
        sb.append(d).append(":");

        String[] directions = {"North", "East", "South", "West"};
        int startIdx = getDirectionIndex(d);

        for (int i = 0; i < 4; i++) {
            int currentIdx = (startIdx + i) % 4;
            List<Card> hand = initialHands.get(directions[currentIdx]);
            sb.append(formatHand(hand));
            if (i < 3) sb.append(" ");
        }
        return sb.toString();
    }

    public String formatHand(List<Card> hand) {
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

}
