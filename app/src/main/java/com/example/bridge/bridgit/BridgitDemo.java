package com.example.bridge.bridgit;

import android.util.Log;

public class BridgitDemo {
    private static final String TAG = "BridgitDemo";

    public void runDemo() {
        Log.i(TAG, "!!! Bridgit Java Dynamic Demo STARTED !!!");

        try {
            // 1. Create a Game
            Game game = new Game();
            game.setDealer(Direction.N);
            game.setVulnerable(Vulnerable.None);

            // 2. Set up a deal (Standard PBN format: [FirstPlayer]:[Hand1] [Hand2] [Hand3] [Hand4])
            // North has 20 HCP, 5 Spades - should open 1S
            // East has a weak hand - should pass
            // South has a weak hand - should pass
            // West has a weak hand - should pass
            String dealStr = "N:Q9763.J.AJ82.T87 AT.AT.KQ63.A9432 J4.Q987652.7.Q65 K852.K43.T954.KJ";

            game.parseDeal(dealStr, false);
            Log.i(TAG, "Deal parsed successfully");

            // 3. Initialize BiddingState
            BiddingState biddingState = new BiddingState(game);
            Log.i(TAG, "BiddingState initialized");

            // 4. Run dynamic simulation
            int safetyLimit = 0;
            while (!biddingState.getContract().isAuctionComplete() && safetyLimit < 20) {
                safetyLimit++;
                
                PositionState next = biddingState.getNextToAct();
                PositionCalls choices = biddingState.getCallChoices();
                CallDetails best = choices.getBestCall();
                
                Call callToMake = (best != null) ? best.getCall() : Call.Pass;
                
                Log.i(TAG, next.getDirection() + " decides to call: " + callToMake);
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
            Log.i(TAG, "Current Status -> Contract: " + contract.toString() +
                       " | Done: " + contract.isAuctionComplete());
        } catch (Exception e) {
            Log.e(TAG, "Error in logStatus: " + e.getMessage());
        }
    }
}
