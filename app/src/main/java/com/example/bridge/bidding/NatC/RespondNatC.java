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
    public static final Range WEAK_LONG = new Range(7, 11);


    public static PositionCalls oneClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(OpenBid2NatC::responderClub),
                properties(new Call[]{Bid._1D}, OpenBid2NatC::responderNegat, true),
                properties(new Call[]{Bid._3D, Bid._3C}, OpenBid2NatC::responderClubJumpMinor, true),
                shows(Bid._1D, highCardPoints(RESPOND_PASS), id("RespondNatC.oneClub _1D")),
                shows(Bid._1H, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _1H")),
                shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _1H")),
                shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _2D")),
                shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneClub _2C")),

                shows(Bid._2H, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub _2H")),
                shows(Bid._2S, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub _2H")),
                shows(Bid._3D, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub _3D")),
                shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneClub _3C")),

                shows(Bid._3H, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneClub _3H")),
                shows(Bid._3S, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneClub _3H")),

                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), id("RespondNatC.oneClub _1NT"))
        );

        choices.addPassRule(points(RESPOND_PASS));
        return choices;
    }

    public static PositionCalls oneDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        if (ps.isPassedHand()) {
            choices.addRules(
                    partnerBids(OpenBid2NatC::responderChangedSuits),
                    shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _1S")),
                    shows(Bid._1H, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _1H")),

                    shows(Bid._2S, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneDiamond _2S")),
                    shows(Bid._2H, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneDiamond _2H")),

                    shows(Bid._3S, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneDiamond _3S")),
                    shows(Bid._3H, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneDiamond _3H")),

                    shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _2C")),
                    shows(Bid._2D, highCardPoints(MINIMUM_HAND), fit(), id("RespondNatC.oneDiamond _2D")),

                    shows(Bid._3C, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneDiamond _3C")),
                    shows(Bid._3D, highCardPoints(JUMP_AFTER_PASS), fit(), id("RespondNatC.oneDiamond _3D")),

                    shows(Bid._2NT, highCardPoints(JUMP_AFTER_PASS), shape(Suit.Diamonds, 0, 2), id("RespondNatC.oneDiamond _2NT")),
                    shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Diamonds, 0, 2), id("RespondNatC.oneDiamond _1NT"))

            );
        } else {
            choices.addRules(
                    partnerBids(OpenBid2NatC::responderChangedSuits),
                    properties(new Call[]{Bid._3D}, OpenBid2NatC::responderRaisedMinor, true),

                    shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _1S")),
                    shows(Bid._1H, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _1H")),

                    shows(Bid._2S, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneDiamond _2S")),
                    shows(Bid._2H, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneDiamond _2H")),

                    shows(Bid._3S, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneDiamond _3S")),
                    shows(Bid._3H, highCardPoints(WEAK_LONG), shape(7, 10), id("RespondNatC.oneDiamond _3H")),

                    shows(Bid._5D, highCardPoints(PAIR_MINOR_GAME), fit(), id("RespondNatC.oneDiamond _5D")),

                    shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneDiamond _2C")),
                    shows(Bid._2D, highCardPoints(MINIMUM_HAND), fit(), id("RespondNatC.oneDiamond _2D")),

                    shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneDiamond _3C")),
                    shows(Bid._3D, highCardPoints(JUMP_HAND), fit(), id("RespondNatC.oneDiamond _3D")),

                    shows(Bid._3NT, BALANCED, pairHighCardPoints(PAIR_GAME), id("RespondNatC.oneDiamond _3NT")),
                    shows(Bid._2NT, highCardPoints(JUMP_HAND), shape(Suit.Diamonds, 0, 2), id("RespondNatC.oneDiamond _2NT")),
                    shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Diamonds, 0, 2), id("RespondNatC.oneDiamond _1NT"))
            );
        }
        choices.addPassRule(points(RESPOND_PASS));
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Call[] raises = new Call[]{Bid._2H, Bid._3H, Bid._4H, Bid._2S, Bid._3S, Bid._4S};
        if (ps.isPassedHand()) {
            choices.addRules(
                    partnerBids(OpenBid2NatC::responderChangedSuits),
                    propertiesAgreeTrump(raises, OpenBid2NatC::responderRaisedMajor, true),
                    propertiesAgreeTrump(new Call[]{Bid._1NT}, OpenBid2NatC::responder1NT, true),
                    propertiesAgreeTrump(new Call[]{Bid._2NT}, OpenBid2NatC::responder2NT, true),

                    shows(Bid._2H, highCardPoints(MINIMUM_HAND), fit(), id("RespondNatC.oneHeart _2H")),
                    shows(Bid._3H, highCardPoints(JUMP_AFTER_PASS), fit(), id("RespondNatC.oneHeart _3H")),

                    shows(Bid._2S, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneHeart _2S")),
                    shows(Bid._3D, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneHeart _3D")),
                    shows(Bid._3C, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneHeart _3C")),

                    shows(Bid._1S, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _1S")),
                    shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _2C")),
                    shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _2D")),

                    shows(Bid._2NT, highCardPoints(JUMP_AFTER_PASS), shape(Suit.Hearts, 0, 2), id("RespondNatC.oneHeart _2NT")),
                    shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Hearts, 0, 2), id("RespondNatC.oneHeart _1NT"))
            );
        } else {
            choices.addRules(
                    partnerBids(OpenBid2NatC::responderChangedSuits),
                    propertiesAgreeTrump(raises, OpenBid2NatC::responderRaisedMajor, true),

                    shows(Bid._2H, highCardPoints(MINIMUM_HAND), fit(), id("RespondNatC.oneHeart _2H")),
                    shows(Bid._2S, highCardPoints(MINIMUM_HAND), shape(Suit.Hearts, 0, 2), shape(5, 10), id("RespondNatC.oneHeart _2S")),

                    shows(Bid._3H, highCardPoints(JUMP_HAND), fit(), id("RespondNatC.oneHeart _3H")),
                    shows(Bid._2S, highCardPoints(JUMP_HAND), shape(Suit.Hearts, 0, 2), shape(5, 10), id("RespondNatC.oneHeart _2S")),

                    shows(Bid._4S, highCardPoints(PAIR_GAME), fit(), id("RespondNatC.oneHeart _4S")),
                    shows(Bid._4H, highCardPoints(PAIR_GAME), fit(), id("RespondNatC.oneHeart _4H")),

                    shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _2C")),
                    shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneHeart _3C")),

                    shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneHeart _2D")),
                    shows(Bid._3D, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneHeart _3D")),

                    shows(Bid._1NT, shape(Suit.Hearts, 0, 2), pairHighCardPoints(MINIMUM_HAND)),
                    shows(Bid._3NT, BALANCED, pairHighCardPoints(PAIR_GAME), id("RespondNatC.oneHeart _3NT"))
            );
        }
        choices.addPassRule(points(RESPOND_PASS), id("RespondNatC.oneHeart _PASS"));
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls oneSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        Call[] raises = new Call[]{Bid._2S, Bid._3S, Bid._4S, Bid._2H, Bid._3H, Bid._4H};
        if (ps.isPassedHand()) {
            choices.addRules(
                    partnerBids(OpenBid2NatC::responderChangedSuits),
                    propertiesAgreeTrump(raises, OpenBid2NatC::responderRaisedMajor, true),
                    propertiesAgreeTrump(new Call[]{Bid._1NT}, OpenBid2NatC::responder1NT, true),
                    propertiesAgreeTrump(new Call[]{Bid._2NT}, OpenBid2NatC::responder2NT, true),

                    shows(Bid._2S, highCardPoints(MINIMUM_HAND), fit(), id("RespondNatC.oneSpade _2S")),
                    shows(Bid._3S, highCardPoints(JUMP_AFTER_PASS), fit(), id("RespondNatC.oneSpade _3S")),

                    shows(Bid._3H, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneSpade _3H")),
                    shows(Bid._3D, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneSpade _3D")),
                    shows(Bid._3C, highCardPoints(JUMP_AFTER_PASS), shape(5, 10), id("RespondNatC.oneSpade _3C")),

                    shows(Bid._2H, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2H")),
                    shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2C")),
                    shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2D")),

                    shows(Bid._2NT, highCardPoints(JUMP_AFTER_PASS), shape(Suit.Spades, 0, 2), id("RespondNatC.oneSpade _2NT")),
                    shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Spades, 0, 2), id("RespondNatC.oneSpade _1NT"))
            );
        } else {
            choices.addRules(SolidSuitNatC.BIDS(ps));
            choices.addRules(
                    partnerBids(OpenBid2NatC::responderChangedSuits),
                    propertiesAgreeTrump(raises, OpenBid2NatC::responderRaisedMajor, true),

                    shows(Bid._2S, highCardPoints(MINIMUM_HAND), fit(), id("RespondNatC.oneSpade _2S")),
                    shows(Bid._2H, highCardPoints(MINIMUM_HAND), shape(Suit.Spades, 0, 2), shape(5, 10), id("RespondNatC.oneSpade _2H")),

                    shows(Bid._3S, highCardPoints(JUMP_HAND), fit(), id("RespondNatC.oneSpade _3S")),
                    shows(Bid._3H, highCardPoints(JUMP_HAND), shape(Suit.Spades, 0, 2), shape(5, 10), id("RespondNatC.oneSpade _3H")),

                    shows(Bid._4S, highCardPoints(PAIR_GAME), fit(), id("RespondNatC.oneSpade _4S")),
                    shows(Bid._4H, highCardPoints(PAIR_GAME), fit(), id("RespondNatC.oneSpade _4H")),

                    shows(Bid._2C, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2C")),
                    shows(Bid._3C, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneSpade _3C")),

                    shows(Bid._2D, highCardPoints(MINIMUM_HAND), shape(5, 10), id("RespondNatC.oneSpade _2D")),
                    shows(Bid._3D, highCardPoints(JUMP_HAND), shape(5, 10), id("RespondNatC.oneSpade _3D")),

                    shows(Bid._1NT, shape(Suit.Spades, 0, 2), pairHighCardPoints(MINIMUM_HAND)),
                    shows(Bid._3NT, BALANCED, pairHighCardPoints(PAIR_GAME), id("RespondNatC.oneSpade _3NT"))
            );
        }
        choices.addPassRule(points(RESPOND_PASS), id("RespondNatC.oneSpade _PASS"));
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static Iterable<CallFeature> weakOpen(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        for (CallFeature cf : AcesAsk.initiateConventionBlok(ps)) bids.add(cf);
        bids.add(shows(Bid._4H, FIT_8_PLUS, ruleOf17()));
        bids.add(shows(Bid._4H, fit(10)));
        bids.add(shows(Bid._4S, FIT_8_PLUS, ruleOf17()));
        bids.add(shows(Bid._4S, fit(10)));
        bids.add(shows(Call.PASS));
        return bids;
    }

}




























































































































































































































































































































































































































































































