package com.example.bridge.ui.history;

import com.example.bridge.ui.game.GameActivity;
import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.bidding.Tools.Hand;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Suit;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class PbnCollection {
    private GameActivity gameActivity;

    private Pbn pbn;
    private Pbn pbnNoSystem;
    private Pbn pbnNatC;
    private Pbn pbnWj2025Simple;
    private Pbn pbnWj2025;
    private Pbn pbnLCStandard;

    public PbnCollection(GameActivity gameActivity) {
        this.gameActivity = gameActivity;

        this.pbn = new Pbn(gameActivity, "QuckGame");
        this.pbnNoSystem = new Pbn(gameActivity, "NoSystem");
        this.pbnNatC = new Pbn(gameActivity, "NatC");
        this.pbnWj2025Simple = new Pbn(gameActivity, "Wj2025Simple");
        this.pbnWj2025 = new Pbn(gameActivity, "Wj2025");
        this.pbnLCStandard = new Pbn(gameActivity, "LCStandard");
    }
    public void initAllPbn() {
        pbn.initNewGame();

        gameActivity.getGameController().calculateAndSetTheBestContract();
        pbn.setContract(gameActivity.getGameController().getCurrentContract(), "South");

        pbnNatC.initNewGame();
        runNatCBidding();
    }

    private void runNatCBidding() {
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

        // Ustawiamy dealera na North dla symulacji i zapisujemy w PbnNatC
        game.dealer = Direction.N;
        pbnNatC.setDealer("N");
        
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
            pbnNatC.setContract(modelContract, dirToString(declarerDir));
        } else {
            pbnNatC.setContract(new Contract(true), null);
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

