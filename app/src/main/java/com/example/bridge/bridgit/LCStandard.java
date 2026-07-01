package com.example.bridge.bridgit;

public class LCStandard implements BiddingSystem {
    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        return new PositionCalls(ps);
    }
}
