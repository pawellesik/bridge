package com.example.bridge.bidding.BridgeBidder;

import com.example.licytacja.moje.BridgeBidder.Constraints.LogID;
import java.util.ArrayList;
import java.util.List;

public class CallDetails {
    private final Call call;
    private final List<CallAnnotation> annotations = new ArrayList<>();
    private final List<BidRule> rules = new ArrayList<>();
    private CallProperties properties = null;
    private final CallGroup group;

    public CallDetails(CallGroup group, Call call) {
        this.group = group;
        this.call = call;
    }

    public Call getCall() {
        return call;
    }

    public List<CallAnnotation> getAnnotations() {
        return annotations;
    }

    public CallGroup getGroup() {
        return group;
    }

    public boolean hasRules() {
        return !rules.isEmpty();
    }

    public CallProperties getProperties() {
        return properties;
    }

    public void add(CallFeature feature) {
        if (feature instanceof BidRule) {
            rules.add((BidRule) feature);
        } else if (feature instanceof CallAnnotation) {
            annotations.add((CallAnnotation) feature);
        } else if (feature instanceof CallProperties) {
            this.properties = (CallProperties) feature;
        }
    }

    public PositionState getPositionState() {
        return group.getPositionState();
    }

    public PositionCallsFactory getBidsFactory() {
        if (properties != null && properties.getPartnerBids() != null) {
            return properties.getPartnerBids();
        }
        if (!this.call.equals(Call.PASS) && group.getPartnerCalls() != null) {
            return group.getPartnerCalls().getPartnerBids();
        }
        return null;
    }

    public boolean pruneRules(PositionState ps) {
        List<BidRule> newRules = new ArrayList<>();
        for (BidRule rule : rules) {
            if (rule.satisfiesHandConstraints(ps, ps.getPublicHandSummary())) {
                newRules.add(rule);
            }
        }
        if (newRules.size() == rules.size()) return false;
        rules.clear();
        rules.addAll(newRules);
        return true;
    }

    public String getDescription(PositionState ps) {
        List<String> descriptions = new ArrayList<>();
        for (BidRule rule : rules) {
            // SPRAWDZAMY: Czy ta konkretna zasada pasuje do mojej prywatnej ręki?
            if (ps.privateHandConforms(rule)) {
                List<String> ruleDescs = rule.constraintDescriptions(ps);
                if (ruleDescs != null) {
                    descriptions.addAll(ruleDescs);
                }
            }
        }
        // Jeśli żadna nie pasuje (np. badamy odzywki partnera), pokaż wszystkie możliwe znaczenia
        if (descriptions.isEmpty()) {
            for (BidRule rule : rules) {
                List<String> ruleDescs = rule.constraintDescriptions(ps);
                if (ruleDescs != null) {
                    descriptions.addAll(ruleDescs);
                }
            }
        }
        return String.join(", ", descriptions);
    }

    public String getMatchedLogID(PositionState ps) {
        for (BidRule rule : rules) {
            if (ps.privateHandConforms(rule)) {
                String id = LogID.getID(rule);
                if (id != null) return id;
            }
        }
        return null;
    }

    public HandSummary showHand() {
        if (!hasRules()) return getPositionState().getPublicHandSummary();
        HandSummary.ShowState showHand = new HandSummary.ShowState();
        boolean firstRule = true;
        for (BidRule rule : rules) {
            HandSummary hs = rule.showHand(getPositionState());
            showHand.combine(hs, firstRule ? State.CombineRule.Show : State.CombineRule.CommonOnly);
            firstRule = false;
        }
        return showHand.getHandSummary();
    }

}
