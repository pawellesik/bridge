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
        if (game == null || auction == null) return explanations;

        try {
            Game tempGame = new Game();
            if (game.getDeal() != null) tempGame.getDeal().putAll(game.getDeal());
            tempGame.dealer = game.dealer;
            tempGame.bidSystemNS = (game.bidSystemNS != null && !game.bidSystemNS.isEmpty()) ? game.bidSystemNS : "NatC";
            tempGame.bidSystemEW = (game.bidSystemEW != null && !game.bidSystemEW.isEmpty()) ? game.bidSystemEW : "PassOnly";

            BiddingState state = new BiddingState(tempGame);

            for (String bidStr : auction) {
                if (bidStr == null || bidStr.trim().isEmpty() || "-".equals(bidStr) || "Pass".equalsIgnoreCase(bidStr) || "P".equalsIgnoreCase(bidStr)) {
                    explanations.add("");
                    continue;
                }

                Call call = null;
                try {
                    call = Call.parse(bidStr);
                } catch (Exception ignored) {}

                if (call == null || call.equals(Call.PASS)) {
                    explanations.add("");
                    continue;
                }

                PositionState ps = state.getNextToAct();
                PositionCalls choices = state.getCallChoices();

                CallDetails details = null;
                if (choices != null) {
                    details = choices.get(call);
                    if (details == null && choices.getBestCall() != null && choices.getBestCall().getCall().equals(call)) {
                        details = choices.getBestCall();
                    }
                }

                String explanation = "";
                if (details != null && ps != null) {
                    String rawDesc = details.getMatchedDescription(ps);
                    if (rawDesc == null || rawDesc.trim().isEmpty()) {
                        rawDesc = details.getDescription(ps);
                    }

                    if (rawDesc != null && !rawDesc.trim().isEmpty()) {
                        String[] lines = rawDesc.trim().split("\n");
                        if (lines.length == 1) {
                            explanation = lines[0];
                        }
                    }
                }

                explanations.add(explanation != null ? explanation : "");

                try {
                    state.makeCall(call);
                } catch (Exception ignored) {
                    break;
                }
            }
        } catch (Exception e) {
            android.util.Log.e("AuctionExplanation", "Error generating explanations", e);
        }

        return explanations;
    }
}
