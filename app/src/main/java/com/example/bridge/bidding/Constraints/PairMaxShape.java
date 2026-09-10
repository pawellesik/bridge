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
     */
    public static class PairShowsMaxShape extends PairHasMaxShape implements IShowsHand, IDescribeConstraint {
        public PairShowsMaxShape(Suit suit, int max) {
            super(suit, max);
        }

        public PairShowsMaxShape(int max) {
            super(max);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit s = getTargetSuit(call, ps);
            if (s != null) {
                HandSummary.SuitSummary ss = ps.getPublicHandSummary().getSuits().get(s);
                if (ss != null) {
                    Range partnerShape = ps.getPartner().getPublicHandSummary().getSuits().get(s).getShape();
                    int newMax = max - partnerShape.getMin();

                    if (newMax < ss.getShape().getMax()) {
                        HandSummary.SuitSummary.ShowState suitShow = showHand.getSuits().get(s);
                        if (suitShow != null) {
                            suitShow.showShape(Math.min(ss.getShape().getMin(), newMax), newMax);
                        }
                    }
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit s = getTargetSuit(call, ps);
            return "max " + max + " pair" + (s != null ? " " + s.toSymbol() : "");
        }
    }
}
