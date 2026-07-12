package com.example.bridge.bidding.BridgeBidder;

import java.util.*;

public class ContractState extends Contract {
    private Direction lastBidBy = null;
    private Direction declarer = null;
    private int callsRemaining = 4;
    private final Map<Strain, List<Direction>> firstToNameStrain = new EnumMap<>(Strain.class);

    public boolean isPassEndsAuction() {
        return callsRemaining == 1;
    }

    public boolean isAuctionComplete() {
        return callsRemaining == 0;
    }

    public boolean isPassedOut() {
        return callsRemaining == 0 && bid == null;
    }

    public boolean isOurs(Direction direction) {
        return declarer != null && (declarer == direction || declarer == direction.partner());
    }

    public boolean isOpponents(Direction direction) {
        return declarer != null && !isOurs(direction);
    }

    public Direction getDeclarer() {
        return declarer;
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
        if (isAuctionComplete()) return "Auction is complete. No more calls allowed";
        if (call instanceof Pass) return null;
        if (call instanceof DoubleCall) {
            if (risk != Risk.Undoubled) return "Can not double contract that is currently " + risk + ".";
            if (isOurs(by)) return "Can not double own side's contract";
            return null;
        }
        if (call instanceof Redouble) {
            if (risk != Risk.Doubled) return "Can not redouble contract that is currently " + risk + ".";
            if (isOpponents(by)) return "Can not redouble contract. Contract is opponents.";
            return null;
        }
        if (call instanceof Bid) {
            Bid newBid = (Bid) call;
            if (this.bid != null && newBid.compareTo(this.bid) <= 0) {
                return "Bid " + newBid + " is lower that current contract of " + this.bid;
            }
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
        if (this.firstToNameStrain.containsKey(bid.getStrain())) {
            for (Direction namedStrain : firstToNameStrain.get(bid.getStrain())) {
                if (namedStrain == by) return;
                if (namedStrain == by.partner()) {
                    this.declarer = by.partner();
                    return;
                }
            }
            this.firstToNameStrain.get(bid.getStrain()).add(by);
        } else {
            List<Direction> directions = new ArrayList<>();
            directions.add(by);
            this.firstToNameStrain.put(bid.getStrain(), directions);
        }
    }

    public void makeCall(Call call, Direction by) {
        validateCall(call, by);
        if (call instanceof Pass) {
            callsRemaining--;
        } else if (call instanceof Bid) {
            makeBid((Bid) call, by);
        } else if (call instanceof DoubleCall) {
            risk = Risk.Doubled;
            callsRemaining = 3;
        } else if (call instanceof Redouble) {
            risk = Risk.Redoubled;
            callsRemaining = (bid.getLevel() == 7 && bid.getStrain() == Strain.NoTrump) ? 0 : 3;
        }
    }

    public int isJump(Bid bid) {
        return (this.bid == null) ? bid.getLevel() - 1 : bid.jumpOver(this.bid);
    }

    public Call nextAvailableBid(Suit suit) {
        if (suit == null) return null;
        return nextAvailableBid(suit.toStrain());
    }

    public Call nextAvailableBid(Strain strain) {
        if (strain == null || isAuctionComplete() || this.bid == null) return null;
        Bid nextBid = new Bid(this.bid.getLevel(), strain);
        if (nextBid.compareTo(this.bid) <= 0) {
            if (this.bid.getLevel() == 7) return null;
            nextBid = new Bid(this.bid.getLevel() + 1, strain);
        }
        return nextBid;
    }

    public static ContractState fromCalls(Direction dealer, Iterable<Call> calls) {
        ContractState contract = new ContractState();
        Direction d = dealer;
        for (Call call : calls) {
            contract.makeCall(call, d);
            d = d.leftHandOpponent();
        }
        return contract;
    }

    public static boolean isValidAuction(Direction dealer, List<Call> calls) {
        ContractState contract = new ContractState();
        Direction d = dealer;
        for (Call call : calls) {
            if (!contract.isValid(call, d)) return false;
            contract.makeCall(call, d);
            d = d.leftHandOpponent();
        }
        return true;
    }
}




























































































































































































































































































































































































































































































