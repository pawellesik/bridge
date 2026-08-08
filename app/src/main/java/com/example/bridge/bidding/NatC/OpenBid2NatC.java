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
                properties(new Call[]{Bid._1NT, Bid._2NT}, RespondBid2NatC::colorAfterPass),

                shows(Bid._1NT, shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), OpenBidding, id("OpenBid2NatC.responderNegat _1NT")),
                shows(Bid._1S, shape(4, 11), DECENT_PLUS_SUIT,  OpenBidding, id("OpenBid2NatC.responderChangedSuits _1S")),
                shows(Bid._1H, shape(4, 11), DECENT_PLUS_SUIT, OpenBidding, id("OpenBid2NatC.responderChangedSuits _1H")),
                shows(Bid._1S, shape(4, 11), DECENT_PLUS_SUIT, OpenAfterPass, id("OpenBid2NatC.responderChangedSuits _1S")),
                shows(Bid._1H, shape(4, 11), DECENT_PLUS_SUIT, OpenAfterPass, id("OpenBid2NatC.responderChangedSuits _1H")),
                shows(Bid._2S, shape(5, 11), DECENT_PLUS_SUIT, OpeningStrongBidding, id("OpenBid2NatC.responderChangedSuits _1S")),
                shows(Bid._2H, shape(5, 11), DECENT_PLUS_SUIT, OpeningStrongBidding, id("OpenBid2NatC.responderChangedSuits _1H")),
                shows(Bid._2D, shape(5, 11),  OpenAfterPass, id("OpenBid2NatC.responderChangedSuits _2D")),
                shows(Bid._2C, shape(5, 11),  OpenAfterPass, id("OpenBid2NatC.responderChangedSuits _2C")),
                shows(Bid._2D, shape(5, 11),  OpenBidding, id("OpenBid2NatC.responderChangedSuits _2D")),
                shows(Bid._2C, shape(5, 11),  OpenBidding, id("OpenBid2NatC.responderChangedSuits _2C")),
                shows(Bid._3D, shape(5, 11),  OpeningStrongBidding, id("OpenBid2NatC.responderChangedSuits _3D")),
                shows(Bid._3C, shape(5, 11),  OpeningStrongBidding, id("OpenBid2NatC.responderChangedSuits _3C")),
                shows(Bid._2NT, OpeningStrongBidding, id("OpenBid2NatC.responderChangedSuits _2NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderClubJumpMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                partnerBids(RespondBid2NatC::secondBidToGame),
                properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::responderClubJumpMinorChangeMajor),

                shows(Bid._3H, shape(4, 11), highCardPoints(12, 17), id("OpenBid2NatC.responderClubJumpMinor _3H")),
                shows(Bid._3S, shape(4, 11), highCardPoints(12, 17), id("OpenBid2NatC.responderClubJumpMinor _3S")),
                shows(Bid._5D, fit(), highCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMinor _5D")),
                shows(Bid._5C, fit(), highCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMinor _5C"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices.addRules(
                    AcesAsk.initiateConvention(ps),
                    partnerBids(RespondBid2NatC::secondBidToGame),
                    shows(Bid._2H, isJump(1), OpeningStrongBiddingRange, shape(5, 10), id("OpenBid2NatC.responderClub _2H")),
                    shows(Bid._2S, isJump(1), OpeningStrongBiddingRange, shape(5, 10), id("OpenBid2NatC.responderClub _2S")),
                    shows(Bid._3H, isJump(1), OpeningStrongBiddingRange, shape(5, 10), id("OpenBid2NatC.responderClub _3H")),
                    shows(Bid._3S, isJump(1), OpeningStrongBiddingRange, shape(5, 10), id("OpenBid2NatC.responderClub _3S")),
                    shows(Bid._4H, isJump(1), OpeningStrongBiddingRange, shape(6, 10), id("OpenBid2NatC.responderClub _3H")),
                    shows(Bid._4S, isJump(1), OpeningStrongBiddingRange, shape(6, 10), id("OpenBid2NatC.responderClub _3H"))
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

                shows(Bid._4H,  fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajor _4H")),
                shows(Bid._4S,  fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajor _4S")),

                shows(Bid._4H,  hasShortness(1,2), secondSuit(Suit.Hearts, 5), fit(), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _4H")),
                shows(Bid._4S,  hasShortness(1,2), secondSuit(Suit.Spades,5), fit(), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _4S")),

                shows(Bid._2H, fit(), OpenBidding, id("OpenBid2NatC.responderChangedSuits _2H")),
                shows(Bid._2S, fit(), OpenBidding, id("OpenBid2NatC.responderChangedSuits _2S")),

                shows(Bid._1S, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1S")),
                shows(Bid._1H, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1H")),

                shows(Bid._2S, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2NatC.responderChangedSuits _2S")),
                shows(Bid._2H, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2NatC.responderChangedSuits _2H")),

                shows(Bid._2S, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _2S")),
                shows(Bid._2H, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _2H")),

                shows(Bid._4H, IS_NEW_SUIT, shape(5, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _4H")),

                shows(Bid._3S, IS_NEW_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3S")),
                shows(Bid._3H, IS_NEW_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3H")),

                shows(Bid._3S, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3S")),
                shows(Bid._3H, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3H")),

                shows(Bid._3S, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits _3S")),
                shows(Bid._3H, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits _3H")),

                shows(Bid._3S, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits _3S")),
                shows(Bid._3H, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits _3H")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME_INVITE), partnerLastSuitShape(0,2), othersAtLeast(3), id("OpenBid2NatC.responderChangedSuits _3NT")),

                shows(Bid._3S, fit(), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits fit _3S")),
                shows(Bid._3H, fit(), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits fit _3H")),

                shows(Bid._3C, fit(), id("OpenBid2NatC.responderChangedSuits fit _3C")),
                shows(Bid._3D, fit(),  id("OpenBid2NatC.responderChangedSuits fit _3D")),

                shows(Bid._3C, twoSuiter(5), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits twoSuiter _3C")),
                shows(Bid._3D, twoSuiter(5), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits twoSuiter _3D")),

                shows(Bid._3D, shape(7,10), IS_ANY_JUMP,  id("OpenBid2NatC.responderChangedSuits fit _3D")),

                shows(Bid._3D, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits shape _3C")),
                shows(Bid._3C, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits shape _3D"))

                );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaisedMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                propertiesAgreeTrump(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::openerInvitedGame, false),
                shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), fit(), id("OpenBid2NatC.responderRaisedMinor _5D")),
                shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(), id("OpenBid2NatC.responderRaisedMinor _5C")),

                shows(Bid._3H, pairHighCardPoints(PAIR_GAME), shape(4,10), id("OpenBid2NatC.responderRaisedMinor _3H")),
                shows(Bid._3S, pairHighCardPoints(PAIR_GAME), shape(4,10), id("OpenBid2NatC.responderRaisedMinor _3S")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, id("OpenBid2NatC.responderRaisedMinor _3NT")),

                shows(Call.PASS, id("OpenBid2NatC.responderRaisedMinor _PASS"))
        );
        choices.addRules(CompeteNatC::compBids);
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

                shows(Bid._3H, shape(6,10), pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _3H")),
                shows(Bid._3S, shape(6,10), pairPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _3S")),

                shows(Bid._2H, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaisedMajor _2H")),
                shows(Bid._2S, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaisedMajor _2S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME_INVITE), partnerLastSuitShape(0,2), othersAtLeast(3), id("OpenBid2NatC.responderRaisedMajor _3NT"))

        );
        choices.addRules(CompeteNatC::compBids);
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
        choices.addRules(CompeteNatC::compBids);
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
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }
}




























































































































































































































































































































































































































































































