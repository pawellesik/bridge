package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;
import java.util.*;
import java.util.stream.IntStream;

public class JumpBid extends Constraint.StaticConstraint {
    private final int[] jumpLevels;

    public JumpBid(int... jumpLevels) {
        this.jumpLevels = jumpLevels;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        if (call instanceof Call.Bid) {
            int jump = ps.getBiddingState().getContract().isJump((Call.Bid) call);
            return IntStream.of(jumpLevels).anyMatch(l -> l == jump);
        }
        return false;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        if (jumpLevels.length > 0 && jumpLevels[0] == 0) {
            return "not a jump bid";
        }
        return "jump " + Arrays.toString(jumpLevels);
    }
}
