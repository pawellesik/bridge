package com.example.bridge.bridgit;

import java.util.*;

public class PositionCalls extends HashMap<Call, CallDetails> {
    private final PositionState ps;
    private CallDetails bestCall = null;

    public PositionCalls(PositionState ps) {
        this.ps = ps;
    }

    public CallDetails getBestCall() {
        return bestCall;
    }

    public void addRules(Iterable<CallFeature> rules) {
        for (CallFeature rule : rules) {
            addRule(rule);
        }
    }

    public void addRules(java.util.function.Function<PositionState, Iterable<CallFeature>> factory) {
        addRules(factory.apply(ps));
    }

    public void addRule(CallFeature feature) {
        if (feature instanceof CallFeatureGroup) {
            for (CallFeature child : ((CallFeatureGroup) feature).getFeatures()) {
                addRule(child);
            }
        } else if (feature instanceof BidRule) {
            BidRule rule = (BidRule) feature;
            if (rule.satisfiesStaticConstraints(ps)) {
                Call call = rule.getCall();
                CallDetails details = get(call);
                if (details == null) {
                    details = new CallDetails(call, ps, this);
                    put(call, details);
                }
                details.addRule(rule);
            }
        } else if (feature instanceof CallAnnotation) {
            CallAnnotation annotation = (CallAnnotation) feature;
            if (annotation.satisfiesStaticConstraints(ps)) {
                Call call = annotation.getCall();
                if (call == null) {
                    for (CallDetails details : values()) {
                        details.addAnnotation(annotation);
                    }
                } else {
                    CallDetails details = get(call);
                    if (details != null) {
                        details.addAnnotation(annotation);
                    }
                }
            }
        } else if (feature instanceof CallProperties) {
            CallProperties props = (CallProperties) feature;
            if (props.satisfiesStaticConstraints(ps)) {
                Call call = props.getCall();
                if (call == null) {
                    for (CallDetails details : values()) {
                        details.setProperties(props);
                    }
                } else {
                    CallDetails details = get(call);
                    if (details != null) {
                        details.setProperties(props);
                    }
                }
            }
        }
    }

    public void addPassRule() {
        addPassRule(null);
    }

    public void addPassRule(Constraint constraint) {
        addRule(new BidRule(Call.Pass, constraint));
    }

    public void createPlaceholderCall(Call call) {
        if (!containsKey(call)) {
            put(call, new CallDetails(call, ps, this));
        }
    }

    public static PositionCalls fromCallFeaturesFactory(java.util.function.Function<PositionState, Iterable<CallFeature>> factory) {
        return new PositionCalls(null) {
            @Override
            public void addRules(Iterable<CallFeature> rules) {
                super.addRules(rules);
            }
        };
    }

    public void selectBestCall() {
        // Simple implementation: first one that conforms to private hand
        for (CallDetails details : values()) {
            if (details.conformsToPrivateHand()) {
                this.bestCall = details;
                return;
            }
        }
    }
}
