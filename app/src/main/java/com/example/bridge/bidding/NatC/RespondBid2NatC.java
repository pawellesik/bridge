package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RespondBid2NatC extends RespondNatC {

    public static PositionCalls secondBidNegat2NTStrong(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        //                                   2NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
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

    public static PositionCalls secondBidNegat1NTStandard(PositionState ps) {
        //odpowiedzi na: Bid._1C ->
        //                          Bid._1D ->
        //                                   1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2H, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2H")),
                shows(Bid._2S, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2S")),
                shows(Bid._2D, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2D")),
                shows(Bid._2C, shape(6, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2C")),
                shows(Bid._2H, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2H")),
                shows(Bid._2S, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2S")),
                shows(Bid._2D, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2D")),
                shows(Bid._2C, shape(5, 10), id("RespondBid2NatC.secondBidNegat2NTStrong _2C"))
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
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBidNegatStrong Pass")),
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
        //                                    1H, 1S, 1NT ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
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

    public static PositionCalls secondBidNoAgreeTrumpDiamods(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        //                              Bid._2H, Bid._1S, Bid._2S, Bid._2C ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3NatC::thirdBidToGameDiamond),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods Pass")),
                shows(Bid._2H, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _2H")),
                shows(Bid._2S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _2S"))
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
                shows(Bid._3S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3S")),
                shows(Bid._5C, fit(), setTrumpColor(Suit.Clubs), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _5C")),
                shows(Bid._5D, shape(2), partner(isLastBid(Bid._3D)), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _5D"))

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
                shows(Bid._3S, fit(), setTrumpColor(Suit.Spades), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, fit(), setTrumpColor(Suit.Hearts), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3H")),
                shows(Bid._3S, shape(6, 10), IS_REBID, id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3S")),
                shows(Bid._3H, shape(6, 10), IS_REBID, id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _3H"))

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
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods Pass")),
                //shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _4H")),
                //shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidNoAgreeTrumpDiamods _4S")),
                shows(Bid._3D, noFit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods 3D")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods 3D")),
                shows(Bid._5D, noFit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidMinorAgreeTrumpDiamods 5D"))
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
                properties(new Call[]{Bid._2H, Bid._2S}, OpenBid3NatC::thirdBidToGame1NTDiamond),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.secondBid1NTDiamods Pass")),
                shows(Bid._2H, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidRebidDiamods _2H")),
                shows(Bid._2S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBidRebidDiamods _2S"))
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
                properties(new Call[]{Bid._3H, Bid._3S}, OpenBid3NatC::thirdBidToGame2NTDiamond),
                shows(Bid._3H, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBid2NTDiamods _2H")),
                shows(Bid._3S, noFit(), DECENT_PLUS_SUIT, shape(4, 10), id("RespondBid2NatC.secondBid2NTDiamods _2S")),
                shows(Bid._3NT, noFit(), PAIR_BALANCED, id("RespondBid2NatC.secondBid2NTDiamods _3NT")),
                shows(Bid._5D, noFit(), partner(isLastBid(Bid._3D)), shape(2), id("RespondBid2NatC.secondBid2NTDiamods _5D"))

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    /// //////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////
    public static PositionCalls responderClubJumpMinorChangeMajor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(partnerBids(OpenBid3NatC::thirdBid),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _4S")),
                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _4H")),
                shows(Bid._3NT, othersAtLeast(3), partnerLastSuitShape(0, 3), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _3NT")),
                shows(Bid._3NT, othersAtLeast(3), partnerLastSuitShape(0, 1), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _3NT")),
                shows(Bid._4S, GOOD_PLUS_SUIT, shape(3, 3), partner(isLastBid(Bid._3S)), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor GOOD_PLUS_SUIT _4S")),
                shows(Bid._4H, GOOD_PLUS_SUIT, shape(3, 3), partner(isLastBid(Bid._3H)), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor GOOD_PLUS_SUIT _4H")),

                shows(Call.PASS)
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls colorAfterPass(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(partnerBids(OpenBid3NatC::thirdBid),
                shows(Bid._2S, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2S")),
                shows(Bid._2H, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2H")),
                shows(Bid._2D, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2D")),
                shows(Bid._2C, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2C")),

                shows(Bid._3S, shape(6, 10), id("RespondBid2NatC.colorAfterPass _3S")),
                shows(Bid._3H, shape(6, 10), id("RespondBid2NatC.colorAfterPass _3H")),
                shows(Bid._3D, shape(6, 10), id("RespondBid2NatC.colorAfterPass _3D")),
                shows(Bid._3C, shape(6, 10), id("RespondBid2NatC.colorAfterPass _3C")),

                shows(Bid._3S, shape(4, 10), highCardPoints(5, 6), id("RespondBid2NatC.colorAfterPass _3S")),
                shows(Bid._3H, shape(4, 10), highCardPoints(5, 6), id("RespondBid2NatC.colorAfterPass _3H")),

                shows(Call.PASS, id("RespondBid2NatC.colorAfterPass PASS"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(OpenBid3NatC::thirdBid));

        bids.add(shows(Bid._2S, IS_PARTNERS_SUIT, betterThan(Suit.Hearts), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, IS_PARTNERS_SUIT, betterThan(Suit.Spades), id("RespondBid2NatC.secondBid _2H")));

        bids.add(shows(Bid._2S, fit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, fit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2H")));

        bids.add(shows(Bid._2S, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2H")));

        bids.add(shows(Bid._2S, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.secondBid _2H")));

        bids.add(shows(Bid._3C, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _3C")));
        bids.add(shows(Bid._3D, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _3D")));

        bids.add(shows(Bid._3C, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.secondBid _3C")));
        bids.add(shows(Bid._3D, noFit(), IS_REBID, shape(6, 10), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.secondBid _3D")));


        for (CallFeature cf : CompeteNatC.compBids(ps)) {
            bids.add(cf);
        }

        return bids;
    }

    public static PositionCalls secondBidToGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME)), id("RespondBid2NatC.secondBidToGame _4H"),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME)), id("RespondBid2NatC.secondBidToGame _4S"),

                shows(Bid._3S, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.secondBidToGame _3S")),
                shows(Bid._3H, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.secondBidToGame _3H")),
                shows(Bid._3S, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidToGame _3S")),
                shows(Bid._3H, IS_REBID, shape(6, 11), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidToGame _3H")),

                shows(Bid._5C, fit(), pairHighCardPoints(PAIR_MINOR_GAME)), id("RespondBid2NatC.secondBidToGame _5C"),
                shows(Bid._5D, fit(), pairHighCardPoints(PAIR_MINOR_GAME)), id("RespondBid2NatC.secondBidToGame _5D"),

                shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidToGame _3NT")),
                shows(Call.PASS), id("RespondBid2NatC.secondBidToGame PASS"));

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls openerInvitedGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                //shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Hearts), id("RespondBid2NatC.openerInvitedGame _4H")),
                // shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Spades), id("RespondBid2NatC.openerInvitedGame _4S")),
                //shows(Bid._3NT, pairHighCardPoints(PAIR_GAME))
                //shows(Call.PASS, id("RespondBid2NatC.openerInvitedGame PASS"))
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
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
//todo

        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls secondBidSearchSuitAfter2NTSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
//todo

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
                shows(Bid._3H, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidToGameMinorHeart _3H")),
                shows(Bid._3S, noFit(), IS_REBID, shape(6, 10), id("RespondBid2NatC.secondBidToGameMinorHeart _3S"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }
}