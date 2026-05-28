package com.example.bridge.model;

import com.example.bridge.R;

public enum Suit {
    SPADES(0, R.drawable.spades, false),
    HEARTS(1, R.drawable.heart, true),
    CLUBS(2, R.drawable.clubs, false),
    DIAMONDS(3, R.drawable.diamonds, true);

    public final int priority;
    public final int resId;
    public final boolean isRed;

    Suit(int priority, int resId, boolean isRed) {
        this.priority = priority;
        this.resId = resId;
        this.isRed = isRed;
    }
}
