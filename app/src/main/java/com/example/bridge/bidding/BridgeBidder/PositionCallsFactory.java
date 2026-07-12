package com.example.bridge.bidding.BridgeBidder;

@FunctionalInterface
public interface PositionCallsFactory {
    PositionCalls apply(PositionState ps);
}
