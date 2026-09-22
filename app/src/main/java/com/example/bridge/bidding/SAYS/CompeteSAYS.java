package com.example.bridge.bidding.SAYS;

import static com.example.bridge.bidding.SAYS.OpenBid1SAYS.OpeningStrongBidding;

import com.example.bridge.bidding.Constraints.Shape;
import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class CompeteSAYS extends SAYS {

    public static Iterable<CallFeature> bids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        bids.add(shows(Bid._4H, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, setTrumpColor(Suit.Hearts), id("CompeteSAYS.compBids FIT_8_PLUS pairHighCardPoints PAIR_GAME _4H")));
        bids.add(shows(Bid._4S, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, setTrumpColor(Suit.Spades), id("CompeteSAYS.compBids FIT_8_PLUS pairHighCardPoints PAIR_GAME _4S")));

        bids.add(shows(Bid._4H, pairHighCardPoints(PAIR_GAME_INVITE), FIT_8_PLUS, IS_FORCED_TO_GAME, setTrumpColor(Suit.Hearts), id("CompeteSAYS.compBids PAIR_GAME_INVITE _4H")));
        bids.add(shows(Bid._4S, pairHighCardPoints(PAIR_GAME_INVITE), FIT_8_PLUS, IS_FORCED_TO_GAME, setTrumpColor(Suit.Spades), id("CompeteSAYS.compBids PAIR_GAME_INVITE _4S")));

        bids.add(shows(Bid._2C, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Clubs), id("CompeteSAYS.compBids _2C")));
        bids.add(shows(Bid._2D, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Diamonds), id("CompeteSAYS.compBids _2D")));
        bids.add(shows(Bid._2H, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Hearts), id("CompeteSAYS.compBids _2H")));
        bids.add(shows(Bid._2S, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, setTrumpColor(Suit.Spades), id("CompeteSAYS.compBids _2S")));

        bids.add(shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, noFit(), not(currentContract(Bid._2C)), setTrumpColor(Suit.Clubs), id("CompeteSAYS.compBids FIT_8_PLUS _3C")));
        bids.add(shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), FIT_8_PLUS, noFit(), not(currentContract(Bid._2D)), setTrumpColor(Suit.Diamonds), id("CompeteSAYS.compBids FIT_8_PLUS _3D")));

        bids.add(shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), fit(9), PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteSAYS.compBids fit(9), _5C")));
        bids.add(shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), fit(9), PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteSAYS.compBids fit(9) _5D")));
        bids.add(shows(Bid._5C, pairPoints(PAIR_GAME), fit(9), PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteSAYS.compBids pairPoints, _5C")));
        bids.add(shows(Bid._5D, pairPoints(PAIR_GAME), fit(9), PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteSAYS.compBids pairPoints _5D")));

        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, id("CompeteSAYS.compBids PAIR_BALANCED PAIR_GAME _3NT")));

        bids.add(shows(Bid._5C, pairHighCardPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteSAYS.compBids PAIR_MINOR_GAME, _5C")));
        bids.add(shows(Bid._5D, pairHighCardPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteSAYS.compBids PAIR_MINOR_GAME _5D")));
        bids.add(shows(Bid._5C, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, IS_FORCED_TO_GAME, setTrumpColor(Suit.Clubs), id("CompeteSAYS.compBids IS_FORCED_TO_GAME _5C")));
        bids.add(shows(Bid._5D, pairHighCardPoints(PAIR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, IS_FORCED_TO_GAME, setTrumpColor(Suit.Diamonds), id("CompeteSAYS.compBids IS_FORCED_TO_GAME _5D")));
        bids.add(shows(Bid._5C, pairPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Clubs), id("CompeteSAYS.compBids pairPoints _5C")));
        bids.add(shows(Bid._5D, pairPoints(PAIR_MINOR_GAME), FIT_8_PLUS, PARTNER_DID_NOT_SIGN_OFF, setTrumpColor(Suit.Diamonds), id("CompeteSAYS.compBids pairPoints _5D")));

        bids.add(shows(Bid._2S, shape(2, 10), partner(isLastBid(Bid._2H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), betterThan(Suit.Spades, Suit.Hearts), id("CompeteSAYS.compBids _2S")));
        bids.add(shows(Bid._3S, shape(3, 4), shape(Suit.Hearts, 0, 3), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Spades, Suit.Hearts), id("CompeteSAYS.compBids _3S")));
        bids.add(shows(Bid._3S, shape(2, 3), shape(Suit.Hearts, 0, 2), partner(isLastBid(Bid._3H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Spades, Suit.Hearts), id("CompeteSAYS.compBids _3S")));
        bids.add(shows(Bid._4S, shape(2, 3), shape(Suit.Hearts, 0, 2), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 5)), betterThan(Suit.Spades, Suit.Hearts), id("CompeteSAYS.compBids _4S")));
        bids.add(shows(Bid._4S, shape(3, 4), shape(Suit.Hearts, 0, 3), partner(isLastBid(Bid._4H)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Spades, Suit.Hearts), id("CompeteSAYS.compBids _4S")));

        bids.add(shows(Bid._4S, partner(isLastBid(Bid._4D)), shape(2, 3), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Spades, Suit.Diamonds), id("CompeteSAYS.compBids betterThan(Suit.Diamonds) _4S")));
        bids.add(shows(Bid._4S, partner(isLastBid(Bid._4C)), shape(2, 3), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Spades, Suit.Clubs), id("CompeteSAYS.compBids betterThan(Suit.Clubs) _4S")));
        bids.add(shows(Bid._4H, partner(isLastBid(Bid._4D)), shape(2, 3), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Hearts, Suit.Diamonds), id("CompeteSAYS.compBids betterThan(Suit.Diamonds) _4H")));
        bids.add(shows(Bid._4H, partner(isLastBid(Bid._4C)), shape(2, 3), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Hearts, Suit.Clubs), id("CompeteSAYS.compBids  betterThan(Suit.Clubs) _4H")));

        bids.add(shows(Bid._5D, partner(isLastBid(Bid._4D)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Diamonds, Suit.Spades), id("CompeteSAYS.compBids _5D")));
        bids.add(shows(Bid._5D, partner(isLastBid(Bid._4D)), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Diamonds, Suit.Hearts), id("CompeteSAYS.compBids _5D")));
        bids.add(shows(Bid._5C, partner(isLastBid(Bid._4C)), partner(new Shape.HasMinShape(Suit.Spades, 4)), betterThan(Suit.Clubs, Suit.Spades), id("CompeteSAYS.compBids _5C")));
        bids.add(shows(Bid._5C, partner(isLastBid(Bid._4C)), partner(new Shape.HasMinShape(Suit.Hearts, 4)), betterThan(Suit.Clubs, Suit.Hearts), id("CompeteSAYS.compBids _5C")));

        bids.add(shows(Bid._3D, partner(isLastBid(Bid._3C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), betterThan(Suit.Diamonds, Suit.Clubs), id("CompeteSAYS.compBids _3D")));
        bids.add(shows(Bid._5D, partner(isLastBid(Bid._5C)), partner(new Shape.HasMinShape(Suit.Diamonds, 5)), betterThan(Suit.Diamonds, Suit.Clubs), id("CompeteSAYS.compBids _5D")));

        bids.add(shows(Bid._3H, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteSAYS.compBids PAIR_GAME _3H")));
        bids.add(shows(Bid._3S, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteSAYS.compBids PAIR_GAME _3S")));
        bids.add(shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteSAYS.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3C, pairHighCardPoints(PAIR_LOW_GAME), shape(6, 10), noFit(), IS_REBID, id("CompeteSAYS.compBids PAIR_GAME _3C")));
        bids.add(shows(Bid._3D, pairHighCardPoints(PAIR_LOW_GAME), shape(7, 10), noFit(), id("CompeteSAYS.compBids PAIR_GAME _3D")));

        bids.add(shows(Bid._3NT, OpeningStrongBidding, noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), IS_NON_JUMP, othersAtLeast(3), id("CompeteSAYS.compBids OpeningStrongBidding exit _3NT")));
        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), IS_NON_JUMP, othersAtLeast(3), id("CompeteSAYS.compBids PAIR_GAME exit _3NT")));
        bids.add(shows(Bid._2NT, noFit(7), partner(isLastBid(Bid._2C, Bid._2D, Bid._2H, Bid._2S)), IS_NON_JUMP, othersAtLeast(3), id("CompeteSAYS.compBids exit _2NT")));
        bids.add(shows(Bid._5NT, pairHighCardPoints(PAIR_MINOR_GAME), noFit(7), partner(isLastBid(Bid._5C, Bid._5D)), IS_NON_JUMP, othersAtLeast(3), id("CompeteSAYS.compBids PAIR_MINOR_GAME exit _5NT")));

        bids.add(shows(Bid._3NT, OpeningStrongBidding, noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteSAYS.compBids OpeningStrongBidding exit _3NT")));
        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), noFit(7), partner(isLastBid(Bid._3H, Bid._3S)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteSAYS.compBids PAIR_GAME exit _3NT")));
        bids.add(shows(Bid._2NT, noFit(7), partner(isLastBid(Bid._2C, Bid._2D, Bid._2H, Bid._2S)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteSAYS.compBids exit _2NT")));
        bids.add(shows(Bid._5NT, pairHighCardPoints(PAIR_MINOR_GAME), noFit(7), partner(isLastBid(Bid._5C, Bid._5D)), PAIR_BALANCED, IS_NON_JUMP, id("CompeteSAYS.compBids PAIR_MINOR_GAME exit _5NT")));

        bids.add(shows(Bid._3C, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Clubs, 5), id("CompeteSAYS.compBids _3C")));
        bids.add(shows(Bid._3D, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Diamonds, 5), id("CompeteSAYS.compBids _3D")));
        bids.add(shows(Bid._3H, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Hearts, 5), id("CompeteSAYS.compBids _3H")));
        bids.add(shows(Bid._3S, shape(5, 10), partner(isLastBid(Bid._2NT)), secondSuit(Suit.Spades, 5), id("CompeteSAYS.compBids _3S")));

        bids.add(shows(Bid._3H, shape(4, 10), partner(isLastBid(Bid._2NT)), NOT_BALANCED, IS_NEW_SUIT, secondSuit(Suit.Hearts, 4), id("CompeteSAYS.compBids _3H")));
        bids.add(shows(Bid._3S, shape(4, 10), partner(isLastBid(Bid._2NT)), NOT_BALANCED, IS_NEW_SUIT, secondSuit(Suit.Spades, 4), id("CompeteSAYS.compBids _3S")));

        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, IS_NON_JUMP, id("CompeteSAYS.recursionFindFitGame BALANCED, IS_NON_JUMP _3NT")));
        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), noFit(), IS_NON_JUMP, othersAtLeast(3), id("CompeteSAYS.recursionFindFitGame pairHighCardPoints(PAIR_GAME), noFit(), IS_NON_JUMP, othersAtLeast(3) _3NT")));
        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), BALANCED, partner(isLastBid(Bid._1NT)), id("CompeteSAYS.recursionFindFitGame pairHighCardPoints(PAIR_GAME), BALANCED, partner(isLastBid(Bid._1NT)), _3NT")));
        bids.add(shows(Bid._3NT, pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._2NT)), id("CompeteSAYS.recursionFindFitGame pairHighCardPoints(PAIR_MINOR_GAME), partner(isLastBid(Bid._2NT)), _3NT")));

        bids.add(shows(Call.PASS, ruleDescription("compBids _PASS"), id("CompeteSAYS.compBids _PASS")));

        bids.add(partnerBids(RecursionSAYS::recursionFindFitGame));

        return bids;
    }

    public static Iterable<CallFeature> compBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        addAcesAskConventions(ps, bids);
        addCompBids(ps, bids);
        bids.add(partnerBids(RecursionSAYS::recursionFindFitGame));

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
        boolean isLevel4 = (lastBid != null && lastBid.getLevel() == 4);

        if (jumpMatch || agreedSuitMatch || isLevel1 || isLevel2) {
            if (agreedTrump != null && !isLevel4) {
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
                if (lastBid != null && lastBid.equals(Bid._2NT)) {
                    for (CallFeature cf : AcesAsk.initiateConventionBlok(ps)) {
                        bids.add(cf);
                    }
                }

            }
        }
    }

    private static void addCompBids(PositionState ps, List<CallFeature> bids) {
        for (CallFeature cf : CompeteSAYS.bids(ps)) {
            bids.add(cf);
        }
    }
}
