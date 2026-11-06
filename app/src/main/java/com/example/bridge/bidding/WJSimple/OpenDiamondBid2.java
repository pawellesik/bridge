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
    public static final Range STANDARD = new Range(12, 17);

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

    //1D -->
    //    1H -->
    //          2C -->
    //              2S -->
    public static PositionCalls twoSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.twoSpade _3NT")),
                shows(Bid._3S, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 0, 3), not(stopped(Suit.Spades)), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.twoSpade _3S")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 3, 3), highCardPoints(STANDARD), ruleShow(1), id("OpenDiamondBid2.twoSpade _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 4, 4), not(stopped(Suit.Spades)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.twoSpade _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 5, 13), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.twoSpade _3D")),
                shows(Bid._2NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.twoSpade _3D"))


                );
        return choices;
    }

}
