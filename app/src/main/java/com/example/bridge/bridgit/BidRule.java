package com.example.bridge.bridgit;

import java.util.*;
import java.util.stream.Collectors;

public class BidRule extends CallFeature {

    public BidRule(Call call, Constraint... constraints) {
        super(call, constraints);
    }

    public boolean satisfiesHandConstraints(PositionState ps, HandSummary hs) {
        for (Constraint constraint : getConstraints()) {
            if (constraint instanceof Constraint.HandConstraint &&
                !((Constraint.HandConstraint) constraint).conforms(getCall(), ps, hs)) {
                return false;
            }
        }
        return true;
    }

    public List<Constraint> failingHandConstraints(PositionState ps, HandSummary hs) {
        List<Constraint> failingConstraints = new ArrayList<>();
        for (Constraint constraint : getConstraints()) {
            if (constraint instanceof Constraint.HandConstraint &&
                !((Constraint.HandConstraint) constraint).conforms(getCall(), ps, hs)) {
                failingConstraints.add(constraint);
            }
        }
        return failingConstraints;
    }

    public HandSummary showHand(PositionState ps) {
        HandSummary.ShowState showHand = new HandSummary.ShowState();
        for (Constraint constraint : getConstraints()) {
            if (constraint instanceof Constraint.IShowsHand) {
                ((Constraint.IShowsHand) constraint).showHand(getCall(), ps, showHand);
            }
        }
        return showHand.handSummary;
    }

    public List<String> constraintDescriptions(PositionState ps) {
        Set<Class<?>> didMultiDescribe = new HashSet<>();
        List<String> descriptions = new ArrayList<>();

        List<Constraint> sortedConstraints = getConstraints().stream()
                .sorted(Comparator.comparingInt(ConstraintSort::forDescription))
                .collect(Collectors.toList());

        for (Constraint constraint : sortedConstraints) {
            if (constraint instanceof Constraint.IDescribeMultipleConstraints) {
                if (!didMultiDescribe.contains(constraint.getClass())) {
                    didMultiDescribe.add(constraint.getClass());
                    List<Constraint> sameConstraint = getConstraints().stream()
                            .filter(c -> c.getClass().equals(constraint.getClass()))
                            .collect(Collectors.toList());
                    descriptions.add(((Constraint.IDescribeMultipleConstraints) constraint).describe(getCall(), ps, sameConstraint));
                }
            } else if (constraint instanceof Constraint.IDescribeConstraint) {
                String d = ((Constraint.IDescribeConstraint) constraint).describe(getCall(), ps);
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
