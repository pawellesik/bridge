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

        switch (opening.getSuit()) {
            case Clubs: return oneClub(ps);
            case Diamonds: return oneDiamond(ps);
            case Hearts: return oneHeart(ps);
            case Spades: return oneSpade(ps);
        }
        return new PositionCalls(ps);
    }

    public static PositionCalls oneClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(respondNoInterference(ps, Call.Bid._1C));
        choices.addPassRule(points(0, 5));
        return choices;
    }

    public static PositionCalls oneDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(respondNoInterference(ps, Call.Bid._1D));
        choices.addPassRule(points(0, 5));
        return choices;
    }

    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(respondNoInterference(ps, Call.Bid._1H));
        choices.addPassRule(points(0, 5));
        return choices;
    }

    public static PositionCalls oneSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(respondNoInterference(ps, Call.Bid._1S));
        choices.addPassRule(points(0, 5));
        return choices;
    }

    private static List<CallFeature> respondNoInterference(PositionState ps, Call.Bid opening) {
        List<CallFeature> rules = new ArrayList<>();
        Suit suit = opening.getSuit();
        
        // 1:1 C# logic for major responses
        if (suit.isMajor()) {
            rules.add(partnerBids(OpenBid2::responderChangedSuits));
            
            rules.add(shows(new Call.Bid(2, suit), fit(), points(6, 10)));
            rules.add(shows(new Call.Bid(3, suit), fit(), points(11, 12)));
            rules.add(shows(new Call.Bid(4, suit), fit(), points(6, 12))); // Weak jump
            
            // New suit at 1-level (1S over 1H)
            if (suit == Suit.Hearts) {
                rules.add(shows(Call.Bid._1S, points(6, 40), shape(Suit.Spades, 4, 13)));
            }
        } else {
            // Minor responses
            rules.add(partnerBids(OpenBid2::responderChangedSuits));
            
            rules.add(shows(Call.Bid._1H, points(6, 40), shape(Suit.Hearts, 4, 13)));
            rules.add(shows(Call.Bid._1S, points(6, 40), shape(Suit.Spades, 4, 13)));
            rules.add(shows(Call.Bid._1D, points(6, 40), shape(Suit.Diamonds, 4, 13)));
        }

        rules.add(shows(new Call.Bid(1, Strain.NoTrump), points(6, 10)));
        return rules;
    }

    private static PositionCalls oppsInterfered(PositionState ps, Call.Bid opening) {
        PositionCalls choices = new PositionCalls(ps);
        
        choices.addRules(NegativeDouble.initiateConvention(ps));

        Call rhoCall = ps.rightHandOpponent().getBidHistory(0);
        if (rhoCall instanceof Call.Bid) {
            Call.Bid rhoBid = (Call.Bid) rhoCall;
            Suit openSuit = opening.getSuit();

            // Support partner
            Call.Bid raise = ps.getBiddingState().getContract().nextAvailableBid(openSuit);
            if (raise != null) {
                if (raise.getLevel() == 2) {
                    choices.addRules(Collections.singletonList(shows(raise, points(6, 10), fit())));
                } else if (raise.getLevel() == 3) {
                    choices.addRules(Collections.singletonList(shows(raise, points(11, 12), fit())));
                }
            }

            // New suit bids
            for (Suit s : Card.Suits) {
                Call.Bid next = ps.getBiddingState().getContract().nextAvailableBid(s);
                if (next != null && s != openSuit && s != rhoBid.getSuit()) {
                    choices.addRules(Collections.singletonList(shows(next, points(10, 40), shape(s, 5, 13))));
                }
            }
        }

        choices.addPassRule();
        return choices;
    }
}
