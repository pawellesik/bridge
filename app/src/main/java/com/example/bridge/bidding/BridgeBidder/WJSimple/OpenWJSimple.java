package com.example.bridge.bidding.BridgeBidder.WJSimple;

import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.CallFeature;
import com.example.bridge.bidding.BridgeBidder.Tools.HandConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCalls;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;

import java.util.ArrayList;
import java.util.List;

public class OpenWJSimple extends WJSimple {

    public static final HandConstraint OpenBidding = highCardPoints(12, 17);
    public static final HandConstraint OpeningWeakBidding = highCardPoints(7, 11);
    public static final HandConstraint OpeningStrongBidding = highCardPoints(18, 40);
    public static final Range OpeningStrongBiddingRange = new Range (18, 40);
    public static final HandConstraint OpenAfterPass = highCardPoints(11, 11);
    public static final HandConstraint DontOpen = highCardPoints(0, 9);
    public static final Range Rebid1NT = new Range(12, 15);
    public static final Range Rebid2NT = new Range(18, 20);

    public static PositionCalls getOpenPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(SolidSuitWJSimple.BIDS(ps));
        choices.addRules(NoTrumpWJSimple.OneNoTrumpBidderWJSimple.open(ps));
        choices.addRules(openSuitWeak(ps));
        choices.addRules(openSuit(ps));
        choices.addRules(CompeteWJSimple::compBids);
        choices.addPassRule(DontOpen);

        return choices;
    }

    public static Iterable<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(Bid._1C, RespondWJSimple::oneClub));
        bids.add(partnerBids(Bid._1D, RespondWJSimple::oneDiamond));
        bids.add(partnerBids(Bid._1H, RespondWJSimple::oneHeart));
        bids.add(partnerBids(Bid._1S, RespondWJSimple::oneSpade));

        bids.add(shows(Bid._1C, OpeningStrongBidding));

        bids.add(shows(Bid._1S, OpenBidding, shape(5, 8), id("OpenWJSimple.openSuit _1S")));
        bids.add(shows(Bid._1H, OpenBidding, shape(5, 8), id("OpenWJSimple.openSuit _1H")));
        bids.add(shows(Bid._1D, OpenBidding, shape(5, 10), id("OpenWJSimple.openSuit _1D")));
        bids.add(shows(Bid._1C, OpenBidding, id("OpenWJSimple.openSuit _1C")));

        if (ps.isPassedHand()) {
            bids.add(shows(Bid._1S, OpenAfterPass, shape(5, 8), id("OpenWJSimple.openSuit _1S")));
            bids.add(shows(Bid._1H, OpenAfterPass, shape(5, 8), id("OpenWJSimple.openSuit _1H")));
            bids.add(shows(Bid._1D, OpenAfterPass, shape(5, 10), id("OpenWJSimple.openSuit _1D")));
            bids.add(shows(Bid._1C, OpenAfterPass, id("OpenWJSimple.openSuit _1C")));
        }
        bids.add(shows(Call.PASS, isSeat(4), DontOpen, id("OpenWJSimple.openSuit _PASS")));

        return bids;
    }

    private static List<CallFeature> openSuitWeak(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(partnerBids(RespondWJSimple::weakOpen));
        rules.add(shows(Bid._3C, OpeningWeakBidding, shape(7, 11), id("OpenWJSimple.openSuitWeak _3C")));
        rules.add(shows(Bid._3D, OpeningWeakBidding, shape(7, 11), id("OpenWJSimple.openSuitWeak _3D")));
        rules.add(shows(Bid._3H, OpeningWeakBidding, shape(7, 11), id("OpenWJSimple.openSuitWeak _3H")));
        rules.add(shows(Bid._3S, OpeningWeakBidding, shape(7, 11), id("OpenWJSimple.openSuitWeak _3S")));
        return rules;
    }

}




























































































































































































































































































































































































































































































