package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.HandConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.HandSummary;
import com.example.bridge.bidding.BridgeBidder.Tools.IDescribeConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.IShowsHand;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

/**
 * Klasa weryfikująca minimalną łączną liczbę kart pary w danym kolorze.
 * Pozwala sprawdzić czy para posiada np. fit 8-kartowy (suma kart obu partnerów).
 */
public class PairMinShape {
    /**
     * Weryfikuje łączną liczbę kart pary bez deklarowania jej w wiedzy publicznej.
     */
    public static class PairHasMinShape extends HandConstraint {
        protected final Suit suit;
        protected final int min;
        protected final boolean desiredValue;
        protected final boolean useContractSuit;

        public PairHasMinShape(Suit suit, int min, boolean desiredValue) {
            this.suit = suit;
            this.min = min;
            this.desiredValue = desiredValue;
            this.useContractSuit = false;
        }

        public PairHasMinShape(int min, boolean desiredValue) {
            this.suit = null;
            this.min = min;
            this.desiredValue = desiredValue;
            this.useContractSuit = true;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            Suit s = null;
            if (useContractSuit) {
                if (ps.getBiddingState().getContract().isOurs(ps.getDirection())) {
                    Call contractBid = ps.getBiddingState().getContract().getBid();
                    if (contractBid instanceof Bid) {
                        s = ((Bid) contractBid).getSuit();
                    }
                }
                if (s == null) return false;
            } else {
                s = getSuit(this.suit, call);
            }
            if (s != null) {
                Range shape = hs.getSuits().get(s).getShape();
                Range partnerShape = ps.getPartner().getPublicHandSummary().getSuits().get(s).getShape();
                return (shape.getMax() + partnerShape.getMin() >= min) == desiredValue;
            }
            return false;
        }
    }

    /**
     * Pokazuje partnerowi brakującą liczbę kart do osiągnięcia sumy pary.
     */
    public static class PairShowsMinShape extends PairHasMinShape implements IShowsHand, IDescribeConstraint {
        public PairShowsMinShape(Suit suit, int min, boolean desiredValue) {
            super(suit, min, desiredValue);
        }

        public PairShowsMinShape(int min, boolean desiredValue) {
            super(min, desiredValue);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                Range shape = ps.getPublicHandSummary().getSuits().get(s).getShape();
                Range partnerShape = ps.getPartner().getPublicHandSummary().getSuits().get(s).getShape();
                int newMin = min - partnerShape.getMin();
                if (newMin > shape.getMin()) {
                    showHand.getSuits().get(s).showShape(newMin, Math.max(newMin, shape.getMax()));
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                return min + "+ pair " + s.toSymbol();
            }
            return null;
        }
    }
}




























































































































































































































































































































































































































































































