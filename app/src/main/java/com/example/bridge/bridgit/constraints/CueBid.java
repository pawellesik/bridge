package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class CueBid extends Constraint.StaticConstraint {
    private final Suit suit;

    public CueBid(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            return ps.getOpponentsPairState().haveShownSuit(s);
        }
        return false;
    }
}
