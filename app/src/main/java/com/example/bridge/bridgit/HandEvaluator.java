package com.example.bridge.bridgit;

import com.example.bridge.bridgit.additional.SuitQuality;
import java.util.*;

public class HandEvaluator {
    private static boolean stopped(Hand hand, Suit suit, int countSuit) {
        return hand.highCardPoints(suit) + countSuit >= 5;
    }

    public static int audreyDummyPoints(Hand hand, Suit trumpSuit) {
        int trumpCount = 0;
        for (Card c : hand) {
            if (c.getSuit() == trumpSuit) trumpCount++;
        }
        int adjust = 0;
        if (trumpCount >= 3) {
            int[] bonus = { 5, 3, 1 };
            Map<Suit, Integer> counts = hand.countsBySuit();
            for (Suit suit : Card.Suits) {
                int count = counts.get(suit);
                if (count < 3) {
                    adjust += bonus[count];
                }
            }
        }
        return adjust;
    }

    private static SuitQuality quality(Hand hand, Suit suit) {
        int hcp = hand.highCardPoints(suit);
        if (hcp == 10) return SuitQuality.Solid;
        if (hcp >= 8) return SuitQuality.Excellent;
        if (hcp >= 3) {
            return hand.isGoodSuit(suit) ? SuitQuality.Good : SuitQuality.Decent;
        }
        return SuitQuality.Poor;
    }

    public static void evaluate(Hand hand, HandSummary.ShowState hs) {
        int hcp = hand.highCardPoints();
        int losers = hand.losers();
        hs.showHighCardPoints(hcp, hcp);
        int startPoints = hcp + hand.lengthPoints();
        hs.showStartingPoints(startPoints, startPoints);
        hs.showNoTrumpDummyPoints(startPoints, startPoints);
        hs.showNoTrumpLongHandPoints(startPoints, startPoints);
        hs.showLosers(losers, losers);

        hs.showIsBalanced(hand.isBalanced());
        hs.showIsFlat(hand.is4333());

        int countAces = 0;
        int countKings = 0;
        for (Card c : hand) {
            if (c.getRank() == Rank.Ace) countAces++;
            if (c.getRank() == Rank.King) countKings++;
        }
        hs.showCountAces(Collections.singleton(countAces));
        hs.showCountKings(Collections.singleton(countKings));

        Map<Suit, Integer> counts = hand.countsBySuit();
        for (Suit suit : Card.Suits) {
            int dp = hcp + audreyDummyPoints(hand, suit);
            int c = counts.get(suit);
            SuitQuality q = quality(hand, suit);
            int ltc = hand.losers(suit);

            HandSummary.SuitSummary.ShowState suitShow = hs.suits.get(suit);
            suitShow.showShape(c, c);
            suitShow.showDummyPoints(dp, dp);
            suitShow.showLongHandPoints(startPoints, startPoints);
            suitShow.showQuality(q, q);
            suitShow.showLosers(ltc, ltc);

            int keyCards = countAces;
            boolean hasKing = false;
            boolean hasQueen = false;
            for (Card card : hand) {
                if (card.getSuit() == suit) {
                    if (card.getRank() == Rank.King) hasKing = true;
                    if (card.getRank() == Rank.Queen) hasQueen = true;
                }
            }
            if (hasKing) keyCards++;
            suitShow.showKeyCards(Collections.singleton(keyCards));
            suitShow.showHaveQueen(hasQueen);
            suitShow.showStopped(stopped(hand, suit, c));

            int rule9 = c;
            for (Card card : hand) {
                if (card.getSuit() == suit && card.getRank().ordinal() >= Rank.Ten.ordinal()) {
                    rule9++;
                }
            }
            suitShow.showRuleOf9Points(rule9);
            boolean hasAce = false;
            for (Card card : hand) {
                if (card.getSuit() == suit && card.getRank() == Rank.Ace) hasAce = true;
            }
            suitShow.showFirstRoundControl(c == 0 || hasAce);
            suitShow.showSecondRoundControl(c <= 1 || hasKing);
        }
    }
}
