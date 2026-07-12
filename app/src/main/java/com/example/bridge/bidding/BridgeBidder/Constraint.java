package com.example.bridge.bidding.BridgeBidder;

public abstract class Constraint {
    public static Suit getSuit(Suit s, Call call) {
        if (s != null) return s;
        if (call instanceof Bid) {
            return ((Bid) call).getSuit();
        }
        return null;
    }

    public static Strain getStrain(Strain strain, Call call) {
        if (strain != null) return strain;
        if (call instanceof Bid) {
            return ((Bid) call).getStrain();
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
}
