package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.bridge.bidding.BridgeBidder.*;

/**
 * Klasa implementująca Metodę Liczenia Przegranych (Losing Trick Count - LTC).
 * Pozwala ocenić siłę ręki na podstawie liczby brakujących asów, króli i dam w kolorach.
 */
public class Losers {
    /**
     * Weryfikuje liczbę przegrywających (całej ręki lub konkretnego koloru).
     */
    public static class HasLosers extends HandConstraint {
        protected final int min;
        protected final int max;
        protected final Suit suit;
        protected final boolean handLosers;

        public HasLosers(boolean handLosers, Suit suit, int min, int max) {
            this.handLosers = handLosers;
            this.suit = suit;
            this.min = min;
            this.max = max;
        }

        private Range getLosers(Call call, HandSummary hs) {
            Range losers;
            if (handLosers) {
                losers = hs.getLosers();
                if (losers == null) losers = new Range(0, 12);
            } else {
                Suit s = getSuit(this.suit, call);
                if (s != null) {
                    losers = hs.getSuits().get(s).getLosers();
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
            return (min <= losers.getMax() && max >= losers.getMin());
        }
    }

    /**
     * Informuje partnera o liczbie przegrywających lew w ręce lub kolorze.
     */
    public static class ShowsLosers extends HasLosers implements IShowsHand, IDescribeConstraint {
        public ShowsLosers(boolean handLosers, Suit suit, int min, int max) {
            super(handLosers, suit, min, max);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            if (handLosers) {
                showHand.showLosers(min, max);
            } else {
                Suit s = getSuit(this.suit, call);
                if (s != null) {
                    showHand.getSuits().get(s).showLosers(min, max);
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            String rangeStr = min + (min == max ? "" : "-" + max);
            String s = (min == 1 && min == max) ? "" : "s";
            if (handLosers) {
                return rangeStr + " loser" + s + " in hand";
            } else {
                Suit suit = getSuit(this.suit, call);
                if (suit != null) {
                    return rangeStr + " loser" + s + " in " + suit.name();
                }
            }
            return null;
        }
    }
}




























































































































































































































































































































































































































































































