package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class PairShowsPoints extends Constraint.HandConstraint implements Constraint.IShowsHand, Constraint.IDescribeConstraint {

    private static class InternalLogic {
        protected boolean useStartingPoints;
        protected boolean useAgreedStrain;
        protected final Suit suit;
        protected final int min;
        protected final int max;

        public InternalLogic(Suit suit, int min, int max) {
            this.useStartingPoints = false;
            this.useAgreedStrain = false;
            this.suit = suit;
            this.min = min;
            this.max = max;
        }

        public InternalLogic(int min, int max) {
            this.useStartingPoints = false;
            this.useAgreedStrain = true;
            this.suit = null;
            this.min = min;
            this.max = max;
        }

        public Suit getSuit(PositionState ps, Call call) {
            if (useAgreedStrain) {
                // To be implemented in PairState: lastShownSuit
                return null; 
            }
            return Constraint.getSuit(suit, call);
        }

        public Range getPoints(Call call, PositionState ps, HandSummary hs) {
            Range points = hs.startingPoints;
            Suit s = getSuit(ps, call);
            if (!useStartingPoints && s != null) {
                PositionState firstToShow = ps.getPairState().firstToShow(s);
                if (firstToShow == ps) {
                    points = hs.suits.get(s).longHandPoints;
                } else if (firstToShow != null) {
                    points = hs.suits.get(s).dummyPoints;
                }
            }
            if (points == null) {
                points = hs.points;
                if (!useStartingPoints && points != null) {
                    points = new Range(points.min, points.max + 8);
                }
            }
            return (points == null) ? new Range(0, 100) : points;
        }

        public boolean dynamicallyConforms(Call call, PositionState ps, HandSummary hs) {
            Range posPoints = getPoints(call, ps, hs);
            Range partnerPoints = getPoints(call, ps.partner(), ps.partner().getPublicHandSummary());
            return (posPoints.max + partnerPoints.min >= min && posPoints.min + partnerPoints.min <= max);
        }

        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Range partnerPoints = getPoints(call, ps.partner(), ps.partner().getPublicHandSummary());
            Suit s = Constraint.getSuit(suit, call);
            int showMin = Math.max(min - partnerPoints.min, 0);
            int showMax = Math.max(max - partnerPoints.min, 0);
            PositionState firstToShow = s == null ? null : ps.getPairState().firstToShow(s);
            
            if (useStartingPoints || firstToShow == null) {
                showHand.showStartingPoints(showMin, showMax);
            } else if (firstToShow == ps) {
                showHand.suits.get(s).showLongHandPoints(showMin, showMax);
            } else {
                showHand.suits.get(s).showDummyPoints(showMin, showMax);
            }
        }

        public String describe(Call call, PositionState ps) {
            return Range.getString(min, max, 40) + " pair points";
        }
    }

    private final InternalLogic logic;

    public PairShowsPoints(Suit suit, int min, int max) {
        this.logic = new InternalLogic(suit, min, max);
    }

    public PairShowsPoints(int min, int max) {
        this.logic = new InternalLogic(min, max);
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        return logic.dynamicallyConforms(call, ps, hs);
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        logic.showHand(call, ps, showHand);
    }

    @Override
    public String describe(Call call, PositionState ps) {
        return logic.describe(call, ps);
    }
}
