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
import com.example.bridge.model.Trick;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Suit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PbnCollection {
    private GameActivity gameActivity;

    private Pbn pbn;
    private Pbn pbnNatC;
    private Pbn pbnNatCRev;
    private Pbn pbnWj2025Simple;
    private Pbn pbnWj2025;
    private Pbn pbnLCStandard;
    private Pbn pbnLCStandardRev;
    private Pbn twoOverOneGameForce;
    private Pbn twoOverOneGameForceRev;

    public PbnCollection(GameActivity gameActivity) {
        this.gameActivity = gameActivity;

        this.pbn = new Pbn(gameActivity, "MyGame");
        this.pbn.setPlayerNames("MyGame W", "MyGame N", "MyGame E", "MyGame S");
        this.pbn.setBidSystemNS("NatC");
        this.pbn.setBidSystemEW("PassOnly");

        this.pbnNatC = new Pbn(gameActivity, "NatC");
        this.pbnNatC.setPlayerNames("NatC W", "NatC N", "NatC E", "NatC S");
        this.pbnNatC.setBidSystemNS("NatC");
        this.pbnNatC.setBidSystemEW("PassOnly");

        this.pbnNatCRev = new Pbn(gameActivity, "NatC Rev");
        this.pbnNatCRev.setPlayerNames("NatC Rev W", "NatC Rev N", "NatC Rev E", "NatC Rev S");
        this.pbnNatCRev.setBidSystemNS("NatC");
        this.pbnNatCRev.setBidSystemEW("PassOnly");

        this.pbnLCStandard = new Pbn(gameActivity, "LCStandard");
        this.pbnLCStandard.setPlayerNames("LCStandard W", "LCStandard N", "LCStandard E", "LCStandard S");
        this.pbnLCStandard.setBidSystemNS("LC-Basic");
        this.pbnLCStandard.setBidSystemEW("PassOnly");

        this.pbnLCStandardRev = new Pbn(gameActivity, "LCStandard Rev");
        this.pbnLCStandardRev.setPlayerNames("LCStandard Rev W", "LCStandard Rev N", "LCStandard Rev E", "LCStandard Rev S");
        this.pbnLCStandardRev.setBidSystemNS("LC-Basic");
        this.pbnLCStandardRev.setBidSystemEW("PassOnly");

        this.twoOverOneGameForce = new Pbn(gameActivity, "2/1 GF (S)");
        this.twoOverOneGameForce.setPlayerNames("2/1 GF W", "2/1 GF N", "2/1 GF E", "2/1 GF S");
        this.twoOverOneGameForce.setBidSystemNS("TwoOverOneGameForce");
        this.twoOverOneGameForce.setBidSystemEW("PassOnly");

        this.twoOverOneGameForceRev = new Pbn(gameActivity, "2/1 GF (N)");
        this.twoOverOneGameForceRev.setPlayerNames("2/1 GF W", "2/1 GF N", "2/1 GF E", "2/1 GF S");
        this.twoOverOneGameForceRev.setBidSystemNS("TwoOverOneGameForce");
        this.twoOverOneGameForceRev.setBidSystemEW("PassOnly");

        //this.pbnWj2025Simple = new Pbn(gameActivity, "Wj2025Simple");
        //this.pbnWj2025 = new Pbn(gameActivity, "Wj2025");
    }

    public void initQiuckPbn(){
        Map<String, List<Card>> hands = gameActivity.getGameController().getHandsMap();
        pbn.initNewGame(hands, gameActivity.getGameMode());

        gameActivity.getGameController().calculateAndSetTheBestContract();
        Contract contract = gameActivity.getGameController().getCurrentContract();

        pbn.setContract(contract, "S");
        pbn.setDealer("N"); //zaczyna licytacje

        if (contract != null) {
            if (contract.isPass()) {
                pbn.addBid("Pass");
                pbn.addBid("Pass");
                pbn.addBid("Pass");
                pbn.addBid("Pass");
            } else {
                pbn.addBid("Pass"); // North
                pbn.addBid("Pass"); // East

                String suitChar = contract.isNoTrump() ? "NT" : contract.getSuit().name().substring(0, 1).toUpperCase();
                String formattedBid = contract.getLevel() + suitChar;

                pbn.addBid(formattedBid); // South
                pbn.addBid("Pass"); // West
                pbn.addBid("Pass"); // North
                pbn.addBid("Pass"); // East
            }
        }

        initAllPbn();
    }

    public void initAllPbn() {
        Map<String, List<Card>> hands = gameActivity.getGameController().getHandsMap();

        String gameMode = gameActivity.getGameMode();

        if (pbn.getInitialHands() == null || pbn.getInitialHands().isEmpty()) {
            pbn.initNewGame(hands, gameMode);
        }

        String mainDealer = getNormalizedDealer(pbn.getDealer());
        String oppositeDealer = getOppositeDealer(mainDealer);

        this.pbnNatC.initNewGame(hands, gameMode);
        runBotSimulationGame(pbnNatC, mainDealer);

        this.pbnNatCRev.initNewGame(hands, gameMode);
        runBotSimulationGame(pbnNatCRev, oppositeDealer);

        this.pbnLCStandard.initNewGame(hands, gameMode);
        runBotSimulationGame(pbnLCStandard, mainDealer);

        this.pbnLCStandardRev.initNewGame(hands, gameMode);
        runBotSimulationGame(pbnLCStandardRev, oppositeDealer);

        this.twoOverOneGameForce.initNewGame(hands, gameMode);
        runBotSimulationGame(twoOverOneGameForce, mainDealer);

        this.twoOverOneGameForceRev.initNewGame(hands, gameMode);
        runBotSimulationGame(twoOverOneGameForceRev, oppositeDealer);
    }

    private String getNormalizedDealer(String dealer) {
        if (dealer == null || dealer.trim().isEmpty()) return "N";
        char c = dealer.trim().toUpperCase().charAt(0);
        switch (c) {
            case 'E': return "E";
            case 'S': return "S";
            case 'W': return "W";
            default: return "N";
        }
    }

    private String getOppositeDealer(String dealer) {
        switch (dealer) {
            case "N": return "S";
            case "S": return "N";
            case "E": return "W";
            case "W": return "E";
            default: return "S";
        }
    }

    private void runBotSimulationGame(Pbn pbn, String dealerDirection) {
        Game game = new Game();
        Map<String, com.example.bridge.model.Player> players = gameActivity.getGameController().getPlayers();

        com.example.bridge.model.Player playerN = players.get("N");
        com.example.bridge.model.Player playerE = players.get("E");
        com.example.bridge.model.Player playerS = players.get("S");
        com.example.bridge.model.Player playerW = players.get("W");

        if (playerN != null) game.getDeal().put(Direction.N, Hand.parse(pbn.formatHand(playerN.getHand())));
        if (playerE != null) game.getDeal().put(Direction.E, Hand.parse(pbn.formatHand(playerE.getHand())));
        if (playerS != null) game.getDeal().put(Direction.S, Hand.parse(pbn.formatHand(playerS.getHand())));
        if (playerW != null) game.getDeal().put(Direction.W, Hand.parse(pbn.formatHand(playerW.getHand())));

        if ("N".equalsIgnoreCase(dealerDirection) || "North".equalsIgnoreCase(dealerDirection)) {
            game.dealer = Direction.N;
        } else if ("E".equalsIgnoreCase(dealerDirection) || "East".equalsIgnoreCase(dealerDirection)) {
            game.dealer = Direction.E;
        } else if ("S".equalsIgnoreCase(dealerDirection) || "South".equalsIgnoreCase(dealerDirection)) {
            game.dealer = Direction.S;
        } else if ("W".equalsIgnoreCase(dealerDirection) || "West".equalsIgnoreCase(dealerDirection)) {
            game.dealer = Direction.W;
        }

        pbn.setDealer(dealerDirection);

        game.bidSystemNS = pbn.getBidSystemNS();
        game.bidSystemEW = pbn.getBidSystemEW();

        BiddingState state = new BiddingState(game);

        // Pętla licytacji - obsługujemy wszystkie pozycje
        while (!state.getContract().isAuctionComplete()) {
            Direction turn = state.getNextToAct().getDirection();

            Call callToMake;
            if (turn == Direction.N || turn == Direction.S) {
                PositionCalls choices = state.getCallChoices();
                CallDetails best = choices.getBestCall();
                callToMake = (best != null) ? best.getCall() : Call.PASS;
            } else {
                // Dla E i W (brak rąk w symulacji) wymuszamy pas
                callToMake = Call.PASS;
            }

            pbn.addBid(callToMake.toString());
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
            pbn.setContract(modelContract, declarerName);

            simulateRobotPlay(pbn, modelContract, declarerName);

        } else {
            pbn.setContract(new Contract(true), null);
            pbn.calculateAndSetScore();
        }
    }

    public void calculateAllImps() {
        List<Pbn> allPbns = new ArrayList<>();
        if (pbn != null) allPbns.add(pbn);
        if (pbnNatC != null) allPbns.add(pbnNatC);
        if (pbnNatCRev != null) allPbns.add(pbnNatCRev);
        if (pbnLCStandard != null) allPbns.add(pbnLCStandard);
        if (pbnLCStandardRev != null) allPbns.add(pbnLCStandardRev);
        if (twoOverOneGameForce != null) allPbns.add(twoOverOneGameForce);
        if (twoOverOneGameForceRev != null) allPbns.add(twoOverOneGameForceRev);

        int n = allPbns.size();
        if (n <= 1) {
            for (Pbn p : allPbns) p.setImp(0.0);
            return;
        }

        // Cross-IMP calculation
        for (int i = 0; i < n; i++) {
            Pbn current = allPbns.get(i);
            double sumImp = 0;
            for (int j = 0; j < n; j++) {
                if (i == j) continue; // Nie porównujemy wyniku z samym sobą
                
                int diff = current.getScore() - allPbns.get(j).getScore();
                sumImp += calculateImp(diff);
            }
            
            // Podział przez liczbę porównań (n-1) i zaokrąglenie do jednego miejsca
            double finalImp = sumImp / (n - 1);
            finalImp = Math.round(finalImp * 10.0) / 10.0;
            current.setImp(finalImp);
        }
    }

    private void simulateRobotPlay(Pbn targetPbn, Contract contract, String declarerName) {
        try {
            Map<String, List<Card>> hands = new HashMap<>();
            Map<String, List<Card>> originalHands = gameActivity.getGameController().getHandsMap();
            if (originalHands == null) return;

            String[] dirs = {"N", "E", "S", "W"};
            for (String dir : dirs) {
                List<Card> orig = originalHands.get(dir);
                if (orig != null) {
                    hands.put(dir, new ArrayList<>(orig));
                }
            }

            DdsSolver solver = new DdsSolver();
            solver.initDds();

            String[] playerOrder = {"N", "E", "S", "W"};
            int declarerIdx = -1;
            String shortDecl = toShortDir(declarerName);
            for (int i = 0; i < 4; i++) {
                if (playerOrder[i].equalsIgnoreCase(shortDecl)) {
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

                if ("N".equals(winner) || "S".equals(winner)) {
                    nsTricks++;
                }
                currentLeader = winner;
            }

            targetPbn.setPlayHistory(playHistory);

            int declarerTricks;
            if ("N".equals(shortDecl) || "S".equals(shortDecl)) {
                declarerTricks = nsTricks;
            } else {
                declarerTricks = 13 - nsTricks;
            }
            targetPbn.setResult(declarerTricks);

            targetPbn.calculateAndSetScore();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int calculateImp(int diff) {
        int adiff = Math.abs(diff);
        int imp = 0;
        if (adiff < 20) imp = 0;
        else if (adiff < 50) imp = 1;
        else if (adiff < 90) imp = 2;
        else if (adiff < 130) imp = 3;
        else if (adiff < 170) imp = 4;
        else if (adiff < 220) imp = 5;
        else if (adiff < 270) imp = 6;
        else if (adiff < 320) imp = 7;
        else if (adiff < 370) imp = 8;
        else if (adiff < 430) imp = 9;
        else if (adiff < 500) imp = 10;
        else if (adiff < 600) imp = 11;
        else if (adiff < 750) imp = 12;
        else if (adiff < 900) imp = 13;
        else if (adiff < 1100) imp = 14;
        else if (adiff < 1300) imp = 15;
        else if (adiff < 1500) imp = 16;
        else if (adiff < 1750) imp = 17;
        else if (adiff < 2000) imp = 18;
        else if (adiff < 2250) imp = 19;
        else if (adiff < 2500) imp = 20;
        else if (adiff < 3000) imp = 21;
        else if (adiff < 3500) imp = 22;
        else if (adiff < 4000) imp = 23;
        else imp = 24;
        return diff < 0 ? -imp : imp;
    }

    private Card calculateBestCardForSim(String playerName, Map<String, List<Card>> hands, List<Card> cardsOnTable, int trump, String leaderName, DdsSolver solver) {
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

        List<Card> currentPlayerHand = hands.get(toShortDir(playerName));
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

    private String toShortDir(String dir) {
        if (dir == null) return "N";
        switch (dir.toUpperCase()) {
            case "N": case "NORTH": return "N";
            case "E": case "EAST": return "E";
            case "S": case "SOUTH": return "S";
            case "W": case "WEST": return "W";
            default: return dir;
        }
    }

    private int getPlayerDdsIndex(String name) {
        String shortDir = toShortDir(name);
        switch (shortDir) {
            case "N": return 0;
            case "E": return 1;
            case "S": return 2;
            case "W": return 3;
            default: return 0;
        }
    }

    private int mapSuitToDdsIndex(Suit suit) {
        if (suit == null) return 0;
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

    private String getNextPlayerName(String name) {
        String shortDir = toShortDir(name);
        switch (shortDir) {
            case "N": return "E";
            case "E": return "S";
            case "S": return "W";
            case "W": return "N";
            default: return "N";
        }
    }

    private String dirToString(Direction dir) {
        if (dir == null) return "N";
        switch (dir) {
            case N: return "N";
            case E: return "E";
            case S: return "S";
            case W: return "W";
            default: return "N";
        }
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
            allPbns.add(pbnLCStandard);
            allPbns.add(pbnLCStandardRev);
            allPbns.add(twoOverOneGameForce);
            allPbns.add(twoOverOneGameForceRev);

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

