package com.example.bridge.model;

import com.example.bridge.R;

public enum Suit {
    SPADES(0, R.drawable.suit_spades, false),
    HEARTS(1, R.drawable.suit_hearts, true),
    CLUBS(2, R.drawable.suit_clubs, false),
    DIAMONDS(3, R.drawable.suit_diamonds, true);

    public final int priority;
    public final int resId;
    public final boolean isRed;

    Suit(int priority, int resId, boolean isRed) {
        this.priority = priority;
        this.resId = resId;
        this.isRed = isRed;
    }
}
