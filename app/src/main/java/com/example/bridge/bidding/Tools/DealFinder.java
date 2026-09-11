package com.example.bridge.bidding.Tools;

import android.util.Log;

public class DealFinder {
    private static final String TAG = "DealFinder";

    /**
     * Finds a deal where one of the specified players would open with the target bid.
     * @param targetBid The bid we are looking for (e.g., "1S")
     * @param system The bidding system to use (e.g., "NatC" or "WJSimple")
     * @param playersToCheck Directions to check (e.g., N and S)
     * @return A Game object with the matching deal, or null if not found after many attempts.
     */
    public static Game findDealMatchingOpening(String targetBid, String system, Direction[] playersToCheck) {
        Game game = new Game();
        game.bidSystemNS = system;
        game.bidSystemEW = "PassOnly"; // Default for opponents
        
        int maxAttempts = 2000;
        for (int i = 0; i < maxAttempts; i++) {
            game.dealRandomHands();
            
            for (Direction dir : playersToCheck) {
                game.dealer = dir;
                game.getAuction().parse(""); // Reset auction for each check
                
                CallDetails suggestion = BridgeBidder.suggestCall(game);
                if (suggestion != null && suggestion.getCall().toString().equalsIgnoreCase(targetBid)) {
                    Log.d(TAG, "Found matching deal for " + targetBid + " at attempt " + i + " for player " + dir);
                    return game;
                }
            }
        }
        
        Log.e(TAG, "Could not find a deal matching opening " + targetBid + " after " + maxAttempts + " attempts.");
        return null;
    }
}
