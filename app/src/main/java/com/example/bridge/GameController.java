package com.example.bridge;

import android.os.Handler;
import android.os.Looper;

import com.example.bridge.model.Card;
import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Trick;

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

        void onGameEnded(int snScore, int weScore, String contract, List<Trick> history, int claim);

        void onInitialHandsHtml();

        void onTotalScore();

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
    private String currentContract = "PASS";
    private String trickLeaderName = "West";
    private int snScore = 0;
    private int weScore = 0;
    private boolean isAutoPlayMode = false;

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
        isAutoPlayMode = false;
        deck = new Deck();

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
        callback.onTotalScore();

    }

    public void startGame() {
        players.get("West").setCurrentMove(true);
        callback.onTurnChanged("West");
        playCardOpponent(players.get("West"));
    }

    public void resetTable() {
        snScore = 0;
        weScore = 0;
        currentTrick = new Trick();
        playHistoryTrick = new ArrayList<>();
        callback.onInitialHandsHtmlClear();
        callback.onTableCleared(currentTrick.getCardsOnTableMap());
        callback.onScoreUpdated(snScore, weScore);
        callback.onClaimButtonVisibilityChanged(false);
        callback.onTurnChanged(null);
    }

    public void playCard(Player player, Card card) {
        if (!player.isCurrentMove() || !isLegalMove(player, card)) {
            return; // Reject moves if it's not the turn or illegal card
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
        for (Map.Entry<Suit, Integer> entry : maxOthersRank.entrySet()) {
            System.out.println("plesik: " + player.getName() + " porownanie do " + entry.getKey() + " -> " + entry.getValue());
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
        //return true;
    }

    public void claimRest() {
        int remainingTricks = players.get("South").getHand().size();
        snScore += remainingTricks;

        for (Player p : players.values()) {
            p.getHand().clear();
            callback.onHandUpdated(p.getName());
        }

        callback.onScoreUpdated(snScore, weScore);
        callback.onGameEnded(snScore, weScore, currentContract, playHistoryTrick,  remainingTricks);
    }

    public boolean isLegalMove(Player player, Card card) {
        if (currentTrick.getCardsOnTable().isEmpty()) {
            return true;
        }

        Card leadCard = currentTrick.getCard(trickLeaderName);
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
                    callback.onGameEnded(snScore, weScore, currentContract, playHistoryTrick,0);
                    return;
                }

                Player nextPlayer = players.get(winnerName);
                trickLeaderName = winnerName;
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
        Card leadCard = currentTrick.getCard(trickLeaderName);
        if (leadCard == null) return players.keySet().iterator().next(); // Should not happen

        Suit ledSuit = leadCard.getSuit();
        Suit trumpSuit = getTrumpSuit();

        String winnerName = trickLeaderName;
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
        if (currentContract == null || currentContract.equals("PASS") || currentContract.endsWith("NT"))
            return null;
        if (currentContract.contains("S")) return Suit.SPADES;
        if (currentContract.contains("H")) return Suit.HEARTS;
        if (currentContract.contains("D")) return Suit.DIAMONDS;
        if (currentContract.contains("C")) return Suit.CLUBS;
        return null;
    }

    private void checkOpponentMove(Player player) {
        if (isAutoPlayMode || "East".equals(player.getName()) || "West".equals(player.getName())) {
            playCardOpponent(player);
        }
    }

    private void playCardOpponent(Player playerOponent) {
        List<Card> hand = playerOponent.getHand();
        if (!hand.isEmpty() && playerOponent.isCurrentMove()) {
            Card bestCard = calculateBestCard(playerOponent);
            if (bestCard == null) {
                bestCard = hand.get((int) (Math.random() * hand.size()));
            }

            final Card finalCard = bestCard;
            handler.postDelayed(() -> playCard(playerOponent, finalCard), 600);
        }
    }

    public String getCurrentContract() {
        return currentContract;
    }

    public List<Trick> calculateOptimalHistory(Map<String, List<Card>> initialHands, String contract) {
        // Przygotuj symulowane ręce
        Map<String, List<Card>> simHands = new HashMap<>();
        for (Map.Entry<String, List<Card>> entry : initialHands.entrySet()) {
            simHands.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        List<Trick> history = new ArrayList<>();
        String currentLeader = "West"; // Pierwszy lider

        for (int t = 0; t < 13; t++) {
            Trick trick = new Trick();
            String currentPlayer = currentLeader;
            List<Card> cardsInThisTrick = new ArrayList<>();

            for (int c = 0; c < 4; c++) {
                Card best = calculateBestCardInternal(currentPlayer, simHands, cardsInThisTrick, contract, currentLeader);
                if (best == null && simHands.get(currentPlayer) != null && !simHands.get(currentPlayer).isEmpty()) {
                    best = simHands.get(currentPlayer).get(0);
                }
                if (best != null) {
                    trick.addCard(currentPlayer, best);
                    cardsInThisTrick.add(best);
                    simHands.get(currentPlayer).remove(best);
                }
                currentPlayer = getNextPlayerName(currentPlayer);
            }

            String winner = determineTrickWinnerInternal(trick.getCardsOnTableMap(), contract, currentLeader);
            trick.setWinnerTrick(winner);
            history.add(trick);
            currentLeader = winner;
        }
        return history;
    }

    private Card calculateBestCardInternal(String playerName, Map<String, List<Card>> hands, List<Card> cardsOnTable, String contract, String leaderName) {
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
        int leaderIdx = getPlayerDdsIndex(leaderName);

        int[] trickSuits = {-1, -1, -1};
        int[] trickRanks = {0, 0, 0};
        for (int i = 0; i < cardsOnTable.size(); i++) {
            Card c = cardsOnTable.get(i);
            trickSuits[i] = mapSuitToDdsIndex(c.getSuit());
            trickRanks[i] = c.getRank().ordinal() + 2;
        }

        int result = getBestCard(playerName, ddsCards, trump, leaderIdx, trickSuits, trickRanks);

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

    private int getBestCard(String playerName, int[] cards, int trump, int leader, int[] trickSuits, int[] trickRanks) {
        //System.out.println("plesik calcBestCards params: trump=" + trump + ", leader=" + leader +
        //        ", cards=" + java.util.Arrays.toString(cards) +
        //        ", trickSuits=" + java.util.Arrays.toString(trickSuits) +
        //        ", trickRanks=" + java.util.Arrays.toString(trickRanks));

        int[] resultTab = ddsSolver.calcBestCards(cards, trump, leader, trickSuits, trickRanks);
        //System.out.println("plesik " + resultTab.length);
        //System.out.println("plesik -----------------------------------------");
        //if (resultTab == null || resultTab.length == 0) {
        //    return ddsSolver.calcDDTable(cards, trump, leader, trickSuits, trickRanks);
        //}

        int cardsOnTableCount = currentTrick.getCardsOnTable().size();
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
        if (currentWinnerIdx != -1) {
            // Opponents are different parity (0,2 vs 1,3)
            if ((currentPlayerIdx % 2) != (currentWinnerIdx % 2)) {
                opponentWinning = true;
            }
        }

        int bestCard = resultTab[0];
        int maxOptimalTrumpRank = -1;
        int minOptimalTrumpRank = 15;
        int maxOptimalTrumpCode = -1;
        int minOptimalTrumpCode = -1;

        // Collect optimal trumps
        for (int cardCode : resultTab) {
            int cardSuit = cardCode / 100;
            int cardRank = cardCode % 100;
            if (cardSuit == trump) {
                if (cardRank > maxOptimalTrumpRank) {
                    maxOptimalTrumpRank = cardRank;
                    maxOptimalTrumpCode = cardCode;
                }
                if (cardRank < minOptimalTrumpRank) {
                    minOptimalTrumpRank = cardRank;
                    minOptimalTrumpCode = cardCode;
                }
            }
        }

        // 1. Trump logic (Ruffing or leading/following trumps)
        if (maxOptimalTrumpRank != -1) {

            // 🔥 NOWA REGUŁA: pierwszy ruch zależny od strony
            if (isFirstMove && resultTab.length > 1) {

                boolean prefersTrump = isNS; // NS → graj atu, EW → unikaj atu

                if (prefersTrump) {
                    // NS: spróbuj zagrać najwyższe atu
                    bestCard = maxOptimalTrumpCode;
                    return bestCard;
                } else {
                    // EW: unikaj atu jeśli masz alternatywy
                    int fallback = -1;

                    for (int cardCode : resultTab) {
                        int suit = cardCode / 100;
                        if (suit != trump) {
                            fallback = cardCode;
                            break;
                        }
                    }

                    if (fallback != -1) {
                        return fallback;
                    }
                    // jeśli nie ma wyboru → gramy atu
                }
            }

            if (maxTrumpOnTable != -1) {
                if (maxOptimalTrumpRank > maxTrumpOnTable) {
                    if (opponentWinning) {
                        bestCard = maxOptimalTrumpCode;
                    } else {
                        bestCard = minOptimalTrumpCode;
                    }
                } else {
                    bestCard = minOptimalTrumpCode;
                }
            } else {
                if (opponentWinning && cardsOnTableCount > 0) {
                    bestCard = maxOptimalTrumpCode;
                } else {
                    bestCard = minOptimalTrumpCode;
                }
            }

            return bestCard;
        }

        // 2. Non-trump logic (Following suit or Discarding)
        // Heuristic: Always prefer the lowest rank among optimal cards to preserve honors.
        int minOptimalRank = 15;
        for (int cardCode : resultTab) {
            int cardRank = cardCode % 100;
            if (cardRank < minOptimalRank) {
                minOptimalRank = cardRank;
                bestCard = cardCode;
            }
        }

        return bestCard;
    }

    private String determineTrickWinnerInternal(Map<String, Card> trickMap, String contract, String leaderName) {
        Card leadCard = trickMap.get(leaderName);
        if (leadCard == null) return players.keySet().iterator().next();

        Suit ledSuit = leadCard.getSuit();
        Suit trumpSuit = getTrumpSuitInternal(contract);

        String winnerName = leaderName;
        Card bestCard = leadCard;

        for (Map.Entry<String, Card> entry : trickMap.entrySet()) {
            Card card = entry.getValue();
            if (isBetterCard(card, bestCard, ledSuit, trumpSuit)) {
                bestCard = card;
                winnerName = entry.getKey();
            }
        }
        return winnerName;
    }

    private Suit getTrumpSuitInternal(String contract) {
        if (contract == null || contract.equals("PASS") || contract.endsWith("NT"))
            return null;
        if (contract.contains("S")) return Suit.SPADES;
        if (contract.contains("H")) return Suit.HEARTS;
        if (contract.contains("D")) return Suit.DIAMONDS;
        if (contract.contains("C")) return Suit.CLUBS;
        return null;
    }

    private String getNextPlayerName(String name) {
        switch (name) {
            case "North": return "East";
            case "East": return "South";
            case "South": return "West";
            case "West": return "North";
            default: return "North";
        }
    }

    private Card calculateBestCard(Player player) {
        return calculateBestCardInternal(player.getName(), getHandsMap(), currentTrick.getCardsOnTable(), currentContract, trickLeaderName);
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
        playHistoryTrick.add(currentTrick);
        callback.onTableCleared(currentTrick.getCardsOnTableMap());
        currentTrick = new Trick();
    }


    public void cleanup() {
        handler.removeCallbacksAndMessages(null);
    }
}
