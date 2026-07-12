package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RespondBid2NatC extends RespondNatC {

    public static PositionCalls responderClubJumpMinorChangeMajor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(partnerBids(OpenBid3NatC::thirdBid),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME),id("RespondBid2NatC.responderClubJumpMinorChangeMajor _4S")),
                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _4H")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _3NT")),
                shows(Bid._3NT, partnerLastSuitShape(0, 2), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _3NT"))
        );
        return choices;
    }
    public static PositionCalls colorAfterPass(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(partnerBids(OpenBid3NatC::thirdBid),
                shows(Bid._2S, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2S")),
                shows(Bid._2H, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2H")),
                shows(Call.PASS, id("RespondBid2NatC.colorAfterPass PASS"))
        );

        return choices;
    }


    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(OpenBid3NatC::thirdBid));
        bids.add(shows(Bid._2S, IS_PARTNERS_SUIT, betterThan(Suit.Hearts), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, IS_PARTNERS_SUIT, betterThan(Suit.Spades), id("RespondBid2NatC.secondBid _2H")));

        bids.add(shows(Bid._2S, fit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, fit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2H")));

        //bids.add(shows(Bid._2S, raisePartner(), points(MINIMUM_HAND)));
        //bids.add(shows(Bid._3S, raisePartner(null, 1, 8), points(MEDIUM_HAND)));
        //bids.add(shows(Bid._4S, raisePartner(null, 1, 8), points(RAISE_TO_4M)));

        //bids.add(shows(Bid._2C, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));
        //bids.add(shows(Bid._2D, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));
        //bids.add(shows(Bid._2H, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));
        //bids.add(shows(Bid._2S, IS_REBID, shape(6, 11), points(MINIMUM_HAND)));

        //bids.add(shows(Bid._3C, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));
        //bids.add(shows(Bid._3D, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));
        //bids.add(shows(Bid._3H, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));
        //bids.add(shows(Bid._3S, IS_REBID, shape(6, 11), points(MEDIUM_HAND)));

        //bids.add(shows(Bid._1NT, points(MINIMUM_HAND)));

        //bids.add(shows(Bid._2C, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        //bids.add(shows(Bid._2D, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        //bids.add(shows(Bid._2H, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        //bids.add(shows(Bid._2S, FIT_8_PLUS, IS_NOT_REBID, IS_FORCED_TO_BID, points(MINIMUM_HAND)));

        //bids.add(shows(Bid._3C, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        //bids.add(shows(Bid._3D, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        //bids.add(shows(Bid._3H, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        //bids.add(shows(Bid._3S, FIT_8_PLUS, IS_NOT_REBID, IS_NON_JUMP, IS_FORCED_TO_BID, points(MINIMUM_HAND)));
        bids.add(shows(Call.PASS));
        return bids;
    }

    public static PositionCalls secondBidToGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME)), id("RespondBid2NatC.secondBidToGame _4H"),
                shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME)),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidToGame _3NT")),
                shows(Call.PASS), id("RespondBid2NatC.secondBidToGame PASS"));

        return choices;
    }

    public static PositionCalls openerInvitedGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME)),
                shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.openerInvitedGame _4S")),
                shows(Call.PASS, id("RespondBid2NatC.openerInvitedGame PASS")));

        return choices;
    }

}




























































































































































































































































































































































































































































































