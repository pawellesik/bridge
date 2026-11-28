package com.example.bridge.bidding.WJ2025.NoTrump;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.Spade.OpenSpadeBid2;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondNoTrump1 extends WJ2025 {

    public static PositionCalls oneNT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);


        choices.addRules(
                shows(Bid._4H, shape(Suit.Spades, 4, 13), shape(Suit.Hearts, 0, 1), setTrumpColor(Suit.Spades), ruleShow(1), note("(splinter)"), id("RespondSpadeBid1.oneSpade _4H")),
                properties(new Call[]{Bid._3NT}, OpenSpadeBid2::spade_3NT, false),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        return choices;
    }
}
