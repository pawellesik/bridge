package com.example.bridge.bridgit;

public class LCStandard implements BiddingSystem {
    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        // For the demo, we only handle opening bids for now
        if (ps.getBidRound() == 1) {
            return Open.getPositionCalls(ps);
        }
        
        // Default to a simple PositionCalls with a Pass rule for responses/overcalls in the demo
        PositionCalls defaultChoices = new PositionCalls(ps);
        defaultChoices.addRules(java.util.Collections.singletonList(
            Bidder.shows(Call.Pass)
        ));
        return defaultChoices;
    }
}
