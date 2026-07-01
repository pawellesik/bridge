package com.example.bridge.bridgit;

import java.util.*;
import java.util.stream.Collectors;

public class Hand extends HashSet<Card> {

    public Hand() {
        super();
    }

    public Hand(Collection<Card> cards) {
        super(cards);
    }

    public int highCardPoints() {
        return highCardPoints(null);
    }

    public int highCardPoints(Suit suit) {
        int points = 0;
        for (Card c : this) {
            if (suit == null || c.getSuit() == suit) {
                switch (c.getRank()) {
                    case Ace:   points += 4; break;
                    case King:  points += 3; break;
                    case Queen: points += 2; break;
                    case Jack:  points += 1; break;
                    default: break;
                }
            }
        }
        return points;
    }

    public int lengthPoints() {
        int points = 0;
        Map<Suit, Integer> counts = countsBySuit();
        for (int count : counts.values()) {
            if (count >= 5) {
                points += (count - 4);
            }
        }
        return points;
    }

    public Map<Suit, Integer> countsBySuit() {
        Map<Suit, Integer> counts = new HashMap<>();
        for (Suit suit : Card.Suits) {
            counts.put(suit, 0);
        }
        for (Card c : this) {
            counts.put(c.getSuit(), counts.get(c.getSuit()) + 1);
        }
        return counts;
    }

    public boolean isBalanced() {
        List<Integer> sortedCounts = countsBySuit().values().stream()
                .sorted()
                .collect(Collectors.toList());
        return sortedCounts.size() == 4 && sortedCounts.get(0) >= 2 && sortedCounts.get(1) >= 3;
    }

    public boolean is4333() {
        List<Integer> sortedCounts = countsBySuit().values().stream()
                .sorted()
                .collect(Collectors.toList());
        return sortedCounts.size() == 4 && sortedCounts.get(0) == 3;
    }

    public boolean isGoodSuit(Suit suit) {
        int top3 = 0;
        int top5 = 0;
        for (Card c : this) {
            if (c.getSuit() == suit) {
                if (c.getRank().ordinal() >= Rank.Queen.ordinal()) top3++;
                if (c.getRank().ordinal() >= Rank.Ten.ordinal()) top5++;
            }
        }
        return top3 >= 2 || top5 >= 3;
    }

    public int losers() {
        return losers(null);
    }

    public int losers(Suit suit) {
        if (suit != null) {
            int count = 0;
            boolean hasAce = false, hasKing = false, hasQueen = false;
            for (Card c : this) {
                if (c.getSuit() == suit) {
                    count++;
                    if (c.getRank() == Rank.Ace) hasAce = true;
                    if (c.getRank() == Rank.King) hasKing = true;
                    if (c.getRank() == Rank.Queen) hasQueen = true;
                }
            }
            int losers = Math.min(count, 3);
            if (losers == 3 && hasQueen) losers--;
            if (losers >= 2 && hasKing) losers--;
            if (losers >= 1 && hasAce) losers--;
            return losers;
        } else {
            int total = 0;
            for (Suit s : Card.Suits) {
                total += losers(s);
            }
            return total;
        }
    }

    public static final Suit[] SuitOrder = { Suit.Spades, Suit.Hearts, Suit.Diamonds, Suit.Clubs };

    public static Hand parse(String s) {
        if (s == null) throw new NullPointerException("s");
        if (s.equals("-")) return null;

        String[] suits = s.split("\\.");
        if (suits.length != SuitOrder.length) {
            throw new IllegalArgumentException("handString does not contain four suits");
        }
        Hand hand = new Hand();
        for (int i = 0; i < suits.length; i++) {
            for (char rankChar : suits[i].toCharArray()) {
                Card card = new Card(Card.parseRank(rankChar), SuitOrder[i]);
                if (hand.contains(card)) {
                    throw new IllegalArgumentException("Duplicate card " + card + " in " + s);
                }
                hand.add(card);
            }
        }
        if (hand.size() != 13) {
            throw new IllegalArgumentException("hand " + s + " contains " + hand.size() + " cards. Should have 13.");
        }
        return hand;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<Suit, List<Card>> suitCards = new HashMap<>();
        for (Card c : this) {
            suitCards.computeIfAbsent(c.getSuit(), k -> new ArrayList<>()).add(c);
        }
        for (int i = 0; i < SuitOrder.length; i++) {
            Suit suit = SuitOrder[i];
            List<Card> cards = suitCards.get(suit);
            if (cards != null) {
                cards.sort((c1, c2) -> c2.getRank().ordinal() - c1.getRank().ordinal());
                for (Card c : cards) {
                    sb.append(c.getRank().toLetter());
                }
            }
            if (i < SuitOrder.length - 1) sb.append(".");
        }
        return sb.toString();
    }
}
