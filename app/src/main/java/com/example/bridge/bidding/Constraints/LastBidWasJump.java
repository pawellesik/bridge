package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

/**
 * Constraint sprawdzający czy ostatnia odzywka gracza była skokiem.
 */
public class LastBidWasJump extends StaticConstraint {
    private final int minJump;

    public LastBidWasJump(int minJump) {
        this.minJump = minJump;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        CallDetails lastDetails = ps.getLastCallDetails();
        if (lastDetails != null) {
            return lastDetails.getJumpLevel() >= minJump;
        }
        return false;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        return "last bid was jump (min " + minJump + ")";
    }
}
