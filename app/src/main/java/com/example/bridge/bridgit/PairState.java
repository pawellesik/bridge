package com.example.bridge.bridgit;

import java.util.*;

public class PairState {
    private final BiddingState biddingState;
    private final Pair pair;
    private final BiddingSystem biddingSystem;
    private final boolean areVulnerable;
    private boolean forcedToGame = false;
    private PositionState forcedPosition = null;
    private int forcedThroughRound = 0;
    private final Map<Suit, PositionState> firstToShow = new HashMap<>();
    private Suit lastShownSuit = null;
    private Suit trumpSuit = null;

    public PairState(BiddingState biddingState, Pair pair, BiddingSystem biddingSystem, Vulnerable vulnerable) {
        this.biddingState = biddingState;
        this.pair = pair;
        this.biddingSystem = biddingSystem;
        this.areVulnerable = (vulnerable == Vulnerable.All ||
                (vulnerable == Vulnerable.NS && pair == Pair.NS) ||
                (vulnerable == Vulnerable.EW && pair == Pair.EW));
    }

    public boolean isForcedToBid(PositionState ps) {
        return (ps == forcedPosition && ps.getBidRound() <= forcedThroughRound);
    }

    public boolean haveShownSuit(Suit suit) {
        return firstToShow.containsKey(suit);
    }

    public Set<Suit> getShownSuits() {
        return firstToShow.keySet();
    }

    public PositionState firstToShow(Suit suit) {
        return firstToShow.get(suit);
    }

    public Suit lastShownSuit() {
        return lastShownSuit;
    }

    public void updateShownSuits(Call call, PositionState ps, HandSummary hs) {
        for (Suit suit : Card.Suits) {
            if (!firstToShow.containsKey(suit)) {
                Range shape = hs.suits.get(suit).getShape();
                int minRequired = (call instanceof Call.Bid && ((Call.Bid) call).getSuit() == suit) ? 2 : 4;
                if (shape.min >= minRequired) {
                    firstToShow.put(suit, ps);
                }
            }
        }
    }

    public void updateLastShownSuit(Call call, PositionState ps, HandSummary hs) {
        for (Suit suit : Card.Suits) {
            Range shape = hs.suits.get(suit).getShape();
            int minRequired = (call instanceof Call.Bid && ((Call.Bid) call).getSuit() == suit) ? 2 : 4;
            if (shape.min >= minRequired) {
                lastShownSuit = suit;
            }
        }
    }

    public void updatePairProperties(CallDetails cd) {
        CallProperties props = cd.getProperties();
        if (props != null) {
            if (props.isForcingToGame()) {
                this.forcedToGame = true;
            }
            if (props.isForcing1Round()) {
                this.forcedPosition = cd.getPositionState().partner();
                this.forcedThroughRound = this.forcedPosition.getBidRound();
            }
            if (props.getTrumpSuit() != null) {
                this.trumpSuit = props.getTrumpSuit();
            }
        }
    }

    public BiddingSystem getBiddingSystem() { return biddingSystem; }
    public boolean areVulnerable() { return areVulnerable; }
}
