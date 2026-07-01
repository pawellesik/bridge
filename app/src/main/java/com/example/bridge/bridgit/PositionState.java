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
    public BiddingState getBiddingState() { return biddingState; }
    public HandSummary getPublicHandSummary() { return publicHandSummary; }
    public boolean hasHand() { return hand != null; }
    public int getBidRound() { return bids.size() + 1; }
    public int getCallCount() { return bids.size(); }
    public int getSeat() { return (direction.ordinal() - biddingState.getDealer().getDirection().ordinal() + 4) % 4 + 1; }
    public boolean isVulnerable() { return pairState.areVulnerable(); }

    public Call getBidHistory(int historyLevel) {
        if (bids.size() <= historyLevel) return null;
        return bids.get(bids.size() - 1 - historyLevel).getCall();
    }

    public CallDetails getCallDetails(int index) {
        return bids.get(index);
    }

    public PositionCalls getPositionCalls() {
        CallDetails lastPartnerCall = partner().getCallCount() > 0 ? partner().getCallDetails(partner().getCallCount() - 1) : null;
        java.util.function.Function<PositionState, PositionCalls> bidFactory = lastPartnerCall != null ? lastPartnerCall.getBidsFactory() : null;
        if (bidFactory != null) return bidFactory.apply(this);
        return pairState.getBiddingSystem().getPositionCalls(this);
    }

    public PositionState leftHandOpponent() {
        return biddingState.getPositions().get(direction.leftHandOpponent());
    }

    public PositionState rightHandOpponent() {
        return biddingState.getPositions().get(direction.rightHandOpponent());
    }

    public PositionState partner() {
        return biddingState.getPositions().get(direction.partner());
    }

    public boolean privateHandConforms(BidRule rule) {
        return hasHand() && rule.satisfiesHandConstraints(this, privateHandSummary);
    }

    public List<Constraint> privateHandFailingConstraints(BidRule rule) {
        if (!hasHand()) return Collections.emptyList();
        return rule.failingHandConstraints(this, privateHandSummary);
    }

    public boolean isValidNextCall(Call call) {
        return biddingState.getContract().isValid(call, direction);
    }

    public boolean isForcedToBid() {
        return pairState.isForcedToBid(this) && !Call.Pass.equals(rightHandOpponent().getBidHistory(0));
    }

    public void setPairState(PairState ps) { this.pairState = ps; }
    public PairState getPairState() { return pairState; }
    public PairState getOpponentsPairState() { return rightHandOpponent().getPairState(); }

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
        if (repeatUpdatesUntilStable(cd)) {
            biddingState.updateStateFromFirstBid();
        }
        pairState.updateLastShownSuit(cd.getCall(), this, cd.showHand());
    }

    private void assignRole(PositionRole role) {
        this.role = role;
        this.roleAssigned = true;
        this.roleAssignedOffset = bids.size();
    }

    public static class UpdateResult {
        public boolean bidExists;
        public boolean stateChanged;
    }

    public UpdateResult updateBidIndex(int bidIndex) {
        UpdateResult res = new UpdateResult();
        if (bidIndex >= bids.size()) {
            res.bidExists = false;
            res.stateChanged = false;
            return res;
        }
        res.bidExists = true;
        res.stateChanged = repeatUpdatesUntilStable(bids.get(bidIndex));
        return res;
    }

    public boolean repeatUpdatesUntilStable(CallDetails cd) {
        boolean stateChanged = false;
        for (int i = 0; i < 1000; i++) {
            boolean pruned = cd.pruneRules(this);
            stateChanged |= pruned;

            HandSummary.ShowState showState = new HandSummary.ShowState(publicHandSummary);
            showState.handSummary.combine(cd.showHand(), State.CombineRule.Merge);

            if (publicHandSummary.equals(showState.handSummary)) {
                return stateChanged;
            }
            stateChanged = true;
            pairState.updateShownSuits(cd.getCall(), this, showState.handSummary);
            publicHandSummary = showState.handSummary;
        }
        return stateChanged;
    }
}
