package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;

public class OpenBid3Bid1NatC extends OpenBid1NatC {

    public static PositionCalls thirdBidNegat2NTStrong(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        //                                   2NT ->
        //                                          3C, 3D, 3H, 3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(RecursionNatC::recursionFindFitGame),
                shows(Bid._3D, shape(5, 10), noFit(7), id("OpenBid3NatC.thirdBidNegat2NTStrong _3D")),
                shows(Bid._3H, shape(4, 10), noFit(7), DECENT_PLUS_SUIT, id("OpenBid3NatC.thirdBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(4, 10), noFit(7), DECENT_PLUS_SUIT, id("OpenBid3NatC.thirdBidNegat2NTStrong _3S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls thirdBidToGameMinorClubStrong(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        //                          3S, 3H, 4D, 4C, 3NT ->
        //                                                3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static PositionCalls thirdBidMinorClubForcingStrong(PositionState ps) {
        //1C ->
        //      Bid._2C, Bid._2D ->
        //                          4C, 4D ->
        //                                    4D ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(RecursionNatC::recursionFindFitGame),
                shows(Bid._5C, noFit(), shape(6, 10), IS_REBID, id("OpenBid3NatC.thirdBidMinorClubForcingStrong _5C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static PositionCalls thirdBidToGameDiamond(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        //                              Bid._2H, Bid._1S, Bid._2S, Bid._2C ->
        //                                                              Bid._2H, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(RecursionNatC::recursionFindFitGame),

                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.thirdBidToGameDiamond Pass")),

                shows(Bid._2S, noFit(), IS_REBID, shape(6, 10), id("OpenBid3NatC.thirdBid _2S")),
                shows(Bid._3D, noFit(), shape(6, 10), id("OpenBid3NatC.thirdBid _3D")),
                shows(Bid._3C, noFit(), shape(6, 10), id("OpenBid3NatC.thirdBid _3C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls thirdBidToGame1NTDiamond(PositionState ps) {
        //1D ->
        //     Bid._1NT->
        //              Bid._2D, Bid._2C, Bid._2H, Bid._2S->
        //                                                 Bid._2H, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(RecursionNatC::recursionFindFitGame),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.thirdBidToGame1NTDiamond Pass")),
                shows(Bid._2S, noFit(), shape(4, 10), id("OpenBid3NatC.thirdBidToGame1NTDiamond _2S")),
                shows(Bid._3C, noFit(), shape(5, 10), id("OpenBid3NatC.thirdBidToGame1NTDiamond _3C")),
                shows(Bid._3D, noFit(), shape(2), id("OpenBid3NatC.thirdBidToGame1NTDiamond _3D")),
                shows(Bid._2NT, noFit(), PAIR_BALANCED, id("OpenBid3NatC.thirdBidToGame1NTDiamond _2NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls thirdBidToGame2NTDiamond(PositionState ps) {
        //1D ->
        //     Bid._2NT->
        //              Bid._3D, Bid._3C, Bid._3H, Bid._3S ->
        //                                                 Bid._3H, Bid._3S
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(RecursionNatC::recursionFindFitGame),
                //shows(Bid._4H, fit(), shape(4, 10), setTrumpColor(Suit.Hearts), id("OpenBid3NatC.thirdBidToGame2NTDiamond _3S")),
                //shows(Bid._4S, fit(), shape(4, 10), setTrumpColor(Suit.Spades), id("OpenBid3NatC.thirdBidToGame2NTDiamond _3S")),
                shows(Bid._3S, noFit(), shape(4, 10), id("OpenBid3NatC.thirdBidToGame2NTDiamond _3S"))
                //shows(Bid._3NT, noFit(), PAIR_BALANCED, id("OpenBid3NatC.thirdBidToGame2NTDiamond _3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls thirdBidToGameHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._3C, Bid._3D, Bid._2S ->
        //                                                     Bid._3H ->
        //                                                              Bid._3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                //shows(Bid._4S, fit(), setTrumpColor(Suit.Spades), pairHighCardPoints(PAIR_GAME), id("OpenBid3NatC.thirdBidToGameHeart _4S"))
                //jest w compBids:
                //shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid3NatC.thirdBidToGameHeart _4S")),
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }
}




























































































































































































































































































































































































































































































