package com.example.bridge.bridgit;

import java.util.*;

public class CallDetails {
    private final Call call;
    private final PositionState ps;
    private final PositionCalls group;
    private final List<BidRule> rules = new ArrayList<>();
    private final List<CallAnnotation> annotations = new ArrayList<>();
    private CallProperties properties = null;

    public CallDetails(Call call, PositionState ps, PositionCalls group) {
        this.call = call;
        this.ps = ps;
        this.group = group;
    }

    public Call getCall() { return call; }
    public PositionState getPositionState() { return ps; }
    public PositionCalls getGroup() { return group; }
    public List<BidRule> getRules() { return rules; }
    public CallProperties getProperties() { return properties; }
    public void setProperties(CallProperties properties) { this.properties = properties; }

    public void addRule(BidRule rule) {
        rules.add(rule);
    }

    public void addAnnotation(CallAnnotation annotation) {
        annotations.add(annotation);
    }

    public boolean conformsToPrivateHand() {
        if (rules.isEmpty()) return true;
        for (BidRule rule : rules) {
            if (ps.privateHandConforms(rule)) return true;
        }
        return false;
    }

    public java.util.function.Function<PositionState, PositionCalls> getBidsFactory() {
        return properties != null ? properties.getPartnerBidsFactory() : null;
    }

    public HandSummary.ShowState showHand() {
        HandSummary.ShowState showHand = new HandSummary.ShowState();
        for (BidRule rule : rules) {
            rule.showHand(ps, showHand);
        }
        return showHand;
    }

    public boolean pruneRules(PositionState ps) {
        // C# implementation logic for pruning rules based on public information
        return false;
    }
}
