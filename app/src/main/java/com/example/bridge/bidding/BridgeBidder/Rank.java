package com.example.bridge.bidding.BridgeBidder;

public enum Rank {
    Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace;

    public String toLetter() {
        switch (this) {
            case Two: return "2";
            case Three: return "3";
            case Four: return "4";
            case Five: return "5";
            case Six: return "6";
            case Seven: return "7";
            case Eight: return "8";
            case Nine: return "9";
            case Ten: return "T";
            case Jack: return "J";
            case Queen: return "Q";
            case King: return "K";
            case Ace: return "A";
            default: return "";
        }
    }
}
