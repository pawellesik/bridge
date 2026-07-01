package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class ShowsLosers extends Constraint.HandConstraint implements Constraint.IShowsHand, Constraint.IDescribeConstraint {
    protected final boolean handLosers;
    protected final Suit suit;
    protected final int min;
    protected final int max;

    public ShowsLosers(boolean handLosers, Suit suit, int min, int max) {
        this.handLosers = handLosers;
        this.suit = suit;
        this.min = min;
        this.max = max;
    }

    private Range getLosers(Call call, HandSummary hs) {
        Range losers;
        if (handLosers) {
            losers = hs.losers;
            if (losers == null) losers = new Range(0, 12);
        } else {
            Suit s = Constraint.getSuit(suit, call);
            if (s != null) {
                losers = hs.suits.get(s).losers;
            } else {
                losers = null;
            }
            if (losers == null) losers = new Range(0, 3);
        }
        return losers;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Range losers = getLosers(call, hs);
        return (min <= losers.max && max >= losers.min);
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        if (handLosers) {
            showHand.showLosers(min, max);
        } else {
            Suit s = Constraint.getSuit(suit, call);
            if (s != null) {
                showHand.suits.get(s).showLosers(min, max);
            }
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        String rangeStr = Range.getString(min, max, 10);
        String s = (min == 1 && min == max) ? "" : "s";
        if (handLosers) {
            return rangeStr + " loser" + s + " in hand";
        } else {
            Suit s_suit = Constraint.getSuit(suit, call);
            if (s_suit != null) {
                return rangeStr + " loser" + s + " in " + s_suit;
            }
        }
        return null;
    }
}
