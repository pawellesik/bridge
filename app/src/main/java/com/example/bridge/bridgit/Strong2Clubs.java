package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class Strong2Clubs extends Bidder {
    public static Iterable<CallFeature> open(PositionState ps) {
        List<CallFeature> res = new ArrayList<>();
        res.add(properties(Call.Bid._2C, Strong2Clubs::respond, true, false, false, null, null, null, "Strong 2C", null));
        res.add(shows(Call.Bid._2C, points(22, 40)));
        return res;
    }

    private static PositionCalls respond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        // Simple waiting response
        choices.addRules(Collections.singletonList(
            shows(Call.Bid._2D, points(0, 18))
        ));
        return choices;
    }
}
