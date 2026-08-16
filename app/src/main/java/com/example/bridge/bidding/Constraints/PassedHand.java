package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

/**
 * Sprawdza czy gracz jest "pasującą ręką" (czy jego pierwszą odzywką był pas).
 */
public class PassedHand extends StaticConstraint {
    private final boolean desiredValue;

    public PassedHand(boolean desiredValue) {
        this.desiredValue = desiredValue;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        return ps.isPassedHand() == desiredValue;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        return desiredValue ? "passed hand" : "not passed hand";
    }
}
