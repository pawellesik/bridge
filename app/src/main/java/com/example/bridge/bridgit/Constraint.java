package com.example.bridge.bridgit;

import java.util.List;

public abstract class Constraint {

    public static Suit getSuit(Suit s, Call call) {
        if (s != null) return s;
        if (call instanceof Call.Bid) {
            return ((Call.Bid) call).getSuit();
        }
        return null;
    }

    public static Strain getStrain(Strain strain, Call call) {
        if (strain != null) return strain;
        if (call instanceof Call.Bid) {
            return ((Call.Bid) call).getStrain();
        }
        return null;
    }

    public String getLogDescription(Call call, PositionState ps) {
        if (this instanceof IDescribeConstraint) {
            String desc = ((IDescribeConstraint) this).describe(call, ps);
            if (desc != null && !desc.isEmpty()) return desc;
        }
        return this.getClass().getSimpleName();
    }

    public interface IDescribeConstraint {
        String describe(Call call, PositionState ps);
    }

    public interface IDescribeMultipleConstraints {
        String describe(Call call, PositionState ps, List<Constraint> constraints);
    }

    public interface IShowsHand {
        void showHand(Call call, PositionState ps, HandSummary.ShowState showHand);
    }

    public static abstract class StaticConstraint extends Constraint {
        public abstract boolean conforms(Call call, PositionState ps);
    }

    public static abstract class HandConstraint extends Constraint {
        public abstract boolean conforms(Call call, PositionState ps, HandSummary hs);
    }
}
