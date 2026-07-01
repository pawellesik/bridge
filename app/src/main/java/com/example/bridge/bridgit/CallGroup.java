package com.example.bridge.bridgit;

import java.util.*;

public class CallGroup extends HashMap<Call, CallDetails> {
    private final PositionCalls positionCalls;
    private CallProperties partnerCalls;
    private final List<CallAnnotation> annotations = new ArrayList<>();
    private CallDetails bestCall = null;

    public CallGroup(PositionCalls positionCalls) {
        this.positionCalls = positionCalls;
    }

    public static CallGroup create(PositionCalls positionCalls, Iterable<CallFeature> rules) {
        CallGroup group = new CallGroup(positionCalls);
        group.addRules(rules);
        return group;
    }

    public PositionState getPositionState() {
        return positionCalls.getPositionState();
    }

    private void addRules(Iterable<CallFeature> features) {
        recurseAddRules(features);
        List<Call> calls = new ArrayList<>(this.keySet());
        for (Call call : calls) {
            CallDetails cd = this.get(call);
            if (cd.getRules().isEmpty()) {
                this.remove(call);
            } else {
                cd.getAnnotations().addAll(this.annotations);
            }
        }
    }

    private void recurseAddRules(Iterable<CallFeature> features) {
        for (CallFeature feature : features) {
            if (feature instanceof CallFeatureGroup) {
                if (feature.satisfiesStaticConstraints(getPositionState())) {
                    recurseAddRules(((CallFeatureGroup) feature).getFeatures());
                }
            } else if (feature.getCall() == null) {
                if (feature.satisfiesStaticConstraints(getPositionState())) {
                    if (feature instanceof CallProperties) {
                        this.partnerCalls = (CallProperties) feature;
                    } else if (feature instanceof CallAnnotation) {
                        this.annotations.add((CallAnnotation) feature);
                    }
                }
            } else {
                Call call = feature.getCall();
                if (getPositionState().isValidNextCall(call) && !positionCalls.containsKey(call)) {
                    if (feature.satisfiesStaticConstraints(getPositionState())) {
                        CallDetails cd = this.computeIfAbsent(call, k -> new CallDetails(this, k));
                        cd.add(feature);

                        if (bestCall == null && feature instanceof BidRule) {
                            if (getPositionState().privateHandConforms((BidRule) feature)) {
                                bestCall = cd;
                            }
                        }
                    }
                }
            }
        }
    }

    public CallDetails getBestCall() {
        return bestCall;
    }
}
