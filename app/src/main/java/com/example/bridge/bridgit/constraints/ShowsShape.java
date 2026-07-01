package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class ShowsShape extends HasShape implements Constraint.IShowsHand, Constraint.IDescribeConstraint {

    public ShowsShape(Suit suit, int min, int max) {
        super(suit, min, max);
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            showHand.suits.get(s).showShape(min, max);
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            return Range.getString(min, max, 10) + " " + s.toSymbol();
        }
        return null;
    }
}
