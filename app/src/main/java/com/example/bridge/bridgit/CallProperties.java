package com.example.bridge.bridgit;

import java.util.function.Function;

public class CallProperties extends CallFeature {
    private final Function<PositionState, PositionCalls> partnerBidsFactory;
    private final boolean forcing1Round;
    private final boolean forcingToGame;
    private final boolean agreeTrump;
    private final Suit trumpSuit;

    public CallProperties(Call call, Function<PositionState, PositionCalls> partnerBidsFactory,
                          boolean forcing1Round, boolean forcingToGame, boolean agreeTrump, Suit trumpSuit,
                          Constraint.StaticConstraint... constraints) {
        super(call, constraints);
        this.partnerBidsFactory = partnerBidsFactory;
        this.forcing1Round = forcing1Round;
        this.forcingToGame = forcingToGame;
        this.agreeTrump = agreeTrump;
        this.trumpSuit = trumpSuit;
    }

    public Function<PositionState, PositionCalls> getPartnerBidsFactory() { return partnerBidsFactory; }
    public boolean isForcing1Round() { return forcing1Round; }
    public boolean isForcingToGame() { return forcingToGame; }
    public boolean isAgreeTrump() { return agreeTrump; }
    public Suit getTrumpSuit() { return trumpSuit; }
}
