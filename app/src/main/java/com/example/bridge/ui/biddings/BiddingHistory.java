package com.example.bridge.ui.biddings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BiddingHistory {
    private final List<String> auction = new ArrayList<>();

    public BiddingHistory() {
    }

    public List<String> getAuction() {
        return auction;
    }

    public void addFakeAuction() {
        auction.clear();
        auction.add("Pass");
        auction.add("1C");
        auction.add("Pass");
        auction.add("1H");
        auction.add("Pass");
        auction.add("1S");
        auction.add("Pass");
        auction.add("2NT");
        auction.add("Pass");
        auction.add("3C");
        auction.add("Pass");
        auction.add("3H");
        auction.add("Pass");
        auction.add("4H");
        auction.add("X");
        auction.add("Pass");
        auction.add("Pass");
        auction.add("XX");
        auction.add("Pass");
        auction.add("Pass");
        auction.add("Pass");
    }
}
