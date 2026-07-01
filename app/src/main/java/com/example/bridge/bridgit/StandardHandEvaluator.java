package com.example.bridge.bridgit;

import java.util.Map;

public class StandardHandEvaluator {
    public static void evaluate(Hand hand, HandSummary.ShowState showState) {
        if (hand == null) return;

        // Points
        int hcp = hand.highCardPoints();
        int lp = hand.lengthPoints();
        showState.showHighCardPoints(hcp, hcp);
        showState.showStartingPoints(hcp + lp, hcp + lp);

        // Shape
        Map<Suit, Integer> counts = hand.countsBySuit();
        for (Suit suit : Card.Suits) {
            Integer count = counts.get(suit);
            if (count != null) {
                HandSummary.SuitSummary.ShowState suitShow = showState.suits.get(suit);
                if (suitShow != null) {
                    suitShow.showShape(count, count);
                }
            }
        }

        // Balanced
        showState.showIsBalanced(hand.isBalanced());
        showState.showIsFlat(hand.is4333());
    }
}
