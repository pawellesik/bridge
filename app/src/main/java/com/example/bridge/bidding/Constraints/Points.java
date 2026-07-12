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
 * Kluczowa klasa do obsługi punktacji w brydżu.
 * Obsługuje punkty honorowe (HCP), punkty układowe (Starting) oraz punkty dummy (wsparcie).
 */
public class Points {
    /**
     * Typy punktacji obsługiwane przez system.
     */
    public enum PointType { 
        HighCard, // Punkty za figury (A=4, K=3, Q=2, J=1)
        Starting, // Punkty honorowe + punkty za długość/krótkość
        Dummy     // Punkty obliczane przy wspieraniu koloru partnera
    }

    /**
     * Weryfikuje czy siła ręki mieści się w zadanym zakresie.
     */
    public static class HasPoints extends HandConstraint {
        protected final int min;
        protected final int max;
        protected final Suit trumpSuit;
        protected final PointType pointType;

        public HasPoints(Suit trumpSuit, int min, int max, PointType pointType) {
            this.trumpSuit = trumpSuit;
            this.min = min;
            this.max = max;
            this.pointType = pointType;
        }

        protected Range getPoints(Call call, PositionState ps, HandSummary hs) {
            Range points = null;
            switch (pointType) {
                case HighCard:
                    points = hs.getHighCardPoints();
                    break;
                case Starting:
                    points = hs.getStartingPoints();
                    break;
                case Dummy:
                    Suit suit = getSuit(trumpSuit, call);
                    if (suit != null) {
                        points = hs.getSuits().get(suit).getDummyPoints();
                    }
                    break;
            }
            if (points == null) {
                points = hs.getPoints();
            }
            return points != null ? points : new Range(0, 100);
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            Range p = getPoints(call, ps, hs);
            return (min <= p.getMax() && max >= p.getMin());
        }
    }

    /**
     * Pokazuje partnerowi siłę ręki w punktach wybranego typu.
     */
    public static class ShowsPoints extends HasPoints implements IShowsHand, IDescribeConstraint {
        public ShowsPoints(Suit trumpSuit, int min, int max, PointType pointType) {
            super(trumpSuit, min, max, pointType);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            switch (pointType) {
                case HighCard:
                    showHand.showHighCardPoints(min, max);
                    break;
                case Starting:
                    showHand.showStartingPoints(min, max);
                    break;
                case Dummy:
                    Suit suit = getSuit(trumpSuit, call);
                    if (suit != null) {
                        showHand.getSuits().get(suit).showDummyPoints(min, max);
                    }
                    break;
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            String rangeStr = min + (min == max ? "" : "-" + max);
            switch (pointType) {
                case HighCard: return rangeStr + " HCP";
                case Starting: return rangeStr + " points";
                case Dummy:
                    Suit suit = getSuit(trumpSuit, call);
                    if (suit != null) return rangeStr + " dummy points";
                    return null;
                default: return null;
            }
        }
    }
}




























































































































































































































































































































































































































































































