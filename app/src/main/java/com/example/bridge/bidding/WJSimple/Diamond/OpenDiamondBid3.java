package com.example.bridge.bidding.WJSimple.Diamond;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJSimple.WJSimple;

public class OpenDiamondBid3 extends WJSimple {
    public static final Range HIGHT = new Range(15, 17);
    public static final Range LOW = new Range(12, 14);
    public static final Range STANDARD = new Range(12, 17);

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
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::diamond_1H_1NT1S_2C_2D, true)
        );
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
                shows(Bid._3NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneHeart_oneSpade1NT_twoDiamond _3NT")),
                shows(Bid._3H, shape(3, 3), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneHeart_oneSpade1NT_twoDiamond _3H")),
                shows(Bid._2NT, shape(Suit.Hearts, 0, 2), stopped(Suit.Clubs), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_oneSpade1NT_twoDiamond _2NT")),
                shows(Bid._2S, shape(Suit.Hearts, 0, 2), not(stopped(Suit.Clubs)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_oneSpade1NT_twoDiamond _2S")),
                shows(Bid._2H, shape(3, 3), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_oneSpade1NT_twoDiamond _2H"))

        );
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
                shows(Bid._3NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3NT")),
                shows(Bid._3S, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 0, 2), not(stopped(Suit.Spades)), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3S")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 3, 3), highCardPoints(STANDARD), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 4, 4), not(stopped(Suit.Spades)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 5, 13), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._2NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Spades), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3D"))


        );
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
                shows(Bid._2D, ruleShow(1), id("OpenDiamondBid3.oneSpade_1NT_twoClub _2D")),
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::diamond_1S_1NT_2C_2D, true)
        );
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
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneSpade_1NT_twoDiamond _3NT")),
                shows(Bid._3S, shape(3, 3), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneSpade_1NT_twoDiamond _3H")),
                shows(Bid._2NT, shape(Suit.Spades, 0, 2), stopped(Suit.Clubs), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneSpade_1NT_twoDiamond _2NT")),
                shows(Bid._2S, shape(3, 3), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneSpade_1NT_twoDiamond _2S")),
                shows(Bid._2H, shape(Suit.Spades, 0, 2), not(stopped(Suit.Clubs)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneSpade_1NT_twoDiamond _2H"))

        );
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
                shows(Bid._3NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Hearts), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3NT")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Spades, 0, 2), not(stopped(Suit.Hearts)), highCardPoints(HIGHT), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3S")),
                shows(Bid._3S, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), shape(Suit.Spades, 3, 3), highCardPoints(STANDARD), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3H")),
                shows(Bid._3D, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 4, 4), not(stopped(Suit.Hearts)), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 13), shape(Suit.Clubs, 5, 13), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3D")),
                shows(Bid._2NT, shape(Suit.Diamonds, 5, 5), shape(Suit.Clubs, 4, 4), stopped(Suit.Hearts), highCardPoints(LOW), ruleShow(1), id("OpenDiamondBid3.oneHeart_twoClub_twoSpade _3D"))

        );
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
                shows(Bid._3NT, shape(Suit.Hearts, 4, 4), ruleShow(1), id("OpenDiamondBid3.oneSpade_twoHeard_2NT _3NT")),
                shows(Bid._3S, shape(Suit.Diamonds, 6, 13), shape(Suit.Spades, 3, 3), ruleShow(1), id("OpenDiamondBid3.oneSpade_twoHeard_2NT _3S")),
                shows(Bid._3H, shape(Suit.Diamonds, 5, 5), shape(Suit.Hearts, 4, 4), shape(Suit.Spades, 3, 3), ruleShow(1), id("OpenDiamondBid3.oneSpade_twoHeard_2NT _3H")),
                shows(Bid._3D, shape(6, 13), shape(Suit.Hearts, 4, 4), ruleShow(1), id("OpenDiamondBid3.oneSpade_twoHeard_2NT _3D")),
                shows(Bid._3C, shape(Suit.Diamonds, 5, 5), shape(Suit.Hearts, 4, 4), shape(Suit.Clubs, 3, 3), ruleShow(1), id("OpenDiamondBid3.oneSpade_twoHeard_2NT _3C"))
        );
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
        choices.addRules(
                shows(Bid._3S, shape(3, 3),shape(Suit.Diamonds, 6, 13), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_3H _3S")),
                shows(Bid._4H, shape(4, 4),shape(Suit.Diamonds, 5, 13), ruleShow(1), id("OpenDiamondBid3.diamond_1S_2H_3H _4H"))

                 );
        return choices;
    }


}
