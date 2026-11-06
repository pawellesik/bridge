package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

public class RespondDiamondBid3 extends WJSimple {

    public static final Range TO_GAME = new Range(13, 16);
    public static final Range LOW = new Range(10, 11);
    public static final Range MEDUIM = new Range(10, 12);
    public static final Range HIGHT = new Range(11, 12);



    //  1D      -->  1H
    //  1NT, 1S -->  2C
    //  2D      -->
    public static PositionCalls oneHeart_oneSpade1NT_twoClub_twoDiamond(PositionState ps) {
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

    //  1D      -->  1S
    // 1NT      -->  2C
    //  2D      -->
    public static PositionCalls oneSpade_1NT_twoClub_twoDiamond(PositionState ps) {
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
