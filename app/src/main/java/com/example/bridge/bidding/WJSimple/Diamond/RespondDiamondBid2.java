package com.example.bridge.bidding.WJSimple.Diamond;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJSimple.WJSimple;

public class RespondDiamondBid2 extends WJSimple {

    public static final Range WEAK = new Range(10, 12);
    public static final Range STRONG = new Range(13, 40);
    public static final Range TO_GAME = new Range(7, 9);
    public static final Range FORCING = new Range(10, 40);


    //  1D        -->  1H
    //  1NT, 1S   -->
    //
    public static PositionCalls diamond_1H_1NT1S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _3H")),
                shows(Bid._3D, shape(4, 13), shape(Suit.Hearts, 4, 4), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _3D")),
                shows(Bid._3C, shape(6, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _3C")),
                shows(Bid._2D, highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _2D")),
                shows(Bid._2C, highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_OneSpade1NT _2C")),

                properties(new Call[]{Bid._2C}, OpenDiamondBid3::diamond_1H_1NT1S_2C, true),
                properties(new Call[]{Bid._2D}, OpenDiamondBid3::diamond_1H_1NT1S_2D, true)

        );
        return choices;
    }

    //  1D   -->  1H
    //  2C   -->
    //
    public static PositionCalls diamond_1H_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _3H")),
                shows(Bid._3D, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _3D")),
                shows(Bid._3C, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _3C")),
                shows(Bid._2NT, highCardPoints(WEAK), BALANCED, ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _2NT")),
                shows(Bid._2S, shape(4, 13), highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _2S")),
                shows(Bid._2D, shape(4, 13), betterThan(Suit.Diamonds, Suit.Clubs), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _2D")),
                shows(Call.PASS, betterThan(Suit.Clubs, Suit.Diamonds), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneHeart_TwoClub _pass")),

                propertiesForcingToGame(new Call[]{Bid._2S}, OpenDiamondBid3::diamond_1H_2C_2S, true)

        );
        return choices;
    }


    //  1D   -->  1S
    //  1NT   -->
    //
    public static PositionCalls diamond_1S_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _3H")),
                shows(Bid._3D, shape(4, 13), shape(Suit.Spades, 4, 4), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _3D")),
                shows(Bid._3C, shape(6, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _3C")),
                shows(Bid._2D, highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _2D")),
                shows(Bid._2C, highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_OneSpade1NT _2C")),

                properties(new Call[]{Bid._2C}, OpenDiamondBid3::diamond_1S_1NT_2C, true),
                properties(new Call[]{Bid._2D}, OpenDiamondBid3::diamond_1S_1NT_2D, true)

        );
        return choices;
    }

    //  1D   -->  1S
    //  2C   -->
    //
    public static PositionCalls diamond_1S_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _3S")),
                shows(Bid._3D, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _3D")),
                shows(Bid._3C, fit(), highCardPoints(WEAK), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _3C")),
                shows(Bid._2NT, highCardPoints(WEAK), BALANCED, ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _2NT")),
                shows(Bid._2H, shape(4, 13), highCardPoints(STRONG), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _2S")),
                shows(Bid._2D, shape(4, 13), betterThan(Suit.Diamonds, Suit.Clubs), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _2D")),
                shows(Call.PASS, betterThan(Suit.Clubs, Suit.Diamonds), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_TwoClub _pass")),

                propertiesForcingToGame(new Call[]{Bid._2H}, OpenDiamondBid3::diamond_1S_2C_2H, true)

        );
        return choices;
    }

    //  1D   -->  1S
    //  2H   -->
    //
    public static PositionCalls diamond_1S_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(4, 13), shape(Suit.Spades, 5, 13), highCardPoints(FORCING), ruleShow(1), id("RespondDiamondBid2.oneSpade_twoHeard _3H")),
                shows(Bid._3C, shape(Suit.Hearts, 0, 3), not(stopped(Suit.Clubs)), highCardPoints(FORCING), ruleShow(1), ruleDescription("Ask for a stopper"), id("RespondDiamondBid2.oneSpade_twoHeard _3C")),
                shows(Bid._2NT, shape(Suit.Hearts, 0, 3), highCardPoints(FORCING), ruleShow(1), ruleDescription("Asking about shape"), id("RespondDiamondBid2.oneSpade_twoHeard _2NT")),
                shows(Bid._2S, shape(6, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_twoHeard _2S")),
                shows(Bid._3D, shape(2, 13), highCardPoints(TO_GAME), ruleShow(1), id("RespondDiamondBid2.oneSpade_twoHeard _3D")),

                properties(new Call[]{Bid._2NT}, OpenDiamondBid3::diamond_1S_2H_2NT, true),
                properties(new Call[]{Bid._3H}, OpenDiamondBid3::diamond_1S_2H_3H, true)
                );
        return choices;
    }

    //  1D   -->  1NT
    //  2H, 2S, 3C   -->
    //
    public static PositionCalls diamond_1NT_2H2S3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3D, fit(), noFit(), partner(isLastBid(Bid._2H, Bid._2C)),  ruleShow(1), id("RespondDiamondBid2.diamond_1NT_2H2S3C _3D")),
                shows(Bid._3D, partner(isLastBid(Bid._3C)), betterThan(Suit.Clubs), ruleShow(1), id("RespondDiamondBid2.diamond_1NT_2H2S3C _3D"))

                );
        return choices;
    }

    //  1D   -->  2H
    //  3C   -->
    //
    public static PositionCalls diamond_2H_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT,  stopped(Suit.Clubs), ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3NT")),
                shows(Bid._3S,  GOOD_PLUS_SUIT, ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3S")),
                shows(Bid._3H,  shape(5,13), ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3H")),
                shows(Bid._3D,  shape(2,2), ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3D"))

        );
        return choices;
    }


}
