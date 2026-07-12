package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.licytacja.moje.BridgeBidder.*;
import com.example.licytacja.moje.BridgeBidder.Conventions.Gerber;
import com.example.licytacja.moje.BridgeBidder.Conventions.TransferBidder;
import java.util.ArrayList;
import java.util.List;

public class ThreeNoTrump extends Bidder {
    public final Constraint openPoints = and(highCardPoints(25, 27), points(25, 28));
    public final Constraint respondNoSlam = points(0, 5);

    public static final ThreeNoTrump OPEN = new ThreeNoTrump();
    public static final ThreeNoTrump AFTER_2C_OPEN = new ThreeNoTrump();

    public Iterable<CallFeature> bids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(Bid._3NT, this::respond));
        bids.add(shows(Bid._3NT, openPoints, BALANCED));
        return bids;
    }

    private PositionCalls respond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Gerber::initiateConvention);
        choices.addRules(new TransferBidder.Transfer3NT(this)::initiateConvention);
        choices.addRules(new Natural3NT(this)::response);
        return choices;
    }

    public static class Natural3NT extends Bidder {
        private final ThreeNoTrump ntb;
        public Natural3NT(ThreeNoTrump ntb) {
            this.ntb = ntb;
        }

        public Iterable<CallFeature> response(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            bids.add(shows(Bid._4H, ntb.respondNoSlam, shape(5, 11)));
            bids.add(shows(Bid._4S, ntb.respondNoSlam, shape(5, 11)));
            bids.add(shows(Call.PASS, ntb.respondNoSlam));
            return bids;
        }
    }
}
