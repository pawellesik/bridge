package com.example.bridge.bidding.Conventions;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.LCStandard.UserText;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class AcesAsk extends Bidder {
    private static final Range ASK_ACES = new Range(16, 40);

    private static final Range HIGHT_GAME = new Range(28, 40);
    private static final Range SLAM_OR_BETTER = new Range(32, 40);
    private static final Range GRAND_SLAM = new Range(36, 40);


    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(properties(Bid._4C, AcesAsk::respondCountAces, true, true, false, ps.getPartner().getBid().getSuit(), null, null, UserText.AcesAsc, null));
        bids.add(shows(Bid._4C, fit(ps.getPartner().getBid().getSuit()), IS_ANY_JUMP, points(ASK_ACES), id("AcesAsk 1"), pairHighCardPoints(HIGHT_GAME)));
        bids.add(shows(Bid._4C, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(SLAM_OR_BETTER), id("AcesAsk 2")));
        return bids;
    }

    private static Suit getAgreedSuit(PositionState ps) {
        Suit trump = ps.getPairState().getTrumpSuit();
        if (trump != null) return trump;
        return ps.getPairState().getLastShownSuit();
    }

    public static Iterable<CallFeature> initiateConventionBlok(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(properties(Bid._4NT, AcesAsk::respondCountAcesBlok, true, true, false, ps.getPartner().getBid().getSuit(), null, null, UserText.AcesAsc, null));
        bids.add(shows(Bid._4NT, fit(ps.getPartner().getBid().getSuit()), pairHighCardPoints(SLAM_OR_BETTER), id("AcesAsk 2")));
        return bids;
    }

    public static PositionCalls respondCountAcesBlok(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                    properties(new Call[]{Bid._5C, Bid._5D, Bid._5H, Bid._5S, Bid._5NT}, AcesAsk::askKing, true),

                    shows(Bid._5C, aces(0)),
                    shows(Bid._5D, aces(1)),
                    shows(Bid._5H, aces(2)),
                    shows(Bid._5S, aces(3)),
                    shows(Bid._5NT, aces(4))
            );
            return choices;
        }
        throw new RuntimeException("This should never happen. No agreed suit.");
    }

    public static PositionCalls respondCountAces(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                    properties(new Call[]{Bid._4D, Bid._4H, Bid._4S, Bid._4NT, Bid._5C}, AcesAsk::askKing, true),

                    shows(Bid._4D, aces(0)),
                    shows(Bid._4H, aces(1)),
                    shows(Bid._4S, aces(2)),
                    shows(Bid._4NT, aces(3)),
                    shows(Bid._5C, aces(4))
            );
            return choices;
        }
        throw new RuntimeException("This should never happen. No agreed suit.");
    }

    public static PositionCalls askKing(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            if (suit.isMinor()) {
                choices.addRules(
                        shows(new Bid(5, suit), pairAces(1)),
                        shows(new Bid(5, suit), pairAces(2))
                );
            } else if (suit.isMajor()) {
                choices.addRules(
                        shows(new Bid(4, suit), pairAces(1)),
                        shows(new Bid(4, suit), pairAces(2)));

            }
            Call partnerCall = ps.getPartner().getLastCall();
            Bid bid = getNextBidWithoutTrump(partnerCall, suit);
            choices.addRules(
                    properties(bid, AcesAsk::respondKings, true),
                    shows(bid, pairAces(3)),
                    shows(bid, pairAces(4)));

            choices.addRules(shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN));
            choices.addRules(shows(Call.PASS));
            return choices;
        }
        throw new RuntimeException("No agreed suit in askKing");
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
                shows(call0Kings, kings(0)),
                shows(call1Kings, kings(1)),
                shows(call2Kings, kings(2)),
                shows(call3Kings, kings(3)),
                shows(call4Kings, kings(4))
        );
        return choices;
    }

    public static PositionCalls tryGrandSlam(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        Call partnerCall = ps.getPartner().getLastCall();
        Bid nextBidWithTrump = getNextBidWithTrump(partnerCall, suit);
        if (suit != null) {
            choices.addRules(
                    shows(new Bid(7, suit), sumPairAcesAndKings(8), id("AcesAsk tryGrandSlam 1")),
                    shows(new Bid(7, suit), pairAces(4), pairKings(3), pairPoints(GRAND_SLAM), id("AcesAsk tryGrandSlam 2")),
                    shows(new Bid(6, suit), pairAces(4), pairKings(3), pairPoints(SLAM_OR_BETTER), id("AcesAsk tryGrandSlam 2")),
                    shows(new Bid(6, suit), sumPairAcesAndKings(7), id("AcesAsk tryGrandSlam 3")),
                    shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, id("AcesAsk tryGrandSlam 4")),
                    shows(new Bid(6, suit), secondSuit(suit, 6), hasShortness(0, 1), sumPairAcesAndKings(6, 7), id("AcesAsk tryGrandSlam 5")),
                    shows(nextBidWithTrump, sumPairAcesAndKings("Suma asów i króli mniejsza od 6", 1, 6), id("AcesAsk tryGrandSlam 6"))
            );
            return choices;
        }
        throw new RuntimeException("This should not happen");
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




























































































































































































































































































































































































































































































