package com.example.bridge.bidding.BridgeBidder;

public class ConstraintGroup extends StaticConstraint {
    private final Constraint[] childConstraints;

    public ConstraintGroup(Constraint... childConstraints) {
        this.childConstraints = childConstraints;
    }

    public Constraint[] getChildConstraints() {
        return childConstraints;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        for (Constraint constraint : childConstraints) {
            if (constraint instanceof StaticConstraint) {
                if (!((StaticConstraint) constraint).conforms(call, ps)) {
                    return false;
                }
            } else {
                // Dynamic constraints are not allowed for constraint groups that are not part of a rule
                return false;
            }
        }
        return true;
    }
}
