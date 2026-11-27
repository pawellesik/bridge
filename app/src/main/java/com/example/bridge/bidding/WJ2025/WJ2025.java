package com.example.bridge.bidding.WJ2025;

import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.IBiddingSystem;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionRole;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;

import java.util.ArrayList;
import java.util.List;

public class WJ2025 extends Bidder implements IBiddingSystem {

    public static Range openRange17(PositionState ps) {
        int min = (ps != null && ps.getSeat() >= 3) ? 11 : 12;
        return new Range(min, 17);
    }

    public static Range openRange14(PositionState ps) {
        int min = (ps != null && ps.getSeat() >= 3) ? 11 : 12;
        return new Range(min, 14);
    }

    public static Range openRange13(PositionState ps) {
        int min = (ps != null && ps.getSeat() >= 3) ? 11 : 12;
        return new Range(min, 13);
    }

    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices;
        if (ps.getRole() == PositionRole.Opener && ps.getRoleRound() == 1) {
            choices = OpenWJ2025.getOpenPositionCalls(ps);
        } else {
            choices = new PositionCalls(ps);
            //choices.addRules(CompeteWJ2025::compBids);
        }

        return choices;
    }


}
