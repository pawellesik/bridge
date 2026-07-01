package com.example.bridge.bridgit;

import java.util.*;

public class PositionState {
    public enum PositionRole { Opener, Overcaller, Responder, Advancer }

    private int roleAssignedOffset = 0;
    private boolean roleAssigned = false;
    private HandSummary privateHandSummary;
    private List<CallDetails> bids;
    private PairState pairState;
    private BiddingState biddingState;
    private PositionRole role;
    private HandSummary publicHandSummary;
    private Direction direction;
    private int seat;

    public PositionState(BiddingState biddingState, PairState pairState, Direction direction, int seat, Hand hand) {
        this.biddingState = biddingState;
        this.pairState = pairState;
        this.direction = direction;
        this.seat = seat;
        this.role = PositionRole.Opener;
        this.publicHandSummary = new HandSummary();
        this.bids = new ArrayList<>();

        if (hand != null) {
            HandSummary.ShowState showHand = new HandSummary.ShowState();
            StandardHandEvaluator.evaluate(hand, showHand);
            this.privateHandSummary = showHand.handSummary;
        } else {
            this.privateHandSummary = null;
        }
    }

    public boolean hasHand() { return privateHandSummary != null; }
    public PairState getPairState() { return pairState; }
    public PairState getOppsPairState() { return getRHO().getPairState(); }
    public BiddingState getBiddingState() { return biddingState; }
    public PositionRole getRole() { return role; }
    public void setRole(PositionRole role) { this.role = role; }
    public HandSummary getPublicHandSummary() { return publicHandSummary; }

    public boolean isPassed() { return bids.isEmpty() || bids.get(bids.size() - 1).getCall().equals(Call.Pass); }
    public boolean isPassedHand() { return !bids.isEmpty() && bids.get(0).getCall().equals(Call.Pass); }
    public boolean isDoubled() { return !bids.isEmpty() && bids.get(bids.size() - 1).getCall().equals(Call.Double); }

    public Call.Bid getBid() {
        if (bids.isEmpty()) return null;
        Call last = bids.get(bids.size() - 1).getCall();
        return (last instanceof Call.Bid) ? (Call.Bid) last : null;
    }

    public Direction getDirection() { return direction; }
    public int getSeat() { return seat; }
    public boolean isVulnerable() { return pairState.areVulnerable(); }

    public boolean isOurContract() { return biddingState.getContract().isOurs(this.direction); }
    public boolean isOpponentsContract() { return biddingState.getContract().isOpponents(this.direction); }

    public Call getBidHistory(int historyLevel) {
        if (bids.size() <= historyLevel) return null;
        return bids.get(bids.size() - 1 - historyLevel).getCall();
    }

    public int getCallCount() { return bids.size(); }
    public CallDetails getCallDetails(int index) { return bids.get(index); }

    public PositionState getPartner() { return biddingState.getPositions().get(direction.partner()); }
    public PositionState getRightHandOpponent() { return biddingState.getPositions().get(direction.rightHandOpponent()); }
    public PositionState getLeftHandOpponent() { return biddingState.getPositions().get(direction.leftHandOpponent()); }
    public PositionState getRHO() { return getRightHandOpponent(); }

    public int getBidRound() { return bids.size() + 1; }
    public int getRoleRound() { return getBidRound() - roleAssignedOffset; }
    public Call getLastCall() { return getBidHistory(0); }

    public boolean isForcedToBid() {
        return pairState.isForcedToBid(this) && !Call.Pass.equals(getRHO().getLastCall());
    }

    public PositionCalls getPositionCalls() {
        PositionState partner = getPartner();
        java.util.function.Function<PositionState, PositionCalls> bidFactory = !partner.bids.isEmpty() ? partner.bids.get(partner.bids.size() - 1).getBidsFactory() : null;
        if (bidFactory != null) return bidFactory.apply(this);
        return pairState.getBiddingSystem().getPositionCalls(this);
    }

