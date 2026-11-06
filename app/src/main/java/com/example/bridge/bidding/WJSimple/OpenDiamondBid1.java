package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.NatC.RecursionNatC;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

public class OpenDiamondBid1 extends WJSimple {

    public static final Range OPEN_STANDARD = new Range(12, 17);
    public static final Range OPEN_WEAK = new Range(12, 14);
    public static final Range OPEN_STRONG = new Range(15, 17);
    public static final Range OPEN_NT = new Range(14, 17);


    public static PositionCalls oneHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, shape(Suit.Diamonds, 7, 13), EXCELLENT_PLUS_SUIT, highCardPoints(OPEN_NT), ruleShow(1), id("OpenDiamondBid1.oneHeart _3NT")),


        );
        return choices;
    }


}
