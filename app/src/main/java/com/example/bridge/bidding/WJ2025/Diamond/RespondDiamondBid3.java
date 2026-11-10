package com.example.bridge.bidding.WJ2025.Diamond;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondDiamondBid3 extends WJ2025 {

    public static final Range TO_GAME = new Range(13, 16);
    public static final Range LOW = new Range(10, 11);
    public static final Range MEDUIM = new Range(10, 12);
    public static final Range HIGHT = new Range(11, 12);


    // =========================================================================
    // WIEDZA PUBLICZNA (PublicHandSummary) W 3. OKRĄŻENIU ODPOWIADAJĄCEGO S (1D)
    // =========================================================================

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1H - Pass
    //  Runda 2: 1NT (lub 1S) - Pass - 2C - Pass
    //  Runda 3: 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-14 | Diamonds: 4-13 | Hearts: 0-2 | Spades: 0-3
    //  * Gracza S (Odpowiadający 1H): HCP: 10-12 | Hearts: 4-13
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1H_1NT1S_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, highCardPoints(TO_GAME), BALANCED, ruleShow(1), id("RespondDiamondBid3.twoDiamond _3NT")),
                shows(Bid._3D, shape(Suit.Hearts, 5, 5), shape(Suit.Diamonds, 4, 13), highCardPoints(MEDUIM), ruleShow(1), id("RespondDiamondBid3.twoDiamond _3D")),
                shows(Bid._3C, shape(Suit.Hearts, 4, 4), shape(Suit.Clubs, 5, 13), highCardPoints(MEDUIM), ruleShow(1), id("RespondDiamondBid3.twoDiamond _3C")),
                shows(Bid._2NT, shape(Suit.Hearts, 5, 5), BALANCED, highCardPoints(HIGHT), ruleShow(1), id("RespondDiamondBid3.twoDiamond _3C")),
                shows(Bid._2S, shape(4, 4), highCardPoints(LOW), ruleShow(1), id("RespondDiamondBid3.twoDiamond _2S")),
                shows(Bid._2H, shape(5, 13), highCardPoints(MEDUIM), ruleShow(1), id("RespondDiamondBid3.twoDiamond _2H")),
                shows(Call.PASS, id("RespondDiamondBid3.twoDiamond PASS"))

                );
        return choices;
    }

    //  SEKWENCJA LICYTACJI:
    //  Runda 1: 1D - Pass - 1S - Pass
    //  Runda 2: 1NT - Pass - 2C - Pass
    //  Runda 3: 2D - Pass
    //  -----------------------------------------------------------------------
    //  WIEDZA PUBLICZNA (PublicHandSummary):
    //  * Gracza N (Otwierający 1D)  : HCP: 12-14 | Diamonds: 4-13 | Spades: 0-2 | Hearts: 0-3
    //  * Gracza S (Odpowiadający 1S): HCP: 10-12 | Spades: 4-13 | Hearts: 0-3
    //  -----------------------------------------------------------------------
    public static PositionCalls diamond_1S_1NT_2C_2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, highCardPoints(TO_GAME), BALANCED, ruleShow(1), id("RespondDiamondBid3.twoDiamond _3NT")),
                shows(Bid._3D, shape(Suit.Hearts, 5, 5), shape(Suit.Diamonds, 4, 13), highCardPoints(MEDUIM), ruleShow(1), id("RespondDiamondBid3.twoDiamond _3D")),
                shows(Bid._3C, shape(Suit.Hearts, 4, 4), shape(Suit.Clubs, 5, 13), highCardPoints(MEDUIM), ruleShow(1), id("RespondDiamondBid3.twoDiamond _3C")),
                shows(Bid._2NT, shape(Suit.Hearts, 5, 5), BALANCED, highCardPoints(HIGHT), ruleShow(1), id("RespondDiamondBid3.twoDiamond _3C")),
                shows(Bid._2H, shape(4, 4), highCardPoints(LOW), ruleShow(1), id("RespondDiamondBid3.twoDiamond _2H")),
                shows(Bid._2S, shape(5, 13), highCardPoints(MEDUIM), ruleShow(1), id("RespondDiamondBid3.twoDiamond _2S")),
                shows(Call.PASS, id("RespondDiamondBid3.twoDiamond PASS"))

        );
        return choices;
    }
}
