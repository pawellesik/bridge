package com.example.bridge.model;

import androidx.annotation.NonNull;

public class Contract {
    private final int level;
    private final Suit suit; // null represents No Trump (NT)
    private final boolean isPass;

    public Contract(int level, Suit suit) {
        this.level = level;
        this.suit = suit;
        this.isPass = false;
    }

    public Contract(boolean isPass) {
        this.level = 0;
        this.suit = null;
        this.isPass = isPass;
    }

    public int getLevel() {
        return level;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isPass() {
        return isPass;
    }

    public boolean isNoTrump() {
        return !isPass && suit == null;
    }

    @NonNull
    @Override
    public String toString() {
        if (isPass) return "PASS";
        String suitName = (suit == null) ? "NT" : suit.name().substring(0, 1).toUpperCase() + suit.name().substring(1).toLowerCase();
        return level + " " + suitName;
    }
}
