package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

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

    /**
     * Sprawdza i deklaruje trzymanie (lub jego brak) w konkretnym kolorze.
     */
    public static class ShowsSuitStopped extends HandConstraint implements IShowsHand, IDescribeConstraint {
        protected final Suit suit;
        protected final boolean desiredValue;

        public ShowsSuitStopped(Suit suit, boolean desiredValue) {
            this.suit = suit;
            this.desiredValue = desiredValue;
        }

        public ShowsSuitStopped(Suit suit) {
            this(suit, true);
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            if (!ps.hasHand() || hs != ps.getPrivateHandSummary()) {
                return true;
            }
            Suit s = getSuit(suit, call);
            if (s != null) {
                Boolean stopped = hs.getSuits().get(s).getStopped();
                if (desiredValue) {
                    return Boolean.TRUE.equals(stopped);
                } else {
                    return !Boolean.TRUE.equals(stopped);
                }
            }
            return false;
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit s = getSuit(suit, call);
            if (s != null) {
                showHand.getSuits().get(s).showStopped(desiredValue);
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit s = getSuit(suit, call);
            if (s != null) {
                return s.toSymbol() + (desiredValue ? " stopped" : " not stopped");
            }
            return desiredValue ? "suit stopped" : "suit not stopped";
        }
    }
}




























































































































































































































































































































































































































































































