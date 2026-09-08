package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.BidRule;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.Constraint;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

public class RuleDescription extends StaticConstraint {
    private final String description;

    public RuleDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        return true;
    }

    public static String getRuleDescription(BidRule rule) {
        if (rule == null) return null;
        for (Constraint constraint : rule.getConstraints()) {
            if (constraint instanceof RuleDescription) {
                return ((RuleDescription) constraint).getDescription();
            }
        }
        return null;
    }
}
