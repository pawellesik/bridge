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
 * Sprawdza czy ręka ma skład 4-4-4-1 (trzy czwórki i jeden singiel).
 */
public class Shape4441 {

    public static class Shows4441 extends HandConstraint implements IShowsHand, IDescribeConstraint {
        private final Suit singletonSuit;

        public Shows4441() {
            this(null);
        }

        public Shows4441(Suit singletonSuit) {
            this.singletonSuit = singletonSuit;
            this.includeInDescription = true;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            if (hs == null || hs.getSuits() == null) return false;

            if (singletonSuit != null) {
                if (ps != null && ps.hasHand() && hs == ps.getPrivateHandSummary()) {
                    int exact4 = 0;
                    int exact1 = 0;
                    for (Suit suit : Suit.values()) {
                        HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                        if (ss == null) continue;
                        Range shape = ss.getShape();
                        if (shape != null) {
                            if (shape.getMin() == 4) exact4++;
                            if (shape.getMin() == 1) exact1++;
                        }
                    }
                    HandSummary.SuitSummary singSS = hs.getSuits().get(singletonSuit);
                    return exact4 == 3 && exact1 == 1 && singSS != null && singSS.getShape() != null && singSS.getShape().getMin() == 1;
                }

                int count4 = 0;
                int count1 = 0;

                for (Suit suit : Suit.values()) {
                    HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                    if (ss == null) continue;
                    Range shape = ss.getShape();
                    if (shape != null) {
                        if (shape.getMin() <= 4 && shape.getMax() >= 4) {
                            count4++;
                        }
                        if (shape.getMin() <= 1 && shape.getMax() >= 1) {
                            count1++;
                        }
                    }
                }
                HandSummary.SuitSummary singSS = hs.getSuits().get(singletonSuit);
                boolean singOk = singSS != null && singSS.getShape() != null && singSS.getShape().getMin() <= 1 && singSS.getShape().getMax() >= 1;

                return count4 >= 3 && count1 >= 1 && singOk;
            }

            int count4 = 0;
            int count1 = 0;

            for (Suit suit : Suit.values()) {
                HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                if (ss == null) continue;
                Range shape = ss.getShape();
                if (shape != null) {
                    if (shape.getMin() <= 4 && shape.getMax() >= 4) {
                        count4++;
                    }
                    if (shape.getMin() <= 1 && shape.getMax() >= 1) {
                        count1++;
                    }
                }
            }

            if (ps != null && ps.hasHand() && hs == ps.getPrivateHandSummary()) {
                int exact4 = 0;
                int exact1 = 0;
                for (Suit suit : Suit.values()) {
                    HandSummary.SuitSummary ss = hs.getSuits().get(suit);
                    if (ss == null) continue;
                    Range shape = ss.getShape();
                    if (shape != null) {
                        if (shape.getMin() == 4) exact4++;
                        if (shape.getMin() == 1) exact1++;
                    }
                }
                return exact4 == 3 && exact1 == 1;
            }

            return count4 >= 3 && count1 >= 1;
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            if (showHand == null || showHand.getSuits() == null) return;
            for (Suit suit : Suit.values()) {
                HandSummary.SuitSummary.ShowState ss = showHand.getSuits().get(suit);
                if (ss != null) {
                    if (singletonSuit != null) {
                        if (suit == singletonSuit) {
                            ss.showShape(1, 1);
                        } else {
                            ss.showShape(4, 4);
                        }
                    } else {
                        ss.showShape(1, 4);
                    }
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            if (singletonSuit != null) {
                return "4441 shape (" + singletonSuit.toSymbol() + " singleton)";
            }
            return "4441 shape";
        }
    }
}
