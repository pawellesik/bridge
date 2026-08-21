package com.example.bridge.bidding.NatC;

import static com.example.bridge.bidding.NatC.OpenNatC.OpeningInviteBidding;
import static com.example.bridge.bidding.NatC.OpenNatC.OpeningStrongBidding;

import com.example.bridge.bidding.Constraints.JumpBid;
import com.example.bridge.bidding.Constraints.Shape;
import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Strain;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class CompeteNatC extends NatC {


    public static Iterable<CallFeature> compBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        Bid partnerBid = ps.getPartner().getBid();

        addAcesAskConventions(ps, bids);

        bids.add(partnerBids(CompeteNatC::compBids));

        bids.add(shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME _4H")));
        bids.add(shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME _4S")));

        bids.add(shows(Bid._4H, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME_INVITE _4H")));
        bids.add(shows(Bid._4S, IS_FORCED_TO_GAME, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME_INVITE), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME_INVITE _4S")));

        bids.add(shows(Bid._2C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids _2C")));
        bids.add(shows(Bid._2D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids _2D")));
        bids.add(shows(Bid._2H, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids _2H")));
        bids.add(shows(Bid._2S, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids _2S")));

        bids.add(shows(Bid._3C, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), not(currentContract(Bid._2C)), setTrumpColor(Suit.Clubs), noFit(), id("CompeteNatC.compBids FIT_8_PLUS _3C")));
        bids.add(shows(Bid._3D, FIT_8_PLUS, pairHighCardPoints(PAIR_LOW_GAME), not(currentContract(Bid._2D)), setTrumpColor(Suit.Diamonds), noFit(), id("CompeteNatC.compBids FIT_8_PLUS _3D")));

        bids.add(shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("CompeteNatC.compBids PAIR_BALANCED PAIR_GAME _3NT")));

        bids.add(shows(Bid._5C, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), PARTNER_DID_NOT_SIGN_OFF, id("CompeteNatC.compBids PAIR_MINOR_GAME, _5C")));
        bids.add(shows(Bid._5D, FIT_8_PLUS, pairHighCardPoints(PAIR_MINOR_GAME), PARTNER_DID_NOT_SIGN_OFF, id("CompeteNatC.compBids PAIR_MINOR_GAME _5D")));
        bids.add(shows(Bid._5C, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), IS_FORCED_TO_GAME, PARTNER_DID_NOT_SIGN_OFF, id("CompeteNatC.compBids IS_FORCED_TO_GAME _5C")));
        bids.add(shows(Bid._5D, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), IS_FORCED_TO_GAME, PARTNER_DID_NOT_SIGN_OFF, id("CompeteNatC.compBids IS_FORCED_TO_GAME _5D")));

        bids.add(shows(Bid._2S, shape(2, 10), betterThan(Suit.Hearts), partner(isLastBid(Bid._2H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), id("CompeteNatC.compBids _2S")));
        bids.add(shows(Bid._3S, shape(3, 4), shape(Suit.Hearts, 3), betterThan(Suit.Hearts), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), id("CompeteNatC.compBids _3S")));
        bids.add(shows(Bid._3S, shape(2, 3), shape(Suit.Hearts, 2), betterThan(Suit.Hearts), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), id("CompeteNatC.compBids _3S")));
        bids.add(shows(Bid._4S, shape(2, 3), shape(Suit.Hearts, 2), betterThan(Suit.Hearts), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), id("CompeteNatC.compBids _4S")));
        bids.add(shows(Bid._4S, shape(3, 4), shape(Suit.Hearts, 3), betterThan(Suit.Hearts), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), id("CompeteNatC.compBids _4S")));

        bids.add(shows(Bid._3D, shape(2, 10), betterThan(Suit.Clubs), partner(isLastBid(Bid._3C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), id("CompeteNatC.compBids _3D")));
        bids.add(shows(Bid._5D, shape(2, 10), betterThan(Suit.Diamonds), partner(isLastBid(Bid._5C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), id("CompeteNatC.compBids _5D")));

        bids.add(shows(Bid._3H, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Hearts), id("CompeteNatC.compBids PAIR_GAME _3H")));
        bids.add(shows(Bid._3S, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Spades), id("CompeteNatC.compBids PAIR_GAME _3S")));
        bids.add(shows(Bid._3C, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3C, shape(6, 10), noFit(), IS_REBID, pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Clubs), id("CompeteNatC.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3D, shape(7, 10), noFit(), pairHighCardPoints(PAIR_LOW_GAME), setTrumpColor(Suit.Diamonds), id("CompeteNatC.compBids PAIR_GAME _3D")));

        if (partnerBid != null) {
            bids.add(shows(Bid._3NT, noFit(7), IS_NON_JUMP, OpeningStrongBidding, partner(isLastBid(Bid._3H, Bid._3S)), othersAtLeast(2), id("CompeteNatC.compBids OpeningStrongBidding exit _3NT")));
            bids.add(shows(Bid._3NT, noFit(7), IS_NON_JUMP, pairHighCardPoints(PAIR_GAME), partner(isLastBid(Bid._3H, Bid._3S)), othersAtLeast(2), id("CompeteNatC.compBids PAIR_GAME exit _3NT")));
            bids.add(shows(Bid._2NT, noFit(7), IS_NON_JUMP, partner(isLastBid(Bid._2C, Bid._2D, Bid._2H, Bid._2S)), othersAtLeast(2), id("CompeteNatC.compBids exit _2NT")));
            bids.add(shows(Bid._4NT, noFit(7), IS_NON_JUMP, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._4H, Bid._4S)), othersAtLeast(2), id("CompeteNatC.compBids PAIR_MINOR_GAME exit _4NT")));
            bids.add(shows(Bid._5NT, noFit(7), IS_NON_JUMP, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._5C, Bid._5D)), othersAtLeast(2), id("CompeteNatC.compBids PAIR_MINOR_GAME exit _5NT")));
        }

        bids.add(shows(Bid._3C, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Clubs, 5), id("CompeteNatC.compBids _3C")));
        bids.add(shows(Bid._3D, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Diamonds, 5), id("CompeteNatC.compBids _3D")));
        bids.add(shows(Bid._3H, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Hearts, 5), id("CompeteNatC.compBids _3H")));
        bids.add(shows(Bid._3S, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Spades, 5), id("CompeteNatC.compBids _3S")));

        bids.add(shows(Bid._3H, IS_NEW_SUIT, shape(4, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Hearts, 4), hasShortness(0, 1), id("CompeteNatC.compBids _3H")));
        bids.add(shows(Bid._3S, IS_NEW_SUIT, shape(4, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Spades, 4), hasShortness(0, 1), id("CompeteNatC.compBids _3S")));

        bids.add(shows(Bid._4H, fit(7), pairHighCardPoints(PAIR_GAME), NOT_BALANCED, partner(isLastBid(Bid._3H)), id("RecursionNatC.recursionFindFitGame NOT_BALANCED _4H")));
        bids.add(shows(Bid._4S, fit(7), pairHighCardPoints(PAIR_GAME), NOT_BALANCED, partner(isLastBid(Bid._3S)), id("RecursionNatC.recursionFindFitGame NOT_BALANCED _4S")));

        bids.add(shows(Call.PASS, id("CompeteNatC.compBids _PASS")));

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






