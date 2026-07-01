package com.example.bridge.bridgit;

public enum Suit {
    Clubs(0), Diamonds(1), Hearts(2), Spades(3);

    private final int value;

    Suit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toLetter() {
        switch (this) {
            case Clubs:    return "C";
            case Diamonds: return "D";
            case Hearts:   return "H";
            case Spades:   return "S";
        }
        return "";
    }

    public String toSymbol() {
        switch (this) {
            case Clubs:    return "♣";
            case Diamonds: return "♦";
            case Hearts:   return "♥";
            case Spades:   return "♠";
        }
        return "";
    }

    public Strain toStrain() {
        switch (this) {
            case Clubs:    return Strain.Clubs;
            case Diamonds: return Strain.Diamonds;
            case Hearts:   return Strain.Hearts;
            case Spades:   return Strain.Spades;
        }
        throw new RuntimeException("Invalid suit");
    }

    public boolean isMajor() {
        return this == Hearts || this == Spades;
    }

    public boolean isMinor() {
        return this == Clubs || this == Diamonds;
    }
}
