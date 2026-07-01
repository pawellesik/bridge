package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class SimpleStaticConstraint extends Constraint.StaticConstraint implements Constraint.IDescribeConstraint {
    
    public interface EvalFunc {
        boolean test(Call call, PositionState ps);
    }
    
    public interface DescFunc {
        String apply(Call call, PositionState ps);
    }

    private final EvalFunc eval;
    private final DescFunc getDescription;
    private final String logDescription;

    public SimpleStaticConstraint(EvalFunc eval, DescFunc getDescription, String logDescription) {
        this.eval = eval != null ? eval : (call, ps) -> true;
        this.getDescription = getDescription != null ? getDescription : (call, ps) -> null;
        this.logDescription = logDescription;
    }

    public SimpleStaticConstraint(EvalFunc eval, DescFunc getDescription) {
        this(eval, getDescription, null);
    }

    public SimpleStaticConstraint(EvalFunc eval, String description) {
        this(eval, (call, ps) -> description, null);
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        return eval.test(call, ps);
    }

    @Override
    public String describe(Call call, PositionState ps) {
        return getDescription.apply(call, ps);
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        if (logDescription != null) return logDescription;
        return super.getLogDescription(call, ps);
    }
}
