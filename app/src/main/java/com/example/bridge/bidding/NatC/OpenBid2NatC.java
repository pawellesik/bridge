package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

public class OpenBid2NatC extends OpenNatC {

    public static PositionCalls responderNegat(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderNegatStrong(ps);
        } else {
            choices = responderNegatStandard(ps);
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderNegatStrong(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                properties(new Call[]{Bid._2NT}, RespondBid2NatC::secondBidNegat2NTStrong),
                partnerBids(RespondBid2NatC::secondBidNegatStrong),
                shows(Bid._2H, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _2H")),
                shows(Bid._2S, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _2S")),
                shows(Bid._3C, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _3C")),
                shows(Bid._3D, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _3D")),
                shows(Bid._2NT, isJump(1), OpeningStrongBidding, id("OpenBid2NatC.responderNegatStandard _2NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderNegatStandard(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                properties(new Call[]{Bid._1NT}, RespondBid2NatC::secondBidNegat1NTStandard),
                partnerBids(RespondBid2NatC::secondBidNegatStandard),
                shows(Bid._1H, shape(6, 10), id("OpenBid2NatC.responderNegatStandard _1H")),
                shows(Bid._1S, shape(6, 10), id("OpenBid2NatC.responderNegatStandard _1S")),
                shows(Bid._2D, shape(6, 10), id("OpenBid2NatC.responderNegatStandard _2D")),
                shows(Bid._2C, shape(6, 10), id("OpenBid2NatC.responderNegatStandard _2C")),
                shows(Bid._1H, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _1H")),
                shows(Bid._1S, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _1S")),
                shows(Bid._2D, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _2D")),
                shows(Bid._2C, shape(5, 10), id("OpenBid2NatC.responderNegatStandard _2C")),
                shows(Bid._1NT, id("OpenBid2NatC.responderNegatStandard _1NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMajorClub(PositionState ps) {
        //1C ->
        //      1S, 1H ->
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderdTrumpMajorClubStrong(ps);
        } else {
            choices = responderdTrumpMajorClubStandard(ps);
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMajorClubStrong(PositionState ps) {
        //1C ->
        //      1S, 1H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                AcesAsk.initiateConvention(ps),
                partnerBids(RespondBid2NatC::secondBidMajorClubStrong),
                shows(Bid._3H, fit(), isJump(1), OpeningStrongBidding, setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderdTrumpMajorClubStrong _3H")),
                shows(Bid._3S, fit(), isJump(1), OpeningStrongBidding, setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderdTrumpMajorClubStrong _3S")),

                shows(Bid._2S, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderdTrumpMajorClubStrong _2S")),
                shows(Bid._3C, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderdTrumpMajorClubStrong _3C")),
                shows(Bid._3D, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderdTrumpMajorClubStrong _3D")),

                shows(Bid._4H, OpeningStrongBidding, shape(6, 10), id("OpenBid2NatC.responderdTrumpMajorClubStrong _4H")),
                shows(Bid._4S, OpeningStrongBidding, shape(6, 10), id("OpenBid2NatC.responderdTrumpMajorClubStrong _4S")),

                shows(Bid._3NT, PAIR_BALANCED, OpeningStrongBidding, id("OpenBid2NatC.responderdTrumpMajorClubStrong _3NT"))
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMajorClubStandard(PositionState ps) {
        //1C ->
        //      1S, 1H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(RespondBid2NatC::secondBidMajorClubStandard),
                properties(new Call[]{Bid._1NT}, RecursionNatC::recursionFindLowFitGame),

                shows(Bid._2H, fit(), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderdTrumpMajorClubStandard _2H")),
                shows(Bid._2S, fit(), setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderdTrumpMajorClubStandard _2S")),

                shows(Bid._2C, shape(5, 10), id("OpenBid2NatC.responderdTrumpMajorClubStandard _2C")),
                shows(Bid._1S, DECENT_PLUS_SUIT, shape(4, 10), id("OpenBid2NatC.responderdTrumpMajorClubStandard _1S")),
                shows(Bid._2S, DECENT_PLUS_SUIT, shape(4, 10), id("OpenBid2NatC.responderdTrumpMajorClubStandard _2S")),
                shows(Bid._2H, DECENT_PLUS_SUIT, shape(4, 10), id("OpenBid2NatC.responderdTrumpMajorClubStandard _2H")),

                shows(Bid._1NT, PAIR_BALANCED, id("OpenBid2NatC.responderdTrumpMajorClubStandard _3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMinorClub(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderdTrumpMinorClubStrong(ps);
        } else {
            choices = responderdTrumpMinorClubStandard(ps);
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMinorClubStrong(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                AcesAsk.initiateConvention(ps),
                partnerBids(RespondBid2NatC::secondBidMinorClubStrong),
                shows(Bid._3S, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderdTrumpMinorClubStrong _3S")),
                shows(Bid._3H, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderdTrumpMinorClubStrong _3H")),
                shows(Bid._4D, fit(), isJump(1), OpeningStrongBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderdTrumpMinorClubStrong _4D")),
                shows(Bid._4C, fit(), isJump(1), OpeningStrongBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderdTrumpMinorClubStrong _4C")),

                shows(Bid._3NT, PAIR_BALANCED, OpeningStrongBidding, id("OpenBid2NatC.responderdTrumpMinorClubStrong _3NT"))
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMinorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //todo
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClub(PositionState ps) {
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderdRaiseTrumpMinorClubStrong(ps);
        } else {
            choices = responderdRaiseTrumpMinorClubStandard(ps);
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMajorClub(PositionState ps) {
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderdRaiseTrumpMajorClubClubStrong(ps);
        } else {
            choices = responderdRaiseTrumpMajorClubStandard(ps);
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMajorClubClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMajorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMinorDiamod(PositionState ps) {
        //1D ->
        //     Bid._2D, Bid._3D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                properties(new Call[]{Bid._2H, Bid._2S, Bid._3H, Bid._3S}, RespondBid2NatC::secondBidMinorAgreeTrumpDiamods),
                shows(Bid._2H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 2H")),
                shows(Bid._2S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 2S")),
                shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 3H")),
                shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 3S")),
                shows(Bid._5D, OpeningLowBidding, partner(IS_ANY_JUMP), id("OpenBid2NatC.responderdTrumpMinorDiamod 5D")),
                shows(Bid._4D, OpeningInviteBidding, partner(IS_ANY_JUMP), id("OpenBid2NatC.responderdTrumpMinorDiamod 4D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderChangedSuitsDiamond(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                shows(Bid._2H, fit(), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderChangedSuitsDiamond 2H")),
                shows(Bid._2S, fit(), setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderChangedSuitsDiamond 2S"))
        );
        choices.addRules(
                properties(new Call[]{Bid._2H, Bid._1S, Bid._2S, Bid._2C}, RespondBid2NatC::secondBidNoAgreeTrumpDiamods),
                properties(new Call[]{Bid._2D, Bid._3D}, RespondBid2NatC::secondBidRebidDiamods),
                // properties(new Call[]{Bid._1NT}, RespondBid2NatC::), //TODO
                shows(Bid._2H, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _2H")),
                shows(Bid._1S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _1S")),
                shows(Bid._2S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _2S")),

                shows(Bid._2D, noFit(), IS_REBID, shape(6, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _2D")),
                shows(Bid._3D, noFit(), IS_REBID, shape(6, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _3D")),
                shows(Bid._2C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _2C")),

                shows(Bid._1NT, noFit(), BALANCED, id("OpenBid2NatC.responderChangedSuitsDiamond _1NT"))
        );

        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("OpenBid2NatC.responderChangedSuitsDiamond Pass"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsMajorDiamond(PositionState ps) {
        //1D ->
        //     Bid._2H, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderChangedSuitsDiamond _3S")),
                shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderChangedSuitsDiamond _3H"))
        );
        choices.addRules(
                properties(new Call[]{Bid._2S, Bid._3H, Bid._3C, Bid._3C}, RespondBid2NatC::secondBidRaiseNoAgreeTrumpDiamods),

                shows(Bid._2S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _3S")),
                shows(Bid._3H, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _3H")),
                shows(Bid._3D, noFit(), shape(6, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _3D")),
                shows(Bid._3C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _3C")),
                shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuitsDiamond _3S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsMinorDiamond(PositionState ps) {
        //1D ->
        //     Bid._3C->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                properties(new Call[]{Bid._3D, Bid._3H, Bid._3S}, RespondBid2NatC::secondBidRaiseChangeSuitMinorDiamods),

                shows(Bid._5C, fit(), setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _5C")),
                shows(Bid._3D, IS_REBID, shape(6, 10), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3D")),
                shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3H")),
                shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3S")),
                shows(Bid._3NT, PAIR_BALANCED, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responder1NTDiamond(PositionState ps) {
        //1D ->
        //     Bid._1NT->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                properties(new Call[]{Bid._2D, Bid._2C, Bid._2H, Bid._2S}, RespondBid2NatC::secondBid1NTDiamods),
                shows(Bid._2D, IS_REBID, shape(6, 10), id("OpenBid2NatC.responder1NTDiamond _2D")),
                shows(Bid._2C, shape(5, 10), id("OpenBid2NatC.responder1NTDiamond _2C")),
                shows(Bid._2H, shape(4, 10), DECENT_PLUS_SUIT, OpeningInviteBidding, id("OpenBid2NatC.responder1NTDiamond _2H")),
                shows(Bid._2S, shape(4, 10), DECENT_PLUS_SUIT, OpeningInviteBidding, id("OpenBid2NatC.responder1NTDiamond _2S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responder2NTDiamond(PositionState ps) {
        //1D ->
        //     Bid._2NT->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                properties(new Call[]{Bid._3D, Bid._3C, Bid._3H, Bid._3S}, RespondBid2NatC::secondBid2NTDiamods),
                shows(Bid._3D, IS_REBID, shape(6, 10), id("OpenBid2NatC.responder1NTDiamond _3D")),
                shows(Bid._3C, shape(5, 10), id("OpenBid2NatC.responder1NTDiamond _3C")),
                shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, OpeningInviteBidding, id("OpenBid2NatC.responder1NTDiamond _3H")),
                shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, OpeningInviteBidding, id("OpenBid2NatC.responder1NTDiamond _3S"))
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static PositionCalls responderdTrumpMajorHeart(PositionState ps) {
        //1H ->
        //     Bid._2H, Bid._3H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                //shows(Bid._4H, fit(), CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajorHeart _4H")),
                shows(Bid._3H, fit(), CONTRACT_IS_AGREED_STRAIN, OpeningInviteBidding, setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderRaisedMajorHeart _3H"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderChangedSuitsHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._1S, Bid._2C, Bid._2D, Bid._1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                propertiesForcingToGame(new Call[]{Bid._3H}, RespondBid2NatC::secondBidLong, true),
                properties(new Call[]{Bid._2NT}, RespondBid2NatC::secondBidSearchSuitAfter2NTHeart),
                propertiesAgreeTrump(new Call[]{Bid._3C, Bid._3D}, RespondBid2NatC::secondBidInviteMinor, true),

                shows(Bid._3H, noFit(), IS_REBID, shape(7, 10), id("OpenBid2NatC.responderChangedSuitsHeart _3H")),

                shows(Bid._2S, fit(), setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderChangedSuitsHeart _2S")),
                shows(Bid._2S, noFit(), isOpeningBid(Bid._1H), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsHeart _2S")),

                shows(Bid._2H, shape(6, 10), IS_REBID, id("OpenBid2NatC.responderChangedSuitsHeart _2H")),

                shows(Bid._2C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsHeart _2C")),
                shows(Bid._2D, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsHeart _2D")),

                shows(Bid._3C, fit(), OpeningInviteBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderChangedSuitsHeart _3C")),
                shows(Bid._3D, fit(), OpeningInviteBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderChangedSuitsHeart _3D")),

                shows(Bid._2NT, PAIR_BALANCED, partner(noFit()), id("OpenBid2NatC.responderChangedSuitsHeart _2NT")),
                shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuitsHeart _3NT"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._3C, Bid._3D, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4S, fit(), OpeningInviteBidding, setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _4S")),
                    shows(Bid._3H, noFit(), shape(6, 10), OpeningInviteBidding, id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3H"))
            );
        } else {
            choices.addRules(
                    propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, RespondBid2NatC::secondBidInviteMinor, true),
                    propertiesAgreeTrump(new Call[]{Bid._3S, Bid._4S}, RespondBid2NatC::secondBidInviteMajorHeart, true),
                    properties(new Call[]{Bid._3H}, RespondBid2NatC::secondBidToGameHeart, false),
                    properties(new Call[]{Bid._3C, Bid._3D}, RespondBid2NatC::secondBidToGameMinorHeart, false),

                    shows(Bid._3S, fit(), OpeningLowBidding, setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3S")),
                    shows(Bid._4S, fit(), OpeningInviteBidding, setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _4S")),
                    shows(Bid._3H, noFit(), IS_REBID, shape(6, 10), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3H")),

                    shows(Bid._3S, shape(5, 10), OpeningLowBidding, id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3S")),

                    shows(Bid._3C, noFit(), shape(5, 10), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3C")),
                    shows(Bid._3D, noFit(), shape(5, 10), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3D")),

                    shows(Bid._4C, fit(), OpeningLowBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _4C")),
                    shows(Bid._4D, fit(), OpeningLowBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _4D")),

                    shows(Bid._5C, fit(), OpeningInviteBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _5C")),
                    shows(Bid._5D, fit(), OpeningInviteBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _5D"))

            );
        }

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._3C, Bid._3D, Bid._3H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4H, fit(), OpeningInviteBidding, setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._4H, shape(5, 10), OpeningInviteBidding, id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._3S, noFit(), shape(6, 10), OpeningInviteBidding, id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3S"))
            );
        } else {
            choices.addRules(
                    propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, RespondBid2NatC::secondBidInviteMinor, true),
                    properties(new Call[]{Bid._3C, Bid._3D}, RespondBid2NatC::secondBidToGameMinorHeart, false),

                    shows(Bid._4H, fit(), OpeningLowBidding, setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4H")),

                    shows(Bid._4H, shape(5, 10), OpeningInviteBidding, id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4H")),

                    shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderRaiseChangedSuitsSpade _3H")),
                    shows(Bid._3S, IS_REBID, shape(6, 10), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _3S")),

                    shows(Bid._3D, noFit(), shape(5, 10), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3D")),

                    shows(Bid._4C, fit(), OpeningLowBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4C")),
                    shows(Bid._4D, fit(), OpeningLowBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4D")),

                    shows(Bid._5C, fit(), OpeningInviteBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _5C")),
                    shows(Bid._5D, fit(), OpeningInviteBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _5D"))

            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderTrumpMajorSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._2S, Bid._3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                //shows(Bid._4S, fit(), CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajorHeart _4S")),
                shows(Bid._3S, fit(), CONTRACT_IS_AGREED_STRAIN, OpeningInviteBidding, setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderRaisedMajorHeart _3S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderChangedSuitsSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._2H, Bid._2C, Bid._2D, Bid._1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                propertiesForcingToGame(new Call[]{Bid._3S}, RespondBid2NatC::secondBidLong, true),
                properties(new Call[]{Bid._2NT}, RespondBid2NatC::secondBidSearchSuitAfter2NTSpade),
                propertiesAgreeTrump(new Call[]{Bid._3C, Bid._3D}, RespondBid2NatC::secondBidInviteMinor, true),

                shows(Bid._3S, noFit(), IS_REBID, shape(7, 10), id("OpenBid2NatC.responderChangedSuitsSpade _3S")),

                shows(Bid._2H, fit(), setTrumpColor(Suit.Hearts), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderChangedSuitsSpade _2H")),
                shows(Bid._2H, noFit(), isOpeningBid(Bid._1S), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsSpade _2H")),

                shows(Bid._2S, shape(6, 10), IS_REBID, id("OpenBid2NatC.responderChangedSuitsSpade _2S")),

                shows(Bid._2C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsSpade _2C")),
                shows(Bid._2D, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsSpade _2D")),

                shows(Bid._3C, fit(), OpeningInviteBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderChangedSuitsSpade _3C")),
                shows(Bid._3D, fit(), OpeningInviteBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderChangedSuitsSpade _3D")),

                shows(Bid._2NT, PAIR_BALANCED, partner(noFit()), id("OpenBid2NatC.responderChangedSuitsSpade _2NT")),
                shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuitsSpade _3NT"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls weakRespond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                shows(Bid._4H, FIT_8_PLUS, setTrumpColor(Suit.Hearts), ruleOf17()),
                shows(Bid._4S, FIT_8_PLUS, setTrumpColor(Suit.Spades), ruleOf17()),
                shows(Bid._4S, setTrumpColor(Suit.Spades), fit(10)),
                shows(Bid._4H, setTrumpColor(Suit.Hearts), fit(10)),
                shows(Call.PASS)
        );
        return choices;
    }



 /*

 public static PositionCalls responderClubJumpMajor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(RespondBid2NatC::secondBidToGame),

                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMajor fit() _4H")),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMajor fit() _4S")),

                shows(Bid._3H, shape(4, 11), DECENT_PLUS_SUIT, highCardPoints(12, 17), id("OpenBid2NatC.responderClubJumpMajor _3H")),
                shows(Bid._2S, shape(4, 11), DECENT_PLUS_SUIT, highCardPoints(12, 17), id("OpenBid2NatC.responderClubJumpMajor _2S")),
                shows(Bid._3S, shape(4, 11), DECENT_PLUS_SUIT, highCardPoints(12, 17), id("OpenBid2NatC.responderClubJumpMajor _3S")),

                shows(Bid._4H, GOOD_PLUS_SUIT, shape(6, 11), hasShortness(1, 1), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMajor GOOD_PLUS_SUIT _4H")),
                shows(Bid._4S, GOOD_PLUS_SUIT, shape(6, 11), hasShortness(1, 1), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMajor GOOD_PLUS_SUIT _4S")),

                shows(Bid._3NT, isJump(1), pairHighCardPoints(PAIR_GAME), othersAtLeast(2), id("OpenBid2NatC.responderClub _3NT"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderClubJumpMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(RespondBid2NatC::secondBidToGame),
                properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::responderClubJumpMinorChangeMajor),

                shows(Bid._3H, shape(4, 11), DECENT_PLUS_SUIT, highCardPoints(12, 17), id("OpenBid2NatC.responderClubJumpMinor _3H")),
                shows(Bid._3S, shape(4, 11), DECENT_PLUS_SUIT, highCardPoints(12, 17), id("OpenBid2NatC.responderClubJumpMinor _3S")),

                shows(Bid._4H, GOOD_PLUS_SUIT, shape(6, 11), hasShortness(1, 1), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMinor _3H")),
                shows(Bid._4S, GOOD_PLUS_SUIT, shape(6, 11), hasShortness(1, 1), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMinor _3S")),

                shows(Bid._5D, fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMinor _5D")),
                shows(Bid._5C, fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderClubJumpMinor _5C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }
    public static PositionCalls responderChangedSuits(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));

        choices.addRules(
                properties(new Call[]{Bid._3S, Bid._3H}, RespondBid2NatC::secondBidToGame),
                partnerBids(RespondBid2NatC::secondBid),

                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajor _4H")),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajor _4S")),

                shows(Bid._4H, hasShortness(1, 2), secondSuit(Suit.Hearts, 5), fit(), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _4H")),
                shows(Bid._4S, hasShortness(1, 2), secondSuit(Suit.Spades, 5), fit(), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderRaisedMajor _4S")),

                shows(Bid._2H, fit(), IS_NON_JUMP, OpenBidding, id("OpenBid2NatC.responderChangedSuits OpenBidding _2H")),
                shows(Bid._2S, fit(), IS_NON_JUMP, OpenBidding, id("OpenBid2NatC.responderChangedSuits OpenBidding _2S")),

                shows(Bid._1S, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1S")),
                shows(Bid._1H, IS_NEW_SUIT, DECENT_PLUS_SUIT, shape(4, 11), id("OpenBid2NatC.responderChangedSuits _1H")),

                shows(Bid._2S, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2NatC.responderChangedSuits IS_REBID _2S")),
                shows(Bid._2H, IS_REBID, shape(6, 11), OpenBidding, id("OpenBid2NatC.responderChangedSuits IS_REBID _2H")),

                shows(Bid._2S, IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, shape(4, 11), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _2S")),
                shows(Bid._2H, IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, shape(4, 11), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _2H")),

                shows(Bid._3S, IS_NEW_SUIT, shape(4, 11), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3S")),
                shows(Bid._3H, IS_NEW_SUIT, shape(4, 11), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3H")),

                shows(Bid._3S, IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, shape(4, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3S")),
                shows(Bid._3H, IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, shape(4, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits IS_NEW_SUIT _3H")),

                shows(Bid._3S, IS_REBID, shape(6, 11), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits _3S")),
                shows(Bid._3H, IS_REBID, shape(6, 11), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits _3H")),

                shows(Bid._3S, IS_REBID, shape(6, 11), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits _3S")),
                shows(Bid._3H, IS_REBID, shape(6, 11), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuits _3H")),

                shows(Bid._3S, fit(), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits fit _3S")),
                shows(Bid._3H, fit(), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits fit _3H")),

                shows(Bid._3C, fit(), pairHighCardPoints(PAIR_GAME_INVITE), IS_NON_JUMP, id("OpenBid2NatC.responderChangedSuits fit _3C")),
                shows(Bid._3D, fit(), pairHighCardPoints(PAIR_GAME_INVITE), IS_NON_JUMP, id("OpenBid2NatC.responderChangedSuits fit _3D")),

                shows(Bid._3C, twoSuiter(5), shape(5, 10), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits twoSuiter _3C")),
                shows(Bid._3D, twoSuiter(5), shape(5, 10), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits twoSuiter _3D")),

                shows(Bid._3D, shape(7, 10), IS_ANY_JUMP, id("OpenBid2NatC.responderChangedSuits IS_ANY_JUMP fit _3D")),

                shows(Bid._3D, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits shape _3C")),
                shows(Bid._3C, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("OpenBid2NatC.responderChangedSuits shape _3D")),

                shows(Bid._2C, DECENT_PLUS_SUIT, shape(5, 10), id("OpenBid2NatC.responderChangedSuits _2C")),
                shows(Bid._2D, DECENT_PLUS_SUIT, shape(5, 10), id("OpenBid2NatC.responderChangedSuits _2D")),

                shows(Bid._3C, shape(5, 10), partner(isLastBid(Bid._2NT)), id("OpenBid2NatC.responderChangedSuits twoSuiter _3C")),

                shows(Bid._3C, noFit(), DECENT_PLUS_SUIT, shape(6, 10), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderChangedSuits _3C")),
                shows(Bid._3D, noFit(), DECENT_PLUS_SUIT, shape(6, 10), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderChangedSuits _3D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }



        public static PositionCalls responderNegat(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                partnerBids(OpenBid2NatC::responderChangedSuits),
                properties(new Call[]{Bid._1NT, Bid._2NT}, RespondBid2NatC::colorAfterPass),

                shows(Bid._1NT, shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), OpenBidding, id("OpenBid2NatC.responderNegat _1NT")),
                shows(Bid._1S, shape(4, 11), DECENT_PLUS_SUIT, OpenBidding, id("OpenBid2NatC.responderNegat _1S")),
                shows(Bid._1H, shape(4, 11), DECENT_PLUS_SUIT, OpenBidding, id("OpenBid2NatC.responderNegat _1H")),
                shows(Bid._1S, shape(4, 11), DECENT_PLUS_SUIT, OpenBiddingThirtSeat, id("OpenBid2NatC.responderNegat _1S")),
                shows(Bid._1H, shape(4, 11), DECENT_PLUS_SUIT, OpenBiddingThirtSeat, id("OpenBid2NatC.responderNegat _1H")),
                shows(Bid._2S, shape(5, 11), DECENT_PLUS_SUIT, OpeningStrongBidding, id("OpenBid2NatC.responderNegat _1S")),
                shows(Bid._2H, shape(5, 11), DECENT_PLUS_SUIT, OpeningStrongBidding, id("OpenBid2NatC.responderNegat _1H")),
                shows(Bid._2D, shape(5, 11), OpenBiddingThirtSeat, id("OpenBid2NatC.responderNegat _2D")),
                shows(Bid._2C, shape(5, 11), OpenBiddingThirtSeat, id("OpenBid2NatC.responderNegat _2C")),
                shows(Bid._2D, shape(5, 11), OpenBidding, id("OpenBid2NatC.responderNegat _2D")),
                shows(Bid._2C, shape(5, 11), OpenBidding, id("OpenBid2NatC.responderNegat _2C")),
                shows(Bid._3D, shape(5, 11), OpeningStrongBidding, id("OpenBid2NatC.responderNegat _3D")),
                shows(Bid._3C, shape(5, 11), OpeningStrongBidding, id("OpenBid2NatC.responderNegat _3C")),
                shows(Bid._2NT, OpeningStrongBidding, id("OpenBid2NatC.responderNegat _2NT"))
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
    }*/
}