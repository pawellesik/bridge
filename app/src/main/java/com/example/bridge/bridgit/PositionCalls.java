package com.example.bridge.bridgit;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PositionCalls extends HashMap<Call, CallDetails> {
    public enum LogAction { Illegal, Duplicate, Rejected, Accepted, Chosen, NotChosen }

    public static class LogEntry {
        public final PositionCalls positionCalls;
        public final BidRule bidRule;
        public LogAction action;
        public List<Constraint> failingConstraints = null;

        public LogEntry(PositionCalls positionCalls, BidRule bidRule) {
            this.positionCalls = positionCalls;
            this.bidRule = bidRule;
        }

        @Override
        public String toString() {
            String callStr = String.format("%3s", bidRule.getCall());
            switch (action) {
                case Illegal:
                case Duplicate:
                    return callStr + " " + action;
                case Chosen:
                case Accepted:
                    return callStr + " " + action + ": " + bidRule.getDescription(positionCalls.positionState);
                default:
                    String failing = (failingConstraints == null) ? "" : failingConstraints.stream()
                            .map(c -> c.getLogDescription(bidRule.getCall(), positionCalls.positionState))
                            .collect(Collectors.joining(", "));
                    return callStr + " " + action + ", not conforming: " + failing;
            }
        }
    }

    private final PositionState positionState;
    private CallDetails bestCall = null;
    private final List<LogEntry> bidRuleLog = new ArrayList<>();

    public PositionCalls(PositionState ps) {
        this.positionState = ps;
    }

    public static Function<PositionState, PositionCalls> fromCallFeaturesFactory(Function<PositionState, Iterable<CallFeature>> callFeatures) {
        return (ps) -> {
            // Simplified logic matching C#
            PositionCalls calls = new PositionCalls(ps);
            calls.addRules(callFeatures.apply(ps));
            return calls;
        };
    }

    public PositionState getPositionState() {
        return positionState;
    }

    public void addRules(Iterable<CallFeature> rules) {
        CallGroup group = CallGroup.create(this, rules);
        this.putAll(group);
        if (bestCall == null) {
            bestCall = group.getBestCall();
        }
    }

    public void addPassRule(Constraint... constraints) {
        addRules(Collections.singletonList(Bidder.shows(Call.Pass, constraints)));
    }

    public void createPlaceholderCall(Call call) {
        addRules(Collections.singletonList(Bidder.shows(call)));
    }

    public CallDetails getBestCall() {
        return bestCall;
    }

    public List<LogEntry> getBidRuleLog() {
        return bidRuleLog;
    }

    void logBidRule(BidRule rule) {
        LogEntry entry = new LogEntry(this, rule);
        if (!positionState.isValidNextCall(rule.getCall())) {
            entry.action = LogAction.Illegal;
        } else if (containsKey(rule.getCall())) {
            entry.action = LogAction.Duplicate;
        } else {
            List<Constraint> failingStatic = rule.failingStaticConstraints(positionState);
            if (!failingStatic.isEmpty()) {
                entry.action = LogAction.Rejected;
                entry.failingConstraints = failingStatic;
            } else if (positionState.hasHand()) {
                List<Constraint> failingHand = positionState.privateHandFailingConstraints(rule);
                if (!failingHand.isEmpty()) {
                    entry.action = LogAction.NotChosen;
                    entry.failingConstraints = failingHand;
                } else {
                    entry.action = LogAction.Chosen;
                }
            } else {
                entry.action = LogAction.Accepted;
            }
        }
        bidRuleLog.add(entry);
    }
}
