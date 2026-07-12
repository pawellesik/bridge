package com.example.bridge.bidding.BridgeBidder.WJSimple;

import com.example.bridge.bidding.BridgeBidder.Conventions.AcesAsk;
import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCalls;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

public class OpenBid2WJSimple extends OpenWJSimple {

    public static PositionCalls responderChangedSuits(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));

        choices.addRules(
                partnerBids(RespondBid2WJSimple::secondBid),
                shows(Bid._2S, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2WJSimple.responderChangedSuits _2S")),
                shows(Bid._2H, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2WJSimple.responderChangedSuits _2H")),

                shows(Bid._2S, IS_NEW_SUIT, shape(4, 11), id("OpenBid2WJSimple.responderChangedSuits _2S")),
                shows(Bid._2H, IS_NEW_SUIT, shape(4, 11), id("OpenBid2WJSimple.responderChangedSuits _2H")),

                shows(Bid._3S, IS_NEW_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2WJSimple.responderChangedSuits _3S")),
                shows(Bid._3H, IS_NEW_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2WJSimple.responderChangedSuits _3H"))

        );
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls responderClub(PositionState ps) {
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            return responderChangedSuits(ps);
        }

        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                shows(Bid._2H, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2WJSimple.responderClub _2H")),
                shows(Bid._2S, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2WJSimple.responderClub _2S")),
                shows(Bid._3H, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2WJSimple.responderClub _3H")),
                shows(Bid._3S, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2WJSimple.responderClub _3S"))
        );
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls semiForcingNT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(RespondBid2WJSimple::secondBid),
                shows(Bid._2C, shape(Suit.Clubs, 3, 11), id("OpenBid2WJSimple.semiForcingNT _2C")),
                shows(Bid._2D, shape(Suit.Diamonds, 3, 11), id("OpenBid2WJSimple.semiForcingNT _2D")),
                shows(Bid._2H, shape(Suit.Hearts, 3, 11), id("OpenBid2WJSimple.semiForcingNT _2H")),
                shows(Bid._2S, shape(Suit.Spades, 3, 11), id("OpenBid2WJSimple.semiForcingNT _2S")),
                shows(Call.PASS, id("OpenBid2WJSimple.semiForcingNT PASS"))
        );
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls responderBidNT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls responderRaisedMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls responderRaisedMajor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls responder1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls responder2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }
}




























































































































































































































































































































































































































































































