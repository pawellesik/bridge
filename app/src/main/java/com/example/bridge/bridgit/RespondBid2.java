package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class RespondBid2 extends Respond {

    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> res = new ArrayList<>();
        
        // Partner (Opener) rebid a suit or NT. 
        // We need to either pass, raise, or bid NT.
        
        res.add(shows(Call.Bid._2H, fit(), points(6, 10)));
        res.add(shows(Call.Bid._2S, fit(), points(6, 10)));
        res.add(shows(Call.Bid._3H, fit(), points(11, 12)));
        res.add(shows(Call.Bid._3S, fit(), points(11, 12)));
        res.add(shows(Call.Bid._4H, fit(), points(13, 16)));
        res.add(shows(Call.Bid._4S, fit(), points(13, 16)));
        
        res.add(shows(Call.Bid._2NT, points(11, 12)));
        res.add(shows(Call.Bid._3NT, points(13, 16)));
        
        // Add Compete rules
        for (CallFeature f : Compete.compBids(ps)) {
            res.add(f);
        }
        
        return res;
    }
}
