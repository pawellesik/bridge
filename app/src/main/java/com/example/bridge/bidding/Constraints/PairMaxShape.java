package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Klasa weryfikująca maksymalną łączną liczbę kart pary w danym kolorze.
 * Pozwala sprawdzić czy para NIE posiada fita (np. suma kart obu partnerów <= 7).
 */
public class PairMaxShape {
    /**
     * Weryfikuje maksymalną liczbę kart pary bez deklarowania jej w wiedzy publicznej.
     */
    public static class PairHasMaxShape extends HandConstraint {
        protected final Suit suit;
        protected final int max;
        protected final boolean usePartnerSuit;

        public PairHasMaxShape(Suit suit, int max) {
            this.suit = suit;
            this.max = max;
            this.usePartnerSuit = false;
        }

        public PairHasMaxShape(int max) {
            this.suit = null;
            this.max = max;
            this.usePartnerSuit = true;
        }

        protected Suit getTargetSuit(Call call, PositionState ps) {
            if (usePartnerSuit) {
                Bid lastPartnerBid = ps.getPartner().getBid();
                if (lastPartnerBid != null) return lastPartnerBid.getSuit();
                return null;
            }
            return getSuit(this.suit, call);
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            Suit s = getTargetSuit(call, ps);
            if (s != null) {
                HandSummary.SuitSummary ss = hs.getSuits().get(s);
                if (ss != null) {
                    Range partnerShape = ps.getPartner().getPublicHandSummary().getSuits().get(s).getShape();
                    return (ss.getShape().getMin() + partnerShape.getMin()) <= max;
                }
            }
            return false;
        }
    }

    /**
     * Pokazuje partnerowi maksymalną liczbę kart, jaką możemy mieć, aby nie przekroczyć sumy pary.
     * Zapamiętuje obliczony limit przy pierwszej iteracji, aby zapobiec "pływaniu" wiedzy,
     * gdy partner później pokaże więcej kart w tym samym kolorze.
     */
    public static class PairShowsMaxShape extends PairHasMaxShape implements IShowsHand, IDescribeConstraint {
        private Integer fixedMyMax = null;
        private Suit fixedSuit = null;

        public PairShowsMaxShape(Suit suit, int max) {
            super(suit, max);
        }

        public PairShowsMaxShape(int max) {
            super(max);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            if (fixedMyMax == null) {
                fixedSuit = getTargetSuit(call, ps);
                if (fixedSuit != null) {
                    HandSummary.SuitSummary partnerSuitSum = ps.getPartner().getPublicHandSummary().getSuits().get(fixedSuit);
                    if (partnerSuitSum != null) {
                        int partnerMin = partnerSuitSum.getShape().getMin();
                        fixedMyMax = max - partnerMin;
                    }
                }
            }

            if (fixedSuit != null && fixedMyMax != null) {
                HandSummary.SuitSummary.ShowState suitShow = showHand.getSuits().get(fixedSuit);
                if (suitShow != null) {
                    suitShow.showShape(0, fixedMyMax);
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit s = (fixedSuit != null) ? fixedSuit : getTargetSuit(call, ps);
            if (s != null) {
                int myMin = 0;
                int myMax = max;
                HandSummary.SuitSummary ss = ps.getPublicHandSummary().getSuits().get(s);
                if (ss != null && ss.getShape() != null) {
                    myMin = ss.getShape().getMin();
                    myMax = ss.getShape().getMax();
                }
                Integer fMax = fixedMyMax;
                if (fMax == null) {
                    HandSummary.SuitSummary partnerSuitSum = ps.getPartner().getPublicHandSummary().getSuits().get(s);
                    if (partnerSuitSum != null) {
                        int partnerMin = partnerSuitSum.getShape().getMin();
                        fMax = max - partnerMin;
                    }
                }
                if (fMax != null) {
                    myMax = Math.min(myMax, fMax);
                }
                String val = (myMin == myMax) ? String.valueOf(myMin) : myMin + "-" + myMax;
                return s.toSymbol() + ": " + val;
            }
            return "";
        }
    }
}
