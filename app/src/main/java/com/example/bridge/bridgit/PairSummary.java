package com.example.bridge.bridgit;

import java.util.*;

public class PairSummary {

    public static class SuitSummary {
        public Range shape;
        public Range points;
        public Range quality;
        public Boolean haveQueen;
        public Boolean stopped;

        public SuitSummary(HandSummary.SuitSummary ss1, HandSummary.SuitSummary ss2) {
            this.shape = addRange(ss1.getShape(), ss2.getShape(), 13);
            this.stopped = null;
            if (Boolean.TRUE.equals(ss1.stopped) || Boolean.TRUE.equals(ss2.stopped)) {
                this.stopped = true;
            } else if (Boolean.FALSE.equals(ss1.stopped) || Boolean.FALSE.equals(ss2.stopped)) {
                this.stopped = false;
            }
        }
    }

    public static Range addRange(Range r1, Range r2, int max) {
        if (r1 == null) {
            return r2 != null ? r2 : new Range(0, max);
        }
        if (r2 == null) {
            return r1;
        }
        return new Range(Math.min(max, r1.min + r2.min), Math.min(max, r1.max + r2.max));
    }

    public Range points;
    public final Map<Suit, SuitSummary> suits = new HashMap<>();
    public final Set<Suit> shownSuits = new HashSet<>();

    public PairSummary(PositionState ps) {
        HandSummary hs1 = ps.getPublicHandSummary();
        HandSummary hs2 = ps.partner().getPublicHandSummary();
        this.points = addRange(hs1.points, hs2.points, 100);
        for (Suit suit : Card.Suits) {
            suits.put(suit, new SuitSummary(hs1.suits.get(suit), hs2.suits.get(suit)));
            if (ps.getPairState().haveShownSuit(suit)) {
                shownSuits.add(suit);
            }
        }
    }

    public static PairSummary opponents(PositionState ps) {
        return new PairSummary(ps.leftHandOpponent());
    }
}
