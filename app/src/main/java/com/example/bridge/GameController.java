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
    private final BiddingManager biddingManager;

    public GameController(List<Player> players, GameCallback callback) {
        this.players = players;
        this.callback = callback;
        this.deck = new Deck();
        this.biddingManager = new BiddingManager(players, callback);
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
        String contract = biddingManager.determineBestContract();
        callback.onContractDetermined(contract);
        clearTable();
        players.get(3).setCurrentMove(true);
        playCardOpponent(players.get(3));
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
