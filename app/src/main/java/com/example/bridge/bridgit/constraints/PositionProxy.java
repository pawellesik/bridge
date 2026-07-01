package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class PositionProxy extends Constraint.StaticConstraint implements Constraint.IDescribeConstraint {
    public enum RelativePosition { Partner, LHO, RHO }

    private final RelativePosition relativePosition;
    private final Constraint.StaticConstraint constraint;

    public PositionProxy(RelativePosition relativePosition, Constraint constraint) {
        this.relativePosition = relativePosition;
        this.constraint = (Constraint.StaticConstraint) constraint;
    }

    private PositionState getPosition(PositionState ps) {
        switch (relativePosition) {
            case Partner: return ps.partner();
            case LHO: return ps.leftHandOpponent();
            default: return ps.rightHandOpponent();
        }
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        return constraint.conforms(call, getPosition(ps));
    }

    private String getPositionName() {
        switch (relativePosition) {
            case Partner: return "partner";
            case LHO: return "LHO";
            default: return "RHO";
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        if (constraint instanceof Constraint.IDescribeConstraint) {
            return getPositionName() + " " + ((Constraint.IDescribeConstraint) constraint).describe(call, getPosition(ps));
        }
        return null;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        String desc = describe(call, ps);
        if (desc != null) return desc;
        return getPositionName() + " " + constraint.getLogDescription(call, getPosition(ps));
    }
}
