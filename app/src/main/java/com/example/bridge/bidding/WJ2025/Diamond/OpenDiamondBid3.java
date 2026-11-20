package com.example.bridge.bidding.WJ2025.Diamond;

import com.example.bridge.bidding.Conventions.Blackwood;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class OpenDiamondBid3 extends WJ2025 {
    public static final Range OPEN_15_17 = new Range(15, 17);
    public static final Range OPEN_12_14 = new Range(12, 14);
    public static final Range OPEN_12_17 = new Range(12, 17);

    // =========================================================================
    // WIEDZA PUBLICZNA (PublicHandSummary) W 3. OKRĄŻENIU OTWIERAJĄCEGO N (1D)
    // =========================================================================

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 1NT (lub 1S) - Pass - 2C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-14 | Diamonds: 4-13 | Hearts: 0-2 | Spades: 0-3
    //  * Gracza S (Odpowiadający 1H): HCP: 10-12 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_1NT1S_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2D, ruleShow(1), id("OpenDiamondBid3.oneHeart_oneSpade1NT_twoClub _2D")),
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::diamond_1H_1NT1S_2C_2D, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }


    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 1NT (lub 1S) - Pass - 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-14 | Diamonds: 4-13 | Hearts: 0-2 | Spades: 0-3
    //  * Gracza S (Odpowiadający 1H): HCP: 10-12 | Hearts: 4-13 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_1NT1S_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1H_1NT1S_2D _3NT")),
                shows(Bid._3H, shape(3, 3), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1H_1NT1S_2D _3H")),//plesik nigdy nie wystąpi !!!
                shows(Bid._2NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1H_1NT1S_2D _2NT")),
                shows(Bid._2S, shape(Suit.Hearts, 0, 2), not(stopped(Suit.Clubs)), ruleShow(1), id("OpenDiamondBid3.diamond_1H_1NT1S_2D _2S")),
                shows(Bid._2H, shape(3, 3), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1H_1NT1S_2D _2H")),
                properties(new Call[]{Bid._2S}, RespondDiamondBid3::diamond_1H_1NT1S_2D_2S, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 2C - Pass - 2S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13 | Clubs: 4-13
    //  * Gracza S (Odpowiadający 1H): HCP: 16-17 | Hearts: 4-13 | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_2C_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1H_2C_2S _3NT")),
                shows(Bid._3S, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 0, 2), not(stopped(Suit.Spades)), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1H_2C_2S _3S")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 3, 3), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenDiamondBid3.diamond_1H_2C_2S _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 4, 4), not(stopped(Suit.Spades)), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1H_2C_2S _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 5, 13), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1H_2C_2S _3D")),
                shows(Bid._2NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1H_2C_2S _3D")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 1NT - Pass - 2C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-14 | Diamonds: 4-13 | Spades: 0-2 | Hearts: 0-3
    //  * Gracza S (Odpowiadający 1S): HCP: 10-12 | Spades: 4-13 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_1NT_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2D, ruleShow(1), id("OpenDiamondBid3.diamond_1S_1NT_2C _2D")),
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::diamond_1S_1NT_2C_2D, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 1NT - Pass - 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-14 | Diamonds: 4-13 | Spades: 0-2 | Hearts: 0-3
    //  * Gracza S (Odpowiadający 1S): HCP: 10-12 | Spades: 4-13 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_1NT_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1S_1NT_2D _3NT")),
                shows(Bid._3S, shape(3, 3), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1S_1NT_2D _3H")),
                shows(Bid._2NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1S_1NT_2D _2NT")),
                shows(Bid._2S, shape(3, 3), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1S_1NT_2D _2S")),
                shows(Bid._2H, shape(Suit.Spades, 0, 2), not(stopped(Suit.Clubs)), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1S_1NT_2D _2H")),
                properties(new Call[]{Bid._2S}, RespondDiamondBid3::diamond_1S_1NT_2D_2S, true),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2C - Pass - 2H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-17 | Diamonds: 4-13 | Clubs: 4-13
    //  * Gracza S (Odpowiadający 1S): HCP: 16-17 | Spades: 4-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_2C_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Hearts), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2C_2H _3NT")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Spades, 0, 2), not(stopped(Suit.Hearts)), highCardPoints(OPEN_15_17), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2C_2H _3S")),
                shows(Bid._3S, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Spades, 3, 3), highCardPoints(OPEN_12_17), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2C_2H _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 4, 4), not(stopped(Suit.Hearts)), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2C_2H _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 5, 13), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2C_2H _3D")),
                shows(Bid._2NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Hearts), highCardPoints(OPEN_12_14), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2C_2H _3D")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }


    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2H - Pass - 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 15-17 | Diamonds: 5-13 | Hearts: 4-13
    //  * Gracza S (Odpowiadający 1S): HCP: 10-12 | Spades: 4-13 | IsBalanced: true
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_2H_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 4, 4), shape(Suit.Diamonds, 5, 5), shape(Suit.Spades, 2, 2), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_2NT _3NT")),
                shows(Bid._3S, shape(Suit.Diamonds, 6, 13), shape(Suit.Spades, 3, 3), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_2NT _3S")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Hearts, 4, 4), shape(Suit.Spades, 3, 3), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_2NT _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Hearts, 4, 4), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_2NT _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 5), shape(Suit.Hearts, 4, 4), shape(Suit.Clubs, 3, 3), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_2NT _3C")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2H - Pass - 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 15-17 | Diamonds: 5-13 | Hearts: 4-13
    //  * Gracza S (Odpowiadający 1S): HCP: 10-12 | Spades: 4-13 | Hearts: 3-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_2H_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(
                shows(Bid._3S, shape(3, 3), shape(Suit.Diamonds, 6, 13), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_3H _3S")),
                shows(Bid._4H, shape(4, 4), shape(Suit.Diamonds, 5, 13), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_3H _4H")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////Licytacja bez rules show/////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static PositionCalls diamond_1S_2H_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(
                shows(Bid._4S, fit(), id("OpenDiamondBid3.diamond_1S_2H_3C _4S")),
                shows(Bid._3NT, stopped(Suit.Clubs), id("OpenDiamondBid3.diamond_1S_2H_3C _3NT")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Spades, 3, 3), id("OpenDiamondBid3.diamond_1S_2H_3C _3D")),
                shows(Bid._3H, shape(4, 13), id("OpenDiamondBid3.diamond_1S_2H_3C _3H")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls diamond_3C_3S_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._5C, fit(), id("RespondDiamondBid2.diamond_3C_3S_4C _5C")),
                shows(Bid._4D, shape(5, 13), id("RespondDiamondBid2.diamond_3C_3S_4C _4D")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );

        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls diamond_3C_3H_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._5C, fit(), id("RespondDiamondBid2.diamond_3C_3S_4C _5C")),
                shows(Bid._4D, shape(5, 13), id("RespondDiamondBid2.diamond_3C_3S_4C _4D")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls diamond_2H_3C_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._4H, fit(), id("RespondDiamondBid2.diamond_2H_3C_3H _4H")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls diamond_2H_3C_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._4S, fit(7), id("RespondDiamondBid2.diamond_2H_3C_3S _4S")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls diamond_1NT_2H2S_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Call.PASS, shape(Suit.Diamonds, 5, 13), id("RespondDiamondBid2.diamond_1NT_2H2S_3D pass")),
                shows(Bid._3H, shape(4, 4), IS_NEW_SUIT, id("RespondDiamondBid2.diamond_1NT_2H2S_3D _3H")),
                shows(Bid._3S, shape(4, 4), IS_NEW_SUIT, id("RespondDiamondBid2.diamond_1NT_2H2S_3D _3S")),
                properties(new Call[]{Bid._3H, Bid._3S}, RespondDiamondBid3::diamond_1NT_2H2S_3D_3H3S, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }


}
