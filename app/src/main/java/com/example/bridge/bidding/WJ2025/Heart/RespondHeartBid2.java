package com.example.bridge.bidding.WJ2025.Heart;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class RespondHeartBid2 extends WJ2025 {

    public static final Range RESPOND_WEAK = new Range(7, 9);
    public static final Range RESPOND_NORMAL = new Range(10, 12);
    public static final Range RESPOND_TO_GAME = new Range(12, 28);
    public static final Range RESPOND_STRONG = new Range(15, 28);
    public static PositionCalls heart_1S_1NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3S, shape(6, 13), highCardPoints(RESPOND_NORMAL), id("RespondHeartBid2.heart_1S_1NT _3S")),
                shows(Bid._3H, shape(3, 13), highCardPoints(RESPOND_NORMAL), id("RespondHeartBid2.heart_1S_1NT _3H")),//to chyba niemozliwe

                shows(Bid._3D, shape(5, 13), shape(Suit.Spades, 5, 13), highCardPoints(RESPOND_TO_GAME), id("RespondHeartBid2.heart_1S_1NT _3D")),

                shows(Bid._3C, shape(6, 13), shape(Suit.Spades, 4, 13), highCardPoints(RESPOND_WEAK), id("RespondHeartBid2.heart_1S_1NT _3D")),

                shows(Bid._2S, shape(5, 13), shape(Suit.Spades, 4, 13), highCardPoints(RESPOND_WEAK), id("RespondHeartBid2.heart_1S_1NT _2S")),
                shows(Bid._2H, shape(3, 13), shape(Suit.Spades, 5, 13), highCardPoints(RESPOND_WEAK), id("RespondHeartBid2.heart_1S_1NT _2H")),

                shows(Bid._2D, highCardPoints(RESPOND_STRONG), id("RespondHeartBid2.heart_1S_1NT _2D")), //?
                shows(Bid._2C, highCardPoints(RESPOND_STRONG), id("RespondHeartBid2.heart_1S_1NT _2C")) //?

                //properties(new Call[]{Bid._3NT}, OpenHeartBid2::heart_3NT, false),

        );
        return choices;
    }

    public static PositionCalls heart_1S_2C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                //shows(Bid._1S, shape(4, 13), highCardPoints(RESPOND_TO_SPADE), id("RespondHeartBid2.oneHeart _1S")),

                //properties(new Call[]{Bid._3NT}, OpenHeartBid2::heart_3NT, false),


        );
        return choices;
    }

}
