package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.PairSummary;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

/**
 * Techniczna klasa służąca do debugowania procesu licytacji.
 * Pozwala na wstawienie "punktu przerwania" w kodzie, aby sprawdzić ile razy 
 * dana reguła była analizowana publicznie i prywatnie.
 */
public class Break {
    /**
     * Punkt przerwania sprawdzany podczas dopasowywania konkretnej ręki.
     */
    public static class HandBreak extends HandConstraint {
        private final String name;
        public int countPublic = 0;
        public int countPrivate = 0;

        public HandBreak(String name) {
            this.name = name;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            PairSummary pairSummary = new PairSummary(ps);
            PairSummary oppsSummary = PairSummary.opponents(ps);
            if (hs == ps.getPublicHandSummary()) {
                countPublic++;
            } else {
                countPrivate++;
            }
            return true;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Punkt przerwania sprawdzany dla warunków statycznych (niezależnych od kart na ręce).
     */
    public static class StaticBreak extends StaticConstraint {
        private final String name;

        public StaticBreak(String name) {
            this.name = name;
        }

        @Override
        public boolean conforms(Call call, PositionState ps) {
            return true;
        }

        public String getName() {
            return name;
        }
    }
}




























































































































































































































































































































































































































































































