package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Compete extends Bidder {
    private static final Constraint.HandConstraint competeTo2 = pairPoints(20, 22);
    private static final Constraint.HandConstraint competeTo3 = pairPoints(23, 25);
    private static final Constraint.HandConstraint competeTo3NT = pairPoints(25, 31);
    private static final Constraint.HandConstraint competeTo4 = pairPoints(26, 28);
    private static final Constraint.HandConstraint competeTo5 = pairPoints(29, 32);

    public static Iterable<CallFeature> compBids(PositionState ps) {
        List<CallFeature> res = new ArrayList<>();
        
        // C# logic: Add conventions first (Blackwood, Gerber - to be added later)
        
        for (Suit s : Card.Suits) {
            Call.Bid bid4 = new Call.Bid(4, s);
            if (ps.isValidNextCall(bid4)) {
                res.add(shows(bid4, fit(8, s), competeTo4));
                res.add(shows(bid4, fit(8, s), competeTo5));
            }
            
            Call.Bid bid3 = new Call.Bid(3, s);
            if (ps.isValidNextCall(bid3)) {
                res.add(shows(bid3, fit(8, s), competeTo3));
            }

            Call.Bid bid2 = new Call.Bid(2, s);
            if (ps.isValidNextCall(bid2)) {
                res.add(shows(bid2, fit(8, s), competeTo2));
            }
        }

        res.add(shows(new Call.Bid(3, Strain.NoTrump), new OppsStopped(true), competeTo3NT));

        // Add Forced Bids (C# AddRange(ForcedBid.Bids(ps)))
        for (CallFeature f : ForcedBid.bids(ps)) {
            res.add(f);
        }

        res.add(shows(Call.Pass));
        
        return res;
    }
}
