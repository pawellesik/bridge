package com.example.bridge.bidding.WJ2025.Heart;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondHeartBid3 extends WJ2025 {


    public static final Range RESPOND_13_16 = new Range(13, 16);
    public static final Range RESPOND_10_12 = new Range(10, 12);
    public static final Range RESPOND_11_12 = new Range(11, 12);


    public static PositionCalls heart_1S_1NT_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, highCardPoints(RESPOND_13_16), shape5332(), ruleShow(1), id("RespondHeartBid3.heart_1S_1NT_2C_2D _3NT")),//nigdy nie zajdzie
                shows(Bid._3H, shape(4,13), shape(Suit.Spades, 5, 5), highCardPoints(RESPOND_10_12), ruleShow(1), id("RespondHeartBid3.heart_1S_1NT_2C_2D _3H")),//nigdy nie zajdzie
                shows(Bid._3C, shape(5,13), shape(Suit.Spades, 4, 4), highCardPoints(RESPOND_10_12), ruleShow(1), id("RespondHeartBid3.heart_1S_1NT_2C_2D _3C")),
                shows(Bid._2NT, shape(Suit.Spades, 5, 5), BALANCED, highCardPoints(RESPOND_11_12), ruleShow(1), id("RespondHeartBid3.heart_1S_1NT_2C_2D _2NT")),
                shows(Bid._2S, shape(5, 13), highCardPoints(RESPOND_10_12), ruleShow(1), id("RespondHeartBid3.heart_1S_1NT_2C_2D _2S")),
                shows(Bid._2H, shape(2, 2), highCardPoints(RESPOND_10_12), id("RespondHeartBid3.heart_1S_1NT_2C_2D _2S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_1NT_2D_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, PAIR_BALANCED, id("RespondHeartBid3.heart_1S_1NT_2D_2S 3NT")),
                shows(Bid._4S, fit(7), id("RespondHeartBid3.heart_1S_1NT_2D_2S 4S")),
                shows(Bid._4H, fit(7), id("RespondHeartBid3.heart_1S_1NT_2D_2S 4H")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_1NT_2D_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, PAIR_BALANCED, stopped(Suit.Clubs), id("RespondHeartBid3.heart_1S_1NT_2D_2S 3NT")),
                shows(Bid._4H, fit(7), id("RespondHeartBid3.heart_1S_1NT_2D_2S 4H")),
                shows(Bid._4S, fit(7), id("RespondHeartBid3.heart_1S_1NT_2D_2S 4S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2C_2D_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, PAIR_BALANCED, stopped(Suit.Diamonds), id("RespondHeartBid3.heart_1S_2C_2D_2H 3NT")),
                shows(Bid._4H, fit(7), id("RespondHeartBid3.heart_1S_2C_2D_2H 4H")),
                shows(Bid._4S, fit(7), id("RespondHeartBid3.heart_1S_2C_2D_2H 4S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }
    public static PositionCalls heart_1S_2C_2D_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, PAIR_BALANCED, stopped(Suit.Clubs), id("RespondHeartBid3.heart_1S_2C_2D_2H 3NT")),
                shows(Bid._4H, fit(7), id("RespondHeartBid3.heart_1S_2C_2D_2H 4H")),
                shows(Bid._4S, fit(7), id("RespondHeartBid3.heart_1S_2C_2D_2H 4S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls heart_1S_2C_2D_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, BALANCED,  id("RespondHeartBid3.heart_1S_2C_2D_2H 3NT")),
                shows(Bid._4H, fit(7), id("RespondHeartBid3.heart_1S_2C_2D_2H 4H")),
                shows(Bid._4S, fit(7), id("RespondHeartBid3.heart_1S_2C_2D_2H 4S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }
}
