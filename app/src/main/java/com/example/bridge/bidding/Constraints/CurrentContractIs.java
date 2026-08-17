package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

/**
 * Sprawdza, czy aktualny najwyższy kontrakt na stole jest identyczny z podanym.
 */
public class CurrentContractIs extends StaticConstraint {
    private final Bid targetBid;

    public CurrentContractIs(Bid targetBid) {
        this.targetBid = targetBid;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        Bid currentTableBid = ps.getBiddingState().getContract().getBid();
        
        if (targetBid == null) {
            return currentTableBid == null; // Sprawdza czy licytacja jeszcze się nie zaczęła
        }
        
        return targetBid.equals(currentTableBid);
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        return "current contract on table is " + (targetBid != null ? targetBid : "None");
    }
}
