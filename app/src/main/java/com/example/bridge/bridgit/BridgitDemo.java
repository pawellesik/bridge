package com.example.bridge.bridgit;

import android.util.Log;
import java.util.Map;

public class BridgitDemo {
    private static final String TAG = "BridgitDemo";

    public void runDemo() {
        Log.i(TAG, "!!! Bridgit Java Full Auction Demo STARTED !!!");

        try {
            Game game = new Game();
            game.setDealer(Direction.N);
            game.setVulnerable(Vulnerable.None);

            // North: 16 HCP, 5 Spades (Opening 1S)
            // East: 5 HCP (Pass)
            // South: 10 HCP, 3 Spades (Response 2S)
            // West: 9 HCP (Pass)
            // Auction should be: 1S - P - 2S - P - P - P
            String dealStr = "N:AKQJT98765.A.A.A 4.KQJT987.KQJT.2 3.65.87659.KQJT9 2.432.432.876543";
            dealStr = "N:K73.AT94.J82.854 AQ4.KQ.Q95.QT976 J865.863.KT763.J T92.J752.A4.AK32";
            dealStr = "N:Q54.T5.AT983.K85 9862.K93.KQ52.76 T.AQ82.76.AJT942 AKJ73.J764.J4.Q3";
            dealStr = "N:862.AKJ8.5.JT873 3.75.AQJ74.Q9542 QJ7.Q92.KT962.K6 AKT954.T643.83.A";
            dealStr = "N:9532.A2.K762.JT4 KT.KQT965.AJ3.K3 AQJ764.87.Q984.2 8.J43.T5.AQ98765";



            game.parseDeal(dealStr, false);
            BiddingState biddingState = new BiddingState(game);

            int turns = 0;
            while (!biddingState.getContract().isAuctionComplete() && turns < 15) {
                turns++;
                PositionState next = biddingState.getNextToAct();
                PositionCalls choices = biddingState.getCallChoices();
                CallDetails best = choices.getBestCall();
                
                Call call = (best != null) ? best.getCall() : Call.Pass;
                
                Log.i(TAG, "Turn " + turns + " | Player: " + next.getDirection() + 
                           " | Role: " + next.getRole() + " (Round " + next.getRoleRound() + ")" +
                           " | CALL: " + call);
                
                biddingState.makeCall(call);
            }

            Log.i(TAG, "Auction Finished in " + turns + " turns.");
            Log.i(TAG, "Final Contract: " + biddingState.getContract().toString());
            Log.i(TAG, "!!! Bridgit Java Full Auction Demo COMPLETED !!!");

        } catch (Exception e) {
            Log.e(TAG, "Demo CRASH: " + e.getMessage(), e);
        }
    }
}
