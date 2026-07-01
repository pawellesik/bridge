package com.example.bridge.bridgit;

import com.example.bridge.bridgit.Call.Bid;
import com.example.bridge.bridgit.Call.Pass;
import com.example.bridge.bridgit.Call.DoubleCall;
import com.example.bridge.bridgit.Call.RedoubleCall;

import java.util.*;

public class ContractState extends Contract {
    public Direction lastBidBy = null;
    public Direction declarer = null;
    public int callsRemaining = 4;
    public Map<Strain, List<Direction>> firstToNameStrain = new HashMap<>();

    public boolean passEndsAuction() {
        return this.callsRemaining == 1;
    }

    public boolean isAuctionComplete() {
        return this.callsRemaining == 0;
    }

    public boolean isPassedOut() {
        return this.callsRemaining == 0 && bid == null;
    }

    public boolean isOurs(Direction direction) {
        return (declarer != null && (declarer == direction || declarer == direction.partner()));
    }

    public boolean isOpponents(Direction direction) {
        return (declarer != null && !isOurs(direction));
    }

    public void validateCall(Call call, Direction by) {
        String error = callError(call, by);
        if (error != null) {
            throw new RuntimeException(call + " is invalid by " + by + " over " + this + ": " + error);
        }
    }

    public boolean isValid(Call call, Direction by) {
        return callError(call, by) == null;
    }

    private String callError(Call call, Direction by) {
        if (isAuctionComplete())
            return "Auction is complete. No more calls allowed";
        if (call instanceof Pass)
            return null;
        if (call instanceof DoubleCall) {
            if (risk != Risk.Undoubled)
                return "Can not double contract that is currently " + risk;
            if (isOurs(by))
                return "Can not double own side's contract";
            if (bid == null)
                return "Can not double before a bid has been made";
            return null;
        }
        if (call instanceof RedoubleCall) {
            if (risk != Risk.Doubled)
                return "Can not redouble contract that is currently " + risk;
            if (isOpponents(by))
                return "Can not redouble contract. Contract is opponents.";
            return null;
        }
        if (call instanceof Bid) {
            Bid newBid = (Bid) call;
            if (this.bid != null && newBid.compareTo(this.bid) <= 0)
                return "Bid " + newBid + " is lower than current contract of " + this.bid;
            return null;
        }
        return "Unknown internal state error";
    }

    private void makeBid(Bid bid, Direction by) {
        this.bid = bid;
        this.lastBidBy = by;
        this.risk = Risk.Undoubled;
        this.callsRemaining = 3;
        this.declarer = by;
        
        List<Direction> directions = firstToNameStrain.computeIfAbsent(bid.getStrain(), k -> new ArrayList<>());
        for (Direction namedStrain : directions) {
            if (namedStrain == by) return;
            if (namedStrain == by.partner()) {
                this.declarer = by.partner();
                return;
            }
        }
        directions.add(by);
    }

    public void makeCall(Call call, Direction by) {
        validateCall(call, by);
        if (call instanceof Pass) {
            callsRemaining -= 1;
        } else if (call instanceof Bid) {
            makeBid((Bid) call, by);
        } else if (call instanceof DoubleCall) {
            risk = Risk.Doubled;
            callsRemaining = 3;
        } else if (call instanceof RedoubleCall) {
            risk = Risk.Redoubled;
            callsRemaining = (bid.getLevel() == 7 && bid.getStrain() == Strain.NoTrump) ? 0 : 3;
        }
    }

    public int isJump(Bid bid) {
        return (this.bid == null) ? bid.getLevel() - 1 : bid.jumpOver(this.bid);
    }

    public Call.Bid nextAvailableBid(Suit suit) {
        return nextAvailableBid(suit.toStrain());
    }

    public Call.Bid nextAvailableBid(Strain strain) {
        if (isAuctionComplete()) return null;
        if (this.bid == null) return new Call.Bid(1, strain);
        
        Call.Bid next = new Call.Bid(this.bid.getLevel(), strain);
        if (next.compareTo(this.bid) <= 0) {
            if (this.bid.getLevel() == 7) return null;
            next = new Call.Bid(this.bid.getLevel() + 1, strain);
        }
        return next;
    }

    public static ContractState fromCalls(Direction dealer, Iterable<Call> calls) {
        ContractState state = new ContractState();
        Direction d = dealer;
        for (Call call : calls) {
            state.makeCall(call, d);
            d = d.leftHandOpponent();
        }
        return state;
    }

    public static boolean isValidAuction(Direction dealer, List<Call> calls) {
        ContractState state = new ContractState();
        Direction d = dealer;
        for (Call call : calls) {
            if (!state.isValid(call, d)) return false;
            state.makeCall(call, d);
            d = d.leftHandOpponent();
        }
        return true;
    }
}
