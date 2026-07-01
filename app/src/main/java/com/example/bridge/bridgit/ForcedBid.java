package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class ForcedBid extends Bidder {
    public static Iterable<CallFeature> bids(PositionState ps) {
        List<CallFeature> res = new ArrayList<>();
        if (ps.isForcedToBid()) {
            res.add(shows(new Call.Bid(2, Suit.Clubs), fit(8, Suit.Clubs)));
            res.add(shows(new Call.Bid(2, Suit.Diamonds), fit(8, Suit.Diamonds)));
            res.add(shows(new Call.Bid(2, Suit.Hearts), fit(8, Suit.Hearts)));
            res.add(shows(new Call.Bid(2, Suit.Spades), fit(8, Suit.Spades)));

            res.add(shows(new Call.Bid(3, Suit.Clubs), isJump(0), fit(8, Suit.Clubs)));
            res.add(shows(new Call.Bid(3, Suit.Diamonds), isJump(0), fit(8, Suit.Diamonds)));
            res.add(shows(new Call.Bid(3, Suit.Hearts), isJump(0), fit(8, Suit.Hearts)));
            res.add(shows(new Call.Bid(3, Suit.Spades), isJump(0), fit(8, Suit.Spades)));

            res.add(shows(new Call.Bid(1, Strain.NoTrump)));
            res.add(shows(new Call.Bid(2, Strain.NoTrump), isJump(0)));
            res.add(shows(new Call.Bid(3, Strain.NoTrump), isJump(0)));
        }
        return res;
    }
}
