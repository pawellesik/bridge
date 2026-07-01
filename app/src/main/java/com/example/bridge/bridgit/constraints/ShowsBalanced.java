package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class ShowsBalanced extends Constraint.HandConstraint implements Constraint.IShowsHand, Constraint.IDescribeConstraint {
    protected boolean desiredValue;

    public ShowsBalanced(boolean desiredValue) {
        this.desiredValue = desiredValue;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        return hs.isBalanced == null || hs.isBalanced == desiredValue;
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        showHand.showIsBalanced(desiredValue);
        if (desiredValue) {
            for (Suit suit : Card.Suits) {
                showHand.suits.get(suit).showShape(2, 5);
            }
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        return desiredValue ? "balanced" : "not balanced";
    }
}
