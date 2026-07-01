package com.example.bridge.bridgit;

public class Range {
    public int min;
    public int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static String getString(int min, int max, int limit) {
        if (min == max) return String.valueOf(max);
        if (max >= limit) return min + "+";
        return min + "–" + max;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Range) {
            Range other = (Range) obj;
            return this.min == other.min && this.max == other.max;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return min * 31 + max;
    }

    @Override
    public String toString() {
        return min == max ? String.valueOf(min) : min + "-" + max;
    }
}
