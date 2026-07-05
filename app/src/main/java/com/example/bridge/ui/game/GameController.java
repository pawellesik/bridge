package com.example.bridge.ui.game;

import android.os.Handler;
import android.os.Looper;

import com.example.bridge.ui.biddings.BiddingManager;
import com.example.bridge.DdsSolver;
import com.example.bridge.core.SharedPref;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Trick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    public interface GameCallback {
        void onHandUpdated(String playerName);

        void onCardPlayed(Player player, Card card);

        void onUpdateLastTrickInTop(Map<String, Card> trickCards);

        void onClearLastCards(List<Card> cardsOnTable);

        void onContractDetermined(Contract contract);

        void onVisibleStartBar(Boolean isVisible);

        void onTurnChanged(String playerName);

        void onScoreUpdated(int snScore, int weScore);

        void onGameEnded(int snScore, int weScore, Contract contract, List<Trick> history, int claim);

        void onClaimButtonVisibilityChanged(boolean visible);

    }

    private final Map<String, Player> players;
    private Deck deck;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final GameCallback callback;
    private final BiddingManager biddingManager;
    private final DdsSolver ddsSolver;
    private Trick currentTrick = new Trick();
    private List<Trick> playHistoryTrick = new ArrayList<>();
    private Contract currentContract = new Contract(true);
    private Player playerWinBidding;
    private int snScore = 0;
    private int weScore = 0;
    private boolean isAutoPlayMode = false;
    private final Map<String, List<Card>> initialPlayerHands = new LinkedHashMap<>();

    private SharedPref sharedPref;

    public GameController(GameCallback callback, Map<String, Player> players, SharedPref sharedPref) {
        this.callback = callback;
        this.players = players;
        this.sharedPref = sharedPref;

        this.deck = new Deck();
        this.ddsSolver = new DdsSolver();
        this.ddsSolver.initDds();
        this.biddingManager = new BiddingManager(players, callback, ddsSolver);
    }

    public Map<String, Player> getPlayers() {
        return this.players;
    }

    public BiddingManager getBiddingManager() {
        return biddingManager;
    }

    public void dealCards() {
        handler.removeCallbacksAndMessages(null);
        resetTable();
        isAutoPlayMode = false;
        deck = new Deck();

        for (Player player : players.values()) {
            player.clearHand();
            player.addCards(deck.deal(13));
            player.setCurrentMove(false);
        }
    }

    public void calculateAndSetTheBestContract() {
        currentContract = biddingManager.determineBestContract();
        callback.onContractDetermined(currentContract);
        playerWinBidding = players.get("West");
        //callback.onTotalScore();
    }

    public void startGame() {
        playerWinBidding.setCurrentMove(true);
        callback.onTurnChanged(playerWinBidding.getName());
        playCardOpponent(playerWinBidding);
    }


    public Contract getCurrentContract() {
        return currentContract;
    }

    public void resetTable() {
        snScore = 0;
        weScore = 0;
        currentTrick = new Trick();
        playHistoryTrick = new ArrayList<>();

        callback.onUpdateLastTrickInTop(currentTrick.getCardsOnTableMap());
        callback.onScoreUpdated(snScore, weScore);
        callback.onClaimButtonVisibilityChanged(false);
        callback.onTurnChanged(null);
    }

    public void playCard(Player player, Card card) {
        if (!player.isCurrentMove() || !isLegalMove(player, card)) {
            return;
        }
        player.setCurrentMove(false);
        player.removeCard(card);

        currentTrick.addCard(player.getName(), card);

        callback.onClearLastCards(currentTrick.getCardsOnTable());
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

    private boolean hasOnlyWinningCards(Player p, Map<Suit, Integer> maxOthersRank) {
        Suit trumpSuit = getTrumpSuit();
        boolean othersHaveTrumps = trumpSuit != null && maxOthersRank.containsKey(trumpSuit);

        int totalNSWinners = 0;
        for (Card c : p.getHand()) {
            // Jeśli inni gracze mają jeszcze atu, nie możemy bezpiecznie claimować lew w kolorach bocznych
            if (othersHaveTrumps && c.getSuit() != trumpSuit) {
                return false;
            }

            if (c.getRank().ordinal() > maxOthersRank.getOrDefault(c.getSuit(), -1)) {
                totalNSWinners++;
            }
        }
        return totalNSWinners == p.getHand().size();
        // return true;
    }

    public void claimRest() {
        int remainingTricks = players.get("South").getHand().size();
        snScore += remainingTricks;

        for (Player p : players.values()) {
            p.getHand().clear();
            callback.onHandUpdated(p.getName());
        }

        callback.onScoreUpdated(snScore, weScore);
        callback.onGameEnded(snScore, weScore, currentContract, playHistoryTrick, remainingTricks);
    }

    public boolean isLegalMove(Player player, Card card) {
        if (currentTrick.getCardsOnTable().isEmpty()) {
            return true;
        }

        Card leadCard = currentTrick.getCard(playerWinBidding.getName());
        if (leadCard == null) return true;

        Suit ledSuit = leadCard.getSuit();
        if (card.getSuit() == ledSuit) {
            return true;
        }

        return !player.hasSuit(ledSuit);
    }

    private void setNextPlayerCurrentMove(Player player) {
        if (currentTrick.getCardsOnTable().size() == 4) {
            String winnerName = determineTrickWinner();
            currentTrick.setWinnerTrick(winnerName);

            if (winnerName.equals("North") || winnerName.equals("South")) {
                snScore++;
            } else {
                weScore++;
            }
            callback.onScoreUpdated(snScore, weScore);
            handler.postDelayed(() -> {
                clearTable();

                if (players.get("South").getHand().isEmpty()) {
                    callback.onGameEnded(snScore, weScore, currentContract, playHistoryTrick, 0);
                    return;
                }

                Player nextPlayer = players.get(winnerName);
                playerWinBidding = players.get(winnerName);
                nextPlayer.setCurrentMove(true);

                callback.onTurnChanged(nextPlayer.getName());
                if (winnerName.equals("North") || winnerName.equals("South")) {
                    checkClaimPossibility(nextPlayer);
                }
                checkOpponentMove(nextPlayer);
            }, 700);
        } else {
            Player nextPlayer = getNextPlayer(player);
            nextPlayer.setCurrentMove(true);
            callback.onTurnChanged(nextPlayer.getName());
            checkOpponentMove(nextPlayer);
        }
    }

    private String determineTrickWinner() {
        Card leadCard = currentTrick.getCard(playerWinBidding.getName());
        if (leadCard == null) return players.keySet().iterator().next(); // Should not happen

        Suit ledSuit = leadCard.getSuit();
        Suit trumpSuit = getTrumpSuit();

        String winnerName = playerWinBidding.getName();
        Card bestCard = leadCard;

        for (Map.Entry<String, Card> entry : currentTrick.getCardsOnTableMap().entrySet()) {
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
        if (currentContract == null || currentContract.isPass())
            return null;
        return currentContract.getSuit();
    }

    private void checkOpponentMove(Player player) {
        if (isAutoPlayMode || "East".equals(player.getName()) || "West".equals(player.getName())) {
            playCardOpponent(player);
        }
    }

    private void playCardOpponent(Player playerOponent) {
        List<Card> hand = playerOponent.getHand();
        if (!hand.isEmpty() && playerOponent.isCurrentMove()) {
            Card bestCard = calculateBestCard(playerOponent.getName(), getHandsMap(), currentTrick.getCardsOnTable(), currentContract, playerWinBidding);
            if (bestCard == null) {
                bestCard = hand.get((int) (Math.random() * hand.size()));//todo wybrac mozna tylko do dozwolona karte a nie losowo
            }

            final Card finalCard = bestCard;
            handler.postDelayed(() -> playCard(playerOponent, finalCard), 600);
        }
    }

    private Card calculateBestCard(String playerName, Map<String, List<Card>> hands, List<Card> cardsOnTable, Contract contract, Player leaderName) {
        int[] ddsCards = new int[16];
        String[] handNames = {"North", "East", "South", "West"};
        for (int h = 0; h < 4; h++) {
            List<Card> hand = hands.get(handNames[h]);
            if (hand != null) {
                for (Card c : hand) {
                    int suitIdx = mapSuitToDdsIndex(c.getSuit());
                    ddsCards[h * 4 + suitIdx] |= (1 << (c.getRank().ordinal() + 2));
                }
            }
        }

        int trump = getTrumpDdsIndex(contract);
        int leaderIdx = getPlayerDdsIndex(leaderName.getName());

        int[] trickSuits = {-1, -1, -1};
        int[] trickRanks = {0, 0, 0};
        for (int i = 0; i < cardsOnTable.size(); i++) {
            Card c = cardsOnTable.get(i);
            trickSuits[i] = mapSuitToDdsIndex(c.getSuit());
            trickRanks[i] = c.getRank().ordinal() + 2;
        }

        int result = getBestCard(playerName, ddsCards, trump, leaderIdx, trickSuits, trickRanks, cardsOnTable.size());

        int resSuitIdx = result / 100;
        int resRankVal = result % 100;

        List<Card> currentPlayerHand = hands.get(playerName);
        if (currentPlayerHand != null) {
            for (Card c : currentPlayerHand) {
                if (mapSuitToDdsIndex(c.getSuit()) == resSuitIdx && (c.getRank().ordinal() + 2) == resRankVal) {
                    return c;
                }
            }
        }
        return null;
    }

    private int getBestCard(String playerName, int[] cards, int trump, int leader, int[] trickSuits, int[] trickRanks, int cardsOnTableCount) {
        /*System.out.println("plesik calcBestCards params: trump=" + trump + ", leader=" + leader +
                ", cards=" + java.util.Arrays.toString(cards) +
                ", trickSuits=" + java.util.Arrays.toString(trickSuits) +
                ", trickRanks=" + java.util.Arrays.toString(trickRanks));*/

        int[] resultTab = ddsSolver.calcBestCards(cards, trump, leader, trickSuits, trickRanks);

        boolean isFirstMove = cardsOnTableCount == 0;

        boolean isNS = playerName.contains("South") || playerName.contains("North");

        // Determine current winner and highest trump on table
        int currentWinnerIdx = -1;
        int maxTrumpOnTable = -1;
        int leadSuit = (cardsOnTableCount > 0) ? trickSuits[0] : -1;
        int maxLeadSuitOnTable = -1;

        for (int i = 0; i < cardsOnTableCount; i++) {
            int s = trickSuits[i];
            int r = trickRanks[i];
            int pIdx = (leader + i) % 4;

            boolean better = false;
            if (s == trump) {
                if (maxTrumpOnTable == -1 || r > maxTrumpOnTable) {
                    better = true;
                    maxTrumpOnTable = r;
                }
            } else if (s == leadSuit && maxTrumpOnTable == -1) {
                if (r > maxLeadSuitOnTable) {
                    better = true;
                    maxLeadSuitOnTable = r;
                }
            }

            if (better) currentWinnerIdx = pIdx;
        }

        int currentPlayerIdx = (leader + cardsOnTableCount) % 4;
        boolean opponentWinning = false;
        boolean partnerWinning = false;
        if (currentWinnerIdx != -1) {
            if ((currentPlayerIdx % 2) != (currentWinnerIdx % 2)) {
                opponentWinning = true;
            } else {
                partnerWinning = true;
            }
        }

        // Heuristic analysis of optimal cards
        int minOptimalCode = resultTab[0];
        int minOptimalRank = 100;
        int maxOptimalTrumpCode = -1;
        int maxOptimalTrumpRank = -1;
        int lowestWinningTrumpCode = -1;
        int lowestWinningTrumpRank = 100;

        for (int cardCode : resultTab) {
            int cardSuit = cardCode / 100;
            int cardRank = cardCode % 100;

            if (cardRank < minOptimalRank) {
                minOptimalRank = cardRank;
                minOptimalCode = cardCode;
            }

            if (cardSuit == trump) {
                if (cardRank > maxOptimalTrumpRank) {
                    maxOptimalTrumpRank = cardRank;
                    maxOptimalTrumpCode = cardCode;
                }
                // Can this trump beat the current winning card?
                if (cardRank > maxTrumpOnTable) {
                    if (cardRank < lowestWinningTrumpRank) {
                        lowestWinningTrumpRank = cardRank;
                        lowestWinningTrumpCode = cardCode;
                    }
                }
            }
        }

        // 1. First move logic
        if (isFirstMove && resultTab.length > 1) {
            boolean prefersTrump = isNS;
            if (prefersTrump && maxOptimalTrumpCode != -1) {
                return maxOptimalTrumpCode;
            } else if (!prefersTrump) {
                // EW: avoid trump if alternatives exist
                for (int code : resultTab) {
                    if (code / 100 != trump) return code;
                }
            }
        }

        // 2. Partner is winning - play absolute lowest optimal card
        if (partnerWinning) {
            return minOptimalCode;
        }

        // 3. Opponent is winning - try to win with the LOWEST possible optimal card
        if (opponentWinning) {
            // If we are ruffing or over-ruffing
            if (lowestWinningTrumpCode != -1) {
                return lowestWinningTrumpCode;
            }

            // If it's a non-trump trick and we can win with lead suit
            // (Usually the first element is fine, but let's be safe)
            int lowestWinningLeadCode = -1;
            int lowestWinningLeadRank = 100;
            for (int code : resultTab) {
                int s = code / 100;
                int r = code % 100;
                if (s == leadSuit && r > maxLeadSuitOnTable && r < lowestWinningLeadRank) {
                    lowestWinningLeadRank = r;
                    lowestWinningLeadCode = code;
                }
            }
            if (lowestWinningLeadCode != -1) return lowestWinningLeadCode;
        }

        // 4. Default: play lowest optimal card to preserve honors
        return minOptimalCode;
    }

    private Map<String, List<Card>> getHandsMap() {
        Map<String, List<Card>> map = new HashMap<>();
        for (Player p : players.values()) {
            map.put(p.getName(), p.getHand());
        }
        return map;
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

    private int getTrumpDdsIndex(Contract contract) {
        if (contract == null || contract.isPass() || contract.isNoTrump()) return 4;
        return mapSuitToDdsIndex(contract.getSuit());
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
        playHistoryTrick.add(currentTrick);
        callback.onUpdateLastTrickInTop(currentTrick.getCardsOnTableMap());
        currentTrick = new Trick();
    }


    public void cleanup() {
        handler.removeCallbacksAndMessages(null);
    }



}
