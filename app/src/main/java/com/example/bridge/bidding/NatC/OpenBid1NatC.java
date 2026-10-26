package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;

import java.util.ArrayList;
import java.util.List;

public class OpenBid1NatC extends NatC {

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

        choices.addRules(SolidSuitNatC.BIDS(ps));
        choices.addRules(NoTrumpNatC.open1NTBid1(ps));
        choices.addRules(openSuitWeak(ps));
        choices.addRules(openSuit(ps));
        //choices.addRules(CompeteNatC::compBids);

        return choices;
    }

    public static Iterable<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        bids.add(shows(Bid._1C, OpeningStrongBidding, ruleShow(1)));

        if (ps.getSeat() < 3) {
            bids.add(shows(Bid._1S, shape(6, 13), OpenBidding, id("OpenNatC.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(6, 13), OpenBidding, id("OpenNatC.openSuit _1H")));
            bids.add(shows(Bid._1S, shape(5, 13), OpenBidding, ruleShow(1), id("OpenNatC.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(5, 13), OpenBidding, ruleShow(1), id("OpenNatC.openSuit _1H")));
            bids.add(shows(Bid._1D, shape(5, 13), OpenBidding, ruleShow(1), id("OpenNatC.openSuit _1D")));
            bids.add(shows(Bid._1C, OpenBidding, ruleShow(1), id("OpenNatC.openSuit _1C")));
        } else if (ps.getSeat() >= 3) {
            bids.add(shows(Bid._1S, shape(6, 13), OpenBiddingThirtSeat, id("OpenNatC.openSuit _1S")));
            bids.add(shows(Bid._1H, shape(6, 13), OpenBiddingThirtSeat, id("OpenNatC.openSuit _1H")));
            bids.add(shows(Bid._1S, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenNatC.openSuit OpenAfterPass _1S")));
            bids.add(shows(Bid._1H, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenNatC.openSuit OpenAfterPass _1H")));
            bids.add(shows(Bid._1D, shape(5, 13), OpenBiddingThirtSeat, ruleShow(1), id("OpenNatC.openSuit OpenAfterPass _1D")));
            bids.add(shows(Bid._1C, OpenBiddingThirtSeat, ruleShow(1), id("OpenNatC.openSuit OpenAfterPass _1C")));
        }
        bids.add(shows(Call.PASS, DontOpen, id("OpenNatC.openSuit _PASS")));

        bids.add(partnerBids(Bid._1S, RespondBid1NatC::oneSpade));
        bids.add(partnerBids(Bid._1H, RespondBid1NatC::oneHeart));
        bids.add(partnerBids(Bid._1D, RespondBid1NatC::oneDiamond));
        bids.add(partnerBids(Bid._1C, RespondBid1NatC::oneClub));

        return bids;
    }

    private static List<CallFeature> openSuitWeak(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(shows(Bid._3S, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenNatC.openSuitWeak _3S")));
        rules.add(shows(Bid._3H, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenNatC.openSuitWeak _3H")));
        rules.add(shows(Bid._3D, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenNatC.openSuitWeak _3D")));
        rules.add(shows(Bid._3C, shape(7, 13), OpeningWeakBidding, ruleShow(1), id("OpenNatC.openSuitWeak _3C")));
        rules.add(partnerBids(RespondBid1NatC::weakOpen));
        return rules;
    }

}
