package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class ShowsQuality extends Constraint.HandConstraint implements Constraint.IShowsHand, Constraint.IDescribeConstraint {
    protected final Suit suit;
    protected final SuitQuality min;
    protected final SuitQuality max;

    public ShowsQuality(Suit suit, SuitQuality min, SuitQuality max) {
        this.suit = suit;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            Range quality = hs.suits.get(s).quality;
            if (quality == null) return true;
            return (min.getValue() <= quality.max && max.getValue() >= quality.min);
        }
        return false;
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            showHand.suits.get(s).showQuality(min, max);
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            String suitSymbol = s.toSymbol();
            String minStr = min.name().toLowerCase();
            String maxStr = max.name().toLowerCase();

            if (max == SuitQuality.Solid) return suitSymbol + " " + minStr + "+";
            if (min == max) return suitSymbol + " " + minStr;
            return suitSymbol + " " + minStr + "–" + maxStr;
        }
        return null;
    }
}
