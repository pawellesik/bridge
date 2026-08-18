package com.example.bridge.bidding.NatC;

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

public class CompeteNatC extends NatC {


    public static Iterable<CallFeature> compBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        addAcesAskConventions(ps, bids);

        bids.add(shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME _4H")));
        bids.add(shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME _4S")));

        bids.add(shows(Bid._4H, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME_INVITE _4H")));
        bids.add(shows(Bid._4S, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME_INVITE _4S")));

        bids.add(shows(Bid._2C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids _2C")));
        bids.add(shows(Bid._2D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids _2D")));
        bids.add(shows(Bid._2H, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids _2H")));
        bids.add(shows(Bid._2S, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids _2S")));

        bids.add(shows(Bid._3C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), not(currentContract(Bid._2C)), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids _3C")));
        bids.add(shows(Bid._3D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), not(currentContract(Bid._2D)), setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids _3D")));

        bids.add(shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("CompeteNatC.compBids PAIR_BALANCED _3NT")));

        bids.add(shows(Bid._5C, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), PARTNER_DID_NOT_SIGN_OFF, fit(Suit.Spades, false), fit(Suit.Hearts, false), id("CompeteNatC.compBids _5C")));
        bids.add(shows(Bid._5D, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), PARTNER_DID_NOT_SIGN_OFF, fit(Suit.Spades, false), fit(Suit.Hearts, false), id("CompeteNatC.compBids _5D")));
        bids.add(shows(Bid._5C, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_GAME), PARTNER_DID_NOT_SIGN_OFF, id("CompeteNatC.compBids _5C")));
        bids.add(shows(Bid._5D, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(PAIR_GAME), PARTNER_DID_NOT_SIGN_OFF, id("CompeteNatC.compBids _5D")));

        bids.add(shows(Bid._2S, shape(2, 10), betterThan(Suit.Hearts), partner(isLastBid(Bid._2H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), id("CompeteNatC.compBids _2S")));
        bids.add(shows(Bid._4S, shape(2, 10), betterThan(Suit.Hearts), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), id("CompeteNatC.compBids _4S")));

        bids.add(shows(Bid._3D, shape(2, 10), betterThan(Suit.Clubs), partner(isLastBid(Bid._3C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), id("CompeteNatC.compBids _3D")));
        bids.add(shows(Bid._5D, shape(2, 10), betterThan(Suit.Diamonds), partner(isLastBid(Bid._5C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), id("CompeteNatC.compBids _5D")));

        bids.add(shows(Call.PASS, id("CompeteNatC.compBids _PASS")));

        bids.add(partnerBids(CompeteNatC::compBids));

        return bids;
    }

    private static void addAcesAskConventions(PositionState ps, List<CallFeature> bids) {
        Bid partnerBid = ps.getPartner().getBid();
        Suit partnerSuit = (partnerBid != null) ? partnerBid.getSuit() : null;
        if (partnerSuit != null) {
            for (CallFeature cf : AcesAsk.initiateConvention(ps)) {
                bids.add(cf);
            }
            for (CallFeature cf : AcesAsk.initiateConventionBlok(ps)) {
                bids.add(cf);
            }
        }
    }
}






