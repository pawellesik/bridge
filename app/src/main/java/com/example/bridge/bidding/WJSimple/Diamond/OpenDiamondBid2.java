package com.example.bridge.bidding.WJSimple.Diamond;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;
import com.example.bridge.bidding.WJSimple.WJSimple;

public class OpenDiamondBid2 extends WJSimple {

    public static final Range OPEN_STANDARD = new Range(12, 17);
    public static final Range OPEN_WEAK = new Range(12, 14);
    public static final Range OPEN_STRONG = new Range(15, 17);
    public static final Range OPEN_NT = new Range(14, 17);


    //  1D -->  1H
    //
    public static PositionCalls diamond_1H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 7, 13), quality(Suit.Diamonds, SuitQuality.Excellent, SuitQuality.Solid), highCardPoints(OPEN_NT), ruleShow(1), id("OpenDiamondBid2.oneHeart _3NT")),
                shows(Bid._3S, shape(0, 1), shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3S")),
                shows(Bid._4C, shape(0, 1), shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _4C")),
                shows(Bid._3H, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.oneHeart _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Hearts, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3D")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3C")),
                shows(Bid._2NT, shape(Suit.Diamonds, 6, 13), shape(Suit.Hearts, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3C")),
                shows(Bid._2S, shape(Suit.Diamonds, 6, 13), shape(Suit.Hearts, 3, 3), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _2S")),
                shows(Bid._2H, shape(4, 4), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid2.oneHeart _2H")),
                shows(Bid._2D, shape(6, 13), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid2.oneHeart _2H")),
                shows(Bid._2C, shape(4, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.oneHeart _2C")),
                shows(Bid._1NT, highCardPoints(OPEN_WEAK), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), ruleShow(1), id("OpenDiamondBid2.oneHeart _1NT")),
                shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.oneHeart _1S")),


                properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, true),
                properties(new Call[]{Bid._2C}, RespondDiamondBid2::diamond_1H_2C, true)
        );
        return choices;
    }

    //  1D -->  1S
    //
    public static PositionCalls diamond_1S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 7, 13), quality(Suit.Diamonds, SuitQuality.Excellent, SuitQuality.Solid), highCardPoints(OPEN_NT), ruleShow(1), id("OpenDiamondBid2.oneHeart _3NT")),
                shows(Bid._3H, shape(0, 1), shape(Suit.Spades, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3H")),
                shows(Bid._4C, shape(0, 1), shape(Suit.Spades, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _4C")),
                shows(Bid._3S, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.oneHeart _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Spades, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3D")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3C")),
                shows(Bid._2NT, shape(Suit.Diamonds, 6, 13), shape(Suit.Spades, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _3C")),
                shows(Bid._2S, shape(Suit.Diamonds, 5, 13), shape(Suit.Spades, 4, 4), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid2.oneHeart _2S")),
                shows(Bid._2H, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _2H")),
                shows(Bid._2H, shape(Suit.Spades, 3, 3), shape(Suit.Diamonds, 6, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.oneHeart _2H")),

                shows(Bid._2D, shape(6, 13), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid2.oneHeart _2H")),
                shows(Bid._2C, shape(4, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid2.oneHeart _2C")),
                shows(Bid._1NT, highCardPoints(OPEN_WEAK), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), ruleShow(1), id("OpenDiamondBid2.oneHeart _1NT")),

                properties(new Call[]{Bid._1NT}, RespondDiamondBid2::diamond_1S_1NT, true),
                properties(new Call[]{Bid._2C}, RespondDiamondBid2::diamond_1S_2C, true),
                properties(new Call[]{Bid._2H}, RespondDiamondBid2::diamond_1S_2H, true)
        );
        return choices;
    }


    //  1D -->  1NT
    //
    public static PositionCalls diamond_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2H, shape(4, 4), highCardPoints(OPEN_STRONG), GOOD_PLUS_SUIT, ruleShow(1), id("OpenDiamondBid2.diamond_1NT _2H")),
                shows(Bid._2S, shape(4, 4), highCardPoints(OPEN_STRONG), GOOD_PLUS_SUIT, ruleShow(1), id("OpenDiamondBid2.diamond_1NT _2S")),
                shows(Bid._3C, shape(4, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STRONG), GOOD_PLUS_SUIT, ruleShow(1), id("OpenDiamondBid2.diamond_1NT _3C")),
                properties(new Call[]{Bid._2H, Bid._2S, Bid._3C}, RespondDiamondBid2::diamond_1NT_2H2S3C, true)
        );
        return choices;
    }


    //  1D -->  2C
    //
    public static PositionCalls diamond_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(0, 1), shape(Suit.Clubs, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.diamond_2C _3H")),
                shows(Bid._3S, shape(0, 1), shape(Suit.Clubs, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.diamond_2C _3S")),

                shows(Bid._3D, shape(6, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.diamond_2C _3D")),
                shows(Bid._3C, shape(3, 13), highCardPoints(OPEN_STRONG), not(hasShortness(0, 1)), ruleShow(1), id("OpenDiamondBid2.diamond_2C _3D")),

                shows(Bid._2NT, shape(3, 13), highCardPoints(OPEN_STANDARD), shape4441(Suit.Clubs), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2NT")),

                shows(Bid._2H, shape(4, 4), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2H")),
                shows(Bid._2S, shape(4, 4), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2S")),

                shows(Bid._2D, highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2D"))

        );
        return choices;
    }

    //  1D -->  2D
    //
    public static PositionCalls diamond_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(0, 1), shape(Suit.Clubs, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid2.diamond_2D _3H"))

        );
        return choices;
    }
}
