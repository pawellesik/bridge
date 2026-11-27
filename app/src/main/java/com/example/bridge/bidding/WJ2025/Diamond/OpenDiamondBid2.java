package com.example.bridge.bidding.WJ2025.Diamond;

import com.example.bridge.bidding.Conventions.BlackwoodWJ2025;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class OpenDiamondBid2 extends WJ2025 {

    public static final Range OPEN_12_17 = new Range(12, 17);
    public static final Range OPEN_12_14 = new Range(12, 14);
    public static final Range OPEN_12_13 = new Range(12, 13);
    public static final Range OPEN_15_17 = new Range(15, 17);
    public static final Range OPEN_16_17 = new Range(16, 17);
    public static final Range OPEN_14_17 = new Range(14, 17);


    // =========================================================================
    // WIEDZA PUBLICZNA (PublicHandSummary) W 2. OKRĄŻENIU OTWIERAJĄCEGO N (1D)
    // =========================================================================

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 4-13 (lub 4441 z ♣)
    //  * Gracza S (Odpowiadający 1H): HCP: 7-28  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 7, 13), quality(Suit.Diamonds, SuitQuality.Excellent, SuitQuality.Solid), highCardPoints(OPEN_14_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3NT")),

                shows(Bid._3S, shape(0, 1), shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_15_17), setTrumpColor(Suit.Hearts), ruleShow(1), note("(splinter)"), id("OpenDiamondBid2.diamond_1H _3S")),
                shows(Bid._4C, shape(0, 1), shape(Suit.Hearts, 4, 13), highCardPoints(OPEN_15_17), setTrumpColor(Suit.Hearts), ruleShow(1), note("(splinter)"), id("OpenDiamondBid2.diamond_1H _4C")),
                shows(Bid._3H, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_15_17), setTrumpColor(Suit.Hearts), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3H")),

                shows(Bid._3D, shape(6, 13), shape(Suit.Hearts, 0, 2), shape(Suit.Spades, 2, 13), shape(Suit.Clubs, 2, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3D")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3C")),
                shows(Bid._2NT, shape(Suit.Diamonds, 6, 13), shape(Suit.Hearts, 0, 2), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _3C")),
                shows(Bid._2S, shape(Suit.Diamonds, 6, 13), shape(Suit.Hearts, 3, 3), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1H _2S")),

                shows(Bid._2H, shape(4, 4), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1H _2H")),
                shows(Bid._2D, shape(6, 13), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1H _2H")),
                shows(Bid._2C, shape(4, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1H _2C")),
                shows(Bid._1NT, highCardPoints(openRange14(ps)), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1NT")),
                shows(Bid._1S, shape(4, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1H _1S")),


                properties(new Call[]{Bid._1NT, Bid._1S}, RespondDiamondBid2::diamond_1H_1NT1S, false),
                properties(new Call[]{Bid._2C}, RespondDiamondBid2::diamond_1H_2C, true),
                properties(new Call[]{Bid._2D}, RespondDiamondBid2::diamond_1H_2D, false),
                properties(new Call[]{Bid._2H}, RespondDiamondBid2::diamond_1H_2H, false),
                properties(new Call[]{Bid._2S}, RespondDiamondBid2::diamond_1H_2S, true),
                properties(new Call[]{Bid._2NT}, RespondDiamondBid2::diamond_1H_2NT, true),
                properties(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_1H_3C, true),
                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_1H_3D, true),
                properties(new Call[]{Bid._3H}, RespondDiamondBid2::diamond_1H_3H, true),
                properties(new Call[]{Bid._3S}, RespondDiamondBid2::diamond_1H_3S, true),
                properties(new Call[]{Bid._4C}, RespondDiamondBid2::diamond_1H_4C, true),
                properties(new Call[]{Bid._3NT}, RespondDiamondBid2::diamond_1H_3NT, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 1S): HCP: 7-28  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2H, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _2H")),
                shows(Bid._2H, shape(Suit.Spades, 3, 4), shape(Suit.Diamonds, 6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _2H")),

                shows(Bid._3NT, shape(Suit.Diamonds, 7, 13), quality(Suit.Diamonds, SuitQuality.Excellent, SuitQuality.Solid), highCardPoints(OPEN_14_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _3NT")),
                shows(Bid._3H, shape(0, 1), shape(Suit.Spades, 4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _3H")),
                shows(Bid._4C, shape(0, 1), shape(Suit.Spades, 4, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _4C")),
                shows(Bid._3S, shape(4, 4), shape(Suit.Diamonds, 5, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1S _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Spades, 0, 2), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _3D")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _3C")),
                shows(Bid._2NT, shape(Suit.Diamonds, 6, 13), shape(Suit.Spades, 0, 2), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_1S _3C")),
                shows(Bid._2S, shape(Suit.Diamonds, 5, 13), shape(Suit.Spades, 4, 4), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1S _2S")),

                shows(Bid._2D, shape(6, 13), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1S _2H")),
                shows(Bid._2C, shape(4, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_1S _2C")),
                shows(Bid._1NT, highCardPoints(openRange14(ps)), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), ruleShow(1), id("OpenDiamondBid2.diamond_1S _1NT")),

                properties(new Call[]{Bid._1NT}, RespondDiamondBid2::diamond_1S_1NT, true),
                properties(new Call[]{Bid._2C}, RespondDiamondBid2::diamond_1S_2C, true),
                properties(new Call[]{Bid._2D}, RespondDiamondBid2::diamond_1S_2D, false),
                properties(new Call[]{Bid._2H}, RespondDiamondBid2::diamond_1S_2H, true),
                properties(new Call[]{Bid._2S}, RespondDiamondBid2::diamond_1S_2S, false),
                properties(new Call[]{Bid._2NT}, RespondDiamondBid2::diamond_1S_2NT, false),
                properties(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_1S_3C, false),
                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_1S_3D, false),
                properties(new Call[]{Bid._3S}, RespondDiamondBid2::diamond_1S_3S, false),
                properties(new Call[]{Bid._3H}, RespondDiamondBid2::diamond_1S_3H, false),
                properties(new Call[]{Bid._4C}, RespondDiamondBid2::diamond_1S_4C, false),
                properties(new Call[]{Bid._3NT}, RespondDiamondBid2::diamond_1S_3NT, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }


    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 1NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 1NT): HCP: 7-10  | Hearts: 0-3 | Spades: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2H, shape(4, 4), highCardPoints(OPEN_15_17), GOOD_PLUS_SUIT, ruleShow(1), id("OpenDiamondBid2.diamond_1NT _2H")),
                shows(Bid._2S, shape(4, 4), highCardPoints(OPEN_15_17), GOOD_PLUS_SUIT, ruleShow(1), id("OpenDiamondBid2.diamond_1NT _2S")),
                shows(Bid._3C, shape(5, 13), shape(Suit.Diamonds, 5, 13), highCardPoints(OPEN_15_17), GOOD_PLUS_SUIT, ruleShow(1), id("OpenDiamondBid2.diamond_1NT _3C")),
                properties(new Call[]{Bid._2H, Bid._2S}, RespondDiamondBid2::diamond_1NT_2H2S, true),
                properties(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_1NT_3C, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }


    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2C): HCP: 12-28 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3H, shape(0, 1), shape(Suit.Clubs, 4, 13), setTrumpColor(Suit.Clubs), highCardPoints(OPEN_15_17), ruleShow(1), note("(splinter)"), id("OpenDiamondBid2.diamond_2C _3H")), //splinter
                shows(Bid._3S, shape(0, 1), shape(Suit.Clubs, 4, 13), setTrumpColor(Suit.Clubs), highCardPoints(OPEN_15_17), ruleShow(1), note("(splinter)"), id("OpenDiamondBid2.diamond_2C _3S")), //splinter

                shows(Bid._3D, shape(6, 13), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_2C _3D")),
                shows(Bid._3C, shape(3, 13), highCardPoints(OPEN_15_17), not(hasShortness(0, 1)), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenDiamondBid2.diamond_2C _3D")),

                shows(Bid._2NT, highCardPoints(openRange17(ps)), shape4441(Suit.Clubs), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2NT")),

                shows(Bid._2H, shape(4, 4), highCardPoints(OPEN_15_17), shape(Suit.Diamonds, 5, 13), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2H")),
                shows(Bid._2S, shape(4, 4), highCardPoints(OPEN_15_17), shape(Suit.Diamonds, 5, 13), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2S")),

                shows(Bid._2D, highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2C _2D")),

                properties(new Call[]{Bid._2D}, RespondDiamondBid2::diamond_2C_2D, false),
                properties(new Call[]{Bid._2H}, RespondDiamondBid2::diamond_2C_2H, false),
                properties(new Call[]{Bid._2S}, RespondDiamondBid2::diamond_2C_2S, false),
                properties(new Call[]{Bid._2NT}, RespondDiamondBid2::diamond_2C_2NT, false),
                properties(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_2C_3C, false),
                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_2C_3D, false),
                properties(new Call[]{Bid._3H}, RespondDiamondBid2::diamond_2C_3H, false),
                properties(new Call[]{Bid._3S}, RespondDiamondBid2::diamond_2C_3S, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2D): HCP: 10-28 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2H, shape(4, 4), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2D _2H")),
                shows(Bid._2S, shape(4, 4), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2D _2H")),

                shows(Bid._3H, shape(0, 1), highCardPoints(OPEN_15_17), ruleShow(1), note("(splinter)"), id("OpenDiamondBid2.diamond_2D _3H")), //splinter
                shows(Bid._3S, shape(0, 1), highCardPoints(OPEN_15_17), ruleShow(1), note("(splinter)"), id("OpenDiamondBid2.diamond_2D _3S")), //splinter

                shows(Bid._3D, NOT_PAIR_BALANCED, highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2D _3D")),
                shows(Bid._2NT, PAIR_BALANCED, highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2D _2NT")),

                shows(Bid._3C, shape(4, 4), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2D _3C")),

                properties(new Call[]{Bid._2H}, RespondDiamondBid2::diamond_2D_2H, false),
                properties(new Call[]{Bid._2S}, RespondDiamondBid2::diamond_2D_2S, false),
                properties(new Call[]{Bid._2NT}, RespondDiamondBid2::diamond_2D_2NT, false),
                properties(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_2D_3C, false),
                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_2D_3D, false),
                properties(new Call[]{Bid._3H}, RespondDiamondBid2::diamond_2D_3H, false),
                properties(new Call[]{Bid._3S}, RespondDiamondBid2::diamond_2D_3S, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 2H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2H): HCP: 7-9   | Spades: 5-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3D, noFit(), shape(Suit.Spades, 0, 2), shape(Suit.Hearts, 0, 3), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2H _3D")),

                shows(Bid._4S, shape(Suit.Spades, 3, 4), highCardPoints(OPEN_16_17), id("OpenDiamondBid2.diamond_2H _4S")),
                shows(Bid._4H, fit(), highCardPoints(OPEN_16_17), id("OpenDiamondBid2.diamond_2H _4H")),

                shows(Bid._2NT, noFit(), PAIR_BALANCED, highCardPoints(OPEN_16_17), ruleShow(1), id("OpenDiamondBid2.diamond_2H _3C")),

                shows(Bid._3C, highCardPoints(OPEN_16_17), ruleShow(1), id("OpenDiamondBid2.diamond_2H _3C")),

                shows(Bid._2S, noFit(), shape(Suit.Spades, 4, 4), ruleShow(1), id("OpenDiamondBid2.diamond_2H _2S")),
                shows(Call.PASS, fit(), ruleShow(1), id("OpenDiamondBid2.diamond_2H _pass")),

                shows(Bid._2S, noFit(7), shape(Suit.Spades, 3, 3), betterThan(Suit.Spades, Suit.Hearts), id("OpenDiamondBid2.diamond_2H _2S")), //bez ruleShow

                properties(new Call[]{Bid._2S}, RespondDiamondBid2::diamond_2H_2S, false),
                properties(new Call[]{Bid._2NT}, RespondDiamondBid2::diamond_2H_2NT, false),
                properties(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_2H_3C, true),
                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_2H_3D, false),
                properties(new Call[]{Bid._4H}, RespondDiamondBid2::diamond_2H_4H, false),
                properties(new Call[]{Bid._4S}, RespondDiamondBid2::diamond_2H_4S, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 2S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2S): HCP: 13-28 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, BALANCED, highCardPoints(openRange13(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2S _3NT")),
                shows(Bid._3D, shape(6, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2S _3D")),
                shows(Bid._3C, shape(4, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2S _3C")),
                shows(Bid._3H, shape(4, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2S _3H")),
                shows(Bid._3S, shape(4, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2S _3S")),
                shows(Bid._2NT, BALANCED, highCardPoints(OPEN_14_17), ruleShow(1), id("OpenDiamondBid2.diamond_2S _2NT")),

                properties(new Call[]{Bid._2NT}, RespondDiamondBid2::diamond_2S_2NT, false),
                properties(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_2S_3C, false),
                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_2S_3D, false),
                properties(new Call[]{Bid._3H}, RespondDiamondBid2::diamond_2S_3H, false),
                properties(new Call[]{Bid._3S}, RespondDiamondBid2::diamond_2S_3S, false),
                properties(new Call[]{Bid._3NT}, RespondDiamondBid2::diamond_2S_3NT, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2NT): HCP: 11-12 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3D, highCardPoints(openRange14(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_2S _3D")),
                shows(Bid._3C, setTrumpColor(Suit.Diamonds), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid2.diamond_2S _3C")),

                propertiesForcingToGame(new Call[]{Bid._3C}, RespondDiamondBid2::diamond_2NT_3C, true),
                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_2NT_3D, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 3C): HCP: 10-11 | Clubs: 6-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._4C, fit(), highCardPoints(OPEN_16_17), ruleShow(1), id("OpenDiamondBid2.diamond_3C _4C")),
                shows(Bid._3H, highCardPoints(OPEN_15_17), stopped(Suit.Hearts), stopped(Suit.Diamonds), ruleShow(1), id("OpenDiamondBid2.diamond_3C _3H")),
                shows(Bid._3S, highCardPoints(OPEN_15_17), stopped(Suit.Spades), stopped(Suit.Diamonds), ruleShow(1), id("OpenDiamondBid2.diamond_3C _3S")),
                shows(Bid._3D, shape(6, 13), noFit(), ruleShow(1), DECENT_PLUS_SUIT, id("OpenDiamondBid2.diamond_3C _3D")),
                shows(Call.PASS, highCardPoints(openRange13(ps)), ruleShow(1), id("OpenDiamondBid2.diamond_3C _PASS")),

                properties(new Call[]{Bid._3D}, RespondDiamondBid2::diamond_3C_3D, false),
                properties(new Call[]{Bid._3H}, RespondDiamondBid2::diamond_3C_3H, true),
                properties(new Call[]{Bid._3S}, RespondDiamondBid2::diamond_3C_3S, true),
                properties(new Call[]{Bid._4C}, RespondDiamondBid2::diamond_3C_4C, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////Licytacja bez rules show/////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 3D) : HCP: 7-9   | Diamonds: 4-13 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, id("OpenDiamondBid2.diamond_3D _PASS")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 3H) : HCP: 13-28 | Diamonds: 4-13 | Hearts: 0-1
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(BlackwoodWJ2025.initiateConvention(ps));
        choices.addRules(
                shows(Bid._3S, shape(4, 4), DECENT_PLUS_SUIT, id("OpenDiamondBid2.diamond_3H _3S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 3S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 3S) : HCP: 13-28 | Diamonds: 4-13 | Spades: 0-1
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(BlackwoodWJ2025.initiateConvention(ps));
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    //  SEKWENCJA LICYTACJI: +
    //  Runda 1: 1D - Pass - 4C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 4C) : HCP: 13-28 | Diamonds: 4-13 | Clubs: 0-1
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(BlackwoodWJ2025.initiateConvention(ps));
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

}
