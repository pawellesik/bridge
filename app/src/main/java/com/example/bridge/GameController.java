package com.example.bridge;

import android.os.Handler;
import android.os.Looper;

import com.example.bridge.model.Card;
import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    public interface GameCallback {
        void onHandUpdated(String playerName);

        void onCardPlayed(Player player, Card card);

        void onTableCleared(Map<String, Card> trickCards);

        void onClearLastCards(List<Card> cardsOnTable);

        void onContractDetermined(String contract);

        void onTurnChanged(String playerName);
    }

    private final Map<String, Player> players;
    private Deck deck;
    private final List<Card> cardsOnTable = new ArrayList<>();
    private final Map<String, Card> currentTrick = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final GameCallback callback;
    private final BiddingManager biddingManager;
    private final DdsSolver ddsSolver;
    private String currentContract = "PASS";
    private String trickLeaderName = "West";

    public GameController(Map<String, Player> players, GameCallback callback) {
        this.players = players;
        this.callback = callback;
        this.deck = new Deck();
        this.biddingManager = new BiddingManager(players, callback);
        this.ddsSolver = new DdsSolver();
        this.ddsSolver.initDds();
    }

    public void dealCards() {
        handler.removeCallbacksAndMessages(null);
        resetTable();
        deck = new Deck();
        deck.shuffle();
        for (Player player : players.values()) {
            player.clearHand();
            player.addCards(deck.deal(13));
            player.setCurrentMove(false);
            callback.onHandUpdated(player.getName());
        }
        currentContract = biddingManager.determineBestContract();
        callback.onContractDetermined(currentContract);

        trickLeaderName = "West";
        players.get("West").setCurrentMove(true);
        callback.onTurnChanged("West");
        playCardOpponent(players.get("West"));
    }

    private void resetTable() {
        cardsOnTable.clear();
        currentTrick.clear();
        callback.onTableCleared(new HashMap<>(currentTrick));
    }

    public void playCard(Player player, Card card) {
        player.setCurrentMove(false);
        callback.onTurnChanged(null); // Clear highlight while processing
        player.removeCard(card);
        cardsOnTable.add(card);
        currentTrick.put(player.getName(), card);
        callback.onClearLastCards(cardsOnTable);
        callback.onCardPlayed(player, card);
        callback.onHandUpdated(player.getName());

        setNextPlayerCurrentMove(player);
    }

    private void setNextPlayerCurrentMove(Player player) {
        if (cardsOnTable.size() == 4) {
            handler.postDelayed(() -> {
                // Find winner of the trick to determine next leader
                // For now, let's just use South as requested in your todo
                clearTable();
                Player nextPlayer = players.get("South"); 
                trickLeaderName = "South";
                nextPlayer.setCurrentMove(true);
                callback.onTurnChanged(nextPlayer.getName());
                checkOpponentMove(nextPlayer);
            }, 1000);
        } else {
            Player nextPlayer = getNextPlayer(player);
            nextPlayer.setCurrentMove(true);
            callback.onTurnChanged(nextPlayer.getName());
            checkOpponentMove(nextPlayer);
        }
    }

    private void checkOpponentMove(Player player) {
        if ("East".equals(player.getName()) || "West".equals(player.getName())) {
            playCardOpponent(player);
        }
    }

    private void playCardOpponent(Player playerOponent) {
        List<Card> hand = playerOponent.getHand();
        if (!hand.isEmpty() && playerOponent.isCurrentMove()) {
            playerOponent.setCurrentMove(false);

            Card bestCard = calculateBestCard(playerOponent);
            if (bestCard == null) {
                bestCard = hand.get((int) (Math.random() * hand.size()));
            }

            final Card finalCard = bestCard;
            handler.postDelayed(() -> playCard(playerOponent, finalCard), 600);
        }
    }

    private Card calculateBestCard(Player player) {
        int[] ddsCards = new int[16];
        String[] handNames = {"North", "East", "South", "West"};
        for (int h = 0; h < 4; h++) {
            Player p = players.get(handNames[h]);
            if (p != null) {
                for (Card c : p.getHand()) {
                    int suitIdx = mapSuitToDdsIndex(c.getSuit());
                    ddsCards[h * 4 + suitIdx] |= (1 << (c.getRank().ordinal() + 2));
                }
            }
        }

        int trump = getTrumpDdsIndex(currentContract);
        int leaderIdx = getPlayerDdsIndex(trickLeaderName);

        int[] trickSuits = {-1, -1, -1};
        int[] trickRanks = {0, 0, 0};
        for (int i = 0; i < cardsOnTable.size(); i++) {
            Card c = cardsOnTable.get(i);
            trickSuits[i] = mapSuitToDdsIndex(c.getSuit());
            trickRanks[i] = c.getRank().ordinal() + 2;
        }

        int result = ddsSolver.calcDDTable(ddsCards, trump, leaderIdx, trickSuits, trickRanks);
        System.out.println("plesik "+result);
        if (result < 0) return null;

        int resSuitIdx = result / 100;
        int resRankVal = result % 100;

        for (Card c : player.getHand()) {
            if (mapSuitToDdsIndex(c.getSuit()) == resSuitIdx && (c.getRank().ordinal() + 2) == resRankVal) {
                return c;
            }
        }
        return null;
    }

    private int mapSuitToDdsIndex(Suit suit) {
        switch (suit) {
            case SPADES: return 0;
            case HEARTS: return 1;
            case DIAMONDS: return 2;
            case CLUBS: return 3;
            default: return 0;
        }
    }

    private int getTrumpDdsIndex(String contract) {
        if (contract == null || contract.equals("PASS") || contract.endsWith("NT")) return 4;
        if (contract.contains("S")) return 0;
        if (contract.contains("H")) return 1;
        if (contract.contains("D")) return 2;
        if (contract.contains("C")) return 3;
        return 4;
    }

    private int getPlayerDdsIndex(String name) {
        switch (name) {
            case "North": return 0;
            case "East": return 1;
            case "South": return 2;
            case "West": return 3;
            default: return 0;
        }
    }

    private Player getNextPlayer(Player player) {
        switch (player.getName()) {
            case "North":
                return players.get("East");
            case "East":
                return players.get("South");
            case "South":
                return players.get("West");
            case "West":
                return players.get("North");
            default:
                return null;
        }
    }

    private void clearTable() {
        if (cardsOnTable.size() == 4) {
            callback.onTableCleared(new HashMap<>(currentTrick));
        }
        cardsOnTable.clear();
        currentTrick.clear();
    }

    public void cleanup() {
        handler.removeCallbacksAndMessages(null);
    }
}
