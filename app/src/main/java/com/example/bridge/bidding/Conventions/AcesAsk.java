package com.example.bridge.bidding.Conventions;

import static com.example.bridge.bidding.NatC.OpenNatC.OpeningStrongBidding;

import com.example.bridge.bidding.Constraints.AgreedStrain;
import com.example.bridge.bidding.NatC.CompeteNatC;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.LCStandard.UserText;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Strain;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class AcesAsk extends Bidder {
    private static final Range ASK_ACES = new Range(14, 40);
    private static final Range HIGHT_GAME = new Range(28, 40);
    private static final Range SLAM_OR_BETTER = new Range(30, 40);
    private static final Range GRAND_SLAM = new Range(36, 40);


    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        Bid partnerBid = ps.getPartner().getBid();
        Suit partnerSuit = (partnerBid != null) ? partnerBid.getSuit() : null;

        bids.add(properties(Bid._4C, AcesAsk::respondCountAces, true, true, false, partnerSuit, null, null, UserText.AcesAsc, null));
        bids.add(shows(Bid._4C, CONTRACT_IS_AGREED_STRAIN, pairHighCardPoints(HIGHT_GAME), highCardPoints(ASK_ACES), id(" initiateConventionAcesAsk 1")));
        bids.add(shows(Bid._4C, fit(partnerSuit), setTrumpColor(partnerSuit), IS_ANY_JUMP, highCardPoints(ASK_ACES), pairHighCardPoints(HIGHT_GAME), id(" initiateConventionAcesAsk 2")));

        return bids;
    }

    private static Suit getAgreedSuit(PositionState ps) {
        Suit trump = ps.getPairState().getTrumpSuit();
        if (trump != null) return trump;
        return null;
    }

    public static Iterable<CallFeature> initiateConventionBlok(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        Bid partnerBid = ps.getPartner().getBid();
        Suit partnerSuit = (partnerBid != null) ? partnerBid.getSuit() : null;
        //System.out.println(ps.getPartner().getPublicHandSummary().getHighCardPoints() );
        bids.add(properties(Bid._4NT, AcesAsk::respondCountAcesBlok, true, true, false, partnerSuit, null, null, UserText.AcesAsc, null));

        bids.add(shows(Bid._4NT, IS_ANY_JUMP, fit(partnerSuit), pairHighCardPoints(SLAM_OR_BETTER), setTrumpColor(partnerSuit), id("initiateConventionBlok AcesAsk 1")));
        bids.add(shows(Bid._4NT, isJump(1), pairHighCardPoints(SLAM_OR_BETTER), highCardPoints(ASK_ACES), id("initiateConventionBlok AcesAsk 2")));
        bids.add(shows(Bid._4NT, CONTRACT_IS_AGREED_STRAIN, highCardPoints(ASK_ACES), pairHighCardPoints(SLAM_OR_BETTER), id("initiateConventionBlok AcesAsk 3")));
        bids.add(shows(Bid._4NT, pairHighCardPoints(SLAM_OR_BETTER), highCardPoints(ASK_ACES), partner(isLastBid(Bid._4D, Bid._4C)), id("initiateConventionBlok AcesAsk 4")));

        return bids;
    }

    public static PositionCalls respondCountAcesBlok(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                properties(new Call[]{Bid._5C, Bid._5D, Bid._5H, Bid._5S, Bid._5NT}, AcesAsk::askKing, true),

                shows(Bid._5C, aces(0), id("respondCountAcesBlok 0")),
                shows(Bid._5D, aces(1), id("respondCountAcesBlok 1")),
                shows(Bid._5H, aces(2), id("respondCountAcesBlok 2")),
                shows(Bid._5S, aces(3), id("respondCountAcesBlok 3")),
                shows(Bid._5NT, aces(4), id("respondCountAcesBlok 4"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls respondCountAces(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                properties(new Call[]{Bid._4D, Bid._4H, Bid._4S, Bid._4NT, Bid._5C}, AcesAsk::askKing, true),

                shows(Bid._4D, aces(0), id("respondCountAces 0")),
                shows(Bid._4H, aces(1), id("respondCountAces 1")),
                shows(Bid._4S, aces(2), id("respondCountAces 2")),
                shows(Bid._4NT, aces(3), id("respondCountAces 3")),
                shows(Bid._5C, aces(4), id("respondCountAces 4"))
        );
        return choices;
    }

    public static PositionCalls askKing(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        Call partnerCall = ps.getPartner().getLastCall();
        if (suit != null) {
            if (suit.isMinor()) {
                choices.addRules(
                        shows(Call.PASS, pairAces(1,3),kings(1,2), partner(isLastBid(5, suit)),id("askKing isMinor 1 5")),
                        shows(new Bid(5, suit), pairAces(1), id("askKing isMinor 1 5")),
                        shows(new Bid(5, suit), pairAces(2), id("askKing isMinor 2 5"))
                );
            } else if (suit.isMajor()) {
                choices.addRules(
                        shows(Call.PASS, pairAces(1,3),kings(1,2), partner(isLastBid(5, suit)),id("askKing isMinor 1 5")),
                        shows(new Bid(4, suit), pairAces(1), id("askKing isMajor 1 4")),
                        shows(new Bid(4, suit), pairAces(2), id("askKing isMajor 2 4")));

            }
            Bid bid = getNextBidWithoutTrump(partnerCall, suit);
            choices.addRules(
                    properties(bid, AcesAsk::respondKings, true),
                    shows(bid, pairAces(3), id("askKing isMajor 3")),
                    shows(bid, pairAces(4), id("askKing isMajor 4")));

            choices.addRules(shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, id("askKing CONTRACT_IS_AGREED_STRAIN")));
            choices.addRules(shows(Call.PASS));
        } else {
            Bid bid = (Bid) Call.getNextCall(partnerCall);
            choices.addRules(
                    properties(bid, AcesAsk::respondKings, true),
                    shows(Bid._4NT, pairAces(1), id("askKing 4NT 1")),
                    shows(Bid._4NT, pairAces(2), id("askKing 4NT 2")),
                    shows(Bid._5NT, pairAces(1), id("askKing 5NT 1")),
                    shows(Bid._5NT, pairAces(2), id("askKing 5NT 2")),
                    shows(bid, pairAces(3), id("askKing 3")),
                    shows(bid, pairAces(4), id("askKing 4")));

            choices.addRules(shows(Call.PASS));
        }
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls respondKings(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Call partnerCall = ps.getPartner().getLastCall();

        Call call0Kings = Call.getNextCall(partnerCall);
        Call call1Kings = Call.getNextCall(call0Kings);
        Call call2Kings = Call.getNextCall(call1Kings);
        Call call3Kings = Call.getNextCall(call2Kings);
        Call call4Kings = Call.getNextCall(call3Kings);

        choices.addRules(
                properties(new Call[]{call0Kings, call1Kings, call2Kings, call3Kings, call4Kings}, AcesAsk::tryGrandSlam, false),
                shows(call0Kings, kings(0), id("respondKings 0")),
                shows(call1Kings, kings(1), id("respondKings 1")),
                shows(call2Kings, kings(2), id("respondKings 2")),
                shows(call3Kings, kings(3), id("respondKings 3")),
                shows(call4Kings, kings(4), id("respondKings 4"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls tryGrandSlam(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        Call partnerCall = ps.getPartner().getLastCall();
        Bid nextBidWithTrump = getNextBidWithTrump(partnerCall, suit);
        if (suit != null) {
            choices.addRules(
                    shows(new Bid(7, suit), FIT_8_PLUS, sumPairAcesAndKings(8), id("AcesAsk tryGrandSlam 1")),
                    shows(new Bid(7, suit), FIT_8_PLUS, pairAces(4), pairKings(3), pairHighCardPoints(GRAND_SLAM), id("AcesAsk tryGrandSlam 2")),
                    shows(new Bid(6, suit), FIT_8_PLUS, pairAces(4), pairKings(3), pairHighCardPoints(SLAM_OR_BETTER), id("AcesAsk tryGrandSlam 2")),
                    shows(new Bid(6, suit), FIT_8_PLUS, sumPairAcesAndKings(7), id("AcesAsk tryGrandSlam 3")),
                    shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, id("AcesAsk tryGrandSlam 4")),
                    shows(new Bid(6, suit), FIT_8_PLUS, secondSuit(suit, 6), hasShortness(0, 1), sumPairAcesAndKings(6, 7), id("AcesAsk tryGrandSlam 5")),
                    shows(nextBidWithTrump, FIT_8_PLUS, sumPairAcesAndKings("Suma asów i króli mniejsza od 6", 1, 6), id("AcesAsk tryGrandSlam 6")),
                    shows(new Bid(7, Strain.NoTrump), pairHighCardPoints(GRAND_SLAM), sumPairAcesAndKings(8), id("AcesAsk tryGrandSlam 7")),
                    shows(new Bid(6, Strain.NoTrump), pairHighCardPoints(SLAM_OR_BETTER), id("AcesAsk tryGrandSlam 8"))
            );

        } else {
            choices.addRules(
                    shows(Bid._7NT, pairHighCardPoints(GRAND_SLAM), pairAces(4), pairKings(3, 4), id("AcesAsk tryGrandSlam 7NT")),
                    shows(Bid._6NT, pairHighCardPoints(SLAM_OR_BETTER), sumPairAcesAndKings(7), id("AcesAsk tryGrandSlam 6NT")),
                    shows(Bid._5NT)

            );
        }


        return choices;
    }

    private static Bid getNextBidWithoutTrump(Call partnerCall, Suit suit) {
        if (partnerCall != null) {
            Call nCall = Call.getNextCall(partnerCall);

            while (true) {
                if (nCall instanceof Bid) {
                    Suit suitOfNextCall = ((Bid) nCall).getSuit();
                    if (suit != suitOfNextCall) {
                        return (Bid) nCall;
                    } else {
                        nCall = Call.getNextCall(nCall);
                    }
                }
            }

        }
        return (Bid) Call.PASS;
    }

    private static Bid getNextBidWithTrump(Call partnerCall, Suit suit) {
        if (partnerCall != null) {
            Call nCall = Call.getNextCall(partnerCall);

            while (true) {
                if (nCall instanceof Bid) {
                    Suit suitOfNextCall = ((Bid) nCall).getSuit();
                    if (suit == suitOfNextCall) {
                        return (Bid) nCall;
                    } else {
                        nCall = Call.getNextCall(nCall);
                    }
                }
            }

        }
        return (Bid) Call.PASS;
    }
}