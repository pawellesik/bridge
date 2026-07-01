package com.example.bridge.bridgit;

import java.util.*;

public abstract class CallFeature {
    private final Call call;
    private final List<Constraint> constraints = new ArrayList<>();

    protected CallFeature(Call call, Constraint... constraints) {
        this.call = call;
        if (constraints != null) {
            for (Constraint c : constraints) {
                addConstraint(c);
            }
        }
    }

    public Call getCall() {
        return call;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void addConstraint(Constraint constraint) {
        if (constraint instanceof com.example.bridge.bridgit.constraints.ConstraintGroup) {
            for (Constraint child : ((com.example.bridge.bridgit.constraints.ConstraintGroup) constraint).getChildConstraints()) {
                addConstraint(child);
            }
        } else if (constraint != null) {
            this.constraints.add(constraint);
        }
    }

    public boolean satisfiesStaticConstraints(PositionState ps) {
        for (Constraint c : constraints) {
            if (c instanceof Constraint.StaticConstraint) {
                if (!((Constraint.StaticConstraint) c).conforms(this.call, ps)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Constraint> failingStaticConstraints(PositionState ps) {
        List<Constraint> failing = new ArrayList<>();
        for (Constraint c : constraints) {
            if (c instanceof Constraint.StaticConstraint) {
                if (!((Constraint.StaticConstraint) c).conforms(this.call, ps)) {
                    failing.add(c);
                }
            }
        }
        return failing;
    }
}
