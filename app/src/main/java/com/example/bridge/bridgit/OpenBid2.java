package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class OpenBid2 extends Open {

    public static PositionCalls responderChangedSuits(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Arrays.asList(
            partnerBids(RespondBid2::secondBid),

            // Major support
            shows(Call.Bid._2H, fit(), Minimum),
            shows(Call.Bid._2S, fit(), Minimum),
            shows(Call.Bid._3H, isJump(1), fit(), MediumOrBetter),
            shows(Call.Bid._3S, isJump(1), fit(), MediumOrBetter),
            shows(Call.Bid._4H, isJump(2), fit(), points(19, 21)),
            shows(Call.Bid._4S, isJump(2), fit(), points(19, 21)),

            // Rebid 6-card suit
            shows(Call.Bid._2C, isLastBid(Call.Bid._1C), shape(Suit.Clubs, 6, 13), Minimum),
            shows(Call.Bid._2D, isLastBid(Call.Bid._1D), shape(Suit.Diamonds, 6, 13), Minimum),
            shows(Call.Bid._2H, isLastBid(Call.Bid._1H), shape(Suit.Hearts, 6, 13), Minimum),
            shows(Call.Bid._2S, isLastBid(Call.Bid._1S), shape(Suit.Spades, 6, 13), Minimum),

            // 1NT rebid
            shows(Call.Bid._1NT, balanced, points(12, 15)),
            
            shows(Call.Pass)
        ));
        return choices;
    }
}
