package com.example.bridge.ui.biddings;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.bridge.R;
import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.ContractState;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.bidding.Tools.Hand;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.ui.game.GameActivity;

import java.util.List;
import java.util.Map;

public class SingleGameBidding {
    private final GameActivity activity;
    private BiddingState liveBiddingState;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public SingleGameBidding(GameActivity activity) {
        this.activity = activity;
    }

    public void start() {
        // 1. Ukrywamy kontrolki na start, aby gracz nie licytował poza kolejnością
        if (activity.getBiddingControlsOverlay() != null) {
            activity.getBiddingControlsOverlay().setVisibility(View.GONE);
        }

        // 2. Ukrywamy wybór systemu licytacyjnego po rozpoczęciu gry
        View selectionContainer = activity.findViewById(R.id.system_selection_container);
        if (selectionContainer != null) {
            selectionContainer.setVisibility(View.GONE);
        }

        Game game = new Game();

        // 2. Random dealer
        Direction[] dirs = Direction.values();
        Direction dealerDir = Direction.S; // Ustawione na sztywno S zgodnie z Twoją prośbą
        game.dealer = dealerDir;
        
        // 3. Sync PBN Dealer (Crucial for correct JSON export alignment)
        activity.getPbnCollection().getPbn().setDealer(dealerDir.toString());

        // 4. Set bidding systems
        game.bidSystemNS = "NatC";
        game.bidSystemEW = "PassOnly";

        // 5. Set hands
        Map<String, List<Card>> hands = activity.getGameController().getHandsMap();
        activity.getPbnCollection().getPbn().initNewGame(hands);
        activity.getPbnCollection().getPbn().setPlayerNames("West", "North", "East", "South");

        game.getDeal().put(Direction.N, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("North"))));
        game.getDeal().put(Direction.E, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("East"))));
        game.getDeal().put(Direction.S, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("South"))));
        game.getDeal().put(Direction.W, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("West"))));

        liveBiddingState = new BiddingState(game);

        // 6. Set first player in UI history
        String firstPlayerName = "West";
        if (dealerDir == Direction.N) firstPlayerName = "North";
        else if (dealerDir == Direction.E) firstPlayerName = "East";
        else if (dealerDir == Direction.S) firstPlayerName = "South";

        activity.getGameBiddingHistory().setFirstPlayer(activity.getGameController().getPlayers().get(firstPlayerName));
        activity.getGameBiddingHistory().getAuction().clear();
        
        // Pokazujemy kafelki licytacji dopiero teraz
        activity.getGameBiddingHistoryAdapter().setShowPreviewTile(true);
        activity.getGameBiddingHistoryAdapter().setHighlightLast(false);
        activity.getGameBiddingHistory().updateBiddingHistory();

        handleNextTurn();
    }

    public void handleNextTurn() {
        if (liveBiddingState == null || liveBiddingState.getContract().isAuctionComplete()) return;

        Direction nextToAct = liveBiddingState.getNextToAct().getDirection();

        if (nextToAct == Direction.S) {
            // Human turn (South)
            activity.runOnUiThread(() -> {
                activity.getGameBiddingHistoryAdapter().setHighlightLast(true);
                activity.getGameBiddingHistory().updateBiddingHistory();
                
                activity.getGameBidding().applyAuctionRules(activity.getGameBiddingHistory());
                if (activity.getBiddingControlsOverlay() != null) {
                    activity.getBiddingControlsOverlay().setVisibility(View.VISIBLE);
                }
            });
            return;
        }

        // Robot turn
        activity.runOnUiThread(() -> {
            activity.getGameBiddingHistoryAdapter().setHighlightLast(false);
            activity.getGameBiddingHistory().updateBiddingHistory();

            if (activity.getBiddingControlsOverlay() != null) {
                activity.getBiddingControlsOverlay().setVisibility(View.GONE);
            }
        });

        handler.postDelayed(() -> {
            PositionCalls choices = liveBiddingState.getCallChoices();
            CallDetails bestCall = choices.getBestCall();
            Call call = (bestCall != null) ? bestCall.getCall() : Call.PASS;

            makeRobotBid(call);
        }, 800);
    }

    private void makeRobotBid(Call call) {
        String bidStr = call.toString();

        // Update live state
        liveBiddingState.makeCall(call);

        // Update UI history
        activity.getGameBiddingHistory().getAuction().add(bidStr);
        activity.getPbnCollection().getPbn().addBid(bidStr);
        activity.getGameBiddingHistory().updateBiddingHistory(null, true);

        // Check for auction end
        if (liveBiddingState.getContract().isAuctionComplete()) {
            onAuctionFinished();
        } else {
            handleNextTurn();
        }
    }

    public void syncManualBid(Call call) {
        if (liveBiddingState != null) {
            liveBiddingState.makeCall(call);
            if (liveBiddingState.getContract().isAuctionComplete()) {
                onAuctionFinished();
            } else {
                handleNextTurn();
            }
        }
    }

    private void onAuctionFinished() {
        ContractState contractState = liveBiddingState.getContract();
        if (contractState.isPassedOut()) {
            activity.getGameController().onBiddingFinished(new Contract(true), null);
        } else {
            Direction declarerDir = contractState.getDeclarer();
            String declarerName = "West";
            if (declarerDir == Direction.N) declarerName = "North";
            else if (declarerDir == Direction.E) declarerName = "East";
            else if (declarerDir == Direction.S) declarerName = "South";

            Player declarer = activity.getGameController().getPlayers().get(declarerName);

            com.example.bridge.bidding.Tools.Bid finalBid = contractState.getBid();
            Suit suit = null;
            if (finalBid.getStrain() != com.example.bridge.bidding.Tools.Strain.NoTrump) {
                suit = Suit.valueOf(finalBid.getStrain().name().toUpperCase());
            }
            Contract contract = new Contract(finalBid.getLevel(), suit);

            activity.getGameController().onBiddingFinished(contract, declarer);
        }
    }
}
