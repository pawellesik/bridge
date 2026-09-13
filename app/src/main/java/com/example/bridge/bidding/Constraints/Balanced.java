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
 * Klasa określająca charakter układu ręki:
 * BALANCED, PAIR_BALANCED, NOT_BALANCED lub PAIR_NOT_BALANCED.
 */
public class Balanced {

    public static class ShowsBalanced extends HandConstraint
            implements IShowsHand, IDescribeConstraint {

        private final boolean checkBalanced;
        private final boolean checkPairBalanced;
        private final boolean checkNotBalanced;
        private final boolean checkPairNotBalanced;

        public ShowsBalanced(
                boolean checkBalanced,
                boolean checkPairBalanced,
                boolean checkNotBalanced,
                boolean checkPairNotBalanced) {

            this.checkBalanced = checkBalanced;
            this.checkPairBalanced = checkPairBalanced;
            this.checkNotBalanced = checkNotBalanced;
            this.checkPairNotBalanced = checkPairNotBalanced;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {

            if (checkBalanced) {
                // Standardowy BALANCED:
                // brak singli i renonsów, maksymalnie jeden dubleton,
                // maksymalnie 5 kart w kolorze.

                for (Suit suit : Suit.values()) {
                    Range shape = hs.getSuits().get(suit).getShape();

                    if (shape.getMax() < 2) return false;
                    if (shape.getMin() > 5) return false;
                }

                if (ps.hasHand() && hs == ps.getPrivateHandSummary()) {
                    int doubles = 0;

                    for (Suit suit : Suit.values()) {
                        if (hs.getSuits().get(suit).getShape().getMin() == 2) {
                            doubles++;
                        }
                    }

                    if (doubles > 1) return false;
                }

                return true;
            }

            if (checkPairBalanced) {
                return isPairBalanced(ps, hs);
            }

            if (checkPairNotBalanced) {
                return !isPairBalanced(ps, hs);
            }

            if (checkNotBalanced) {
                return hs.getIsBalanced() == null || !hs.getIsBalanced();
            }

            return false;
        }

        /**
         * Sprawdza PAIR_BALANCED.
         *
         * Dopuszcza renons w kolorze partnera,
         * ale w pozostałych kolorach wymaga minimum jednej karty
         * oraz odpowiedniej jakości krótkiego koloru.
         */
        private boolean isPairBalanced(PositionState ps, HandSummary hs) {

            for (Suit suit : Suit.values()) {

                Range shape = hs.getSuits().get(suit).getShape();
                Range hcp = hs.getSuits().get(suit).getHighCardPoints();

                boolean isPartnerSuit =
                        ps.getPairState().firstToShow(suit) == ps.getPartner();

                if (!isPartnerSuit) {

                    // W kolorach nie licytowanych przez partnera
                    // wymagamy minimum jednej karty.
                    if (shape.getMax() < 1) {
                        return false;
                    }

                    // Jakość krótkich kolorów.
                    if (shape.getMin() == 1
                            && shape.getMax() == 1
                            && hcp.getMax() < 4) {
                        return false;
                    }

                    if (shape.getMin() == 2
                            && shape.getMax() == 2
                            && hcp.getMax() < 3) {
                        return false;
                    }

                    if (shape.getMin() == 3
                            && shape.getMax() == 3
                            && hcp.getMax() < 1) {
                        return false;
                    }
                }
            }

            // Maksymalnie jeden singiel.
            if (ps.hasHand() && hs == ps.getPrivateHandSummary()) {

                int single = 0;

                for (Suit suit : Suit.values()) {
                    if (hs.getSuits().get(suit).getShape().getMin() == 1) {
                        single++;
                    }
                }

                if (single > 1) {
                    return false;
                }
            }

            // Nie ograniczamy długości koloru do 5.
            // Pozwala to np. na licytację 3NT z długim kolorem.
            return true;
        }

        @Override
        public void showHand(
                Call call,
                PositionState ps,
                HandSummary.ShowState showHand) {

            if (checkBalanced) {

                showHand.showIsBalanced(true);

                for (Suit suit : Suit.values()) {
                    showHand.getSuits()
                            .get(suit)
                            .showShape(2, 13);
                }

            } else if (checkPairBalanced) {

                for (Suit suit : Suit.values()) {

                    // Nie nadpisujemy wiedzy o własnych kolorach
                    // (np. otwarcie 1H).
                    if (ps.getPairState().firstToShow(suit) == ps) {
                        continue;
                    }

                    boolean isPartnerSuit =
                            ps.getPairState().firstToShow(suit) == ps.getPartner();

                    // 0-13 w kolorze partnera,
                    // 1-13 w pozostałych kolorach.
                    int min = isPartnerSuit ? 0 : 1;

                    showHand.getSuits()
                            .get(suit)
                            .showShape(min, 13);
                }

            } else if (checkNotBalanced) {

                showHand.showIsBalanced(false);

            } else if (checkPairNotBalanced) {

                showHand.showIsBalanced(false);
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {

            if (checkBalanced) {
                return "balanced";
            }

            if (checkPairBalanced) {
                return "pair_balanced";
            }

            if (checkPairNotBalanced) {
                return "pair_not_balanced";
            }

            if (checkNotBalanced) {
                return "not_balanced";
            }

            return "";
        }
    }
}