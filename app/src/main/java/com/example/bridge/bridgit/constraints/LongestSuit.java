package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class LongestSuit extends Constraint.HandConstraint implements Constraint.IDescribeConstraint {
    protected final Suit suit;

    public LongestSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            int longestOther = 0;
            for (Suit other : Card.Suits) {
                if (other != s) {
                    Range otherShape = hs.suits.get(other).getShape();
                    longestOther = Math.max(longestOther, otherShape.max);
                }
            }
            Range shape = hs.suits.get(s).getShape();
            return shape.min > longestOther;
        }
        return false;
    }

    @Override
    public String describe(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            return "longest suit is " + s.toSymbol();
        }
        return null;
    }

    public static class ShowsLongestSuit extends LongestSuit implements Constraint.IShowsHand {
        public ShowsLongestSuit(Suit suit) {
            super(suit);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit s = Constraint.getSuit(suit, call);
            if (s != null) {
                int minOther = 3;
                for (Suit other : Card.Suits) {
                    if (other != s) {
                        Range otherShape = ps.getPublicHandSummary().suits.get(other).getShape();
                        minOther = Math.max(minOther, otherShape.min);
                    }
                }
                Range shape = ps.getPublicHandSummary().suits.get(s).getShape();
                showHand.suits.get(s).showShape(minOther + 1, shape.max);
            }
        }
    }
}
