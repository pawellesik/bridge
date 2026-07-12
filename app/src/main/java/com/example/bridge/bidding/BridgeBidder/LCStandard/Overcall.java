package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.bridge.bidding.BridgeBidder.*;
import com.example.bridge.bidding.BridgeBidder.Conventions.TakeoutDouble;
import java.util.ArrayList;
import java.util.List;

public class Overcall extends LCStandard {

    public static PositionCalls getOvercallPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Overcall::suitOvercall);
        choices.addRules(NoTrump::strongOvercall);
        choices.addRules(TakeoutDouble::initiateConvention);
        choices.addRules(NoTrump::balancingOvercall);
        choices.addPassRule(points(LESS_THAN_OVERCALL));

        return choices;
    }

    public static Iterable<CallFeature> suitOvercall(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(Advance::firstBid));

        // Weak jump overcalls
        bids.add(shows(Bid._2D, IS_SINGLE_JUMP, IS_NOT_CUE_BID, points(OVERCALL_WEAK_2_LEVEL), shape(6), GOOD_PLUS_SUIT));
        bids.add(shows(Bid._2H, IS_SINGLE_JUMP, IS_NOT_CUE_BID, points(OVERCALL_WEAK_2_LEVEL), shape(6), GOOD_PLUS_SUIT));
        bids.add(shows(Bid._2S, IS_SINGLE_JUMP, IS_NOT_CUE_BID, points(OVERCALL_WEAK_2_LEVEL), shape(6), GOOD_PLUS_SUIT));

        bids.add(shows(Bid._3C, IS_SINGLE_JUMP, IS_NOT_CUE_BID, points(OVERCALL_WEAK_3_LEVEL), shape(7), DECENT_PLUS_SUIT));
        bids.add(shows(Bid._3D, isJump(1, 2), IS_NOT_CUE_BID, points(OVERCALL_WEAK_3_LEVEL), shape(7), DECENT_PLUS_SUIT));
        bids.add(shows(Bid._3H, isJump(1, 2), IS_NOT_CUE_BID, points(OVERCALL_WEAK_3_LEVEL), shape(7), DECENT_PLUS_SUIT));
        bids.add(shows(Bid._3S, isJump(1, 2), IS_NOT_CUE_BID, points(OVERCALL_WEAK_3_LEVEL), shape(7), DECENT_PLUS_SUIT));

        // 1-level overcalls
        bids.add(shows(Bid._1S, points(OVERCALL_1_LEVEL), shape(6, 10)));
        bids.add(shows(Bid._1H, points(OVERCALL_1_LEVEL), shape(6, 10)));
        bids.add(shows(Bid._1D, points(OVERCALL_1_LEVEL), shape(6, 10)));

        bids.add(shows(Bid._1S, points(OVERCALL_1_LEVEL), shape(5), DECENT_PLUS_SUIT));
        bids.add(shows(Bid._1H, points(OVERCALL_1_LEVEL), shape(5), DECENT_PLUS_SUIT));
        bids.add(shows(Bid._1D, points(OVERCALL_1_LEVEL), shape(5), DECENT_PLUS_SUIT));

        bids.add(shows(Bid._1S, points(10, 16), shape(5)));
        bids.add(shows(Bid._1H, points(10, 16), shape(5)));
        bids.add(shows(Bid._1D, points(10, 16), shape(5)));

        // Strong non-jump overcalls at 2-level
        bids.add(shows(Bid._2S, IS_NON_JUMP, IS_NOT_CUE_BID, points(OVERCALL_STRONG_2_LEVEL), shape(5, 11)));
        bids.add(shows(Bid._2H, IS_NON_JUMP, IS_NOT_CUE_BID, points(OVERCALL_STRONG_2_LEVEL), shape(5, 11)));
        bids.add(shows(Bid._2D, IS_NON_JUMP, IS_NOT_CUE_BID, points(OVERCALL_STRONG_2_LEVEL), shape(5, 11)));
        bids.add(shows(Bid._2C, IS_NOT_CUE_BID, points(OVERCALL_STRONG_2_LEVEL), shape(5, 11)));

        return bids;
    }

    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(Advance::secondBid));

        Range supportAdvancer = new Range(12, 17);
        bids.add(shows(Bid._2H, IS_NOT_REBID, fit(), IS_NON_JUMP, points(supportAdvancer)));
        bids.add(shows(Bid._2S, IS_NOT_REBID, fit(), IS_NON_JUMP, points(supportAdvancer)));
        bids.add(shows(Bid._3C, IS_NOT_REBID, fit(), IS_NON_JUMP, points(supportAdvancer)));
        bids.add(shows(Bid._3D, IS_NOT_REBID, fit(), IS_NON_JUMP, points(supportAdvancer)));
        bids.add(shows(Bid._3H, IS_NOT_REBID, fit(), IS_NON_JUMP, points(supportAdvancer)));
        bids.add(shows(Bid._3S, IS_NOT_REBID, fit(), IS_NON_JUMP, points(supportAdvancer)));

        bids.add(shows(Bid._2H, IS_REBID, shape(6, 10)));
        bids.add(shows(Bid._2S, IS_REBID, shape(6, 10)));
        bids.add(shows(Bid._3C, IS_REBID, shape(6, 10), IS_NON_JUMP));
        bids.add(shows(Bid._3D, IS_REBID, shape(6, 10), IS_NON_JUMP));
        bids.add(shows(Bid._3H, IS_REBID, shape(6, 10), IS_NON_JUMP));
        bids.add(shows(Bid._3S, IS_REBID, shape(6, 10), IS_NON_JUMP));

        bids.add(shows(Bid._3NT, OPPS_STOPPED, pairPoints(25, 30)));
        bids.add(shows(Call.PASS));
        return bids;
    }
}




























































































































































































































































































































































































































































































