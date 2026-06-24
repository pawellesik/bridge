package com.example.bridge.model;

import com.example.bridge.R;

public enum Suit {
    SPADES(0, R.drawable.spades, false, "♠", 0xFF000000, "black"),
    HEARTS(1, R.drawable.heart, true, "♥", 0xFFFF0000, "red"),
    CLUBS(2, R.drawable.clubs, false, "♣", 0xFF0D47A1, "#0D47A1"),
    DIAMONDS(3, R.drawable.diamonds, true, "♦", 0xFFFF6D00, "#FF6D00");

    public final int priority;
    public final int resId;
    public final boolean isRed;
    public final String symbol;
    public final int color;
    public final String hexColor;

    Suit(int priority, int resId, boolean isRed, String symbol, int color, String hexColor) {
        this.priority = priority;
        this.resId = resId;
        this.isRed = isRed;
        this.symbol = symbol;
        this.color = color;
        this.hexColor = hexColor;
    }

    public static Suit getSuit(String colorName) {
        switch (colorName) {
            case "Spades":
                return Suit.SPADES;
            case "Hearts":
                return Suit.HEARTS;
            case "Diamonds":
                return Suit.DIAMONDS;
            case "Clubs":
                return Suit.CLUBS;
        }
        return null;
    }
}
