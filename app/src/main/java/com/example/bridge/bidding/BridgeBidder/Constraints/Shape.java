package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.licytacja.moje.BridgeBidder.*;

/**
 * Podstawowa klasa do definiowania układu (długości kolorów) w ręce.
 * Pozwala określić ile kart w danym kolorze musi posiadać gracz.
 */
public class Shape {
    /**
     * Sprawdza wymaganą długość koloru bez jej deklarowania w wiedzy publicznej.
     */
    public static class HasShape extends HandConstraint {
        protected final Suit suit;
        protected final int min;
        protected final int max;

        public HasShape(Suit suit, int min, int max) {
            this.suit = suit;
            this.min = min;
            this.max = max;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                Range shape = hs.getSuits().get(s).getShape();
                return (shape.getMax() >= min && shape.getMin() <= max);
            }
            return false;
        }
    }

    /**
     * Pokazuje partnerowi informację o posiadanej liczbie kart w danym kolorze.
     */
    public static class ShowsShape extends HasShape implements IShowsHand, IDescribeConstraint {
        public ShowsShape(Suit suit, int min, int max) {
            super(suit, min, max);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                showHand.getSuits().get(s).showShape(min, max);
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                String rangeStr = min + (min == max ? "" : "-" + max);
                return rangeStr + " " + s.toSymbol();
            }
            return null;
        }
    }

    /**
     * Uproszczona wersja sprawdzająca tylko minimalną liczbę kart w kolorze.
     */
    public static class HasMinShape extends HandConstraint {
        protected final Suit suit;
        protected final int min;

        public HasMinShape(Suit suit, int min) {
            this.suit = suit;
            this.min = min;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                return hs.getSuits().get(s).getShape().getMin() >= min;
            }
            return false;
        }
    }
}
