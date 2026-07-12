package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.bridge.bidding.BridgeBidder.*;
import com.example.bridge.bidding.BridgeBidder.Conventions.Blackwood;
import java.util.ArrayList;
import java.util.List;

public class Advance extends LCStandard {
    public static final Range ADVANCE_NEW_SUIT_1_LEVEL = new Range(6, 40);
    public static final Range NEW_SUIT_2_LEVEL = new Range(11, 40);
    public static final Range ADVANCE_TO_1NT = new Range(6, 10);
    public static final Range PAIR_ADVANCE_TO_2NT = new Range(23, 24);
    public static final Range PAIR_ADVANCE_TO_3NT = new Range(25, 31);
    public static final Range WEAK_JUMP_RAISE = new Range(0, 8);
    public static final Range RAISE = new Range(6, 10);
    public static final Range ADVANCE_CUEBID = new Range(11, 40);

    public static PositionCalls firstBid(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Call lastCall = ps.getPartner().getLastCall();
        if (lastCall instanceof Bid) {
            Bid partnerBid = (Bid) lastCall;
            Suit partnerSuit = partnerBid.getSuit();

            choices.addRules(
                partnerBids(Overcall::secondBid),

                shows(Bid._4C, isJump(1, 2), fit(10), dummyPoints(WEAK_JUMP_RAISE)),
                shows(Bid._4D, isJump(1, 2, 3), fit(10), dummyPoints(WEAK_JUMP_RAISE)),
                shows(Bid._4H, isJump(1, 2, 3), fit(10), dummyPoints(WEAK_JUMP_RAISE)),
                shows(Bid._4S, isJump(1, 2, 3), fit(10), dummyPoints(WEAK_JUMP_RAISE)),

                shows(Bid._2D, raisePartner(), dummyPoints(RAISE)),
                shows(Bid._2H, raisePartner(), dummyPoints(RAISE)),
                shows(Bid._2S, raisePartner(), dummyPoints(RAISE)),

                shows(Bid._1H, points(ADVANCE_NEW_SUIT_1_LEVEL), shape(5), GOOD_PLUS_SUIT),
                shows(Bid._1H, points(ADVANCE_NEW_SUIT_1_LEVEL), shape(6, 11)),

                shows(Bid._1S, points(ADVANCE_NEW_SUIT_1_LEVEL), shape(5), GOOD_PLUS_SUIT),
                shows(Bid._1S, points(ADVANCE_NEW_SUIT_1_LEVEL), shape(6, 11)),

                shows(Bid._2C, IS_NEW_SUIT, points(NEW_SUIT_2_LEVEL), shape(5), GOOD_PLUS_SUIT),
                shows(Bid._2C, IS_NEW_SUIT, points(NEW_SUIT_2_LEVEL), shape(6, 11)),
                shows(Bid._2D, IS_NEW_SUIT, points(NEW_SUIT_2_LEVEL), shape(5), GOOD_PLUS_SUIT),
                shows(Bid._2D, IS_NEW_SUIT, points(NEW_SUIT_2_LEVEL), shape(6, 11)),
                shows(Bid._2H, IS_NEW_SUIT, IS_NON_JUMP, points(NEW_SUIT_2_LEVEL), shape(5), GOOD_PLUS_SUIT),
                shows(Bid._2H, IS_NEW_SUIT, IS_NON_JUMP, points(NEW_SUIT_2_LEVEL), shape(6, 11)),
                shows(Bid._2S, IS_NEW_SUIT, IS_NON_JUMP, points(NEW_SUIT_2_LEVEL), shape(5), GOOD_PLUS_SUIT),
                shows(Bid._2S, IS_NEW_SUIT, IS_NON_JUMP, points(NEW_SUIT_2_LEVEL), shape(6, 11)),

                properties(new Bid[] { Bid._2C, Bid._2D, Bid._2H, Bid._2S }, true, IS_CUE_BID),
                shows(Bid._2C, IS_CUE_BID, fit(partnerSuit), dummyPoints(ADVANCE_CUEBID)),
                shows(Bid._2D, IS_CUE_BID, fit(partnerSuit), dummyPoints(ADVANCE_CUEBID)),
                shows(Bid._2H, IS_CUE_BID, fit(partnerSuit), dummyPoints(ADVANCE_CUEBID)),
                shows(Bid._2S, IS_CUE_BID, fit(partnerSuit), dummyPoints(ADVANCE_CUEBID)),

                shows(Bid._3C, IS_SINGLE_JUMP, fit(9), dummyPoints(WEAK_JUMP_RAISE)),
                shows(Bid._3D, IS_SINGLE_JUMP, fit(9), dummyPoints(WEAK_JUMP_RAISE)),
                shows(Bid._3H, IS_SINGLE_JUMP, fit(9), dummyPoints(WEAK_JUMP_RAISE)),
                shows(Bid._3S, IS_SINGLE_JUMP, fit(9), dummyPoints(WEAK_JUMP_RAISE)),

                shows(Bid._1NT, OPPS_STOPPED, points(ADVANCE_TO_1NT)),
                shows(Bid._2NT, OPPS_STOPPED, pairPoints(PAIR_ADVANCE_TO_2NT)),
                shows(Bid._3NT, OPPS_STOPPED, pairPoints(PAIR_ADVANCE_TO_3NT))
            );
            choices.addRules(Blackwood.initiateConvention(ps));
            choices.addPassRule();
        }
        return choices;
    }

    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._4C, fit(), pairPoints(26, 28)));
        bids.add(shows(Bid._4D, fit(), pairPoints(26, 28)));
        bids.add(shows(Bid._4H, fit(), pairPoints(26, 31)));
        bids.add(shows(Bid._4S, fit(), pairPoints(26, 31)));
        bids.add(shows(Call.PASS));
        return bids;
    }
}




























































































































































































































































































































































































































































































