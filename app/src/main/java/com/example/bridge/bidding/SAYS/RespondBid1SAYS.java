package com.example.bridge.bidding.SAYS;

import com.example.bridge.bidding.Conventions.Jacoby2NT;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RespondBid1SAYS extends SAYS {
    public static final Range RESPOND_PASS = new Range(0, 5);
    public static final Range MINIMUM_HAND = new Range(6, 10);
    public static final Range INVITE_HAND = new Range(11, 12);
    public static final Range GAME_FORCE_HAND = new Range(13, 28);
    public static final Range SLAM_INVITE_HAND = new Range(17, 28);
    public static final Range WEAK_LONG = new Range(6, 10);

    public static PositionCalls oneClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                // 1D: 6+ PC, 4+ diamonds
                shows(Bid._1D, highCardPoints(6, 28), shape(Suit.Diamonds, 4, 13), id("RespondSAYS.oneClub _1D")),
                // 1H: 6+ PC, 4+ hearts (excludes 4 diamonds)
                shows(Bid._1H, highCardPoints(6, 28), shape(Suit.Hearts, 4, 13), shape(Suit.Diamonds, 0, 3), id("RespondSAYS.oneClub _1H")),
                // 1S: 6+ PC, 4+ spades (excludes 4 diamonds, 4 hearts)
                shows(Bid._1S, highCardPoints(6, 28), shape(Suit.Spades, 4, 13), shape(Suit.Diamonds, 0, 3), shape(Suit.Hearts, 0, 3), id("RespondSAYS.oneClub _1S")),

                // 1NT: 6-10 PC, balanced, excludes 4D, 4H, 4S, 5C
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Diamonds, 0, 3), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), shape(Suit.Clubs, 0, 4), BALANCED, id("RespondSAYS.oneClub _1NT")),

                // 2C: 6-9 PC, 5+ clubs, excludes 4D, 4H, 4S
                shows(Bid._2C, highCardPoints(6, 9), shape(Suit.Clubs, 5, 13), shape(Suit.Diamonds, 0, 3), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), id("RespondSAYS.oneClub _2C")),

                // 2NT: 13-14 PC, balanced, no 4-card major
                shows(Bid._2NT, highCardPoints(13, 14), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), BALANCED, id("RespondSAYS.oneClub _2NT")),

                // 3C: 10-12 PC, 5+ clubs, excludes 4D, 4H, 4S
                shows(Bid._3C, highCardPoints(10, 12), shape(Suit.Clubs, 5, 13), shape(Suit.Diamonds, 0, 3), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), id("RespondSAYS.oneClub _3C")),

                // 3NT: 15-17 PC, balanced, no 4-card major
                shows(Bid._3NT, highCardPoints(15, 17), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), BALANCED, id("RespondSAYS.oneClub _3NT")),

                // Pass: 0-5 PC
                shows(Call.PASS, highCardPoints(RESPOND_PASS), id("RespondSAYS.oneClub PASS")),

                properties(new Call[]{Bid._1D}, OpenBid2Bid1SAYS::responderNegat, false),
                properties(new Call[]{Bid._1S, Bid._1H}, OpenBid2Bid1SAYS::responderdTrumpMajorClub, false),
                properties(new Call[]{Bid._2C}, OpenBid2Bid1SAYS::responderdTrumpMinorClub, false),
                properties(new Call[]{Bid._3C}, OpenBid2Bid1SAYS::responderdRaiseTrumpMinorClub, true),
                properties(new Call[]{Bid._1NT}, OpenBid2Bid1SAYS::responderd1NTClub, true),
                properties(new Call[]{Bid._2NT}, OpenBid2Bid1SAYS::responderd2NTClub, true)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls oneDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                // 1H: 6+ PC, 4+ hearts
                shows(Bid._1H, highCardPoints(6, 28), shape(Suit.Hearts, 4, 13), id("RespondSAYS.oneDiamond _1H")),
                // 1S: 6+ PC, 4+ spades (excludes 4 hearts)
                shows(Bid._1S, highCardPoints(6, 28), shape(Suit.Spades, 4, 13), shape(Suit.Hearts, 0, 3), id("RespondSAYS.oneDiamond _1S")),

                // 1NT: 6-10 PC, excludes 4H, 4S, 5C
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), BALANCED, id("RespondSAYS.oneDiamond _1NT")),

                // 2C: 11+ PC, 5+ clubs, excludes 4H, 4S
                shows(Bid._2C, highCardPoints(11, 28), shape(Suit.Clubs, 5, 13), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), id("RespondSAYS.oneDiamond _2C")),

                // 2D: 6-9 PC, 4+ diamonds, excludes 4H, 4S
                shows(Bid._2D, highCardPoints(6, 9), shape(Suit.Diamonds, 4, 13), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), setTrumpColor(Suit.Diamonds), id("RespondSAYS.oneDiamond _2D")),

                // 2NT: 13-14 PC, balanced, no 4-card major
                shows(Bid._2NT, highCardPoints(13, 14), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), BALANCED, id("RespondSAYS.oneDiamond _2NT")),

                // 3D: 10-12 PC, 4+ diamonds, excludes 4H, 4S
                shows(Bid._3D, highCardPoints(10, 12), shape(Suit.Diamonds, 4, 13), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), setTrumpColor(Suit.Diamonds), id("RespondSAYS.oneDiamond _3D")),

                // 3NT: 15-17 PC, balanced, no 4-card major
                shows(Bid._3NT, highCardPoints(15, 17), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), BALANCED, id("RespondSAYS.oneDiamond _3NT")),

                // Pass: 0-5 PC
                shows(Call.PASS, highCardPoints(RESPOND_PASS), id("RespondSAYS.oneDiamond PASS")),

                propertiesAgreeTrump(new Call[]{Bid._2D, Bid._3D}, OpenBid2Bid1SAYS::responderdTrumpMinorDiamod2D, true),
                properties(new Call[]{Bid._1S, Bid._1H, Bid._2C}, OpenBid2Bid1SAYS::responderChangedSuitsDiamond, false),
                properties(new Call[]{Bid._1NT}, RecursionSAYS::recursionFindFitGame, false),
                properties(new Call[]{Bid._2NT}, RecursionSAYS::recursionFindFitGame, true)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        // Jacoby 2NT convention (13+ PC, 3+ heart fit)
        choices.addRules(Jacoby2NT.initiateConvention(ps));

        choices.addRules(
                // 1S: 6+ PC, 4+ spades
                shows(Bid._1S, highCardPoints(6, 28), shape(Suit.Spades, 4, 13), id("RespondSAYS.oneHeart _1S")),

                // 1NT: 6-10 PC, excludes 4 spades
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Spades, 0, 3), BALANCED, id("RespondSAYS.oneHeart _1NT")),

                // 2C: 11+ PC, 4+ clubs, excludes 4 spades
                shows(Bid._2C, highCardPoints(11, 28), shape(Suit.Clubs, 4, 13), shape(Suit.Spades, 0, 3), id("RespondSAYS.oneHeart _2C")),

                // 2D: 11+ PC, 4+ diamonds, excludes 4 spades, 4 clubs
                shows(Bid._2D, highCardPoints(11, 28), shape(Suit.Diamonds, 4, 13), shape(Suit.Clubs, 0, 3), shape(Suit.Spades, 0, 3), id("RespondSAYS.oneHeart _2D")),

                // 2H: 6-10 PC, 3+ heart fit
                shows(Bid._2H, highCardPoints(6, 10), shape(Suit.Hearts, 3, 13), setTrumpColor(Suit.Hearts), id("RespondSAYS.oneHeart _2H")),

                // 3H: 10-12 PC, 3+ heart fit (invite)
                shows(Bid._3H, highCardPoints(10, 12), shape(Suit.Hearts, 3, 13), setTrumpColor(Suit.Hearts), id("RespondSAYS.oneHeart _3H")),

                // 2S / 3C / 3D: 17+ PC natural slam invite
                shows(Bid._2S, highCardPoints(SLAM_INVITE_HAND), shape(Suit.Spades, 5, 13), id("RespondSAYS.oneHeart _2S_SlamTry")),
                shows(Bid._3C, highCardPoints(SLAM_INVITE_HAND), shape(Suit.Clubs, 5, 13), id("RespondSAYS.oneHeart _3C_SlamTry")),
                shows(Bid._3D, highCardPoints(SLAM_INVITE_HAND), shape(Suit.Diamonds, 5, 13), id("RespondSAYS.oneHeart _3D_SlamTry")),

                // 3NT: 15-17 PC, balanced, no heart fit
                shows(Bid._3NT, highCardPoints(15, 17), shape(Suit.Hearts, 0, 2), BALANCED, id("RespondSAYS.oneHeart _3NT")),

                // 4H: max 10 PC, 4+ heart fit, good distribution
                shows(Bid._4H, highCardPoints(0, 10), shape(Suit.Hearts, 4, 13), NOT_BALANCED, setTrumpColor(Suit.Hearts), id("RespondSAYS.oneHeart _4H")),

                // Pass: 0-5 PC
                shows(Call.PASS, highCardPoints(RESPOND_PASS), id("RespondSAYS.oneHeart PASS")),

                propertiesAgreeTrump(new Call[]{Bid._2H, Bid._3H}, OpenBid2Bid1SAYS::responderTrumpMajorHeart, true),
                properties(new Call[]{Bid._1S, Bid._2C, Bid._2D}, OpenBid2Bid1SAYS::responderChangedSuitsHeart, false),
                properties(new Call[]{Bid._1NT}, RecursionSAYS::recursionFindFitGame, false),
                properties(new Call[]{Bid._2NT}, RecursionSAYS::recursionFindFitGame, true)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls oneSpade(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);

        // Jacoby 2NT convention (13+ PC, 3+ spade fit)
        choices.addRules(Jacoby2NT.initiateConvention(ps));

        choices.addRules(
                // 1NT: 6-10 PC, excludes 3 spades
                shows(Bid._1NT, highCardPoints(MINIMUM_HAND), shape(Suit.Spades, 0, 2), BALANCED, id("RespondSAYS.oneSpade _1NT")),

                // 2C: 11+ PC, 4+ clubs
                shows(Bid._2C, highCardPoints(11, 28), shape(Suit.Clubs, 4, 13), id("RespondSAYS.oneSpade _2C")),

                // 2D: 11+ PC, 4+ diamonds, excludes 4 clubs
                shows(Bid._2D, highCardPoints(11, 28), shape(Suit.Diamonds, 4, 13), shape(Suit.Clubs, 0, 3), id("RespondSAYS.oneSpade _2D")),

                // 2H: 11+ PC, 4+ hearts, excludes 4 clubs, 4 diamonds
                shows(Bid._2H, highCardPoints(11, 28), shape(Suit.Hearts, 4, 13), shape(Suit.Clubs, 0, 3), shape(Suit.Diamonds, 0, 3), id("RespondSAYS.oneSpade _2H")),

                // 2S: 6-10 PC, 3+ spade fit
                shows(Bid._2S, highCardPoints(6, 10), shape(Suit.Spades, 3, 13), setTrumpColor(Suit.Spades), id("RespondSAYS.oneSpade _2S")),

                // 3S: 10-12 PC, 3+ spade fit (invite)
                shows(Bid._3S, highCardPoints(10, 12), shape(Suit.Spades, 3, 13), setTrumpColor(Suit.Spades), id("RespondSAYS.oneSpade _3S")),

                // 3C / 3D / 3H: 17+ PC natural slam invite
                shows(Bid._3C, highCardPoints(SLAM_INVITE_HAND), shape(Suit.Clubs, 5, 13), id("RespondSAYS.oneSpade _3C_SlamTry")),
                shows(Bid._3D, highCardPoints(SLAM_INVITE_HAND), shape(Suit.Diamonds, 5, 13), id("RespondSAYS.oneSpade _3D_SlamTry")),
                shows(Bid._3H, highCardPoints(SLAM_INVITE_HAND), shape(Suit.Hearts, 5, 13), id("RespondSAYS.oneSpade _3H_SlamTry")),

                // 3NT: 15-17 PC, balanced, no spade fit
                shows(Bid._3NT, highCardPoints(15, 17), shape(Suit.Spades, 0, 2), BALANCED, id("RespondSAYS.oneSpade _3NT")),

                // 4S: max 10 PC, 4+ spade fit, good distribution
                shows(Bid._4S, highCardPoints(0, 10), shape(Suit.Spades, 4, 13), NOT_BALANCED, setTrumpColor(Suit.Spades), id("RespondSAYS.oneSpade _4S")),

                // Pass: 0-5 PC
                shows(Call.PASS, highCardPoints(RESPOND_PASS), id("RespondSAYS.oneSpade PASS")),

                propertiesAgreeTrump(new Call[]{Bid._2S, Bid._3S}, OpenBid2Bid1SAYS::responderTrumpMajorSpade, true),
                properties(new Call[]{Bid._2H, Bid._2C, Bid._2D}, OpenBid2Bid1SAYS::responderChangedSuitsSpade, false),
                properties(new Call[]{Bid._1NT}, RecursionSAYS::recursionFindFitGame, false),
                properties(new Call[]{Bid._2NT}, RecursionSAYS::recursionFindFitGame, true)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static Iterable<CallFeature> weakOpen(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._4H, FIT_8_PLUS, setTrumpColor(Suit.Hearts), ruleOf17(), id("RespondSAYS.weakOpen _4H")));
        bids.add(shows(Bid._4H, fit(10), setTrumpColor(Suit.Hearts), id("RespondSAYS.weakOpen _4H")));
        bids.add(shows(Bid._4S, FIT_8_PLUS, setTrumpColor(Suit.Spades), ruleOf17(), id("RespondSAYS.weakOpen _4S")));
        bids.add(shows(Bid._4S, fit(10), setTrumpColor(Suit.Spades), id("RespondSAYS.weakOpen _4S")));
        bids.add(shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(10), setTrumpColor(Suit.Clubs), id("RespondSAYS.weakOpen _5C")));
        bids.add(shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), fit(10), setTrumpColor(Suit.Diamonds), id("RespondSAYS.weakOpen _5D")));
        for (CallFeature cf : CompeteSAYS.compBids(ps)) {
            bids.add(cf);
        }
        return bids;
    }
}
