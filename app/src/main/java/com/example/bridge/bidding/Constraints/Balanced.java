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
 * Klasa określająca czy ręka jest zrównoważona (bez singli i renonsów).
 * Ręka zrównoważona może posiadać co najwyżej jeden dubleton.
 */
public class Balanced {
    /**
     * Pokazuje i opisuje zrównoważony charakter ręki.
     */
    public static class ShowsBalanced extends HandConstraint implements IShowsHand, IDescribeConstraint {
        private final boolean checkBalanced;
        private final boolean checkPairBalanced;

        public ShowsBalanced(boolean checkBalanced, boolean checkPairBalanced) {
            this.checkBalanced = checkBalanced;
            this.checkPairBalanced = checkPairBalanced;
        }

        public ShowsBalanced(boolean checkBalanced) {
            this(checkBalanced, false);
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            if (checkBalanced) {
                // Standardowy BALANCED (np. otwarcie 1NT): brak singli, max 1 dubleton, max 5 kart
                for (Suit suit : Suit.values()) {
                    Range shape = hs.getSuits().get(suit).getShape();
                    if (shape.getMax() < 2) return false;
                    if (shape.getMin() > 5) return false;
                }
                if (ps.hasHand() && hs == ps.getPrivateHandSummary()) {
                    int doubles = 0;
                    for (Suit suit : Suit.values()) if (hs.getSuits().get(suit).getShape().getMin() == 2) doubles++;
                    if (doubles > 1) return false;
                }
                return true;
            }

            if (checkPairBalanced) {
                // PAIR_BALANCED: dopuszcza renons w kolorze partnera, sprawdza jakość w pozostałych
                for (Suit suit : Suit.values()) {
                    Range shape = hs.getSuits().get(suit).getShape();
                    Range hcp = hs.getSuits().get(suit).getHighCardPoints();
                    boolean isPartnerSuit = ps.getPairState().firstToShow(suit) == ps.getPartner();

                    if (!isPartnerSuit) {
                        // W kolorach nie licytowanych przez partnera wymagamy min 1 karty
                        if (shape.getMax() < 1) return false; 
                        
                        // Jakość krótkich kolorów
                        if (shape.getMin() == 1 && shape.getMax() == 1 && hcp.getMax() < 4) return false;
                        if (shape.getMin() == 2 && shape.getMax() == 2 && hcp.getMax() < 3) return false;
                        if (shape.getMin() == 3 && shape.getMax() == 3 && hcp.getMax() < 1) return false;
                    }
                    // W tym trybie nie ograniczamy długości koloru do 5, aby pozwolić na licytację 3NT z długim kolorem
                }
                return true;
            }

            // Przypadek NOT_BALANCED
            if (hs.getIsBalanced() != null && hs.getIsBalanced()) return false;
            return true;
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            if (checkBalanced) {
                showHand.showIsBalanced(true);
                for (Suit suit : Suit.values()) {
                    showHand.getSuits().get(suit).showShape(2, 5);
                }
            } else if (checkPairBalanced) {
                for (Suit suit : Suit.values()) {
                    // Nie nadpisujemy wiedzy o własnych kolorach (np. otwarcie 1H)
                    if (ps.getPairState().firstToShow(suit) == ps) continue;

                    boolean isPartnerSuit = ps.getPairState().firstToShow(suit) == ps.getPartner();
                    // Obiecujemy 0-5 w kolorze partnera (brak wymuszonego fita)
                    // Obiecujemy 1-5 w pozostałych (brak renonsów)
                    int min = isPartnerSuit ? 0 : 1;
                    showHand.getSuits().get(suit).showShape(min, 5);
                }
            } else {
                showHand.showIsBalanced(false);
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            if (this.checkPairBalanced) return "pair_balanced";
            if (this.checkBalanced) return "balanced";
            return "not balanced";
        }
    }
}




























































































































































































































































































































































































































































































