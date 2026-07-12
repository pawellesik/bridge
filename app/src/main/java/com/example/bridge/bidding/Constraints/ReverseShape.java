package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Constraint obsługujący licytację typu "Reverse" (odwrotka).
 * Wymusza specyficzny układ ręki dla silnych odzywek dwukolorowych,
 * gdzie pierwszy licytowany kolor musi być dłuższy od drugiego (układ min. 5-4).
 */
public class ReverseShape {
    /**
     * Weryfikuje czy układ ręki pozwala na zalicytowanie odzywki typu Reverse.
     */
    public static class HasReverseShape extends HandConstraint {
        protected Suit openSuit(PositionState ps) {
            Bid openingBid = ps.getBiddingState().getOpeningBid();
            return openingBid != null ? openingBid.getSuit() : null;
        }

        protected Suit bidSuit(Call call) {
            return call instanceof Bid ? ((Bid) call).getSuit() : null;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            if (ps.isReverse(call)) {
                Suit oSuit = openSuit(ps);
                Suit bSuit = bidSuit(call);
                if (oSuit != null && bSuit != null) {
                    Range openingShape = hs.getSuits().get(oSuit).getShape();
                    Range reverseSuitShape = hs.getSuits().get(bSuit).getShape();
                    return (reverseSuitShape.getMax() > 3 && reverseSuitShape.getMin() < openingShape.getMax());
                }
            }
            return false;
        }
    }

    /**
     * Pokazuje partnerowi minimalną długość obu kolorów przy licytacji Reverse.
     */
    public static class ShowsReverseShape extends HasReverseShape implements IShowsHand {
        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit oSuit = openSuit(ps);
            Suit bSuit = bidSuit(call);
            if (oSuit != null && bSuit != null) {
                Range openingShape = ps.getPublicHandSummary().getSuits().get(oSuit).getShape();
                showHand.getSuits().get(bSuit).showShape(4, openingShape.getMax() - 1);
                showHand.getSuits().get(oSuit).showShape(5, openingShape.getMax());
            }
        }
    }
}




























































































































































































































































































































































































































































































