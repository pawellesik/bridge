package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;

public class RespondDiamondBid2 extends WJSimple {

    public static final Range WEAK = new Range(10, 12);
    public static final Range STRONG = new Range(13, 40);
    public static final Range TO_GAME = new Range(7, 9);


    //1D -->
    //    1H -->
    //          1NT, 1S -->
    public static PositionCalls oneSpade1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade1NT _3H")),
                shows(Bid._3D, shape(4, 13), shape(Suit.Hearts, 4, 4), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade1NT _3H")),
                shows(Bid._3C, shape(6, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade1NT _3H")),
                shows(Bid._2D, highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneSpade1NT _3H")),
                shows(Bid._2C, highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade1NT _3H")),

                properties(new Call[]{Bid._2C}, OpenDiamondBid2::twoClub, true),
                properties(new Call[]{Bid._2D}, OpenDiamondBid2::twoDiamond, true)

                );
        return choices;
    }

    //1D -->
    //    1H -->
    //          2C -->
    public static PositionCalls twoClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.twoClub _3H")),
                shows(Bid._3D, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.twoClub _3D")),
                shows(Bid._3C, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.twoClub _3C")),
                shows(Bid._2NT, highCardPoints(WEAK), BALANCED, ruleShow(1), id("RespondDiamondBid2.twoClub _2NT")),
                shows(Bid._2S, shape(4,13), highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.twoClub _2S")),
                shows(Bid._2D, shape(4,13), betterThan(Suit.Diamonds, Suit.Clubs), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.twoClub _2D")),
                shows(Call.PASS, betterThan(Suit.Clubs, Suit.Diamonds), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.twoClub _pass")),

                propertiesForcingToGame(new Call[]{Bid._2S}, OpenDiamondBid2::twoSpade, true)

        );
        return choices;
    }

}
