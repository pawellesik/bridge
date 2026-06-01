package com.example.bridge;

public class DdsSolver {
    static {
        System.loadLibrary("bridge_dds");
    }

    public native void initDds();

    /**
     * Calculates double dummy table for a single denomination/trump.
     * @param cards Array of 16 integers (4 hands * 4 suits)
     * @param trump 0=NT, 1=Spades, 2=Hearts, 3=Diamonds, 4=Clubs
     * @param leader 0=North, 1=East, 2=South, 3=West
     * @return suit * 100 + rank
     */
    public native int calcDDTable(int[] cards, int trump, int leader);

    /**
     * Calculates full double dummy table for all denominations and leads.
     * @param cards Array of 16 integers
     * @return Array of 20 integers (5 trumps * 4 leaders)
     */
    public native int[] calcFullDDTable(int[] cards);
}
