package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.HandConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.HandSummary;
import com.example.bridge.bidding.BridgeBidder.Tools.IDescribeConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.IShowsHand;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

/**
 * Klasa weryfikująca posiadanie "zatrzymań" (stopperów) w kolorach przeciwników.
 * Kluczowa przy licytacji Bez Atutu (NT).
 */
public class OppsStopped {
    /**
     * Sprawdza czy kolory przeciwników są zatrzymane przez nas lub partnera.
     */
    public static class HasOppsStopped extends HandConstraint {
        protected final boolean desiredValue;

        public HasOppsStopped(boolean desiredValue) {
            this.desiredValue = desiredValue;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            for (Suit suit : ps.getOppsPairState().getShownSuits()) {
                Boolean thisStop = hs.getSuits().get(suit).getStopped();
                Boolean partnerStop = ps.getPartner().getPublicHandSummary().getSuits().get(suit).getStopped();
                if (!Boolean.TRUE.equals(thisStop) && !Boolean.TRUE.equals(partnerStop)) {
                    if (Boolean.FALSE.equals(thisStop)) return !desiredValue;
                }
            }
            return desiredValue;
        }
    }

    /**
     * Deklaruje partnerowi posiadanie zatrzymania w kolorze przeciwnika.
     */
    public static class ShowsOppsStopped extends HasOppsStopped implements IShowsHand, IDescribeConstraint {
        public ShowsOppsStopped(boolean desiredValue) {
            super(desiredValue);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            if (desiredValue) {
                for (Suit suit : ps.getOppsPairState().getShownSuits()) {
                    Boolean partnerStop = ps.getPartner().getPublicHandSummary().getSuits().get(suit).getStopped();
                    if (partnerStop == null) {
                        showHand.getSuits().get(suit).showStopped(true);
                    }
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            int numOppsSuits = ps.getOppsPairState().getShownSuits().size();
            if (numOppsSuits > 0) {
                if (desiredValue) {
                    return numOppsSuits == 1 ? "opponents suit stopped" : "opponents suits stopped";
                } else {
                    return "opponent suit not stopped";
                }
            }
            return null;
        }
    }
}




























































































































































































































































































































































































































































































