package com.example.bridge.bidding.NatC;

import static com.example.bridge.bidding.NatC.OpenBid1NatC.OpeningStrongBidding;

import com.example.bridge.bidding.Constraints.Shape;
import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class CompeteNatC extends NatC {
    /* Pytania o asy dodadza sie tylko w sytuacji gdy poprzednia odzywka bedzie z przeskokiem
     * lub bedzie uzgodniony kolor i beda to odzywki 4C i 4NT, wtedy system bedzie odpowiadać
     * czyli wzsedzie gdzie dodajemy CompeteNatC.natC nalezy wdzesniej wczytac pytania o asy !!!*/

    public static Iterable<CallFeature> bids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        bids.add(partnerBids(RecursionNatC::recursionFindFitGame));

        bids.add(shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME _4H")));
        bids.add(shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME _4S")));

        bids.add(shows(Bid._4H, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME_INVITE _4H")));
        bids.add(shows(Bid._4S, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME_INVITE _4S")));

        bids.add(shows(Bid._2C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids _2C")));
        bids.add(shows(Bid._2D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids _2D")));
        bids.add(shows(Bid._2H, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids _2H")));
        bids.add(shows(Bid._2S, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids _2S")));

        bids.add(shows(Bid._3C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), not(currentContract(Bid._2C)), noFit(), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids FIT_8_PLUS _3C")));
        bids.add(shows(Bid._3D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), not(currentContract(Bid._2D)), noFit(), setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids FIT_8_PLUS _3D")));

        bids.add(shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("CompeteNatC.compBids PAIR_BALANCED PAIR_GAME _3NT")));

        bids.add(shows(Bid._5C, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids PAIR_MINOR_GAME, _5C")));
        bids.add(shows(Bid._5D, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids PAIR_MINOR_GAME _5D")));
        bids.add(shows(Bid._5C, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), IS_FORCED_TO_GAME, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids IS_FORCED_TO_GAME _5C")));
        bids.add(shows(Bid._5D, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), IS_FORCED_TO_GAME, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids IS_FORCED_TO_GAME _5D")));

        bids.add(shows(Bid._2S, shape(2, 10), betterThan(Suit.Hearts), partner(isLastBid(Bid._2H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), id("CompeteNatC.compBids _2S")));
        bids.add(shows(Bid._3S, shape(3, 4), shape(Suit.Hearts, 0, 3), betterThan(Suit.Hearts), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), id("CompeteNatC.compBids _3S")));
        bids.add(shows(Bid._3S, shape(2, 3), shape(Suit.Hearts, 0, 2), betterThan(Suit.Hearts), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), id("CompeteNatC.compBids _3S")));
        bids.add(shows(Bid._4S, shape(2, 3), shape(Suit.Hearts, 0, 2), betterThan(Suit.Hearts), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), id("CompeteNatC.compBids _4S")));
        bids.add(shows(Bid._4S, shape(3, 4), shape(Suit.Hearts, 0, 3), betterThan(Suit.Hearts), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), id("CompeteNatC.compBids _4S")));

        bids.add(shows(Bid._3D, shape(2, 10), betterThan(Suit.Clubs), partner(isLastBid(Bid._3C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), id("CompeteNatC.compBids _3D")));
        bids.add(shows(Bid._5D, shape(2, 10), betterThan(Suit.Diamonds), partner(isLastBid(Bid._5C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), id("CompeteNatC.compBids _5D")));

        bids.add(shows(Bid._3H, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), id("CompeteNatC.compBids PAIR_GAME _3H")));
        bids.add(shows(Bid._3S, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), id("CompeteNatC.compBids PAIR_GAME _3S")));
        bids.add(shows(Bid._3C, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), id("CompeteNatC.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3C, shape(6, 10), noFit(), IS_REBID, pairHighCardPoints(PAIR_LOW_GAME), id("CompeteNatC.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3D, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), id("CompeteNatC.compBids PAIR_GAME _3D")));

        bids.add(shows(Bid._3NT, noFit(7), IS_NON_JUMP, OpeningStrongBidding, partner(isLastBid(Bid._3H, Bid._3S)), othersAtLeast(3), id("CompeteNatC.compBids OpeningStrongBidding exit _3NT")));
        bids.add(shows(Bid._3NT, noFit(7), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), partner(isLastBid(Bid._3H, Bid._3S)), othersAtLeast(3), id("CompeteNatC.compBids PAIR_GAME exit _3NT")));
        bids.add(shows(Bid._2NT, noFit(7), IS_NON_JUMP, partner(isLastBid(Bid._2C, Bid._2D, Bid._2H, Bid._2S)), othersAtLeast(3), id("CompeteNatC.compBids exit _2NT")));
        bids.add(shows(Bid._5NT, noFit(7), IS_NON_JUMP, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._5C, Bid._5D)), othersAtLeast(3), id("CompeteNatC.compBids PAIR_MINOR_GAME exit _5NT")));

        bids.add(shows(Bid._3NT, noFit(7), IS_NON_JUMP, OpeningStrongBidding, partner(isLastBid(Bid._3H, Bid._3S)), PAIR_BALANCED, id("CompeteNatC.compBids OpeningStrongBidding exit _3NT")));
        bids.add(shows(Bid._3NT, noFit(7), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), partner(isLastBid(Bid._3H, Bid._3S)), PAIR_BALANCED, id("CompeteNatC.compBids PAIR_GAME exit _3NT")));
        bids.add(shows(Bid._2NT, noFit(7), IS_NON_JUMP, partner(isLastBid(Bid._2C, Bid._2D, Bid._2H, Bid._2S)), PAIR_BALANCED, id("CompeteNatC.compBids exit _2NT")));
        bids.add(shows(Bid._5NT, noFit(7), IS_NON_JUMP, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._5C, Bid._5D)), PAIR_BALANCED, id("CompeteNatC.compBids PAIR_MINOR_GAME exit _5NT")));

        bids.add(shows(Bid._3C, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Clubs, 5), id("CompeteNatC.compBids _3C")));
        bids.add(shows(Bid._3D, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Diamonds, 5), id("CompeteNatC.compBids _3D")));
        bids.add(shows(Bid._3H, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Hearts, 5), id("CompeteNatC.compBids _3H")));
        bids.add(shows(Bid._3S, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Spades, 5), id("CompeteNatC.compBids _3S")));

        bids.add(shows(Bid._3H, IS_NEW_SUIT, shape(4, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Hearts, 4), NOT_BALANCED, id("CompeteNatC.compBids _3H")));
        bids.add(shows(Bid._3S, IS_NEW_SUIT, shape(4, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Spades, 4), NOT_BALANCED, id("CompeteNatC.compBids _3S")));

        bids.add(shows(Bid._4H, fit(7), pairHighCardPoints(PAIR_GAME), NOT_BALANCED, partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids NOT_BALANCED _4H")));
        bids.add(shows(Bid._4S, fit(7), pairHighCardPoints(PAIR_GAME), NOT_BALANCED, partner(isLastBid(Bid._3S)), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids NOT_BALANCED _4S")));

        bids.add(shows(Bid._3NT, BALANCED, IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), id("RecursionNatC.recursionFindFitGame BALANCED, IS_NON_JUMP _3NT")));

        bids.add(shows(Call.PASS, id("CompeteNatC.compBids _PASS")));

        return bids;
    }

    public static Iterable<CallFeature> compBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        bids.add(partnerBids(RecursionNatC::recursionFindFitGame));
        addAcesAskConventions(ps, bids);
        addCompBids(ps, bids);

        return bids;
    }

    private static void addAcesAskConventions(PositionState ps, List<CallFeature> bids) {
        Suit agreedTrump = ps.getPairState().getTrumpSuit();
        CallDetails lastCallDetails = ps.getPartner().getLastCallDetails();
        int jumpLevel = (lastCallDetails != null) ? lastCallDetails.getJumpLevel() : 0;
        Bid lastBid = ps.getPartner().getBid();

        boolean jumpMatch = jumpLevel > 0;
        boolean agreedSuitMatch = (agreedTrump != null && lastBid != null && lastBid.getSuit() == agreedTrump && ps.getBiddingState().getContract().isOurs(ps.getDirection()));

        if (jumpMatch || agreedSuitMatch) {
            for (CallFeature cf : AcesAsk.initiateConvention(ps)) {
                bids.add(cf);
            }
            for (CallFeature cf : AcesAsk.initiateConventionBlok(ps)) {
                bids.add(cf);
            }
        }
    }

    private static void addCompBids(PositionState ps, List<CallFeature> bids) {
        for (CallFeature cf : CompeteNatC.bids(ps)) {
            bids.add(cf);
        }
    }


}






