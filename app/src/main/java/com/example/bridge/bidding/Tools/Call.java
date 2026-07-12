package com.example.bridge.bidding.Tools;

import java.util.HashMap;
import java.util.Map;

public abstract class Call implements Comparable<Call> {
    protected final int rawValue;

    protected Call(int rawValue) {
        this.rawValue = rawValue;
    }

    public int getRawValue() {
        return rawValue;
    }

    public static Call getNextCall(Call call) {
        int val = call.getRawValue() + 1;
        if (val <= 37) {
            return Call.fromRawValue(val);
        }
        return PASS;
    }

    @Override
    public int hashCode() {
        return rawValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Call)) return false;
        Call other = (Call) obj;
        return rawValue == other.rawValue;
    }

    @Override
    public int compareTo(Call other) {
        return Integer.compare(this.rawValue, other.rawValue);
    }

    public static Call fromRawValue(int rawValue) {
        if (rawValue == 0) return PASS;
        if (rawValue == 1) return DOUBLE;
        if (rawValue == 2) return REDOUBLE;
        if (rawValue >= 3 && rawValue <= 37) {
            int level = (rawValue - 3) / 5 + 1;
            int strainIndex = (rawValue - 3) % 5;
            return new Bid(level, Strain.values()[strainIndex]);
        }
        throw new IllegalArgumentException("Invalid raw value " + rawValue);
    }

    public static final Call PASS = new Pass();
    public static final Call DOUBLE = new DoubleCall();
    public static final Call REDOUBLE = new Redouble();

    public static Call parse(String str) {
        if (str.equalsIgnoreCase("Pass")) return PASS;
        if (str.equals("X")) return DOUBLE;
        if (str.equals("XX")) return REDOUBLE;

        int level = Integer.parseInt(str.substring(0, 1));
        if (level < 1 || level > 7) {
            throw new IllegalArgumentException("Bids must start with a number from 1 to 7");
        }
        Strain strain = parseStrain(str.substring(1));
        return new Bid(level, strain);
    }

    private static Strain parseStrain(String strainString) {
        switch (strainString) {
            case "C":
                return Strain.Clubs;
            case "D":
                return Strain.Diamonds;
            case "H":
                return Strain.Hearts;
            case "S":
                return Strain.Spades;
            case "NT":
                return Strain.NoTrump;
            default:
                throw new IllegalArgumentException("Strain " + strainString + " not recognized.");
        }
    }

    public static final Map<Strain, String> STRAIN_TO_SYMBOL = new HashMap<>();

    static {
        STRAIN_TO_SYMBOL.put(Strain.Clubs, "C");
        STRAIN_TO_SYMBOL.put(Strain.Diamonds, "D");
        STRAIN_TO_SYMBOL.put(Strain.Hearts, "H");
        STRAIN_TO_SYMBOL.put(Strain.Spades, "S");
        STRAIN_TO_SYMBOL.put(Strain.NoTrump, "NT");
    }
}




























































































































































































































































































































































































































































































