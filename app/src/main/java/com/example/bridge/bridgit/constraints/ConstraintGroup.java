package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;
import java.util.*;

public class ConstraintGroup extends Constraint {
    private final List<Constraint> childConstraints = new ArrayList<>();

    public ConstraintGroup(Constraint... constraints) {
        if (constraints != null) {
            for (Constraint c : constraints) {
                if (c instanceof ConstraintGroup) {
                    childConstraints.addAll(((ConstraintGroup) c).childConstraints);
                } else if (c != null) {
                    childConstraints.add(c);
                }
            }
        }
    }

    public List<Constraint> getChildConstraints() {
        return childConstraints;
    }
}
