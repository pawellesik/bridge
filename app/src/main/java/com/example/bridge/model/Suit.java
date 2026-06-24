package com.example.bridge.model;

import android.content.Context;
import com.example.bridge.R;

public enum Suit {
    SPADES(0, R.drawable.spades, false, "♠", 0xFF000000, 0xFF000000),
    HEARTS(1, R.drawable.heart, true, "♥", 0xFFFF0000, 0xFFFF0000),
    CLUBS(2, R.drawable.clubs, false, "♣", 0xFF1976D2, 0xFF000000),
    DIAMONDS(3, R.drawable.diamonds, true, "♦", 0xFFF57C00, 0xFFFF0000);

    public final int priority;
    public final int resId;
    public final boolean isRed;
    public final String symbol;
    private final int colorfulColor;
    private final int standardColor;

    Suit(int priority, int resId, boolean isRed, String symbol, int colorfulColor, int standardColor) {
        this.priority = priority;
        this.resId = resId;
        this.isRed = isRed;
        this.symbol = symbol;
        this.colorfulColor = colorfulColor;
        this.standardColor = standardColor;
    }

    public int getColor(Context context) {
        if (context == null) return colorfulColor;
        boolean isColorful = context.getSharedPreferences("BridgePrefs", Context.MODE_PRIVATE)
                .getBoolean("card_colors_colorful", true);
        return isColorful ? colorfulColor : standardColor;
    }

    public String getHexColor(Context context) {
        return String.format("#%06X", (0xFFFFFF & getColor(context)));
    }

    public static Suit getSuit(String colorName) {
        for (Suit s : values()) {
            if (s.name().equalsIgnoreCase(colorName)) return s;
        }
        return null;
    }
}
