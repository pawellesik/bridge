package com.example.bridge.bidding.BridgeBidder;

import java.util.*;

public class BiddingState {
    private final Map<Direction, PositionState> positions = new EnumMap<>(Direction.class);
    private final PositionState dealer;
    private PositionCalls currentPositionCalls = null;
    private PositionState nextToAct;
    private final Game game;
    private final ContractState contract;
    private Bid openingBid = null;
    private PositionState opener = null;

    public BiddingState(Game game) {
        this.game = game;
        this.contract = new ContractState();
        Direction d = game.dealer;
        PairState ns = new PairState(this, Pair.NS, getBidSystem(game.bidSystemNS), game.vulnerable);
        PairState ew = new PairState(this, Pair.EW, getBidSystem(game.bidSystemEW), game.vulnerable);
        
        for (int seat = 1; seat <= 4; seat++) {
            Hand hand = game.getDeal().get(d);
            this.positions.put(d, new PositionState(this, d.pair() == Pair.NS ? ns : ew, d, seat, hand));
            d = d.leftHandOpponent();
        }
        this.dealer = positions.get(game.dealer);
        this.nextToAct = dealer;

        if (game.getAuction() != null && !game.getAuction().isEmpty()) {
            List<Call> calls = game.getAuction().getCalls();
            game.getAuction().clear();
            for (Call call : calls) {
                makeCall(call);
            }
        }
    }

    private static IBiddingSystem getBidSystem(String bidSystem) {
        if (bidSystem == null || bidSystem.isEmpty() || bidSystem.equals("TwoOverOneGameForce") || bidSystem.equals("LC-Basic")) {
            return new com.example.licytacja.moje.BridgeBidder.LCStandard.LCStandard();
        }
        else if (bidSystem.equals("NatC")){
            return new com.example.licytacja.moje.BridgeBidder.NatC.NatC();
        }
        throw new IllegalArgumentException("Unknown bidding system " + bidSystem);
    }

    public PositionCalls getCallChoices() {
        if (currentPositionCalls == null) {
            currentPositionCalls = nextToAct.getPositionCalls();
        }
        return currentPositionCalls;
    }

    public void makeCall(Call call) {
        contract.validateCall(call, nextToAct.getDirection());
        PositionCalls choices = getCallChoices();
        if (!choices.containsKey(call)) {
            choices.createPlaceholderCall(call);
        }
        makeCall(choices.get(call));
    }

    public void makeCall(CallDetails callDetails) {
        callDetails.getPositionState().makeCall(callDetails);
        contract.makeCall(callDetails.getCall(), callDetails.getPositionState().getDirection());
        
        if (this.openingBid == null && callDetails.getCall() instanceof Bid) {
            this.openingBid = (Bid) callDetails.getCall();
            this.opener = nextToAct;
        }
        game.getAuction().add(callDetails);
        if (contract.isAuctionComplete()) {
            game.contract = contract;
            if (!contract.isPassedOut()) {
                game.declarer = contract.getDeclarer();
            }
        }
        nextToAct = nextToAct.getLHO();
        currentPositionCalls = null;
    }

    public Map<Direction, PositionState> getPositions() {
        return positions;
    }

    public PositionState getNextToAct() {
        return nextToAct;
    }

    public ContractState getContract() {
        return contract;
    }

    public Bid getOpeningBid() {
        return openingBid;
    }

    public void updateStateFromFirstBid() {
        for (int i = 0; i < 50; i++) {
            PositionState position = dealer;
            int bidIndex = 0;
            boolean someStateChanged = false;
            boolean[] posStateChanged = new boolean[1];
            while (position.updateBidIndex(bidIndex, posStateChanged)) {
                someStateChanged |= posStateChanged[0];
                position = position.getLHO();
                if (position == dealer) {
                    bidIndex++;
                }
            }
            if (!someStateChanged) {
                return;
            }
        }
        throw new RuntimeException("Unable to resolve to a stable state. Giving up");
    }
}