    public void makeCall(CallDetails callDetails) {
        biddingState.getContract().validateCall(callDetails.getCall(), this.direction);
        if (!callDetails.getCall().equals(Call.Pass) && !this.roleAssigned) {
            if (role == PositionRole.Opener) {
                assignRole(PositionRole.Opener);
                getPartner().assignRole(PositionRole.Responder);
                getLeftHandOpponent().role = PositionRole.Overcaller;
                getRightHandOpponent().role = PositionRole.Overcaller;
            } else if (this.role == PositionRole.Overcaller) {
                assignRole(PositionRole.Overcaller);
                getPartner().assignRole(PositionRole.Advancer);
            }
        }
        bids.add(callDetails);
        pairState.updatePairProperties(callDetails);

        if (repeatUpdatesUntilStable(callDetails)) {
            biddingState.updateStateFromFirstBid();
        }
        pairState.updateLastShownSuit(callDetails.getCall(), this, callDetails.showHand());
    }

    private void assignRole(PositionRole role) {
        this.role = role;
        this.roleAssigned = true;
        this.roleAssignedOffset = bids.size();
    }

    public static class UpdateResult {
        public boolean updateHappened;
    }

    public boolean updateBidIndex(int bidIndex, UpdateResult result) {
        if (bidIndex >= bids.size()) {
            result.updateHappened = false;
            return false;
        }
        result.updateHappened = repeatUpdatesUntilStable(bids.get(bidIndex));
        return true;
    }

    public boolean repeatUpdatesUntilStable(CallDetails callDetails) {
        boolean stateChanged = false;
        for (int i = 0; i < 1000; i++) {
            stateChanged |= callDetails.pruneRules(this);
            HandSummary.ShowState showHand = new HandSummary.ShowState(publicHandSummary);
            showHand.combine(callDetails.showHand(), State.CombineRule.Merge);

            if (this.publicHandSummary.equals(showHand.handSummary)) {
                return stateChanged;
            }
            stateChanged = true;
            pairState.updateShownSuits(callDetails.getCall(), this, showHand.handSummary);
            publicHandSummary = showHand.handSummary;
        }
        return false;
    }

    public boolean isOpenerJumpShift(Call call) {
        if (this.role == PositionRole.Opener && biddingState.getOpeningBid() instanceof Call.Bid && call instanceof Call.Bid) {
            Call.Bid openingBid = (Call.Bid) biddingState.getOpeningBid();
            Call.Bid thisBid = (Call.Bid) call;
            return thisBid.getStrain() != Strain.NoTrump && openingBid.getStrain() != Strain.NoTrump && thisBid.jumpOver(openingBid) == 1 && pairState.firstToShow(thisBid.getSuit()) == null;
        }
        return false;
    }

    public boolean isReverse(Call call) {
        if (call instanceof Call.Bid) {
            Call.Bid bid = (Call.Bid) call;
            Suit bidSuit = bid.getSuit();
            if (bidSuit != null) {
                for (int i = bids.size() - 1; i >= 0; i--) {
                    Call last = bids.get(i).getCall();
                    if (last instanceof Call.Bid) {
                        Call.Bid lastBid = (Call.Bid) last;
                        return (lastBid.getLevel() == bid.getLevel() - 1 && lastBid.getSuit() != null && lastBid.getSuit().ordinal() < bidSuit.ordinal() && biddingState.getContract().isJump(bid) == 0 && pairState.firstToShow(bidSuit) == null);
                    }
                }
            }
        }
        return false;
    }

    public boolean privateHandConforms(BidRule rule) {
        return hasHand() && rule.satisfiesHandConstraints(this, this.privateHandSummary);
    }

    public List<Constraint> privateHandFailingConstraints(BidRule rule) {
        return rule.failingHandConstraints(this, this.privateHandSummary);
    }

    public boolean isValidNextCall(Call call) {
        return biddingState.getContract().isValid(call, this.direction);
    }
}
