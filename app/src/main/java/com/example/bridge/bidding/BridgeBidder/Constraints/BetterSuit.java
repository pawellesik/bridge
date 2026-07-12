package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.licytacja.moje.BridgeBidder.*;

/**
 * Klasa służąca do porównywania dwóch kolorów w ręce gracza.
 * Pozwala określić, który kolor jest dłuższy lub silniejszy (lepszej jakości).
 */
public class BetterSuit {
    /**
     * Weryfikuje relację między dwoma kolorami (np. czy Piki są lepsze od Kierów).
     */
    public static class IsBetterSuit extends HandConstraint {
        protected final Suit better;
        protected final Suit worse;
        protected final Suit defaultIfEqual;
        protected final boolean lengthOnly;

        public IsBetterSuit(Suit better, Suit worse, Suit defaultIfEqual, boolean lengthOnly) {
            this.better = better;
            this.worse = worse;
            this.defaultIfEqual = defaultIfEqual;
            this.lengthOnly = lengthOnly;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            Suit b = getSuit(this.better, call);
            Suit w = getSuit(this.worse, call);
            Suit d = getSuit(this.defaultIfEqual, call);
            
            if (b != null && w != null) {
                Range bShape = hs.getSuits().get(b).getShape();
                Range wShape = hs.getSuits().get(w).getShape();
                
                // 1. Jeśli lepszy kolor jest ewidentnie krótszy niż gorszy -> FALSE
                if (bShape.getMax() < wShape.getMin()) return false;
                
                // 2. Logika dla równych długości
                if (bShape.getMax() == wShape.getMin()) {
                    // Jeśli interesuje nas TYLKO długość, a domyślnym przy remisie jest kolor "gorszy" -> FALSE
                    if (lengthOnly && w == d) return false;

                    // Jeśli sprawdzamy też JAKOŚĆ (to jest Twój przypadek!)
                    if (!lengthOnly) {
                        int bq = hs.getSuits().get(b).getQuality().getMin();
                        int wq = hs.getSuits().get(w).getQuality().getMin();
                        
                        if (bq > wq) return true;  // Kiery (8 pkt) > Piki (4 pkt) -> TRUE
                        if (wq > bq) return false; // Piki silniejsze -> FALSE
                        
                        // Jeśli jakość też jest identyczna, decyduje defaultIfEqual
                        if (w == d) return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Informuje partnera o relacji między dwoma kolorami (pokazuje ich względną siłę/długość).
     */
    public static class ShowsBetterSuit extends IsBetterSuit implements IShowsHand, IDescribeConstraint {
        public ShowsBetterSuit(Suit better, Suit worse, Suit defaultIfEqual, boolean lengthOnly) {
            super(better, worse, defaultIfEqual, lengthOnly);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit b = getSuit(this.better, call);
            Suit w = getSuit(this.worse, call);
            if (b != null && w != null) {
                Range bShape = ps.getPublicHandSummary().getSuits().get(b).getShape();
                Range wShape = ps.getPublicHandSummary().getSuits().get(w).getShape();
                showHand.getSuits().get(w).showShape(wShape.getMin(), Math.min(wShape.getMax(), bShape.getMax()));
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit b = getSuit(this.better, call);
            Suit w = getSuit(this.worse, call);
            Suit d = getSuit(this.defaultIfEqual, call);
            if (b != null && w != null && d != null) {
                boolean betterOrEqual = (d == b);
                if (lengthOnly) {
                    return b.toSymbol() + (betterOrEqual ? " longer or equal to " : " longer than ") + w.toSymbol();
                }
                return b.toSymbol() + (betterOrEqual ? " better or equal to " : " better than ") + w.toSymbol();
            }
            return null;
        }
    }
}
