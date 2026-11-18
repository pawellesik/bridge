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

    public static final Range RESPOND_WEAK = new Range(7, 9);
    public static final Range RESPOND_NORMAL = new Range(10, 12);
    public static final Range RESPOND_TO_GAME = new Range(12, 28);
    public static final Range RESPOND_STRONG = new Range(15, 28);

    public static PositionCalls heart_1S_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(RESPOND_NORMAL), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3S")),
                shows(Bid._3H, shape(3, 13), highCardPoints(RESPOND_NORMAL), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3H")),//to chyba niemozliwe

                shows(Bid._3D, shape(5, 13), shape(Suit.Spades, 5, 13), highCardPoints(RESPOND_TO_GAME), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3D")),

                shows(Bid._3C, shape(6, 13), shape(Suit.Spades, 4, 13), highCardPoints(RESPOND_WEAK), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _3D")),

                shows(Bid._2S, shape(5, 13), shape(Suit.Spades, 4, 13), highCardPoints(RESPOND_WEAK), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2S")),
                shows(Bid._2H, shape(3, 13), shape(Suit.Spades, 5, 13), highCardPoints(RESPOND_WEAK), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2H")),

                shows(Bid._2D, highCardPoints(RESPOND_TO_GAME), note("strong checkback"), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2D")),
                shows(Bid._2C, highCardPoints(RESPOND_NORMAL), note("weak checkback"), ruleShow(1), id("RespondHeartBid2.heart_1S_1NT _2C")),

                properties(new Call[]{Bid._2C}, OpenHeartBid3::heart_1S_1NT_2C, true),
                properties(new Call[]{Bid._2D}, OpenHeartBid3::heart_1S_1NT_2D, true)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(5, 13), highCardPoints(RESPOND_TO_GAME), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _3S")),
                shows(Bid._3H, shape(2, 2), highCardPoints(RESPOND_NORMAL), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _3H")),
                shows(Bid._3C, shape(4, 13), highCardPoints(RESPOND_NORMAL), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _3C")),
                shows(Bid._2NT, BALANCED, highCardPoints(RESPOND_NORMAL), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2NT")),
                shows(Bid._2NT, PAIR_BALANCED, highCardPoints(RESPOND_NORMAL), id("RespondHeartBid2.heart_1S_2C _2NT")),
                shows(Bid._2S, shape(5, 13), highCardPoints(RESPOND_WEAK), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2S")),
                shows(Bid._2H, shape(2, 3), highCardPoints(RESPOND_WEAK), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2H")),
                shows(Bid._2D, shape(4, 13), highCardPoints(RESPOND_TO_GAME), ruleShow(1), id("RespondHeartBid2.heart_1S_2C _2D")),
                properties(new Call[]{Bid._2D}, OpenHeartBid3::heart_1S_2C_2D, false)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2NT, highCardPoints(RESPOND_STRONG), ruleShow(1), id("RespondHeartBid2.heart_1S_2H _2NT")),
                shows(Bid._2S, shape(5, 13), ruleShow(1), id("RespondHeartBid2.heart_1S_2H _2S")),
                shows(Bid._3S, shape(6, 13), ruleShow(1), highCardPoints(RESPOND_TO_GAME), id("RespondHeartBid2.heart_1S_2H _2S")),
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
}
