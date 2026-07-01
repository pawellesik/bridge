package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Compete extends Bidder {
    private static final Constraint.HandConstraint competeTo2 = pairPoints(20, 22);
    private static final Constraint.HandConstraint competeTo3 = pairPoints(23, 25);
    private static final Constraint.HandConstraint competeTo2NT = pairPoints(20, 24);
    private static final Constraint.HandConstraint competeTo3NT = pairPoints(25, 31);
    private static final Constraint.HandConstraint competeTo4 = pairPoints(26, 28);
    private static final Constraint.HandConstraint competeTo5 = pairPoints(29, 32);

    public static Iterable<CallFeature> compBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        
        // Simple competition for demo
        bids.add(shows(new Call.Bid(4, Suit.Spades), fit(8, Suit.Spades), competeTo4));
        bids.add(shows(new Call.Bid(4, Suit.Hearts), fit(8, Suit.Hearts), competeTo4));

        bids.add(shows(new Call.Bid(2, Suit.Spades), fit(8, Suit.Spades), competeTo2));
        bids.add(shows(new Call.Bid(2, Suit.Hearts), fit(8, Suit.Hearts), competeTo2));
        bids.add(shows(new Call.Bid(2, Suit.Diamonds), fit(8, Suit.Diamonds), competeTo2));
        bids.add(shows(new Call.Bid(2, Suit.Clubs), fit(8, Suit.Clubs), competeTo2));

        bids.add(shows(new Call.Bid(3, Strain.NoTrump), new OppsStopped(true), competeTo3NT));

        bids.add(shows(Call.Pass));
        
        return bids;
    }
}
