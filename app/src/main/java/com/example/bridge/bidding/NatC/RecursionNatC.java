package com.example.bridge.bidding.NatC;

import static com.example.bridge.bidding.Conventions.AcesAsk.SLAM_OR_BETTER;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

public class RecursionNatC extends NatC {

    public static PositionCalls recursionFindFitGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, not(PARTNER_DID_NOT_SIGN_OFF), id("RecursionNatC.recursionFindFitGame CONTRACT_IS_AGREED_STRAIN _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(PAIR_LOW_GAME), partnerBidLevel(2, 3), id("RecursionNatC.recursionFindFitGame pass")),

                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._3NT)), id("RecursionNatC.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4H)), id("RecursionNatC.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4S)), id("RecursionNatC.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5D)), id("RecursionNatC.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5C)), id("RecursionNatC.recursionFindFitGame fit _Pass")),

                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._2S, Bid._2H)), pairHighCardPoints(PAIR_LOW_GAME), id("RecursionNatC.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._3C, Bid._3D)), pairHighCardPoints(PAIR_LOW_GAME), id("RecursionNatC.recursionFindFitGame fit _Pass")),

                shows(Bid._2H, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindFitGame IS_REBID _2H")),
                shows(Bid._2S, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindFitGame IS_REBID _2S")),
                shows(Bid._2C, shape(6, 10), id("RecursionNatC.recursionFindFitGame IS_REBID _2C")),
                shows(Bid._2D, shape(6, 10), IS_REBID, id("RecursionNatC.recursionFindFitGame IS_REBID _2D")),

                shows(Bid._2H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _2H")),
                shows(Bid._2S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _2S")),

                shows(Bid._2C, shape(5, 10), id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _2C")),
                shows(Bid._2D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _2D")),

                shows(Bid._2H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _2H")),
                shows(Bid._2S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _2S")),

                shows(Bid._3C, shape(6, 10), IS_REBID, noFit(7), id("RecursionNatC.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(6, 10), IS_REBID, noFit(7), id("RecursionNatC.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(6, 10), IS_REBID, noFit(7), id("RecursionNatC.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(6, 10), IS_REBID, noFit(7), id("RecursionNatC.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._3C, shape(6, 10), IS_REBID, partner(isLastBid(Bid._2NT)), id("RecursionNatC.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(6, 10), IS_REBID, partner(isLastBid(Bid._2NT)), id("RecursionNatC.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(6, 10), IS_REBID, partner(isLastBid(Bid._2NT)), id("RecursionNatC.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(6, 10), IS_REBID, partner(isLastBid(Bid._2NT)), id("RecursionNatC.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._3H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _3H")),
                shows(Bid._3S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _3S")),
                shows(Bid._3C, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _3C")),
                shows(Bid._3D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT _3D")),
                shows(Bid._3H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3H")),

                shows(Bid._3S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3S")),

                shows(Bid._3C, fit(Suit.Clubs), pairHighCardPoints(PAIR_LOW_GAME), id("RecursionNatC.recursionFindFitGame  _3C")),
                shows(Bid._3D, fit(Suit.Diamonds), pairHighCardPoints(PAIR_LOW_GAME), id("RecursionNatC.recursionFindFitGame  _3D")),

                CompeteNatC.bids(ps)
        );
        return choices;
    }
}






