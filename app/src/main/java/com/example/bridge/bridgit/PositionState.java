package com.example.bridge.bridgit;

import java.util.*;
import java.util.stream.Collectors;

public class PositionState {
    public enum PositionRole { Opener, Overcaller, Responder, Advancer }

    // When the first bid is made for a role, this variable is assigned to the length of _bids.  This allows
    // the property RoleRound to return the proper value.  For example, if a position has passed twice and then
    // places a bid as Advancer, the offset would be 2, indicating that the position became the Advancer on the
    // third round of bidding.  This allows us to property compute the "RoleRound" property.
    private int roleAssignedOffset = 0;
    private boolean roleAssigned = false;

    private HandSummary privateHandSummary;

    public boolean hasHand() {
        return privateHandSummary != null;
    }

    private List<CallDetails> bids;

    public PairState getPairState() {
        return pairState;
    }
    private void setPairState(PairState pairState) {
        this.pairState = pairState;
    }
    private PairState pairState;

    public PairState getOppsPairState() {
        return getRHO().getPairState();
    }

    public BiddingState getBiddingState() {
        return biddingState;
    }
    private BiddingState biddingState;

    public PositionRole getRole() {
        return role;
    }
    public void setRole(PositionRole role) {
        this.role = role;
    }
    private PositionRole role;

    public HandSummary getPublicHandSummary() {
        return publicHandSummary;
    }
    private HandSummary publicHandSummary;

    public boolean isPassed() {
        return bids.size() == 0 || bids.get(bids.size() - 1).getCall().equals(Call.Pass);
    }

    public boolean isPassedHand() {
        return bids.size() > 0 && bids.get(0).getCall().equals(Call.Pass);
    }

    public boolean isDoubled() {
        return bids.size() > 0 && bids.get(bids.size() - 1).getCall().equals(Call.Double);
    }

    public Call.Bid getBid() {
        if (bids.size() == 0) return null;
        Call lastCall = bids.get(bids.size() - 1).getCall();
        if (lastCall instanceof Call.Bid) {
            return (Call.Bid) lastCall;
        }
        return null;
    }

    public Direction getDirection() {
        return direction;
    }
    private Direction direction;

    public int getSeat() {
        return seat;
    }
    private int seat;

    public boolean isVulnerable() {
        return pairState.areVulnerable();
    }

    public boolean isOurContract() {
        return biddingState.getContract().isOurs(this.direction);
    }

    public boolean isOpponentsContract() {
        return biddingState.getContract().isOpponents(this.direction);
    }

    public Call getBidHistory(int historyLevel) {
        if (bids.size() <= historyLevel) {
            return null;
        }
        return bids.get(bids.size() - 1 - historyLevel).getCall();
    }

    public int getCallCount() {
        return bids.size();
    }

    public CallDetails getCallDetails(int index) {
        return bids.get(index);
    }

    public PositionState getPartner() {
        return biddingState.getPositions().get(direction.partner());
    }
    public PositionState getRightHandOpponent() {
        return biddingState.getPositions().get(direction.rightHandOpponent());
    }
    public PositionState getLeftHandOpponent() {
        return biddingState.getPositions().get(direction.leftHandOpponent());
    }

    public PositionState getRHO() {
        return getRightHandOpponent();
    }

    public PositionState(BiddingState biddingState, PairState pairState, Direction direction, int seat, Hand hand) {
        // Debug.Assert(seat >= 1 && seat <= 4);
        this.biddingState = biddingState;
        this.direction = direction;
        this.seat = seat;
        this.role = PositionRole.Opener;    // Best start for any position.  Will change with time.
        this.publicHandSummary = new HandSummary();
        this.pairState = pairState;
        this.bids = new ArrayList<CallDetails>();

        if (hand != null) {
            HandSummary.ShowState showHand = new HandSummary.ShowState();
            // TODO: This is where we would need to use a differnet implementation of HandSummary evaluator...
            StandardHandEvaluator.evaluate(hand, showHand);
            this.privateHandSummary = showHand.getHandSummary();
        } else {
            this.privateHandSummary = null;
        }
    }

    public int getBidRound() {
        return this.bids.size() + 1;
    }

    public int getRoleRound() {
        return getBidRound() - roleAssignedOffset;
    }

    public Call getLastCall() {
        return getBidHistory(0);
    }

    public boolean isForcedToBid() {
        // TODO: This only returns true IFF we have to bid because of a forcing 1 round bid. NOT is we are forced to game.
        return (pairState.isForcedToBid(this) && !getRightHandOpponent().bids.get(getRightHandOpponent().bids.size() - 1).equals(Call.Pass));
    }

    public PositionCalls getPositionCalls() {
        java.util.function.Function<PositionState, PositionCalls> bidFactory = getPartner().bids.size() > 0 ? getPartner().bids.get(getPartner().bids.size() - 1).getBidsFactory() : null;
        if (bidFactory != null) return bidFactory.apply(this);
        return pairState.getBiddingSystem().getPositionCalls(this);
    }

