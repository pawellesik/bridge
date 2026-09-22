package com.example.bridge.bidding.SAYS;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

public class RecursionSAYS extends SAYS {

    public static PositionCalls recursionFindFitGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));

        choices.addRules(
                shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, not(PARTNER_DID_NOT_SIGN_OFF), id("RecursionSAYS.recursionFindFitGame CONTRACT_IS_AGREED_STRAIN _Pass")),
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), partnerBidLevel(2, 3), id("RecursionSAYS.recursionFindFitGame pass")),

                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._3NT)), id("RecursionSAYS.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4H)), setTrumpColor(Suit.Hearts), id("RecursionSAYS.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4S)), setTrumpColor(Suit.Spades), id("RecursionSAYS.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5D)), setTrumpColor(Suit.Diamonds), id("RecursionSAYS.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5C)), setTrumpColor(Suit.Clubs), id("RecursionSAYS.recursionFindFitGame fit _Pass")),

                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._2S, Bid._2H)), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RecursionSAYS.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._3C, Bid._3D)), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RecursionSAYS.recursionFindFitGame fit _Pass")),

                shows(Bid._2H, shape(6, 10), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _2H")),
                shows(Bid._2S, shape(6, 10), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _2S")),
                shows(Bid._2C, shape(6, 10), id("RecursionSAYS.recursionFindFitGame IS_REBID _2C")),
                shows(Bid._2D, shape(6, 10), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _2D")),

                shows(Bid._2H, shape(5, 10), IS_NEW_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _2H")),
                shows(Bid._2S, shape(5, 10), IS_NEW_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _2S")),

                shows(Bid._2C, shape(5, 10), id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _2C")),
                shows(Bid._2D, shape(5, 10), noFitMajor(), IS_NEW_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _2D")),

                shows(Bid._2H, shape(4, 10), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _2H")),
                shows(Bid._2S, shape(4, 10), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _2S")),

                shows(Bid._3C, shape(6, 10), noFit(7), not(suitBidCount(2)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(6, 10), noFit(7), not(suitBidCount(2)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(6, 10), noFit(7), not(suitBidCount(2)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(6, 10), noFit(7), not(suitBidCount(2)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._3C, shape(6, 10), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(6, 10), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(6, 10), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(6, 10), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._3C, shape(7, 10), noFit(7), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(7, 10), noFit(7), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(7, 10), noFit(7), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(7, 10), noFit(7), IS_REBID, id("RecursionSAYS.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._3H, shape(5, 10), noFitMajor(), IS_NEW_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _3H")),
                shows(Bid._3S, shape(5, 10), noFitMajor(), IS_NEW_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _3S")),
                shows(Bid._3C, shape(5, 10), noFitMajor(), IS_NEW_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _3C")),
                shows(Bid._3D, shape(5, 10), noFitMajor(), IS_NEW_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT _3D")),

                shows(Bid._3H, shape(4, 10), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3H")),
                shows(Bid._3S, shape(4, 10), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionSAYS.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3S")),

                shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Clubs), id("RecursionSAYS.recursionFindFitGame  _3C")),
                shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Diamonds), id("RecursionSAYS.recursionFindFitGame  _3D")),

                shows(Bid._4D, shape(5, 10), twoSuiter(5), hasShortness(0, 0), partner(isLastBid(Bid._3NT)), id("RecursionSAYS.recursionFindFitGame  exit 1 _4D")),
                shows(Bid._4C, shape(5, 10), twoSuiter(5), hasShortness(0, 0), partner(isLastBid(Bid._3NT)), id("RecursionSAYS.recursionFindFitGame  exit 1 _4C")),

                shows(Bid._4C, shape(6, 10), IS_REBID, noFit(7), NOT_BALANCED, NOT_PAIR_BALANCED, partner(isLastBid(Bid._3H,Bid._3S)), id("RecursionSAYS.recursionFindFitGame exit 2 _4C")),
                shows(Bid._4D, shape(6, 10), IS_REBID, noFit(7), NOT_BALANCED, NOT_PAIR_BALANCED, partner(isLastBid(Bid._3H,Bid._3S)), id("RecursionSAYS.recursionFindFitGame exit 2 _4D")),

                CompeteSAYS.bids(ps)
        );
        return choices;
    }
}
