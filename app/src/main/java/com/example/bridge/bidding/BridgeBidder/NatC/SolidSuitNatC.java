package com.example.bridge.bidding.BridgeBidder.NatC;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.ArrayList;
import java.util.List;

public class SolidSuitNatC extends NatC {
    public static Iterable<CallFeature> BIDS(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            bids.add(shows(new Bid(7, suit), shape(13)));
            bids.add(shows(new Bid(7, suit), shape(12), aces(2)));
            bids.add(shows(new Bid(6, suit), shape(12), aces(1)));
            bids.add(shows(new Bid(6, suit), shape(11), aces(3)));
            bids.add(shows(new Bid(6, suit), shape(10), aces(4)));
        }
        return bids;
    }
}
