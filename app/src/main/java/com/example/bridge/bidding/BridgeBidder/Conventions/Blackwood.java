package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.bridge.bidding.BridgeBidder.LCStandard.UserText;
import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.Bidder;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.CallFeature;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCalls;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class Blackwood extends Bidder {
    private static final Range SLAM_OR_BETTER = new Range(32, 100);
    private static final Range GRAND_SLAM = new Range(36, 100);

    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            bids.add(properties(Bid._4NT, Blackwood::respondKeyCards, true, UserText.Blackwood));
            bids.add(shows(Bid._4NT, pairPoints(suit, SLAM_OR_BETTER.getMin(), SLAM_OR_BETTER.getMax())));
        }
        return bids;
    }

    private static Suit getAgreedSuit(PositionState ps) {
        Suit trump = ps.getPairState().getTrumpSuit();
        if (trump != null) return trump;
        return ps.getPairState().getLastShownSuit();
    }

    public static PositionCalls respondKeyCards(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                properties(new Call[]{Bid._5C, Bid._5D, Bid._5H, Bid._5S}, Blackwood::placeContract, true),
                shows(Bid._5C, keyCards(suit, 1, 4)),
                shows(Bid._5D, keyCards(suit, 0, 3)),
                shows(Bid._5H, keyCards(suit, false, 2, 5)),
                shows(Bid._5S, keyCards(suit, true, 2, 5))
            );
            return choices;
        }
        throw new RuntimeException("This should never happen. No agreed suit.");
    }

    public static PositionCalls placeContract(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                properties(Bid._5NT, Blackwood::respondKings, true),
                shows(Bid._5NT, pairKeyCards(suit, true, 5), pairPoints(GRAND_SLAM)),
                shows(Bid._5NT, pairKeyCards(suit, true, 5), pairPoints(SLAM_OR_BETTER), fit(9, suit)),

                shows(new Bid(6, suit), pairPoints(SLAM_OR_BETTER), pairKeyCards(suit, null, 4, 5)),

                shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, pairKeyCards(suit, null, 0, 1, 2, 3)),

                shows(new Bid(5, suit), pairKeyCards(suit, null, 0, 1, 2, 3)),
                shows(new Bid(6, suit), IS_NON_JUMP, pairKeyCards(suit, null, 0, 1, 2, 3))
            );
            return choices;
        }
        throw new RuntimeException("This should not happen");
    }

    public static PositionCalls respondKings(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            properties(new Call[]{Bid._6C, Bid._6D, Bid._6H, Bid._6S}, Blackwood::tryGrandSlam, true),
            shows(Bid._6C, kings(0, 4)),
            shows(Bid._6D, kings(1)),
            shows(Bid._6H, kings(2)),
            shows(Bid._6S, kings(3))
        );
        return choices;
    }

    public static PositionCalls tryGrandSlam(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                shows(new Bid(7, suit), pairKeyCards(suit, true, 5), pairKings(4)),
                shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN),
                shows(new Bid(6, suit)),
                shows(new Bid(7, suit))
            );
            return choices;
        }
        throw new RuntimeException("This should not happen");
    }
}




























































































































































































































































































































































































































































































