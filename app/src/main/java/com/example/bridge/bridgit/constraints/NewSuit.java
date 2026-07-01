package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class NewSuit extends Constraint.StaticConstraint implements Constraint.IDescribeConstraint {
    private final Suit suit;

    public NewSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            return ps.getPairState().firstToShow(s) == null;
        }
        return false;
    }

    @Override
    public String describe(Call call, PositionState ps) {
        return "new suit";
    }
}
