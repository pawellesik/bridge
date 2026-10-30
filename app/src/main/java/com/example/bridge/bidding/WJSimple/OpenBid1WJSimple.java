package com.example.bridge.bidding.WJSimple;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;

import java.util.ArrayList;
import java.util.List;

public class OpenBid1WJSimple extends WJSimple {

    public static final HandConstraint OpenBidding = highCardPoints(12, 17);
    public static final HandConstraint OpeningInviteBidding = highCardPoints(14, 17);
    public static final HandConstraint OpeningLowBidding = highCardPoints(12, 13);
    public static final HandConstraint OpeningWeakBidding = highCardPoints(7, 11);
    public static final HandConstraint OpeningStrongBidding = highCardPoints(18, 40);
    public static final HandConstraint OpenBiddingThirtSeat = highCardPoints(11, 17);
    public static final HandConstraint DontOpen = highCardPoints(0, 11);
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

        bids.add(shows(Bid._1C, OpeningStrongBidding, ruleShow(1)));

        if (ps.getSeat() < 3) {
            bids.add(shows(Bid._2S, shape(6, 13), OpenBidding, id("OpenWJSimple.openSuit _1S")));
            bids.add(shows(Bid._2H, shape(6, 13), OpenBidding, id("OpenWJSimple.openSuit _1H")));
            bids.add(shows(Bid._2S, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1S")));
            bids.add(shows(Bid._2H, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1H")));
            bids.add(shows(Bid._2D, shape(5, 13), OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1D")));
            bids.add(shows(Bid._2C, OpenBidding, ruleShow(1), id("OpenWJSimple.openSuit _1C")));
        } else if (ps.getSeat() >= 3) {
            bids.add(shows(Bid._2S, shape(6, 13), OpenBiddingThirtSeat, id("OpenWJSimple.openSuit _1S")));
            bids.add(shows(Bid._2H, shape(6, 13), OpenBiddingThirtSeat, id("OpenWJSimple.openSuit _1H")));
            bids.add(shows(Bid._2S, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit OpenAfterPass _1S")));
            bids.add(shows(Bid._2H, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit OpenAfterPass _1H")));
            bids.add(shows(Bid._2D, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit OpenAfterPass _1D")));
            bids.add(shows(Bid._2C, OpenBiddingThirtSeat, ruleShow(1), id("OpenWJSimple.openSuit OpenAfterPass _1C")));
        }
        bids.add(shows(Call.PASS, DontOpen, id("OpenWJSimple.openSuit _PASS")));

        bids.add(partnerBids(Bid._2S, RespondBid1WJSimple::oneSpade));
        bids.add(partnerBids(Bid._2H, RespondBid1WJSimple::oneHeart));
        bids.add(partnerBids(Bid._2D, RespondBid1WJSimple::oneDiamond));
        bids.add(partnerBids(Bid._2C, RespondBid1WJSimple::oneClub));

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
