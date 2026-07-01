package com.example.bridge.bridgit;

import java.util.*;

public class PositionState {
    public enum PositionRole { Opener, Overcaller, Responder, Advancer }

    private final BiddingState biddingState;
    private final Direction direction;
    private final Hand hand;
    private PairState pairState;
    private PositionRole role = PositionRole.Opener;
    private boolean roleAssigned = false;
    private int roleAssignedOffset = 0;
    private HandSummary publicHandSummary = new HandSummary();
    private HandSummary privateHandSummary;
    private final List<CallDetails> bids = new ArrayList<>();

    public PositionState(BiddingState biddingState, Direction direction, Hand hand) {
        this.biddingState = biddingState;
        this.direction = direction;
        this.hand = hand;
        if (hand != null) {
            HandSummary.ShowState showState = new HandSummary.ShowState();
            StandardHandEvaluator.evaluate(hand, showState);
            this.privateHandSummary = showState.handSummary;
        }
    }

    public Direction getDirection() { return direction; }
    public HandSummary getPublicHandSummary() { return publicHandSummary; }
    public boolean hasHand() { return hand != null; }
    public int getBidRound() { return bids.size() + 1; }
    public int getSeat() { return (direction.ordinal() - biddingState.getDealer().getDirection().ordinal() + 4) % 4 + 1; }
    public boolean isVulnerable() { return pairState.areVulnerable(); }

    public Call getBidHistory(int historyLevel) {
        if (bids.size() <= historyLevel) return null;
        return bids.get(bids.size() - 1 - historyLevel).getCall();
    }

    public PositionCalls getPositionCalls() {
        return pairState.getBiddingSystem().getPositionCalls(this);
    }

    public PositionState leftHandOpponent() {
        return biddingState.getPositions().get(direction.leftHandOpponent());
    }

    public PositionState partner() {
        return biddingState.getPositions().get(direction.partner());
    }

    public boolean privateHandConforms(BidRule rule) {
        return hasHand() && rule.satisfiesHandConstraints(this, privateHandSummary);
    }

    public boolean isValidNextCall(Call call) {
        return biddingState.getContract().isValid(call, direction);
    }

    public void setPairState(PairState ps) { this.pairState = ps; }
    public PairState getPairState() { return pairState; }

    public void makeCall(CallDetails cd) {
        biddingState.getContract().validateCall(cd.getCall(), direction);
        if (!(cd.getCall() instanceof Call.Pass) && !roleAssigned) {
            if (role == PositionRole.Opener) {
                assignRole(PositionRole.Opener);
                partner().assignRole(PositionRole.Responder);
                leftHandOpponent().role = PositionRole.Overcaller;
                partner().leftHandOpponent().role = PositionRole.Overcaller;
            } else if (role == PositionRole.Overcaller) {
                assignRole(PositionRole.Overcaller);
                partner().assignRole(PositionRole.Advancer);
            }
        }
        bids.add(cd);
        pairState.updatePairProperties(cd);
        repeatUpdatesUntilStable(cd);
    }

    private void assignRole(PositionRole role) {
        this.role = role;
        this.roleAssigned = true;
        this.roleAssignedOffset = bids.size();
    }

    private void repeatUpdatesUntilStable(CallDetails cd) {
        for (int i = 0; i < 1000; i++) {
            cd.pruneRules(this);
            HandSummary.ShowState showState = new HandSummary.ShowState(publicHandSummary);
            showState.handSummary.combine(cd.showHand(), State.CombineRule.Merge);
            if (publicHandSummary.equals(showState.handSummary)) return;
            publicHandSummary = showState.handSummary;
        }
    }
}
