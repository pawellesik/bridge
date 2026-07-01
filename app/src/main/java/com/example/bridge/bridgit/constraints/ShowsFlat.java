package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class ShowsFlat extends Constraint.HandConstraint implements Constraint.IShowsHand, Constraint.IDescribeConstraint {
    protected final boolean desiredValue;

    public ShowsFlat(boolean desiredValue) {
        this.desiredValue = desiredValue;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        return hs.isFlat == null || hs.isFlat == desiredValue;
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        showHand.showIsBalanced(desiredValue);
        if (desiredValue) {
            for (Suit suit : Card.Suits) {
                showHand.suits.get(suit).showShape(3, 4);
            }
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        return desiredValue ? "flat" : "not flat";
    }
}
