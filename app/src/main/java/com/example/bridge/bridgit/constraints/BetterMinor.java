package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class BetterMinor extends Constraint.HandConstraint {
    private final Suit suit;

    public BetterMinor(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit better = Constraint.getSuit(this.suit, call);
        if (better == Suit.Clubs || better == Suit.Diamonds) {
            Range shapeClubs = hs.suits.get(Suit.Clubs).getShape();
            Range shapeDiamonds = hs.suits.get(Suit.Diamonds).getShape();

            if (shapeClubs.min != shapeClubs.max || shapeDiamonds.min != shapeDiamonds.max) return true;
            
            if (shapeClubs.min < shapeDiamonds.min) return (better == Suit.Diamonds);
            if (shapeClubs.min > shapeDiamonds.min) return (better == Suit.Clubs);
            
            // They are equal. If 4 or longer then select diamonds, otherwise clubs are the "best"
            if (shapeClubs.min < 4) return (better == Suit.Clubs);
            return (better == Suit.Diamonds);
        }
        return false;
    }
}
