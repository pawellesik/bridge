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

        bids.add(shows(Bid._4H, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids FIT_8_PLUS pairHighCardPoints PAIR_GAME _4H")));
        bids.add(shows(Bid._4S, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, setTrumpColor(Suit.Spades), id("CompeteNatC.compBids FIT_8_PLUS pairHighCardPoints PAIR_GAME _4S")));

        bids.add(shows(Bid._4H, pairHighCardPoints(PAIR_GAME_INVITE), FIT_8_PLUS, IS_FORCED_TO_GAME, setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME_INVITE _4H")));
        bids.add(shows(Bid._4S, pairHighCardPoints(PAIR_GAME_INVITE), FIT_8_PLUS, IS_FORCED_TO_GAME, setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME_INVITE _4S")));

        bids.add(shows(Bid._2C, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids _2C")));
        bids.add(shows(Bid._2D, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids _2D")));
        bids.add(shows(Bid._2H, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids _2H")));
        bids.add(shows(Bid._2S, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Spades), id("CompeteNatC.compBids _2S")));

        bids.add(shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, noFit(), not(currentContract(Bid._2C)), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids FIT_8_PLUS _3C")));
        bids.add(shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, noFit(), not(currentContract(Bid._2D)), setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids FIT_8_PLUS _3D")));

        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("CompeteNatC.compBids PAIR_BALANCED PAIR_GAME _3NT")));

        bids.add(shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids PAIR_MINOR_GAME, _5C")));
        bids.add(shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids PAIR_MINOR_GAME _5D")));
        bids.add(shows(Bid._5C, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, IS_FORCED_TO_GAME, setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids IS_FORCED_TO_GAME _5C")));
        bids.add(shows(Bid._5D, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, IS_FORCED_TO_GAME, setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids IS_FORCED_TO_GAME _5D")));
        bids.add(shows(Bid._5C, pairPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids pairPoints _5C")));
        bids.add(shows(Bid._5D, pairPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids pairPoints _5D")));

        bids.add(shows(Bid._2S, shape(2, 10), partner(isLastBid(Bid._2H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), betterThan(Suit.Hearts), id("CompeteNatC.compBids _2S")));
        bids.add(shows(Bid._3S, shape(3, 4), shape(Suit.Hearts, 0, 3), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Hearts), id("CompeteNatC.compBids _3S")));
        bids.add(shows(Bid._3S, shape(2, 3), shape(Suit.Hearts, 0, 2), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Hearts), id("CompeteNatC.compBids _3S")));
        bids.add(shows(Bid._4S, shape(2, 3), shape(Suit.Hearts, 0, 2), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), betterThan(Suit.Hearts), id("CompeteNatC.compBids _4S")));
        bids.add(shows(Bid._4S, shape(3, 4), shape(Suit.Hearts, 0, 3), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Hearts), id("CompeteNatC.compBids _4S")));


        bids.add(shows(Bid._4S, partner(isLastBid(Bid._4D)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Diamonds), id("CompeteNatC.compBids betterThan(Suit.Diamonds) _4S")));
        bids.add(shows(Bid._4S, partner(isLastBid(Bid._4C)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Clubs), id("CompeteNatC.compBids betterThan(Suit.Clubs) _4S")));
        bids.add(shows(Bid._4H, partner(isLastBid(Bid._4D)), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Diamonds), id("CompeteNatC.compBids betterThan(Suit.Diamonds) _4H")));
        bids.add(shows(Bid._4H, partner(isLastBid(Bid._4C)), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Clubs), id("CompeteNatC.compBids  betterThan(Suit.Clubs) _4H")));

        bids.add(shows(Bid._5D, partner(isLastBid(Bid._4D)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Spades), id("CompeteNatC.compBids _5D")));
        bids.add(shows(Bid._5D, partner(isLastBid(Bid._4D)), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Hearts), id("CompeteNatC.compBids _5D")));
        bids.add(shows(Bid._5C, partner(isLastBid(Bid._4C)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Spades), id("CompeteNatC.compBids _5C")));
        bids.add(shows(Bid._5C, partner(isLastBid(Bid._4C)), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Hearts), id("CompeteNatC.compBids _5C")));


        bids.add(shows(Bid._3D, shape(2, 10), partner(isLastBid(Bid._3C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), betterThan(Suit.Clubs), id("CompeteNatC.compBids _3D")));
        bids.add(shows(Bid._5D, shape(2, 10), partner(isLastBid(Bid._5C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), betterThan(Suit.Diamonds), id("CompeteNatC.compBids _5D")));

        bids.add(shows(Bid._3H, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteNatC.compBids PAIR_GAME _3H")));
        bids.add(shows(Bid._3S, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteNatC.compBids PAIR_GAME _3S")));
        bids.add(shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteNatC.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), shape(6, 10), noFit(), IS_REBID, id("CompeteNatC.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteNatC.compBids PAIR_GAME _3D")));

        bids.add(shows(Bid._3NT, OpeningStrongBidding, noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), IS_NON_JUMP, othersAtLeast(3), id("CompeteNatC.compBids OpeningStrongBidding exit _3NT")));
        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), IS_NON_JUMP, othersAtLeast(3), id("CompeteNatC.compBids PAIR_GAME exit _3NT")));
        bids.add(shows(Bid._2NT, noFit(7), partner(isLastBid(Bid._2C, Bid._2D, Bid._2H, Bid._2S)), IS_NON_JUMP, othersAtLeast(3), id("CompeteNatC.compBids exit _2NT")));
        bids.add(shows(Bid._5NT, pairHighCardPoints(PAIR_MINOR_GAME), noFit(7), partner(isLastBid(Bid._5C, Bid._5D)), IS_NON_JUMP, othersAtLeast(3), id("CompeteNatC.compBids PAIR_MINOR_GAME exit _5NT")));

        bids.add(shows(Bid._3NT, OpeningStrongBidding, noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteNatC.compBids OpeningStrongBidding exit _3NT")));
        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteNatC.compBids PAIR_GAME exit _3NT")));
        bids.add(shows(Bid._2NT, noFit(7), partner(isLastBid(Bid._2C, Bid._2D, Bid._2H, Bid._2S)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteNatC.compBids exit _2NT")));
        bids.add(shows(Bid._5NT, pairHighCardPoints(PAIR_MINOR_GAME), noFit(7), partner(isLastBid(Bid._5C, Bid._5D)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteNatC.compBids PAIR_MINOR_GAME exit _5NT")));

        bids.add(shows(Bid._3C, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Clubs, 5), id("CompeteNatC.compBids _3C")));
        bids.add(shows(Bid._3D, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Diamonds, 5), id("CompeteNatC.compBids _3D")));
        bids.add(shows(Bid._3H, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Hearts, 5), id("CompeteNatC.compBids _3H")));
        bids.add(shows(Bid._3S, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Spades, 5), id("CompeteNatC.compBids _3S")));

        bids.add(shows(Bid._3H, shape(4, 10), partner(isLastBid(Bid._2NT)), NOT_BALANCED, IS_NEW_SUIT, secondSuit(Suit.Hearts, 4), id("CompeteNatC.compBids _3H")));
        bids.add(shows(Bid._3S, shape(4, 10), partner(isLastBid(Bid._2NT)), NOT_BALANCED, IS_NEW_SUIT, secondSuit(Suit.Spades, 4), id("CompeteNatC.compBids _3S")));

        bids.add(shows(Bid._4H, pairHighCardPoints(PAIR_GAME), fit(7), partner(isLastBid(Bid._3H)), NOT_BALANCED, setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids NOT_BALANCED _4H")));
        bids.add(shows(Bid._4S, pairHighCardPoints(PAIR_GAME), fit(7), partner(isLastBid(Bid._3S)), NOT_BALANCED, setTrumpColor(Suit.Spades), id("CompeteNatC.compBids NOT_BALANCED _4S")));

        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, IS_NON_JUMP, id("RecursionNatC.recursionFindFitGame BALANCED, IS_NON_JUMP _3NT")));

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

    public static void addAcesAskConventions(PositionState ps, List<CallFeature> bids) {
        Suit agreedTrump = ps.getPairState().getTrumpSuit();
        Bid lastBid = ps.getPartner().getBid();

        boolean pairHadJump = false;
        for (int i = 0; i < ps.getPartner().getCallCount(); i++) {
            if (ps.getPartner().getCallDetails(i).getJumpLevel() > 0) {
                pairHadJump = true;
                break;
            }
        }
        if (!pairHadJump) {
            for (int i = 0; i < ps.getCallCount(); i++) {
                if (ps.getCallDetails(i).getJumpLevel() > 0) {
                    pairHadJump = true;
                    break;
                }
            }
        }

        boolean jumpMatch = pairHadJump;
        boolean agreedSuitMatch = (agreedTrump != null && lastBid != null && lastBid.getSuit() == agreedTrump && ps.getBiddingState().getContract().isOurs(ps.getDirection()));
        boolean isLevel1 = (lastBid != null && lastBid.getLevel() == 1);
        boolean isLevel2 = (lastBid != null && lastBid.getLevel() == 2);

        if (jumpMatch || agreedSuitMatch || isLevel1 || isLevel2) {
            if (agreedTrump != null) {
                int countBefore = bids.size();
                for (CallFeature cf : AcesAsk.initiateConvention(ps)) {
                    bids.add(cf);
                }
                if (bids.size() == countBefore) {
                    for (CallFeature cf : AcesAsk.initiateConventionBlok(ps)) {
                        bids.add(cf);
                    }
                }
            } else {
                if (isLevel1 || isLevel2) {
                    for (CallFeature cf : AcesAsk.initiateConvention(ps)) {
                        bids.add(cf);
                    }
                } else {
                    for (CallFeature cf : AcesAsk.initiateConventionBlok(ps)) {
                        bids.add(cf);
                    }
                }
            }
        }
    }

    private static void addCompBids(PositionState ps, List<CallFeature> bids) {
        for (CallFeature cf : CompeteNatC.bids(ps)) {
            bids.add(cf);
        }
    }


}






