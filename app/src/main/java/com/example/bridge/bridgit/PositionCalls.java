package com.example.bridge.bridgit;

import java.util.*;

public class PositionCalls extends HashMap<Call, CallDetails> {
    private final PositionState positionState;
    private CallDetails bestCall = null;

    public PositionCalls(PositionState ps) {
        this.positionState = ps;
    }

    public PositionState getPositionState() {
        return positionState;
    }

    public void addRules(Iterable<CallFeature> rules) {
        CallGroup group = CallGroup.create(this, rules);
        for (Map.Entry<Call, CallDetails> entry : group.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
        if (bestCall == null) {
            bestCall = group.getBestCall();
        }
    }

    public CallDetails getBestCall() {
        return bestCall;
    }
}
