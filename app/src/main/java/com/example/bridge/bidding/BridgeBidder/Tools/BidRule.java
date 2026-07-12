package com.example.bridge.bidding.BridgeBidder.Tools;

import java.util.ArrayList;
import java.util.List;

public class BidRule extends CallFeature {
    public BidRule(Call call, Constraint... constraints) {
        super(call, constraints);
    }

    public boolean satisfiesHandConstraints(PositionState ps, HandSummary hs) {
        for (Constraint constraint : getConstraints()) {
            if (constraint instanceof HandConstraint) {
                if (!((HandConstraint) constraint).conforms(getCall(), ps, hs)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Constraint> failingHandConstraints(PositionState ps, HandSummary hs) {
        List<Constraint> failingConstraints = new ArrayList<>();
        for (Constraint constraint : getConstraints()) {
            if (constraint instanceof HandConstraint) {
                if (!((HandConstraint) constraint).conforms(getCall(), ps, hs)) {
                    failingConstraints.add(constraint);
                }
            }
        }
        return failingConstraints;
    }

    public HandSummary showHand(PositionState ps) {
        HandSummary.ShowState showHand = new HandSummary.ShowState();
        for (Constraint constraint : getConstraints()) {
            if (constraint instanceof IShowsHand) {
                ((IShowsHand) constraint).showHand(getCall(), ps, showHand);
            }
        }
        return showHand.getHandSummary();
    }

    public List<String> constraintDescriptions(PositionState ps) {
        List<String> descriptions = new ArrayList<>();
        // TODO: Port logic for IDescribeMultipleConstraints if needed
        for (Constraint constraint : getConstraints()) {
            if (constraint instanceof IDescribeConstraint) {
                String d = ((IDescribeConstraint) constraint).describe(getCall(), ps);
                if (d != null) {
                    descriptions.add(d);
                }
            }
        }
        return descriptions.isEmpty() ? null : descriptions;
    }

    public String getDescription(PositionState ps) {
        List<String> desc = constraintDescriptions(ps);
        if (desc == null) return "";
        return String.join(", ", desc);
    }
}




























































































































































































































































































































































































































































































