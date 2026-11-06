package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;

public class OpenDiamondBid2 extends WJSimple {
    public static final Range HIGHT = new Range(15, 17);
    public static final Range LOW = new Range(12, 14);

    //1D -->
    //    1H -->
    //          1NT, 1S -->
    //                  2C -->
    public static PositionCalls twoClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2D, ruleShow(1), id("OpenDiamondBid2.twoClub _2D")),
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::twoDiamond, true)
        );
        return choices;
    }


    //1D -->
    //    1H -->
    //          1NT, 1S -->
    //                  2D -->
    public static PositionCalls twoDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.twoDiamond _3NT")),
                shows(Bid._3H, shape(Suit.Hearts, 3, 3), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.twoDiamond _3H")),
                shows(Bid._2NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.twoDiamond _2NT")),
                shows(Bid._2S, shape(Suit.Hearts, 0, 2), not(stopped(Suit.Clubs)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.twoDiamond _2S")),
                shows(Bid._2H, shape(3, 3), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.twoDiamond _2H"))

        );
        return choices;
    }


}
