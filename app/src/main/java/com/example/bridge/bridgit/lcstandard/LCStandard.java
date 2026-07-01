package com.example.bridge.bridgit.lcstandard;

import com.example.bridge.bridgit.Bidder;
import com.example.bridge.bridgit.BiddingSystem;
import com.example.bridge.bridgit.PositionCalls;
import com.example.bridge.bridgit.PositionState;
import com.example.bridge.bridgit.Range;

public class LCStandard extends Bidder implements BiddingSystem {

    @Override
    public PositionCalls getPositionCalls(PositionState ps) {
        if (ps.getRole() == PositionState.PositionRole.Opener && ps.getRoleRound() == 1) {
            return Open.getPositionCalls(ps);
        } else if (ps.getRole() == PositionState.PositionRole.Overcaller && ps.getRoleRound() == 1) {
            return Overcall.getPositionCalls(ps);
        } else {
            PositionCalls calls = new PositionCalls(ps);
            calls.addRules(Compete.compBids(ps));
            return calls;
        }
    }

    public static final Range pairGameInvite = new Range(23, 24);
    public static final Range pairGame = new Range(25, 31);

    // TODO: This is not a great name. Not exactly right. Fix later.....
    public static final Range lessThanOvercall = new Range(0, 17);
    public static final Range overcall1Level = new Range(7, 17);
    public static final Range overcallStrong2Level = new Range(13, 17);
    public static final Range overcallWeak2Level = new Range(7, 11);
    public static final Range overcallWeak3Level = new Range(7, 11);
}
