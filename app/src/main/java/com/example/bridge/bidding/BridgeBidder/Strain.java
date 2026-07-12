package com.example.bridge.bidding.BridgeBidder;

public enum Strain {
    Clubs, Diamonds, Hearts, Spades, NoTrump;

    public String toLetter() {
        switch (this) {
            case Clubs: return "C";
            case Diamonds: return "D";
            case Hearts: return "H";
            case Spades: return "S";
            case NoTrump: return "NT";
            default: return "";
        }
    }

    public String toSymbol() {
        switch (this) {
            case Clubs: return "♣";
            case Diamonds: return "♦";
            case Hearts: return "♥";
            case Spades: return "♠";
            case NoTrump: return "NT";
            default: return "";
        }
    }

    public Suit toSuit() {
        switch (this) {
            case Clubs: return Suit.Clubs;
            case Diamonds: return Suit.Diamonds;
            case Hearts: return Suit.Hearts;
            case Spades: return Suit.Spades;
            case NoTrump: return null;
            default: return null;
        }
    }
}
