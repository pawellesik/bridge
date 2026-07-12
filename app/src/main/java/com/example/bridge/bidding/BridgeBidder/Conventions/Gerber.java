package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.licytacja.moje.BridgeBidder.*;
import com.example.licytacja.moje.BridgeBidder.LCStandard.UserText;
import java.util.ArrayList;
import java.util.List;

public class Gerber extends Bidder {
    private static final Range SLAM_OR_BETTER = new Range(31, 100);
    private static final Range GRAND_SLAM = new Range(36, 100);

    public static final StaticConstraint APPLIES = new SimpleStaticConstraint((call, ps) -> {
        Call partnerLast = ps.getPartner().getBidHistory(0);
        if (partnerLast instanceof Bid) {
            Bid bid = (Bid) partnerLast;
            return bid.getStrain() == Strain.NoTrump && bid.getLevel() < 3;
        }
        return false;
    }, "partner's last bid was 1NT or 2NT");

    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(properties(Bid._4C, Gerber::respondAces, true, UserText.Gerber, APPLIES));
        bids.add(shows(Bid._4C, APPLIES, pairPoints(SLAM_OR_BETTER)));
        return bids;
    }

    public static PositionCalls respondAces(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            properties(new Call[]{Bid._4D, Bid._4H, Bid._4S, Bid._4NT}, Gerber::placeContract, true),
            shows(Bid._4D, aces(0, 4)),
            shows(Bid._4H, aces(1)),
            shows(Bid._4S, aces(2)),
            shows(Bid._4NT, aces(3))
        );
        return choices;
    }

    public static PositionCalls placeContract(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Bid._7NT, pairPoints(GRAND_SLAM), pairAces(4)),
            shows(Bid._6NT, pairAces(3, 4)),
            shows(Bid._4NT, pairAces(0, 1, 2))
        );
        return choices;
    }
}
