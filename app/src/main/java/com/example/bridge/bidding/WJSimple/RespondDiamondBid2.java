package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;

public class RespondDiamondBid2 extends WJSimple {

    public static final Range WEAK = new Range(10, 12);
    public static final Range STRONG = new Range(13, 40);
    public static final Range TO_GAME = new Range(7, 9);
    public static final Range FORCING = new Range(10, 40);


    //  1D        -->  1H
    //  1NT, 1S   -->
    //
    public static PositionCalls oneHeart_oneSpade1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _3H")),
                shows(Bid._3D, shape(4, 13), shape(Suit.Hearts, 4, 4), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _3D")),
                shows(Bid._3C, shape(6, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _3C")),
                shows(Bid._2D, highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _2D")),
                shows(Bid._2C, highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _2C")),

                properties(new Call[]{Bid._2C}, OpenDiamondBid2::oneHeart_oneSpade1NT_twoClub, true),
                properties(new Call[]{Bid._2D}, OpenDiamondBid2::oneHeart_oneSpade1NT_twoDiamond, true)

        );
        return choices;
    }

    //  1D   -->  1H
    //  2C   -->
    //
    public static PositionCalls oneHeart_twoClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _3H")),
                shows(Bid._3D, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _3D")),
                shows(Bid._3C, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _3C")),
                shows(Bid._2NT, highCardPoints(WEAK), BALANCED, ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _2NT")),
                shows(Bid._2S, shape(4, 13), highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _2S")),
                shows(Bid._2D, shape(4, 13), betterThan(Suit.Diamonds, Suit.Clubs), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _2D")),
                shows(Call.PASS, betterThan(Suit.Clubs, Suit.Diamonds), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _pass")),

                propertiesForcingToGame(new Call[]{Bid._2S}, OpenDiamondBid2::oneHeart_twoClub_twoSpade, true)

        );
        return choices;
    }


    //  1D   -->  1S
    //  1NT   -->
    //
    public static PositionCalls oneSpade_oneSpade1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _3H")),
                shows(Bid._3D, shape(4, 13), shape(Suit.Spades, 4, 4), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _3D")),
                shows(Bid._3C, shape(6, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _3C")),
                shows(Bid._2D, highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _2D")),
                shows(Bid._2C, highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _2C")),

                properties(new Call[]{Bid._2C}, OpenDiamondBid2::oneSpade_1NT_twoClub, true),
                properties(new Call[]{Bid._2D}, OpenDiamondBid2::oneSpade_1NT_twoDiamond, true)

        );
        return choices;
    }

    //  1D   -->  1S
    //  2C   -->
    //
    public static PositionCalls oneSpade_twoClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _3S")),
                shows(Bid._3D, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _3D")),
                shows(Bid._3C, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _3C")),
                shows(Bid._2NT, highCardPoints(WEAK), BALANCED, ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _2NT")),
                shows(Bid._2H, shape(4, 13), highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _2S")),
                shows(Bid._2D, shape(4, 13), betterThan(Suit.Diamonds, Suit.Clubs), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _2D")),
                shows(Call.PASS, betterThan(Suit.Clubs, Suit.Diamonds), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _pass")),

                propertiesForcingToGame(new Call[]{Bid._2H}, OpenDiamondBid2::oneSpade_twoClub_twoHeart, true)

        );
        return choices;
    }

    //  1D   -->  1S
    //  2H   -->
    //
    public static PositionCalls oneSpade_twoHeard(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(4, 13), shape(Suit.Spades, 5, 13), highCardPoints(FORCING), ruleShow(1), id("RespondDiamondBid2.oneSpade_twoHeard _3H")),
                shows(Bid._3C, shape(Suit.Hearts, 0, 3), not(stopped(Suit.Clubs)), highCardPoints(FORCING), ruleShow(1), ruleDescription("Ask for a stopper"), id("RespondDiamondBid2.oneSpade_twoHeard _3C")),
                shows(Bid._2NT, shape(Suit.Hearts, 0, 3), highCardPoints(FORCING), ruleShow(1), ruleDescription("Asking about shape"), id("RespondDiamondBid2.oneSpade_twoHeard _2NT")),
                shows(Bid._2S, shape(6, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_twoHeard _2S")),
                shows(Bid._3D, shape(2, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_twoHeard _3D")),

                properties(new Call[]{Bid._2NT}, OpenDiamondBid2::oneSpade_twoHeard_2NT, true),
                properties(new Call[]{Bid._3H}, OpenDiamondBid2::diamond_1S_2H_3H, true)
                );
        return choices;
    }

}
