package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class OpenBid1WJSimple extends WJSimple {

    public static final HandConstraint OpenBidding = highCardPoints(12, 17);
    public static final HandConstraint OpenBiddingThirtSeat = highCardPoints(11, 17);
    public static final HandConstraint OpenBiddingLow = highCardPoints(12, 14);
    public static final HandConstraint OpeningLowBidding = OpenBiddingLow;
    public static final HandConstraint OpenBiddingLowThirtSeat = highCardPoints(11, 14);
    public static final HandConstraint OpenBiddingHight = highCardPoints(15, 17);
    public static final HandConstraint OpeningInviteBidding = OpenBiddingHight;
    public static final HandConstraint OpeningStrongBidding = highCardPoints(18, 40);

    public static final HandConstraint DontOpen = highCardPoints(0, 10);

    public static final HandConstraint OpeningWeakBidding = highCardPoints(6, 10);
    public static final HandConstraint GameAfterNegat = highCardPoints(5, 6);

    public static PositionCalls getOpenPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(SolidSuitWJSimple.BIDS(ps));
        choices.addRules(NoTrumpWJSimple.open1NTBid1(ps));
        choices.addRules(openSuitWeak(ps));
        choices.addRules(openSuit(ps));

        return choices;
    }

    public static Iterable<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        bids.add(shows(Bid._2NT, shape(Suit.Clubs, 5, 13),shape(Suit.Diamonds, 5, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJSimple.openSuit _2NT")));

        bids.add(shows(Bid._2S, shape(5, 13), shape(Suit.Clubs, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJSimple.openSuit _2S")));
        bids.add(shows(Bid._2S, shape(5, 13), shape(Suit.Diamonds, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJSimple.openSuit _2S")));

        bids.add(shows(Bid._2H, shape(5, 13), shape(Suit.Clubs, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJSimple.openSuit _2H")));
        bids.add(shows(Bid._2H, shape(5, 13), shape(Suit.Diamonds, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJSimple.openSuit _2H")));
        bids.add(shows(Bid._2H, shape(5, 13), shape(Suit.Spades, 5, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJSimple.openSuit _2H")));

        bids.add(shows(Bid._2D, shape(Suit.Spades, 6, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJSimple.openSuit _2D")));
        bids.add(shows(Bid._2D, shape(Suit.Hearts, 6, 13), OpeningWeakBidding, ruleShow(1),id("OpenWJSimple.openSuit _2D")));

        bids.add(shows(Bid._2C, shape(5, 13), shape(Suit.Hearts, 4, 13), OpenBiddingLowThirtSeat, ruleShow(1),id("OpenWJSimple.openSuit _2C")));
        bids.add(shows(Bid._2C, shape(5, 13), shape(Suit.Spades, 4, 13), OpenBiddingLowThirtSeat, ruleShow(1),id("OpenWJSimple.openSuit _2C")));
        bids.add(shows(Bid._2C, shape(6, 13), OpenBiddingLowThirtSeat, ruleShow(1),id("OpenWJSimple.openSuit _2C")));


        if (ps.getSeat() < 3) {
            bids.add(shows(Bid._1S, shape(6, 13), OpenBidding, id("OpenWJSimple.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(6, 13), OpenBidding, id("OpenWJSimple.openSuit _1H")));
            bids.add(shows(Bid._1S, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1H")));

            //bids.add(shows(Bid._1D, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1D")));
            //bids.add(shows(Bid._1D, shape(4, 13), shape(Suit.Clubs, 5, 13), OpenBiddingLow, ruleShow(1), id("OpenWJSimple.openSuit _1D")));
            bids.add(shows(Bid._1D, shape(4, 4), shape(Suit.Clubs, 1, 1),  shape4441(), OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1D")));

            bids.add(shows(Bid._1C, OpenBiddingLow, BALANCED, ruleShow(1), id("OpenWJSimple.openSuit _1C")));
            bids.add(shows(Bid._1C, shape(5, 13), OpenBiddingHight, ruleShow(1), id("OpenWJSimple.openSuit _1C")));
            bids.add(shows(Bid._1C, OpeningStrongBidding, ruleShow(1)));

        } else if (ps.getSeat() >= 3) {
            bids.add(shows(Bid._1S, shape(6, 13), OpenBiddingThirtSeat, id("OpenWJSimple.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(6, 13), OpenBiddingThirtSeat, id("OpenWJSimple.openSuit _1H")));
            bids.add(shows(Bid._1S, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit OpenAfterPass _1S")));
            bids.add(shows(Bid._1H, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit OpenAfterPass _1H")));

            //bids.add(shows(Bid._1D, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit _1D")));
            //bids.add(shows(Bid._1D, shape(4, 13), shape(Suit.Clubs, 5, 13), OpenBiddingLowThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit _1D")));
            bids.add(shows(Bid._1D, shape(4, 4), shape(Suit.Clubs, 1, 1), shape4441(), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit _1D")));

            bids.add(shows(Bid._1C, OpenBiddingLowThirtSeat, BALANCED, ruleShow(1), id("OpenWJSimple.openSuit _1C")));
            bids.add(shows(Bid._1C, shape(5, 13), OpenBiddingHight, ruleShow(1), id("OpenWJSimple.openSuit _1C")));
            bids.add(shows(Bid._1C, OpeningStrongBidding, ruleShow(1)));
        }

        bids.add(shows(Call.PASS, DontOpen, id("OpenWJSimple.openSuit _PASS")));

        //bids.add(shows(Bid._1D, id("OpenWJSimple.openSuit _1D")));// czy to konieczne ?

        bids.add(partnerBids(Bid._1S, RespondBid1WJSimple::oneSpade));
        bids.add(partnerBids(Bid._1H, RespondBid1WJSimple::oneHeart));
        bids.add(partnerBids(Bid._1D, RespondBid1WJSimple::oneDiamond));
        bids.add(partnerBids(Bid._1C, RespondBid1WJSimple::oneClub));

        return bids;
    }

    private static List<CallFeature> openSuitWeak(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(shows(Bid._3S, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJSimple.openSuitWeak _3S")));
        rules.add(shows(Bid._3H, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJSimple.openSuitWeak _3H")));
        rules.add(shows(Bid._3D, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJSimple.openSuitWeak _3D")));
        rules.add(shows(Bid._3C, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenWJSimple.openSuitWeak _3C")));
        rules.add(partnerBids(RespondBid1WJSimple::weakOpen));
        return rules;
    }
}
