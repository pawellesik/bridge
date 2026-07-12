package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.licytacja.moje.BridgeBidder.*;

/**
 * Sprawdza czy dana odzywka jest Cue-bidem (licytacją koloru przeciwnika).
 * Często używane w licytacji dwustronnej do pokazania silnego wsparcia w kolorze partnera
 * lub pytania o zatrzymanie w kolorze przeciwnika.
 */
public class IsCueBid extends StaticConstraint {
    private final Suit suit; // Kolor przeciwnika (null jeśli sprawdzamy aktualną odzywkę)

    public IsCueBid(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        Suit s = getSuit(this.suit, call);
        if (s != null) {
            return ps.getOppsPairState().haveShownSuit(s);
        }
        return false;
    }
}
