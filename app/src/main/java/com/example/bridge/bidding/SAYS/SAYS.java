package com.example.bridge.bidding.SAYS;

import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.IBiddingSystem;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionRole;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;

import java.util.ArrayList;
import java.util.List;

public class SAYS extends Bidder implements IBiddingSystem {
    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices;
        if (ps.getRole() == PositionRole.Opener && ps.getRoleRound() == 1) {
            choices = OpenBid1SAYS.getOpenPositionCalls(ps);
        } else {
            choices = new PositionCalls(ps);
        }

        return choices;
    }

    public static final Range PAIR_GAME_INVITE = new Range(23, 24);
    public static final Range PAIR_GAME = new Range(25, 31);
    public static final Range PAIR_MINOR_GAME = new Range(27, 31);
    public static final Range PAIR_LOW_GAME = new Range(16, 24);

    public static Iterable<CallFeature> finishBiddingCompBids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        for (CallFeature cf : CompeteSAYS.compBids(ps)) {
            bids.add(cf);
        }
        return bids;
    }
}
