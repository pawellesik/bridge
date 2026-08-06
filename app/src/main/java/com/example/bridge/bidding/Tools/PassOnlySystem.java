package com.example.bridge.bidding.Tools;

public class PassOnlySystem implements IBiddingSystem {
    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addPassRule();
        return choices;
    }
}
