package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Sprawdza czy ręka ma układ 5-4-2-2 (jeden kolor 5-kartowy, jeden 4-kartowy i dwa dubletony).
 */
public class Shape5422 {

    public static class Shows5422 extends HandConstraint implements IShowsHand, IDescribeConstraint {
        private final Suit suit5; // Kolor 5-kartowy (opcjonalny)
        private final Suit suit4; // Kolor 4-kartowy (opcjonalny)

        public Shows5422() {
            this(null, null);
        }

        public Shows5422(Suit suit5) {
            this(suit5, null);
        }

        public Shows5422(Suit suit5, Suit suit4) {
            this.suit5 = suit5;
            this.suit4 = suit4;
            this.includeInDescription = true;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            if (hs == null || hs.getSuits() == null) return false;

            Suit s5 = getSuit(this.suit5, call);
            Suit s4 = getSuit(this.suit4, call);

            if (ps != null && ps.hasHand() && hs == ps.getPrivateHandSummary()) {
                int exact5 = 0;
                int exact4 = 0;
                int exact2 = 0;
                for (Suit suit : Suit.values()) {
                    HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                    if (ss == null) continue;
                    Range shape = ss.getShape();
                    if (shape != null) {
                        if (shape.getMin() == 5) exact5++;
                        if (shape.getMin() == 4) exact4++;
                        if (shape.getMin() == 2) exact2++;
                    }
                }
                boolean exactMatch = (exact5 == 1 && exact4 == 1 && exact2 == 2);
                if (!exactMatch) return false;

                if (s5 != null) {
                    HandSummary.SuitSummary ss5 = hs.getSuits().get(s5);
                    if (ss5 == null || ss5.getShape() == null || ss5.getShape().getMin() != 5) return false;
                }
                if (s4 != null) {
                    HandSummary.SuitSummary ss4 = hs.getSuits().get(s4);
                    if (ss4 == null || ss4.getShape() == null || ss4.getShape().getMin() != 4) return false;
                }
                return true;
            }

            int count5 = 0;
            int count4 = 0;
            int count2 = 0;

            for (Suit suit : Suit.values()) {
                HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                if (ss == null) continue;
                Range shape = ss.getShape();
                if (shape != null) {
                    if (shape.getMin() <= 5 && shape.getMax() >= 5) count5++;
                    if (shape.getMin() <= 4 && shape.getMax() >= 4) count4++;
                    if (shape.getMin() <= 2 && shape.getMax() >= 2) count2++;
                }
            }

            if (s5 != null) {
                HandSummary.SuitSummary ss5 = hs.getSuits().get(s5);
                if (ss5 == null || ss5.getShape() == null || ss5.getShape().getMin() > 5 || ss5.getShape().getMax() < 5) return false;
            }
            if (s4 != null) {
                HandSummary.SuitSummary ss4 = hs.getSuits().get(s4);
                if (ss4 == null || ss4.getShape() == null || ss4.getShape().getMin() > 4 || ss4.getShape().getMax() < 4) return false;
            }

            return count5 >= 1 && count4 >= 1 && count2 >= 2;
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            if (showHand == null || showHand.getSuits() == null) return;
            Suit s5 = getSuit(this.suit5, call);
            Suit s4 = getSuit(this.suit4, call);

            for (Suit suit : Suit.values()) {
                HandSummary.SuitSummary.ShowState ss = showHand.getSuits().get(suit);
                if (ss != null) {
                    if (s5 != null && suit == s5) {
                        ss.showShape(5, 5);
                    } else if (s4 != null && suit == s4) {
                        ss.showShape(4, 4);
                    } else if (s5 != null || s4 != null) {
                        ss.showShape(2, 5);
                    } else {
                        ss.showShape(2, 5);
                    }
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit s5 = getSuit(this.suit5, call);
            Suit s4 = getSuit(this.suit4, call);

            //if (s5 != null && s4 != null) {
            //    return "5422 shape (" + s5.toSymbol() + " 5, " + s4.toSymbol() + " 4)";
            //} else if (s5 != null) {
            //    return "5422 shape (" + s5.toSymbol() + " 5)";
            //}
            return "5422 shape";
        }
    }
}
