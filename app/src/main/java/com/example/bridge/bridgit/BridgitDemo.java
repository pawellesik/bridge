package com.example.bridge.bridgit;

import android.util.Log;
import java.util.Map;

public class BridgitDemo {
    private static final String TAG = "BridgitDemo";

    public void runDemo() {
        Log.i(TAG, "!!! Bridgit Java Dynamic Demo STARTED !!!");

        try {
            // 1. Create a Game
            Game game = new Game();
            game.setDealer(Direction.N);
            game.setVulnerable(Vulnerable.None);

            // NORTH: 10 Spades (AKQJT98765), 1H(A), 1D(A), 1C(A) -> 22 HCP, 28 Starting
            // EAST: 1S(4), 7 Hearts (KQJT987), 4D (KQJT), 1C(2) -> 12 HCP
            String dealStr = "N:AKQJT98765.A.A.A 4.KQJT987.KQJT.2 3.65.87659.KQJT9 2.432.432.876543";

            game.parseDeal(dealStr, false);
            Log.i(TAG, "Deal parsed successfully");

            // 3. Initialize BiddingState
            BiddingState biddingState = new BiddingState(game);
            Log.i(TAG, "BiddingState initialized");

            // 4. Run dynamic simulation
            while (!biddingState.getContract().isAuctionComplete()) {

                PositionState next = biddingState.getNextToAct();
                
                Log.i(TAG, "--- Decision turn for " + next.getDirection() + " ---");
                
                // DIAGNOSTIC: Log real private hand stats
                Hand hand = game.getDeal().get(next.getDirection());
                if (hand != null) {
                    Map<Suit, Integer> counts = hand.countsBySuit();
                    Log.i(TAG, "Hand: " + hand.toString());
                    Log.i(TAG, "Stats -> HCP: " + hand.highCardPoints() + " | S:" + counts.get(Suit.Spades) + " H:" + counts.get(Suit.Hearts) + " D:" + counts.get(Suit.Diamonds) + " C:" + counts.get(Suit.Clubs));
                }

                PositionCalls choices = biddingState.getCallChoices();
                CallDetails best = choices.getBestCall();
                
                Call callToMake = (best != null) ? best.getCall() : Call.Pass;
                
                Log.i(TAG, ">>> " + next.getDirection() + " CALLS: " + callToMake);
                biddingState.makeCall(callToMake);
                
                logStatus(biddingState);
            }

            Log.i(TAG, "!!! Bridgit Java Dynamic Demo COMPLETED SUCCESSFULY !!!");

        } catch (Exception e) {
            Log.e(TAG, "CRASH in BridgitDemo: " + e.getMessage(), e);
        }
    }

    private void logStatus(BiddingState state) {
        try {
            ContractState contract = state.getContract();
            Log.i(TAG, "Status -> Contract: " + contract.toString() + " | Done: " + contract.isAuctionComplete());
        } catch (Exception e) {
            Log.e(TAG, "Error in logStatus: " + e.getMessage());
        }
    }
}
