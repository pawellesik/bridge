package com.example.bridge.bridgit;

import java.util.*;

public class LCStandard implements BiddingSystem {
    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        if (ps.getRole() == PositionState.PositionRole.Opener && ps.getRoleRound() == 1) {
            return Open.getPositionCalls(ps);
        } else if (ps.getRole() == PositionState.PositionRole.Overcaller && ps.getRoleRound() == 1) {
            return Overcall.getPositionCalls(ps);
        } else if (ps.getRole() == PositionState.PositionRole.Responder && ps.getRoleRound() == 1) {
            return Respond.getPositionCalls(ps);
        } else {
            PositionCalls calls = new PositionCalls(ps);
            calls.addRules(Compete.compBids(ps));
            return calls;
        }
    }
}
