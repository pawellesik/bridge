package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Respond extends Bidder {
    
    public static PositionCalls getPositionCalls(PositionState ps) {
        Call.Bid opening = ps.getBiddingState().getOpeningBid();
        if (opening == null) return new PositionCalls(ps);

        if (!ps.rightHandOpponent().isPassed()) {
            return oppsInterfered(ps, opening);
        }

        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(respondNoInterference(ps, opening));
        choices.addPassRule(points(0, 5));
        return choices;
    }

    private static List<CallFeature> respondNoInterference(PositionState ps, Call.Bid opening) {
        List<CallFeature> rules = new ArrayList<>();
        
        // Simplified Standard Responses
        Suit suit = opening.getSuit();
        
        // Major Support
        if (suit.isMajor()) {
            rules.add(shows(new Call.Bid(2, suit), points(6, 10), shape(suit, 3, 13)));
            rules.add(shows(new Call.Bid(3, suit), points(11, 12), shape(suit, 3, 13)));
            rules.add(shows(new Call.Bid(4, suit), points(6, 12), shape(suit, 5, 13)));
        }

        // New Suit at 1-level (6+ points)
        for (Suit s : Card.Suits) {
            Call.Bid bid = new Call.Bid(1, s);
            if (ps.isValidNextCall(bid) && s != suit) {
                rules.add(shows(bid, points(6, 40), shape(s, 4, 13)));
            }
        }

        // New Suit at 2-level (10+ points)
        for (Suit s : Card.Suits) {
            Call.Bid bid = new Call.Bid(2, s);
            if (ps.isValidNextCall(bid) && s != suit && !s.isMajor()) { // 2-level minor
                rules.add(shows(bid, points(10, 40), shape(s, 4, 13)));
            }
            if (ps.isValidNextCall(bid) && s.isMajor() && s != suit) { // 2-level major
                rules.add(shows(bid, points(10, 40), shape(s, 5, 13)));
            }
        }

        rules.add(shows(new Call.Bid(1, Strain.NoTrump), points(6, 10)));
        
        return rules;
    }

    private static PositionCalls oppsInterfered(PositionState ps, Call.Bid opening) {
        PositionCalls choices = new PositionCalls(ps);
        Suit openSuit = opening.getSuit();

        if (ps.rightHandOpponent().isDoubled()) {
            // RHO doubled. Handle Redouble etc.
            choices.addRules(Collections.singletonList(shows(Call.Redouble, points(10, 40))));
        }

        Call rhoCall = ps.rightHandOpponent().getBidHistory(0);
        if (rhoCall instanceof Call.Bid) {
            Call.Bid rhoBid = (Call.Bid) rhoCall;
            Suit rhoSuit = rhoBid.getSuit();

            // Simple competition: bid a new suit if strong enough (10+)
            for (Suit s : Card.Suits) {
                Call.Bid next = ps.getBiddingState().getContract().nextAvailableBid(s);
                if (next != null && s != openSuit && s != rhoSuit) {
                    if (next.getLevel() == 1) {
                        choices.addRules(Collections.singletonList(shows(next, points(6, 40), shape(s, 4, 13))));
                    } else if (next.getLevel() == 2) {
                        choices.addRules(Collections.singletonList(shows(next, points(10, 40), shape(s, 5, 13))));
                    }
                }
            }

            // Raise partner
            Call.Bid raise = ps.getBiddingState().getContract().nextAvailableBid(openSuit);
            if (raise != null) {
                if (raise.getLevel() == 2) {
                    choices.addRules(Collections.singletonList(shows(raise, points(6, 10), shape(openSuit, 3, 13))));
                } else if (raise.getLevel() == 3) {
                    choices.addRules(Collections.singletonList(shows(raise, points(11, 12), shape(openSuit, 3, 13))));
                }
            }
            
            // Negative Double placeholder
            if (rhoBid.getLevel() == 1) {
                choices.addRules(Collections.singletonList(shows(Call.Double, points(8, 40))));
            }
        }

        choices.addPassRule();
        return choices;
    }
}
