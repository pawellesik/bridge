package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Compete extends Bidder {
    private static final Constraint.HandConstraint competeTo2 = pairPoints(20, 22);
    private static final Constraint.HandConstraint competeTo3 = pairPoints(23, 25);
    private static final Constraint.HandConstraint competeTo3NT = pairPoints(25, 31);
    private static final Constraint.HandConstraint competeTo4 = pairPoints(26, 40);

    public static Iterable<CallFeature> compBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        
        // Re-evaluate fits
        for (Suit s : Card.Suits) {
            Call.Bid next = ps.getBiddingState().getContract().nextAvailableBid(s);
            if (next == null) continue;

            if (s.isMajor()) {
                // Raise to Game
                bids.add(shows(new Call.Bid(4, s), fit(8, s), competeTo4));
                // Invite or compete
                bids.add(shows(new Call.Bid(3, s), fit(8, s), competeTo3));
                bids.add(shows(new Call.Bid(2, s), fit(8, s), competeTo2));
            } else {
                // Minors
                bids.add(shows(new Call.Bid(3, s), fit(8, s), competeTo3));
                bids.add(shows(new Call.Bid(2, s), fit(8, s), competeTo2));
            }

            // Rebid own long suit even without fit if strong
            if (next.getLevel() <= 2) {
                bids.add(shows(next, points(16, 40), shape(s, 6, 13)));
            }
        }

        bids.add(shows(new Call.Bid(3, Strain.NoTrump), new OppsStopped(true), competeTo3NT));

        bids.add(shows(Call.Pass));
        
        return bids;
    }
}
