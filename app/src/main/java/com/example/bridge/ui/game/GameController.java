package com.example.bridge.ui.game;

import android.os.Handler;
import android.os.Looper;

import com.example.bridge.ui.biddings.QuickGameBidding;
import com.example.bridge.DdsSolver;

import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Trick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    public interface GameCallback {
        void onHandUpdated(String playerDirection);

        void onCardPlayed(Player player, Card card);

        void onUpdateLastTrickInTop(Map<String, Card> trickCards);

        void onClearLastCards(List<Card> cardsOnTable);

        void onContractDetermined(Contract contract, Player declarer);

        void onVisibleStartBar(Boolean isVisible);

        void onTurnChanged(String playerDirection);

        void onScoreUpdated(int snScore, int weScore);

        void onPlayersSwapped(boolean ns, boolean ew);

        void onGameEnded(int snScore, int weScore, Contract contract, List<Trick> history, int claim);

        void onClaimButtonVisibilityChanged(boolean visible);

    }

    private final Map<String, Player> players;
    private final android.content.Context context;
    private Deck deck;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final GameCallback callback;
    private final QuickGameBidding quickGameBidding;
    private final DdsSolver ddsSolver;
    private Trick currentTrick = new Trick();
    private List<Trick> playHistoryTrick = new ArrayList<>();
    private boolean isGameRunning = false;

    public void setCurrentContract(Contract currentContract) {
        this.currentContract = currentContract;
    }

    private Contract currentContract = new Contract(true);
    private Player playerFirstPlayCard;
    private int snScore = 0;
    private int weScore = 0;
    private boolean isAutoPlayMode = false;
    private final Map<String, List<Card>> initialPlayerHands = new LinkedHashMap<>();

    public GameController(GameCallback callback, Map<String, Player> players) {
        this.callback = callback;
        this.context = (callback instanceof android.content.Context) ? (android.content.Context) callback : null;
        this.players = players;

        this.deck = new Deck();
        this.ddsSolver = new DdsSolver();
        this.ddsSolver.initDds();
        this.quickGameBidding = new QuickGameBidding(players, callback, ddsSolver);
    }

    public Map<String, Player> getPlayers() {
        return this.players;
    }

    public QuickGameBidding getBiddingManager() {
        return quickGameBidding;
    }

    public void dealCards() {
        handler.removeCallbacksAndMessages(null);
        resetTable();
        isGameRunning = false;
        isAutoPlayMode = false;

        boolean loadedFromPbn = false;
        if (context != null) {
            com.example.bridge.core.SettingsManager settings = com.example.bridge.core.SettingsManager.getInstance(context);
            if (settings.isLoadFromTestPbn()) {
                loadedFromPbn = loadDealFromTestPbn();
            }
        }

        if (loadedFromPbn) {
            return;
        }

        boolean strongHandFound = false;
        int attempts = 0;

        while (!strongHandFound && attempts < 1000) {
            attempts++;
            deck = new Deck();
            for (Player player : players.values()) {
                player.clearHand();
                player.addCards(deck.deal(13));
                player.setInitialHCP(player.calculateHCP());
                player.setCurrentMove(false);
            }

            Player south = players.get("S");
            Player north = players.get("N");

            if (south != null && north != null) {
                int combinedHCP = south.calculateHCP() + north.calculateHCP();
                if (combinedHCP >= 20) {
                    strongHandFound = true;
                }
            }
        }
    }

    private boolean loadDealFromTestPbn() {
        try {
            String pbnContent;
            try (java.io.InputStream is = context.getAssets().open("test.pbn");
                 java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                pbnContent = sb.toString();
            }

            com.example.bridge.bidding.Tools.Game pbnGame = com.example.bridge.bidding.Tools.Game.parse(pbnContent);
            com.example.bridge.bidding.Tools.Deal deal = pbnGame.getDeal();

            for (Map.Entry<com.example.bridge.bidding.Tools.Direction, com.example.bridge.bidding.Tools.Hand> entry : deal.entrySet()) {
                com.example.bridge.bidding.Tools.Direction dir = entry.getKey();
                com.example.bridge.bidding.Tools.Hand bHand = entry.getValue();
                if (bHand == null) continue;

                String playerName = getPlayerNameForDirection(dir);
                Player player = players.get(playerName);
                if (player != null) {
                    player.clearHand();
                    List<Card> modelCards = new ArrayList<>();
                    for (com.example.bridge.bidding.Tools.Card bCard : bHand) {
                        modelCards.add(toModelCard(bCard));
                    }
                    player.addCards(modelCards);
                    player.setInitialHCP(player.calculateHCP());
                    player.setCurrentMove(false);
                }
            }
            return true;
        } catch (Exception e) {
            android.util.Log.e("GameController", "Failed to load deal from test.pbn", e);
            return false;
        }
    }

    private String getPlayerNameForDirection(com.example.bridge.bidding.Tools.Direction dir) {
        switch (dir) {
            case N:
                return "North";
            case E:
                return "East";
            case S:
                return "South";
            case W:
                return "West";
            default:
                return "South";
        }
    }

    private Card toModelCard(com.example.bridge.bidding.Tools.Card bCard) {
        com.example.bridge.model.Suit mSuit;
        switch (bCard.getSuit()) {
            case Clubs:
                mSuit = com.example.bridge.model.Suit.CLUBS;
                break;
            case Diamonds:
                mSuit = com.example.bridge.model.Suit.DIAMONDS;
                break;
            case Hearts:
                mSuit = com.example.bridge.model.Suit.HEARTS;
                break;
            case Spades:
                mSuit = com.example.bridge.model.Suit.SPADES;
                break;
            default:
                mSuit = com.example.bridge.model.Suit.SPADES;
                break;
        }

        com.example.bridge.model.Rank mRank;
        switch (bCard.getRank()) {
            case Two:
                mRank = com.example.bridge.model.Rank.TWO;
                break;
            case Three:
                mRank = com.example.bridge.model.Rank.THREE;
                break;
            case Four:
                mRank = com.example.bridge.model.Rank.FOUR;
                break;
            case Five:
                mRank = com.example.bridge.model.Rank.FIVE;
                break;
            case Six:
                mRank = com.example.bridge.model.Rank.SIX;
                break;
            case Seven:
                mRank = com.example.bridge.model.Rank.SEVEN;
                break;
            case Eight:
                mRank = com.example.bridge.model.Rank.EIGHT;
                break;
            case Nine:
                mRank = com.example.bridge.model.Rank.NINE;
                break;
            case Ten:
                mRank = com.example.bridge.model.Rank.TEN;
                break;
            case Jack:
                mRank = com.example.bridge.model.Rank.JACK;
                break;
            case Queen:
                mRank = com.example.bridge.model.Rank.QUEEN;
                break;
            case King:
                mRank = com.example.bridge.model.Rank.KING;
                break;
            case Ace:
                mRank = com.example.bridge.model.Rank.ACE;
                break;
            default:
                mRank = com.example.bridge.model.Rank.ACE;
                break;
        }

        return new Card(mSuit, mRank);
    }

    public void setPlayerFirstPlayCard(Player playerFirstPlayCard) {
        this.playerFirstPlayCard = playerFirstPlayCard;
    }

    public void calculateAndSetTheBestContract() {
        setCurrentContract(quickGameBidding.determineBestContract());
        callback.onContractDetermined(currentContract, players.get("S"));
        playerFirstPlayCard = players.get("W");
    }

    public void onBiddingFinished(Contract contract, Player declarer) {
        this.currentContract = contract;

        if (declarer != null) {
            // If North won the bidding, we swap hands N<->S and E<->W
            // This ensures the human player (South) is always the declarer if NS wins.
            if ("N".equals(declarer.getDirectionString())) {
                swapHands(players.get("N"), players.get("S"));
                swapHands(players.get("E"), players.get("W"));
                declarer = players.get("S"); // Now South is the declarer
                callback.onPlayersSwapped(true, true);
            }

            this.playerFirstPlayCard = getNextPlayer(declarer);

            if (contract.getSuit() != null || contract.isNoTrump()) {
                quickGameBidding.sortHandsByContract(contract.getSuit());
            }

            callback.onContractDetermined(contract, declarer);
            callback.onHandUpdated("N");
            callback.onHandUpdated("S");

            handler.postDelayed(this::startGame, 300);
        } else {
            this.playerFirstPlayCard = null;
            callback.onContractDetermined(contract, null);
            callback.onHandUpdated("N");
            callback.onHandUpdated("S");
            callback.onGameEnded(0, 0, currentContract, playHistoryTrick, 0);
        }
    }

    private void swapHands(Player p1, Player p2) {
        if (p1 == null || p2 == null) return;
        List<Card> tempHand = new ArrayList<>(p1.getHand());
        int tempHCP = p1.getInitialHCP();

        p1.setHandDirectly(new ArrayList<>(p2.getHand()));
        p1.setInitialHCP(p2.getInitialHCP());

        p2.setHandDirectly(tempHand);
        p2.setInitialHCP(tempHCP);
    }

    public void startGame() {
        if (playerFirstPlayCard == null) return;
        isGameRunning = true;
        playerFirstPlayCard.setCurrentMove(true);
        callback.onTurnChanged(playerFirstPlayCard.getDirectionString());
        playCardOpponent(playerFirstPlayCard);
    }


    public Contract getCurrentContract() {
        return currentContract;
    }

    public void resetTable() {
        snScore = 0;
        weScore = 0;
        currentTrick = new Trick();
        playHistoryTrick = new ArrayList<>();

        callback.onPlayersSwapped(false, false);
        callback.onUpdateLastTrickInTop(currentTrick.getCardsOnTableMap());
        callback.onScoreUpdated(snScore, weScore);
        callback.onClaimButtonVisibilityChanged(false);
        callback.onTurnChanged(null);
    }

    public void playCard(Player player, Card card) {
        if (!isGameRunning || !player.isCurrentMove() || !isLegalMove(player, card)) {
            return;
        }
        player.setCurrentMove(false);
        player.removeCard(card);

        currentTrick.addCard(player.getDirectionString(), card);

        callback.onClearLastCards(currentTrick.getCardsOnTable());
        callback.onCardPlayed(player, card);
        callback.onHandUpdated(player.getDirectionString());
        setNextPlayerCurrentMove(player);
    }

    private void checkClaimPossibility(Player player) {
        Suit trumpSuit = getTrumpSuit();

        if (hasOnlyWinningCards(player, trumpSuit)) {
            callback.onClaimButtonVisibilityChanged(true);
        } else if (hasOnlyWinningCards(getPartner(player), trumpSuit)) {
            callback.onClaimButtonVisibilityChanged(true);
        } else if (isHandOnlyTrumps(player, trumpSuit)) {
            callback.onClaimButtonVisibilityChanged(true);
        } else if (isHandOnlyTrumps(getPartner(player), trumpSuit)) {
            callback.onClaimButtonVisibilityChanged(true);
        } else {
            callback.onClaimButtonVisibilityChanged(false);
        }
    }

    private boolean isHandOnlyTrumps(Player p, Suit trumpSuit) {
        if (p == null || trumpSuit == null) return false;
        List<Card> hand = p.getHand();
        if (hand.isEmpty()) return false;

        for (Card c : hand) {
            if (c.getSuit() != trumpSuit) {
                return false; // Znaleziono kartę w innym kolorze
            }
        }
        return true; // Wszystkie karty na ręce to atuty
    }

    private boolean hasOnlyWinningCards(Player p, Suit trumpSuit) {
        Map<Suit, Integer> maxOthersRank = new HashMap<>();
        for (Player other : players.values()) {
            if (other != p) {
                for (Card c : other.getHand()) {
                    int rank = c.getRank().ordinal();
                    if (rank > maxOthersRank.getOrDefault(c.getSuit(), -1)) {
                        maxOthersRank.put(c.getSuit(), rank);
                    }
                }
            }
        }

        boolean othersHaveTrumps = trumpSuit != null && maxOthersRank.containsKey(trumpSuit);

        for (Card c : p.getHand()) {
            // Jeśli inni mają atuty, boczny kolor blokuje claim
            if (othersHaveTrumps && c.getSuit() != trumpSuit) {
                return false;
            }

            // Jeśli ranga naszej karty jest MNIEJSZA LUB RÓWNA najwyższej karcie innych, to nie wygrywamy
            if (c.getRank().ordinal() <= maxOthersRank.getOrDefault(c.getSuit(), -1)) {
                return false;
            }
        }
        // Jeśli pętla doszła do końca, znaczy to, że WSZYSTKIE karty przeszły test
        return true;
    }

    public void claimRest() {
        if (!isGameRunning) return;
        isGameRunning = false;
        int remainingTricks = players.get("S") != null ? players.get("S").getHand().size() : 0;
        snScore += remainingTricks;

        for (Player p : players.values()) {
            p.getHand().clear();
            callback.onHandUpdated(p.getDirectionString());
        }

        callback.onScoreUpdated(snScore, weScore);
        callback.onGameEnded(snScore, weScore, currentContract, playHistoryTrick, remainingTricks);
    }

    public boolean isLegalMove(Player player, Card card) {
        if (currentTrick.getCardsOnTable().isEmpty()) {
            return true;
        }

        Card leadCard = currentTrick.getCard(playerFirstPlayCard.getDirectionString());
        if (leadCard == null) return true;

        Suit ledSuit = leadCard.getSuit();
        if (card.getSuit() == ledSuit) {
            return true;
        }

        return !player.hasSuit(ledSuit);
    }

    private void setNextPlayerCurrentMove(Player player) {
        if (currentTrick.getCardsOnTable().size() == 4) {
            String winnerDirection = determineTrickWinner();
            currentTrick.setWinnerTrick(winnerDirection);

            if ("N".equals(winnerDirection) || "S".equals(winnerDirection)) {
                snScore++;
            } else {
                weScore++;
            }
            callback.onScoreUpdated(snScore, weScore);
            handler.postDelayed(() -> {
                clearTable();

                if (isGameEnd()) {
                    isGameRunning = false;
                    callback.onGameEnded(snScore, weScore, currentContract, playHistoryTrick, 0);
                    return;
                }

                Player nextPlayer = getPlayerByDirection(winnerDirection);
                playerFirstPlayCard = nextPlayer;
                if (nextPlayer != null) {
                    nextPlayer.setCurrentMove(true);
                    callback.onTurnChanged(nextPlayer.getDirectionString());
                    if ("N".equals(winnerDirection) || "S".equals(winnerDirection)) {
                        checkClaimPossibility(nextPlayer);
                    }
                    checkOpponentMove(nextPlayer);
                }
            }, 700);
        } else {
            Player nextPlayer = getNextPlayer(player);
            if (nextPlayer != null) {
                nextPlayer.setCurrentMove(true);
                callback.onTurnChanged(nextPlayer.getDirectionString());
                checkOpponentMove(nextPlayer);
            }
        }
    }

    private boolean isGameEnd() {
        for (Player player : players.values()) {
            if (player.getHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private String determineTrickWinner() {
        if (currentTrick.getCardsOnTableMap().isEmpty()) return players.keySet().iterator().next();

        // The first card played in the trick is the lead card
        Map.Entry<String, Card> leadEntry = currentTrick.getCardsOnTableMap().entrySet().iterator().next();
        String leaderDirection = leadEntry.getKey();
        Card leadCard = leadEntry.getValue();

        Suit ledSuit = leadCard.getSuit();
        Suit trumpSuit = getTrumpSuit();

        String winnerDirection = leaderDirection;
        Card bestCard = leadCard;

        for (Map.Entry<String, Card> entry : currentTrick.getCardsOnTableMap().entrySet()) {
            Card card = entry.getValue();
            if (isBetterCard(card, bestCard, ledSuit, trumpSuit)) {
                bestCard = card;
                winnerDirection = entry.getKey();
            }
        }
        return winnerDirection;
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
        if (player == null) return;
        String dir = player.getDirectionString();
        if (isAutoPlayMode || "E".equals(dir) || "W".equals(dir)) {
            playCardOpponent(player);
        }
    }

    private void playCardOpponent(Player playerOponent) {
        List<Card> hand = playerOponent.getHand();
        if (!hand.isEmpty() && playerOponent.isCurrentMove()) {
            Card bestCard = calculateBestCard(playerOponent.getDirectionString(), getHandsMap(), currentTrick.getCardsOnTable(), currentContract, playerFirstPlayCard);
            if (bestCard == null) {
                bestCard = hand.get((int) (Math.random() * hand.size()));
            }

            final Card finalCard = bestCard;
            handler.postDelayed(() -> playCard(playerOponent, finalCard), 600);
        }
    }

    private Card calculateBestCard(String playerDirection, Map<String, List<Card>> hands, List<Card> cardsOnTable, Contract contract, Player leaderPlayer) {
        int[] ddsCards = new int[16];
        String[] handNames = {"N", "E", "S", "W"};
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
        int leaderIdx = getPlayerDdsIndex(leaderPlayer != null ? leaderPlayer.getDirectionString() : "W");

        int[] trickSuits = {-1, -1, -1};
        int[] trickRanks = {0, 0, 0};
        for (int i = 0; i < cardsOnTable.size(); i++) {
            Card c = cardsOnTable.get(i);
            trickSuits[i] = mapSuitToDdsIndex(c.getSuit());
            trickRanks[i] = c.getRank().ordinal() + 2;
        }

        int result = getBestCard(playerDirection, ddsCards, trump, leaderIdx, trickSuits, trickRanks, cardsOnTable.size());

        int resSuitIdx = result / 100;
        int resRankVal = result % 100;

        List<Card> currentPlayerHand = hands.get(playerDirection);
        if (currentPlayerHand != null) {
            for (Card c : currentPlayerHand) {
                if (mapSuitToDdsIndex(c.getSuit()) == resSuitIdx && (c.getRank().ordinal() + 2) == resRankVal) {
                    return c;
                }
            }
        }
        return null;
    }

    private int getBestCard(String playerDirection, int[] cards, int trump, int leader, int[] trickSuits, int[] trickRanks, int cardsOnTableCount) {
        int[] resultTab = ddsSolver.calcBestCards(cards, trump, leader, trickSuits, trickRanks);

        boolean isFirstMove = cardsOnTableCount == 0;

        boolean isNS = playerDirection != null && (playerDirection.equals("S") || playerDirection.equals("N"));

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

    public Map<String, List<Card>> getHandsMap() {
        Map<String, List<Card>> map = new HashMap<>();
        for (Player p : players.values()) {
            if (p.getDirectionString() != null) {
                map.put(p.getDirectionString(), p.getHand());
            }
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

    private int getPlayerDdsIndex(String playerDirection) {
        if (playerDirection == null) return 0;
        switch (playerDirection.toUpperCase()) {
            case "N":
            case "NORTH":
                return 0;
            case "E":
            case "EAST":
                return 1;
            case "S":
            case "SOUTH":
                return 2;
            case "W":
            case "WEST":
                return 3;
            default:
                return 0;
        }
    }

    private Player getNextPlayer(Player player) {
        if (player == null) return null;
        String dir = player.getDirectionString();
        if (dir == null) return null;
        switch (dir.toUpperCase()) {
            case "N":
            case "NORTH":
                return getPlayerByDirection("E");
            case "E":
            case "EAST":
                return getPlayerByDirection("S");
            case "S":
            case "SOUTH":
                return getPlayerByDirection("W");
            case "W":
            case "WEST":
                return getPlayerByDirection("N");
            default:
                return null;
        }
    }

    public Player getPartner(Player player) {
        if (player == null || player.getDirectionString() == null) return null;
        switch (player.getDirectionString().toUpperCase()) {
            case "N":
                return players.get("S");
            case "S":
                return players.get("N");
            case "E":
                return players.get("W");
            case "W":
                return players.get("E");
            default:
                return null;
        }
    }

    private Player getPlayerByDirection(String dir) {
        if (dir == null) return null;
        Player p = players.get(dir);
        if (p != null) return p;
        for (Player player : players.values()) {
            if (dir.equalsIgnoreCase(player.getDirectionString())) {
                return player;
            }
        }
        return players.get(dir);
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
