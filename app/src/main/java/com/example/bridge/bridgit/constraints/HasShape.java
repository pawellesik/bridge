package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class HasShape extends Constraint.HandConstraint {
    protected final Suit suit;
    protected final int min;
    protected final int max;

    public HasShape(Suit suit, int min, int max) {
        this.suit = suit;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            Range shape = hs.suits.get(s).getShape();
            return (shape.max >= min && shape.min <= max);
        }
        return false;
    }
}
