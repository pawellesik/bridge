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

public class RecursionWJSimple extends WJSimple {

    public static PositionCalls recursionFindFitGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        List<CallFeature> conventions = new ArrayList<>();
        CompeteWJSimple.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, not(PARTNER_DID_NOT_SIGN_OFF), id("RecursionWJSimple.recursionFindFitGame CONTRACT_IS_AGREED_STRAIN _Pass")),
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), partnerBidLevel(2, 3), id("RecursionWJSimple.recursionFindFitGame pass")),

                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._3NT)), id("RecursionWJSimple.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4H)), setTrumpColor(Suit.Hearts), id("RecursionWJSimple.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._4S)), setTrumpColor(Suit.Spades), id("RecursionWJSimple.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5D)), setTrumpColor(Suit.Diamonds), id("RecursionWJSimple.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._5C)), setTrumpColor(Suit.Clubs), id("RecursionWJSimple.recursionFindFitGame fit _Pass")),

                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._2S, Bid._2H)), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RecursionWJSimple.recursionFindFitGame fit _Pass")),
                shows(Call.PASS, pairHighCardPoints(PAIR_LOW_GAME), fit(ps.getPartner().getBid().getSuit()), partner(isLastBid(Bid._3C, Bid._3D)), setTrumpColor(ps.getPartner().getBid().getSuit()), id("RecursionWJSimple.recursionFindFitGame fit _Pass")),

                shows(Bid._3C, shape(7, 13), noFitMajor(), IS_ANY_JUMP, IS_REBID, IS_OPENER, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(7, 13), noFitMajor(), IS_ANY_JUMP, IS_REBID, IS_OPENER, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(7, 13), noFitMajor(), IS_ANY_JUMP, IS_REBID, IS_OPENER, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(7, 13), noFitMajor(), IS_ANY_JUMP, IS_REBID, IS_OPENER, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._2C, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Clubs), id("RecursionWJSimple.recursionFindFitGame  _2C")),
                shows(Bid._2D, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Diamonds), id("RecursionWJSimple.recursionFindFitGame  _2D")),
                shows(Bid._2H, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Clubs), id("RecursionWJSimple.recursionFindFitGame  _2H")),
                shows(Bid._2S, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Diamonds), id("RecursionWJSimple.recursionFindFitGame  _2S")),

                shows(Bid._2H, shape(6, 13), noFitMajor(), IS_REBID, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _2H")),
                shows(Bid._2S, shape(6, 13), noFitMajor(), IS_REBID, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _2S")),
                shows(Bid._2C, shape(6, 13), id("RecursionWJSimple.recursionFindFitGame IS_REBID _2C")),
                shows(Bid._2D, shape(6, 13), IS_REBID, IS_OPENER, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _2D")),

                shows(Bid._2H, shape(5, 13), IS_NEW_SUIT, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _2H")),
                shows(Bid._2S, shape(5, 13), IS_NEW_SUIT, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _2S")),

                shows(Bid._2C, shape(5, 13), ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _2C")),
                shows(Bid._2D, shape(5, 13), noFit(), IS_NEW_SUIT, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _2D")),

                shows(Bid._2H, shape(4, 13), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _2H")),
                shows(Bid._2S, shape(4, 13), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _2S")),

                shows(Bid._3C, shape(6, 13), noFit(), not(suitBidCount(2)), IS_NON_JUMP, IS_REBID, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(6, 13), noFit(), not(suitBidCount(2)), IS_NON_JUMP, IS_REBID, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(6, 13), noFit(), not(suitBidCount(2)), IS_NON_JUMP, IS_REBID, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(6, 13), noFit(), not(suitBidCount(2)), IS_NON_JUMP, IS_REBID, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._3C, shape(6, 13), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionWJSimple.recursionFindFitGame IS_REBID _3C")),
                shows(Bid._3D, shape(6, 13), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionWJSimple.recursionFindFitGame IS_REBID _3D")),
                shows(Bid._3H, shape(6, 13), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionWJSimple.recursionFindFitGame IS_REBID _3H")),
                shows(Bid._3S, shape(6, 13), partner(isLastBid(Bid._2NT)), IS_REBID, id("RecursionWJSimple.recursionFindFitGame IS_REBID _3S")),

                shows(Bid._3H, shape(5, 13), noFitMajor(), highCardPoints(7, 40), IS_NEW_SUIT, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _3H")),
                shows(Bid._3S, shape(5, 13), noFitMajor(), highCardPoints(7, 40), IS_NEW_SUIT, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _3S")),
                shows(Bid._3H, shape(5, 13), noFitMajor(), pairHighCardPoints(PAIR_GAME), IS_NEW_SUIT, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _3H")),
                shows(Bid._3S, shape(5, 13), noFitMajor(), pairHighCardPoints(PAIR_GAME), IS_NEW_SUIT, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _3S")),
                shows(Bid._3C, shape(5, 13), noFit(), IS_NEW_SUIT, IS_NON_JUMP, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _3C")),
                shows(Bid._3D, shape(5, 13), noFit(), IS_NEW_SUIT, IS_NON_JUMP, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT _3D")),

                shows(Bid._3H, shape(4, 13), highCardPoints(7, 10), not(IS_OPENER), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3H")),
                shows(Bid._3S, shape(4, 13), highCardPoints(7, 10), not(IS_OPENER), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3S")),
                shows(Bid._3H, shape(4, 13), IS_OPENER, noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3H")),
                shows(Bid._3S, shape(4, 13), IS_OPENER, noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, ruleShow(1), id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3S")),

                shows(Bid._3H, shape(4, 13), pairHighCardPoints(PAIR_GAME), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3H")),
                shows(Bid._3S, shape(4, 13), pairHighCardPoints(PAIR_GAME), noFitMajor(), IS_NEW_SUIT, DECENT_PLUS_SUIT, IS_NON_JUMP, id("RecursionWJSimple.recursionFindFitGame IS_NEW_SUIT DECENT_PLUS_SUIT _3S")),

                shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Clubs), id("RecursionWJSimple.recursionFindFitGame  _3C")),
                shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), fit(), setTrumpColor(Suit.Diamonds), id("RecursionWJSimple.recursionFindFitGame  _3D")),

                shows(Bid._4D, shape(5, 13), twoSuiter(5), hasShortness(0, 0), partner(isLastBid(Bid._3NT)), id("RecursionWJSimple.recursionFindFitGame  exit 1 _4D")),
                shows(Bid._4C, shape(5, 13), twoSuiter(5), hasShortness(0, 0), partner(isLastBid(Bid._3NT)), id("RecursionWJSimple.recursionFindFitGame  exit 1 _4C")),

                shows(Bid._4C, shape(6, 13), IS_REBID, noFit(7), NOT_BALANCED, NOT_PAIR_BALANCED, partner(isLastBid(Bid._3H, Bid._3S)), id("RecursionWJSimple.recursionFindFitGame exit 2 _4C")),
                shows(Bid._4D, shape(6, 13), IS_REBID, noFit(7), NOT_BALANCED, NOT_PAIR_BALANCED, partner(isLastBid(Bid._3H, Bid._3S)), id("RecursionWJSimple.recursionFindFitGame exit 2 _4D")),

                CompeteWJSimple.bids(ps)
        );
        return choices;
    }
}
