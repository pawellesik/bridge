package com.example.bridge.bridgit;

public class LCStandard implements BiddingSystem {
    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        if (ps.getBidRound() == 1) {
            // In C# this checks Role == Opener and RoleRound == 1
            // Since we start with Opener role, round 1 is the opening.
            return Open.getPositionCalls(ps);
        }
        
        // Default to a simple PositionCalls with a Pass rule for now
        PositionCalls defaultChoices = new PositionCalls(ps);
        defaultChoices.addPassRule();
        return defaultChoices;
    }
}
