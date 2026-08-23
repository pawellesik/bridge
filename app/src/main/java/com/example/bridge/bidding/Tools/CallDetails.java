package com.example.bridge.bidding.Tools;

import com.example.bridge.bidding.Constraints.LogID;
import java.util.ArrayList;
import java.util.List;

public class CallDetails {
    private final Call call;
    private final List<CallAnnotation> annotations = new ArrayList<>();
    private final List<BidRule> rules = new ArrayList<>();
    private final List<BidRule> matchedRules = new ArrayList<>();
    private CallProperties properties = null;
    private final CallGroup group;
    private int jumpLevel = 0;

    public CallDetails(CallGroup group, Call call) {
        this.group = group;
        this.call = call;
    }

    public void setJumpLevel(int jumpLevel) {
        this.jumpLevel = jumpLevel;
    }

    public int getJumpLevel() {
        return jumpLevel;
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

    public void addMatchedRule(BidRule rule) {
        if (!matchedRules.contains(rule)) {
            matchedRules.add(rule);
        }
    }

    public List<BidRule> getMatchedRules() {
        return matchedRules;
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
        
        // Also prune matched rules if they no longer satisfy public knowledge (rare but safe)
        matchedRules.removeIf(rule -> !rules.contains(rule));
        
        return true;
    }

    public String getDescription(PositionState ps) {
        List<String> ruleDescriptions = new ArrayList<>();
        // Zawsze pokazujemy opisy wszystkich możliwych reguł dla danej odzywki.
        // Dzięki temu uzasadnienie jest spójne z tym, co trafia do Wiedzy Publicznej.
        for (BidRule rule : rules) {
            List<String> ruleDescs = rule.constraintDescriptions(ps);
            if (ruleDescs != null) {
                String desc = String.join(", ", ruleDescs);
                if (!ruleDescriptions.contains(desc)) {
                    ruleDescriptions.add(desc);
                }
            }
        }
        return String.join("\n", ruleDescriptions);
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
        PositionState ps = getPositionState();
        if (!hasRules()) return ps.getPublicHandSummary();

        HandSummary.ShowState showHand = new HandSummary.ShowState();
        boolean firstRule = true;
        for (BidRule rule : rules) {
            // Decydujemy czy wykonać akcję uzgodnienia atutu:
            // 1. Jeśli znamy rękę (Player context), wykonujemy tylko dla reguł, które faktycznie pasują.
            // 2. Jeśli nie znamy ręki (Observer context), wykonujemy tylko gdy reguła jest jedyną możliwością.
            boolean includeTrump = matchedRules.contains(rule) || (matchedRules.isEmpty() && rules.size() == 1);

            HandSummary hs = rule.showHand(ps, includeTrump);
            showHand.combine(hs, firstRule ? State.CombineRule.Show : State.CombineRule.CommonOnly);
            firstRule = false;
        }

        return showHand.getHandSummary();
    }

}
