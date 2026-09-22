package com.example.bridge.bidding.SAYS;

import static com.example.bridge.bidding.SAYS.OpenBid1SAYS.OpeningInviteBidding;
import static com.example.bridge.bidding.SAYS.OpenBid1SAYS.OpeningLowBidding;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RespondBid2Bid1SAYS extends RespondBid1SAYS {

    public static PositionCalls secondBidNegat2NTStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(6, 10), id("RespondBid2SAYS.secondBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(6, 10), id("RespondBid2SAYS.secondBidNegat2NTStrong _3S")),
                shows(Bid._3D, shape(6, 10), id("RespondBid2SAYS.secondBidNegat2NTStrong _3D")),
                shows(Bid._3C, shape(6, 10), id("RespondBid2SAYS.secondBidNegat2NTStrong _3C")),
                shows(Bid._3H, shape(5, 10), ruleShow(1), id("RespondBid2SAYS.secondBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(5, 10), ruleShow(1), id("RespondBid2SAYS.secondBidNegat2NTStrong _3S")),
                shows(Bid._3D, shape(5, 10), ruleShow(1), id("RespondBid2SAYS.secondBidNegat2NTStrong _3D")),
                shows(Bid._3C, shape(5, 10), ruleShow(1), id("RespondBid2SAYS.secondBidNegat2NTStrong _3C")),
                partnerBids(OpenBid3Bid1SAYS::thirdBidNegat2NTStrong)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidNegatStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.secondBidNegatStrong Pass")),

                shows(Bid._4S, pairPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Spades), ruleShow(1), id("RespondBid2SAYS.secondBidNegatStrong _4S")),
                shows(Bid._4H, pairPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Hearts), ruleShow(1), id("RespondBid2SAYS.secondBidNegatStrong _4H")),
                shows(Bid._2S, shape(5, 10), noFit(), ruleShow(1), id("RespondBid2SAYS.secondBidNegatStrong _2S")),
                shows(Bid._3H, shape(6, 10), noFit(), id("RespondBid2SAYS.secondBidNegatStrong _3H")),
                shows(Bid._3H, shape(5, 10), shape(ps.getPartner().getBid().getSuit(), 0, 1), noFit(), id("RespondBid2SAYS.secondBidNegatStrong _3H")),
                shows(Bid._3C, shape(5, 10), noFit(), DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidNegatStrong _3C")),
                shows(Bid._3D, shape(5, 10), noFit(), DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidNegatStrong _3D")),
                shows(Bid._3C, shape(5, 10), shape(ps.getPartner().getBid().getSuit(), 0, 1), noFit(), id("RespondBid2SAYS.secondBidNegatStrong _3C")),
                shows(Bid._3D, shape(5, 10), shape(ps.getPartner().getBid().getSuit(), 0, 1), noFit(), id("RespondBid2SAYS.secondBidNegatStrong _3D")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidNegatStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.secondBidNegatStandard Pass")),
                shows(Bid._1S, shape(6, 10), id("RespondBid2SAYS.secondBidNegatStandard _1S")),
                shows(Bid._2H, shape(6, 10), id("RespondBid2SAYS.secondBidNegatStandard _2H")),
                shows(Bid._2S, shape(6, 10), id("RespondBid2SAYS.secondBidNegatStandard _2S")),
                shows(Bid._2D, shape(6, 10), id("RespondBid2SAYS.secondBidNegatStandard _2D")),
                shows(Bid._2C, shape(6, 10), id("RespondBid2SAYS.secondBidNegatStandard _2C")),
                shows(Bid._1S, shape(5, 10), id("RespondBid2SAYS.secondBidNegatStandard _1S")),
                shows(Bid._2H, shape(5, 10), id("RespondBid2SAYS.secondBidNegatStandard _2H")),
                shows(Bid._2S, shape(5, 10), id("RespondBid2SAYS.secondBidNegatStandard _2S")),
                shows(Bid._2D, shape(5, 10), id("RespondBid2SAYS.secondBidNegatStandard _2D")),
                shows(Bid._2C, shape(5, 10), id("RespondBid2SAYS.secondBidNegatStandard _2C")),
                shows(Bid._1NT, id("RespondBid2SAYS.secondBidNegatStandard _1NT")),
                shows(Bid._2NT, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("RespondBid2SAYS.secondBidNegatStandard _2NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMajorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMajorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2S, noFit(), shape(6, 10), IS_REBID, ruleShow(1), id("RespondBid2SAYS.secondBidMajorClubStandard _2S")),
                shows(Bid._2H, noFit(), shape(6, 10), IS_REBID, ruleShow(1), id("RespondBid2SAYS.secondBidMajorClubStandard _2H")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4S, pairHighCardPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._3S)), ruleShow(1), setTrumpColor(Suit.Spades), id("RespondBid2SAYS.secondBidMinorClubStrong _4S")),
                shows(Bid._4H, pairHighCardPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._3H)), ruleShow(1), setTrumpColor(Suit.Hearts), id("RespondBid2SAYS.secondBidMinorClubStrong _4H")),

                shows(Bid._3S, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("RespondBid2SAYS.secondBidMinorClubStrong _3S")),
                shows(Bid._4C, shape(6, 10), noFit(), IS_REBID, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2SAYS.secondBidMinorClubStrong _4C")),
                shows(Bid._4D, shape(6, 10), noFit(), IS_REBID, PARTNER_DID_NOT_SIGN_OFF, ruleShow(1), id("RespondBid2SAYS.secondBidMinorClubStrong _4D")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, ruleShow(1), id("RespondBid2SAYS.secondBidMinorClubStrong _3NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, ruleShow(1), id("RespondBid2SAYS.secondBidMinorClubStrong _3NT")),

                partnerBids(RecursionSAYS::recursionFindFitGame),
                properties(new Call[]{Bid._3S}, OpenBid3Bid1SAYS::thirdBidToGameMinorClubStrong)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorClubForcingStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4D, shape(6, 10), noFit(), partner(isLastBid(Bid._4C)), IS_REBID, ruleShow(1), id("RespondBid2SAYS.secondBidMinorClubForcingStrong _4D")),
                partnerBids(OpenBid3Bid1SAYS::thirdBidMinorClubForcingStrong)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, id("RespondBid2SAYS.secondBidMinorClubStandard _2S")),
                shows(Bid._3C, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidMinorClubStandard _3C")),
                shows(Bid._3D, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidMinorClubStandard _3D")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4S, shape(4, 10), noFit(), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubStrong _4S")),
                shows(Bid._5C, shape(6, 10), noFit(), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubStrong _5C")),
                shows(Bid._5D, shape(6, 10), noFit(), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubStrong _5D")),
                shows(Bid._5NT, PAIR_BALANCED, id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubStrong _5NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubMajorStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4H, fit(), setTrumpColor(Suit.Hearts), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, fit(), setTrumpColor(Suit.Spades), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _4S")),
                shows(Bid._4D, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _4D")),
                shows(Bid._3S, shape(4, 10), noFit(), IS_NEW_SUIT, id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _3S")),
                shows(Bid._3NT, PAIR_BALANCED, id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _3NT")),

                shows(Bid._4H, OpeningInviteBidding, fit(7), partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, OpeningInviteBidding, fit(7), partner(isLastBid(Bid._3S)), setTrumpColor(Suit.Spades), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _4S")),

                shows(Bid._4H, OpeningLowBidding, fit(7), partner(isLastBid(Bid._3H)), EXCELLENT_PLUS_SUIT, setTrumpColor(Suit.Hearts), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, OpeningLowBidding, fit(7), partner(isLastBid(Bid._3S)), EXCELLENT_PLUS_SUIT, setTrumpColor(Suit.Spades), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _4S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _3NT")),

                shows(Call.PASS, OpeningLowBidding, fit(7), partner(isLastBid(Bid._3S)), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _pass")),
                shows(Call.PASS, OpeningLowBidding, fit(7), partner(isLastBid(Bid._3H)), id("RespondBid2SAYS.secondBidRaiseTrumpMinorClubMajorStandard _pass")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4H, shape(6, 10), noFit(), id("RespondBid2SAYS.secondBidRaiseTrumpMajorClubClubStrong 4H")),
                shows(Bid._3NT, othersAtLeast(2), id("RespondBid2SAYS.secondBidRaiseTrumpMajorClubClubStrong 3NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.secondBidRaiseTrumpMajorClubStandard pass")),
                shows(Bid._3H, shape(6, 10), shape(Suit.Spades, 0, 2), noFit(), partner(isLastBid(Bid._2S)), IS_REBID, id("RespondBid2SAYS.responderdRaiseTrumpMajorClubStandard 3H")),
                shows(Bid._3C, shape(5, 10), shape(Suit.Spades, 0, 2), noFit(), partner(isLastBid(Bid._2S)), id("RespondBid2SAYS.responderdRaiseTrumpMajorClubStandard 3C")),
                shows(Bid._3D, shape(5, 10), shape(Suit.Spades, 0, 2), noFit(), partner(isLastBid(Bid._2S)), id("RespondBid2SAYS.responderdRaiseTrumpMajorClubStandard 3C")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorFitClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubClubStandard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _3H")),
                shows(Bid._3S, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _3S")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidNoAgreeTrumpDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods Pass")),

                shows(Bid._2H, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _2H")),
                shows(Bid._2H, shape(6, 10), noFit(), IS_REBID, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _2H")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _2S")),
                shows(Bid._2S, shape(6, 10), noFit(), IS_REBID, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _2S")),
                partnerBids(RecursionSAYS::recursionFindFitGame),
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3Bid1SAYS::thirdBidToGameDiamond)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseChangeSuitMinorDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(4, 10), noFit(), DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidRaiseChangeSuitMinorDiamods _3H")),
                shows(Bid._3S, shape(4, 10), noFit(), DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidRaiseChangeSuitMinorDiamods _3S")),
                shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(), setTrumpColor(Suit.Clubs), id("RespondBid2SAYS.secondBidRaiseChangeSuitMinorDiamods _5C")),
                shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), fit(), partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), id("RespondBid2SAYS.secondBidRaiseChangeSuitMinorDiamods _5D")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRaiseNoAgreeTrumpDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), id("RespondBid2SAYS.secondBidRaiseNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), id("RespondBid2SAYS.secondBidRaiseNoAgreeTrumpDiamods _3H")),
                shows(Bid._3S, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidRaiseNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidRaiseNoAgreeTrumpDiamods _3H")),
                shows(Bid._3C, fit(), setTrumpColor(Suit.Clubs), id("RecursionSAYS.recursionFindFitGame _3C")),
                shows(Bid._3D, fit(), setTrumpColor(Suit.Diamonds), id("RecursionSAYS.recursionFindFitGame _3D")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidMinorAgreeTrumpDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._4H, pairHighCardPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Hearts), id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _4H")),
                shows(Bid._4S, pairHighCardPoints(PAIR_GAME), fit(), setTrumpColor(Suit.Spades), id("RespondBid2SAYS.secondBidNoAgreeTrumpDiamods _4S")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondBid2SAYS.secondBidMinorAgreeTrumpDiamods 3D")),
                shows(Bid._5D, pairHighCardPoints(PAIR_GAME), noFit(), id("RespondBid2SAYS.secondBidMinorAgreeTrumpDiamods 5D")),

                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(), id("RespondBid2SAYS.secondBidMinorAgreeTrumpDiamods Pass")),

                shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), noFit(), id("RespondBid2SAYS.secondBidMinorAgreeTrumpDiamods 3D")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidRebidDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.secondBidRebidDiamods Pass")),
                shows(Bid._2S, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidRebidDiamods _2S")),
                shows(Bid._2H, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidRebidDiamods _2H")),
                shows(Bid._3C, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidRebidDiamods _3C")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidRebidDiamods _2S")),
                shows(Bid._2H, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidRebidDiamods _2H")),
                shows(Bid._3C, shape(5, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBidRebidDiamods _3C")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBid1NTDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.secondBid1NTDiamods Pass")),
                shows(Bid._2H, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBid1NTDiamods _2H")),
                shows(Bid._2S, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBid1NTDiamods _2S")),
                partnerBids(RecursionSAYS::recursionFindFitGame),
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3Bid1SAYS::thirdBidToGame1NTDiamond)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBid2NTDiamods(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBid2NTDiamods _2H")),
                shows(Bid._3S, shape(4, 10), noFit(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RespondBid2SAYS.secondBid2NTDiamods _2S")),
                shows(Bid._3NT, PAIR_BALANCED, id("RespondBid2SAYS.secondBid2NTDiamods _3NT")),
                shows(Bid._5D, fit(), partner(isLastBid(Bid._3D)), id("RespondBid2SAYS.secondBid2NTDiamods _5D")),
                partnerBids(RecursionSAYS::recursionFindFitGame),
                properties(new Call[]{Bid._3H, Bid._3S}, OpenBid3Bid1SAYS::thirdBidToGame2NTDiamond)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidLong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidSearchSuitAfter2NTHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, shape(6, 10), IS_REBID, id("RespondBid2SAYS.secondBidSearchSuitAfter2NTHeart _3S")),
                shows(Bid._3D, shape(6, 10), IS_REBID, id("RespondBid2SAYS.secondBidSearchSuitAfter2NTHeart _3D")),
                shows(Bid._3C, shape(6, 10), IS_REBID, id("RespondBid2SAYS.secondBidSearchSuitAfter2NTHeart _3C")),
                shows(Bid._3D, shape(5, 10), IS_NEW_SUIT, id("RespondBid2SAYS.secondBidSearchSuitAfter2NTHeart _3D")),
                shows(Bid._3C, shape(5, 10), IS_NEW_SUIT, id("RespondBid2SAYS.secondBidSearchSuitAfter2NTHeart _3C")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidSearchSuitAfter2NTSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(6, 10), IS_REBID, ruleShow(1), id("RespondBid2SAYS.secondBidSearchSuitAfter2NTSpade _3H")),
                shows(Bid._3D, shape(6, 10), IS_REBID, ruleShow(1), id("RespondBid2SAYS.secondBidSearchSuitAfter2NTSpade _3D")),
                shows(Bid._3C, shape(6, 10), IS_REBID, ruleShow(1), id("RespondBid2SAYS.secondBidSearchSuitAfter2NTSpade _3C")),
                shows(Bid._3D, shape(5, 10), IS_NEW_SUIT, ruleShow(1), id("RespondBid2SAYS.secondBidSearchSuitAfter2NTSpade _3D")),
                shows(Bid._3C, shape(5, 10), IS_NEW_SUIT, ruleShow(1), id("RespondBid2SAYS.secondBidSearchSuitAfter2NTSpade _3C")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidInviteMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidInviteMajorHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidToGameHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3S, shape(4, 10), noFit(), IS_NEW_SUIT, ruleShow(1), id("RespondBid2SAYS.secondBidToGameHeart _3S")),
                shows(Bid._3S, shape(6, 10), noFit(), IS_REBID, ruleShow(1), id("RespondBid2SAYS.secondBidToGameHeart _3S")),
                partnerBids(RecursionSAYS::recursionFindFitGame),
                properties(new Call[]{Bid._3S}, OpenBid3Bid1SAYS::thirdBidToGameHeart, false)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls secondBidToGameMinorHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3H, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidToGameMinorHeart _3H")),
                shows(Bid._3S, shape(6, 10), noFit(), IS_REBID, id("RespondBid2SAYS.secondBidToGameMinorHeart _3S")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }
}
