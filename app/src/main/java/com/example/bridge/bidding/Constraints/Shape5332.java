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
 * Sprawdza czy ręka ma układ 5-3-3-2 (jeden kolor 5-kartowy, dwa 3-kartowe i jeden dubleton).
 */
public class Shape5332 {

    public static class Shows5332 extends HandConstraint implements IShowsHand, IDescribeConstraint {
        private final Suit fiveCardSuit;

        public Shows5332() {
            this(null);
        }

        public Shows5332(Suit fiveCardSuit) {
            this.fiveCardSuit = fiveCardSuit;
            this.includeInDescription = true;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            if (hs == null || hs.getSuits() == null) return false;

            if (ps != null && ps.hasHand() && hs == ps.getPrivateHandSummary()) {
                int count5 = 0;
                int count3 = 0;
                int count2 = 0;

                for (Suit suit : Suit.values()) {
                    HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                    if (ss == null) continue;
                    Range shape = ss.getShape();
                    if (shape != null) {
                        int min = shape.getMin();
                        if (min == 5) count5++;
                        else if (min == 3) count3++;
                        else if (min == 2) count2++;
                    }
                }

                boolean exactPattern = (count5 == 1 && count3 == 2 && count2 == 1);
                if (!exactPattern) return false;

                if (fiveCardSuit != null) {
                    HandSummary.SuitSummary ss5 = hs.getSuits().get(fiveCardSuit);
                    return ss5 != null && ss5.getShape() != null && ss5.getShape().getMin() == 5;
                }
                return true;
            }

            int count5 = 0;
            int count3 = 0;
            int count2 = 0;

            for (Suit suit : Suit.values()) {
                HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                if (ss == null) continue;
                Range shape = ss.getShape();
                if (shape != null) {
                    if (shape.getMin() <= 5 && shape.getMax() >= 5) count5++;
                    if (shape.getMin() <= 3 && shape.getMax() >= 3) count3++;
                    if (shape.getMin() <= 2 && shape.getMax() >= 2) count2++;
                }
            }

            if (fiveCardSuit != null) {
                HandSummary.SuitSummary ss5 = hs.getSuits().get(fiveCardSuit);
                boolean suit5Ok = ss5 != null && ss5.getShape() != null && ss5.getShape().getMin() <= 5 && ss5.getShape().getMax() >= 5;
                return count5 >= 1 && count3 >= 2 && count2 >= 1 && suit5Ok;
            }

            return count5 >= 1 && count3 >= 2 && count2 >= 1;
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            if (showHand == null || showHand.getSuits() == null) return;
            showHand.showIsBalanced(true);

            for (Suit suit : Suit.values()) {
                HandSummary.SuitSummary.ShowState ss = showHand.getSuits().get(suit);
                if (ss != null) {
                    if (fiveCardSuit != null) {
                        if (suit == fiveCardSuit) {
                            ss.showShape(5, 5);
                        } else {
                            ss.showShape(2, 3);
                        }
                    } else {
                        ss.showShape(2, 5);
                    }
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            //if (fiveCardSuit != null) {
            //    return "5332 shape (" + fiveCardSuit.toSymbol() + " 5-card suit)";
            //}
            return "5332 shape";
        }
    }
}
