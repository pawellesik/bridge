package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;

public class BidHistory extends Constraint.StaticConstraint {
    private final int bidIndex;
    private final Call call;

    public BidHistory(int bidIndex, Call call) {
        this.bidIndex = bidIndex;
        this.call = call;
    }

    @Override
    public boolean conforms(Call currentCall, PositionState ps) {
        Call previousCall = ps.getBidHistory(bidIndex);
        if (previousCall != null) {
            if (this.call != null) {
                return previousCall.equals(this.call);
            }
            if (currentCall instanceof Call.Bid && previousCall instanceof Call.Bid) {
                return ((Call.Bid) currentCall).getSuit() == ((Call.Bid) previousCall).getSuit();
            }
        }
        return false;
    }

    @Override
    public String getLogDescription(Call currentCall, PositionState ps) {
        if (this.call != null && bidIndex == 0) return "last call was " + this.call;
        return super.getLogDescription(currentCall, ps);
    }
}
