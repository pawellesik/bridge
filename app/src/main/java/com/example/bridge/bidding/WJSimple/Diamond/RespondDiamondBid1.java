package com.example.bridge.bidding.WJSimple.Diamond;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJSimple.WJSimple;

public class RespondDiamondBid1 extends WJSimple {

    public static final Range RESPOND_STANDARD = new Range(7, 40); // oryginalenie jest 6,40
    public static final Range RESPOND_TO_1NT = new Range(7, 10);
    public static final Range RESPOND_TO_2NT = new Range(11, 12);
    public static final Range RESPOND_TO_GAME = new Range(13, 16);
    public static final Range RESPOND_FORCING = new Range(13, 40);
    public static final Range RESPOND_FORCING_2C = new Range(12, 40);
    public static final Range RESPOND_WEAK = new Range(7, 9); // oryginalenie jest 5,9
    public static final Range RESPOND_3C = new Range(10, 11);
    public static final Range RESPOND_2D = new Range(10, 40);

    //1D -->
    public static PositionCalls oneDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Spades, 0, 3), shape(Suit.Hearts, 0, 3), highCardPoints(RESPOND_TO_GAME), ruleShow(1), id("RespondDiamondBid1.oneDiamond _3NT")),

                shows(Bid._3H, shape(Suit.Diamonds, 4, 13), shape(Suit.Hearts, 0, 1), highCardPoints(RESPOND_TO_GAME), ruleShow(1), id("RespondDiamondBid1.oneDiamond _3H")),
                shows(Bid._3S, shape(Suit.Diamonds, 4, 13), shape(Suit.Spades, 0, 1), highCardPoints(RESPOND_TO_GAME), ruleShow(1), id("RespondDiamondBid1.oneDiamond _3H")),
                shows(Bid._4C, shape(Suit.Diamonds, 4, 13), shape(Suit.Clubs, 0, 1), highCardPoints(RESPOND_TO_GAME), ruleShow(1), id("RespondDiamondBid1.oneDiamond _3H")),

                shows(Bid._3D, shape(4, 13), shape(Suit.Spades, 0, 3), shape(Suit.Hearts, 0, 3), highCardPoints(RESPOND_WEAK), ruleShow(1), id("RespondDiamondBid1.oneDiamond _3D")),

                shows(Bid._3C, shape(6, 13), highCardPoints(RESPOND_3C), ruleShow(1), id("RespondDiamondBid1.oneDiamond _3C")),

                shows(Bid._2NT, shape(Suit.Spades, 0, 3), shape(Suit.Hearts, 0, 3), highCardPoints(RESPOND_TO_2NT), ruleShow(1), id("RespondDiamondBid1.oneDiamond _2NT")),

                shows(Bid._2S, highCardPoints(RESPOND_FORCING), shape(Suit.Spades, 0, 3), shape(Suit.Hearts, 0, 3), ruleShow(1), id("RespondDiamondBid1.oneDiamond _2S")),

                shows(Bid._2H, highCardPoints(RESPOND_WEAK), shape(Suit.Spades, 5, 13), shape(Suit.Hearts, 4, 13), ruleShow(1), id("RespondDiamondBid1.oneDiamond _2H")),

                shows(Bid._2D, shape(4, 13), highCardPoints(RESPOND_2D), ruleShow(1), id("RespondDiamondBid1.oneDiamond _2D")),

                shows(Bid._2C, shape(5, 13), highCardPoints(RESPOND_FORCING_2C), ruleShow(1), id("RespondDiamondBid1.oneDiamond _2C")),

                shows(Bid._1NT, shape(Suit.Spades, 0, 3), shape(Suit.Hearts, 0, 3), highCardPoints(RESPOND_TO_1NT), ruleShow(1), id("RespondDiamondBid1.oneDiamond _1NT")),
                shows(Bid._1H, shape(4, 13), highCardPoints(RESPOND_STANDARD), ruleShow(1), id("RespondDiamondBid1.oneDiamond _1H")),
                shows(Bid._1S, shape(4, 13), highCardPoints(RESPOND_STANDARD), ruleShow(1), id("RespondDiamondBid1.oneDiamond _1S")),

                properties(new Call[]{Bid._1H}, OpenDiamondBid2::diamond_1H, true),
                properties(new Call[]{Bid._1S}, OpenDiamondBid2::diamond_1S, true),
                properties(new Call[]{Bid._1NT}, OpenDiamondBid2::diamond_1NT, false),
                properties(new Call[]{Bid._2C}, OpenDiamondBid2::diamond_2C, true),
                properties(new Call[]{Bid._2D}, OpenDiamondBid2::diamond_2D, true),
                properties(new Call[]{Bid._2D}, OpenDiamondBid2::diamond_2H, true),
                properties(new Call[]{Bid._2S}, OpenDiamondBid2::diamond_2S, true),
                properties(new Call[]{Bid._2NT}, OpenDiamondBid2::diamond_2NT, true)

        );
        return choices;
    }


}
