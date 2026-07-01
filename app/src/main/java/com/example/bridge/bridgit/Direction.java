package com.example.bridge.bridgit;

public enum Direction {
    N(0), E(1), S(2), W(3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Direction partner() {
        return values()[(ordinal() + 2) % 4];
    }

    public Direction rightHandOpponent() {
        return values()[(ordinal() + 3) % 4];
    }

    public Direction leftHandOpponent() {
        return values()[(ordinal() + 1) % 4];
    }

    public Pair pair() {
        return (ordinal() % 2 == 0) ? Pair.NS : Pair.EW;
    }
}
