package com.example.bridge.ui.history;

import com.example.bridge.ui.game.GameActivity;
import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.bidding.Tools.Hand;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.DdsSolver;
import com.example.bridge.model.Card;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Trick;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Suit;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
public class PbnCollection {
    private GameActivity gameActivity;

    private Pbn pbn;
    private Pbn pbnNoSystem;
    private Pbn pbnNatC;
    private Pbn pbnNatCRev;
    private Pbn pbnWj2025Simple;
    private Pbn pbnWj2025;
    private Pbn pbnLCStandard;

    public PbnCollection(GameActivity gameActivity) {
        this.gameActivity = gameActivity;

        this.pbn = new Pbn(gameActivity, "MyGame");
        this.pbnNoSystem = new Pbn(gameActivity, "NoSystem");
        this.pbnNatC = new Pbn(gameActivity, "NatC");
        this.pbnNatCRev = new Pbn(gameActivity, "NatC Rev");
        this.pbnWj2025Simple = new Pbn(gameActivity, "Wj2025Simple");
        this.pbnWj2025 = new Pbn(gameActivity, "Wj2025");
        this.pbnLCStandard = new Pbn(gameActivity, "LCStandard");
    }
    public void initAllPbn() {
        pbn.initNewGame();

        gameActivity.getGameController().calculateAndSetTheBestContract();
        pbn.setContract(gameActivity.getGameController().getCurrentContract(), "South");

        pbnNatC.initNewGame();
        runNatCBidding(pbnNatC, "N");

        pbnNatCRev.initNewGame();
        runNatCBidding(pbnNatCRev, "S");

    }

    private void runNatCBidding(Pbn pbnNatC, String dealerDirection) {
        Game game = new Game();
        Map<String, com.example.bridge.model.Player> players = gameActivity.getGameController().getPlayers();
        
        com.example.bridge.model.Player playerN = players.get("North");
        com.example.bridge.model.Player playerS = players.get("South");

        if (playerN != null) {
            game.getDeal().put(Direction.N, Hand.parse(pbnNatC.formatHand(playerN.getHand())));
        }
        if (playerS != null) {
            game.getDeal().put(Direction.S, Hand.parse(pbnNatC.formatHand(playerS.getHand())));
        }

        if (dealerDirection.equals("N")) {
            game.dealer = Direction.N;
        } else if (dealerDirection.equals("S")){
            game.dealer = Direction.S;
        }

        pbnNatC.setDealer(dealerDirection);
        
        game.bidSystemNS = "NatC";
        game.bidSystemEW = "NatC";

        BiddingState state = new BiddingState(game);

        // Pętla licytacji - obsługujemy wszystkie pozycje
        while (!state.getContract().isAuctionComplete()) {
            Direction turn = state.getNextToAct().getDirection();
            
            Call callToMake;
            // Dla N i S sprawdzamy co zalicytuje system NatC
            if (turn == Direction.N || turn == Direction.S) {
                PositionCalls choices = state.getCallChoices();
                CallDetails best = choices.getBestCall();
                callToMake = (best != null) ? best.getCall() : Call.PASS;
            } else {
                // Dla E i W (brak rąk w symulacji) wymuszamy pas
                callToMake = Call.PASS;
            }

            // Zapisujemy licytację w PbnNatC
            pbnNatC.addBid(callToMake.toString());
            state.makeCall(callToMake);
        }

        if (!state.getContract().isPassedOut()) {
            com.example.bridge.bidding.Tools.Bid finalBid = state.getContract().getBid();
            Direction declarerDir = state.getContract().getDeclarer();
            
            Suit modelSuit = null;
            if (finalBid.getStrain() != com.example.bridge.bidding.Tools.Strain.NoTrump) {
                modelSuit = Suit.valueOf(finalBid.getStrain().name().toUpperCase());
            }
            
            Contract modelContract = new Contract(finalBid.getLevel(), modelSuit);
            String declarerName = dirToString(declarerDir);
            pbnNatC.setContract(modelContract, declarerName);

            // Symulacja rozgrywki dla systemu NatC
            simulateRobotPlay(pbnNatC, modelContract, declarerName);
        } else {
            pbnNatC.setContract(new Contract(true), null);
        }
    }

