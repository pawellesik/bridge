package com.example.bridge.bidding.Tools;

import java.util.*;

public class PositionCalls extends LinkedHashMap<Call, CallDetails> {
    private final PositionState positionState;
    private CallDetails bestCall = null;

    public PositionCalls(PositionState ps) {
        this.positionState = ps;
    }

    public static PositionCallsFactory fromCallFeaturesFactory(CallFeaturesFactory callFeatures) {
        return (ps) -> {
            if (ps.getRHO().isPassed() || ps.getRHO().isDoubled()) {
                PositionCalls calls = new PositionCalls(ps);
                calls.addRules(callFeatures.apply(ps));
                return calls;
            }
            return ps.getPairState().getBiddingSystem().getPositionCalls(ps);
        };
    }

    public PositionState getPositionState() {
        return positionState;
    }

    public CallDetails getBestCall() {
        return bestCall;
    }

    public void addRules(CallFeaturesFactory factory) {
        addRules(factory.apply(positionState));
    }

    public void addRules(Iterable<CallFeature> rules) {
        CallGroup group = CallGroup.create(this, rules);
        for (Entry<Call, CallDetails> entry : group.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
        if (bestCall == null) {
            bestCall = group.getBestCall();
        }
    }

    public void addRules(Object... rules) {
        List<CallFeature> list = new ArrayList<>();
        for (Object r : rules) {
            if (r instanceof CallFeature) {
                list.add((CallFeature) r);
            } else if (r instanceof CallFeature[]) {
                list.addAll(Arrays.asList((CallFeature[]) r));
            } else if (r instanceof Iterable) {
                for (Object item : (Iterable<?>) r) {
                    if (item instanceof CallFeature) {
                        list.add((CallFeature) item);
                    }
                }
            } else if (r instanceof CallFeaturesFactory) {
                for (CallFeature item : ((CallFeaturesFactory) r).apply(positionState)) {
                    list.add(item);
                }
            } else if (r instanceof PositionCallsFactory) {
                // If it's a PositionCallsFactory, we can't easily extract CallFeatures without a PositionState
            }
        }
        addRules(list);
    }

    public PositionCalls addPassRule(Constraint... constraints) {
        addRules(Bidder.shows(Call.PASS, constraints));
        return this;
    }

    public void createPlaceholderCall(Call call) {
        addRules(Bidder.shows(call));
    }

    public void logBidRule(BidRule rule) {
        // TODO: Implement logging
    }
}




























































































































































































































































































































































































































































































