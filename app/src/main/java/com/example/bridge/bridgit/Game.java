package com.example.bridge.bridgit;

import java.util.*;

public class Game {
    private String event = "Casual Game";
    private int board = 0;
    private Scoring scoring = Scoring.MP;
    private String bidSystemEW = null;
    private String bidSystemNS = null;
    private Vulnerable vulnerable = Vulnerable.None;
    private Direction dealer = Direction.N;
    private final Deal deal;
    private Direction declarer = null;
    private Contract contract = null;
    private final Auction auction;

    public Game() {
        this.deal = new Deal(this);
        this.auction = new Auction(this);
    }

    public Direction getDealer() { return dealer; }
    public void setDealer(Direction dealer) { this.dealer = dealer; }
    public Vulnerable getVulnerable() { return vulnerable; }
    public void setVulnerable(Vulnerable vulnerable) { this.vulnerable = vulnerable; }
    public String getBidSystemNS() { return bidSystemNS; }
    public void setBidSystemNS(String system) { this.bidSystemNS = system; }
    public String getBidSystemEW() { return bidSystemEW; }
    public void setBidSystemEW(String system) { this.bidSystemEW = system; }
    public Deal getDeal() { return deal; }
    public Auction getAuction() { return auction; }
    public Contract getContract() { return contract; }
    public void setContract(Contract contract) { this.contract = contract; }
    public Direction getDeclarer() { return declarer; }
    public void setDeclarer(Direction declarer) { this.declarer = declarer; }

    public void setStandardBoard(int board) {
        if (board <= 0) throw new IllegalArgumentException("Board must be >= 1");
        this.board = board;
        this.dealer = Direction.values()[(board - 1) % 4];
        int vulOffset = (board - 1) / 4;
        this.vulnerable = Vulnerable.values()[(board - 1 + vulOffset) % 4];
    }

    public void parseDeal(String dealStr, boolean overrideDealer) {
        if (dealStr == null || dealStr.length() < 9) throw new IllegalArgumentException("Invalid deal string");
        
        String prefix = dealStr.substring(0, 1);
        Direction startDir = Direction.valueOf(prefix);
        if (overrideDealer) {
            this.dealer = startDir;
        } else if (startDir != this.dealer) {
            throw new IllegalArgumentException("Deal prefix doesn't match dealer");
        }

        String[] hands = dealStr.substring(2).split(" ");
        Direction current = startDir;
        for (String handStr : hands) {
            deal.put(current, Hand.parse(handStr));
            current = current.leftHandOpponent();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tag("Event", event));
        sb.append(tag("Board", board == 0 ? "" : String.valueOf(board)));
        sb.append(tag("Dealer", dealer.name()));
        sb.append(tag("Vulnerable", vulnerable.name()));
        sb.append(tag("Deal", deal.toString()));
        sb.append(tag("Scoring", scoring.name()));
        if (declarer != null) sb.append(tag("Declarer", declarer.name()));
        if (contract != null) sb.append(tag("Contract", contract.toString()));
        
        sb.append(auction.toString());
        
        if (bidSystemEW != null) sb.append(tag("BidSystemEW", bidSystemEW));
        if (bidSystemNS != null) sb.append(tag("BidSystemNS", bidSystemNS));
        
        return sb.toString();
    }

    private String tag(String name, String value) {
        return "[" + name + " \"" + (value == null ? "" : value) + "\"]\n";
    }

    public void updateContractFromAuction() {
        ContractState state = ContractState.fromCalls(dealer, auction.getCalls());
        if (state.isAuctionComplete()) {
            this.contract = state;
            this.declarer = state.isPassedOut() ? null : state.declarer;
        } else {
            this.contract = null;
            this.declarer = null;
        }
    }
}
