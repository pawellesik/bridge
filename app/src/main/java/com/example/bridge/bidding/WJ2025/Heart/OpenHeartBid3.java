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


    public static final Range OPEN_STRONG = new Range(15, 17);
    public static final Range OPEN_LOW = new Range(15, 17);

    public static PositionCalls heart_1S_1NT_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._2D, ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2C _2D")),
                properties(new Call[]{Bid._2D}, RespondHeartBid3::heart_1S_1NT_2C_2D, false)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }
    public static PositionCalls heart_1S_1NT_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _3NT")),
                shows(Bid._3S, shape(3, 3), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _3H")),
                shows(Bid._2NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_LOW), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _2NT")),
                shows(Bid._2H, shape(Suit.Spades, 0, 2), not(stopped(Suit.Clubs)), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _2H")),
                shows(Bid._2S, shape(3, 3), highCardPoints(OPEN_LOW), ruleShow(1), id("OpenHeartBid3.heart_1S_1NT_2D _2S")),
                properties(new Call[]{Bid._2S}, RespondHeartBid3::heart_1S_1NT_2D_2S, true)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                //todo
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }



}
