package com.example.bridge.bidding.BridgeBidder;

import java.util.*;

public class PairSummary {
    public static class SuitSummary {
        private Range shape;
        private Range points;
        private Boolean stopped;

        public SuitSummary(HandSummary.SuitSummary ss1, HandSummary.SuitSummary ss2) {
            this.shape = addRange(ss1.getShape(), ss2.getShape(), 13);
            this.stopped = null;
            if (Boolean.TRUE.equals(ss1.getStopped()) || Boolean.TRUE.equals(ss2.getStopped())) {
                this.stopped = true;
            } else if (Boolean.FALSE.equals(ss1.getStopped()) || Boolean.FALSE.equals(ss2.getStopped())) {
                this.stopped = false;
            }
        }

        public Range getShape() {
            return shape;
        }

        public Boolean getStopped() {
            return stopped;
        }
    }

    private final Range points;
    private final Map<Suit, SuitSummary> suits = new EnumMap<>(Suit.class);
    private final Set<Suit> shownSuits = new HashSet<>();

    public PairSummary(PositionState ps) {
        HandSummary hs1 = ps.getPublicHandSummary();
        HandSummary hs2 = ps.getPartner().getPublicHandSummary();
        this.points = addRange(hs1.getPoints(), hs2.getPoints(), 100);
        for (Suit suit : Suit.values()) {
            suits.put(suit, new SuitSummary(hs1.getSuits().get(suit), hs2.getSuits().get(suit)));
            if (ps.getPairState().haveShownSuit(suit)) {
                shownSuits.add(suit);
            }
        }
    }

    public static Range addRange(Range r1, Range r2, int max) {
        if (r1 == null) {
            return r2 == null ? new Range(0, max) : r2;
        }
        if (r2 == null) {
            return r1;
        }
        return new Range(Math.min(max, r1.getMin() + r2.getMin()), Math.min(max, r1.getMax() + r2.getMax()));
    }

    public Range getPoints() {
        return points;
    }

    public Map<Suit, SuitSummary> getSuits() {
        return suits;
    }

    public Set<Suit> getShownSuits() {
        return shownSuits;
    }

    public static PairSummary opponents(PositionState ps) {
        return new PairSummary(ps.getLHO());
    }
}
