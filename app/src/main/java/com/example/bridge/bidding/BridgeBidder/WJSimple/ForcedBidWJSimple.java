package com.example.bridge.bidding.BridgeBidder.WJSimple;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.ArrayList;
import java.util.List;

public class ForcedBidWJSimple extends WJSimple {
    public static Iterable<CallFeature> bids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        if (ps.isForcedToBid()) {
            bids.add(shows(Bid._2C, fit(8)));
            bids.add(shows(Bid._2D, fit(8)));
            bids.add(shows(Bid._2H, fit(8)));
            bids.add(shows(Bid._2S, fit(8)));

            bids.add(shows(Bid._3C, IS_NON_JUMP, fit(8)));
            bids.add(shows(Bid._3D, IS_NON_JUMP, fit(8)));
            bids.add(shows(Bid._3H, IS_NON_JUMP, fit(8)));
            bids.add(shows(Bid._3S, IS_NON_JUMP, fit(8)));

            bids.add(shows(Bid._4C, IS_NON_JUMP, fit(8)));
            bids.add(shows(Bid._4D, IS_NON_JUMP, fit(8)));
            bids.add(shows(Bid._4H, IS_NON_JUMP, fit(8)));
            bids.add(shows(Bid._4S, IS_NON_JUMP, fit(8)));

            bids.add(shows(Bid._1NT));
            bids.add(shows(Bid._2NT, IS_NON_JUMP));
            bids.add(shows(Bid._3NT, IS_NON_JUMP));
        }
        return bids;
    }
}
