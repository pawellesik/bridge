package com.example.bridge;

import android.os.Handler;
import android.os.Looper;

import com.example.bridge.model.Card;
import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    public interface GameCallback {
        void onHandUpdated(int playerIndex);

        void onCardPlayed(Player player, Card card);

        void onTableCleared(Map<String, Card> trickCards);

        void onClearLastCards(List<Card> cardsOnTable);

        void onContractDetermined(String contract);
    }

    private final List<Player> players;
    private Deck deck;
    private final List<Card> cardsOnTable = new ArrayList<>();
    private final Map<String, Card> currentTrick = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final GameCallback callback;

    public GameController(List<Player> players, GameCallback callback) {
        this.players = players;
        this.callback = callback;
        this.deck = new Deck();
    }

    public void dealCards() {
        handler.removeCallbacksAndMessages(null);
        deck = new Deck();
        deck.shuffle();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.clearHand();
            player.addCards(deck.deal(13));
            player.setCurrentMove(false);
            callback.onHandUpdated(i);
        }

        // Calculate HCP for South (index 2) and North (index 0)
        int hcpSouth = players.get(2).calculateHCP();
        int hcpNorth = players.get(0).calculateHCP();
        int totalHCP = hcpSouth + hcpNorth;

        callback.onContractDetermined(determineBestContract(totalHCP));

        clearTable();
        players.get(3).setCurrentMove(true);
        playCardOpponent(players.get(3));
    }

    private String determineBestContract(int totalHCP) {
        if (totalHCP < 12) return "PASS";

        // Find longest combined suit for SN
        com.example.bridge.model.Suit bestSuit = null;
        int maxCount = 0;
        for (com.example.bridge.model.Suit s : com.example.bridge.model.Suit.values()) {
            int count = players.get(0).countSuit(s) + players.get(2).countSuit(s);
            if (count > maxCount) {
                maxCount = count;
                bestSuit = s;
            }
        }

        String suitChar = "";
        if (bestSuit != null && maxCount >= 8) {
            switch (bestSuit) {
                case SPADES: suitChar = "S"; break;
                case HEARTS: suitChar = "H"; break;
                case DIAMONDS: suitChar = "D"; break;
                case CLUBS: suitChar = "C"; break;
            }
        }

        if (totalHCP >= 25) {
            if (suitChar.isEmpty()) return "3NT";
            String level = (suitChar.equals("S") || suitChar.equals("H")) ? "4" : "5";
            return level + suitChar;
        } else if (totalHCP >= 20) {
            return suitChar.isEmpty() ? "2NT" : "3" + suitChar;
        } else if (totalHCP >= 15) {
            return suitChar.isEmpty() ? "1NT" : "2" + suitChar;
        } else {
            return suitChar.isEmpty() ? "1NT" : "1" + suitChar;
        }
    }

    public void playCard(Player player, Card card) {
        player.removeCard(card);
        cardsOnTable.add(card);
        currentTrick.put(player.getName(), card);
        callback.onClearLastCards(cardsOnTable);
        callback.onCardPlayed(player, card);
        callback.onHandUpdated(players.indexOf(player));

        setNextPlayerCurrentMove(player);
    }

    private void setNextPlayerCurrentMove(Player player) {
        if (cardsOnTable.size() == 4) {
            handler.postDelayed(() -> {
                clearTable();
                Player nextPlayer = players.get(2); //todo
                nextPlayer.setCurrentMove(true);
                checkOpponentMove(nextPlayer);
            }, 1000);
        } else {
            Player nextPlayer = getNextPlayer(player);
            nextPlayer.setCurrentMove(true);
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
            Card randomCard = hand.get((int) (Math.random() * hand.size()));
            handler.postDelayed(() -> playCard(playerOponent, randomCard), 600);
        }
    }

    private Player getNextPlayer(Player player) {
        int current = players.indexOf(player);
        int next = (current == 2) ? 3 : (current == 3) ? 0 : (current == 0) ? 1 : 2;
        return players.get(next);
    }

    private void clearTable() {
        callback.onTableCleared(new HashMap<>(currentTrick));
        cardsOnTable.clear();
        currentTrick.clear();
    }

    public void cleanup() {
        handler.removeCallbacksAndMessages(null);
    }
}
