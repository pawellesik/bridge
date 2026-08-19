package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Constraints.Shape;
import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class FinishPassNatC extends NatC {


    public static Iterable<CallFeature> finishBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._2S, shape(2, 10), betterThan(Suit.Hearts), partner(isLastBid(Bid._2H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), id("CompeteNatC.compBids _2S")));

        bids.add(shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_LOW_GAME), partner(isLastBid(Bid._2S)), id("FinishPassNatC.finishBids pass")));
        bids.add(shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_LOW_GAME),  longerOrEqual(Suit.Spades), partner(isLastBid(Bid._2H)), not(partner(new Shape.HasMinShape(Suit.Spades, 5))), id("FinishPassNatC.finishBids pass")));

        bids.add(shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_LOW_GAME), partner(isLastBid(Bid._3C, Bid._3D)), id("FinishPassNatC.finishBids pass")));
        bids.add(shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_GAME), partner(isLastBid(Bid._4H, Bid._4S)), id("FinishPassNatC.finishBids pass")));
        bids.add(shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._5C, Bid._5D)), id("FinishPassNatC.finishBids pass")));

        bids.add(shows(Call.PASS, pairHighCardPoints(PAIR_GAME), partner(isLastBid(Bid._3NT)), id("FinishPassNatC.finishBids pass")));


        return bids;
    }
}