    public void makeCall(CallDetails callDetails) {
        biddingState.getContract().validateCall(callDetails.getCall(), this.direction);
        if (!callDetails.getCall().equals(Call.Pass) && !this.roleAssigned) {
            if (role == PositionRole.Opener) {
                assignRole(PositionRole.Opener);
                getPartner().assignRole(PositionRole.Responder);
                getLeftHandOpponent().setRole(PositionRole.Overcaller);
                getRightHandOpponent().setRole(PositionRole.Overcaller);
            } else if (this.role == PositionRole.Overcaller) {
                assignRole(PositionRole.Overcaller);
                getPartner().assignRole(PositionRole.Advancer);
            }
        }
        bids.add(callDetails);
        pairState.updatePairProperties(callDetails);

        // TODO: FIX! HACK!

        // Now show any state changes to the PairAgreements.  This only happens once per call
        // and does not update dynamically like the PublicHandSummary.
        // TODO: Should this happen here?  
        //var showAgreements = new PairAgreements.ShowState(PairState.Agreements);
        //showAgreements.Combine(callDetails.ShowAgreements(), State.CombineRule.Merge);
        // TODO:  NEED TO SHOW AGREEMENTS!!! PairState.Agreements = callDetails.ShowAgreements();

        ///	if (callDetails.BidForce == BidForce.ForcingToGame)
        //	{
        //		PairState.InGameForcingAuction = true;
        //		}
        // Now we prune any rules that do not 

        if (repeatUpdatesUntilStable(callDetails)) {
            biddingState.updateStateFromFirstBid();
        }
        // TODO: Seems ugly to do this again, but whatever...
        pairState.updateLastShownSuit(callDetails.getCall(), this, callDetails.showHand());
    }

    private void assignRole(PositionRole role) {
        // Debug.Assert(_roleAssigned == false);
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
        result.updateHappened = repeatUpdatesUntilStable(this.bids.get(bidIndex));
        return true;
    }

    public boolean repeatUpdatesUntilStable(CallDetails callDetails) {
        // Debug.Assert(callDetails.PositionState == this);

        boolean stateChanged = false;
        for (int i = 0; i < 1000; i++) {
            stateChanged |= callDetails.pruneRules(this);

            HandSummary.ShowState showHand = new HandSummary.ShowState(publicHandSummary);
            showHand.combine(callDetails.showHand(), State.CombineRule.Merge);

            if (this.publicHandSummary.equals(showHand.getHandSummary())) {
                return stateChanged;
            }
            stateChanged = true;
            pairState.updateShownSuits(callDetails.getCall(), this, showHand.getHandSummary());
            publicHandSummary = showHand.getHandSummary();
        }
        // Debug.Assert(false); // This is bad - we had over 1000 state changes.  Infinite loop time...
        return false;	// Seems the best thing to do to avoid repeated
    }

    // TODO: Perhaps remove this.  Looks like combinations of other 
    // constraints are used instead.
    public boolean isOpenerJumpShift(Call call) {
        if (this.role == PositionRole.Opener &&
                this.biddingState.getOpeningBid() instanceof Call.Bid &&
                call instanceof Call.Bid) {
            Call.Bid openingBid = (Call.Bid) this.biddingState.getOpeningBid();
            Call.Bid thisBid = (Call.Bid) call;
            return (thisBid.getStrain() != Strain.NoTrump &&
                    openingBid.getStrain() != Strain.NoTrump &&
                    thisBid.jumpOver(openingBid) == 1 &&
                    pairState.firstToShow(thisBid.getSuit()) == null);
        }
        return false;
    }

    public boolean isReverse(Call call) {
        if (call instanceof Call.Bid) {
            Call.Bid bid = (Call.Bid) call;
            Suit bidSuit = bid.getSuit();
            if (bidSuit != null) {
                for (int i = bids.size() - 1; i >= 0; i--) {
                    Call lastCall = bids.get(i).getCall();
                    if (lastCall instanceof Call.Bid) {
                        Call.Bid lastBid = (Call.Bid) lastCall;
                        if (lastBid.getLevel() == bid.getLevel() - 1 &&
                                lastBid.getSuit() != null && lastBid.getSuit().ordinal() < bidSuit.ordinal() &&
                                biddingState.getContract().isJump(bid) == 0 &&
                                pairState.firstToShow(bidSuit) == null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // TODO: This logic is spread out across several classes.  Think about how to consolidate it.
    public boolean privateHandConforms(BidRule rule) {
        return hasHand() ? rule.satisfiesHandConstraints(this, this.privateHandSummary) : false;
    }

    // Returns a list of dynamic constraints that do not conform to the private hand.
    // If the list is empty, then the hand conforms.  Any caller should check HasHand first.
    public List<Constraint> privateHandFailingConstraints(BidRule rule) {
        // Debug.Assert(HasHand);
        return rule.failingHandConstraints(this, this.privateHandSummary);
    }

    public boolean isValidNextCall(Call call) {
        return biddingState.getContract().isValid(call, this.direction);
    }
}
