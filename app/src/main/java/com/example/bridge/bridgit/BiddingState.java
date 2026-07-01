package com.example.bridge.bridgit;

import java.util.*;

public class BiddingState {
    private final Map<Direction, PositionState> positions = new HashMap<>();
    private final PositionState dealer;
    private PositionState nextToAct;
    private PositionState opener;
    private Call.Bid openingBid;
    private final ContractState contract = new ContractState();
    private final Game game;
    private PositionCalls positionChoices = null;

    public BiddingState(Game game) {
        this.game = game;
        Direction d = game.getDealer();
        PairState ns = new PairState(this, Pair.NS, new LCStandard(), game.getVulnerable());
        PairState ew = new PairState(this, Pair.EW, new LCStandard(), game.getVulnerable());
        
        for (int seat = 1; seat <= 4; seat++) {
            Hand hand = game.getDeal().get(d);
            PositionState ps = new PositionState(this, d, hand);
            ps.setPairState(d.pair() == Pair.NS ? ns : ew);
            positions.put(d, ps);
            d = d.leftHandOpponent();
        }
        this.dealer = positions.get(game.getDealer());
        this.nextToAct = this.dealer;

        if (game.getAuction() != null && !game.getAuction().isEmpty()) {
            List<Call> calls = game.getAuction().getCalls();
            game.getAuction().clear();
            for (Call call : calls) {
                makeCall(call);
            }
        }
    }

    public void updateStateFromFirstBid() {
        for (int i = 0; i < 50; i++) {
            PositionState position = dealer;
            int bidIndex = 0;
            boolean someStateChanged = false;
            
            while (true) {
                PositionState.UpdateResult res = position.updateBidIndex(bidIndex);
                someStateChanged |= res.stateChanged;
                
                position = position.leftHandOpponent();
                if (position == dealer) {
                    bidIndex++;
                    boolean anyMoreBids = false;
                    for (PositionState ps : positions.values()) {
                        if (bidIndex < ps.getCallCount()) {
                            anyMoreBids = true;
                            break;
                        }
                    }
                    if (!anyMoreBids) break;
                }
            }
            if (!someStateChanged) return;
        }
        throw new RuntimeException("Unable to resolve to a stable state. Giving up");
    }

    public ContractState getContract() { return contract; }
    public PositionState getDealer() { return dealer; }
    public PositionState getNextToAct() { return nextToAct; }
    public Map<Direction, PositionState> getPositions() { return positions; }

    public PositionCalls getCallChoices() {
        if (positionChoices == null) {
            positionChoices = nextToAct.getPositionCalls();
        }
        return positionChoices;
    }

    public void makeCall(Call call) {
        contract.validateCall(call, nextToAct.getDirection());
        PositionCalls choices = getCallChoices();
        if (!choices.containsKey(call)) {
            CallDetails cd = new CallDetails(null, call);
            cd.setPositionState(nextToAct);
            choices.put(call, cd);
        }
        makeCall(choices.get(call));
    }

    public void makeCall(CallDetails cd) {
        nextToAct.makeCall(cd);
        contract.makeCall(cd.getCall(), cd.getPositionState().getDirection());
        
        if (openingBid == null && cd.getCall() instanceof Call.Bid) {
            openingBid = (Call.Bid) cd.getCall();
            opener = cd.getPositionState();
        }

        // Update the actual game object
        game.getAuction().addCall(cd.getCall());
        game.updateContractFromAuction();

        if (contract.isAuctionComplete()) {
            game.setContract(contract);
            if (!contract.isPassedOut()) {
                game.setDeclarer(contract.declarer);
            }
        }

        nextToAct = nextToAct.leftHandOpponent();
        positionChoices = null;
    }

    public Call.Bid getOpeningBid() { return openingBid; }
    public PositionState getOpener() { return opener; }
}
