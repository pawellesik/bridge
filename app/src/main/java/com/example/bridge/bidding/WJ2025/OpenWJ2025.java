package com.example.bridge.bidding.WJ2025;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.Diamond.RespondDiamondBid1;

import java.util.ArrayList;
import java.util.List;

public class OpenWJ2025 extends WJ2025 {

    public static final HandConstraint OpenBidding = highCardPoints(12, 17);
    public static final HandConstraint OpenBiddingThirtSeat = highCardPoints(11, 17);
    public static final HandConstraint OpenBiddingLow = highCardPoints(12, 14);
    public static final HandConstraint OpenBiddingLowThirtSeat = highCardPoints(11, 14);
    public static final HandConstraint OpenBiddingHight = highCardPoints(15, 17);
    public static final HandConstraint OpeningStrongBidding = highCardPoints(18, 40);
    public static final HandConstraint DontOpen = highCardPoints(0, 10);
    public static final HandConstraint OpeningWeakBidding = highCardPoints(6, 11);
    public static final HandConstraint Opening2NT = highCardPoints(4, 11);

    public static PositionCalls getOpenPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(SolidSuitWJ2025.BIDS(ps));
        choices.addRules(NoTrumpWJ2025.open1NTBid1(ps));
        choices.addRules(openSuitWeak(ps));
        choices.addRules(openSuit(ps));

        return choices;
    }

    public static Iterable<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        bids.add(shows(Bid._2NT, shape(Suit.Clubs, 5, 13), shape(Suit.Diamonds, 5, 13), Opening2NT, ruleShow(1), id("OpenWJ2025.openSuit _2NT")));

        bids.add(shows(Bid._2S, shape(5, 13), shape(Suit.Clubs, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2S")));
        bids.add(shows(Bid._2S, shape(5, 13), shape(Suit.Diamonds, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2S")));

        bids.add(shows(Bid._2H, shape(5, 13), shape(Suit.Clubs, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2H")));
        bids.add(shows(Bid._2H, shape(5, 13), shape(Suit.Diamonds, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2H")));
        bids.add(shows(Bid._2H, shape(5, 13), shape(Suit.Spades, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2H")));

        bids.add(shows(Bid._2D, shape(Suit.Spades, 6, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2D")));
        bids.add(shows(Bid._2D, shape(Suit.Hearts, 6, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2D")));

        bids.add(shows(Bid._2C, shape(Suit.Spades, 4, 13), shape(Suit.Hearts, 4, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJ2025.openSuit _2C")));

        if (ps.getSeat() < 3) {
            bids.add(shows(Bid._1S, shape(6, 13), OpenBidding, id("OpenWJ2025.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(6, 13), OpenBidding, id("OpenWJ2025.openSuit _1H")));
            bids.add(shows(Bid._1S, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJ2025.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJ2025.openSuit _1H")));

            bids.add(shows(Bid._1D, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJ2025.openSuit _1D")));
            bids.add(shows(Bid._1D, shape4441(Suit.Clubs), OpenBidding, ruleShow(1), id("OpenWJ2025.openSuit _1D")));

            bids.add(shows(Bid._1C, OpenBiddingLow, BALANCED, ruleShow(1), id("OpenWJ2025.openSuit _1C")));
            bids.add(shows(Bid._1C, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJ2025.openSuit _1C")));
            bids.add(shows(Bid._1C, OpeningStrongBidding, ruleShow(1)));
            bids.add(shows(Bid._1C, OpenBidding, id("OpenWJ2025.openSuit _1C")));

        } else if (ps.getSeat() >= 3) {
            bids.add(shows(Bid._1S, shape(6, 13), OpenBiddingThirtSeat, id("OpenWJ2025.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(6, 13), OpenBiddingThirtSeat, id("OpenWJ2025.openSuit _1H")));
            bids.add(shows(Bid._1S, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJ2025.openSuit OpenAfterPass _1S")));
            bids.add(shows(Bid._1H, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJ2025.openSuit OpenAfterPass _1H")));

            bids.add(shows(Bid._1D, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJ2025.openSuit _1D")));
            bids.add(shows(Bid._1D, shape4441(Suit.Clubs), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJ2025.openSuit _1D")));

            bids.add(shows(Bid._1C, OpenBiddingLowThirtSeat, BALANCED, ruleShow(1), id("OpenWJ2025.openSuit _1C")));
            bids.add(shows(Bid._1C, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJ2025.openSuit _1C")));
            bids.add(shows(Bid._1C, OpeningStrongBidding, ruleShow(1)));
            bids.add(shows(Bid._1C, OpenBiddingThirtSeat, id("OpenWJ2025.openSuit _1C")));
        }

        bids.add(shows(Call.PASS, DontOpen, id("OpenWJ2025.openSuit _PASS")));



        bids.add(partnerBids(Bid._1D, RespondDiamondBid1::oneDiamond));

        return bids;
    }

    private static List<CallFeature> openSuitWeak(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(shows(Bid._3S, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJ2025.openSuitWeak _3S")));
        rules.add(shows(Bid._3H, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJ2025.openSuitWeak _3H")));
        rules.add(shows(Bid._3D, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJ2025.openSuitWeak _3D")));
        rules.add(shows(Bid._3C, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJ2025.openSuitWeak _3C")));
        return rules;
    }
}
