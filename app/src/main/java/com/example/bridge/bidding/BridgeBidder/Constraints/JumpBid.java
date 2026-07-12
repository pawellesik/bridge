package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.Arrays;

/**
 * Constraint sprawdzający czy odzywka wiąże się ze skokiem.
 * Pozwala odróżnić odzywki ekonomiczne od tych ze skokiem (np. licytacja forsująca).
 */
public class JumpBid extends StaticConstraint {
    private final int[] jumpLevels; // Lista dopuszczalnych poziomów skoku (0 = bez skoku, 1 = pojedynczy skok, itd.)

    /**
     * @param jumpLevels Tablica poziomów skoku (np. 1 dla pojedynczego skoku).
     */
    public JumpBid(int... jumpLevels) {
        this.jumpLevels = jumpLevels;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        if (call instanceof Bid) {
            int jump = ps.getBiddingState().getContract().isJump((Bid) call);
            for (int level : jumpLevels) {
                if (level == jump) return true;
            }
        }
        return false;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        if (jumpLevels.length > 0 && jumpLevels[0] == 0) {
            return "not a jump bid";
        }
        StringBuilder sb = new StringBuilder("jump ");
        for (int i = 0; i < jumpLevels.length; i++) {
            sb.append(jumpLevels[i]);
            if (i < jumpLevels.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
