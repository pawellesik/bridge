package com.example.bridge.bidding.BridgeBidder;

public class CallProperties extends CallFeature {
    private final PositionCallsFactory partnerBids;
    private final boolean forcing1Round;
    private final boolean forcingToGame;
    private Suit trumpSuit = null;

    public CallProperties(Call call, PositionCallsFactory partnerBids, boolean forcing1Round, boolean forcingToGame,
                          boolean agreeTrump, Suit trump, StaticConstraint... constraints) {
        super(call, (Constraint[]) constraints);
        this.partnerBids = partnerBids;
        this.forcing1Round = forcing1Round;
        this.forcingToGame = forcingToGame;
        
        // Priorytet ma jawnie przekazany kolor 'trump'. 
        // Jeśli go nie ma, a agreeTrump jest true, bierzemy kolor z samej odzywki.
        if (trump != null) {
            this.trumpSuit = trump;
        } else if (agreeTrump && call instanceof Bid) {
            this.trumpSuit = ((Bid) call).getSuit();
        }
    }

    public PositionCallsFactory getPartnerBids() {
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
