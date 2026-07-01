package com.example.bridge.bridgit;

import java.util.*;

public abstract class Call implements Comparable<Call> {
    protected final int rawValue;

    protected Call(int rawValue) {
        this.rawValue = rawValue;
    }

    public int getRawValue() {
        return rawValue;
    }

    @Override
    public int hashCode() {
        return rawValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Call) {
            return ((Call) obj).rawValue == this.rawValue;
        }
        return false;
    }

    @Override
    public int compareTo(Call other) {
        return this.rawValue - other.rawValue;
    }

    public static final Call Pass = new Pass();
    public static final Call Double = new DoubleCall();
    public static final Call Redouble = new RedoubleCall();

    public static Call parse(String str) {
        if (str.equalsIgnoreCase("Pass")) return Pass;
        if (str.equals("X")) return Double;
        if (str.equals("XX")) return Redouble;
        try {
            int level = Integer.parseInt(str.substring(0, 1));
            Strain strain = parseStrain(str.substring(1));
            return new Bid(level, strain);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid call string: " + str);
        }
    }

    private static Strain parseStrain(String s) {
        switch (s) {
            case "C": return Strain.Clubs;
            case "D": return Strain.Diamonds;
            case "H": return Strain.Hearts;
            case "S": return Strain.Spades;
            case "NT": return Strain.NoTrump;
            default: throw new IllegalArgumentException("Invalid strain: " + s);
        }
    }

    public static class Pass extends Call {
        public Pass() { super(0); }
        @Override
        public String toString() { return "Pass"; }
    }

    public static class DoubleCall extends Call {
        public DoubleCall() { super(1); }
        @Override
        public String toString() { return "X"; }
    }

    public static class RedoubleCall extends Call {
        public RedoubleCall() { super(2); }
        @Override
        public String toString() { return "XX"; }
    }

    public static class Bid extends Call {
        private final int level;
        private final Strain strain;

        public Bid(int level, Suit suit) {
            this(level, suit.toStrain());
        }

        public Bid(int level, Strain strain) {
            super((level - 1) * 5 + strain.ordinal() + 3);
            this.level = level;
            this.strain = strain;
        }

        public int getLevel() { return level; }
        public Strain getStrain() { return strain; }
        public Suit getSuit() { return strain.toSuit(); }

        @Override
        public String toString() {
            return level + strain.toLetter();
        }

        public int jumpOver(Bid other) {
            return (this.rawValue - other.rawValue - 1) / 5;
        }
    }
}
