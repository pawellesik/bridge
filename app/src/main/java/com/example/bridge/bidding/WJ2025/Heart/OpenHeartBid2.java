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

    public static final Range OPEN_STANDARD = new Range(12, 17);
    public static final Range OPEN_WEAK = new Range(12, 14);
    public static final Range OPEN_WEAK_GAME = new Range(12, 13);
    public static final Range OPEN_STRONG = new Range(15, 17);
    public static final Range OPEN_STRONG_FORSING = new Range(16, 17);
    public static final Range OPEN_NT = new Range(14, 17);


    public static PositionCalls heart_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

    public static PositionCalls heart_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
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
                //shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),
                //properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
        );
        return choices;
    }

}
