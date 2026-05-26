package com.example.bridge.model;

public enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;

    public static int getSuitPriority(Suit suit) {
        switch (suit) {
            case SPADES: return 0;
            case HEARTS: return 1;
            case CLUBS: return 2;
            case DIAMONDS: return 3;
            default: return 4;
        }
    }
}

