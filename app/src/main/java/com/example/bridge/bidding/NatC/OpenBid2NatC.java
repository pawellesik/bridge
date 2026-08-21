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
        return choices;
    }

    public static PositionCalls responderNegatStrong(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        PositionCalls choices = new PositionCalls(ps);
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
                properties(new Call[]{Bid._1NT}, RecursionNatC::recursionFindLowFitGame),
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
        return choices;
    }

    public static PositionCalls responderdTrumpMajorClubStrong(PositionState ps) {
        //1C ->
        //      1S, 1H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
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
        return choices;
    }

    public static PositionCalls responderdTrumpMinorClubStrong(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(RespondBid2NatC::secondBidMinorClubStrong),
                shows(Bid._3S, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderdTrumpMinorClubStrong _3S")),
                shows(Bid._3H, isJump(1), OpeningStrongBidding, shape(5, 10), id("OpenBid2NatC.responderdTrumpMinorClubStrong _3H")),
                shows(Bid._4D, fit(), isJump(1), OpeningStrongBidding, setTrumpColor(Suit.Diamonds), partner(isLastBid(Bid._2D)), id("OpenBid2NatC.responderdTrumpMinorClubStrong fit() _4D")),
                shows(Bid._4C, fit(), isJump(1), OpeningStrongBidding, setTrumpColor(Suit.Clubs), partner(isLastBid(Bid._2C)), id("OpenBid2NatC.responderdTrumpMinorClubStrong fit() _4C")),

                shows(Bid._3NT, PAIR_BALANCED, OpeningStrongBidding, id("OpenBid2NatC.responderdTrumpMinorClubStrong _3NT"))
        );
        choices.addRules(
                propertiesForcingToGame(new Call[]{Bid._4C, Bid._4D}, RespondBid2NatC::secondBidMinorClubForcingStrong, true),
                shows(Bid._4C, noFit(), shape(5, 10), OpeningStrongBidding, partner(isLastBid(Bid._2D)), id("OpenBid2NatC.responderdTrumpMinorClubStrong noFit() _4C")),
                shows(Bid._4D, noFit(), shape(5, 10), OpeningStrongBidding, partner(isLastBid(Bid._2C)), id("OpenBid2NatC.responderdTrumpMinorClubStrong noFit() _4D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdTrumpMinorClubStandard(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(RespondBid2NatC::secondBidMinorClubStandard),
                //shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), OpeningLowBidding, id("OpenBid2NatC.responderdTrumpMinorClubStandard Pass")),
                //shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderdTrumpMinorClubStandard Pass")),

                shows(Bid._3C, noFit(), shape(6, 10), id("OpenBid2NatC.responderdTrumpMinorClubStandard _3C")),
                shows(Bid._2S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorClubStandard _2S")),
                shows(Bid._2H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorClubStandard _2H")),
                shows(Bid._3C, shape(5, 10), id("OpenBid2NatC.responderdTrumpMinorClubStandard _3C")),
                shows(Bid._3D, noFit(), shape(5, 10), id("OpenBid2NatC.responderdTrumpMinorClubStandard _3D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClub(PositionState ps) {
        //1C ->
        //      Bid._3D, Bid._3C ->
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderdRaiseTrumpMinorClubStrong(ps);
        } else {
            choices = responderdRaiseTrumpMinorClubStandard(ps);
        }
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClubStrong(PositionState ps) {
        //1C ->
        //      Bid._3D, Bid._3C ->
        PositionCalls choices = new PositionCalls(ps);
        //choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));

        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._4H, Bid._4S}, RespondBid2NatC::secondBidRaiseTrumpMinorClubStrong),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Bid._4H, noFit(), IS_ANY_JUMP, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStrong _4H")),
                    shows(Bid._4S, noFit(), IS_ANY_JUMP, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStrong _4S")),
                    shows(Bid._3NT, noFit(), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStrong _3NT"))
            );
        } else {
            choices.addRules(
                    properties(new Call[]{Bid._4H, Bid._4S}, RespondBid2NatC::secondBidRaiseTrumpMinorClubStrong),
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Bid._4H, noFit(), IS_ANY_JUMP, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStrong _4H")),
                    shows(Bid._4S, noFit(), IS_ANY_JUMP, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStrong _4S")),
                    shows(Bid._3NT, noFit(), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStrong _3NT"))
            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClubStandard(PositionState ps) {
        //1C ->
        //      Bid._3D, Bid._3C ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));

        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::secondBidRaiseTrumpMinorClubMajorStandard),
                    properties(new Call[]{Bid._4D, Bid._4C}, RespondBid2NatC::secondBidRaiseTrumpMinorClubStandard),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), OpeningLowBidding, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard Pass")),

                    shows(Bid._3H, shape(4, 10), GOOD_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3H")),
                    shows(Bid._3S, shape(4, 10), GOOD_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3S")),

                    shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _3NT")),

                    shows(Bid._4D, fit(), id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4D")),
                    shows(Bid._4C, fit(), id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4C")),

                    shows(Bid._3H, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3H")),
                    shows(Bid._3S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3S")),

                    shows(Bid._4C, noFit(), shape(6, 10), GOOD_PLUS_SUIT, IS_REBID, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4C")),
                    shows(Bid._4D, noFit(), shape(6, 10), GOOD_PLUS_SUIT, IS_REBID, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4D"))

            );
        } else {
            choices.addRules(
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::secondBidRaiseTrumpMinorClubMajorStandard),
                    properties(new Call[]{Bid._4D, Bid._4C}, RespondBid2NatC::secondBidRaiseTrumpMinorClubStandard),
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Bid._3H, shape(4, 10), GOOD_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3H")),
                    shows(Bid._3S, shape(4, 10), GOOD_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3S")),

                    shows(Bid._3NT, PAIR_BALANCED, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _3NT")),

                    shows(Bid._4D, fit(), id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4D")),
                    shows(Bid._4C, fit(), id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4C")),

                    shows(Bid._3H, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3H")),
                    shows(Bid._3S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3S")),

                    shows(Bid._4C, noFit(), shape(6, 10), GOOD_PLUS_SUIT, IS_REBID, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4C")),
                    shows(Bid._4D, noFit(), shape(6, 10), GOOD_PLUS_SUIT, IS_REBID, id("OpenBid2NatC.responderdRaiseTrumpMinorClubStandard _4D"))
            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMajorClub(PositionState ps) {
        //1C ->
        //     2H, 2S ->
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderdRaiseTrumpMajorClubStrong(ps);
        } else {
            choices = responderdRaiseTrumpMajorClubStandard(ps);
        }
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMajorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        //1C ->
        //     2H, 2S ->
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._3S, Bid._4D, Bid._4C}, RespondBid2NatC::secondBidRaiseTrumpMajorClubStrong),
                    properties(new Call[]{Bid._3S, Bid._4D, Bid._4C}, RespondBid2NatC::secondBidRaiseTrumpMajorClubStrong),
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Bid._3S, shape(5, 10), noFit(), OpeningStrongBidding, partner(isLastBid(Bid._2H)), id("OpenBid2NatC.responderdRaiseTrumpMajorClubClubStrong 3S")),
                    shows(Bid._4D, shape(5, 10), noFit(), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMajorClubClubStrong 4D")),
                    shows(Bid._4C, shape(5, 10), noFit(), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMajorClubClubStrong 4C"))
            );
        } else {
            choices.addRules(
                    properties(new Call[]{Bid._3S, Bid._4D, Bid._4C}, RespondBid2NatC::secondBidRaiseTrumpMajorClubStrong),
                    properties(new Call[]{Bid._3S, Bid._4D, Bid._4C}, RespondBid2NatC::secondBidRaiseTrumpMajorClubStrong),
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Bid._3S, shape(5, 10), noFit(), OpeningStrongBidding, partner(isLastBid(Bid._2H)), id("OpenBid2NatC.responderdRaiseTrumpMajorClubClubStrong 3S")),
                    shows(Bid._4D, shape(5, 10), noFit(), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMajorClubClubStrong 4D")),
                    shows(Bid._4C, shape(5, 10), noFit(), OpeningStrongBidding, id("OpenBid2NatC.responderdRaiseTrumpMajorClubClubStrong 4C"))
            );
        }

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMajorClubStandard(PositionState ps) {
        //1C ->
        //     2H, 2S ->
        PositionCalls choices = new PositionCalls(ps);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._2S}, RespondBid2NatC::secondBidRaiseTrumpMajorClubStandard),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::secondBidRaiseTrumpMajorFitClubStandard),
                    properties(new Call[]{Bid._3C}, RespondBid2NatC::secondBidRaiseTrumpMajorClubClubStandard),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), OpeningLowBidding, id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard Pass")),

                    shows(Bid._4H, fit(), setTrumpColor(Suit.Hearts), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard fit 3H")),
                    shows(Bid._4S, fit(), setTrumpColor(Suit.Spades), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard fit 3S")),

                    shows(Bid._2S, shape(4, 10), noFit(), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard noFit 2S")),

                    shows(Bid._3NT, noFit(), PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard 3NT")),

                    shows(Bid._3C, shape(5, 10), noFit(), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard noFit 3C")),
                    shows(Bid._3H, shape(4, 10), noFit(), shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._2S)), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard noFit 3H"))
            );
        } else {
            choices.addRules(
                    properties(new Call[]{Bid._2S}, RespondBid2NatC::secondBidRaiseTrumpMajorClubStandard),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2NatC::secondBidRaiseTrumpMajorFitClubStandard),
                    properties(new Call[]{Bid._3C}, RespondBid2NatC::secondBidRaiseTrumpMajorClubClubStandard),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard pass")),

                    shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard fit 3H")),
                    shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard fit 3S")),

                    shows(Bid._2S, shape(4, 10), noFit(), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard noFit 2S")),

                    shows(Bid._3NT, noFit(), PAIR_BALANCED, id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard 3NT")),

                    shows(Bid._3C, shape(5, 10), noFit(), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard noFit 3C")),
                    shows(Bid._3H, shape(4, 10), noFit(), shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._2S)), id("OpenBid2NatC.responderdRaiseTrumpMajorClubStandard noFit 3H"))
            );
        }

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static PositionCalls responderd1NTClub(PositionState ps) {
        //1C ->
        //      1NT ->
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderd1NTClubStrong(ps);
        } else {
            choices = responderd1NTClubStandard(ps);
        }
        return choices;
    }

    public static PositionCalls responderd1NTClubStrong(PositionState ps) {
        //1C ->
        //     1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                properties(new Call[]{Bid._3H, Bid._3S, Bid._3C, Bid._3D}, RecursionNatC::recursionFindFitGame, true),
                partnerBids(NatC::finishBiddingCompBids),
                shows(Bid._3H, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderd1NTClubStrong 3H")),
                shows(Bid._3S, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderd1NTClubStrong 3S")),
                shows(Bid._3H, shape(4, 10), OpeningStrongBidding, GOOD_PLUS_SUIT, id("OpenBid2NatC.responderd1NTClubStrong 3H")),
                shows(Bid._3S, shape(4, 10), OpeningStrongBidding, GOOD_PLUS_SUIT, id("OpenBid2NatC.responderd1NTClubStrong 3S")),
                shows(Bid._3C, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderd1NTClubStrong 3S")),
                shows(Bid._3D, shape(5, 10), OpeningStrongBidding, id("OpenBid2NatC.responderd1NTClubStrong 3D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderd1NTClubStandard(PositionState ps) {
        //1C ->
        //     1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                properties(new Call[]{Bid._2H, Bid._2S, Bid._2C, Bid._2D}, RecursionNatC::recursionFindLowFitGame, true),
                partnerBids(NatC::finishBiddingCompBids),

                shows(Bid._2S, shape(5, 10), id("OpenBid2NatC.responderd1NTClubStandard shape(5, 10) 2S")),
                shows(Bid._2H, shape(5, 10), id("OpenBid2NatC.responderd1NTClubStandard shape(5, 10) 2H")),

                shows(Bid._2S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd1NTClubStandard shape(4, 10) 2S")),
                shows(Bid._2H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd1NTClubStandard shape(4, 10) 2H")),

                shows(Bid._2C, shape(5, 10), id("OpenBid2NatC.responderd1NTClubStandard 2C")),
                shows(Bid._2D, shape(5, 10), id("OpenBid2NatC.responderd1NTClubStandard 2D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderd2NTClub(PositionState ps) {
        //1C ->
        //      2NT ->
        PositionCalls choices;
        if (ps.getPrivateHandSummary() != null && OpeningStrongBidding.conforms(null, ps, ps.getPrivateHandSummary())) {
            choices = responderd2NTClubStrong(ps);
        } else {
            choices = responderd2NTClubStandard(ps);
        }
        return choices;
    }

    public static PositionCalls responderd2NTClubStrong(PositionState ps) {
        //1C ->
        //     2NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
            );
        } else {
            choices.addRules(
            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderd2NTClubStandard(PositionState ps) {
        //1C ->
        //     2NT ->
        PositionCalls choices = new PositionCalls(ps);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._3H, Bid._3S}, RecursionNatC::recursionFindFitGame, true),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderd2NTClubStandard pass")),

                    shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd2NTClubStandard 3H")),
                    shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd2NTClubStandard 3S")),
                    shows(Bid._3C, shape(5, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd2NTClubStandard 3C"))
            );
        } else {
            choices.addRules(
                    properties(new Call[]{Bid._3H, Bid._3S}, RecursionNatC::recursionFindFitGame, true),
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd2NTClubStandard 3H")),
                    shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd2NTClubStandard 3S")),
                    shows(Bid._3C, shape(5, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderd2NTClubStandard 3C"))
            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static PositionCalls responderdTrumpMinorDiamod(PositionState ps) {
        //1D ->
        //     Bid._2D, Bid._3D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._2H, Bid._2S, Bid._3H, Bid._3S}, RespondBid2NatC::secondBidMinorAgreeTrumpDiamods),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderdTrumpMinorDiamod pass")),

                    shows(Bid._2H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 2H")),
                    shows(Bid._2S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 2S")),
                    shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 3H")),
                    shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 3S")),
                    shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._3D)), id("OpenBid2NatC.responderdTrumpMinorDiamod 5D"))

            );
        } else {
            choices.addRules(
                    properties(new Call[]{Bid._2H, Bid._2S, Bid._3H, Bid._3S}, RespondBid2NatC::secondBidMinorAgreeTrumpDiamods),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderdTrumpMinorDiamod pass")),

                    shows(Bid._2H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 2H")),
                    shows(Bid._2S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 2S")),
                    shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 3H")),
                    shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderdTrumpMinorDiamod 3S")),
                    shows(Bid._5D, OpeningLowBidding, partner(isLastBid(Bid._3D)), id("OpenBid2NatC.responderdTrumpMinorDiamod 5D")),
                    shows(Bid._4D, OpeningInviteBidding, partner(isLastBid(Bid._3D)), id("OpenBid2NatC.responderdTrumpMinorDiamod 4D"))
            );
        }

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderChangedSuitsDiamond(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingCompBids),
                shows(Bid._2H, fit(), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderChangedSuitsDiamond 2H")),
                shows(Bid._2S, fit(), setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderChangedSuitsDiamond 2S"))
        );
        choices.addRules(
                partnerBids(NatC::finishBiddingCompBids),
                properties(new Call[]{Bid._2H, Bid._1S, Bid._2S, Bid._2C}, RespondBid2NatC::secondBidNoAgreeTrumpDiamods),
                properties(new Call[]{Bid._2D, Bid._3D}, RespondBid2NatC::secondBidRebidDiamods),
                // properties(new Call[]{Bid._1NT}, RespondBid2NatC::), //TODO
                shows(Bid._2H, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _2H")),
                shows(Bid._1S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _1S")),
                shows(Bid._2S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _2S")),

                shows(Bid._2D, noFit(), IS_REBID, shape(6, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _2D")),
                shows(Bid._3D, noFit(), IS_REBID, shape(6, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _3D")),
                shows(Bid._2C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _2C")),

                shows(Bid._1NT, noFit(), PAIR_BALANCED, id("OpenBid2NatC.responderChangedSuitsDiamond _1NT"))
        );

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsMajorDiamond(PositionState ps) {
        //1D ->
        //     Bid._2H, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._2S, Bid._3H, Bid._3D, Bid._3C}, RespondBid2NatC::secondBidRaiseNoAgreeTrumpDiamods),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderChangedSuitsDiamond Pass")),

                    shows(Bid._2S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsDiamond _2S")),
                    shows(Bid._3D, noFit(), shape(6, 10), IS_REBID, id("OpenBid2NatC.responderChangedSuitsDiamond _3D")),
                    shows(Bid._3C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _3C")),
                    shows(Bid._3H, noFit(), shape(4, 10), DECENT_PLUS_SUIT, pairHighCardPoints(PAIR_GAME), OpeningInviteBidding, id("OpenBid2NatC.responderChangedSuitsDiamond _3H"))
            );
        } else {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), partner(isLastBid(Bid._2S)), id("OpenBid2NatC.responderChangedSuitsDiamond _3S")),
                    shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), partner(isLastBid(Bid._2H)),id("OpenBid2NatC.responderChangedSuitsDiamond _3H"))
            );
            choices.addRules(
                    properties(new Call[]{Bid._2S, Bid._3H, Bid._3D, Bid._3C}, RespondBid2NatC::secondBidRaiseNoAgreeTrumpDiamods),

                    shows(Bid._2S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, partner(isLastBid(Bid._2H)), id("OpenBid2NatC.responderChangedSuitsDiamond _3S")),
                    shows(Bid._3H, noFit(), shape(4, 10), pairHighCardPoints(PAIR_GAME), partner(isLastBid(Bid._2S)), DECENT_PLUS_SUIT, OpeningInviteBidding, id("OpenBid2NatC.responderChangedSuitsDiamond _3H")),
                    shows(Bid._3D, noFit(), shape(6, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _3D")),
                    shows(Bid._3C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsDiamond _3C"))

            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsMinorDiamond(PositionState ps) {
        //1D ->
        //     Bid._3C->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    properties(new Call[]{Bid._3D, Bid._3H, Bid._3S}, RespondBid2NatC::secondBidRaiseChangeSuitMinorDiamods),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond Pass")),

                    shows(Bid._5C, fit(), setTrumpColor(Suit.Clubs), pairHighCardPoints(PAIR_MINOR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _5C")),
                    shows(Bid._3D, noFit(), IS_REBID, shape(6, 10), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3D")),
                    shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3H")),
                    shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3S")),
                    shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3NT"))
            );
        } else {
            choices.addRules(
                    properties(new Call[]{Bid._3D, Bid._3H, Bid._3S}, RespondBid2NatC::secondBidRaiseChangeSuitMinorDiamods),
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Bid._5C, fit(), setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _5C")),
                    shows(Bid._3D, noFit(), IS_REBID, shape(6, 10), id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3D")),
                    shows(Bid._3H, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3H")),
                    shows(Bid._3S, shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3S")),
                    shows(Bid._3NT, PAIR_BALANCED, id("OpenBid2NatC.responderRaiseChangedSuitsMinorDiamond _3NT"))
            );
        }

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderTrumpMajorHeart(PositionState ps) {
        //1H ->
        //     Bid._2H, Bid._3H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Call.PASS, partner(isLastBid(Bid._2H, Bid._3H)), OpeningLowBidding, id("OpenBid2NatC.responderRaisedMajorHeart OpeningLowBidding _pass")),
                    shows(Call.PASS, partner(isLastBid(Bid._2H, Bid._3H)), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaisedMajorHeart PAIR_LOW_GAME _pass")),

                    shows(Bid._4H, pairPoints(PAIR_GAME), partner(isLastBid(Bid._2H, Bid._3H)), id("OpenBid2NatC.responderRaisedMajorHeart pass hand _4H"))
            );
        } else {
            choices.addRules(

            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderChangedSuitsHeart(PositionState ps) {
        //Bid._1H ->
        //       Bid._1S, Bid._2C, Bid._2D, Bid._1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingCompBids),
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

                shows(Bid._1NT, PAIR_BALANCED, partner(noFit()), id("OpenBid2NatC.responderChangedSuitsHeart _2NT")),
                shows(Bid._2NT, PAIR_BALANCED, partner(noFit()), id("OpenBid2NatC.responderChangedSuitsHeart _2NT")),
                shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderChangedSuitsHeart _3NT"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsToSpadeHeart(PositionState ps) {
        //Bid._1H ->
        //        Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsHeart Pass")),

                    shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Spades), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _4S")),
                    shows(Bid._3H, noFit(), shape(6, 10), IS_REBID, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3H")),
                    shows(Bid._3H, noFit(), shape(6, 10), IS_REBID, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3H")),
                    shows(Bid._3S, noFit(), shape(5, 10), DECENT_PLUS_SUIT, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3S")),
                    shows(Bid._3NT, noFit(), PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3NT"))
            );
        } else {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),
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
    public static PositionCalls responderRaiseChangedSuitsToMinorHeart(PositionState ps) {
        //Bid._1H ->
        //        Bid._3C, Bid._3D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsHeart Pass")),

                    shows(Bid._3H, noFit(), shape(6, 10), IS_REBID, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3H")),
                    shows(Bid._3H, noFit(), shape(6, 10), IS_REBID, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3H")),
                    shows(Bid._3S, noFit(), shape(5, 10), DECENT_PLUS_SUIT, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3S")),
                    shows(Bid._3NT, noFit(), PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsHeart _3NT"))
            );
        } else {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),
                    propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, RespondBid2NatC::secondBidInviteMinor, true),
                    propertiesAgreeTrump(new Call[]{Bid._3S, Bid._4S}, RespondBid2NatC::secondBidInviteMajorHeart, true),
                    properties(new Call[]{Bid._3H}, RespondBid2NatC::secondBidToGameHeart, false),
                    properties(new Call[]{Bid._3C, Bid._3D}, RespondBid2NatC::secondBidToGameMinorHeart, false),

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
                    partnerBids(NatC::finishBiddingCompBids),

                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsHeart Pass")),

                    shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._4H, shape(5, 10), pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._3S, noFit(), shape(6, 10), IS_REBID, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _3S")),
                    shows(Bid._3S, noFit(), shape(6, 10), IS_REBID, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _3S")),
                    shows(Bid._3NT, noFit(), PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaiseChangedSuitsSpade _3NT"))
            );
        } else {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),
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
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    partnerBids(NatC::finishBiddingCompBids),
                    shows(Call.PASS, partner(isLastBid(Bid._2S, Bid._3S)), OpeningLowBidding, id("OpenBid2NatC.responderRaisedMajorSpade _pass")),
                    shows(Call.PASS, partner(isLastBid(Bid._2S, Bid._3S)), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderRaisedMajorSpade _pass")),

                    shows(Bid._4S, pairPoints(PAIR_GAME), partner(isLastBid(Bid._2S, Bid._3S)), id("OpenBid2NatC.responderRaisedMajorSpade pass hand PAIR_GAME _4S"))
            );
        } else {
            choices.addRules(
            );
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls responderChangedSuitsSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._2H, Bid._2C, Bid._2D, Bid._1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingCompBids),
                propertiesForcingToGame(new Call[]{Bid._3S}, RespondBid2NatC::secondBidLong, true),
                properties(new Call[]{Bid._2NT}, RespondBid2NatC::secondBidSearchSuitAfter2NTSpade),
                propertiesAgreeTrump(new Call[]{Bid._3C, Bid._3D}, RespondBid2NatC::secondBidInviteMinor, true),

                shows(Call.PASS, fit(), partner(isLastBid(Bid._2H)), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2NatC.responderChangedSuitsSpade _2H")),

                shows(Bid._3S, noFit(), IS_REBID, shape(7, 10), id("OpenBid2NatC.responderChangedSuitsSpade _3S")),

                shows(Bid._2H, fit(), setTrumpColor(Suit.Hearts), setTrumpColor(Suit.Hearts), id("OpenBid2NatC.responderChangedSuitsSpade _2H")),
                shows(Bid._2H, noFit(), isOpeningBid(Bid._1S), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid2NatC.responderChangedSuitsSpade _2H")),

                shows(Bid._2S, shape(6, 10), IS_REBID, id("OpenBid2NatC.responderChangedSuitsSpade _2S")),

                shows(Bid._2C, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsSpade _2C")),
                shows(Bid._2D, noFit(), shape(5, 10), id("OpenBid2NatC.responderChangedSuitsSpade _2D")),

                shows(Bid._3C, fit(), OpeningInviteBidding, setTrumpColor(Suit.Clubs), id("OpenBid2NatC.responderChangedSuitsSpade _3C")),
                shows(Bid._3D, fit(), OpeningInviteBidding, setTrumpColor(Suit.Diamonds), id("OpenBid2NatC.responderChangedSuitsSpade _3D")),

                shows(Bid._1NT, PAIR_BALANCED, partner(noFit()), id("OpenBid2NatC.responderChangedSuitsHeart _1NT")),
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
                partnerBids(NatC::finishBiddingCompBids),
                shows(Bid._4H, FIT_8_PLUS, setTrumpColor(Suit.Hearts), ruleOf17()),
                shows(Bid._4S, FIT_8_PLUS, setTrumpColor(Suit.Spades), ruleOf17()),
                shows(Bid._4S, setTrumpColor(Suit.Spades), fit(10)),
                shows(Bid._4H, setTrumpColor(Suit.Hearts), fit(10)),
                shows(Call.PASS)
        );
        return choices;
    }
}