package com.example.bridge.bridgit;

import com.example.bridge.bridgit.Call.Bid;

public class Contract {
    public Bid bid = null;
    public Risk risk = Risk.Undoubled;

    public static Contract parse(String s) {
        Contract contract = new Contract();
        if (!s.equalsIgnoreCase("Pass")) {
            if (s.length() < 2) {
                throw new IllegalArgumentException("Invalid contract " + s);
            }
            int bidLength = 2;
            if (s.length() >= 3 && s.substring(1, 3).equalsIgnoreCase("NT")) {
                bidLength = 3;
            }
            Call call = Call.parse(s.substring(0, bidLength));
            if (call instanceof Bid) {
                contract.bid = (Bid) call;
            } else {
                throw new IllegalArgumentException("Contract can not be " + s);
            }
            String riskStr = s.substring(bidLength).trim();
            if (riskStr.equals("X")) {
                contract.risk = Risk.Doubled;
            } else if (riskStr.equals("XX")) {
                contract.risk = Risk.Redoubled;
            } else if (!riskStr.isEmpty()) {
                throw new IllegalArgumentException("Invalid risk " + riskStr + ". Must be X or XX or nothing.");
            }
        }
        return contract;
    }

    @Override
    public String toString() {
        String s = "Pass";
        if (bid != null) {
            s = bid.toString();
            if (risk != Risk.Undoubled) {
                s += (risk == Risk.Doubled ? "X" : "XX");
            }
        }
        return s;
    }
}
