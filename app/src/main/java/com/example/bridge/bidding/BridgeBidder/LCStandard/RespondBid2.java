package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.licytacja.moje.BridgeBidder.*;
import com.example.licytacja.moje.BridgeBidder.Conventions.Blackwood;
import java.util.ArrayList;
import java.util.List;

public class RespondBid2 extends Respond {

    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(OpenBid3::thirdBid));

        bids.add(shows(Bid._2S, raisePartner(), points(MINIMUM_HAND)));
        bids.add(shows(Bid._3S, raisePartner(null, 1, 8), points(MEDIUM_HAND)));
        bids.add(shows(Bid._4S, raisePartner(null, 1, 8), points(RAISE_TO_4M)));

        bids.add(shows(Bid._2C, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));
        bids.add(shows(Bid._2D, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));
        bids.add(shows(Bid._2H, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));
        bids.add(shows(Bid._2S, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));

        bids.add(shows(Bid._3C, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));
        bids.add(shows(Bid._3D, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));
        bids.add(shows(Bid._3H, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));
        bids.add(shows(Bid._3S, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));

        bids.add(shows(Bid._1NT, points(MINIMUM_HAND)));

        bids.add(shows(Bid._2C, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        bids.add(shows(Bid._2D, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        bids.add(shows(Bid._2H, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        bids.add(shows(Bid._2S, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));

        bids.add(shows(Bid._3C, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        bids.add(shows(Bid._3D, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        bids.add(shows(Bid._3H, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        bids.add(shows(Bid._3S, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));

        for (CallFeature cf : Compete.compBids(ps)) {
            bids.add(cf);
        }
        return bids;
    }

    public static Iterable<CallFeature> openerInvitedGame(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._4H, FIT_8_PLUS, pairPoints(PAIR_GAME)));
        bids.add(shows(Bid._4S, FIT_8_PLUS, pairPoints(PAIR_GAME)));
        bids.add(shows(Call.PASS));
        return bids;
    }

    public static PositionCalls secondBid2Over1(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit trump = ps.getPairState().getTrumpSuit();
        if (trump != null) {
            choices.addRules(shows(new Bid(4, trump), pairPoints(25, 27)));
        } else {
            choices.addRules(
                properties(new Call[]{Bid._2H, Bid._2S, Bid._3H, Bid._3S, Bid._4H, Bid._4S}, true, IS_PARTNERS_SUIT),
                shows(Bid._4H, IS_PARTNERS_SUIT, FIT_8_PLUS, dummyPoints(12, 13)),
                shows(Bid._4S, IS_PARTNERS_SUIT, FIT_8_PLUS, dummyPoints(12, 13)),
                shows(Bid._2H, IS_PARTNERS_SUIT, FIT_8_PLUS, dummyPoints(14, 40)),
                shows(Bid._2S, IS_PARTNERS_SUIT, FIT_8_PLUS, dummyPoints(14, 40)),
                shows(Bid._3H, IS_PARTNERS_SUIT, IS_NON_JUMP, FIT_8_PLUS, dummyPoints(14, 40)),
                shows(Bid._3S, IS_PARTNERS_SUIT, IS_NON_JUMP, FIT_8_PLUS, dummyPoints(14, 40))
            );
        }
        choices.addRules(Blackwood.initiateConvention(ps));
        return choices;
    }
}
