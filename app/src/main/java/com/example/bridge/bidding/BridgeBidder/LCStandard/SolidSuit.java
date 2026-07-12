package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.ArrayList;
import java.util.List;

public class SolidSuit extends LCStandard {
    public static Iterable<CallFeature> BIDS(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            bids.add(shows(new Bid(7, suit), shape(13)));
            bids.add(shows(new Bid(7, suit), shape(12), aces(2)));
            bids.add(shows(new Bid(6, suit), shape(12), aces(1)));

        }
        return bids;
    }
}
