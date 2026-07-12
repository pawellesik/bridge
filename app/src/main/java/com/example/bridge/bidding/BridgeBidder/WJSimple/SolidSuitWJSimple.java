package com.example.bridge.bidding.BridgeBidder.WJSimple;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.ArrayList;
import java.util.List;

public class SolidSuitWJSimple extends WJSimple {
    public static Iterable<CallFeature> BIDS(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(shows(Bid._2D, EXCELLENT_PLUS_SUIT, shape(6, 11), points(12, 16), id("SolidSuitWJSimple.BIDS _2D")));
        rules.add(shows(Bid._2H, EXCELLENT_PLUS_SUIT, shape(6, 11), points(12, 16), id("SolidSuitWJSimple.BIDS _2H")));
        rules.add(shows(Bid._2S, EXCELLENT_PLUS_SUIT, shape(6, 11), points(12, 16), id("SolidSuitWJSimple.BIDS _2S")));
        return rules;
    }
}
