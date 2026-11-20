package com.example.bridge.bidding.WJ2025.Heart;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class OpenHeartBid3 extends WJ2025 {

    public static final Range OPEN_12_14 = new Range(12, 14);
    public static final Range OPEN_15_17= new Range(15, 17);
    public static final Range OPEN_12_17= new Range(12, 17);

    public static PositionCalls heart_1S_1NT_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2D, ruleShow(1), note("weak checkback respond"), id("OpenHeartBid3.heart_1S_1NT_2C _2D")),
                properties(new Call[]{Bid._2D}, RespondHeartBid3::heart_1S_1NT_2C_2D, false)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_1NT_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _3NT")),
                shows(Bid._3S, shape(3, 3), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _3H")),
                shows(Bid._2NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _2NT")),
                shows(Bid._2H, shape(Suit.Spades, 0, 2), not(stopped(Suit.Clubs)), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _2H")),
                shows(Bid._2S, shape(3, 3), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _2S")),
                properties(new Call[]{Bid._2S}, RespondHeartBid3::heart_1S_1NT_2D_2S, true)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), stopped(Suit.Diamonds), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _3NT")),
                shows(Bid._3S, shape(Suit.Spades, 3, 3), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _3S")),
                shows(Bid._3H, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Spades, 0, 2), not(stopped(Suit.Diamonds)), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _3D")),
                shows(Bid._3C, shape(5, 13), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _3D")),
                shows(Bid._2NT, highCardPoints(OPEN_12_14), stopped(Suit.Diamonds), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _2NT")),
                shows(Bid._2S, shape(3, 3), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _2S")),
                shows(Bid._2H, shape(Suit.Spades, 0, 2), not(stopped(Suit.Diamonds)), ruleShow(1), id("OpenHeartBid3.heart_1S_2C_2D _2H"))
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2H_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 6, 6), shape(Suit.Spades, 0, 2), ruleShow(1), id("OpenHeartBid3.heart_1S_2H_2NT _3NT")),
                shows(Bid._3S, shape(Suit.Spades, 3, 3), ruleShow(1), id("OpenHeartBid3.heart_1S_2H_2NT _3S")),
                shows(Bid._3H, shape(7, 13), ruleShow(1), id("OpenHeartBid3.heart_1S_2H_2NT _3S")),
                shows(Bid._3D, shape(4, 13), ruleShow(1), id("OpenHeartBid3.heart_1S_2H_2NT _3D")),
                shows(Bid._3C, shape(4, 13), ruleShow(1), id("OpenHeartBid3.heart_1S_2H_2NT _3C"))
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2C_2D_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, SHAPE_5422, highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3NT")),
                shows(Bid._3S, shape(3, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3S")),
                shows(Bid._3H, shape(6, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3H")),
                shows(Bid._3D, shape(5, 13), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3D")),
                shows(Bid._3C, shape(3, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3C"))

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_2C_2H_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape(4, 13), shape(Suit.Spades, 2, 13), shape(Suit.Clubs, 2, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3NT")),
                shows(Bid._3S, shape(3, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3S")),
                shows(Bid._3H, shape(6, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3H")),
                shows(Bid._3D, shape(5, 13), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3D")),
                shows(Bid._3C, shape(3, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenHeartBid3.heart_2C_2D_2NT _3C"))

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

}
