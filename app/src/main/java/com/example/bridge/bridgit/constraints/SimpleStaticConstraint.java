package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;
import java.util.function.*;

public class SimpleStaticConstraint extends Constraint.StaticConstraint implements Constraint.IDescribeConstraint {
    private final BiPredicate<Call, PositionState> eval;
    private final BiFunction<Call, PositionState, String> getDescription;
    private final String logDescription;

    public SimpleStaticConstraint(BiPredicate<Call, PositionState> eval, BiFunction<Call, PositionState, String> getDescription, String logDescription) {
        this.eval = eval != null ? eval : (call, ps) -> true;
        this.getDescription = getDescription != null ? getDescription : (call, ps) -> null;
        this.logDescription = logDescription;
    }

    public SimpleStaticConstraint(BiPredicate<Call, PositionState> eval, String description) {
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
