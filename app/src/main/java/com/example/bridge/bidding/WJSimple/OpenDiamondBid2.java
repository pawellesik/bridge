package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;

public class OpenDiamondBid2 extends WJSimple {
    public static final Range HIGHT = new Range(15, 17);
    public static final Range LOW = new Range(12, 14);
    public static final Range STANDARD = new Range(12, 17);

    //  1D      -->  1H
    //  1NT, 1S -->  2C
    //
    public static PositionCalls oneHeart_oneSpade1NT_twoClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2D, ruleShow(1), id("OpenDiamondBid2.oneHeart_oneSpade1NT_twoClub _2D")),
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::oneHeart_oneSpade1NT_twoClub_twoDiamond, true)
        );
        return choices;
    }


    //  1D      -->  1H
    //  1NT, 1S -->  2D
    //
    public static PositionCalls oneHeart_oneSpade1NT_twoDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneHeart_oneSpade1NT_twoDiamond _3NT")),
                shows(Bid._3H, shape(3, 3), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneHeart_oneSpade1NT_twoDiamond _3H")),
                shows(Bid._2NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_oneSpade1NT_twoDiamond _2NT")),
                shows(Bid._2S, shape(Suit.Hearts, 0, 2), not(stopped(Suit.Clubs)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_oneSpade1NT_twoDiamond _2S")),
                shows(Bid._2H, shape(3, 3), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_oneSpade1NT_twoDiamond _2H"))

        );
        return choices;
    }

    //  1D     -->  1H
    //  2C     -->  2S
    //
    public static PositionCalls oneHeart_twoClub_twoSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3NT")),
                shows(Bid._3S, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 0, 2), not(stopped(Suit.Spades)), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3S")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 3, 3), highCardPoints(STANDARD), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 4, 4), not(stopped(Suit.Spades)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 5, 13), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._2NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3D"))


        );
        return choices;
    }

    //  1D      -->  1S
    //  1NT     -->  2C
    //
    public static PositionCalls oneSpade_1NT_twoClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2D, ruleShow(1), id("OpenDiamondBid2.oneSpade_1NT_twoClub _2D")),
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::oneSpade_1NT_twoClub_twoDiamond, true)
        );
        return choices;
    }

    //  1D      -->  1S
    //  1NT     -->  2D
    //
    public static PositionCalls oneSpade_1NT_twoDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneSpade_1NT_twoDiamond _3NT")),
                shows(Bid._3S, shape(3, 3), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneSpade_1NT_twoDiamond _3H")),
                shows(Bid._2NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneSpade_1NT_twoDiamond _2NT")),
                shows(Bid._2S, shape(3, 3), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneSpade_1NT_twoDiamond _2S")),
                shows(Bid._2H, shape(Suit.Spades, 0, 2), not(stopped(Suit.Clubs)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneSpade_1NT_twoDiamond _2H"))

        );
        return choices;
    }

    //  1D     -->  1S
    //  2C     -->  2H
    //
    public static PositionCalls oneSpade_twoClub_twoHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Hearts), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3NT")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Spades, 0, 2), not(stopped(Suit.Hearts)), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3S")),
                shows(Bid._3S, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Spades, 3, 3), highCardPoints(STANDARD), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 4, 4), not(stopped(Suit.Hearts)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 5, 13), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._2NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Hearts), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid2.oneHeart_twoClub_twoSpade _3D"))

        );
        return choices;
    }


    //  1D   -->   1S
    //  2H   -->  2NT
    //
    public static PositionCalls oneSpade_twoHeard_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 4, 4), ruleShow(1), id("OpenDiamondBid2.oneSpade_twoHeard_2NT _3NT")),
                shows(Bid._3S, shape(Suit.Diamonds, 6, 13), shape(Suit.Spades, 3, 3), ruleShow(1), id("OpenDiamondBid2.oneSpade_twoHeard_2NT _3S")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Hearts, 4, 4), shape(Suit.Spades, 3, 3), ruleShow(1), id("OpenDiamondBid2.oneSpade_twoHeard_2NT _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Hearts, 4, 4), ruleShow(1), id("OpenDiamondBid2.oneSpade_twoHeard_2NT _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 5), shape(Suit.Hearts, 4, 4), shape(Suit.Clubs, 3, 3), ruleShow(1), id("OpenDiamondBid2.oneSpade_twoHeard_2NT _3C"))
        );
        return choices;
    }

    //  1D   -->  1S
    //  2H   -->  2H
    //
    public static PositionCalls diamond_1S_2H_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3S, shape(3, 3),shape(Suit.Diamonds, 6, 13), ruleShow(1), id("OpenDiamondBid2.diamond_1S_2H_3H _3S")),
                shows(Bid._4H, shape(4, 4),shape(Suit.Diamonds, 5, 13), ruleShow(1), id("OpenDiamondBid2.diamond_1S_2H_3H _4H"))

                 );
        return choices;
    }


}
