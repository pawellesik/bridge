package com.example.bridge.bidding.WJ2025.Heart;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.Diamond.OpenDiamondBid2;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondHeartBid1 extends WJ2025 {

    public static final Range RESPOND_9_14 = new Range(9, 14);
    public static final Range RESPOND_12_16 = new Range(12, 16);
    public static final Range RESPOND_10_11 = new Range(10, 11);
    public static final Range RESPOND_7_9 = new Range(7, 9);
    public static final Range RESPOND_12_28 = new Range(12, 28);
    public static final Range RESPOND_7_11 = new Range(7, 11);
    public static final Range RESPOND_7_28 = new Range(7, 28);

    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);


        choices.addRules(
                //shows(Bid._3NT, shape(Suit.Hearts, 4, 13), highCardPoints(RESPOND_9_14), DECENT_PLUS_SUIT, ruleShow(1), id("RespondHeartBid1.oneHeart _3NT")),

                shows(Bid._3S, shape(Suit.Hearts, 4, 13), shape(Suit.Spades, 0, 1), setTrumpColor(Suit.Hearts), highCardPoints(RESPOND_12_16), ruleShow(1), note("(splinter)"), id("RespondHeartBid1.oneHeart _3S")),
                shows(Bid._4C, shape(Suit.Hearts, 4, 13), shape(Suit.Clubs, 0, 1), setTrumpColor(Suit.Hearts), highCardPoints(RESPOND_12_16), ruleShow(1), note("(splinter)"), id("RespondHeartBid1.oneHeart _4C")),
                shows(Bid._4D, shape(Suit.Hearts, 4, 13), shape(Suit.Diamonds, 0, 1), setTrumpColor(Suit.Hearts), highCardPoints(RESPOND_12_16), ruleShow(1), note("(splinter)"), id("RespondHeartBid1.oneHeart _4D")),

                shows(Bid._3D, shape(6, 13), highCardPoints(RESPOND_10_11), ruleShow(1), id("RespondHeartBid1.oneHeart _3D")),
                shows(Bid._3C, shape(6, 13), highCardPoints(RESPOND_10_11), ruleShow(1), id("RespondHeartBid1.oneHeart _3C")),

                shows(Bid._2NT, shape(Suit.Hearts, 3, 13), setTrumpColor(Suit.Hearts), highCardPoints(RESPOND_10_11), ruleShow(1), id("RespondHeartBid1.oneHeart _2NT")),

                shows(Bid._2S, shape(6, 13), highCardPoints(RESPOND_7_9), ruleShow(1), id("RespondHeartBid1.oneHeart _2S")),

                shows(Bid._2H, fit(), highCardPoints(RESPOND_7_9), ruleShow(1), id("RespondHeartBid1.oneHeart _2H")),

                shows(Bid._2D, shape(5, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid1.oneHeart _2D")),

                shows(Bid._2C, shape(2, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid1.oneHeart _2C")),

                shows(Bid._1NT, shape(Suit.Hearts, 0, 2), highCardPoints(RESPOND_7_11), ruleShow(1), id("RespondHeartBid1.oneHeart _1NT")),

                shows(Bid._1S, shape(4, 13), highCardPoints(RESPOND_7_28), id("RespondHeartBid1.oneHeart _1S")),

                properties(new Call[]{Bid._3NT}, OpenHeartBid2::heart_3NT, false),
                properties(new Call[]{Bid._3S}, OpenHeartBid2::heart_3S, false),
                properties(new Call[]{Bid._4C}, OpenHeartBid2::heart_4C, false),
                properties(new Call[]{Bid._4D}, OpenHeartBid2::heart_4D, false),
                properties(new Call[]{Bid._3D}, OpenHeartBid2::heart_3D, false),
                properties(new Call[]{Bid._3C}, OpenHeartBid2::heart_3C, false),
                properties(new Call[]{Bid._2NT}, OpenHeartBid2::heart_2NT, false),
                properties(new Call[]{Bid._2S}, OpenHeartBid2::heart_2S, false),
                properties(new Call[]{Bid._2H}, OpenHeartBid2::heart_2H, false),
                properties(new Call[]{Bid._2D}, OpenHeartBid2::heart_2D, false),
                properties(new Call[]{Bid._2C}, OpenHeartBid2::heart_2C, false),
                properties(new Call[]{Bid._1NT}, OpenHeartBid2::heart_1NT, false),
                properties(new Call[]{Bid._1S}, OpenHeartBid2::heart_1S, false)

        );
        return choices;
    }


}
