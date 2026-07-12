package com.example.bridge.bidding.Tools;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HandEvaluator {
    public static class StandardHandEvaluator {
        private static boolean stopped(Hand hand, Suit suit, int countSuit) {
            return hand.highCardPoints(suit) + countSuit >= 5;
        }

        public static int audreyDummyPoints(Hand hand, Suit trumpSuit) {
            long trumpCount = hand.stream().filter(c -> c.getSuit() == trumpSuit).count();
            int adjust = 0;
            if (trumpCount >= 3) {
                int[] bonus = {5, 3, 1};
                for (Suit suit : Suit.values()) {
                    long count = hand.stream().filter(c -> c.getSuit() == suit).count();
                    if (count < 3) {
                        adjust += bonus[(int) count];
                    }
                }
            }
            return adjust;
        }

        private static SuitQuality quality(Hand hand, Suit suit) {
            SuitQuality q = SuitQuality.Poor;
            int hcp = hand.highCardPoints(suit);
            switch (hcp) {
                case 10:
                    q = SuitQuality.Solid;
                    break;
                case 8:
                case 9:
                    q = SuitQuality.Excellent;
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    q = hand.isGoodSuit(suit) ? SuitQuality.Good : SuitQuality.Decent;
                    break;
                default:
                    q = SuitQuality.Poor;
                    break;
            }
            return q;
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
            Map<Suit, Integer> counts = hand.countsBySuit();
            hs.showIsBalanced(hand.isBalanced());
            hs.showIsFlat(hand.is4333());

            long countAces = hand.stream().filter(c -> c.getRank() == Rank.Ace).count();
            Set<Integer> acesSet = new HashSet<>();
            acesSet.add((int) countAces);
            hs.showCountAces(acesSet);

            long countKings = hand.stream().filter(c -> c.getRank() == Rank.King).count();
            Set<Integer> kingsSet = new HashSet<>();
            kingsSet.add((int) countKings);
            hs.showCountKings(kingsSet);

            for (Suit suit : Suit.values()) {
                int dp = hcp + audreyDummyPoints(hand, suit);
                int c = counts.get(suit);
                SuitQuality q = quality(hand, suit);
                int ltc = hand.losers(suit);
                
                HandSummary.SuitSummary.ShowState suitShow = hs.getSuits().get(suit);
                suitShow.showShape(c, c);
                suitShow.showDummyPoints(dp, dp);
                suitShow.showLongHandPoints(startPoints, startPoints);
                suitShow.showQuality(q, q);
                suitShow.showLosers(ltc, ltc);
                
                int keyCards = (int) countAces;
                if (hand.contains(new Card(Rank.King, suit))) {
                    keyCards += 1;
                }
                Set<Integer> kcSet = new HashSet<>();
                kcSet.add(keyCards);
                suitShow.showKeyCards(kcSet);
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
}




























































































































































































































































































































































































































































































