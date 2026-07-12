package com.example.bridge.bidding.BridgeBidder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CallGroup extends LinkedHashMap<Call, CallDetails> {
    private final PositionCalls positionCalls;
    private final List<CallAnnotation> annotations;
    private CallProperties partnerCalls = null;
    private CallDetails bestCall = null;

    public CallGroup(PositionCalls positionCalls) {
        this.positionCalls = positionCalls;
        this.annotations = new ArrayList<>();
    }

    public PositionState getPositionState() {
        return positionCalls.getPositionState();
    }

    public CallDetails getBestCall() {
        return bestCall;
    }

    public void setBestCall(CallDetails bestCall) {
        this.bestCall = bestCall;
    }

    public static CallGroup create(PositionCalls positionCalls, Iterable<CallFeature> rules) {
        CallGroup group = new CallGroup(positionCalls);
        group.addRules(rules);
        return group;
    }

    private void addRules(Iterable<CallFeature> features) {
        recurseAddRules(features);
        List<Call> calls = new ArrayList<>(this.keySet());
        for (Call call : calls) {
            CallDetails details = this.get(call);
            if (!details.hasRules()) {
                this.remove(call);
            } else {
                details.getAnnotations().addAll(this.annotations);
            }
        }
    }

    public CallProperties getPartnerCalls() {
        return partnerCalls;
    }

    private void recurseAddRules(Iterable<CallFeature> features) {
        for (CallFeature feature : features) {
            if (feature instanceof CallFeatureGroup) {
                recurseAddRules(((CallFeatureGroup) feature).getFeatures());
            } else if (feature.getCall() == null) {
                if (feature.satisfiesStaticConstraints(getPositionState())) {
                    if (feature instanceof CallProperties) {
                        this.partnerCalls = (CallProperties) feature;
                    } else if (feature instanceof CallAnnotation) {
                        annotations.add((CallAnnotation) feature);
                    }
                }
            } else {
                if (feature instanceof BidRule) {
                    positionCalls.logBidRule((BidRule) feature);
                }
                if (getPositionState().isValidNextCall(feature.getCall()) && !positionCalls.containsKey(feature.getCall())) {
                    if (feature.satisfiesStaticConstraints(getPositionState())) {
                        CallDetails details = this.get(feature.getCall());
                        if (details == null) {
                            details = new CallDetails(this, feature.getCall());
                            this.put(feature.getCall(), details);
                        }
                        details.add(feature);
                        if (bestCall == null && feature instanceof BidRule && getPositionState().privateHandConforms((BidRule) feature)) {
                            bestCall = details;
                        }
                    }
                }
            }
        }
    }

}
