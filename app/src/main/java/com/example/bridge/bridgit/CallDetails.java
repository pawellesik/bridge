package com.example.bridge.bridgit;

import java.util.*;

public class CallDetails {
    private final Call call;
    private PositionState positionState;
    private final CallGroup group;
    private final List<CallAnnotation> annotations = new ArrayList<>();
    private CallProperties properties = null;
    private List<BidRule> rules = new ArrayList<>();

    public CallDetails(CallGroup group, Call call) {
        this.group = group;
        this.call = call;
        if (group != null) {
            this.positionState = group.getPositionState();
        }
    }

    public Call getCall() { return call; }
    public List<CallAnnotation> getAnnotations() { return annotations; }
    public CallProperties getProperties() { return properties; }
    public List<BidRule> getRules() { return rules; }

    public void setPositionState(PositionState ps) {
        this.positionState = ps;
    }

    public void add(CallFeature feature) {
        if (feature instanceof BidRule) {
            rules.add((BidRule) feature);
        } else if (feature instanceof CallProperties) {
            this.properties = (CallProperties) feature;
        } else if (feature instanceof CallAnnotation) {
            annotations.add((CallAnnotation) feature);
        }
    }

    public HandSummary showHand() {
        PositionState ps = getPositionState();
        if (rules.isEmpty()) {
            return ps != null ? ps.getPublicHandSummary() : new HandSummary();
        }

        HandSummary.ShowState showState = new HandSummary.ShowState();
        boolean firstRule = true;
        for (BidRule rule : rules) {
            HandSummary hs = rule.showHand(ps);
            showState.combine(hs, firstRule ? State.CombineRule.Show : State.CombineRule.CommonOnly);
            firstRule = false;
        }
        return showState.handSummary;
    }

    public boolean pruneRules(PositionState ps) {
        List<BidRule> newRules = new ArrayList<>();
        for (BidRule rule : rules) {
            if (rule.satisfiesHandConstraints(ps, ps.getPublicHandSummary())) {
                newRules.add(rule);
            }
        }
        if (newRules.size() == rules.size()) return false;
        rules = newRules;
        return true;
    }

    public PositionState getPositionState() {
        if (positionState != null) return positionState;
        if (group != null) return group.getPositionState();
        return null;
    }

    public CallGroup getGroup() {
        return group;
    }
}
