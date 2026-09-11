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
        if (game == null || auction == null || auction.isEmpty()) {
            return explanations;
        }

        try {
            Game simGame = Game.parse(game.getDeal().toString(), game.vulnerable.name());
            simGame.dealer = game.dealer;
            simGame.bidSystemNS = game.bidSystemNS;
            simGame.bidSystemEW = game.bidSystemEW;

            BiddingState biddingState = new BiddingState(simGame);

            for (String bidStr : auction) {
                if (bidStr == null || bidStr.trim().isEmpty() || "-".equals(bidStr)) {
                    explanations.add("");
                    continue;
                }

                PositionState ps = biddingState.getNextToAct();
                PositionCalls choices = biddingState.getCallChoices();
                Call call = Call.parse(bidStr);
                CallDetails details = choices != null ? choices.get(call) : null;

                String desc = "";
                if (details != null) {
                    List<BidRule> showRules = new ArrayList<>();
                    for (BidRule rule : details.getRules()) {
                        if (com.example.bridge.bidding.Constraints.RuleShow.hasRuleShow(rule)) {
                            showRules.add(rule);
                        }
                    }

                    if (!showRules.isEmpty()) {
                        desc = BiddingInfoFormatter.getDescriptionsForRules(ps, showRules);
                    }
                }

                explanations.add(desc != null ? desc : "");

                try {
                    biddingState.makeCall(call);
                } catch (Exception e) {
                    break;
                }
            }
        } catch (Exception e) {
            for (int i = 0; i < auction.size(); i++) {
                if (explanations.size() <= i) {
                    explanations.add("");
                }
            }
        }

        return explanations;
    }
}
