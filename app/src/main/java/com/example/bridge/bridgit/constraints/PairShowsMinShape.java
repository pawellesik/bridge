package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class PairShowsMinShape extends Constraint.HandConstraint implements Constraint.IShowsHand, Constraint.IDescribeConstraint {
    protected final Suit suit;
    protected final int min;
    protected final boolean desiredValue;
    protected final boolean useContractSuit;

    public PairShowsMinShape(Suit suit, int min, boolean desiredValue) {
        this.suit = suit;
        this.min = min;
        this.desiredValue = desiredValue;
        this.useContractSuit = false;
    }

    public PairShowsMinShape(int min, boolean desiredValue) {
        this.suit = null;
        this.min = min;
        this.desiredValue = desiredValue;
        this.useContractSuit = true;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Suit s = null;
        if (useContractSuit) {
            ContractState contract = ps.getBiddingState().getContract();
            if (contract.isOurs(ps.getDirection()) && contract.bid != null) {
                s = contract.bid.getSuit();
            }
            if (s == null) return false;
        } else {
            s = Constraint.getSuit(suit, call);
        }

        if (s != null) {
            Range shape = hs.suits.get(s).getShape();
            Range partnerShape = ps.partner().getPublicHandSummary().suits.get(s).getShape();
            return (shape.max + partnerShape.min >= min) ? desiredValue : !desiredValue;
        }
        return false;
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            Range shape = ps.getPublicHandSummary().suits.get(s).getShape();
            Range partnerShape = ps.partner().getPublicHandSummary().suits.get(s).getShape();
            int newMin = min - partnerShape.min;
            if (newMin > shape.min) {
                showHand.suits.get(s).showShape(newMin, Math.max(newMin, shape.max));
            }
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        Suit s = Constraint.getSuit(suit, call);
        if (s != null) {
            return min + "+ pair " + s.toSymbol();
        }
        return null;
    }
}
