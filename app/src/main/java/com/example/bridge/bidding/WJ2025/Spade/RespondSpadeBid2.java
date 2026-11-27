package com.example.bridge.bidding.WJ2025.Spade;

import com.example.bridge.bidding.Conventions.Blackwood;
import com.example.bridge.bidding.Conventions.BlackwoodWJ2025;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondSpadeBid2 extends WJ2025 {

    public static final Range RESPOND_10_11 = new Range(10, 11);
    public static final Range RESPOND_8_11 = new Range(8, 11);
    public static final Range RESPOND_12_28 = new Range(12, 28);


    public static PositionCalls spade_1NT_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2H, shape(Suit.Clubs, 5, 13), highCardPoints(RESPOND_10_11), setTrumpColor(Suit.Clubs), ruleShow(1), id("RespondSpadeBid2.spade_1S_3C _2S")),
                shows(Bid._3C, shape(4, 13), highCardPoints(RESPOND_8_11), setTrumpColor(Suit.Clubs), ruleShow(1), id("RespondSpadeBid2.spade_1S_3C _3S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_1NT_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2H, shape(Suit.Diamonds, 5, 13), highCardPoints(RESPOND_10_11), setTrumpColor(Suit.Diamonds), ruleShow(1), id("RespondSpadeBid2.spade_1S_3C _2S")),
                shows(Bid._3D, shape(4, 13), highCardPoints(RESPOND_8_11), setTrumpColor(Suit.Diamonds), ruleShow(1), id("RespondSpadeBid2.spade_1S_3C _3S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, fit(), shape(Suit.Clubs, 5, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid2.spade_2C_2D2H _2S")),
                shows(Bid._2S, fit(), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid2.spade_2C_2D2H _2S")),
                shows(Bid._3C, shape(6, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid2.spade_2C_2D2H _3C")),
                shows(Bid._2H, shape(4, 13), ruleShow(1), highCardPoints(RESPOND_12_28), id("RespondSpadeBid2.spade_2C_2D2H _2H")),
                shows(Bid._2NT, highCardPoints(RESPOND_12_28), ruleShow(1), note("forcing"), id("RespondSpadeBid2.spade_2C_2D2H _2NT")),
                properties(new Call[]{Bid._2NT}, OpenSpadeBid3::spade_2C_2D_2NT, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2C_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, fit(), shape(Suit.Clubs, 5, 13), ruleShow(1), id("RespondSpadeBid2.spade_2C_2H _3S")),
                shows(Bid._3C, shape(6, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid2.spade_2C_2D2H _3C")),
                shows(Bid._2NT, highCardPoints(RESPOND_12_28), ruleShow(1), note("forcing"), id("RespondSpadeBid2.spade_2C_2D2H _2NT")),
                properties(new Call[]{Bid._2NT}, OpenSpadeBid3::spade_2C_2H_2NT, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2D_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, fit(), shape(Suit.Diamonds, 5, 13), ruleShow(1), id("RespondSpadeBid2.spade_2D_2H _3S")),
                shows(Bid._3D, shape(6, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid2.spade_2D_2H _3D")),
                shows(Bid._2NT, highCardPoints(RESPOND_12_28), ruleShow(1), note("forcing"), id("RespondSpadeBid2.spade_2D_2H _2NT")),
                properties(new Call[]{Bid._2NT}, OpenSpadeBid3::spade_2D_2H_2NT, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2H_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4C, ruleShow(1), note("Asking for shortness"), id("RespondSpadeBid2.spade_2H_3NT _4C")),
                properties(new Call[]{Bid._4C}, OpenSpadeBid3::spade_2H_3NT_4C, true)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2NT_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS,  note("Asking for shortness"), id("RespondSpadeBid2.spade_2NT_3S _pass"))
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }
}
