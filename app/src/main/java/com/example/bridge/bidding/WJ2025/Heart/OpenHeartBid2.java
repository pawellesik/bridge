package com.example.bridge.bidding.WJ2025.Heart;

import com.example.bridge.bidding.Conventions.Blackwood;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.Diamond.RespondDiamondBid2;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class OpenHeartBid2 extends WJ2025 {

    public static final Range OPEN_12_14 = new Range(12, 14);
    public static final Range OPEN_15_17 = new Range(15, 17);
    public static final Range OPEN_12_17 = new Range(12, 17);


    public static PositionCalls heart_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_4D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4H, highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_2NT _4H")),
                shows(Bid._3H, ruleShow(1), id("OpenDiamondBid2.heart_2NT _3H")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, shape(0, 1), shape(Suit.Diamonds, 4, 13), setTrumpColor(Suit.Diamonds), highCardPoints(OPEN_15_17), ruleShow(1), note("splinter"), id("OpenDiamondBid2.heart_2D _3S")),
                shows(Bid._3H, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_2D _3H")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Hearts, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_2D _3C")),
                shows(Bid._3D, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_2D _3H")),
                shows(Bid._2NT, highCardPoints(OPEN_15_17), BALANCED, ruleShow(1), id("OpenDiamondBid2.heart_2D _2NT")),
                shows(Bid._2S, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_2D _2S")),
                shows(Bid._2H, highCardPoints(OPEN_12_14), shape(Suit.Clubs, 0, 3), ruleShow(1), id("OpenDiamondBid2.heart_2D _2H")),

                properties(new Call[]{Bid._2H}, RespondHeartBid2::heart_2D_2H, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, shape(0, 1), shape(Suit.Clubs, 4, 13), setTrumpColor(Suit.Clubs), highCardPoints(OPEN_15_17), ruleShow(1), note("splinter"), id("OpenDiamondBid2.diamond_1H _3S")),
                shows(Bid._3H, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3H")),
                shows(Bid._3D, shape(5, 13), shape(Suit.Hearts, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3H")),
                shows(Bid._3C, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3C")),
                shows(Bid._2NT, highCardPoints(OPEN_15_17), BALANCED, ruleShow(1), id("OpenDiamondBid2.diamond_1H _2NT")),
                shows(Bid._2S, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _2S")),
                shows(Bid._2H, highCardPoints(OPEN_12_14), shape(Suit.Diamonds, 0, 3), ruleShow(1), id("OpenDiamondBid2.diamond_1H _2H")),
                shows(Bid._2D, shape(4, 13), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid2.diamond_1H _2D")),

                properties(new Call[]{Bid._2D}, RespondHeartBid2::heart_2C_2D, false),
                properties(new Call[]{Bid._2H}, RespondHeartBid2::heart_2C_2H, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }


    public static PositionCalls heart_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_1NT _3H")),

                shows(Bid._3C, shape(5, 13), shape(Suit.Hearts, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_1NT _3C")),
                shows(Bid._3D, shape(5, 13), shape(Suit.Hearts, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_1NT _3D")),

                shows(Bid._2NT, shape5332(), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_1NT _3D")),
                shows(Bid._2S, shape(4, 13), shape(Suit.Hearts, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.heart_1NT _3D")),

                shows(Bid._2C, shape(4, 13), shape(Suit.Hearts, 5, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenDiamondBid2.heart_1NT _2C")),
                shows(Bid._2D, shape(4, 13), shape(Suit.Hearts, 5, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenDiamondBid2.heart_1NT _2D")),

                shows(Call.PASS, shape(4, 13), shape5332(), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid2.heart_1NT _pass")),

                properties(new Call[]{Bid._2C}, RespondHeartBid2::heart_1NT_2C, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4C, shape(Suit.Spades, 4, 13), shape(Suit.Clubs, 0, 1), setTrumpColor(Suit.Spades), highCardPoints(OPEN_15_17), ruleShow(1), note("(splinter)"), id("OpenHeartBid2.heart_1S _4C")),
                shows(Bid._4D, shape(Suit.Spades, 4, 13), shape(Suit.Diamonds, 0, 1), setTrumpColor(Suit.Spades), highCardPoints(OPEN_15_17), ruleShow(1), note("(splinter)"), id("OpenHeartBid2.heart_1S _4D")),

                shows(Bid._3S, fit(), highCardPoints(OPEN_15_17), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenHeartBid2.heart_1S _3S")),

                shows(Bid._3C, shape(5, 13), noFit(), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid2.heart_1S _3C")),
                shows(Bid._2C, shape(4, 13), noFit(), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid2.heart_1S _2C")),

                shows(Bid._3H, shape(Suit.Hearts, 6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid2.heart_1S _3H")),

                shows(Bid._3D, shape(5, 13), noFit(), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid2.heart_1S _3D")),

                shows(Bid._2NT, noFit(), shape5332(Suit.Hearts), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid2.heart_1S _2NT")),

                shows(Bid._2S, fit(), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid2.heart_1S _2S")),

                shows(Bid._2C, shape(6, 13), noFit(), highCardPoints(OPEN_12_17), id("OpenHeartBid2.heart_1S _2C")),

                shows(Bid._2H, shape(6, 13), noFit(), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid2.heart_1S _2H")),

                shows(Bid._2C, shape(4, 13), noFit(), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid2.heart_1S _2C")),
                shows(Bid._2D, shape(4, 13), noFit(), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid2.heart_1S _2D")),

                shows(Bid._1NT, noFit(), shape5332(Suit.Hearts), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid2.heart_1S _1NT")),

                properties(new Call[]{Bid._1NT}, RespondHeartBid2::heart_1S_1NT, false),
                properties(new Call[]{Bid._2C}, RespondHeartBid2::heart_1S_2C, true),
                properties(new Call[]{Bid._2H}, RespondHeartBid2::heart_1S_2H, false),
                properties(new Call[]{Bid._3C}, RespondHeartBid2::heart_1S_3C, false),
                properties(new Call[]{Bid._3D}, RespondHeartBid2::heart_1S_3D, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

}
