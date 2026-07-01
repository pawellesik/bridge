package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class RuleOf17 extends Constraint.HandConstraint implements Constraint.IDescribeConstraint {
    private final Suit suit;

    public RuleOf17(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            if (hs.highCardPoints == null) return true;
            int pts = hs.highCardPoints.max;
            pts += hs.suits.get(s).getShape().max;
            return pts >= 17;
        }
        return false;
    }

    @Override
    public String describe(Call call, PositionState ps) {
        return "rule of 17";
    }
}
