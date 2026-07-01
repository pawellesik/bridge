package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class PassIn4thSeat extends Constraint.HandConstraint {
    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        if (ps.getSeat() != 4) return false;
        if (hs.highCardPoints == null) return true;

        Range hcp = hs.highCardPoints;
        Range spadeShape = hs.suits.get(Suit.Spades).getShape();

        return (hcp.max + spadeShape.max < 15);
    }
}
