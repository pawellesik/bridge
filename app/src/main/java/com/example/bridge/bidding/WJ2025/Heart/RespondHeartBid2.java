package com.example.bridge.bidding.WJ2025.Heart;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.Diamond.OpenDiamondBid3;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondHeartBid2 extends WJ2025 {

    public static final Range RESPOND_7_9 = new Range(7, 9);
    public static final Range RESPOND_7_10 = new Range(7, 10);
    public static final Range RESPOND_10_12 = new Range(10, 12);
    public static final Range RESPOND_8_11 = new Range(8, 11);
    public static final Range RESPOND_9_11 = new Range(9, 11);
    public static final Range RESPOND_12_28 = new Range(12, 28);
    public static final Range RESPOND_13_28 = new Range(13, 28);
    public static final Range RESPOND_15_28 = new Range(15, 28);
    public static final Range RESPOND_9_12 = new Range(9, 12);


    public static PositionCalls heart_1S_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(RESPOND_10_12), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3S")),
                shows(Bid._3H, shape(3, 13), highCardPoints(RESPOND_10_12), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3H")),//to chyba niemozliwe

                shows(Bid._3D, shape(5, 13), shape(Suit.Spades, 5, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3D")),

                shows(Bid._3C, shape(6, 13), shape(Suit.Spades, 4, 13), highCardPoints(RESPOND_7_9), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3D")),

                shows(Bid._2S, shape(5, 13), highCardPoints(RESPOND_7_9), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2S")),
                shows(Bid._2H, shape(3, 13), shape(Suit.Spades, 5, 13), highCardPoints(RESPOND_7_9), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2H")),//to chyba niemozliwe

                shows(Bid._2D, highCardPoints(RESPOND_13_28), note("strong checkback"), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2D")),
                shows(Bid._2C, highCardPoints(RESPOND_10_12), note("weak checkback"), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2C")),

                properties(new Call[]{Bid._2C}, OpenHeartBid3::heart_1S_1NT_2C, true),
                properties(new Call[]{Bid._2D}, OpenHeartBid3::heart_1S_1NT_2D, true)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(5, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _3S")),
                shows(Bid._3H, shape(2,3), highCardPoints(RESPOND_9_12), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _3H")),
                shows(Bid._3C, shape(4, 13), highCardPoints(RESPOND_9_12), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _3C")),
                shows(Bid._2S, shape(5, 13), highCardPoints(RESPOND_9_11), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2S")),
                shows(Bid._2H, shape(2, 3), highCardPoints(RESPOND_7_10), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2H")),
                shows(Bid._2D, shape(4, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2D")),
                shows(Bid._2NT, BALANCED, highCardPoints(RESPOND_9_12), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2NT")),
                shows(Bid._2NT, PAIR_BALANCED, highCardPoints(RESPOND_9_12), id("RespondHeartBid2.heart_1S_2C _2NT")),
                properties(new Call[]{Bid._2D}, OpenHeartBid3::heart_1S_2C_2D, false),
                properties(new Call[]{Bid._2H}, OpenHeartBid3::heart_1S_2C_2H, false)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2NT, highCardPoints(RESPOND_15_28), ruleShow(1), id("RespondHeartBid2.heart_1S_2H _2NT")),
                shows(Bid._3S, shape(6, 13), ruleShow(1), highCardPoints(RESPOND_12_28), id("RespondHeartBid2.heart_1S_2H _2S")),
                shows(Bid._2S, noFit(), shape(5, 13), ruleShow(1), highCardPoints(RESPOND_9_11), DECENT_PLUS_SUIT, id("RespondHeartBid2.heart_1S_2H _2S")),
                properties(new Call[]{Bid._2NT}, OpenHeartBid3::heart_1S_2H_2NT, true)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(5, 13), ruleShow(1), id("RespondHeartBid2.heart_1S_3C _3S")),
                shows(Bid._3H, shape(3, 3), ruleShow(1), id("RespondHeartBid2.heart_1S_3C _3H")),
                shows(Bid._3D, shape(4, 13), ruleShow(1), id("RespondHeartBid2.heart_1S_3C _3D"))

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(5, 13), ruleShow(1), id("RespondHeartBid2.heart_1S_3C _3S")),
                shows(Bid._3H, shape(3, 3), ruleShow(1), id("RespondHeartBid2.heart_1S_3C _3H"))
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1NT_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3C, shape(4, 13), highCardPoints(RESPOND_8_11), ruleShow(1), id("RespondHeartBid2.heart_1S_3C _3S")),
                shows(Bid._2S, shape(Suit.Clubs, 5, 13), highCardPoints(RESPOND_10_12), ruleShow(1), id("RespondHeartBid2.heart_1S_3C _2S"))
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2H, fit(), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_2C_2D2H _2H")),
                shows(Bid._3H, fit(), shape(Suit.Clubs, 5, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_2C_2D2H _2H")),
                shows(Bid._3C, shape(6, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_2C_2D2H _3C")),
                shows(Bid._2S, shape(4, 13), ruleShow(1), highCardPoints(RESPOND_12_28), id("RespondHeartBid2.heart_2C_2D2H _2NT")),
                shows(Bid._2NT, highCardPoints(RESPOND_12_28), ruleShow(1), note("forcing"), id("RespondHeartBid2.heart_2C_2D2H _2NT")),
                properties(new Call[]{Bid._2NT}, OpenHeartBid3::heart_2C_2D_2NT, true)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2C_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3C, shape(6, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_2C_2D2H _3C")),
                shows(Bid._2S, shape(4, 13), ruleShow(1), highCardPoints(RESPOND_12_28), id("RespondHeartBid2.heart_2C_2D2H _2S")),
                shows(Bid._2NT, highCardPoints(RESPOND_12_28), ruleShow(1), note("forcing"), id("RespondHeartBid2.heart_2C_2D2H _2NT")),
                properties(new Call[]{Bid._2NT}, OpenHeartBid3::heart_2C_2H_2NT, false)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2D_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, fit(), shape(Suit.Diamonds, 5, 13), ruleShow(1), id("RespondHeartBid2.heart_2D_2H _3D")),
                shows(Bid._3D, shape(6, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondHeartBid2.heart_2D_2H _3D")),
                shows(Bid._2S, shape(4, 13), ruleShow(1), highCardPoints(RESPOND_12_28), id("RespondHeartBid2.heart_2D_2H _2S")),
                shows(Bid._2NT, highCardPoints(RESPOND_12_28), ruleShow(1), note("forcing"), id("RespondHeartBid2.heart_2D_2H _2NT")),
                properties(new Call[]{Bid._2NT}, OpenHeartBid3::heart_2D_2H_2NT, false)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }
}
