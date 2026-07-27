package com.example.bridge.model;

public enum Rank {
    TWO("2", 0), THREE("3", 0), FOUR("4", 0), FIVE("5", 0), SIX("6", 0), SEVEN("7", 0), 
    EIGHT("8", 0), NINE("9", 0), TEN("10", 0), JACK("J", 1), QUEEN("Q", 2), KING("K", 3), ACE("A", 4);

    public final String display;
    public final int hcp;

    Rank(String display, int hcp) {
        this.display = display;
        this.hcp = hcp;
    }

    public static Rank fromPbnLetter(char c) {
        switch (c) {
            case 'A': return ACE;
            case 'K': return KING;
            case 'Q': return QUEEN;
            case 'J': return JACK;
            case 'T': return TEN;
            case '9': return NINE;
            case '8': return EIGHT;
            case '7': return SEVEN;
            case '6': return SIX;
            case '5': return FIVE;
            case '4': return FOUR;
            case '3': return THREE;
            case '2': return TWO;
        }
        return null;
    }
}
