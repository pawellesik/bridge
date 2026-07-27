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

    public static Contract fromString(String contractStr) {
        if (contractStr == null || contractStr.equalsIgnoreCase("PASS") || contractStr.isEmpty()) {
            return new Contract(true);
        }
        try {
            // Remove any extra spaces and split by space or just take parts
            String clean = contractStr.trim();
            int level;
            String suitPart;

            if (clean.contains(" ")) {
                String[] parts = clean.split(" ");
                level = Integer.parseInt(parts[0]);
                suitPart = parts[1].toUpperCase();
            } else {
                // Handle formats like "4S" or "1NT"
                level = Character.getNumericValue(clean.charAt(0));
                suitPart = clean.substring(1).toUpperCase();
            }

            if (suitPart.equals("NT") || suitPart.equals("BA") || suitPart.equals("NOTRUMP")) {
                return new Contract(level, null);
            }

            // Handle PBN shorthands: S, H, D, C
            if (suitPart.equals("S")) return new Contract(level, Suit.SPADES);
            if (suitPart.equals("H")) return new Contract(level, Suit.HEARTS);
            if (suitPart.equals("D")) return new Contract(level, Suit.DIAMONDS);
            if (suitPart.equals("C")) return new Contract(level, Suit.CLUBS);

            // Handle full names or plurals
            if (suitPart.endsWith("S")) suitPart = suitPart.substring(0, suitPart.length() - 1);
            for (Suit s : Suit.values()) {
                if (s.name().startsWith(suitPart)) {
                    return new Contract(level, s);
                }
            }
            return new Contract(level, null);
        } catch (Exception e) {
            return new Contract(true);
        }
    }
}
