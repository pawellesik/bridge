package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.BidRule;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.Constraint;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

public class RuleShow extends StaticConstraint {
    private final int flag;

    public RuleShow(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        return true;
    }

    public static int getRuleShowFlag(BidRule rule) {
        if (rule == null) return 0;
        for (Constraint constraint : rule.getConstraints()) {
            if (constraint instanceof RuleShow) {
                return ((RuleShow) constraint).getFlag();
            }
        }
        return 0;
    }

    public static boolean hasRuleShow(BidRule rule) {
        return getRuleShowFlag(rule) > 0;
    }
}
