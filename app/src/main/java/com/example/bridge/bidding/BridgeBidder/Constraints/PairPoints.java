package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.Constraint;
import com.example.bridge.bidding.BridgeBidder.Tools.HandConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.HandSummary;
import com.example.bridge.bidding.BridgeBidder.Tools.IDescribeConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.IShowsHand;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;
import com.example.bridge.bidding.BridgeBidder.Tools.StaticConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

/**
 * Zaawansowana klasa do obsługi wspólnej siły punktowej pary.
 * Pozwala na dynamiczne sprawdzanie punktów na podstawie aktualnej wiedzy o obu rękach.
 */
public class PairPoints {
    protected final boolean useStartingPoints; // Czy używać punktów początkowych
    protected final boolean useAgreedStrain;   // Czy bazować na uzgodnionym kolorze atutowym
    protected final Suit suit;                 // Opcjonalny konkretny kolor atutowy
    protected final int min;                   // Minimalna suma punktów pary
    protected final int max;                   // Maksymalna suma punktów pary

    public PairPoints(Suit suit, int min, int max) {
        this.useStartingPoints = false;
        this.useAgreedStrain = false;
        this.suit = suit;
        this.min = min;
        this.max = max;
    }

    public PairPoints(int min, int max) {
        this.useStartingPoints = false;
        this.useAgreedStrain = true;
        this.suit = null;
        this.min = min;
        this.max = max;
    }

    public Suit getSuit(PositionState ps, Call call) {
        if (useAgreedStrain) {
            return ps.getPairState().getLastShownSuit();
        }
        return Constraint.getSuit(this.suit, call);
    }

    public Range getPoints(Call call, PositionState ps, HandSummary hs, boolean highCard) {
        if (highCard) {
            Range hcp = hs.getHighCardPoints();
            return hcp != null ? hcp : new Range(0, 40);
        }
        
        Range points = hs.getStartingPoints();
        Suit s = getSuit(ps, call);
        if (!useStartingPoints && s != null) {
            PositionState firstToShow = ps.getPairState().firstToShow(s);
            if (firstToShow == ps) {
                points = hs.getSuits().get(s).getLongHandPoints();
            } else if (firstToShow != null) {
                points = hs.getSuits().get(s).getDummyPoints();
            }
        }
        if (points == null) {
            points = hs.getPoints();
            if (!useStartingPoints && points != null) {
                points = new Range(points.getMin(), points.getMax() + 8);
            }
        }
        return points != null ? points : new Range(0, 100);
    }

    public boolean dynamicallyConforms(Call call, PositionState ps, HandSummary hs, boolean highCard) {
        Range posPoints = getPoints(call, ps, hs, highCard);
        Range partnerPoints = getPoints(call, ps.getPartner(), ps.getPartner().getPublicHandSummary(), highCard);
        return (posPoints.getMax() + partnerPoints.getMin() >= min && posPoints.getMin() + partnerPoints.getMin() <= max);
    }

    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand, boolean highCard) {
        Range pointsPartner = getPoints(call, ps.getPartner(), ps.getPartner().getPublicHandSummary(), highCard);
        int showMin = Math.max(min - pointsPartner.getMin(), 0);
        int showMax = Math.max(max - pointsPartner.getMin(), 0);
        if (highCard) {
            showHand.showHighCardPoints(showMin, showMax);
            return;
        }
        Suit s = getSuit(ps, call);
        PositionState firstToShow = s == null ? null : ps.getPairState().firstToShow(s);
        if (useStartingPoints || firstToShow == null) {
            showHand.showStartingPoints(showMin, showMax);
        } else if (firstToShow == ps) {
            showHand.getSuits().get(s).showLongHandPoints(showMin, showMax);
        } else {
            showHand.getSuits().get(s).showDummyPoints(showMin, showMax);
        }
    }

    /**
     * Weryfikuje sumę punktów pary bez pokazywania nowych informacji.
     */
    public static class PairHasShownPoints extends StaticConstraint {
        private final PairPoints pairPoints;
        private final boolean highCard;

        public PairHasShownPoints(Suit suit, int min, int max, boolean highCard) {
            this.pairPoints = new PairPoints(suit, min, max);
            this.highCard = highCard;
        }

        @Override
        public boolean conforms(Call call, PositionState ps) {
            Range posPoints = pairPoints.getPoints(call, ps, ps.getPublicHandSummary(), highCard);
            Range partnerPoints = pairPoints.getPoints(call, ps.getPartner(), ps.getPartner().getPublicHandSummary(), highCard);
            int minP = posPoints.getMin() + partnerPoints.getMin();
            return (minP >= pairPoints.min && minP <= pairPoints.max);
        }
    }

    /**
     * Pokazuje partnerowi brakujące punkty do osiągnięcia sumy pary.
     */
    public static class PairShowsPoints extends HandConstraint implements IShowsHand, IDescribeConstraint {
        private final PairPoints pairPoints;
        private final boolean highCard;

        public PairShowsPoints(Suit suit, int min, int max, boolean highCard) {
            this.pairPoints = new PairPoints(suit, min, max);
            this.highCard = highCard;
        }

        public PairShowsPoints(int min, int max, boolean highCard) {
            this.pairPoints = new PairPoints(min, max);
            this.highCard = highCard;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            return pairPoints.dynamicallyConforms(call, ps, hs, highCard);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            pairPoints.showHand(call, ps, showHand, highCard);
        }

        @Override
        public String describe(Call call, PositionState ps) {
            return pairPoints.min + (pairPoints.min == pairPoints.max ? "" : "-" + pairPoints.max) + " pair " + (highCard ? "HCP" : "points");
        }
    }
}




























































































































































































































































































































































































































































































