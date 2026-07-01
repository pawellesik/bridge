package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Open extends Bidder {
    
    public static final Constraint.HandConstraint OneLevel = points(12, 21);
    public static final Constraint.HandConstraint Minimum = points(12, 16);
    public static final Constraint.HandConstraint MediumOrBetter = points(17, 21);
    public static final Constraint.HandConstraint DontOpen = points(0, 11);

    public static PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(Strong2Clubs.open(ps));
        choices.addRules(NoTrump.open(ps));
        choices.addRules(openSuit(ps));

        if (ps.getSeat() != 4) {
            choices.addPassRule(DontOpen);
        } else {
            choices.addRules(Collections.singletonList(shows(Call.Pass, isSeat(4), new PassIn4thSeat())));
            choices.addPassRule(DontOpen);
        }
        return choices;
    }

    public static Iterable<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        
        bids.add(partnerBids(Call.Bid._1C, Respond::oneClub));
        bids.add(partnerBids(Call.Bid._1D, Respond::oneDiamond));
        bids.add(partnerBids(Call.Bid._1H, Respond::oneHeart));
        bids.add(partnerBids(Call.Bid._1S, Respond::oneSpade));

        // In 4th seat we want to pass if the Rule of 15 does not apply.
        bids.add(shows(Call.Pass, isSeat(4), new PassIn4thSeat()));

        // For medium+ hands we will always bid the longest suit first.
        bids.add(shows(Call.Bid._1C, MediumOrBetter, shape(4, 10), new LongestSuit(Suit.Clubs)));
        bids.add(shows(Call.Bid._1D, MediumOrBetter, shape(4, 10), new LongestSuit(Suit.Diamonds)));

        // Minimum hands 5/4 or 6/5 minors (bid Diamonds to avoid reverse)
        bids.add(shows(Call.Bid._1D, Minimum, shape(Suit.Clubs, 5, 5), shape(Suit.Diamonds, 4, 4)));
        bids.add(shows(Call.Bid._1D, Minimum, shape(Suit.Clubs, 6, 6), shape(Suit.Diamonds, 5, 5)));

        bids.add(shows(Call.Bid._1C, OneLevel, new LongestSuit(Suit.Clubs), longestMajor(4)));
        bids.add(shows(Call.Bid._1C, OneLevel, shape(3, 3), shape(Suit.Diamonds, 0, 3), longestMajor(4)));

        bids.add(shows(Call.Bid._1D, OneLevel, new LongestSuit(Suit.Diamonds), longestMajor(4)));
        bids.add(shows(Call.Bid._1D, OneLevel, shape(3, 3), shape(Suit.Clubs, 0, 2), longestMajor(4)));

        // Special case longer hearts than spades, but not enough points to reverse. Bid spades first.
        bids.add(shows(Call.Bid._1S, Minimum, shape(5, 10), new BetterSuit.ShowsBetterSuit(Suit.Hearts, Suit.Spades, Suit.Hearts, true)));

        bids.add(shows(Call.Bid._1H, OneLevel, shape(5, 10), new BetterSuit.ShowsBetterSuit(Suit.Hearts, Suit.Spades, Suit.Hearts, true)));
        bids.add(shows(Call.Bid._1S, OneLevel, shape(5, 10), new BetterSuit.ShowsBetterSuit(Suit.Spades, Suit.Hearts, Suit.Spades, true)));

        bids.add(shows(Call.Pass, isSeat(4), DontOpen));

        return bids;
    }
}
