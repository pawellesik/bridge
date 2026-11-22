package com.example.bridge.bidding.WJ2025.Spade;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondSpadeBid1 extends WJ2025 {

    public static final Range RESPOND_9_14 = new Range(9, 14);
    public static final Range RESPOND_12_16 = new Range(12, 16);
    public static final Range RESPOND_10_11 = new Range(10, 11);
    public static final Range RESPOND_7_9 = new Range(7, 9);
    public static final Range RESPOND_12_28 = new Range(12, 28);
    public static final Range RESPOND_15_28 = new Range(15, 28);
    public static final Range RESPOND_7_11 = new Range(7, 11);
    public static final Range RESPOND_7_28 = new Range(7, 28);

    public static PositionCalls oneSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);


        choices.addRules(
                //shows(Bid._3NT, shape(Suit.Spades, 4, 13), highCardPoints(RESPOND_9_14), DECENT_PLUS_SUIT, ruleShow(1), id("RespondSpadeBid1.oneSpade _3NT")),

                shows(Bid._3H, shape(Suit.Spades, 4, 13), shape(Suit.Hearts, 0, 1), setTrumpColor(Suit.Spades), highCardPoints(RESPOND_12_16), ruleShow(1), note("(splinter)"), id("RespondSpadeBid1.oneSpade _3H")),
                shows(Bid._4C, shape(Suit.Spades, 4, 13), shape(Suit.Clubs, 0, 1), setTrumpColor(Suit.Spades), highCardPoints(RESPOND_12_16), ruleShow(1), note("(splinter)"), id("RespondSpadeBid1.oneSpade _4C")),
                shows(Bid._4D, shape(Suit.Spades, 4, 13), shape(Suit.Diamonds, 0, 1), setTrumpColor(Suit.Spades), highCardPoints(RESPOND_12_16), ruleShow(1), note("(splinter)"), id("RespondSpadeBid1.oneSpade _4D")),

                shows(Bid._2NT, shape(Suit.Spades, 3, 13), setTrumpColor(Suit.Spades), highCardPoints(RESPOND_10_11), ruleShow(1), id("RespondSpadeBid1.oneSpade _2NT")),

                shows(Bid._3D, shape(6, 13), highCardPoints(RESPOND_10_11), ruleShow(1), id("RespondSpadeBid1.oneSpade _3D")),
                shows(Bid._3C, shape(6, 13), highCardPoints(RESPOND_10_11), ruleShow(1), id("RespondSpadeBid1.oneSpade _3C")),

                shows(Bid._2S, fit(), highCardPoints(RESPOND_7_9), ruleShow(1), id("RespondSpadeBid1.oneSpade _2S")),

                shows(Bid._2H, shape(5, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid1.oneSpade _2S")),

                shows(Bid._2D, shape(5, 13), highCardPoints(RESPOND_15_28),  id("RespondSpadeBid1.oneSpade _2D")),
                shows(Bid._2C, shape(2, 13), highCardPoints(RESPOND_15_28), note("forcing"), id("RespondSpadeBid1.oneSpade _2C")),

                shows(Bid._1S, shape(4, 13), highCardPoints(RESPOND_7_28), ruleShow(1), id("RespondSpadeBid1.oneSpade _1S")),

                shows(Bid._2D, shape(5, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid1.oneSpade _2D")),

                shows(Bid._2C, shape(2, 13), highCardPoints(RESPOND_12_28), ruleShow(1), id("RespondSpadeBid1.oneSpade _2C")),

                shows(Bid._1NT, shape(Suit.Spades, 0, 2), highCardPoints(RESPOND_7_11), ruleShow(1), id("RespondSpadeBid1.oneSpade _1NT")),

                //properties(new Call[]{Bid._3NT}, OpenSpadeBid2::spade_3NT, false),
                properties(new Call[]{Bid._3H}, OpenSpadeBid2::spade_3H, false),
                properties(new Call[]{Bid._4C}, OpenSpadeBid2::spade_4C, false),
                properties(new Call[]{Bid._4D}, OpenSpadeBid2::spade_4D, false),
                properties(new Call[]{Bid._3D}, OpenSpadeBid2::spade_3D, false),
                properties(new Call[]{Bid._3C}, OpenSpadeBid2::spade_3C, false),
                properties(new Call[]{Bid._2NT}, OpenSpadeBid2::spade_2NT, false),
                properties(new Call[]{Bid._2S}, OpenSpadeBid2::spade_2S, false),
                properties(new Call[]{Bid._2H}, OpenSpadeBid2::spade_2H, false),
                properties(new Call[]{Bid._2D}, OpenSpadeBid2::spade_2D, false),
                properties(new Call[]{Bid._2C}, OpenSpadeBid2::spade_2C, false),
                properties(new Call[]{Bid._1NT}, OpenSpadeBid2::spade_1NT, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        return choices;
    }


}
