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

        public Shows4441() {
            this.includeInDescription = true;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            if (hs == null || hs.getSuits() == null) return false;

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
                    ss.showShape(1, 4);
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            return "4441 shape";
        }
    }
}
