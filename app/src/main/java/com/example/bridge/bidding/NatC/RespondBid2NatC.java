package com.example.bridge.bidding.NatC;

import static com.example.bridge.bidding.NatC.OpenNatC.OpeningInviteBidding;
import static com.example.bridge.bidding.NatC.OpenNatC.OpeningLowBidding;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

public class RespondBid2NatC extends RespondNatC {

    public static PositionCalls secondBidNegat2NTStrong(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        //                                   2NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(OpenBid3NatC::thirdBidNegat2NTStrong),
                shows(Bid._3H, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3S")),
                shows(Bid._3D, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3D")),
                shows(Bid._3C, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3C")),
                shows(Bid._3H, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3S")),
                shows(Bid._3D, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3D")),
                shows(Bid._3C, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _3C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidNegatStrong(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        //                                  2H, 2S, 3C, 3D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBidNegatStrong Pass")),

                shows(Bid._4S, fit(), pairPoints(PAIR_GAME), id("RespondBid2NatC.secondBidNegatStrong _4S")),
                shows(Bid._4H, fit(), pairPoints(PAIR_GAME), id("RespondBid2NatC.secondBidNegatStrong _4H")),
                shows(Bid._2S, noFit(), shape(5, 10), id("RespondBid2NatC.secondBidNegatStrong _2S")),
                shows(Bid._3H, noFit(), shape(6, 10), id("RespondBid2NatC.secondBidNegatStrong _3H")),
                shows(Bid._3H, shape(5, 10), shape(ps.getPartner().getBid().getSuit(), 0, 1), id("RespondBid2NatC.secondBidNegatStrong _3H")),
                shows(Bid._3C, noFit(), shape(5, 10), id("RespondBid2NatC.secondBidNegatStrong _3C")),
                shows(Bid._3D, noFit(), shape(5, 10), id("RespondBid2NatC.secondBidNegatStrong _3D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidNegatStandard(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        //                                    1H, 1S, 1NT, 2C, 2D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBidNegatStandard Pass")),
                shows(Bid._1S, shape(6, 10), id("RespondBid2NatC.secondBidNegatStandard _1S")),
                shows(Bid._2H, shape(6, 10), id("RespondBid2NatC.secondBidNegatStandard _2H")),
                shows(Bid._2S, shape(6, 10), id("RespondBid2NatC.secondBidNegatStandard _2S")),
                shows(Bid._2D, shape(6, 10), id("RespondBid2NatC.secondBidNegatStandard _2D")),
                shows(Bid._2C, shape(6, 10), id("RespondBid2NatC.secondBidNegatStandard _2C")),
                shows(Bid._1S, shape(5, 10), id("RespondBid2NatC.secondBidNegatStandard _1S")),
                shows(Bid._2H, shape(5, 10), id("RespondBid2NatC.secondBidNegatStandard _2H")),
                shows(Bid._2S, shape(5, 10), id("RespondBid2NatC.secondBidNegatStandard _2S")),
                shows(Bid._2D, shape(5, 10), id("RespondBid2NatC.secondBidNegatStandard _2D")),
                shows(Bid._2C, shape(5, 10), id("RespondBid2NatC.secondBidNegatStandard _2C")),
                shows(Bid._1NT, id("RespondBid2NatC.secondBidNegatStandard _1NT")),
                shows(Bid._2NT, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("RespondBid2NatC.secondBidNegatStandard _2NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidMajorClubStrong(PositionState ps) {
        //1C ->
        //      1S, 1H ->
        //              2S, 3H, 3S, 3C, 3D, 3NT, 4H, 4S
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                AcesAsk.initiateConvention(ps),
                AcesAsk.initiateConventionBlok(ps)
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidMajorClubStandard(PositionState ps) {
        //1C ->
        //      1S, 1H ->
        //              1S, 2C, 2H, 2S, 1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._2S, noFit(), shape(6,10), IS_REBID, id("RespondBid2NatC.secondBidMajorClubStandard _2S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidMinorClubStrong(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        //                          3S, 3H, 4D, 4C, 3NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                AcesAsk.initiateConvention(ps),
                AcesAsk.initiateConventionBlok(ps),
                partnerBids(NatC::finishBiddingIterable),
                properties(new Call[]{Bid._3S}, OpenBid3NatC::thirdBidToGameMinorClubStrong),
                shows(Bid._3S, shape(4, 10), IS_NEW_SUIT, id("RespondBid2NatC.secondBidMinorClubStrong _3S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidMinorClubStandard(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        //                      3C, 2H, 2S, 3C ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3C, shape(6, 10), IS_REBID, shape(ps.getPartner().getBid().getSuit(), 1, 2), id("RespondBid2NatC.secondBidMinorClubStandard _3C")),
                shows(Bid._3D, shape(6, 10), IS_REBID, shape(ps.getPartner().getBid().getSuit(), 1, 2), id("RespondBid2NatC.secondBidMinorClubStandard _3D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubStrong(PositionState ps) {
        //1C ->
        //      Bid._3D, Bid._3C ->
        //                      4H, 4S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._4S, noFit(), shape(4, 10), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubStrong _4S")),
                shows(Bid._5C, noFit(), shape(6, 10), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubStrong _5C")),
                shows(Bid._5D, noFit(), shape(6, 10), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubStrong _5D")),
                shows(Bid._4NT, noFit(), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubStrong _4NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubMajorStandard(PositionState ps) {
        //1C ->
        //      Bid._3D, Bid._3C ->
        //                        3H, 3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._4H, fit(), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, fit(), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _4S")),
                shows(Bid._4D, noFit(), shape(6, 10), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _4D")),
                shows(Bid._3S, noFit(), shape(4, 10), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _3S")),
                shows(Bid._3NT, noFit(), PAIR_BALANCED, id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _3NT")),
                shows(Bid._4H, shape(3), OpeningInviteBidding, partner(isLastBid(Bid._3H)), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, shape(3), OpeningInviteBidding, partner(isLastBid(Bid._3S)), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _4S")),

                shows(Bid._4H, shape(3), OpeningLowBidding, partner(isLastBid(Bid._3H)), EXCELLENT_PLUS_SUIT, id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _4H")),
                shows(Bid._4S, shape(3), OpeningLowBidding, partner(isLastBid(Bid._3S)), EXCELLENT_PLUS_SUIT, id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _4S")),

                shows(Call.PASS, shape(Suit.Spades, 0, 2), OpeningLowBidding, partner(isLastBid(Bid._3S)), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _pass")),
                shows(Call.PASS, shape(Suit.Hearts, 0, 2), OpeningLowBidding, partner(isLastBid(Bid._3H)), id("RespondBid2NatC.secondBidRaiseTrumpMinorClubMajorStandard _pass"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMinorClubStandard(PositionState ps) {

        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubStrong(PositionState ps) {
        //1C ->
        //     2H, 2S ->
        //             3S, 4D, 4C ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._4H, shape(6, 10), noFit(), id("RespondBid2NatC.secondBidRaiseTrumpMajorClubClubStrong 4H")),
                shows(Bid._3NT, noFit(), id("RespondBid2NatC.secondBidRaiseTrumpMajorClubClubStrong 3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubStandard(PositionState ps) {
        //1C ->
        //     2H, 2S ->
        //             2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBidRaiseTrumpMajorClubStandard pass")),
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3H, shape(6, 10), noFit(), shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._2S)), IS_REBID, id("RespondBid2NatC.responderdRaiseTrumpMajorClubStandard 3H")),
                shows(Bid._3C, shape(5, 10), noFit(), shape(5, 10), shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._2S)), id("RespondBid2NatC.responderdRaiseTrumpMajorClubStandard 3C")),
                shows(Bid._3D, shape(5, 10), noFit(), shape(5, 10), shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._2S)), id("RespondBid2NatC.responderdRaiseTrumpMajorClubStandard 3C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorFitClubStandard(PositionState ps) {
        //1C ->
        //     2H, 2S ->
        //              3H, 3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseTrumpMajorClubClubStandard(PositionState ps) {
        //1C ->
        //     2H, 2S ->
        //              3C ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3H, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3H")),
                shows(Bid._3S, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static PositionCalls secondBidNoAgreeTrumpDiamods(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        //                              Bid._2H, Bid._1S, Bid._2S, Bid._2C ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3NatC::thirdBidToGameDiamond),

                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods Pass")),

                shows(Bid._2H, noFit(), DECENT_PLUS_SUIT, shape(4, 10), IS_NEW_SUIT, id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _2H")),
                shows(Bid._2H, noFit(), DECENT_PLUS_SUIT, shape(6, 10), IS_REBID, id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _2H")),
                shows(Bid._2S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), IS_NEW_SUIT, id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _2S")),
                shows(Bid._2S, noFit(), DECENT_PLUS_SUIT, shape(6, 10), IS_REBID, id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _2S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseChangeSuitMinorDiamods(PositionState ps) {
        //1D ->
        //     Bid._3C->
        //              Bid._3D, Bid._3H, Bid._3S
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidRaiseChangeSuitMinorDiamods _3S")),
                shows(Bid._5C, fit(), setTrumpColor(Suit.Clubs), id("RespondBid2NatC.secondBidRaiseChangeSuitMinorDiamods _5C")),
                shows(Bid._5D, shape(2), partner(isLastBid(Bid._3D)), id("RespondBid2NatC.secondBidRaiseChangeSuitMinorDiamods _5D"))

                //shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRaiseNoAgreeTrumpDiamods(PositionState ps) {
        //1D ->
        //     Bid._2H, Bid._2S->
        //                      Bid._2S, Bid._3H, Bid._3C, Bid._3C ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), id("RespondBid2NatC.secondBidRaiseNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), id("RespondBid2NatC.secondBidRaiseNoAgreeTrumpDiamods _3H")),
                shows(Bid._3S, shape(6, 10), IS_REBID, id("RespondBid2NatC.secondBidRaiseNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, shape(6, 10), IS_REBID, id("RespondBid2NatC.secondBidRaiseNoAgreeTrumpDiamods _3H"))

                //shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidRaiseNoAgreeTrumpDiamods _3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidMinorAgreeTrumpDiamods(PositionState ps) {
        //1D ->
        //     Bid._2D, Bid._3D ->
        //                      Bid._2H, Bid._2S, Bid._3H, Bid._3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));

        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _4H")),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _4S")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods 3D")),
                shows(Bid._5D, noFit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods 5D")),

                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods Pass")),

                shows(Bid._3D, noFit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods 3D"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidRebidDiamods(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        //                              Bid._2D, Bid._3D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBidRebidDiamods Pass")),
                shows(Bid._2S, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidRebidDiamods _2S")),
                shows(Bid._2H, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidRebidDiamods _2H")),
                shows(Bid._3C, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidRebidDiamods _3C")),
                shows(Bid._2S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidRebidDiamods _2S")),
                shows(Bid._2H, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidRebidDiamods _2H")),
                shows(Bid._3C, noFit(), DECENT_PLUS_SUIT, shape(5, 10), id("RespondBid2NatC.secondBidRebidDiamods _3C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBid1NTDiamods(PositionState ps) {
        //1D ->
        //     Bid._1NT->
        //              Bid._2D, Bid._2C, Bid._2H, Bid._2S
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3NatC::thirdBidToGame1NTDiamond),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBid1NTDiamods Pass")),
                shows(Bid._2H, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBid1NTDiamods _2H")),
                shows(Bid._2S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBid1NTDiamods _2S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBid2NTDiamods(PositionState ps) {
        //1D ->
        //     Bid._2NT->
        //              Bid._3D, Bid._3C, Bid._3H, Bid._3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                properties(new Call[]{Bid._3H, Bid._3S}, OpenBid3NatC::thirdBidToGame2NTDiamond),
                shows(Bid._3H, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBid2NTDiamods _2H")),
                shows(Bid._3S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBid2NTDiamods _2S")),
                shows(Bid._3NT, noFit(), PAIR_BALANCED, id("RespondBid2NatC.secondBid2NTDiamods _3NT")),
                shows(Bid._5D, noFit(), partner(isLastBid(Bid._3D)), shape(2), id("RespondBid2NatC.secondBid2NTDiamods _5D"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidLong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.openerInvitedGame _4H")),
                //shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.openerInvitedGame _4S")),
                //shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidSearchSuitAfter2NTHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._1S, Bid._2C, Bid._2D, Bid._1NT ->
        //                                                                2NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3S, IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTHeart _3S")),
                shows(Bid._3D, IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTHeart _3D")),
                shows(Bid._3C, IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTHeart _3C")),

                shows(Bid._3D, IS_NEW_SUIT, shape(5, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTHeart _3D")),
                shows(Bid._3C, IS_NEW_SUIT, shape(5, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTHeart _3C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidSearchSuitAfter2NTSpade(PositionState ps) {
        //odpowiedzi na: Bid._1S ->
        //                          Bid._2H, Bid._2C, Bid._2D, Bid._1NT ->
        //                                                              2NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3H, IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTSpade _3H")),
                shows(Bid._3D, IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTSpade _3D")),
                shows(Bid._3C, IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTSpade _3C")),

                shows(Bid._3D, IS_NEW_SUIT, shape(5, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTSpade _3D")),
                shows(Bid._3C, IS_NEW_SUIT, shape(5, 10), id("RespondBid2NatC.secondBidSearchSuitAfter2NTSpade _3C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidInviteMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                //shows(Bid._5C, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajorHeart _5C")),
                //shows(Bid._5D, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_GAME), id("OpenBid2NatC.responderRaisedMajorHeart _5D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidInviteMajorHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._3C, Bid._3D, Bid._2S ->
        //                                                      Bid._3S, Bid._4S -->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                //myśle że nic nie trzeba dodawać licytacja powinna sie zakonczyc na 4S, 5S, 6S lub 7S
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidToGameHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._3C, Bid._3D, Bid._2S ->
        //                                                     Bid._3H ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                properties(new Call[]{Bid._3S}, OpenBid3NatC::thirdBidToGameHeart, false),
                shows(Bid._3S, noFit(), shape(4, 10), not(isLastBid(Bid._2S)), id("RespondBid2NatC.secondBidToGameHeart  not(myLastBid(Bid._2S)) _3S")),
                shows(Bid._3S, noFit(), shape(6, 10), IS_REBID, id("RespondBid2NatC.secondBidToGameHeart _3S"))
                //jest w compBids:
                //shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidToGameHeart _4H")),
                //shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME),id("RespondBid2NatC.secondBidToGameHeart _3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidToGameMinorHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._3C, Bid._3D, Bid._2S ->
        //                                                      Bid._3C, Bid._3D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(NatC::finishBiddingIterable),
                shows(Bid._3H, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidToGameMinorHeart _3H")),
                shows(Bid._3S, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidToGameMinorHeart _3S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }
}