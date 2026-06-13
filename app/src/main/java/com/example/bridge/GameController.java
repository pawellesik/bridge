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

        void onInitialHandsHtmlClear();

        void onClearLastCards(List<Card> cardsOnTable);

        void onContractDetermined(String contract);

        void onVisibleStartBar(Boolean isVisible);

        void onTurnChanged(String playerName);

        void onScoreUpdated(int snScore, int weScore);

        void onGameEnded(int snScore, int weScore, String contract, List<String> history, List<String> historyWinTrick);

        void onInitialHandsHtml();

        void onClaimButtonVisibilityChanged(boolean visible);
    }

    private final Map<String, Player> players;
    private Deck deck;
    private final List<Card> cardsOnTable = new ArrayList<>();
    private final Map<String, Card> currentTrick = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final GameCallback callback;
    private final BiddingManager biddingManager;
    private final DdsSolver ddsSolver;
    private final List<String> playHistory = new ArrayList<>();
    private final List<String> playHistoryWinTrick = new ArrayList<>();
    private String currentContract = "PASS";
    private String trickLeaderName = "West";
    private int snScore = 0;
    private int weScore = 0;

    public GameController(Map<String, Player> players, GameCallback callback) {
        this.players = players;
        this.callback = callback;
        this.deck = new Deck();
        this.ddsSolver = new DdsSolver();
        this.ddsSolver.initDds();
        this.biddingManager = new BiddingManager(players, callback, ddsSolver);
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
        callback.onInitialHandsHtml();

        trickLeaderName = "West";
        callback.onVisibleStartBar(true);
    }

    public void startGame() {
        players.get("West").setCurrentMove(true);
        callback.onTurnChanged("West");
        playCardOpponent(players.get("West"));
    }

    private void resetTable() {
        snScore = 0;
        weScore = 0;
        cardsOnTable.clear();
        currentTrick.clear();
        playHistory.clear();
        playHistoryWinTrick.clear();
        callback.onInitialHandsHtmlClear();
        callback.onTableCleared(new HashMap<>(currentTrick));
        callback.onScoreUpdated(snScore, weScore);
        callback.onClaimButtonVisibilityChanged(false);
        callback.onTurnChanged(null);
     }

    public void playCard(Player player, Card card) {
        if (!player.isCurrentMove() || !isLegalMove(player, card)) {
            return; // Reject moves if it's not the turn or illegal card
        }

        player.setCurrentMove(false);
        //callback.onTurnChanged(null); plesik
        player.removeCard(card);
        cardsOnTable.add(card);
        currentTrick.put(player.getName(), card);

        playHistory.add(player.getName() + ": " + card.getRank().display + " " + card.getSuit().symbol);

        callback.onClearLastCards(cardsOnTable);
        callback.onCardPlayed(player, card);
        callback.onHandUpdated(player.getName());
        setNextPlayerCurrentMove(player);

    }

    private void checkClaimPossibility(Player player) {
        Map<Suit, Integer> maxOthersRank = new HashMap<>();

        for (String p : players.keySet()) {
            if (!p.equals(player.getName())) {
                for (Card c : players.get(p).getHand()) {
                    int rank = c.getRank().ordinal();
                    if (rank > maxOthersRank.getOrDefault(c.getSuit(), -1)) {
                        maxOthersRank.put(c.getSuit(), rank);
                    }
                }
            }
        }

        callback.onClaimButtonVisibilityChanged(hasOnlyWinningCards(player, maxOthersRank));
    }

    private boolean hasOnlyWinningCards(Player p, Map<Suit, Integer> maxEWRank) {
        int totalNSWinners = 0;
        for (Card c : p.getHand()) {
            if (c.getRank().ordinal() > maxEWRank.getOrDefault(c.getSuit(), -1)) {
                totalNSWinners++;
            }
        }
        //return totalNSWinners >= p.getHand().size();//todo to test
        return true;
    }

    public void claimRest() {
        int remainingTricks = players.get("South").getHand().size();
        snScore += remainingTricks;

        playHistory.add("NS claimed the remaining " + remainingTricks + " tricks.");

        for (Player p : players.values()) {
            p.getHand().clear();
            callback.onHandUpdated(p.getName());
        }

        callback.onScoreUpdated(snScore, weScore);
        callback.onGameEnded(snScore, weScore, currentContract, new ArrayList<>(playHistory), new ArrayList<>(playHistoryWinTrick));
    }

    public boolean isLegalMove(Player player, Card card) {
        if (cardsOnTable.isEmpty()) {
            return true;
        }

        Card leadCard = currentTrick.get(trickLeaderName);
        if (leadCard == null) return true;

        Suit ledSuit = leadCard.getSuit();
        if (card.getSuit() == ledSuit) {
            return true;
        }

        return !player.hasSuit(ledSuit);
    }

    private void setNextPlayerCurrentMove(Player player) {
        if (cardsOnTable.size() == 4) {
            String winnerName = determineTrickWinner();
            playHistoryWinTrick.add(winnerName);

            if (winnerName.equals("North") || winnerName.equals("South")) {
                snScore++;
            } else {
                weScore++;
            }
            callback.onScoreUpdated(snScore, weScore);
            handler.postDelayed(() -> {
                clearTable();

                if (players.get("South").getHand().isEmpty()) {
                    callback.onGameEnded(snScore, weScore, currentContract, new ArrayList<>(playHistory), new ArrayList<>(playHistoryWinTrick));
                    return;
                }

                Player nextPlayer = players.get(winnerName);
                trickLeaderName = winnerName;
                nextPlayer.setCurrentMove(true);

                callback.onTurnChanged(nextPlayer.getName());
                checkOpponentMove(nextPlayer);
                if (winnerName.equals("North") || winnerName.equals("South")) {
                    checkClaimPossibility(nextPlayer);
                }
            }, 700);
        } else {
            Player nextPlayer = getNextPlayer(player);
            nextPlayer.setCurrentMove(true);
            callback.onTurnChanged(nextPlayer.getName());
            checkOpponentMove(nextPlayer);
        }
    }

    private String determineTrickWinner() {
        Card leadCard = currentTrick.get(trickLeaderName);
        if (leadCard == null) return players.keySet().iterator().next(); // Should not happen

        Suit ledSuit = leadCard.getSuit();
        Suit trumpSuit = getTrumpSuit();

        String winnerName = trickLeaderName;
        Card bestCard = leadCard;

        for (Map.Entry<String, Card> entry : currentTrick.entrySet()) {
            Card card = entry.getValue();
            if (isBetterCard(card, bestCard, ledSuit, trumpSuit)) {
                bestCard = card;
                winnerName = entry.getKey();
            }
        }
        return winnerName;
    }

    private boolean isBetterCard(Card challenger, Card currentBest, Suit ledSuit, Suit trumpSuit) {
        if (challenger.getSuit() == trumpSuit) {
            if (currentBest.getSuit() != trumpSuit) return true;
            return challenger.getRank().ordinal() > currentBest.getRank().ordinal();
        }
        if (currentBest.getSuit() == trumpSuit) return false;

        if (challenger.getSuit() == ledSuit) {
            if (currentBest.getSuit() != ledSuit) return true;
            return challenger.getRank().ordinal() > currentBest.getRank().ordinal();
        }
        return false;
    }

    private Suit getTrumpSuit() {
        if (currentContract == null || currentContract.equals("PASS") || currentContract.endsWith("NT"))
            return null;
        if (currentContract.contains("S")) return Suit.SPADES;
        if (currentContract.contains("H")) return Suit.HEARTS;
        if (currentContract.contains("D")) return Suit.DIAMONDS;
        if (currentContract.contains("C")) return Suit.CLUBS;
        return null;
    }

    private void checkOpponentMove(Player player) {
        if ("East".equals(player.getName()) || "West".equals(player.getName())) {
            playCardOpponent(player);
        }
    }

    private void playCardOpponent(Player playerOponent) {
        List<Card> hand = playerOponent.getHand();
        if (!hand.isEmpty() && playerOponent.isCurrentMove()) {
            // Do NOT set setCurrentMove(false) here. 
            // playCard will handle it.
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
            case "North":
                return 0;
            case "East":
                return 1;
            case "South":
                return 2;
            case "West":
                return 3;
            default:
                return 0;
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
