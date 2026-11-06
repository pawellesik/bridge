package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.Tools.SuitQuality;


public class OpenDiamondBid2 extends WJSimple {

    //1D -->
    //    1H -->
    //          1NT, 1S -->
    //                  2C -->
    public static PositionCalls twoClub(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._2D, ruleShow(1), id("OpenDiamondBid2.twoClub _2D")),
                properties(new Call[]{Bid._2D}, RespondDiamondBid3::twoDiamond, true)
        );
        return choices;
    }


    //1D -->
    //    1H -->
    //          1NT, 1S -->
    //                  2D -->
    public static PositionCalls twoDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(


        );
        return choices;
    }



}
