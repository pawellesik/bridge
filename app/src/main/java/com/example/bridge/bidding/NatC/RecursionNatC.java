package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Constraints.AgreedStrain;
import com.example.bridge.bidding.Constraints.Shape;
import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RecursionNatC extends NatC {

    public static PositionCalls recursionBids(PositionState ps) {
        return recursionBids(ps, 1);
    }

    public static PositionCalls recursionBids(PositionState ps, int level) {
        PositionCalls choices = new PositionCalls(ps);

        if (level < 10) {
            System.out.println("plesik "+ ps.getBiddingState().getContract().getBid().getSuit()+" trump "+ ps.getPairState().getTrumpSuit());
            System.out.println("czy "+ (ps.getBiddingState().getContract().getBid().getSuit() == ps.getPairState().getTrumpSuit()));
            choices.addRules(
                    shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, id("RecursionNatC.recursionBids CONTRACT_IS_AGREED_STRAIN _Pass")),

                    shows(Bid._2H, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._2S, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _1S")),
                    shows(Bid._2C, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _2C")),
                    shows(Bid._2D, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _2D")),
                    shows(Bid._3C, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._3D, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _1S")),
                    shows(Bid._3H, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _2D")),
                    shows(Bid._3S, shape(6, 10), IS_REBID, id("RecursionNatC.recursionBids IS_REBID _2D")),

                    shows(Bid._2H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._2S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionBids IS_REBID _1S")),
                    shows(Bid._2C, shape(5, 10), id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._2D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionBids IS_REBID _1S")),
                    shows(Bid._3H, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._3S, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionBids IS_REBID _1S")),
                    shows(Bid._3C, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._3D, shape(5, 10), IS_NEW_SUIT, id("RecursionNatC.recursionBids IS_REBID _1S")),

                    shows(Bid._2H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._2S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionBids IS_REBID _1S")),
                    shows(Bid._3H, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionBids IS_REBID _1H")),
                    shows(Bid._3S, shape(4, 10), IS_NEW_SUIT, DECENT_PLUS_SUIT, id("RecursionNatC.recursionBids IS_REBID _1S")),

                    shows(Bid._1NT, PAIR_BALANCED, id("RecursionNatC.recursionBids IS_REBID _2D")),
                    shows(Bid._2NT, PAIR_BALANCED, id("RecursionNatC.recursionBids IS_REBID _2D")),
                    shows(Bid._3NT, PAIR_BALANCED, id("RecursionNatC.recursionBids IS_REBID _2D"))
            );
            choices.addRules(RecursionNatC.recursionBids(ps, level + 1));
        } else {
            choices.addRules(shows(Call.PASS, id("RecursionNatC.recursionBids Pass")));
        }
        return choices;
    }


}






