package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Open {
    public static PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        
        choices.addRules(openSuit(ps));
        
        // If not in 4th seat, we can pass if we don't have enough points
        if (ps.getSeat() != 4) {
            choices.addRules(Collections.singletonList(
                Bidder.shows(Call.Pass, new ShowsPoints(null, 0, 11, HasPoints.PointType.Starting))
            ));
        } else {
            // In 4th seat, use Rule of 15 or similar (simplified to 12 pts for demo)
            choices.addRules(Collections.singletonList(
                Bidder.shows(Call.Pass, new ShowsPoints(null, 0, 11, HasPoints.PointType.Starting))
            ));
        }
        
        return choices;
    }

    private static List<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        
        // Standard openings at level 1 (12-21 points)
        // Rule order matters!
        
        // 1. Majors (5+ cards)
        rules.add(Bidder.shows(new Call.Bid(1, Suit.Spades), 
            new ShowsPoints(null, 12, 21, HasPoints.PointType.Starting),
            new ShowsShape(Suit.Spades, 5, 13)));
            
        rules.add(Bidder.shows(new Call.Bid(1, Suit.Hearts), 
            new ShowsPoints(null, 12, 21, HasPoints.PointType.Starting),
            new ShowsShape(Suit.Hearts, 5, 13)));
            
        // 2. 1NT (15-17 HCP, Balanced)
        rules.add(Bidder.shows(new Call.Bid(1, Strain.NoTrump),
            new ShowsPoints(null, 15, 17, HasPoints.PointType.HighCard),
            new ShowsBalanced(true)));

        // 3. Minors (preferred longer, or Diamonds if equal 3-3, or Clubs if equal 4-4)
        rules.add(Bidder.shows(new Call.Bid(1, Suit.Diamonds), 
            new ShowsPoints(null, 12, 21, HasPoints.PointType.Starting),
            new ShowsShape(Suit.Diamonds, 3, 13)));

        rules.add(Bidder.shows(new Call.Bid(1, Suit.Clubs), 
            new ShowsPoints(null, 12, 21, HasPoints.PointType.Starting),
            new ShowsShape(Suit.Clubs, 3, 13)));
            
        return rules;
    }
}
