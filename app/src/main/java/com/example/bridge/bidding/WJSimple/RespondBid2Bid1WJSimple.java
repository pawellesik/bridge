package com.example.bridge.bidding.WJSimple;

import static com.example.bridge.bidding.WJSimple.OpenBid1WJSimple.GameAfterNegat;
import static com.example.bridge.bidding.WJSimple.OpenBid1WJSimple.OpeningInviteBidding;
import static com.example.bridge.bidding.WJSimple.OpenBid1WJSimple.OpeningLowBidding;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RespondBid2Bid1WJSimple extends RespondBid1WJSimple {

    public static PositionCalls secondBidNegat2NTStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(6, 13), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(6, 13), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3S")),
                shows(Bid._3D, shape(6, 13), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3D")),
                shows(Bid._3C, shape(6, 13), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3C")),
                shows(Bid._3H, shape(5, 13), ruleShow(1), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(5, 13), ruleShow(1), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3S")),
                shows(Bid._3D, shape(5, 13), ruleShow(1), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3D")),
                shows(Bid._3C, shape(5, 13), ruleShow(1), id("RespondBid2WJSimple.secondBidNegat2NTStrong _3C")),
                partnerBids(OpenBid3Bid1WJSimple::thirdBidNegat2NTStrong)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidNegatStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBidNegatStrong Pass")),

                shows(Bid._4S, pairPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Spades), id("RespondBid2WJSimple.secondBidNegatStrong _4S")),
                shows(Bid._4H, pairPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Hearts), id("RespondBid2WJSimple.secondBidNegatStrong _4H")),
                shows(Bid._2S, shape(5, 13), IS_NEW_SUIT, noFit(), ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStrong _2S")),

                shows(Bid._3H, shape(6, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(), id("RespondBid2WJSimple.secondBidNegatStrong _3H")),
                shows(Bid._3H, shape(5, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(), GameAfterNegat, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStrong _3H")),
                shows(Bid._3H, shape(5, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(7), id("RespondBid2WJSimple.secondBidNegatStrong _3H")),

                shows(Bid._3S, shape(6, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(), id("RespondBid2WJSimple.secondBidNegatStrong _3S")),
                shows(Bid._3S, shape(5, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(), GameAfterNegat, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStrong _3S")),
                shows(Bid._3S, shape(5, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(7), id("RespondBid2WJSimple.secondBidNegatStrong _3S")),

                shows(Bid._3C, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStrong _3C")),
                shows(Bid._3D, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStrong _3D")),

                shows(Bid._3C, shape(5, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(7), id("RespondBid2WJSimple.secondBidNegatStrong _3C")),
                shows(Bid._3D, shape(5, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(7), id("RespondBid2WJSimple.secondBidNegatStrong _3D")),
                shows(Bid._4C, shape(5, 13), IS_NEW_SUIT, IS_NON_JUMP, noFit(7), IS_NON_JUMP, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStrong _4C")),

                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidNegatStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBidNegatStandard Pass")),
                shows(Bid._1S, shape(6, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, id("RespondBid2WJSimple.secondBidNegatStandard _1S")),
                shows(Bid._2H, shape(6, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, id("RespondBid2WJSimple.secondBidNegatStandard _2H")),
                shows(Bid._2S, shape(6, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, id("RespondBid2WJSimple.secondBidNegatStandard _2S")),
                shows(Bid._2D, shape(6, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, id("RespondBid2WJSimple.secondBidNegatStandard _2D")),
                shows(Bid._2C, shape(6, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, id("RespondBid2WJSimple.secondBidNegatStandard _2C")),
                shows(Bid._1S, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStandard _1S")),
                shows(Bid._2H, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStandard _2H")),
                shows(Bid._2S, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStandard _2S")),
                shows(Bid._2D, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStandard _2D")),
                shows(Bid._2C, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNegatStandard _2C")),
                shows(Bid._1NT, id("RespondBid2WJSimple.secondBidNegatStandard _1NT")),
                shows(Bid._2NT, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("RespondBid2WJSimple.secondBidNegatStandard _2NT")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMajorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMajorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBidMajorClubStandard Pass")),
                shows(Bid._2S, noFit(), shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _2S")),
                shows(Bid._2H, noFit(), shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _2H")),
                shows(Bid._2H, noFit(), shape(4, 13), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _2H")),
                shows(Bid._3H, noFit(), shape(4, 13), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _3H")),
                shows(Bid._3H, noFit(), shape(4, 13), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _3H")),
                shows(Bid._2S, noFit(), shape(4, 13), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _2S")),
                shows(Bid._2C, noFit(), shape(5, 13), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _2C")),
                shows(Bid._2D, noFit(), shape(5, 13), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _2D")),
                shows(Bid._3C, noFit(), shape(5, 13), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _3C")),
                shows(Bid._3D, noFit(), shape(5, 13), IS_NON_JUMP, IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMajorClubStandard _3D")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4S, pairHighCardPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._3S)), setTrumpColor(Suit.Spades), id("RespondBid2WJSimple.secondBidMinorClubStrong _4S")),
                shows(Bid._4H, pairHighCardPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), id("RespondBid2WJSimple.secondBidMinorClubStrong _4H")),

                shows(Bid._3H, shape(4, 13), noFit(), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _3H")),
                shows(Bid._3S, shape(4, 13), noFit(), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _3S")),
                shows(Bid._4C, shape(6, 13), noFit(), IS_REBID, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _4C")),
                shows(Bid._4D, shape(6, 13), noFit(), IS_REBID, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _4D")),

                shows(Bid._4C, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _4C")),
                shows(Bid._4D, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _4D")),
                shows(Bid._4H, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _4H")),
                shows(Bid._4S, shape(5, 13), noFit(), IS_NON_JUMP, IS_NEW_SUIT, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStrong _4S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondBid2WJSimple.secondBidMinorClubStrong _3NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, id("RespondBid2WJSimple.secondBidMinorClubStrong _3NT")),

                partnerBids(RecursionWJSimple::recursionFindFitGame),
                properties(new Call[]{Bid._3S}, OpenBid3Bid1WJSimple::thirdBidToGameMinorClubStrong)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorClubForcingStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4D, shape(6, 13), noFit(), partner(isLastBid(Bid._4C)), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubForcingStrong _4D")),
                partnerBids(OpenBid3Bid1WJSimple::thirdBidMinorClubForcingStrong)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2S, shape(4, 13), noFit(), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStandard _2S")),
                shows(Bid._3C, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStandard _3C")),
                shows(Bid._3D, shape(5, 13), noFit(), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStandard _3D")),
                shows(Bid._3D, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidMinorClubStandard _3D")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4S, shape(4, 13), noFit(), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubStrong _4S")),
                shows(Bid._5C, shape(6, 13), noFit(), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubStrong _5C")),
                shows(Bid._5D, shape(6, 13), noFit(), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubStrong _5D")),
                shows(Bid._5NT, PAIR_BALANCED, id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubStrong _5NT")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubMajorStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4H, fit(), setTrumpColor(Suit.Hearts), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, fit(), setTrumpColor(Suit.Spades), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _4S")),
                shows(Bid._4D, shape(6, 13), noFit(), IS_REBID, id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _4D")),
                shows(Bid._3S, shape(4, 13), noFit(), IS_NEW_SUIT, id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _3S")),
                shows(Bid._3NT, PAIR_BALANCED, id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _3NT")),

                shows(Bid._4H, OpeningInviteBidding, fit(7), partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, OpeningInviteBidding, fit(7), partner(isLastBid(Bid._3S)), setTrumpColor(Suit.Spades), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _4S")),

                shows(Bid._4H, OpeningLowBidding, fit(7), partner(isLastBid(Bid._3H)), EXCELLENT_PLUS_SUIT, setTrumpColor(Suit.Hearts), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, OpeningLowBidding, fit(7), partner(isLastBid(Bid._3S)), EXCELLENT_PLUS_SUIT, setTrumpColor(Suit.Spades), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _4S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _3NT")),

                shows(Call.PASS, OpeningLowBidding, fit(7, Suit.Spades), partner(isLastBid(Bid._3S)), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _pass")),
                shows(Call.PASS, OpeningLowBidding, fit(7, Suit.Hearts), partner(isLastBid(Bid._3H)), id("RespondBid2WJSimple.secondBidRaiseTrumpMinorClubMajorStandard _pass")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4H, shape(6, 13), noFit(), id("RespondBid2WJSimple.secondBidRaiseTrumpMajorClubClubStrong 4H")),
                shows(Bid._3NT, othersAtLeast(2), id("RespondBid2WJSimple.secondBidRaiseTrumpMajorClubClubStrong 3NT")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBidRaiseTrumpMajorClubStandard pass")),
                shows(Bid._3H, shape(6, 13), shape(Suit.Spades, 0, 2), noFit(), partner(isLastBid(Bid._2S)), IS_REBID, id("RespondBid2WJSimple.responderdRaiseTrumpMajorClubStandard 3H")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Spades, 0, 2), noFit(), partner(isLastBid(Bid._2S)), id("RespondBid2WJSimple.responderdRaiseTrumpMajorClubStandard 3C")),
                shows(Bid._3D, shape(5, 13), shape(Suit.Spades, 0, 2), noFit(), partner(isLastBid(Bid._2S)), id("RespondBid2WJSimple.responderdRaiseTrumpMajorClubStandard 3C")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorFitClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(6, 13), noFit(), IS_REBID, id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _3H")),
                shows(Bid._3S, shape(6, 13), noFit(), IS_REBID, id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _3S")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }


    public static PositionCalls secondBidNoAgreeTrumpDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods Pass")),
                shows(Bid._2H, shape(6, 13), noFit(), IS_REBID, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _2H")),
                shows(Bid._2S, shape(6, 13), noFit(), IS_REBID, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _2S")),
                shows(Bid._2H, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _2H")),
                shows(Bid._2S, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _2S")),
                shows(Bid._2C, shape(5, 13), noFit(7), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _3C")),
                shows(Bid._3C, shape(5, 13), IS_NON_JUMP, noFit(7), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _3C")),
                shows(Bid._3C, shape(6, 13), noFit(7), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _3C")),
                partnerBids(RecursionWJSimple::recursionFindFitGame),
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3Bid1WJSimple::thirdBidToGameDiamond)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseChangeSuitMinorDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(4, 13), noFit(), DECENT_PLUS_SUIT, id("RespondBid2WJSimple.secondBidRaiseChangeSuitMinorDiamods _3H")),
                shows(Bid._3S, shape(4, 13), noFit(), DECENT_PLUS_SUIT, id("RespondBid2WJSimple.secondBidRaiseChangeSuitMinorDiamods _3S")),
                shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(), setTrumpColor(Suit.Clubs), id("RespondBid2WJSimple.secondBidRaiseChangeSuitMinorDiamods _5C")),
                shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), fit(), partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), id("RespondBid2WJSimple.secondBidRaiseChangeSuitMinorDiamods _5D")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)

        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseNoAgreeTrumpDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), id("RespondBid2WJSimple.secondBidRaiseNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), id("RespondBid2WJSimple.secondBidRaiseNoAgreeTrumpDiamods _3H")),
                shows(Bid._3S, shape(6, 13), noFit(), IS_REBID, id("RespondBid2WJSimple.secondBidRaiseNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, shape(6, 13), noFit(), IS_REBID, id("RespondBid2WJSimple.secondBidRaiseNoAgreeTrumpDiamods _3H")),
                shows(Bid._3C, fit(), setTrumpColor(Suit.Clubs), id("RecursionWJSimple.recursionFindFitGame  _3C")),
                shows(Bid._3D, fit(), setTrumpColor(Suit.Diamonds), id("RecursionWJSimple.recursionFindFitGame  _3D")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)

        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorAgreeTrumpDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4H, pairHighCardPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Hearts), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _4H")),
                shows(Bid._4S, pairHighCardPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Spades), id("RespondBid2WJSimple.secondBidNoAgreeTrumpDiamods _4S")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondBid2WJSimple.secondBidMinorAgreeTrumpDiamods 3D")),
                shows(Bid._5D, pairHighCardPoints(PAIR_GAME), noFit(), id("RespondBid2WJSimple.secondBidMinorAgreeTrumpDiamods 5D")),

                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBidMinorAgreeTrumpDiamods Pass")),

                shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), noFit(), id("RespondBid2WJSimple.secondBidMinorAgreeTrumpDiamods 3D")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)

        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRebidDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBidRebidDiamods Pass")),
                shows(Bid._2H, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidRebidDiamods _2H")),
                shows(Bid._2S, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidRebidDiamods _2S")),
                shows(Bid._3C, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidRebidDiamods _3C")),
                shows(Bid._2H, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidRebidDiamods _2H")),
                shows(Bid._2S, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidRebidDiamods _2S")),
                shows(Bid._3C, shape(5, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidRebidDiamods _3C")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBid1NTDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2WJSimple.secondBid1NTDiamods Pass")),
                shows(Bid._2H, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2WJSimple.secondBid1NTDiamods _2H")),
                shows(Bid._2S, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2WJSimple.secondBid1NTDiamods _2S")),
                partnerBids(RecursionWJSimple::recursionFindFitGame),
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3Bid1WJSimple::thirdBidToGame1NTDiamond)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBid2NTDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2WJSimple.secondBid2NTDiamods _2H")),
                shows(Bid._3S, shape(4, 13), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2WJSimple.secondBid2NTDiamods _2S")),
                shows(Bid._3NT, PAIR_BALANCED, id("RespondBid2WJSimple.secondBid2NTDiamods _3NT")),
                shows(Bid._5D, fit(), partner(isLastBid(Bid._3D)), id("RespondBid2WJSimple.secondBid2NTDiamods _5D")),
                partnerBids(RecursionWJSimple::recursionFindFitGame),
                properties(new Call[]{Bid._3H, Bid._3S}, OpenBid3Bid1WJSimple::thirdBidToGame2NTDiamond)

        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidLong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidSearchSuitAfter2NTHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTHeart _3S")),
                shows(Bid._3D, shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTHeart _3D")),
                shows(Bid._3C, shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTHeart _3C")),

                shows(Bid._3D, shape(5, 13), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTHeart _3D")),
                shows(Bid._3C, shape(5, 13), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTHeart _3C")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidSearchSuitAfter2NTSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTSpade _3H")),
                shows(Bid._3D, shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTSpade _3D")),
                shows(Bid._3C, shape(6, 13), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTSpade _3C")),

                shows(Bid._3D, shape(5, 13), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTSpade _3D")),
                shows(Bid._3C, shape(5, 13), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidSearchSuitAfter2NTSpade _3C")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidInviteMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidInviteMajorHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidToGameHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3S, shape(4, 13), noFit(), IS_NEW_SUIT, ruleShow(1), id("RespondBid2WJSimple.secondBidToGameHeart  not(myLastBid(Bid._2S)) _3S")),
                shows(Bid._3S, shape(6, 13), noFit(), IS_REBID, ruleShow(1), id("RespondBid2WJSimple.secondBidToGameHeart _3S")),
                partnerBids(RecursionWJSimple::recursionFindFitGame),
                properties(new Call[]{Bid._3S}, OpenBid3Bid1WJSimple::thirdBidToGameHeart, false)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidToGameMinorHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(6, 13), noFit(), IS_REBID, id("RespondBid2WJSimple.secondBidToGameMinorHeart _3H")),
                shows(Bid._3S, shape(6, 13), noFit(), IS_REBID, id("RespondBid2WJSimple.secondBidToGameMinorHeart _3S")),
                partnerBids(RecursionWJSimple::recursionFindFitGame)
        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }
}
