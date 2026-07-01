package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class OppsStopped extends Constraint.HandConstraint implements Constraint.IShowsHand, Constraint.IDescribeConstraint {
    private final boolean desiredValue;

    public OppsStopped(boolean desiredValue) {
        this.desiredValue = desiredValue;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        for (Suit suit : ps.getOpponentsPairState().getShownSuits()) {
            Boolean thisStop = hs.suits.get(suit).stopped;
            Boolean partnerStop = ps.partner().getPublicHandSummary().suits.get(suit).stopped;
            
            if (!Boolean.TRUE.equals(thisStop) && !Boolean.TRUE.equals(partnerStop)) {
                if (Boolean.FALSE.equals(thisStop)) return !desiredValue;
                // If null, it's possible but not certain (matches C# logic)
            }
        }
        return desiredValue;
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        if (desiredValue) {
            for (Suit suit : ps.getOpponentsPairState().getShownSuits()) {
                Boolean partnerStop = ps.partner().getPublicHandSummary().suits.get(suit).stopped;
                if (partnerStop == null) {
                    showHand.suits.get(suit).showStopped(true);
                }
            }
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        int numOppsSuits = ps.getOpponentsPairState().getShownSuits().size();
        if (numOppsSuits > 0) {
            if (desiredValue) {
                return numOppsSuits == 1 ? "opponents suit stopped" : "opponents suits stopped";
            } else {
                return "opponent suit not stopped";
            }
        }
        return null;
    }
}
