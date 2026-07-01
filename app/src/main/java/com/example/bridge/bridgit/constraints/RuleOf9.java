package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class RuleOf9 extends Constraint.HandConstraint implements Constraint.IDescribeConstraint {
    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        ContractState contract = ps.getBiddingState().getContract();
        if (contract.isOpponents(ps.getDirection()) && contract.bid != null) {
            Suit oppsSuit = contract.bid.getSuit();
            if (oppsSuit != null) {
                int level = contract.bid.getLevel();
                Integer ruleOf9Points = hs.suits.get(oppsSuit).ruleOf9Points;
                if (ruleOf9Points != null) {
                    return (level + ruleOf9Points >= 9);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String describe(Call call, PositionState ps) {
        return "rule of 9";
    }
}
