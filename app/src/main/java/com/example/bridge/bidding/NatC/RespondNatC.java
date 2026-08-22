package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RespondNatC extends NatC {
    public static final Range RESPOND_PASS = new Range(0, 6);
    public static final Range MINIMUM_HAND = new Range(7, 10);
    public static final Range JUMP_HAND = new Range(11, 28);
    public static final Range JUMP_AFTER_PASS = new Range(11, 11);
    public static final Range WEAK_LONG = new Range(7, 10);


    public static PositionCalls oneClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                properties(new Call[]{Bid._1D}, OpenBid2NatC::responderNegat, false),
                properties(new Call[]{Bid._1S, Bid._1H}, OpenBid2NatC::responderdTrumpMajorClub, false),
                properties(new Call[]{Bid._2S, Bid._2H}, OpenBid2NatC::responderdRaiseTrumpMajorClub, true),
                properties(new Call[]{Bid._2C, Bid._2D}, OpenBid2NatC::responderdTrumpMinorClub, false),
                properties(new Call[]{Bid._3C, Bid._3D}, OpenBid2NatC::responderdRaiseTrumpMinorClub, true),
                properties(new Call[]{Bid._1NT}, OpenBid2NatC::responderd1NTClub, true),
                properties(new Call[]{Bid._2NT}, OpenBid2NatC::responderd2NTClub, true),
                properties(new Call[]{Bid._3S, Bid._3H}, OpenBid2NatC::weakRespond, true),

                shows(Bid._1D, highCardPoints(RESPOND_PASS), id("RespondNatC.oneClub _1D")),
                shows(Bid._1H, highCardPoints(MINIMUM_HAND), shape(6, 10), id("RespondNatC.oneClub _1H")),
                shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(6, 10), id("RespondNatC.oneClub _1S")),

                shows(Bid._1H, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _1H")),
                shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _1S")),

                shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _2D")),
                shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _2C")),

                shows(Bid._2H, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneClub JUMP_AFTER_PASS _2H")),
                shows(Bid._2S, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneClub JUMP_AFTER_PASS _2S")),
                shows(Bid._3D, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneClub JUMP_AFTER_PASS _3D")),
                shows(Bid._3C, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneClub JUMP_AFTER_PASS _3C")),

                shows(Bid._2H, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub JUMP_HAND _2H")),
                shows(Bid._2S, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub JUMP_HAND _2S")),
                shows(Bid._3D, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub JUMP_HAND _3D")),
                shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub JUMP_HAND _3C")),

                shows(Bid._3H, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneClub _3H")),
                shows(Bid._3S, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneClub _3S")),

                shows(Bid._2NT, highCardPoints(JUMP_HAND), id("RespondNatC.oneClub _2NT")),
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), id("RespondNatC.oneClub _1NT"))
        );
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    public static PositionCalls oneDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                propertiesAgreeTrump(new Call[]{Bid._2D, Bid._3D}, OpenBid2NatC::responderdTrumpMinorDiamod, true),
                properties(new Call[]{Bid._1S, Bid._1H, Bid._2C}, OpenBid2NatC::responderChangedSuitsDiamond, false),
                properties(new Call[]{Bid._2S, Bid._2H}, OpenBid2NatC::responderRaiseChangedSuitsMajorDiamond, false),
                properties(new Call[]{Bid._3C}, OpenBid2NatC::responderRaiseChangedSuitsMinorDiamond, false),
                properties(new Call[]{Bid._3S, Bid._3H}, OpenBid2NatC::weakRespond, false),
                properties(new Call[]{Bid._1NT}, RecursionNatC::recursionFindFitGame, false),
                properties(new Call[]{Bid._2NT}, RecursionNatC::recursionFindFitGame, true),

                shows(Bid._3S, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneDiamond _3S")),
                shows(Bid._3H, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneDiamond _3H")),

                shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _1S")),
                shows(Bid._1H, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _1H")),

                shows(Bid._2S, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneDiamond _2S")),
                shows(Bid._2H, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneDiamond _2H")),

                shows(Bid._2S, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneDiamond _2S")),
                shows(Bid._2H, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneDiamond _2H")),

                shows(Bid._2D, highCardPoints(MINIMUM_HAND), fit(), setTrumpColor(Suit.Diamonds), id("RespondNatC.oneDiamond _2D")),
                shows(Bid._3D, highCardPoints(JUMP_AFTER_PASS), fit(), setTrumpColor(Suit.Diamonds), PASSED_HAND, id("RespondNatC.oneDiamond _3D")),
                shows(Bid._3D, highCardPoints(JUMP_HAND), fit(), setTrumpColor(Suit.Diamonds), id("RespondNatC.oneDiamond _3D")),

                shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _2C")),
                shows(Bid._3C, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneDiamond _3C")),
                shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneDiamond _3C")),

                shows(Bid._2NT, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(Suit.Diamonds, 0, 2), id("RespondNatC.oneDiamond _2NT")),
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Diamonds, 0, 2), id("RespondNatC.oneDiamond _1NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondNatC.oneDiamond _3NT"))
        );
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }


    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                propertiesAgreeTrump(new Call[]{Bid._2H, Bid._3H}, OpenBid2NatC::responderTrumpMajorHeart, true),
                properties(new Call[]{Bid._1S, Bid._2C, Bid._2D}, OpenBid2NatC::responderChangedSuitsHeart, false),
                properties(new Call[]{Bid._2S}, OpenBid2NatC::responderRaiseChangedSuitsToSpadeHeart, false),
                properties(new Call[]{Bid._3C, Bid._3D}, OpenBid2NatC::responderRaiseChangedSuitsToMinorHeart, false),
                properties(new Call[]{Bid._3S}, OpenBid2NatC::weakRespond, false),
                properties(new Call[]{Bid._1NT}, RecursionNatC::recursionFindFitGame, false),
                properties(new Call[]{Bid._2NT}, RecursionNatC::recursionFindFitGame, true),

                shows(Bid._3S, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneHeart WEAK_LONG _3S")),

                shows(Bid._2H, highCardPoints(MINIMUM_HAND), fit(), setTrumpColor(Suit.Hearts), id("RespondNatC.oneHeart _2H")),
                shows(Bid._3H, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, fit(), setTrumpColor(Suit.Hearts), id("RespondNatC.oneHeart _3H")),
                shows(Bid._3H, highCardPoints(JUMP_HAND), fit(), setTrumpColor(Suit.Hearts), id("RespondNatC.oneHeart _3H")),

                shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _1S")),
                shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _2C")),
                shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _2D")),
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Hearts, 0, 2), id("RespondNatC.oneHeart _1NT")),

                shows(Bid._2S, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneHeart _2S")),
                shows(Bid._3C, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneHeart _3C")),
                shows(Bid._3D, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneHeart _3D")),
                shows(Bid._2S, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneHeart _2S")),
                shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneHeart _3C")),
                shows(Bid._3D, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneHeart _3D")),

                shows(Bid._2NT, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(Suit.Hearts, 0, 2), id("RespondNatC.oneHeart _2NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondNatC.oneSpade _3NT"))
        );
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    public static PositionCalls oneSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(
                propertiesAgreeTrump(new Call[]{Bid._2S, Bid._3S}, OpenBid2NatC::responderTrumpMajorSpade, true),
                properties(new Call[]{Bid._2H, Bid._2C, Bid._2D}, OpenBid2NatC::responderChangedSuitsSpade, false),
                properties(new Call[]{Bid._3C, Bid._3D, Bid._3H}, OpenBid2NatC::responderRaiseChangedSuitsSpade, false),
                properties(new Call[]{Bid._1NT}, RecursionNatC::recursionFindFitGame, false),
                properties(new Call[]{Bid._2NT}, RecursionNatC::recursionFindFitGame, true),

                shows(Bid._2S, highCardPoints(MINIMUM_HAND), fit(), setTrumpColor(Suit.Spades), id("RespondNatC.oneSpade _2S")),
                shows(Bid._3S, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, fit(), setTrumpColor(Suit.Spades), id("RespondNatC.oneSpade _3S")),
                shows(Bid._3S, highCardPoints(JUMP_HAND), fit(), setTrumpColor(Suit.Spades), id("RespondNatC.oneSpade _3S")),

                shows(Bid._2H, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2H")),
                shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2C")),
                shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2D")),
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Spades, 0, 2), BALANCED, id("RespondNatC.oneSpade _1NT")),

                shows(Bid._3H, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneSpade _3H")),
                shows(Bid._3C, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneSpade _3C")),
                shows(Bid._3D, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(5, 10), id("RespondNatC.oneSpade _3D")),
                shows(Bid._3H, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneSpade _3H")),
                shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneSpade _3C")),
                shows(Bid._3D, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneSpade _3D")),

                shows(Bid._2NT, highCardPoints(JUMP_AFTER_PASS), PASSED_HAND, shape(Suit.Hearts, 0, 2), id("RespondNatC.oneSpade _2NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("RespondNatC.oneSpade _3NT"))

        );
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    public static Iterable<CallFeature> weakOpen(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        for (CallFeature cf : AcesAsk.initiateConventionBlok(ps)) bids.add(cf);
        bids.add(shows(Bid._4H, FIT_8_PLUS, setTrumpColor(Suit.Hearts), ruleOf17(), id("RespondNatC.weakOpen _4H")));
        bids.add(shows(Bid._4H, fit(10), setTrumpColor(Suit.Hearts), id("RespondNatC.weakOpen _4H")));
        bids.add(shows(Bid._4S, FIT_8_PLUS, setTrumpColor(Suit.Spades), ruleOf17(), id("RespondNatC.weakOpen _4S")));
        bids.add(shows(Bid._4S, fit(10), setTrumpColor(Suit.Spades), id("RespondNatC.weakOpen _4S")));
        bids.add(shows(Bid._5C, fit(10), pairHighCardPoints(PAIR_MINOR_GAME), setTrumpColor(Suit.Clubs), id("RespondNatC.weakOpen _5C")));
        bids.add(shows(Bid._5D, fit(10), pairHighCardPoints(PAIR_MINOR_GAME), setTrumpColor(Suit.Diamonds), id("RespondNatC.weakOpen _5C")));
        bids.add(shows(Call.PASS));
        return bids;
    }
}










