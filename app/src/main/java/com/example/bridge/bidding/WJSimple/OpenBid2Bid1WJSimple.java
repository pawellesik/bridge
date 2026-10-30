package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class OpenBid2Bid1WJSimple extends OpenBid1WJSimple {

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
                shows(Bid._2H, OpeningStrongBidding, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _2H")),
                shows(Bid._2S, OpeningStrongBidding, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _2S")),
                shows(Bid._3C, OpeningStrongBidding, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _3C")),
                shows(Bid._3D, OpeningStrongBidding, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _3D")),
                shows(Bid._2NT, OpeningStrongBidding, isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _2NT")),
                properties(new Call[]{Bid._2NT}, RespondBid2Bid1WJSimple::secondBidNegat2NTStrong),
                partnerBids(RespondBid2Bid1WJSimple::secondBidNegatStrong)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderNegatStandard(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2C, shape(5, 13), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _2C")),
                shows(Bid._2D, shape(5, 13), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _2D")),
                shows(Bid._1S, shape(4, 13), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _1S")),
                shows(Bid._1H, shape(4, 13), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _1H")),
                shows(Bid._1NT, shape(Suit.Spades, 0, 3), shape(Suit.Hearts, 0, 3), ruleShow(1), id("OpenBid2WJSimple.responderNegatStandard _1NT")),
                properties(new Call[]{Bid._1NT}, RecursionWJSimple::recursionFindFitGame),
                partnerBids(RespondBid2Bid1WJSimple::secondBidNegatStandard)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
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
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, OpeningStrongBidding, fit(), isJump(1), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _3H")),
                shows(Bid._3S, OpeningStrongBidding, fit(), isJump(1), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _3S")),

                shows(Bid._2S, OpeningStrongBidding, IS_NEW_SUIT, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _2S")),
                shows(Bid._3H, OpeningStrongBidding, IS_NEW_SUIT, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _3H")),

                shows(Bid._3C, OpeningStrongBidding, noFit(), shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _3C")),
                shows(Bid._3D, OpeningStrongBidding, noFit(), shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _3D")),

                shows(Bid._4H, OpeningStrongBidding, shape(6, 13), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _4H")),
                shows(Bid._4S, OpeningStrongBidding, shape(6, 13), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _4S")),

                shows(Bid._3NT, OpeningStrongBidding, PAIR_BALANCED, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStrong _3NT")),
                partnerBids(RespondBid2Bid1WJSimple::secondBidMajorClubStrong)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdTrumpMajorClubStandard(PositionState ps) {
        //1C ->
        //      1S, 1H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3C, noFit(), shape(7, 13), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _3C")),

                shows(Bid._2H, fit(), partner(isLastBid(Bid._1H)), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _2H")),
                shows(Bid._2S, fit(), partner(isLastBid(Bid._1S)), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _2S")),

                shows(Bid._2C, noFit(), shape(5, 13), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _2C")),
                shows(Bid._2D, noFit(), shape(5, 13), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _2D")),

                shows(Bid._1S, noFit(), shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _1S")),
                shows(Bid._2H, noFit(), shape(4, 13), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _2H")),

                shows(Bid._1NT, PAIR_BALANCED, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMajorClubStandard _3NT")),
                partnerBids(RespondBid2Bid1WJSimple::secondBidMajorClubStandard),
                properties(new Call[]{Bid._1NT}, RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
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
        choices.addRules(AcesAsk.initiateConventionBlok(ps));//bo sa odzywki 4C
        choices.addRules(
                shows(Bid._3S, OpeningStrongBidding, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong _3S")),
                shows(Bid._3H, OpeningStrongBidding, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong _3H")),
                shows(Bid._3D, OpeningStrongBidding, shape(5, 13), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong _3D")),
                shows(Bid._4C, OpeningStrongBidding, shape(5, 13), noFit(), partner(isLastBid(Bid._2D)), isJump(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong _3C")),

                shows(Bid._4D, OpeningStrongBidding, fit(), partner(isLastBid(Bid._2D)), isJump(1), setTrumpColor(Suit.Diamonds), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong fit() _4D")),
                shows(Bid._4C, OpeningStrongBidding, fit(), partner(isLastBid(Bid._2C)), isJump(1), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong fit() _4C")),

                shows(Bid._3NT, OpeningStrongBidding, PAIR_BALANCED, ruleShow(1), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong _3NT")),

                shows(Bid._4C, OpeningStrongBidding, IS_NEW_SUIT, shape(5, 13), noFit(), partner(isLastBid(Bid._2D)), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong noFit() _4C")),
                shows(Bid._4D, OpeningStrongBidding, IS_NEW_SUIT, shape(5, 13), noFit(), partner(isLastBid(Bid._2C)), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStrong noFit() _4D")),
                partnerBids(RespondBid2Bid1WJSimple::secondBidMinorClubStrong),
                propertiesForcingToGame(new Call[]{Bid._4C, Bid._4D}, RespondBid2Bid1WJSimple::secondBidMinorClubForcingStrong, true)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdTrumpMinorClubStandard(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3C, shape(6, 13), noFit(), partner(isLastBid(Bid._2D)), id("OpenBid2WJSimple.responderdTrumpMinorClubStandard _3C")),
                shows(Bid._2S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStandard _2S")),
                shows(Bid._2H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStandard _2H")),
                shows(Bid._3C, shape(5, 13), noFit(), partner(isLastBid(Bid._2D)), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStandard _3C")),
                shows(Bid._2D, shape(5, 13), noFit(), partner(isLastBid(Bid._2C)), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorClubStandard _2D")),
                partnerBids(RespondBid2Bid1WJSimple::secondBidMinorClubStandard)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
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
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4H, OpeningStrongBidding, DECENT_PLUS_SUIT, shape(6, 13), noFit(), IS_ANY_JUMP, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStrong _4H")),
                    shows(Bid._4S, OpeningStrongBidding, DECENT_PLUS_SUIT, shape(6, 13), noFit(), IS_ANY_JUMP, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStrong _4S")),
                    shows(Bid._3NT, OpeningStrongBidding, noFit(), id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStrong _3NT")),
                    properties(new Call[]{Bid._4H, Bid._4S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMinorClubStrong),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._4H, OpeningStrongBidding, DECENT_PLUS_SUIT, shape(6, 13), noFit(), IS_ANY_JUMP, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStrong _4H")),
                    shows(Bid._4S, OpeningStrongBidding, DECENT_PLUS_SUIT, shape(6, 13), noFit(), IS_ANY_JUMP, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStrong _4S")),
                    shows(Bid._3NT, OpeningStrongBidding, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStrong _3NT")),
                    properties(new Call[]{Bid._4H, Bid._4S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMinorClubStrong),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMinorClubStandard(PositionState ps) {
        //1C ->
        //      Bid._3D, Bid._3C ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, OpeningLowBidding, fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard Pass")),
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard Pass")),

                    shows(Bid._3H, shape(4, 13), GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3H")),
                    shows(Bid._3S, shape(4, 13), GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3S")),

                    shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _3NT")),

                    shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), fit(), setTrumpColor(Suit.Diamonds), id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4D")),
                    shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(), setTrumpColor(Suit.Clubs), id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4C")),

                    shows(Bid._3H, shape(4, 13), noFit(), DECENT_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3H")),
                    shows(Bid._3S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3S")),

                    shows(Bid._4C, shape(6, 13), IS_NEW_SUIT, noFit(), IS_REBID, GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4C")),
                    shows(Bid._4D, shape(6, 13), IS_NEW_SUIT, noFit(), IS_REBID, GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4D")),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMinorClubMajorStandard),
                    properties(new Call[]{Bid._4D, Bid._4C}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMinorClubStandard),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)

            );
        } else {
            choices.addRules(
                    shows(Bid._3H, shape(4, 13), GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3H")),
                    shows(Bid._3S, shape(4, 13), GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard GOOD_PLUS_SUIT _3S")),

                    shows(Bid._3NT, PAIR_BALANCED, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _3NT")),

                    shows(Bid._4D, fit(), setTrumpColor(Suit.Diamonds), id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4D")),
                    shows(Bid._4C, fit(), setTrumpColor(Suit.Clubs), id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4C")),

                    shows(Bid._3H, shape(4, 13), noFit(), DECENT_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3H")),
                    shows(Bid._3S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard DECENT_PLUS_SUIT _3S")),

                    shows(Bid._4C, shape(6, 13), noFit(), IS_REBID, GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4C")),
                    shows(Bid._4D, shape(6, 13), noFit(), IS_REBID, GOOD_PLUS_SUIT, id("OpenBid2WJSimple.responderdRaiseTrumpMinorClubStandard _4D")),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMinorClubMajorStandard),
                    propertiesForcingToGame(new Call[]{Bid._4D, Bid._4C}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMinorClubStandard, true),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
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
        choices.addRules(AcesAsk.initiateConventionBlok(ps));//bo sa odzywki 4C
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._3S, OpeningStrongBidding, shape(5, 13), noFit(), partner(isLastBid(Bid._2H)), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubClubStrong 3S")),
                    shows(Bid._4C, OpeningStrongBidding, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubClubStrong 4C")),
                    shows(Bid._4D, OpeningStrongBidding, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubClubStrong 4D")),
                    properties(new Call[]{Bid._3S, Bid._4D, Bid._4C}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorClubStrong),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._3S, OpeningStrongBidding, shape(5, 13), noFit(), partner(isLastBid(Bid._2H)), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubClubStrong 3S")),
                    shows(Bid._4C, OpeningStrongBidding, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubClubStrong 4C")),
                    shows(Bid._4D, OpeningStrongBidding, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubClubStrong 4D")),
                    properties(new Call[]{Bid._3S, Bid._4D, Bid._4C}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorClubStrong),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }

        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdRaiseTrumpMajorClubStandard(PositionState ps) {
        //1C ->
        //     2H, 2S ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, OpeningLowBidding, fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard Pass")),
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard Pass")),

                    shows(Bid._4H, pairHighCardPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Hearts), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard fit 4H")),
                    shows(Bid._4S, pairHighCardPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Spades), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard fit 4S")),

                    shows(Bid._2S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 2S")),

                    shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard 3NT")),

                    shows(Bid._3C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 3C")),
                    shows(Bid._3D, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 3D")),

                    shows(Bid._3H, shape(4, 13), shape(Suit.Spades, 0, 2), noFit(), partner(isLastBid(Bid._2S)), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 3H")),

                    properties(new Call[]{Bid._2S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorClubStandard),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorFitClubStandard),
                    properties(new Call[]{Bid._3C}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorClubClubStandard),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard pass")),

                    shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard fit 3H")),
                    shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard fit 3S")),

                    shows(Bid._2S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 2S")),

                    shows(Bid._3NT, PAIR_BALANCED, id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard 3NT")),

                    shows(Bid._3C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 3C")),
                    shows(Bid._3D, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 3D")),

                    shows(Bid._3H, shape(4, 13), noFit(), partner(isLastBid(Bid._2S)), ruleShow(1), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard noFit 3H")),

                    shows(Bid._2NT, pairHighCardPoints(PAIR_GAME), noFit(), id("OpenBid2WJSimple.responderdRaiseTrumpMajorClubStandard 2NT")),

                    properties(new Call[]{Bid._2S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorClubStandard),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorFitClubStandard),
                    properties(new Call[]{Bid._3C}, RespondBid2Bid1WJSimple::secondBidRaiseTrumpMajorClubClubStandard),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }

        choices.addRules(CompeteWJSimple.compBids(ps));
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
                shows(Bid._3H, OpeningStrongBidding, shape(5, 13), id("OpenBid2WJSimple.responderd1NTClubStrong 3H")),
                shows(Bid._3S, OpeningStrongBidding, shape(5, 13), id("OpenBid2WJSimple.responderd1NTClubStrong 3S")),
                shows(Bid._3C, OpeningStrongBidding, shape(5, 13), id("OpenBid2WJSimple.responderd1NTClubStrong 3S")),
                shows(Bid._3D, OpeningStrongBidding, shape(5, 13), id("OpenBid2WJSimple.responderd1NTClubStrong 3D")),
                properties(new Call[]{Bid._3H, Bid._3S, Bid._3C, Bid._3D}, RecursionWJSimple::recursionFindFitGame, true),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderd1NTClubStandard(PositionState ps) {
        //1C ->
        //     1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3C, shape(7, 13), ruleShow(1), id("OpenBid2WJSimple.responderd1NTClubStandard 3C")),

                shows(Bid._2S, shape(5, 13), id("OpenBid2WJSimple.responderd1NTClubStandard shape(5, 10) 2S")),
                shows(Bid._2H, shape(5, 13), id("OpenBid2WJSimple.responderd1NTClubStandard shape(5, 10) 2H")),

                shows(Bid._2S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd1NTClubStandard shape(4, 10) 2S")),
                shows(Bid._2H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd1NTClubStandard shape(4, 10) 2H")),

                shows(Bid._2C, shape(5, 13), ruleShow(1), id("OpenBid2WJSimple.responderd1NTClubStandard 2C")),
                shows(Bid._2D, shape(5, 13), ruleShow(1), id("OpenBid2WJSimple.responderd1NTClubStandard 2D")),
                properties(new Call[]{Bid._2H, Bid._2S, Bid._2C, Bid._2D}, RecursionWJSimple::recursionFindFitGame, true),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
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
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderd2NTClubStandard(PositionState ps) {
        //1C ->
        //     2NT ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._3C, shape(5, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3C")),
                    shows(Bid._3D, shape(5, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3D")),

                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderd2NTClubStandard pass")),

                    shows(Bid._3H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3H")),
                    shows(Bid._3S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3S")),

                    properties(new Call[]{Bid._3H, Bid._3S, Bid._3D, Bid._3C}, RecursionWJSimple::recursionFindFitGame, true),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._3H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3H")),
                    shows(Bid._3S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3S")),
                    shows(Bid._3C, shape(5, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3C")),
                    shows(Bid._3D, shape(5, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderd2NTClubStandard 3D")),
                    properties(new Call[]{Bid._3H, Bid._3S, Bid._3D, Bid._3C}, RecursionWJSimple::recursionFindFitGame, true),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }


    public static PositionCalls responderdTrumpMinorDiamod2D(PositionState ps) {
        //1D ->
        //     Bid._2D ->
        PositionCalls choices = new PositionCalls(ps);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderdTrumpMinorDiamod pass")),
                    shows(Bid._2H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 2H")),
                    shows(Bid._2S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 2S")),
                    shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._3D)), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 5D")),
                    properties(new Call[]{Bid._2H, Bid._2S}, RespondBid2Bid1WJSimple::secondBidMinorAgreeTrumpDiamods),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderdTrumpMinorDiamod pass")),
                    shows(Bid._2H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 2H")),
                    shows(Bid._2S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 2S")),
                    shows(Bid._5D, OpeningLowBidding, partner(isLastBid(Bid._3D)), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 5D")),
                    shows(Bid._4D, OpeningInviteBidding, partner(isLastBid(Bid._3D)), ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 4D")),
                    properties(new Call[]{Bid._2H, Bid._2S}, RespondBid2Bid1WJSimple::secondBidMinorAgreeTrumpDiamods),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderdTrumpMinorDiamod3D(PositionState ps) {
        //1D ->
        //     Bid._3D ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderdTrumpMinorDiamod pass")),
                    shows(Bid._3H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 3H")),
                    shows(Bid._3S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 3S")),
                    shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._3D)), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 5D")),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidMinorAgreeTrumpDiamods),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderdTrumpMinorDiamod pass")),
                    shows(Bid._3H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 3H")),
                    shows(Bid._3S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 3S")),
                    shows(Bid._5D, OpeningLowBidding, partner(isLastBid(Bid._3D)), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 5D")),
                    shows(Bid._4D, OpeningInviteBidding, partner(isLastBid(Bid._3D)), id("OpenBid2WJSimple.responderdTrumpMinorDiamod 4D")),
                    properties(new Call[]{Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidMinorAgreeTrumpDiamods),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderChangedSuitsDiamond(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3D, shape(7, 13), noFitMajor(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3D")),

                shows(Bid._2H, fit(), partner(isLastBid(Bid._1H)), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond 2H")),
                shows(Bid._2H, shape(4, 13), noFit(), DECENT_PLUS_SUIT, IS_NEW_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _2H")),
                shows(Bid._2S, fit(), partner(isLastBid(Bid._1S)), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond 2S")),
                shows(Bid._1S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, IS_NEW_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond 2S")),
                shows(Bid._2S, shape(4, 13), partner(isLastBid(Bid._2C)), noFit(), DECENT_PLUS_SUIT, IS_NEW_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond 2S")),

                shows(Bid._1S, shape(4, 13), noFit(), partner(isLastBid(Bid._1H)), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _1S")),
                shows(Bid._2D, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _2D")),
                shows(Bid._2C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _2C")),

                shows(Bid._1NT, PAIR_BALANCED, id("OpenBid2WJSimple.responderChangedSuitsDiamond _1NT")),
                shows(Bid._1NT, BALANCED, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _1NT")),
                shows(Bid._1NT, noFit(7), id("OpenBid2WJSimple.responderChangedSuitsDiamond _1NT")),
                partnerBids(RecursionWJSimple::recursionFindFitGame),
                properties(new Call[]{Bid._2H, Bid._1S, Bid._2S, Bid._2C}, RespondBid2Bid1WJSimple::secondBidNoAgreeTrumpDiamods),
                properties(new Call[]{Bid._2D, Bid._3D}, RespondBid2Bid1WJSimple::secondBidRebidDiamods)
        );


        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsMajorDiamond(PositionState ps) {
        //1D ->
        //     Bid._2H, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderChangedSuitsDiamond Pass")),
                    shows(Bid._2S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _2S")),
                    shows(Bid._3D, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3D")),
                    shows(Bid._3C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3C")),
                    shows(Bid._3H, shape(4, 10), noFit(), pairHighCardPoints(PAIR_GAME), OpeningInviteBidding, IS_NEW_SUIT, DECENT_PLUS_SUIT, id("OpenBid2WJSimple.responderChangedSuitsDiamond _3H")),
                    properties(new Call[]{Bid._2S, Bid._3H, Bid._3D, Bid._3C}, RespondBid2Bid1WJSimple::secondBidRaiseNoAgreeTrumpDiamods),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), partner(isLastBid(Bid._2S)), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3S")),
                    shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), partner(isLastBid(Bid._2H)), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3H")),

                    shows(Bid._2S, shape(4, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _2S")),
                    shows(Bid._3S, shape(4, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3S")),
                    shows(Bid._3H, shape(4, 13), noFit(), pairHighCardPoints(PAIR_GAME), OpeningInviteBidding, IS_NEW_SUIT, DECENT_PLUS_SUIT, id("OpenBid2WJSimple.responderChangedSuitsDiamond _3H")),
                    shows(Bid._3D, shape(6, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3D")),
                    shows(Bid._3C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsDiamond _3C")),
                    shows(Bid._2NT, noFit(), id("OpenBid2WJSimple.responderChangedSuitsDiamond _2NT")),
                    partnerBids(RecursionWJSimple::recursionFindFitGame),
                    properties(new Call[]{Bid._2S, Bid._3H, Bid._3D, Bid._3C}, RespondBid2Bid1WJSimple::secondBidRaiseNoAgreeTrumpDiamods)

            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsMinorDiamond(PositionState ps) {
        //1D ->
        //     Bid._3C->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond Pass")),

                    shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(), partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _5C")),
                    shows(Bid._3D, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3D")),
                    shows(Bid._3H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3H")),
                    shows(Bid._3S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3S")),
                    shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3NT")),
                    properties(new Call[]{Bid._3D, Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidRaiseChangeSuitMinorDiamods),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._5C, fit(), partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _5C")),
                    shows(Bid._3D, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3D")),
                    shows(Bid._3H, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3H")),
                    shows(Bid._3S, shape(4, 13), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3S")),
                    shows(Bid._3NT, PAIR_BALANCED, id("OpenBid2WJSimple.responderRaiseChangedSuitsMinorDiamond _3NT")),
                    properties(new Call[]{Bid._3D, Bid._3H, Bid._3S}, RespondBid2Bid1WJSimple::secondBidRaiseChangeSuitMinorDiamods),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderTrumpMajorHeart(PositionState ps) {
        //1H ->
        //     Bid._2H, Bid._3H ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4H, pairPoints(PAIR_MINOR_GAME), fit(), OpenBidding, id("OpenBid2WJSimple.responderRaisedMajorHeart _4H")),
                    shows(Bid._4H, pairPoints(PAIR_GAME), fit(9), OpenBidding, id("OpenBid2WJSimple.responderRaisedMajorHeart _4H")),

                    shows(Call.PASS, OpeningLowBidding, id("OpenBid2WJSimple.responderRaisedMajorHeart OpeningLowBidding _pass")),
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderRaisedMajorHeart PAIR_LOW_GAME _pass")),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)

            );
        } else {
            choices.addRules(
                    shows(Bid._4H, pairPoints(PAIR_GAME), partner(isLastBid(Bid._2H, Bid._3H)), ruleShow(1), id("OpenBid2WJSimple.responderRaisedMajorHeart _4H"))
            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderChangedSuitsHeart(PositionState ps) {
        //Bid._1H ->
        //       Bid._1S, Bid._2C, Bid._2D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(

                shows(Bid._3H, shape(7, 13), noFit(), partner(isLastBid(Bid._1S)), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _3H")),
                shows(Bid._3H, shape(7, 13), not(partner(isLastBid(Bid._1S))), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _3H")),

                shows(Bid._2S, fit(), partner(isLastBid(Bid._1S)), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _2S")),

                shows(Bid._2H, shape(6), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _2H")),

                shows(Bid._2S, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _2S")),

                shows(Bid._2C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _2C")),
                shows(Bid._2D, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _2D")),

                shows(Bid._3C, OpeningInviteBidding, fit(), partner(isLastBid(Bid._2C)), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _3C")),
                shows(Bid._3D, OpeningInviteBidding, fit(), partner(isLastBid(Bid._2D)), setTrumpColor(Suit.Diamonds), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _3D")),

                shows(Bid._1NT, PAIR_BALANCED, id("OpenBid2WJSimple.responderChangedSuitsDiamond _1NT")),
                shows(Bid._1NT, BALANCED, id("OpenBid2WJSimple.responderChangedSuitsDiamond _1NT")),
                shows(Bid._1NT, noFit(7), id("OpenBid2WJSimple.responderChangedSuitsDiamond _1NT")),

                shows(Bid._3C, shape(5, 13), IS_NEW_SUIT, noFit(), partner(isLastBid(Bid._2D)), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _3C noFit")),

                shows(Bid._2NT, PAIR_BALANCED, id("OpenBid2WJSimple.responderChangedSuitsHeart _2NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderChangedSuitsHeart _3NT")),

                shows(Bid._4S, pairPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._2S)), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsHeart _4S")),
                partnerBids(RecursionWJSimple::recursionFindFitGame),
                propertiesForcingToGame(new Call[]{Bid._3H}, RespondBid2Bid1WJSimple::secondBidLong, true),
                properties(new Call[]{Bid._2NT}, RespondBid2Bid1WJSimple::secondBidSearchSuitAfter2NTHeart),
                propertiesAgreeTrump(new Call[]{Bid._3C, Bid._3D}, RespondBid2Bid1WJSimple::secondBidInviteMinor, true)

        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsToSpadeHeart(PositionState ps) {
        //Bid._1H ->
        //        Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart Pass")),

                    shows(Bid._4S, pairHighCardPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Spades), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _4S")),
                    shows(Bid._3H, pairHighCardPoints(PAIR_GAME), shape(6, 13), noFit(), IS_REBID, id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3H")),

                    shows(Bid._3H, shape(6, 13), noFit(7), IS_REBID, id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3H")),

                    shows(Bid._3C, shape(5, 13), noFit(7), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3C")),
                    shows(Bid._3D, shape(5, 13), noFit(7), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3D")),

                    shows(Bid._2NT, noFit(7), IS_NON_JUMP, othersAtLeast(2), id("OpenBid2WJSimple.responderRaiseChangedSuitsToSpadeHeart exit _2NT")),
                    shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3NT")),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._3S, OpeningLowBidding, fit(), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3S")),
                    shows(Bid._4S, OpeningInviteBidding, fit(), setTrumpColor(Suit.Spades), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _4S")),

                    shows(Bid._3H, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3H")),

                    shows(Bid._3C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3C")),
                    shows(Bid._3D, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3D")),
                    shows(Bid._2NT, noFit(), IS_NON_JUMP, othersAtLeast(2), id("OpenBid2WJSimple.responderRaiseChangedSuitsToSpadeHeart nofit _2NT")),

                    partnerBids(RecursionWJSimple::recursionFindFitGame),
                    propertiesAgreeTrump(new Call[]{Bid._3S, Bid._4S}, RespondBid2Bid1WJSimple::secondBidInviteMajorHeart, true),
                    properties(new Call[]{Bid._3H}, RespondBid2Bid1WJSimple::secondBidToGameHeart, false),
                    properties(new Call[]{Bid._3C, Bid._3D}, RespondBid2Bid1WJSimple::secondBidToGameMinorHeart, false)

            );
        }

        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsToMinorHeart(PositionState ps) {
        //Bid._1H ->
        //        Bid._3C, Bid._3D ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart Pass")),

                    shows(Bid._3H, pairHighCardPoints(PAIR_GAME), shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3H")),
                    shows(Bid._3H, shape(6, 13), noFit(), IS_REBID, id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3H")),
                    shows(Bid._3S, shape(5, 13), noFit(), DECENT_PLUS_SUIT, IS_NEW_SUIT, id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3S")),
                    shows(Bid._3S, pairHighCardPoints(PAIR_GAME), shape(4, 13), DECENT_PLUS_SUIT, noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _3H")),

                    shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3NT")),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._3H, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3H")),

                    shows(Bid._3S, shape(5, 13), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3S")),
                    shows(Bid._3S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3S")),

                    shows(Bid._4C, OpeningLowBidding, fit(), setTrumpColor(Suit.Clubs), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _4C")),
                    shows(Bid._4D, OpeningLowBidding, fit(), setTrumpColor(Suit.Diamonds), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _4D")),

                    shows(Bid._4C, shape(5, 13), noFit(), partner(isLastBid(Bid._3D)), ruleShow(1),id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _4C")),
                    shows(Bid._4D, shape(5, 13), noFit(), partner(isLastBid(Bid._3C)), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _4D")),

                    shows(Bid._3D, shape(5, 13), noFit(), partner(isLastBid(Bid._3C)), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3D")),

                    shows(Bid._5C, OpeningInviteBidding, fit(), partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _5C")),
                    shows(Bid._5D, OpeningInviteBidding, fit(), partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _5D")),

                    partnerBids(RecursionWJSimple::recursionFindFitGame),
                    propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, RespondBid2Bid1WJSimple::secondBidInviteMinor, true),
                    propertiesAgreeTrump(new Call[]{Bid._3S, Bid._4S}, RespondBid2Bid1WJSimple::secondBidInviteMajorHeart, true),
                    properties(new Call[]{Bid._3H}, RespondBid2Bid1WJSimple::secondBidToGameHeart, false),
                    properties(new Call[]{Bid._3C, Bid._3D}, RespondBid2Bid1WJSimple::secondBidToGameMinorHeart, false)

            );
        }

        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderRaiseChangedSuitsSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._3C, Bid._3D, Bid._3H ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart Pass")),

                    shows(Bid._4H, pairHighCardPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._3H)), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._4H, pairHighCardPoints(PAIR_GAME), IS_NEW_SUIT, shape(5, 13), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._3S, pairHighCardPoints(PAIR_GAME), shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _3S")),
                    shows(Bid._3S, shape(6, 13), noFit(), IS_REBID, id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _3S")),
                    shows(Bid._3H, pairHighCardPoints(PAIR_GAME), shape(4, 13), DECENT_PLUS_SUIT, noFit(), IS_NEW_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _3H")),
                    shows(Bid._4H, pairHighCardPoints(PAIR_GAME), shape(5, 13), noFit(), IS_NEW_SUIT, id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _3NT")),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)
            );
        } else {
            choices.addRules(
                    shows(Bid._4H, fit(), partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _4H")),
                    shows(Bid._4H, OpeningInviteBidding, shape(5, 13), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _4H")),

                    shows(Bid._3S, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _3S")),
                    shows(Bid._3H, shape(4, 13), DECENT_PLUS_SUIT, partner(isLastBid(Bid._3C, Bid._3D)), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _3H")),

                    shows(Bid._4D, OpeningLowBidding, fit(), partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _4D")),
                    shows(Bid._4C, OpeningLowBidding, fit(), partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _4C")),

                    shows(Bid._3D, shape(5, 13), noFit(), partner(isLastBid(Bid._3C)), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsHeart _3D")),

                    shows(Bid._4C, shape(5, 13), noFit(), partner(isLastBid(Bid._3H, Bid._3D)), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade NOT_PAIR_BALANCED _4C")),
                    shows(Bid._4D, shape(5, 13), noFit(), partner(isLastBid(Bid._3H)), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade NOT_PAIR_BALANCED _4D")),

                    shows(Bid._5C, fit(), OpeningInviteBidding, partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _5C")),
                    shows(Bid._5D, fit(), OpeningInviteBidding, partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), ruleShow(1), id("OpenBid2WJSimple.responderRaiseChangedSuitsSpade _5D")),

                    partnerBids(RecursionWJSimple::recursionFindFitGame),
                    properties(new Call[]{Bid._4D}, RespondBid2Bid1WJSimple::secondBidInviteMinor),
                    properties(new Call[]{Bid._3D}, RespondBid2Bid1WJSimple::secondBidToGameMinorHeart, false)

            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderTrumpMajorSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._2S, Bid._3S ->
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4S, pairPoints(PAIR_MINOR_GAME), fit(), OpenBidding, id("OpenBid2WJSimple.responderRaisedMajorSpade pass hand PAIR_MINOR_GAME _4S")),
                    shows(Bid._4S, pairPoints(PAIR_GAME), fit(9), OpenBidding, id("OpenBid2WJSimple.responderRaisedMajorSpade pass hand PAIR_GAME 9 _4S")),

                    shows(Call.PASS, OpeningLowBidding, id("OpenBid2WJSimple.responderRaisedMajorSpade _pass")),
                    shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), id("OpenBid2WJSimple.responderRaisedMajorSpade _pass")),
                    partnerBids(RecursionWJSimple::recursionFindFitGame)

            );
        } else {
            choices.addRules(
                    shows(Bid._4S, pairPoints(PAIR_GAME), ruleShow(1), id("OpenBid2WJSimple.responderRaisedMajorSpade pass hand PAIR_GAME _4S"))

            );
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls responderChangedSuitsSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._2H, Bid._2C, Bid._2D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(Suit.Hearts), partner(isLastBid(Bid._2H)), id("OpenBid2WJSimple.responderChangedSuitsSpade _pass")),
                shows(Bid._3S, shape(7, 13), noFit(), partner(isLastBid(Bid._2H)), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _3S")),
                shows(Bid._3S, shape(7, 13), not(partner(isLastBid(Bid._2H))), IS_REBID, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _3S")),

                shows(Bid._2S, shape(6), IS_REBID, not(partner(isLastBid(Bid._2H))), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _2S")),
                shows(Bid._2S, shape(6), noFit(), IS_REBID, partner(isLastBid(Bid._2H)), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _2S")),

                shows(Bid._2H, shape(4, 13), noFit(), DECENT_PLUS_SUIT, ruleShow(1), id("OpenBid2WJSimple.responderChangedSuit sSpade _2H")),

                shows(Bid._2C, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _2C")),
                shows(Bid._2D, shape(5, 13), noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _2D")),

                shows(Bid._3C, OpeningInviteBidding, fit(), partner(isLastBid(Bid._2C)), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _3C fit")),
                shows(Bid._3D, OpeningInviteBidding, fit(), partner(isLastBid(Bid._2D)), setTrumpColor(Suit.Diamonds), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _3D fit")),

                shows(Bid._3C, shape(5, 13), IS_NEW_SUIT, noFit(), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _3C noFit")),
                shows(Bid._3D, shape(5, 13), IS_NEW_SUIT, noFit(), not(partner(isLastBid(Bid._2C))), ruleShow(1), id("OpenBid2WJSimple.responderChangedSuitsSpade _3D noFit")),

                shows(Bid._2NT, PAIR_BALANCED, id("OpenBid2WJSimple.responderChangedSuitsSpade _2NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("OpenBid2WJSimple.responderChangedSuitsSpade _3NT")),

                shows(Bid._4H, pairPoints(PAIR_GAME), fit(), id("OpenBid2WJSimple.responderChangedSuitsSpade _4H")),
                partnerBids(RecursionWJSimple::recursionFindFitGame),
                propertiesForcingToGame(new Call[]{Bid._3S}, RespondBid2Bid1WJSimple::secondBidLong, true),
                properties(new Call[]{Bid._2NT}, RespondBid2Bid1WJSimple::secondBidSearchSuitAfter2NTSpade),
                propertiesAgreeTrump(new Call[]{Bid._3C, Bid._3D}, RespondBid2Bid1WJSimple::secondBidInviteMinor, true)

        );
        choices.addRules(CompeteWJSimple.compBids(ps));
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
                shows(Call.PASS),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        return choices;
    }
}
