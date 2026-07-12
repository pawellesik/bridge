package com.example.bridge.bidding.Tools;

import java.util.*;

public class PairState {
    private final BiddingState biddingState;
    private final Pair pair;
    private final IBiddingSystem biddingSystem;
    private final boolean areVulnerable;
    private boolean forcedToGame = false;
    private PositionState forcedPosition = null;
    private int forcedThroughRound = 0;
    private final Map<Suit, PositionState> firstToShow = new EnumMap<>(Suit.class);
    private Suit lastShownSuit = null;
    private Suit trumpSuit = null;

    public PairState(BiddingState biddingState, Pair pair, IBiddingSystem biddingSystem, Vulnerable vulnerable) {
        this.biddingState = biddingState;
        this.pair = pair;
        this.biddingSystem = biddingSystem;
        this.areVulnerable = (vulnerable == Vulnerable.All ||
                (vulnerable == Vulnerable.NS && pair == Pair.NS) ||
                (vulnerable == Vulnerable.EW && pair == Pair.EW));
    }

    public Set<Suit> getShownSuits() {
        return firstToShow.keySet();
    }

    public Suit getLastShownSuit() {
        return lastShownSuit;
    }

    public boolean haveShownSuit(Suit suit) {
        return firstToShow.containsKey(suit);
    }

    public boolean areVulnerable() {
        return areVulnerable;
    }

    public IBiddingSystem getBiddingSystem() {
        return biddingSystem;
    }

    public boolean isForcedToBid(PositionState ps) {
        return (ps == forcedPosition && ps.getBidRound() <= forcedThroughRound);
    }

    public PositionState firstToShow(Suit suit) {
        return firstToShow.get(suit);
    }

    public void updateShownSuits(Call call, PositionState ps, HandSummary hs) {
        for (Suit suit : Suit.values()) {
            if (!firstToShow.containsKey(suit)) {
                Range shape = hs.getSuits().get(suit).getShape();
                if (shape != null) {
                    int minRequired = (call instanceof Bid && ((Bid) call).getSuit() == suit) ? 2 : 4;
                    if (shape.getMin() >= minRequired) {
                        firstToShow.put(suit, ps);
                    }
                }
            }
        }
    }

    public void updateLastShownSuit(Call call, PositionState ps, HandSummary hs) {
        for (Suit suit : Suit.values()) {
            Range shape = hs.getSuits().get(suit).getShape();
            if (shape != null) {
                int minRequired = (call instanceof Bid && ((Bid) call).getSuit() == suit) ? 2 : 4;
                if (shape.getMin() >= minRequired) {
                    lastShownSuit = suit;
                }
            }
        }
    }

    public void updatePairProperties(CallDetails callDetails) {
        if (callDetails.getProperties() != null) {
            CallProperties props = callDetails.getProperties();
            if (props.isForcingToGame()) {
                forcedToGame = true;
            }
            if (props.isForcing1Round()) {
                forcedPosition = callDetails.getPositionState().getPartner();
                forcedThroughRound = callDetails.getPositionState().getPartner().getBidRound();
            }
            if (props.getTrumpSuit() != null) {
                trumpSuit = props.getTrumpSuit();
            }
        }
    }

    public boolean isForcedToGame() {
        return forcedToGame;
    }

    public Suit getTrumpSuit() {
        return trumpSuit;
    }
}




























































































































































































































































































































































































































































































