package com.example.bridge.ui.biddings;

import com.example.bridge.bidding.Tools.*;
import com.example.bridge.ui.history.Pbn;
import java.util.ArrayList;
import java.util.List;

public class AuctionExplanationHelper {

    public static List<String> generateExplanations(Pbn pbn) {
        if (pbn == null) return new ArrayList<>();
        try {
            Game game = pbn.toGame();
            if (game == null) return new ArrayList<>();
            return generateExplanations(game, pbn.getAuction());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<String> generateExplanations(Game game, List<String> auction) {
        List<String> explanations = new ArrayList<>();

        return explanations;
    }
}
