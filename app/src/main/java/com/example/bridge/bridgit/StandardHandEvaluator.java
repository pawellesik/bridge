package com.example.bridge.bridgit;

import java.util.*;

public class StandardHandEvaluator {

    private static boolean stopped(Hand hand, Suit suit, int countSuit) {
        return hand.highCardPoints(suit) + countSuit >= 5;
    }

    public static int audreyDummyPoints(Hand hand, Suit trumpSuit) {
        long trumpCount = hand.stream().filter(c -> c.getSuit() == trumpSuit).count();
        int adjust = 0;
        if (trumpCount >= 3) {
            int[] bonus = { 5, 3, 1 };
            for (Suit suit : Card.Suits) {
                long count = hand.stream().filter(c -> c.getSuit() == suit).count();
                if (count < 3) {
                    adjust += bonus[(int) count];
                }
            }
        }
        return adjust;
    }

    private static SuitQuality quality(Hand hand, Suit suit) {
        int hcp = hand.highCardPoints(suit);
        switch (hcp) {
            case 10: return SuitQuality.Solid;
            case 8:
            case 9: return SuitQuality.Excellent;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7: return hand.isGoodSuit(suit) ? SuitQuality.Good : SuitQuality.Decent;
            default: return SuitQuality.Poor;
        }
    }

    public static void evaluate(Hand hand, HandSummary.ShowState hs) {
        int hcp = hand.highCardPoints();
        int losers = hand.losers();
        hs.showHighCardPoints(hcp, hcp);
        
        int lp = hand.lengthPoints();
        int startPoints = hcp + lp;
        hs.showStartingPoints(startPoints, startPoints);
        hs.showNoTrumpDummyPoints(startPoints, startPoints);
        hs.showNoTrumpLongHandPoints(startPoints, startPoints);
        hs.showLosers(losers, losers);
        
        Map<Suit, Integer> counts = hand.countsBySuit();
        hs.showIsBalanced(hand.isBalanced());
        hs.showIsFlat(hand.is4333());
        
        int countAces = (int) hand.stream().filter(c -> c.getRank() == Rank.Ace).count();
        hs.showCountAces(Collections.singleton(countAces));
        
        int countKings = (int) hand.stream().filter(c -> c.getRank() == Rank.King).count();
        hs.showCountKings(Collections.singleton(countKings));
        
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
            if (hand.contains(new Card(Rank.King, suit))) {
                keyCards += 1;
            }
            suitShow.showKeyCards(Collections.singleton(keyCards));
            suitShow.showHaveQueen(hand.contains(new Card(Rank.Queen, suit)));
            suitShow.showStopped(stopped(hand, suit, c));
            
            int rule9 = c;
            for (Card card : hand) {
                if (card.getSuit() == suit && card.getRank().ordinal() >= Rank.Ten.ordinal()) {
                    rule9++;
                }
            }
            suitShow.showRuleOf9Points(rule9);
            suitShow.showFirstRoundControl(c == 0 || hand.contains(new Card(Rank.Ace, suit)));
            suitShow.showSecondRoundControl(c <= 1 || hand.contains(new Card(Rank.King, suit)));
        }
    }
}
