package com.example.bridge.bridgit;

public enum Strain {
    Clubs(0), Diamonds(1), Hearts(2), Spades(3), NoTrump(4);

    private final int value;

    Strain(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toLetter() {
        switch (this) {
            case Clubs:     return "C";
            case Diamonds:  return "D";
            case Hearts:    return "H";
            case Spades:    return "S";
            case NoTrump:   return "NT";
        }
        return "";
    }

    public String toSymbol() {
        switch (this) {
            case Clubs:    return "♣";
            case Diamonds: return "♦";
            case Hearts:   return "♥";
            case Spades:   return "♠";
            case NoTrump:  return "NT";
        }
        return "";
    }

    public Suit toSuit() {
        switch (this) {
            case Clubs:    return Suit.Clubs;
            case Diamonds: return Suit.Diamonds;
            case Hearts:   return Suit.Hearts;
            case Spades:   return Suit.Spades;
        }
        return null; // NoTrump
    }
}
