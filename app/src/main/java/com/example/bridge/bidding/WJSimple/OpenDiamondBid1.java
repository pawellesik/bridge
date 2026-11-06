package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Constraints.Shape;
import com.example.bridge.bidding.NatC.RecursionNatC;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;

public class OpenDiamondBid1 extends WJSimple {

    public static final Range OPEN_STANDARD = new Range(12, 17);
    public static final Range OPEN_WEAK = new Range(12, 14);
    public static final Range OPEN_STRONG = new Range(15, 17);
    public static final Range OPEN_NT = new Range(14, 17);


    //1D -->
    //    1H -->
    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 7, 13), quality(Suit.Diamonds, SuitQuality.Excellent, SuitQuality.Solid), highCardPoints(OPEN_NT), ruleShow(1), id("OpenDiamondBid1.oneHeart _3NT")),
                shows(Bid._3S, shape(0, 1), shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3S")),
                shows(Bid._4C, shape(0, 1), shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _4C")),
                shows(Bid._3H, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid1.oneHeart _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Hearts, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3D")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3C")),
                shows(Bid._2NT, shape(Suit.Diamonds, 6, 13), shape(Suit.Hearts, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3C")),
                shows(Bid._2S, shape(Suit.Diamonds, 6, 13), shape(Suit.Hearts, 3, 3), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _2S")),
                shows(Bid._2H, shape(4, 4), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid1.oneHeart _2H")),
                shows(Bid._2D, shape(6, 13), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid1.oneHeart _2H")),
                shows(Bid._2C, shape(4, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid1.oneHeart _2C")),
                shows(Bid._1NT, highCardPoints(OPEN_WEAK), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), ruleShow(1), id("OpenDiamondBid1.oneHeart _1NT")),
                shows(Bid._1S, shape(4, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid1.oneHeart _1S")),


                properties(new Call[]{Bid._1NT,Bid._1S}, RespondDiamondBid2::oneSpade1NT, true),
                properties(new Call[]{Bid._2C}, RespondDiamondBid2::twoClub, true)
                );
        return choices;
    }

    //1D -->
    //    1S -->
    public static PositionCalls oneSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 7, 13), quality(Suit.Diamonds, SuitQuality.Excellent, SuitQuality.Solid), highCardPoints(OPEN_NT), ruleShow(1), id("OpenDiamondBid1.oneHeart _3NT")),
                shows(Bid._3H, shape(0, 1), shape(Suit.Spades, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3H")),
                shows(Bid._4C, shape(0, 1), shape(Suit.Spades, 4, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _4C")),
                shows(Bid._3S, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid1.oneHeart _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Spades, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3D")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3C")),
                shows(Bid._2NT, shape(Suit.Diamonds, 6, 13), shape(Suit.Spades, 0, 2), highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _3C")),
                shows(Bid._2S, shape(Suit.Diamonds, 5, 13), shape(Suit.Spades, 4, 4), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid1.oneHeart _2S")),
                shows(Bid._2H, shape(4, 4), shape(Suit.Diamonds, 5, 13),highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _2H")),
                shows(Bid._2H, shape(Suit.Spades, 3, 3), shape(Suit.Diamonds, 6, 13),highCardPoints(OPEN_STRONG), ruleShow(1), id("OpenDiamondBid1.oneHeart _2H")),

                shows(Bid._2D, shape(6, 13), highCardPoints(OPEN_WEAK), ruleShow(1), id("OpenDiamondBid1.oneHeart _2H")),
                shows(Bid._2C, shape(4, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_STANDARD), ruleShow(1), id("OpenDiamondBid1.oneHeart _2C")),
                shows(Bid._1NT, highCardPoints(OPEN_WEAK), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), ruleShow(1), id("OpenDiamondBid1.oneHeart _1NT")),
                //to do
                //properties(new Call[]{Bid._1NT,Bid._1S}, RespondDiamondBid2::oneSpade1NT, true),
                //properties(new Call[]{Bid._2C}, RespondDiamondBid2::twoClub, true)
        );
        return choices;
    }

}
