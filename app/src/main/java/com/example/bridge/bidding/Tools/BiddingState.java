package com.example.bridge.bidding.Tools;

import com.example.bridge.bidding.LCStandard.LCStandard;
import com.example.bridge.bidding.NatC.NatC;

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
            return new LCStandard();
        }
        else if (bidSystem.equals("NatC")){
            return new NatC();
        }
        else if (bidSystem.equals("PassOnly")) {
            return new PassOnlySystem();
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
        if (callDetails.getCall() instanceof Bid) {
            callDetails.setJumpLevel(contract.isJump((Bid) callDetails.getCall()));
        }
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

    public Game getGame() {
        return game;
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
            someStateChanged |= balancePublicKnowledge();
            if (!someStateChanged) {
                return;
            }
        }
        throw new RuntimeException("Unable to resolve to a stable state. Giving up");
    }

    private boolean balancePublicKnowledge() {
        boolean changed = false;

        // 1. HCP balancing (Sum of all 4 hands must be exactly 40 HCP)
        int totalMinHCP = 0;
        for (PositionState pos : positions.values()) {
            Range hcp = pos.getPublicHandSummary().getHighCardPoints();
            if (hcp != null) totalMinHCP += hcp.getMin();
        }
        for (PositionState pos : positions.values()) {
            HandSummary hs = pos.getPublicHandSummary();
            Range hcpBefore = hs.getHighCardPoints();
            if (hcpBefore != null) {
                int othersMin = totalMinHCP - hcpBefore.getMin();
                hs.trimHCP(othersMin);
                if (!hs.getHighCardPoints().equals(hcpBefore)) changed = true;
            }
        }

        // 2. Suit balancing (Sum of cards in each suit across all 4 hands must be exactly 13)
        for (Suit suit : Suit.values()) {
            int totalMinSuit = 0;
            for (PositionState pos : positions.values()) {
                totalMinSuit += pos.getPublicHandSummary().getSuits().get(suit).getShape().getMin();
            }
            for (PositionState pos : positions.values()) {
                HandSummary.SuitSummary ss = pos.getPublicHandSummary().getSuits().get(suit);
                Range shapeBefore = ss.getShape();
                int othersMin = totalMinSuit - shapeBefore.getMin();
                ss.trimShapeGlobal(othersMin);
                if (!ss.getShape().equals(shapeBefore)) changed = true;
            }
        }

        return changed;
    }
}




























































































































































































































































































































































































































































































