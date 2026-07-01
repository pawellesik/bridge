package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;
import java.util.*;

public class ConstraintGroup extends Constraint.StaticConstraint {
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
            if (constraint instanceof Constraint.StaticConstraint) {
                if (!((Constraint.StaticConstraint) constraint).conforms(call, ps)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
