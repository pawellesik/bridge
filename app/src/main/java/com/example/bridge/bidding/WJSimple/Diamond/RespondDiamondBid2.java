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


    // =========================================================================
    // WIEDZA PUBLICZNA (PublicHandSummary) W 2. OKRĄŻENIU ODPOWIADAJĄCEGO S (1D)
    // =========================================================================

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 1NT (lub 1S) - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 4-13 | Hearts: 0-3 | Spades: 0-3
    //  * Gracza S (Odpowiadający 1H): HCP: 7-28  | Hearts: 4-13
    //  -----------------------------------------------------------------------
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

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 2C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 5-13 | Clubs: 4-13
    //  * Gracza S (Odpowiadający 1H): HCP: 7-28  | Hearts: 4-13
    //  -----------------------------------------------------------------------
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


    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 1NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 4-13 | Spades: 0-3 | Hearts: 0-3
    //  * Gracza S (Odpowiadający 1S): HCP: 7-28  | Spades: 4-13
    //  -----------------------------------------------------------------------
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

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 5-13 | Clubs: 4-13
    //  * Gracza S (Odpowiadający 1S): HCP: 7-28  | Spades: 4-13
    //  -----------------------------------------------------------------------
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

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 5-13 | Hearts: 4-13
    //  * Gracza S (Odpowiadający 1S): HCP: 7-25  | Spades: 4-13
    //  -----------------------------------------------------------------------
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

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1NT - Pass
    //  Runda 2: 2H (lub 2S / 3C) - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 15-17 | Diamonds: 5-13 | Hearts/Spades/Clubs: 4-13
    //  * Gracza S (Odpowiadający 1NT): HCP: 7-10  | Hearts: 0-3 | Spades: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1NT_2H2S3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3D, fit(), noFit(), partner(isLastBid(Bid._2H, Bid._2C)), ruleShow(1), id("RespondDiamondBid2.diamond_1NT_2H2S3C _3D")),
                shows(Bid._3D, partner(isLastBid(Bid._3C)), betterThan(Suit.Clubs), ruleShow(1), id("RespondDiamondBid2.diamond_1NT_2H2S3C _3D"))

        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2H - Pass
    //  Runda 2: 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 16-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2H): HCP: 7-9   | Spades: 5-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2H_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, stopped(Suit.Clubs), ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3NT")),
                shows(Bid._3S, GOOD_PLUS_SUIT, ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3S")),
                shows(Bid._3H, shape(5, 13), ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3H")),
                shows(Bid._3D, shape(2, 2), ruleShow(1), id("RespondDiamondBid2.diamond_2H_3C _3D"))

        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2NT - Pass
    //  Runda 2: 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 15-17 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2NT): HCP: 11-12 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2NT_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._5D, fit(Suit.Diamonds), id("RespondDiamondBid2.diamond_2NT_3C _5D")),
                shows(Bid._3NT, BALANCED, id("RespondDiamondBid2.diamond_2NT_3C _3NT"))

        );
        return choices;
    }


    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////Licytacja bez rules show/////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 6-13 | Hearts: 0-2
    //  * Gracza S (Odpowiadający 1H): HCP: 7-28  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                // --- Końcówki (13-16 HCP) ---
                shows(Bid._3NT, PAIR_BALANCED, highCardPoints(13, 16), id("RespondDiamondBid2.diamond_1H_2D _3NT")),
                shows(Bid._5D, fit(), highCardPoints(13, 16), id("RespondDiamondBid2.diamond_1H_2D _5D")),

                // --- Inwity do końcówki (11-12 HCP) ---
                shows(Bid._3H, shape(Suit.Hearts, 6, 13), highCardPoints(11, 12), id("RespondDiamondBid2.diamond_1H_2D _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 3, 13), highCardPoints(11, 12), id("RespondDiamondBid2.diamond_1H_2D _3D")),
                shows(Bid._2NT, BALANCED, highCardPoints(11, 12), id("RespondDiamondBid2.diamond_1H_2D _2NT")),
                shows(Bid._2S, shape(Suit.Spades, 4, 13), highCardPoints(11, 12), id("RespondDiamondBid2.diamond_1H_2D _2S")),

                // --- Odzywki częściowe / Słabe (7-10 HCP) ---
                shows(Bid._2H, shape(Suit.Hearts, 6, 13), highCardPoints(7, 10), id("RespondDiamondBid2.diamond_1H_2D _2H")),
                shows(Call.PASS, highCardPoints(7, 10), id("RespondDiamondBid2.diamond_1H_2D PASS"))
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 2H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 4-13 | Hearts: 4-4
    //  * Gracza S (Odpowiadający 1H): HCP: 7-28  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                //todo blackwood
                shows(Bid._4H, setTrumpColor(Suit.Hearts), highCardPoints(13, 16), id("RespondDiamondBid2.diamond_1H_2H _4H")),
                shows(Bid._3H, setTrumpColor(Suit.Hearts), highCardPoints(11, 12), id("RespondDiamondBid2.diamond_1H_2H _3H")),
                shows(Call.PASS, highCardPoints(7, 10), id("RespondDiamondBid2.diamond_1H_2H PASS"))
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 2S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 6-13 | Hearts: 3-3
    //  * Gracza S (Odpowiadający 1H): HCP: 7-25  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                //todo blackwood

                // --- 2. Końcówki (10-16 HCP po silnym rebidzie 15-17 N) ---
                // a) 4H przy 5+ kierach S (N ma 3 + S ma 5 = fit 8+ kart w kierach)
                shows(Bid._4H, setTrumpColor(Suit.Hearts), shape(Suit.Hearts, 5, 13), highCardPoints(10, 16), id("RespondDiamondBid2.diamond_1H_2S _4H")),
                // c) 5D przy ficie karowym (N ma 6, S ma 3+ kara)
                shows(Bid._5D, fit(), highCardPoints(10, 16), id("RespondDiamondBid2.diamond_1H_2S _5D")),

                // b) 3NT przy 4 kierach S (7-kartowy fit, układ zrównoważony)
                shows(Bid._3NT, BALANCED, shape(Suit.Hearts, 4, 4), highCardPoints(10, 16), id("RespondDiamondBid2.diamond_1H_2S _3NT")),

                // --- 3. Inwity / Bilansowanie przy dół odpowiedzi (7-9 HCP) ---
                shows(Bid._3H, shape(Suit.Hearts, 5, 13), highCardPoints(7, 9), id("RespondDiamondBid2.diamond_1H_2S _3H")),
                shows(Bid._3D, fit(), highCardPoints(7, 9), id("RespondDiamondBid2.diamond_1H_2S _3D")),
                shows(Bid._2NT, id("RespondDiamondBid2.diamond_1H_2S _2NT"))
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 6-13 | Hearts: 0-2
    //  * Gracza S (Odpowiadający 1H): HCP: 7-25  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                //todo blackwood

                shows(Bid._5D, fit(), highCardPoints(10, 14), id("RespondDiamondBid2.diamond_1H_2NT _5D")),

                shows(Bid._3S, shape(4, 13), highCardPoints(10, 14), id("RespondDiamondBid2.diamond_1H_2NT _3S")),

                shows(Bid._3H, shape(Suit.Hearts, 6, 13), highCardPoints(10, 14), id("RespondDiamondBid2.diamond_1H_2NT _4H")),

                shows(Bid._3NT, highCardPoints(10, 14), id("RespondDiamondBid2.diamond_1H_2NT _3NT")),

                shows(Bid._3D, fit(7), highCardPoints(7, 9), id("RespondDiamondBid2.diamond_1H_2NT _3D"))
        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 5-13 | Clubs: 5-13
    //  * Gracza S (Odpowiadający 1H): HCP: 7-25  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                //todo blackwood

                shows(Bid._5D, fit(), highCardPoints(7, 16), id("RespondDiamondBid2.diamond_1H_3C _5D")),
                shows(Bid._5C, fit(), highCardPoints(7, 16), id("RespondDiamondBid2.diamond_1H_3C _5C")),

                shows(Call.PASS, betterThan(Suit.Diamonds), highCardPoints(7, 9), id("RespondDiamondBid2.diamond_1H_2NT _pass")),
                shows(Bid._3D, betterThan(Suit.Clubs), highCardPoints(7, 9), id("RespondDiamondBid2.diamond_1H_2NT _pass")),
                shows(Bid._3H, shape(6, 13), shape(Suit.Diamonds, 0, 2), shape(Suit.Clubs, 0, 2), highCardPoints(10, 16), id("RespondDiamondBid2.diamond_1H_2NT _pass")),
                shows(Bid._3NT, stopped(Suit.Hearts), stopped(Suit.Spades), highCardPoints(10, 16), id("RespondDiamondBid2.diamond_1H_2NT 3NT"))

        );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 6-13 | Hearts: 0-2
    //  * Gracza S (Odpowiadający 1H): HCP: 7-25  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 5-13 | Hearts: 4-4
    //  * Gracza S (Odpowiadający 1H): HCP: 7-28  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 3S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Hearts: 4-13 | Spades: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 1H): HCP: 7-25  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 4C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Hearts: 4-13 | Clubs: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 1H): HCP: 7-25  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 3NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 14-17 | Diamonds: 7-13
    //  * Gracza S (Odpowiadający 1H): HCP: 7-26  | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 6-13
    //  * Gracza S (Odpowiadający 1S): HCP: 7-28  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 5-13 | Spades: 4-4
    //  * Gracza S (Odpowiadający 1S): HCP: 7-28  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 6-13 | Spades: 0-2
    //  * Gracza S (Odpowiadający 1S): HCP: 7-25  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 5-13 | Clubs: 5-13
    //  * Gracza S (Odpowiadający 1S): HCP: 7-25  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 6-13 | Spades: 0-2
    //  * Gracza S (Odpowiadający 1S): HCP: 7-25  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 3S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 5-13 | Spades: 4-4
    //  * Gracza S (Odpowiadający 1S): HCP: 7-28  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Spades: 4-13 | Hearts: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 1S): HCP: 7-25  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 4C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Spades: 4-13 | Clubs: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 1S): HCP: 7-25  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 3NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 14-17 | Diamonds: 7-13
    //  * Gracza S (Odpowiadający 1S): HCP: 7-26  | Spades: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }


    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2C): HCP: 12-28 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 2H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Hearts: 4-4
    //  * Gracza S (Odpowiadający 2C): HCP: 12-25 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 2S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Spades: 4-4
    //  * Gracza S (Odpowiadający 2C): HCP: 12-25 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | shape4441 z ♣
    //  * Gracza S (Odpowiadający 2C): HCP: 12-28 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Clubs: 3-13
    //  * Gracza S (Odpowiadający 2C): HCP: 12-25 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 6-13
    //  * Gracza S (Odpowiadający 2C): HCP: 12-25 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Clubs: 4-13 | Hearts: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 2C): HCP: 12-25 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2C - Pass
    //  Runda 2: 3S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Clubs: 4-13 | Spades: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 2C): HCP: 12-25 | Clubs: 5-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2C_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2D - Pass
    //  Runda 2: 2H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Hearts: 4-4
    //  * Gracza S (Odpowiadający 2D): HCP: 10-28 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D_2H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2D - Pass
    //  Runda 2: 2S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Spades: 4-4
    //  * Gracza S (Odpowiadający 2D): HCP: 10-28 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2D - Pass
    //  Runda 2: 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | PAIR_BALANCED
    //  * Gracza S (Odpowiadający 2D): HCP: 10-28 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2D - Pass
    //  Runda 2: 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Clubs: 4-4
    //  * Gracza S (Odpowiadający 2D): HCP: 10-28 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2D - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | NOT_PAIR_BALANCED
    //  * Gracza S (Odpowiadający 2D): HCP: 10-28 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2D - Pass
    //  Runda 2: 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 4-13 | Clubs: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 2D): HCP: 10-25 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2D - Pass
    //  Runda 2: 3S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | Diamonds: 4-13 | Clubs: 0-1 (Splinter)
    //  * Gracza S (Odpowiadający 2D): HCP: 10-25 | Diamonds: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2D_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2H - Pass
    //  Runda 2: 2S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Spades: 4-4
    //  * Gracza S (Odpowiadający 2H): HCP: 7-9   | Spades: 5-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2H_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2H - Pass
    //  Runda 2: 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 16-17 | PAIR_BALANCED | Spades: 0-2
    //  * Gracza S (Odpowiadający 2H): HCP: 7-9   | Spades: 5-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2H_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2H - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-14 | Diamonds: 4-13 | Spades: 0-2
    //  * Gracza S (Odpowiadający 2H): HCP: 7-9   | Spades: 5-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2H_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2H - Pass
    //  Runda 2: 4H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 16-17 | fit w ♥
    //  * Gracza S (Odpowiadający 2H): HCP: 7-9   | Spades: 5-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2H_4H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2H - Pass
    //  Runda 2: 4S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 16-17 | Spades: 4-4
    //  * Gracza S (Odpowiadający 2H): HCP: 7-9   | Spades: 5-13 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2H_4S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2S - Pass
    //  Runda 2: 2NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 14-17 | BALANCED
    //  * Gracza S (Odpowiadający 2S): HCP: 13-26 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2S_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2S - Pass
    //  Runda 2: 3C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Clubs: 4-13
    //  * Gracza S (Odpowiadający 2S): HCP: 13-28 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2S_3C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2S - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 6-13
    //  * Gracza S (Odpowiadający 2S): HCP: 13-28 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2S_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2S - Pass
    //  Runda 2: 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Hearts: 4-13
    //  * Gracza S (Odpowiadający 2S): HCP: 13-28 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2S_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2S - Pass
    //  Runda 2: 3S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Spades: 4-13
    //  * Gracza S (Odpowiadający 2S): HCP: 13-28 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2S_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2S - Pass
    //  Runda 2: 3NT - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-13 | BALANCED
    //  * Gracza S (Odpowiadający 2S): HCP: 13-28 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2S_3NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 2NT - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-14 | Diamonds: 4-13
    //  * Gracza S (Odpowiadający 2NT): HCP: 11-12 | Spades: 0-3 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_2NT_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 3C - Pass
    //  Runda 2: 3D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 12-17 | Diamonds: 6-13 | noFit()
    //  * Gracza S (Odpowiadający 3C): HCP: 10-11 | Clubs: 6-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3C_3D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 3C - Pass
    //  Runda 2: 3H - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | fit(♣) | stopped(♥)
    //  * Gracza S (Odpowiadający 3C): HCP: 10-11 | Clubs: 6-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3C_3H(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 3C - Pass
    //  Runda 2: 3S - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 15-17 | fit(♣) | stopped(♠)
    //  * Gracza S (Odpowiadający 3C): HCP: 10-11 | Clubs: 6-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3C_3S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 3C - Pass
    //  Runda 2: 4C - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary po trimHCP):
    //  * Gracza N (Otwierający 1D) : HCP: 16-17 | fit(♣)
    //  * Gracza S (Odpowiadający 3C): HCP: 10-11 | Clubs: 6-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_3C_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        return choices;
    }
}
