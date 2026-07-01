package com.example.bridge.bridgit;

public enum Vulnerable {
    None(0), NS(1), EW(2), All(3);

    private final int value;

    Vulnerable(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
