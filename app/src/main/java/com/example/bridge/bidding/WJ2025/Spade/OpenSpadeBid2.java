package com.example.bridge.bidding.WJ2025.Spade;

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

public class OpenSpadeBid2 extends WJ2025 {

    public static final Range OPEN_12_14 = new Range(12, 14);
    public static final Range OPEN_15_17 = new Range(15, 17);
    public static final Range OPEN_12_17 = new Range(12, 17);


    public static PositionCalls spade_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_4H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(BlackwoodWJ2025.initiateConvention(ps));
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(BlackwoodWJ2025.initiateConvention(ps));
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_4D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(BlackwoodWJ2025.initiateConvention(ps));
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4S, highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_2NT _4S")),
                shows(Bid._3S, highCardPoints(OPEN_12_14), ruleShow(1), id("OpenSpadeBid2.spade_2NT _3S")),
                properties(new Call[]{Bid._3S}, RespondSpadeBid2::spade_2NT_3S, true),

                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4H, fit(), highCardPoints(OPEN_12_14), shape(Suit.Diamonds, 2, 13), shape(Suit.Clubs, 2, 13), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenSpadeBid2.spade_2H _4H")),
                //shows(Bid._4H, fit(), highCardPoints(OPEN_12_14), setTrumpColor(Suit.Hearts), id("OpenSpadeBid2.spade_2H _4H")),

                shows(Bid._4C, shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_15_17), shape(Suit.Clubs, 0, 1), setTrumpColor(Suit.Hearts), ruleShow(1), note("splinter"), id("OpenSpadeBid2.spade_2H _4C")),
                shows(Bid._4D, shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_15_17), shape(Suit.Diamonds, 0, 1), setTrumpColor(Suit.Hearts), ruleShow(1), note("splinter"), id("OpenSpadeBid2.spade_2H _4C")),

                shows(Bid._3NT, shape(Suit.Hearts, 3, 13), highCardPoints(OPEN_12_14), shape(Suit.Diamonds, 0, 1), ruleShow(1), id("OpenSpadeBid2.spade_2H _3NT")),
                shows(Bid._3NT, shape(Suit.Hearts, 3, 13), highCardPoints(OPEN_12_14), shape(Suit.Clubs, 0, 1), ruleShow(1), id("OpenSpadeBid2.spade_2H _3NT")),

                shows(Bid._3H, shape(Suit.Hearts, 3, 13), highCardPoints(OPEN_15_17), hasShortness(0, 1), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenSpadeBid2.spade_2H _3H")),
                shows(Bid._3H, shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_15_17), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenSpadeBid2.spade_2H _3H")),

                shows(Bid._3C, shape(Suit.Clubs, 4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_2H _3C")),
                shows(Bid._3D, shape(Suit.Diamonds, 4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_2H _3D")),

                shows(Bid._2NT, highCardPoints(OPEN_15_17), BALANCED, ruleShow(1), id("OpenSpadeBid2.spade_2H _2NT")),

                shows(Bid._2S, highCardPoints(OPEN_12_14), shape(Suit.Hearts, 0, 2), ruleShow(1), id("OpenSpadeBid2.spade_2H _2S")),

                properties(new Call[]{Bid._3NT}, RespondSpadeBid2::spade_2H_3NT, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(0, 1), shape(Suit.Diamonds, 4, 13), setTrumpColor(Suit.Diamonds), highCardPoints(OPEN_15_17), ruleShow(1), note("splinter"), id("OpenSpadeBid2.spade_2D _3H")),
                shows(Bid._3S, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_2D _3S")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Spades, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_2D _3C")),
                shows(Bid._3D, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_2D _3H")),
                shows(Bid._2NT, highCardPoints(OPEN_15_17), BALANCED, ruleShow(1), id("OpenSpadeBid2.spade_2D _2NT")),
                shows(Bid._2H, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), DECENT_PLUS_SUIT, id("OpenSpadeBid2.spade_2D _2H")),
                shows(Bid._2S, highCardPoints(OPEN_12_14), shape(Suit.Clubs, 0, 3), ruleShow(1), id("OpenSpadeBid2.spade_2D _2S")),

                properties(new Call[]{Bid._2S}, RespondSpadeBid2::spade_2D_2S, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3H, shape(0, 1), shape(Suit.Clubs, 4, 13), setTrumpColor(Suit.Clubs), highCardPoints(OPEN_15_17), ruleShow(1), note("splinter"), id("OpenSpadeBid2.diamond_1H _3H")),
                shows(Bid._3S, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.diamond_1H _3S")),
                shows(Bid._3D, shape(5, 13), shape(Suit.Spades, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.diamond_1H _3D")),
                shows(Bid._3C, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.diamond_1H _3C")),
                shows(Bid._2NT, highCardPoints(OPEN_15_17), BALANCED, ruleShow(1), id("OpenSpadeBid2.diamond_1H _2NT")),
                shows(Bid._2H, shape(4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.diamond_1H _2H")),
                shows(Bid._2S, highCardPoints(OPEN_12_14), shape(Suit.Diamonds, 0, 3), ruleShow(1), id("OpenSpadeBid2.diamond_1H _2S")),
                shows(Bid._2D, shape(4, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenSpadeBid2.diamond_1H _2D")),

                properties(new Call[]{Bid._2D}, RespondSpadeBid2::spade_2C_2D, false),
                properties(new Call[]{Bid._2S}, RespondSpadeBid2::spade_2C_2S, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }


    public static PositionCalls spade_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_1NT _3S")),

                shows(Bid._3C, shape(5, 13), shape(Suit.Spades, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_1NT _3C")),
                shows(Bid._3D, shape(5, 13), shape(Suit.Spades, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_1NT _3D")),

                shows(Bid._2NT, shape5332(), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_1NT _3D")),
                shows(Bid._2H, shape(4, 13), shape(Suit.Spades, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenSpadeBid2.spade_1NT _2H")),

                shows(Bid._2S, shape(6, 13), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenSpadeBid2.spade_1NT _2S")),

                shows(Bid._2C, shape(4, 13), shape(Suit.Spades, 5, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenSpadeBid2.spade_1NT _2C")),
                shows(Bid._2D, shape(4, 13), shape(Suit.Spades, 5, 13), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenSpadeBid2.spade_1NT _2D")),

                shows(Call.PASS, shape(4, 13), shape5332(), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenSpadeBid2.spade_1NT _pass")),

                properties(new Call[]{Bid._2C}, RespondSpadeBid2::spade_1NT_2C, false),
                properties(new Call[]{Bid._2D}, RespondSpadeBid2::spade_1NT_2D, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }


}
