package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Respond extends Bidder {
    
    public static PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        
        Call.Bid opening = ps.getBiddingState().getOpeningBid();
        if (opening == null) return choices;

        // Simple response logic to keep auction going
        choices.addRules(respondToOneLevel(ps, opening));
        
        // Default Pass if very weak
        choices.addPassRule(points(0, 5));
        
        return choices;
    }

    private static List<CallFeature> respondToOneLevel(PositionState ps, Call.Bid opening) {
        List<CallFeature> rules = new ArrayList<>();
        
        // Support partner's major (6-10 points, 3+ support)
        if (opening.getSuit() == Suit.Spades || opening.getSuit() == Suit.Hearts) {
            rules.add(shows(new Call.Bid(2, opening.getSuit()), points(6, 10), shape(opening.getSuit(), 3, 13)));
        }
        
        // New suit at 1-level (6+ points)
        rules.add(shows(new Call.Bid(1, Suit.Spades), points(6, 25), shape(Suit.Spades, 4, 13)));
        rules.add(shows(new Call.Bid(1, Suit.Hearts), points(6, 25), shape(Suit.Hearts, 4, 13)));
        rules.add(shows(new Call.Bid(1, Suit.Diamonds), points(6, 25), shape(Suit.Diamonds, 4, 13)));
        
        // 1NT response (6-10 points, no fit)
        rules.add(shows(new Call.Bid(1, Strain.NoTrump), points(6, 10)));
        
        return rules;
    }
}
