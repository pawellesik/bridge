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
import com.example.bridge.bidding.WJ2025.WJ2025;

public class OpenHeartBid2 extends WJ2025 {

    public static final Range OPEN_STRONG = new Range(15, 17);
    public static final Range OPEN_STANDARD = new Range(12, 17);
    public static final Range OPEN_WEAK = new Range(12, 14);



    public static final Range OPEN_WEAK_GAME = new Range(12, 13);
    public static final Range OPEN_STRONG_FORSING = new Range(16, 17);
    public static final Range OPEN_NT = new Range(14, 17);


    public static PositionCalls heart_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenHeartBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenHeartBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_4D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }


    public static PositionCalls heart_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_1S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4C, shape(Suit.Spades, 4, 13), shape(Suit.Clubs, 0, 1), setTrumpColor(Suit.Spades), highCardPoints(OPEN_STRONG), ruleShow(1), note("(splinter)"), id("OpenHeartBid2.heart_1S _4C")),
                shows(Bid._4D, shape(Suit.Spades, 4, 13), shape(Suit.Diamonds, 0, 1), setTrumpColor(Suit.Spades), highCardPoints(OPEN_STRONG), ruleShow(1), note("(splinter)"), id("OpenHeartBid2.heart_1S _4D")),

                shows(Bid._3S, fit(), highCardPoints(OPEN_STRONG), setTrumpColor(Suit.Spades), ruleShow(1), id("OpenHeartBid2.heart_1S _3S")),
                shows(Bid._3S, fit(), shape(Suit.Hearts, 6, 13), highCardPoints(OPEN_STANDARD), setTrumpColor(Suit.Spades), id("OpenHeartBid2.heart_1S _3S")),

                shows(Bid._3C, shape(5, 13), noFit(), highCardPoints(OPEN_STRONG), id("OpenHeartBid2.heart_1S _3C")),
                shows(Bid._3D, shape(5, 13), noFit(), highCardPoints(OPEN_STRONG), id("OpenHeartBid2.heart_1S _3D")),

                shows(Bid._2NT, noFit(), shape5332(Suit.Hearts), highCardPoints(OPEN_STRONG), id("OpenHeartBid2.heart_1S _2NT")),

                shows(Bid._2S, fit(), highCardPoints(OPEN_WEAK), id("OpenHeartBid2.heart_1S _2S")),

                shows(Bid._2H, shape(6, 13), noFit(), highCardPoints(OPEN_WEAK), id("OpenHeartBid2.heart_1S _2H")),

                shows(Bid._2C, shape(4, 13), noFit(), highCardPoints(OPEN_STANDARD), id("OpenHeartBid2.heart_1S _2C")),
                shows(Bid._2D, shape(4, 13), noFit(), highCardPoints(OPEN_STANDARD), id("OpenHeartBid2.heart_1S _2D")),

                shows(Bid._1NT, shape(4, 13), noFit(), shape5332(Suit.Hearts), highCardPoints(OPEN_STANDARD), id("OpenHeartBid2.heart_1S _1NT")),

                properties(new Call[]{Bid._1NT}, RespondHeartBid2::heart_1S_1NT, false),
                properties(new Call[]{Bid._2C}, RespondHeartBid2::heart_1S_2C, false)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

}
