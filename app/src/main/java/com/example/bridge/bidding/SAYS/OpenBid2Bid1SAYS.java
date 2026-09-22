package com.example.bridge.bidding.SAYS;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class OpenBid2Bid1SAYS extends OpenBid1SAYS {

    public static PositionCalls responderNegat(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2NT, OpeningStrongBidding, isJump(1), ruleShow(1), id("OpenBid2SAYS.responderNegat _2NT")),
                properties(new Call[]{Bid._2NT}, RespondBid2Bid1SAYS::secondBidNegat2NTStrong),
                partnerBids(RespondBid2Bid1SAYS::secondBidNegatStrong)
        );
        choices.addRules(
                shows(Bid._1H, shape(4, 10), id("OpenBid2SAYS.responderNegat _1H")),
                shows(Bid._1S, shape(4, 10), id("OpenBid2SAYS.responderNegat _1S")),
                shows(Bid._2D, shape(6, 10), id("OpenBid2SAYS.responderNegat _2D")),
                shows(Bid._2C, shape(6, 10), id("OpenBid2SAYS.responderNegat _2C")),
                shows(Bid._1NT, id("OpenBid2SAYS.responderNegat _1NT")),
                properties(new Call[]{Bid._1NT}, RecursionSAYS::recursionFindFitGame),
                partnerBids(RespondBid2Bid1SAYS::secondBidNegatStandard)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdTrumpMajorClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, OpeningStrongBidding, fit(), isJump(1), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _3H")),
                shows(Bid._3S, OpeningStrongBidding, fit(), isJump(1), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _3S")),
                shows(Bid._2S, OpeningStrongBidding, IS_NEW_SUIT, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _2S")),
                shows(Bid._3C, OpeningStrongBidding, noFit(), shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _3C")),
                shows(Bid._3D, OpeningStrongBidding, noFit(), shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _3D")),
                shows(Bid._4H, OpeningStrongBidding, shape(6, 10), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _4H")),
                shows(Bid._4S, OpeningStrongBidding, shape(6, 10), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _4S")),
                shows(Bid._3NT, OpeningStrongBidding, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClub _3NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidMajorClubStrong)
        );
        choices.addRules(
                shows(Bid._2H, fit(), partner(isLastBid(Bid._1H)), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClubStandard _2H")),
                shows(Bid._2S, fit(), partner(isLastBid(Bid._1S)), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClubStandard _2S")),
                shows(Bid._2C, noFit(), shape(5, 10), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClubStandard _2C")),
                shows(Bid._2D, noFit(), shape(5, 10), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClubStandard _2D")),
                shows(Bid._1S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClubStandard _1S")),
                shows(Bid._2H, noFit(), shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClubStandard _2H")),
                shows(Bid._1NT, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMajorClubStandard _1NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidMajorClubStandard),
                properties(new Call[]{Bid._1NT}, RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdTrumpMinorClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClub _3S")),
                shows(Bid._3H, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClub _3H")),
                shows(Bid._4D, OpeningStrongBidding, fit(), partner(isLastBid(Bid._2D)), isJump(1), setTrumpColor(Suit.Diamonds), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClub _4D")),
                shows(Bid._4C, OpeningStrongBidding, fit(), partner(isLastBid(Bid._2C)), isJump(1), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClub _4C")),
                shows(Bid._3NT, OpeningStrongBidding, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClub _3NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidMinorClubStrong)
        );
        choices.addRules(
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClubStandard _2S")),
                shows(Bid._3C, shape(6, 10), noFit(), IS_REBID, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClubStandard _3C")),
                shows(Bid._3D, shape(6, 10), noFit(), IS_REBID, ruleShow(1), id("OpenBid2SAYS.responderdTrumpMinorClubStandard _3D")),
                partnerBids(RespondBid2Bid1SAYS::secondBidMinorClubStandard)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4S, OpeningStrongBidding, shape(4, 10), noFit(), ruleShow(1), id("OpenBid2SAYS.responderdRaiseTrumpMinorClubStrong _4S")),
                shows(Bid._5C, OpeningStrongBidding, shape(6, 10), noFit(), ruleShow(1), id("OpenBid2SAYS.responderdRaiseTrumpMinorClubStrong _5C")),
                shows(Bid._5D, OpeningStrongBidding, shape(6, 10), noFit(), ruleShow(1), id("OpenBid2SAYS.responderdRaiseTrumpMinorClubStrong _5D")),
                shows(Bid._5NT, OpeningStrongBidding, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderdRaiseTrumpMinorClubStrong _5NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidRaiseTrumpMinorClubStrong)
        );
        choices.addRules(
                shows(Bid._3H, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderdRaiseTrumpMinorClubStandard _3H")),
                shows(Bid._3S, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderdRaiseTrumpMinorClubStandard _3S")),
                shows(Bid._3NT, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderdRaiseTrumpMinorClubStandard _3NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidRaiseTrumpMinorClubMajorStandard)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderd1NTClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStrong _3H")),
                shows(Bid._3S, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStrong _3S")),
                shows(Bid._3D, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStrong _3D")),
                shows(Bid._3C, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStrong _3C")),
                shows(Bid._3NT, OpeningStrongBidding, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStrong _3NT"))
        );
        choices.addRules(
                shows(Bid._2H, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStandard _2H")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStandard _2S")),
                shows(Bid._2D, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStandard _2D")),
                shows(Bid._2C, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStandard _2C")),
                shows(Bid._2NT, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderd1NTClubStandard _2NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderd2NTClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStrong _3H")),
                shows(Bid._3S, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStrong _3S")),
                shows(Bid._3D, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStrong _3D")),
                shows(Bid._3C, OpeningStrongBidding, shape(5, 10), isJump(1), ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStrong _3C")),
                shows(Bid._3NT, OpeningStrongBidding, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStrong _3NT"))
        );
        choices.addRules(
                shows(Bid._3H, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStandard _3H")),
                shows(Bid._3S, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStandard _3S")),
                shows(Bid._3D, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStandard _3D")),
                shows(Bid._3C, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStandard _3C")),
                shows(Bid._3NT, PAIR_BALANCED, ruleShow(1), id("OpenBid2SAYS.responderd2NTClubStandard _3NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdTrumpMinorDiamod2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2H, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderdTrumpMinorDiamod2D _2H")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderdTrumpMinorDiamod2D _2S")),
                shows(Bid._3D, fit(), setTrumpColor(Suit.Diamonds), id("OpenBid2SAYS.responderdTrumpMinorDiamod2D _3D")),
                shows(Bid._2NT, PAIR_BALANCED, id("OpenBid2SAYS.responderdTrumpMinorDiamod2D _2NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidMinorAgreeTrumpDiamods)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdTrumpMinorDiamod3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderdTrumpMinorDiamod3D _3H")),
                shows(Bid._3S, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderdTrumpMinorDiamod3D _3S")),
                shows(Bid._4D, fit(), setTrumpColor(Suit.Diamonds), id("OpenBid2SAYS.responderdTrumpMinorDiamod3D _4D")),
                shows(Bid._3NT, PAIR_BALANCED, id("OpenBid2SAYS.responderdTrumpMinorDiamod3D _3NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidMinorAgreeTrumpDiamods)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderChangedSuitsDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2H, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsDiamond _2H")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsDiamond _2S")),
                shows(Bid._2C, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsDiamond _2C")),
                shows(Bid._2D, shape(5, 10), IS_REBID, id("OpenBid2SAYS.responderChangedSuitsDiamond _2D")),
                shows(Bid._1NT, PAIR_BALANCED, id("OpenBid2SAYS.responderChangedSuitsDiamond _1NT")),
                partnerBids(RespondBid2Bid1SAYS::secondBidNoAgreeTrumpDiamods)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderTrumpMajorHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), id("OpenBid2SAYS.responderTrumpMajorHeart _3H")),
                shows(Bid._4H, fit(), OpeningInviteBidding, setTrumpColor(Suit.Hearts), id("OpenBid2SAYS.responderTrumpMajorHeart _4H")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderTrumpMajorHeart _2S")),
                shows(Bid._3C, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderTrumpMajorHeart _3C")),
                shows(Bid._3D, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderTrumpMajorHeart _3D")),
                shows(Bid._2NT, PAIR_BALANCED, id("OpenBid2SAYS.responderTrumpMajorHeart _2NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderChangedSuitsHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._1S, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsHeart _1S")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsHeart _2S")),
                shows(Bid._2D, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsHeart _2D")),
                shows(Bid._2C, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsHeart _2C")),
                shows(Bid._2H, shape(6, 10), IS_REBID, id("OpenBid2SAYS.responderChangedSuitsHeart _2H")),
                shows(Bid._1NT, PAIR_BALANCED, id("OpenBid2SAYS.responderChangedSuitsHeart _1NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderTrumpMajorSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), id("OpenBid2SAYS.responderTrumpMajorSpade _3S")),
                shows(Bid._4S, fit(), OpeningInviteBidding, setTrumpColor(Suit.Spades), id("OpenBid2SAYS.responderTrumpMajorSpade _4S")),
                shows(Bid._3H, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderTrumpMajorSpade _3H")),
                shows(Bid._3C, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderTrumpMajorSpade _3C")),
                shows(Bid._3D, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderTrumpMajorSpade _3D")),
                shows(Bid._2NT, PAIR_BALANCED, id("OpenBid2SAYS.responderTrumpMajorSpade _2NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls responderChangedSuitsSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2H, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsSpade _2H")),
                shows(Bid._2D, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsSpade _2D")),
                shows(Bid._2C, shape(4, 10), noFit(), IS_NEW_SUIT, id("OpenBid2SAYS.responderChangedSuitsSpade _2C")),
                shows(Bid._2S, shape(6, 10), IS_REBID, id("OpenBid2SAYS.responderChangedSuitsSpade _2S")),
                shows(Bid._1NT, PAIR_BALANCED, id("OpenBid2SAYS.responderChangedSuitsSpade _1NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls weakRespond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }
}
