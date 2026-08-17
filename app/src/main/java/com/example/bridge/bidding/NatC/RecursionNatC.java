package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;

public class RecursionNatC extends NatC {

    public static PositionCalls recursionFindLowFitGame(PositionState ps) {
        return recursionFindLowFitGame(ps, 1);
    }

    public static PositionCalls recursionFindLowFitGame(PositionState ps, int level) {
        PositionCalls choices = new PositionCalls(ps);
        //choices.addRules(CompeteNatC.compBids(ps));
        if (level < 20) {
            choices.addRules(
                    shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, id("RecursionNatC.recursionFindLowFitGame CONTRACT_IS_AGREED_STRAIN _Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RecursionNatC.recursionFindLowFitGame fit _Pass")),

                    shows(Bid._2H, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2H")),
                    shows(Bid._2S, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2S")),
                    shows(Bid._2C, shape(6, 10), id("RecursionNatC.recursionFindLowFitGame IS_REBID _2C")),
                    shows(Bid._2D, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2D")),
                    shows(Bid._3C, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3C")),
                    shows(Bid._3D, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3D")),
                    shows(Bid._3H, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3H")),
                    shows(Bid._3S, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3S")),

                    shows(Bid._2H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2H")),
                    shows(Bid._2S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2S")),
                    shows(Bid._2C, shape(5, 10), id("RecursionNatC.recursionFindLowFitGame IS_REBID _2C")),
                    shows(Bid._2D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2D")),
                    shows(Bid._3H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3H")),
                    shows(Bid._3S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3S")),
                    shows(Bid._3C, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3C")),
                    shows(Bid._3D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3D")),

                    shows(Bid._2H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2H")),
                    shows(Bid._2S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2S")),
                    shows(Bid._3H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3H")),
                    shows(Bid._3S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3S")),

                    shows(Bid._1NT, PAIR_BALANCED, id("RecursionNatC.recursionFindLowFitGame IS_REBID _1NT")),
                    shows(Bid._2NT, PAIR_BALANCED, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2NT")),
                    shows(Bid._3NT, PAIR_BALANCED, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3NT"))
            );
            choices.addRules(RecursionNatC.recursionFindLowFitGame(ps, level + 1));
        } else {
            choices.addRules(shows(Call.PASS, id("RecursionNatC.recursionFindLowFitGame Pass")));
        }
        return choices;
    }


    public static PositionCalls recursionFindFitGame(PositionState ps) {
        return recursionFindFitGame(ps, 1);
    }

    public static PositionCalls recursionFindFitGame(PositionState ps, int level) {
        PositionCalls choices = new PositionCalls(ps);
        if (level < 20) {
            choices.addRules(AcesAsk.initiateConvention(ps));
            choices.addRules(AcesAsk.initiateConventionBlok(ps));
            choices.addRules(
                    shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, not(PARTNER_DID_NOT_SIGN_OFF), id("RecursionNatC.recursionFindLowFitGame CONTRACT_IS_AGREED_STRAIN _Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._3NT)), id("RecursionNatC.recursionFindLowFitGame fit _Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4H)), id("RecursionNatC.recursionFindLowFitGame fit _Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4S)), id("RecursionNatC.recursionFindLowFitGame fit _Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5D)), id("RecursionNatC.recursionFindLowFitGame fit _Pass")),
                    shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5C)), id("RecursionNatC.recursionFindLowFitGame fit _Pass")),

                    shows(Bid._4H, fit(), id("RecursionNatC.recursionFindLowFitGame IS_REBID _4H")),
                    shows(Bid._4S, fit(), id("RecursionNatC.recursionFindLowFitGame IS_REBID _4S")),
                    shows(Bid._5C, fit(), id("RecursionNatC.recursionFindLowFitGame IS_REBID _5C")),
                    shows(Bid._5D, fit(), id("RecursionNatC.recursionFindLowFitGame IS_REBID _5D")),

                    shows(Bid._2H, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2H")),
                    shows(Bid._2S, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2S")),
                    shows(Bid._2C, shape(6, 10), id("RecursionNatC.recursionFindLowFitGame IS_REBID _2C")),
                    shows(Bid._2D, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2D")),
                    shows(Bid._3C, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3C")),
                    shows(Bid._3D, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3D")),
                    shows(Bid._3H, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3H")),
                    shows(Bid._3S, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3S")),

                    shows(Bid._2H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2H")),
                    shows(Bid._2S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2S")),
                    shows(Bid._2C, shape(5, 10), id("RecursionNatC.recursionFindLowFitGame IS_REBID _2C")),
                    shows(Bid._2D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2D")),
                    shows(Bid._3H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3H")),
                    shows(Bid._3S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3S")),
                    shows(Bid._3C, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3C")),
                    shows(Bid._3D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3D")),

                    shows(Bid._2H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2H")),
                    shows(Bid._2S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2S")),
                    shows(Bid._3H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3H")),
                    shows(Bid._3S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3S")),

                    shows(Bid._1NT, PAIR_BALANCED, id("RecursionNatC.recursionFindLowFitGame IS_REBID _1NT")),
                    shows(Bid._2NT, PAIR_BALANCED, id("RecursionNatC.recursionFindLowFitGame IS_REBID _2NT")),
                    shows(Bid._3NT, PAIR_BALANCED, id("RecursionNatC.recursionFindLowFitGame IS_REBID _3NT"))
            );
            choices.addRules(RecursionNatC.recursionFindFitGame(ps, level + 1));
        } else {
            choices.addRules(shows(Call.PASS, id("RecursionNatC.recursionFindLowFitGame Pass")));
        }
        return choices;
    }


}






