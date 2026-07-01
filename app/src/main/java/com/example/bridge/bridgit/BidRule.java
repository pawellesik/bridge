package com.example.bridge.bridgit;

import java.util.*;

public class BidRule extends CallFeature {

    public BidRule(Call call, Constraint... constraints) {
        super(call, constraints);
    }

    public boolean satisfiesHandConstraints(PositionState ps, HandSummary hs) {
        for (Constraint c : getConstraints()) {
            if (c instanceof Constraint.HandConstraint) {
                if (!((Constraint.HandConstraint) c).conforms(getCall(), ps, hs)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Constraint> failingHandConstraints(PositionState ps, HandSummary hs) {
        List<Constraint> failing = new ArrayList<>();
        for (Constraint c : getConstraints()) {
            if (c instanceof Constraint.HandConstraint) {
                if (!((Constraint.HandConstraint) c).conforms(getCall(), ps, hs)) {
                    failing.add(c);
                }
            }
        }
        return failing;
    }

    public HandSummary showHand(PositionState ps) {
        HandSummary.ShowState showState = new HandSummary.ShowState();
        for (Constraint c : getConstraints()) {
            if (c instanceof Constraint.IShowsHand) {
                ((Constraint.IShowsHand) c).showHand(getCall(), ps, showState);
            }
        }
        return showState.handSummary;
    }
}
