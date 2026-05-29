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
}
