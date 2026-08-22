package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

/**
 *
 */
public class PairBalanced {
    /**
     *
     */
    public static class ShowsPairBalanced extends HandConstraint implements IShowsHand, IDescribeConstraint {

        public ShowsPairBalanced() {
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            for (Suit suit : Suit.values()) {
                // Sprawdzamy czy partner licytował ten kolor
                boolean partnerHasSuit = ps.getPairState().firstToShow(suit) == ps.getPartner();

                if (!partnerHasSuit) {
                    int count = hs.getSuits().get(suit).getShape().getMin();
                    int hcpSuit = hs.getSuits().get(suit).getHighCardPoints().getMin();

                    if (count == 0) return false; // Renons wyklucza zrównoważenie

                    // Nowe warunki jakości koloru dla ręki zrównoważonej:
                    // Singleton musi być asem (4 HCP)
                    if (count == 1 && hcpSuit < 4) return false;
                    // Dubleton musi mieć min. króla (3 HCP)
                    if (count == 2 && hcpSuit < 3) return false;
                    // Trójka musi mieć min. waleta (1 HCP)
                    if (count == 3 && hcpSuit < 1) return false;
                    // Czwórka i więcej - akceptujemy zawsze
                }
            }
            return true;
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            for (Suit suit : Suit.values()) {
                // Sprawdzamy czy partner licytował ten kolor
                boolean partnerHasSuit = ps.getPairState().firstToShow(suit) == ps.getPartner();

                if (!partnerHasSuit) {
                    showHand.getSuits().get(suit).showShape(1, 5);
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            return  "pair_balanced";
        }
    }
}




























































































































































































































































































































































































































































































