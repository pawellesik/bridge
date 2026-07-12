package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.licytacja.moje.BridgeBidder.*;
import com.example.licytacja.moje.BridgeBidder.LCStandard.UserText;
import java.util.ArrayList;
import java.util.List;

public class Jacoby2NT extends Bidder {
    private static final Range RESPOND_POINTS = new Range(13, 40);
    private static final Range OPENER_POINTS_MIN = new Range(12, 13);
    private static final Range OPENER_POINTS_MEDIUM = new Range(14, 15);
    private static final Range OPENER_POINTS_MAX = new Range(16, 40);

    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        if (!ps.isPassedHand() && ps.getRHO().isPassed()) {
            Call lastPartnerCall = ps.getPartner().getLastCall();
            if (lastPartnerCall instanceof Bid) {
                Suit suit = ((Bid) lastPartnerCall).getSuit();
                if (suit != null && suit.isMajor()) {
                    List<CallFeature> bids = new ArrayList<>();
                    bids.add(properties(Bid._2NT, Jacoby2NT::openerRebid, false, true, true, suit, UserText.Jacoby2NTDescription, null, UserText.Jacoby2NT, null));
                    bids.add(shows(Bid._2NT, fit(suit), shape(suit, 4, 10), dummyPoints(suit, RESPOND_POINTS)));
                    return bids;
                }
            }
        }
        return new ArrayList<>();
    }

    public static PositionCalls openerRebid(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                partnerBids(Jacoby2NT::placeContract),
                properties(Bid._3C, UserText.ShowsVoidOrSingleton),
                properties(Bid._3D, UserText.ShowsVoidOrSingleton),
                properties(Bid._3H, UserText.ShowsVoidOrSingleton, isOpeningBid(Bid._1S)),
                properties(Bid._3S, UserText.ShowsVoidOrSingleton, isOpeningBid(Bid._1H)),
                shows(Bid._3C, shape(0, 1)),
                shows(Bid._3D, shape(0, 1)),
                shows(Bid._3H, isOpeningBid(Bid._1S), shape(0, 1)),
                shows(Bid._3S, isOpeningBid(Bid._1H), shape(0, 1)),
                shows(Bid._3H, isOpeningBid(Bid._1H), points(OPENER_POINTS_MAX)),
                shows(Bid._3S, isOpeningBid(Bid._1S), points(OPENER_POINTS_MAX)),
                shows(Bid._3NT, points(OPENER_POINTS_MEDIUM)),
                shows(Bid._4H, isOpeningBid(Bid._1H), points(OPENER_POINTS_MIN)),
                shows(Bid._4S, isOpeningBid(Bid._1S), points(OPENER_POINTS_MIN))
        );
        return choices;
    }

    public static PositionCalls placeContract(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Blackwood.initiateConvention(ps));
        choices.addRules(
                shows(Bid._4H, isOpeningBid(Bid._1H)),
                shows(Bid._4S, isOpeningBid(Bid._1S)),
                shows(Call.PASS)
        );
        return choices;
    }
}