    private void simulateRobotPlay(Pbn targetPbn, Contract contract, String declarerName) {
        try {
            Map<String, List<Card>> hands = new HashMap<>();
            Map<String, List<Card>> originalHands = gameActivity.getGameController().getHandsMap();
            if (originalHands == null) return;

            for (Map.Entry<String, List<Card>> entry : originalHands.entrySet()) {
                hands.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }

            DdsSolver solver = new DdsSolver();
            solver.initDds();

            String[] playerOrder = {"North", "East", "South", "West"};
            int declarerIdx = -1;
            for (int i = 0; i < 4; i++) {
                if (playerOrder[i].equalsIgnoreCase(declarerName)) {
                    declarerIdx = i;
                    break;
                }
            }
            if (declarerIdx == -1) return;

            String currentLeader = playerOrder[(declarerIdx + 1) % 4];
            int trumpDds = (contract.isNoTrump() || contract.isPass()) ? 4 : mapSuitToDdsIndex(contract.getSuit());
            
            List<Trick> playHistory = new ArrayList<>();
            int nsTricks = 0;

            for (int trickCount = 0; trickCount < 13; trickCount++) {
                Trick currentTrick = new Trick();
                String currentPlayer = currentLeader;

                for (int p = 0; p < 4; p++) {
                    Card cardToPlay = calculateBestCardForSim(currentPlayer, hands, currentTrick.getCardsOnTable(), trumpDds, currentLeader, solver);
                    if (cardToPlay == null) break;

                    currentTrick.addCard(currentPlayer, cardToPlay);
                    List<Card> hand = hands.get(currentPlayer);
                    if (hand != null) {
                        hand.remove(cardToPlay);
                    }
                    currentPlayer = getNextPlayerName(currentPlayer);
                }

                String winner = determineWinner(currentTrick, contract.getSuit());
                currentTrick.setWinnerTrick(winner);
                playHistory.add(currentTrick);

                if ("North".equals(winner) || "South".equals(winner)) {
                    nsTricks++;
                }
                currentLeader = winner;
            }

            targetPbn.setPlayHistory(playHistory);
            
            int declarerTricks;
            if ("North".equals(declarerName) || "South".equals(declarerName)) {
                declarerTricks = nsTricks;
            } else {
                declarerTricks = 13 - nsTricks;
            }
            targetPbn.setResult(declarerTricks);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Card calculateBestCardForSim(String playerName, Map<String, List<Card>> hands, List<Card> cardsOnTable, int trump, String leaderName, DdsSolver solver) {
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

        int leaderIdx = getPlayerDdsIndex(leaderName);
        int[] trickSuits = {-1, -1, -1};
        int[] trickRanks = {0, 0, 0};
        for (int i = 0; i < cardsOnTable.size(); i++) {
            Card c = cardsOnTable.get(i);
            trickSuits[i] = mapSuitToDdsIndex(c.getSuit());
            trickRanks[i] = c.getRank().ordinal() + 2;
        }

        int[] resultTab = solver.calcBestCards(ddsCards, trump, leaderIdx, trickSuits, trickRanks);
        if (resultTab == null || resultTab.length == 0) return null;
        
        int result = resultTab[0];
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

    private String determineWinner(Trick trick, Suit trumpSuit) {
        if (trick.getCardsOnTable().isEmpty()) return "";

        Map<String, Card> cardsMap = trick.getCardsOnTableMap();
        Map.Entry<String, Card> leadEntry = cardsMap.entrySet().iterator().next();
        String winner = leadEntry.getKey();
        Card bestCard = leadEntry.getValue();
        Suit leadSuit = bestCard.getSuit();

        for (Map.Entry<String, Card> entry : cardsMap.entrySet()) {
            Card challenger = entry.getValue();
            if (isBetterCardSim(challenger, bestCard, leadSuit, trumpSuit)) {
                bestCard = challenger;
                winner = entry.getKey();
            }
        }
        return winner;
    }

    private boolean isBetterCardSim(Card challenger, Card currentBest, Suit leadSuit, Suit trumpSuit) {
        if (challenger.getSuit() == trumpSuit) {
            if (currentBest.getSuit() != trumpSuit) return true;
            return challenger.getRank().ordinal() > currentBest.getRank().ordinal();
        }
        if (currentBest.getSuit() == trumpSuit) return false;

        if (challenger.getSuit() == leadSuit) {
            if (currentBest.getSuit() != leadSuit) return true;
            return challenger.getRank().ordinal() > currentBest.getRank().ordinal();
        }
        return false;
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

    private int mapSuitToDdsIndex(Suit suit) {
        if (suit == null) return 0;
        switch (suit) {
            case SPADES: return 0;
            case HEARTS: return 1;
            case DIAMONDS: return 2;
            case CLUBS: return 3;
            default: return 0;
        }
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

    private String dirToString(Direction dir) {
        switch (dir) {
            case N: return "North";
            case E: return "East";
            case S: return "South";
            case W: return "West";
            default: return "";
        }
    }
    public Pbn getPbnNoSystem() {
        return pbnNoSystem;
    }
    public Pbn getPbnNatC() {
        return pbnNatC;
    }
    public Pbn getPbnWj2025Simple() {
        return pbnWj2025Simple;
    }
    public Pbn getPbnWj2025() {
        return pbnWj2025;
    }
    public Pbn getPbnLCStandard() {
        return pbnLCStandard;
    }
    public Pbn getPbn() {
        return pbn;
    }

    public String generateJsonExport() {
        try {
            JSONArray jsonArray = new JSONArray();
            List<Pbn> allPbns = new ArrayList<>();
            allPbns.add(pbn);
            allPbns.add(pbnNatC);
            allPbns.add(pbnNatCRev);
            //allPbns.add(pbnNoSystem);
            //allPbns.add(pbnWj2025Simple);
            //allPbns.add(pbnWj2025);
            //allPbns.add(pbnLCStandard);

            for (Pbn p : allPbns) {
                if (p != null) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("system", p.getBoard()); 
                    jsonObj.put("data", p.toJsonObject()); // Teraz przekazujemy obiekt, nie String
                    jsonArray.put(jsonObj);
                }
            }
            return jsonArray.toString(4);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}

