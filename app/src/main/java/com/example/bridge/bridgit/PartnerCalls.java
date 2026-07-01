package com.example.bridge.bridgit;

import java.util.function.Function;

public class PartnerCalls extends CallFeature {
    private final Function<PositionState, PositionCalls> partnerBids;
    private final boolean forcing1Round;
    private final boolean forcingToGame;
    private final Suit trumpSuit;

    public PartnerCalls(Call call, Function<PositionState, PositionCalls> partnerBids,
                        boolean forcing1Round, boolean forcingToGame,
                        boolean agreeTrump, Suit trump,
                        Constraint.StaticConstraint... constraints) {
        super(call, constraints);
        this.partnerBids = partnerBids;
        this.forcing1Round = forcing1Round;
        this.forcingToGame = forcingToGame;
        
        Suit t = null;
        if (agreeTrump) {
            if (call instanceof Call.Bid) {
                t = ((Call.Bid) call).getSuit();
            }
        } else {
            t = trump;
        }
        this.trumpSuit = t;
    }

    public Function<PositionState, PositionCalls> getPartnerBids() {
        return partnerBids;
    }

    public boolean isForcing1Round() {
        return forcing1Round;
    }

    public boolean isForcingToGame() {
        return forcingToGame;
    }

    public Suit getTrumpSuit() {
        return trumpSuit;
    }
}
