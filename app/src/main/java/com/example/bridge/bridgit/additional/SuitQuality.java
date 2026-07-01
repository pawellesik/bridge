package com.example.bridge.bridgit.additional;

public enum SuitQuality {
    Poor(0), Decent(1), Good(2), Excellent(3), Solid(4);

    private final int value;

    SuitQuality(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
