package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

public class OpenBid2NatC extends OpenNatC {

    public static PositionCalls responderNegat(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                partnerBids(OpenBid2NatC::responderChangedSuits),
                properties(new Call[]{Bid._1NT}, RespondBid2NatC::colorAfterPass),

                shows(Bid._1NT, shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), highCardPoints(12, 17), id("OpenBid2NatC.responderNegat _1NT")),
                shows(Bid._1S, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1S")),
                shows(Bid._1H, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1H"))


        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices.addRules(
                    AcesAsk.initiateConvention(ps),

                    shows(Bid._2H, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2NatC.responderClub _2H")),
                    shows(Bid._2S, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2NatC.responderClub _2S")),
                    shows(Bid._3H, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2NatC.responderClub _3H")),
                    shows(Bid._3S, isJump(1), highCardPoints(OpeningStrongBiddingRange), shape(5, 10), id("OpenBid2NatC.responderClub _3S"))
            );
        } else {
            return responderChangedSuits(ps);
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderChangedSuits(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                properties(new Call[]{Bid._3S, Bid._3H}, RespondBid2NatC::secondBidToGame),
                partnerBids(RespondBid2NatC::secondBid),

                shows(Bid._1S, IS_NEW_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1S")),
                shows(Bid._1H, IS_NEW_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1H")),

                shows(Bid._2S, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2NatC.responderChangedSuits _2S")),
                shows(Bid._2H, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2NatC.responderChangedSuits _2H")),

                shows(Bid._2S, IS_NEW_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _2S")),
                shows(Bid._2H, IS_NEW_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _2H")),

                shows(Bid._3S, IS_NEW_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits _3S")),
                shows(Bid._3H, IS_NEW_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits _3H"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaisedMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), fit(), id("RespondNatC.oneSpade _5D")),
                shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(), id("RespondNatC.oneSpade _5C")),
                shows(Call.PASS, id("OpenBid2NatC.responderRaisedMajor _PASS"))
        );
        return choices;
    }

    public static PositionCalls responderRaisedMajor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                propertiesAgreeTrump(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::openerInvitedGame, false),

                shows(Bid._4H, FIT_8_PLUS, pairPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajor _4H")),
                shows(Bid._4S, FIT_8_PLUS, pairPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajor _4S")),

                shows(Bid._3H, FIT_8_PLUS, pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _3H")),
                shows(Bid._3S, FIT_8_PLUS, pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _3S")),

                shows(Bid._2H, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaisedMajor _2H")),
                shows(Bid._2S, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaisedMajor _2S")),

                shows(Call.PASS, id("OpenBid2NatC.responderRaisedMajor _PASS"))
        );
        return choices;
    }

    public static PositionCalls responder1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2H, IS_REBID, shape(6, 11), points(12, 17), id("OpenBid2NatC.responder1NT _2H")),
                shows(Bid._2S, IS_REBID, shape(6, 11), points(12, 17), id("OpenBid2NatC.responder1NT _2S")),

                shows(Bid._2C, IS_NEW_SUIT, shape(5, 6), points(12, 17), id("OpenBid2NatC.responder1NT _2C")),
                shows(Bid._2D, IS_NEW_SUIT, shape(5, 6), points(12, 17), id("OpenBid2NatC.responder1NT _2D")),
                shows(Bid._2H, IS_NEW_SUIT, shape(5, 6), points(12, 17), id("OpenBid2NatC.responder1NT _2H")),
                shows(Bid._2S, IS_NEW_SUIT, shape(5, 6), points(12, 17), id("OpenBid2NatC.responder1NT _2S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responder1NT _3NT")),
                shows(Call.PASS)
        );
        return choices;
    }

    public static PositionCalls responder2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                propertiesAgreeTrump(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::openerInvitedGame, false),

                shows(Bid._3H, FIT_8_PLUS, pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responder2NT _3H")),
                shows(Bid._3S, FIT_8_PLUS, pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responder2NT _3S")),

                shows(Bid._3H, IS_NEW_SUIT, shape(5, 10), pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responder2NT _3H")),
                shows(Bid._3S, IS_NEW_SUIT, shape(5, 10), pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responder2NT _3S")),

                shows(Bid._3D, IS_NEW_SUIT, shape(5, 10), id("OpenBid2NatC.responder2NT _3D")),
                shows(Bid._3C, IS_NEW_SUIT, shape(5, 10), id("OpenBid2NatC.responder2NT _3C")),

                shows(Bid._3NT, FIT_8_PLUS, pairPoints(PAIR_GAME), id("OpenBid2NatC.responder2NT _3NT")),
                shows(Bid._3NT, FIT_8_PLUS, pairPoints(PAIR_GAME), id("OpenBid2NatC.responder2NT _3NT")),
                shows(Call.PASS, id("OpenBid2NatC.responder2NT _PASS"))
        );
        return choices;
    }
}




























































































































































































































































































































































































































































































