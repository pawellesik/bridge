package com.example.bridge.ui.biddings;

import com.example.bridge.bidding.Constraints.LogID;
import com.example.bridge.bidding.Tools.*;
import com.example.bridge.ui.history.Pbn;
import java.util.ArrayList;
import java.util.List;

public class AuctionDescriptionHelper {

    public static List<String> generateDescriptions(Pbn pbn) {
        if (pbn == null) return new ArrayList<>();
        try {
            Game game = pbn.toGame();
            if (game == null) return new ArrayList<>();
            return generateDescriptions(game, pbn.getAuction());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<String> generateDescriptions(Game game, List<String> auction) {
        List<String> descriptions = new ArrayList<>();
        if (game == null || auction == null) return descriptions;

        try {
            Game tempGame = new Game();
            if (game.getDeal() != null) tempGame.getDeal().putAll(game.getDeal());
            tempGame.dealer = game.dealer;
            tempGame.bidSystemNS = (game.bidSystemNS != null && !game.bidSystemNS.isEmpty()) ? game.bidSystemNS : "NatC";
            tempGame.bidSystemEW = (game.bidSystemEW != null && !game.bidSystemEW.isEmpty()) ? game.bidSystemEW : "PassOnly";

            BiddingState state = new BiddingState(tempGame);

            for (String bidStr : auction) {
                if (bidStr == null || bidStr.isEmpty() || bidStr.equals("-")) {
                    descriptions.add("");
                    continue;
                }

                Call call = null;
                try {
                    call = Call.parse(bidStr);
                } catch (Exception ignored) {}

                if (call == null) {
                    descriptions.add("");
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

                String desc = "";
                if (details != null && ps != null) {
                    String conventionDesc = getConventionDescription(ps, details);
                    if (conventionDesc != null) {
                        desc = conventionDesc;
                    } else {
                        String rawDesc = details.getDescription(ps);
                        if (rawDesc != null && !rawDesc.trim().isEmpty()) {
                            desc = GameBiddingHistoryAdapter.flattenAndDeduplicateLines(rawDesc);
                        }
                    }
                }

                descriptions.add(desc != null ? desc : "");

                try {
                    state.makeCall(call);
                } catch (Exception ignored) {
                    break;
                }
            }
        } catch (Exception e) {
            android.util.Log.e("AuctionDescHelper", "Error generating descriptions", e);
        }

        return descriptions;
    }

    public static String getConventionDescription(PositionState ps, CallDetails details) {
        if (details == null) return null;

        List<BidRule> rules = !details.getMatchedRules().isEmpty() ? details.getMatchedRules() : details.getRules();
        for (BidRule rule : rules) {
            String ruleId = LogID.getID(rule);
            if (ruleId != null) {
                String lowerId = ruleId.toLowerCase();
                if (lowerId.contains("askking") || lowerId.contains("respondking") || lowerId.contains("king")) {
                    return "Ask for Kings";
                }
                if (lowerId.contains("acesask") || lowerId.contains("initiateconvention") || lowerId.contains("respondcountaces") || lowerId.contains("ace")) {
                    return "Ask for Aces";
                }
            }
        }

        if (details.getCall() != null) {
            String callStr = details.getCall().toString();
            if (callStr.equalsIgnoreCase("4C") || callStr.equalsIgnoreCase("4NT")) {
                return "Ask for Aces";
            }
        }

        return null;
    }
}
