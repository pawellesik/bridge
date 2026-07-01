package com.example.bridge.bridgit;

import java.util.*;

public class BiddingState {
    private final Map<Direction, PositionState> positions = new HashMap<>();
    private final PositionState dealer;
    private PositionState nextToAct;
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
            // Simplified: Add placeholder rule if not found
            CallDetails cd = new CallDetails(null, call);
            cd.setPositionState(nextToAct);
            choices.put(call, cd);
        }
        makeCall(choices.get(call));
    }

    public void makeCall(CallDetails cd) {
        nextToAct.makeCall(cd);
        contract.makeCall(cd.getCall(), cd.getPositionState().getDirection());
        
        nextToAct = nextToAct.leftHandOpponent();
        positionChoices = null;
    }
}
