package com.example.bridge.bidding.BridgeBidder.WJSimple;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.ArrayList;
import java.util.List;

public class CompeteWJSimple extends WJSimple {

    public static Iterable<CallFeature> compBids(PositionState ps) {

        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), id("CompeteWJSimple.compBids _4H")));
        bids.add(shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), id("CompeteWJSimple.compBids _4S")));

        bids.add(shows(Bid._4H, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), id("CompeteWJSimple.compBids _4H")));
        bids.add(shows(Bid._4S, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), id("CompeteWJSimple.compBids _4S")));

        bids.add(shows(Bid._2C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("CompeteWJSimple.compBids _2C")));
        bids.add(shows(Bid._2D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("CompeteWJSimple.compBids _2D")));
        bids.add(shows(Bid._2H, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("CompeteWJSimple.compBids _2H")));
        bids.add(shows(Bid._2S, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("CompeteWJSimple.compBids _2S")));

        bids.add(shows(Bid._3C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("CompeteWJSimple.compBids _3C")));
        bids.add(shows(Bid._3D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("CompeteWJSimple.compBids _3D")));

        bids.add(shows(Bid._3NT, OPPS_STOPPED, pairHighCardPoints(PAIR_GAME), id("CompeteWJSimple.compBids _3NT")));

        bids.add(shows(Bid._5C, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), fit(Suit.Spades, false), fit(Suit.Hearts, false), id("CompeteWJSimple.compBids _5C")));
        bids.add(shows(Bid._5D, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), fit(Suit.Spades, false), fit(Suit.Hearts, false), id("CompeteWJSimple.compBids _5D")));

        for (CallFeature cf : ForcedBidWJSimple.bids(ps)) {
            bids.add(cf);
        }
        bids.add(shows(Call.PASS, id("CompeteWJSimple.compBids _PASS")));
        return bids;
    }
}
