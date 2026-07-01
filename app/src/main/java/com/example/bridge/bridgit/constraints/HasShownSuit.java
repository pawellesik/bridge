package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class HasShownSuit extends Constraint.StaticConstraint {
    private final Suit suit;
    private final boolean eitherPartner;

    public HasShownSuit(Suit suit, boolean eitherPartner) {
        this.suit = suit;
        this.eitherPartner = eitherPartner;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            PositionState firstToShow = ps.getPairState().firstToShow(s);
            if (firstToShow == null) return false;
            return eitherPartner || firstToShow == ps;
        }
        return false;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            if (eitherPartner) {
                return ps.getDirection().pair().name() + " has shown " + s.toSymbol();
            }
            return ps.getDirection().name() + " has shown " + s.toSymbol();
        }
        return super.getLogDescription(call, ps);
    }
}
