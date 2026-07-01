package com.example.bridge.bridgit;

import java.util.*;

public class CallDetails {
    private final Call call;
    private final CallGroup group;
    private final List<CallAnnotation> annotations = new ArrayList<>();
    private CallProperties properties = null;
    private List<BidRule> rules = new ArrayList<>();

    public CallDetails(CallGroup group, Call call) {
        this.group = group;
        this.call = call;
    }

    public Call getCall() { return call; }
    public List<CallAnnotation> getAnnotations() { return annotations; }
    public CallProperties getProperties() { return properties; }
    public List<BidRule> getRules() { return rules; }

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
        if (rules.isEmpty()) {
            return getPositionState().getPublicHandSummary();
        }

        HandSummary.ShowState showState = new HandSummary.ShowState();
        boolean firstRule = true;
        for (BidRule rule : rules) {
            HandSummary hs = rule.showHand(getPositionState());
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
        return group.getPositionState();
    }
}
