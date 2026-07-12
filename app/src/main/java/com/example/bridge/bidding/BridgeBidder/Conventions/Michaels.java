package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.licytacja.moje.BridgeBidder.*;
import com.example.licytacja.moje.BridgeBidder.LCStandard.UserText;
import java.util.ArrayList;
import java.util.List;

public class Michaels extends Bidder {
    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        Call[] cueMinors = {Bid._2C, Bid._2D};
        bids.add(properties(cueMinors, Michaels::respondMajors, true, false, false, null, null, null, UserText.Michaels, IS_CUE_BID));
        bids.add(shows(Bid._2C, IS_CUE_BID, shape(Suit.Hearts, 5), shape(Suit.Spades, 5)));
        bids.add(shows(Bid._2D, IS_CUE_BID, shape(Suit.Hearts, 5), shape(Suit.Spades, 5)));

        bids.add(properties(Bid._2H, p -> respondMajorMinor(p, Suit.Spades), true, false, false, null, null, null, UserText.Michaels, IS_CUE_BID));
        bids.add(shows(Bid._2H, IS_CUE_BID, shape(Suit.Spades, 5), shape(Suit.Clubs, 5)));
        bids.add(shows(Bid._2H, IS_CUE_BID, shape(Suit.Spades, 5), shape(Suit.Diamonds, 5)));

        bids.add(properties(Bid._2S, p -> respondMajorMinor(p, Suit.Spades), true, false, false, null, null, null, UserText.Michaels, IS_CUE_BID));
        bids.add(shows(Bid._2S, IS_CUE_BID, shape(Suit.Hearts, 5), shape(Suit.Clubs, 5)));
        bids.add(shows(Bid._2S, IS_CUE_BID, shape(Suit.Hearts, 5), shape(Suit.Diamonds, 5)));
        return bids;
    }

    private static PositionCalls respondMajors(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(shows(Bid._2H, betterThan(Suit.Spades), points(0, 5)));
        choices.addRules(shows(Bid._2S, betterOrEqualTo(Suit.Hearts), points(0, 5)));
        return choices;
    }

    private static PositionCalls respondMajorMinor(PositionState ps, Suit majorSuit) {
        return new PositionCalls(ps);
    }
}
