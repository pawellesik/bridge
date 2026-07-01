package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class TakeoutSuit extends Constraint.HandConstraint implements Constraint.IShowsHand {
    private final Suit suit;

    public TakeoutSuit(Suit suit) {
        this.suit = suit;
    }

    public static Suit higherRanking(Suit s1, Suit s2) {
        if (s1 == s2) return s1;
        switch (s1) {
            case Clubs: return s2;
            case Diamonds: return (s2 == Suit.Clubs) ? s1 : s2;
            case Hearts: return (s2 == Suit.Spades) ? s2 : s1;
            case Spades: return s1;
            default: return s1;
        }
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit s = Constraint.getSuit(this.suit, call);
        if (s != null) {
            if (ps.getOpponentsPairState().haveShownSuit(s)) return false;
            for (Suit other : Suit.values()) {
                if (other != s && !ps.getOpponentsPairState().haveShownSuit(other)) {
                    BetterSuit betterSuit = new BetterSuit(s, other, higherRanking(s, other), false);
                    if (!betterSuit.conforms(call, ps, hs)) return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        Suit s = Constraint.getSuit(this.suit, call);
        if (s != null) {
            showHand.suits.get(s).showShape(4, 11);
        }
    }
}
