package com.example.bridge.bidding.BridgeBidder.WJSimple;

import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.CallFeature;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCalls;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;

import java.util.ArrayList;
import java.util.List;

public class RespondWJSimple extends WJSimple {
    public static final Range RESPOND_PASS = new Range(0, 6);
    public static final Range MINIMUM_HAND = new Range(7, 10);
    public static final Range JUMP_HAND = new Range(11, 28);
    public static final Range JUMP_AFTER_PASS = new Range(11, 11);
    public static final Range WEAK = new Range(7, 11);

    public static PositionCalls oneClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(OpenBid2WJSimple::responderClub),
                shows(Bid._1D, points(7, 40), id("RespondWJSimple.oneClub _1D")),
                shows(Bid._1H, points(7, 40), shape(4, 13), id("RespondWJSimple.oneClub _1H")),
                shows(Bid._1S, points(7, 40), shape(4, 13), id("RespondWJSimple.oneClub _1S"))
        );
        choices.addPassRule(points(RESPOND_PASS));
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls oneDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(OpenBid2WJSimple::responderChangedSuits),
                shows(Bid._1H, points(7, 40), shape(4, 13), id("RespondWJSimple.oneDiamond _1H")),
                shows(Bid._1S, points(7, 40), shape(4, 13), id("RespondWJSimple.oneDiamond _1S"))
        );
        choices.addPassRule(points(RESPOND_PASS));
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(OpenBid2WJSimple::responderChangedSuits),
                shows(Bid._1S, points(7, 40), shape(4, 13), id("RespondWJSimple.oneHeart _1S")),
                shows(Bid._2H, points(7, 10), fit(), id("RespondWJSimple.oneHeart _2H"))
        );
        choices.addPassRule(points(RESPOND_PASS));
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls oneSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(OpenBid2WJSimple::responderChangedSuits),
                shows(Bid._2S, points(7, 10), fit(), id("RespondWJSimple.oneSpade _2S"))
        );
        choices.addPassRule(points(RESPOND_PASS));
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static Iterable<CallFeature> weakOpen(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Call.PASS));
        return bids;
    }
}




























































































































































































































































































































































































































































































