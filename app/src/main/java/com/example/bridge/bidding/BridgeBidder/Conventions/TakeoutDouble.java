package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.bridge.bidding.BridgeBidder.LCStandard.Compete;
import com.example.bridge.bidding.BridgeBidder.LCStandard.UserText;
import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.BidRule;
import com.example.bridge.bidding.BridgeBidder.Tools.Bidder;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.CallFeature;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCalls;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;
import com.example.bridge.bidding.BridgeBidder.Tools.Strain;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class TakeoutDouble extends Bidder {
    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        if (ps.isOpponentsContract() && ps.getBiddingState().getOpeningBid() != null && ps.getBiddingState().getOpeningBid().getStrain() != Strain.NoTrump) {
            Bid contractBid = ps.getBiddingState().getContract().getBid();
            if (contractBid != null && contractBid.getLevel() <= 3) {
                bids.addAll(takeout(ps, contractBid.getLevel()));
            }
        }
        return bids;
    }

    private static List<CallFeature> takeout(PositionState ps, int level) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(properties(Call.DOUBLE, TakeoutDouble::respond, true, UserText.TakeoutDouble));
        bids.add(shows(Call.DOUBLE, points(17, 100)));

        BidRule rule = shows(Call.DOUBLE, points(12, 16), isBidAvailable(4, Suit.Clubs));
        for (Suit s : Suit.values()) {
            if (ps.getOppsPairState().haveShownSuit(s)) {
                rule.addConstraint(shape(s, 0, 4));
            } else {
                rule.addConstraint(shape(s, 3, 4));
            }
        }
        bids.add(rule);
        return bids;
    }

    public static PositionCalls respond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(partnerBids(TakeoutDouble::doublerRebid));
        choices.addRules(
                shows(Call.PASS, ruleOf9()),
                shows(Bid._1D, takeoutSuit(), points(0, 8)),
                shows(Bid._1H, takeoutSuit(), points(0, 8)),
                shows(Bid._1S, takeoutSuit(), points(0, 8)),
                shows(Bid._1NT, BALANCED, OPPS_STOPPED, points(6, 10)),
                shows(Bid._2C, takeoutSuit(), points(0, 8)),
                shows(Bid._2D, takeoutSuit(), IS_NON_JUMP, points(0, 8)),
                shows(Bid._2D, takeoutSuit(), IS_SINGLE_JUMP, points(9, 11)),
                shows(Bid._2H, takeoutSuit(), IS_NON_JUMP, points(0, 8)),
                shows(Bid._2H, takeoutSuit(), IS_SINGLE_JUMP, points(9, 11)),
                shows(Bid._2S, takeoutSuit(), IS_NON_JUMP, points(0, 8)),
                shows(Bid._2S, takeoutSuit(), IS_SINGLE_JUMP, points(9, 11)),
                shows(Bid._2NT, BALANCED, OPPS_STOPPED, points(11, 12)),
                shows(Bid._4H, takeoutSuit(), points(12, 40)),
                shows(Bid._4S, takeoutSuit(), points(12, 40)),
                shows(Bid._3NT, BALANCED, OPPS_STOPPED, points(13, 40)),
                shows(Call.PASS, not(rho(isLastBid(Call.PASS))))
        );
        return choices;
    }

    private static Iterable<CallFeature> doublerRebid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(TakeoutDouble::advancerRebid));
        bids.add(shows(Bid._4H, FIT_8_PLUS, pairPoints(25, 30)));
        bids.add(shows(Bid._4S, FIT_8_PLUS, pairPoints(25, 30)));
        
        Range mediumTakeout = new Range(17, 19);
        Range maximumTakeout = new Range(20, 100);
        Range minimumTakeout = new Range(11, 16);

        bids.add(shows(Bid._2D, raisePartner(), dummyPoints(mediumTakeout)));
        bids.add(shows(Bid._2H, raisePartner(), dummyPoints(mediumTakeout)));
        bids.add(shows(Bid._2S, raisePartner(), dummyPoints(mediumTakeout)));

        bids.add(shows(Bid._3C, raisePartner(), dummyPoints(mediumTakeout)));
        bids.add(shows(Bid._3D, raisePartner(), dummyPoints(mediumTakeout)));
        bids.add(shows(Bid._3D, raisePartner(null, 1, 8), dummyPoints(maximumTakeout)));
        bids.add(shows(Bid._3H, raisePartner(), dummyPoints(mediumTakeout)));
        bids.add(shows(Bid._3H, raisePartner(null, 1, 8), dummyPoints(maximumTakeout)));
        bids.add(shows(Bid._3S, raisePartner(), dummyPoints(mediumTakeout)));
        bids.add(shows(Bid._3S, raisePartner(null, 1, 8), dummyPoints(maximumTakeout)));

        bids.add(shows(Bid._1H, shape(5, 11), points(mediumTakeout)));
        bids.add(shows(Bid._1S, shape(5, 11), points(mediumTakeout)));
        bids.add(shows(Bid._2C, shape(5, 11), points(mediumTakeout)));
        bids.add(shows(Bid._2D, shape(5, 11), points(mediumTakeout)));
        bids.add(shows(Bid._2H, IS_NON_JUMP, shape(5, 11), points(mediumTakeout)));
        bids.add(shows(Bid._2S, IS_NON_JUMP, shape(5, 11), points(mediumTakeout)));

        bids.add(shows(Call.PASS, points(minimumTakeout)));
        return bids;
    }

    public static PositionCalls advancerRebid(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Compete.compBids(ps));
        return choices;
    }
}




























































































































































































































































































































































































































































































