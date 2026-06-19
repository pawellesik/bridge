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

    private Contract(boolean isPass) {
        this.level = 0;
        this.suit = null;
        this.isPass = isPass;
    }

    public static final Contract PASS = new Contract(true);

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

    /**
     * Helper to create Contract from existing string representation like "1 Spades" or "3 NT"
     */
    public static Contract fromString(String contractStr) {
        if (contractStr == null || contractStr.equalsIgnoreCase("PASS")) {
            return PASS;
        }
        try {
            String[] parts = contractStr.split(" ");
            int level = Integer.parseInt(parts[0]);
            if (parts.length < 2 || parts[1].equalsIgnoreCase("NT")) {
                return new Contract(level, null);
            }
            String suitName = parts[1].toUpperCase();
            // Handle plural or singular
            if (suitName.endsWith("S")) suitName = suitName.substring(0, suitName.length() - 1);
            
            for (Suit s : Suit.values()) {
                if (s.name().startsWith(suitName)) {
                    return new Contract(level, s);
                }
            }
            return new Contract(level, null);
        } catch (Exception e) {
            return PASS;
        }
    }
}
