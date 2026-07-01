package com.example.bridge.bridgit;

import android.util.Log;

public class BridgitDemo {
    private static final String TAG = "BridgitDemo";

    public void runDemo() {
        Log.i(TAG, "!!! Bridgit Java Demo STARTED !!!");

        try {
            // 1. Create a Game
            Game game = new Game();
            game.setDealer(Direction.N);
            game.setVulnerable(Vulnerable.None);

            // 2. Set up a deal (Standard PBN format: 13 cards per hand, T for 10)
            String dealStr = "N:AKJ.Q9.T852.7432 965.AK82.KJ.QT65 T87432.J74.A6.A9 Q.T653.Q9743.KJ8";
            game.parseDeal(dealStr, false);
            Log.i(TAG, "Deal parsed successfully");

            // 3. Initialize BiddingState
            BiddingState biddingState = new BiddingState(game);
            Log.i(TAG, "BiddingState initialized");

            // 4. Simulate some calls
            logStatus(biddingState);

            Log.i(TAG, "Simulating North PASS...");
            biddingState.makeCall(Call.Pass);
            logStatus(biddingState);

            Log.i(TAG, "Simulating East 1C...");
            biddingState.makeCall(Call.parse("1C"));
            logStatus(biddingState);

            Log.i(TAG, "Simulating South PASS...");
            biddingState.makeCall(Call.Pass);
            logStatus(biddingState);

            Log.i(TAG, "Simulating West 1H...");
            biddingState.makeCall(Call.parse("1H"));
            logStatus(biddingState);

            Log.i(TAG, "!!! Bridgit Java Demo COMPLETED SUCCESSFULY !!!");

        } catch (Exception e) {
            Log.e(TAG, "CRASH in BridgitDemo: " + e.getMessage(), e);
        }
    }

    private void logStatus(BiddingState state) {
        try {
            PositionState next = state.getNextToAct();
            ContractState contract = state.getContract();
            
            Log.i(TAG, "Status -> Contract: " + contract.toString() + 
                       " | Next: " + next.getDirection() + 
                       " | Done: " + contract.isAuctionComplete());
        } catch (Exception e) {
            Log.e(TAG, "Error in logStatus: " + e.getMessage());
        }
    }
}
