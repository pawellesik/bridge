package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.bridge.bidding.BridgeBidder.LCStandard.TwoNoTrump;
import com.example.bridge.bidding.BridgeBidder.LCStandard.ThreeNoTrump;
import com.example.bridge.bidding.BridgeBidder.LCStandard.UserText;
import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.Bidder;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.CallFeature;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCalls;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;

import java.util.ArrayList;
import java.util.List;

public class Strong2Clubs extends Bidder {
    protected static final Range STRONG_OPEN_RANGE = new Range(22, 40);
    protected static final Range GAME_IN_HAND = new Range(25, 40);
    protected static final Range POSITIVE_RESPONSE = new Range(8, 18);
    protected static final Range WAITING = new Range(0, 18);

    protected static final Range RESPOND_BUST = new Range(0, 4);
    protected static final Range RESPOND_SUIT_NOT_BUST = new Range(5, 7);
    protected static final Range RESPOND_NT_NOT_BUST = new Range(5, 9);

    public static Iterable<CallFeature> open(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(properties(Bid._2C, Strong2Clubs::respond, true, UserText.Strong));
        rules.add(shows(Bid._2C, points(STRONG_OPEN_RANGE)));
        return rules;
    }

    private static PositionCalls respond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        if (ps.getRHO().isPassed()) {
            Call[] positiveCalls = {Bid._2H, Bid._2S, Bid._2NT, Bid._3C, Bid._3D};
            choices.addRules(
                propertiesForcingToGame(positiveCalls, Strong2Clubs::openerRebidPositiveResponse, true),
                shows(Bid._2H, points(POSITIVE_RESPONSE), shape(5, 11), GOOD_PLUS_SUIT),
                shows(Bid._2S, points(POSITIVE_RESPONSE), shape(5, 11), GOOD_PLUS_SUIT),
                shows(Bid._2NT, points(POSITIVE_RESPONSE), BALANCED),
                shows(Bid._3C, points(POSITIVE_RESPONSE), shape(5, 11), GOOD_PLUS_SUIT),
                shows(Bid._3D, points(POSITIVE_RESPONSE), shape(5, 11), GOOD_PLUS_SUIT),

                properties(Bid._2D, Strong2Clubs::openerRebidWaiting, true),
                shows(Bid._2D, points(WAITING))
            );
        } else {
            choices.addPassRule();
        }
        return choices;
    }

    private static PositionCalls openerRebidWaiting(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(TwoNoTrump.AFTER_2C_OPEN.bids(ps));
        choices.addRules(ThreeNoTrump.AFTER_2C_OPEN.bids(ps));
        choices.addRules(
            properties(new Call[]{Bid._2H, Bid._2S, Bid._3C, Bid._3D}, Strong2Clubs::responder2ndBid, true),
            shows(Bid._2H, shape(5, 11)),
            shows(Bid._2S, shape(5, 11)),
            shows(Bid._3C, shape(5, 11)),
            shows(Bid._3D, shape(5, 11))
        );
        return choices;
    }

    private static PositionCalls openerRebidPositiveResponse(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(Gerber.initiateConvention(ps));
        choices.addRules(
            partnerBids(Strong2Clubs::responder2ndBid),
            shows(Bid._3H, FIT_8_PLUS),
            shows(Bid._3S, FIT_8_PLUS),
            shows(Bid._4C, FIT_8_PLUS),
            shows(Bid._4D, FIT_8_PLUS),
            shows(Bid._2S, shape(5, 11)),
            shows(Bid._3C, shape(5, 11)),
            shows(Bid._3D, shape(5, 11)),
            shows(Bid._3H, shape(5, 11)),
            shows(Bid._3S, IS_NON_JUMP, shape(5, 11)),
            shows(Bid._3NT, BALANCED),
            shows(Bid._4C, shape(5, 11), IS_NON_JUMP)
        );
        return choices;
    }

    private static PositionCalls responder2ndBid(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(Gerber.initiateConvention(ps));
        choices.addRules(
            partnerBids(Strong2Clubs::openerPlaceContractFactory),
            shows(Bid._3H, FIT_8_PLUS),
            shows(Bid._3S, FIT_8_PLUS),
            shows(Bid._4C, FIT_8_PLUS),
            shows(Bid._4D, FIT_8_PLUS),

            properties(Bid._3C, Strong2Clubs::partnerIsBust, true),
            properties(Bid._3D, Strong2Clubs::partnerIsBust, true, partner(isLastBid(Bid._3C))),
            shows(Bid._3C, points(RESPOND_BUST)),
            shows(Bid._3D, partner(isLastBid(Bid._3C)), points(RESPOND_BUST)),

            shows(Bid._2S, shape(5, 11), points(RESPOND_SUIT_NOT_BUST)),
            shows(Bid._3H, shape(5, 11), points(RESPOND_SUIT_NOT_BUST)),
            shows(Bid._3S, IS_NON_JUMP, shape(5, 11), points(RESPOND_SUIT_NOT_BUST)),

            shows(Bid._3NT, points(RESPOND_NT_NOT_BUST))
        );
        choices.addPassRule();
        return choices;
    }

    private static PositionCalls openerPlaceContractFactory(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(openerPlaceContract(ps));
        return choices;
    }

    private static Iterable<CallFeature> openerPlaceContract(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        for (CallFeature cf : Blackwood.initiateConvention(ps)) {
            bids.add(cf);
        }
        bids.add(shows(Bid._4H, FIT_8_PLUS));
        bids.add(shows(Bid._4S, FIT_8_PLUS));
        bids.add(shows(Bid._4C, FIT_8_PLUS));
        bids.add(shows(Bid._4D, FIT_8_PLUS));
        bids.add(shows(Bid._3NT));
        bids.add(shows(Call.PASS));
        return bids;
    }

    private static PositionCalls partnerIsBust(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Bid._4H, IS_REBID, points(GAME_IN_HAND)),
            shows(Bid._4S, IS_REBID, points(GAME_IN_HAND)),
            shows(Bid._5C, IS_REBID, shape(7, 11), points(GAME_IN_HAND)),
            shows(Bid._5D, IS_REBID, shape(7, 11), points(GAME_IN_HAND)),
            shows(Bid._3NT, points(GAME_IN_HAND)),
            shows(Bid._3H, IS_REBID),
            shows(Bid._3S, IS_REBID),
            shows(Bid._4C, IS_REBID),
            shows(Bid._4D, IS_REBID)
        );
        return choices;
    }
}




























































































































































































































































































































































































































































































