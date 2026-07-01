package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class HasPoints extends Constraint.HandConstraint {
    public enum PointType { HighCard, Starting, Dummy }

    protected final int min;
    protected final int max;
    protected final Suit trumpSuit;
    protected final PointType pointType;

    public HasPoints(Suit trumpSuit, int min, int max, PointType pointType) {
        this.trumpSuit = trumpSuit;
        this.min = min;
        this.max = max;
        this.pointType = pointType;
    }

    protected Range getPoints(Call call, PositionState ps, HandSummary hs) {
        Range p = null;
        switch (pointType) {
            case HighCard:
                p = hs.highCardPoints;
                break;
            case Starting:
                p = hs.startingPoints;
                break;
            case Dummy:
                Suit suit = Constraint.getSuit(trumpSuit, call);
                if (suit != null) {
                    p = hs.suits.get(suit).dummyPoints;
                }
                break;
        }
        if (p == null) {
            p = hs.points;
        }
        return p != null ? p : new Range(0, 100);
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Range points = getPoints(call, ps, hs);
        return (min <= points.max && max >= points.min);
    }
}
