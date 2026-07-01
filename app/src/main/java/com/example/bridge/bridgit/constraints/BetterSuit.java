package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class BetterSuit extends Constraint.HandConstraint {
    protected final Suit better;
    protected final Suit worse;
    protected final Suit defaultIfEqual;
    protected final boolean lengthOnly;

    public BetterSuit(Suit better, Suit worse, Suit defaultIfEqual, boolean lengthOnly) {
        this.better = better;
        this.worse = worse;
        this.defaultIfEqual = defaultIfEqual;
        this.lengthOnly = lengthOnly;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit b = Constraint.getSuit(better, call);
        Suit w = Constraint.getSuit(worse, call);
        Suit d = Constraint.getSuit(defaultIfEqual, call);

        if (b != null && w != null && d != null) {
            Range betterShape = hs.suits.get(b).getShape();
            Range worseShape = hs.suits.get(w).getShape();

            if (betterShape.max < worseShape.min) return false;
            if (betterShape.max == worseShape.min && w == d) return false;

            if (!lengthOnly && betterShape.equals(worseShape)) {
                Range bQuality = hs.suits.get(b).quality;
                Range wQuality = hs.suits.get(w).quality;
                int bq = (bQuality != null) ? bQuality.min : 0;
                int wq = (wQuality != null) ? wQuality.min : 0;
                if (bq > wq) return true;
                if (wq > bq) return false;
            }
            return true;
        }
        return false;
    }

    public static class ShowsBetterSuit extends BetterSuit implements Constraint.IShowsHand, Constraint.IDescribeConstraint {
        public ShowsBetterSuit(Suit better, Suit worse, Suit defaultIfEqual, boolean lengthOnly) {
            super(better, worse, defaultIfEqual, lengthOnly);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit b = Constraint.getSuit(better, call);
            Suit w = Constraint.getSuit(worse, call);
            if (b != null && w != null) {
                Range betterShape = ps.getPublicHandSummary().suits.get(b).getShape();
                Range worseShape = ps.getPublicHandSummary().suits.get(w).getShape();
                showHand.suits.get(w).showShape(worseShape.min, Math.min(worseShape.max, betterShape.max));
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit b = Constraint.getSuit(better, call);
            Suit w = Constraint.getSuit(worse, call);
            Suit d = Constraint.getSuit(defaultIfEqual, call);
            if (b != null && w != null && d != null) {
                boolean betterOrEqual = (d == b);
                if (lengthOnly) {
                    return b.toSymbol() + (betterOrEqual ? " longer or equal to " : " longer than ") + w.toSymbol();
                }
                return b.toSymbol() + (betterOrEqual ? " better or equal to " : " better than ") + w.toSymbol();
            }
            return null;
        }
    }
}
