package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Open extends Bidder {
    public static PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        // Choices from C# logic:
        choices.addRules(NoTrump.open(ps));
        choices.addRules(openSuit(ps));
        
        if (ps.getSeat() != 4) {
            choices.addPassRule(points(0, 11));
        } else {
            choices.addRules(Collections.singletonList(shows(Call.Pass, isSeat(4), new PassIn4thSeat())));
            choices.addPassRule(points(0, 11));
        }
        return choices;
    }

    private static List<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        
        // C# OpenSuit priorities:
        
        // 1. Medium+ hands (17-21) - longest suit
        rules.add(shows(new Call.Bid(1, Suit.Spades), points(17, 21), shape(Suit.Spades, 5, 13)));
        rules.add(shows(new Call.Bid(1, Suit.Hearts), points(17, 21), shape(Suit.Hearts, 5, 13)));
        
        // 2. Minimum hands (12-16)
        // 5+ Majors
        rules.add(shows(new Call.Bid(1, Suit.Spades), points(12, 16), shape(Suit.Spades, 5, 13)));
        rules.add(shows(new Call.Bid(1, Suit.Hearts), points(12, 16), shape(Suit.Hearts, 5, 13)));
        
        // 3. Minors (12-21)
        // Better minor logic
        rules.add(shows(new Call.Bid(1, Suit.Diamonds), points(12, 21), new BetterMinor(Suit.Diamonds)));
        rules.add(shows(new Call.Bid(1, Suit.Clubs), points(12, 21), new BetterMinor(Suit.Clubs)));
        
        // 4. Default minor opening if not balanced and no major
        rules.add(shows(new Call.Bid(1, Suit.Diamonds), points(12, 21), shape(Suit.Diamonds, 3, 13)));
        rules.add(shows(new Call.Bid(1, Suit.Clubs), points(12, 21), shape(Suit.Clubs, 3, 13)));
            
        return rules;
    }
}
