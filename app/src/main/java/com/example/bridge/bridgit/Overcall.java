package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Overcall extends Bidder {
    public static PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        
        // Simple overcall: 8-17 points, 5+ suit
        choices.addRules(simpleOvercalls(ps));
        
        // Always add a pass rule for overcaller
        choices.addPassRule();
        
        return choices;
    }

    private static List<CallFeature> simpleOvercalls(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        
        for (Suit suit : Card.Suits) {
            Call.Bid bid = new Call.Bid(1, suit);
            if (ps.isValidNextCall(bid)) {
                rules.add(shows(bid, points(8, 17), shape(suit, 5, 13)));
            }
        }
        
        return rules;
    }
}
