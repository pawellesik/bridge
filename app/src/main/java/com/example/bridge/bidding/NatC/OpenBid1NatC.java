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
        bids.add(partnerBids(Bid._1C, RespondBid1NatC::oneClub));
        bids.add(partnerBids(Bid._1D, RespondBid1NatC::oneDiamond));
        bids.add(partnerBids(Bid._1H, RespondBid1NatC::oneHeart));
        bids.add(partnerBids(Bid._1S, RespondBid1NatC::oneSpade));

        bids.add(shows(Bid._1C, OpeningStrongBidding));


        bids.add(shows(Bid._1S, OpenBidding, shape(6, 10), id("OpenNatC.openSuit _1S")));
        bids.add(shows(Bid._1H, OpenBidding, shape(6, 10), id("OpenNatC.openSuit _1H")));
        bids.add(shows(Bid._1S, OpenBidding, shape(5, 10), id("OpenNatC.openSuit _1S")));
        bids.add(shows(Bid._1H, OpenBidding, shape(5, 10), id("OpenNatC.openSuit _1H")));
        bids.add(shows(Bid._1D, OpenBidding, shape(5, 10), id("OpenNatC.openSuit _1D")));
        bids.add(shows(Bid._1C, OpenBidding, id("OpenNatC.openSuit _1C")));

        if (ps.getSeat() >= 3) {
            bids.add(shows(Bid._1S, OpenBiddingThirtSeat, shape(6, 10), id("OpenNatC.openSuit _1S")));
            bids.add(shows(Bid._1H, OpenBiddingThirtSeat, shape(6, 10), id("OpenNatC.openSuit _1H")));
            bids.add(shows(Bid._1S, OpenBiddingThirtSeat, shape(5, 10), id("OpenNatC.openSuit OpenAfterPass _1S")));
            bids.add(shows(Bid._1H, OpenBiddingThirtSeat, shape(5, 10), id("OpenNatC.openSuit OpenAfterPass _1H")));
            bids.add(shows(Bid._1D, OpenBiddingThirtSeat, shape(5, 10), id("OpenNatC.openSuit OpenAfterPass _1D")));
            bids.add(shows(Bid._1C, OpenBiddingThirtSeat, id("OpenNatC.openSuit OpenAfterPass _1C")));
        }
        bids.add(shows(Call.PASS, DontOpen, id("OpenNatC.openSuit _PASS")));

        return bids;
    }

    private static List<CallFeature> openSuitWeak(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(partnerBids(RespondBid1NatC::weakOpen));
        rules.add(shows(Bid._3C, OpeningWeakBidding, shape(7, 11), id("OpenNatC.openSuitWeak _3C")));
        rules.add(shows(Bid._3D, OpeningWeakBidding, shape(7, 11), id("OpenNatC.openSuitWeak _3D")));
        rules.add(shows(Bid._3H, OpeningWeakBidding, shape(7, 11), id("OpenNatC.openSuitWeak _3H")));
        rules.add(shows(Bid._3S, OpeningWeakBidding, shape(7, 11), id("OpenNatC.openSuitWeak _3S")));
        return rules;
    }

}
