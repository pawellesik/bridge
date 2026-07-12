package com.example.bridge.bidding.Tools;

import java.util.*;

public class Hand extends HashSet<Card> {
    public Hand() {
        super();
    }

    public Hand(Collection<? extends Card> cards) {
        super(cards);
    }

    public int highCardPoints() {
        return highCardPoints(null);
    }

    public int highCardPoints(Suit suit) {
        int hcp = 0;
        for (Card c : this) {
            if (suit == null || c.getSuit() == suit) {
                if (c.getRank() == Rank.Ace) hcp += 4;
                else if (c.getRank() == Rank.King) hcp += 3;
                else if (c.getRank() == Rank.Queen) hcp += 2;
                else if (c.getRank() == Rank.Jack) hcp += 1;
            }
        }
        return hcp;
    }

    public int lengthPoints() {
        int lp = 0;
        Map<Suit, Integer> counts = countsBySuit();
        for (int count : counts.values()) {
            if (count >= 5) {
                lp += (count - 4);
            }
        }
        return lp;
    }

    public Map<Suit, Integer> countsBySuit() {
        Map<Suit, Integer> counts = new EnumMap<>(Suit.class);
        for (Suit s : Suit.values()) {
            counts.put(s, 0);
        }
        for (Card c : this) {
            counts.put(c.getSuit(), counts.get(c.getSuit()) + 1);
        }
        return counts;
    }

    public boolean isBalanced() {
        List<Integer> counts = new ArrayList<>(countsBySuit().values());
        Collections.sort(counts);
        return counts.size() == 4 && counts.get(0) >= 2 && counts.get(1) >= 3;
    }

    public boolean is4333() {
        List<Integer> counts = new ArrayList<>(countsBySuit().values());
        Collections.sort(counts);
        return counts.size() == 4 && counts.get(0) == 3;
    }

    public boolean isGoodSuit(Suit suit) {
        int honorsCount = 0;
        int topHonorsCount = 0;
        for (Card c : this) {
            if (c.getSuit() == suit) {
                if (c.getRank().ordinal() >= Rank.Queen.ordinal()) honorsCount++;
                if (c.getRank().ordinal() >= Rank.Ten.ordinal()) topHonorsCount++;
            }
        }
        return honorsCount >= 2 || topHonorsCount >= 3;
    }

    public int losers() {
        return losers(null);
    }

    public int losers(Suit suit) {
        if (suit != null) {
            int suitCount = 0;
            boolean hasAce = false;
            boolean hasKing = false;
            boolean hasQueen = false;
            for (Card c : this) {
                if (c.getSuit() == suit) {
                    suitCount++;
                    if (c.getRank() == Rank.Ace) hasAce = true;
                    if (c.getRank() == Rank.King) hasKing = true;
                    if (c.getRank() == Rank.Queen) hasQueen = true;
                }
            }
            int losers = Math.min(suitCount, 3);
            if (losers == 3 && hasQueen) losers--;
            if (losers >= 2 && hasKing) losers--;
            if (losers >= 1 && hasAce) losers--;
            return losers;
        } else {
            int totalLosers = 0;
            for (Suit s : Suit.values()) {
                totalLosers += losers(s);
            }
            return totalLosers;
        }
    }

    public static final Suit[] SUIT_ORDER = new Suit[]{Suit.Spades, Suit.Hearts, Suit.Diamonds, Suit.Clubs};

    public static Hand parse(String s) {
        if (s == null) throw new NullPointerException("s");
        if (s.equals("-")) return null;

        String[] suits = s.split("\\.", -1);
        if (suits.length != SUIT_ORDER.length) {
            throw new RuntimeException("handString does not contain four suits: " + s);
        }

        Hand hand = new Hand();
        for (int i = 0; i < suits.length; i++) {
            for (char rankChar : suits[i].toCharArray()) {
                Card card = new Card(Card.parseRank(rankChar), SUIT_ORDER[i]);
                if (hand.contains(card)) {
                    throw new IllegalArgumentException("Duplicate card " + card + " in " + s);
                }
                hand.add(card);
            }
        }

        if (hand.size() != 13) {
            throw new RuntimeException("hand " + s + " contains " + hand.size() + " cards. Should have 13.");
        }
        return hand;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<Suit, List<Card>> suitCards = new EnumMap<>(Suit.class);
        for (Card c : this) {
            suitCards.computeIfAbsent(c.getSuit(), k -> new ArrayList<>()).add(c);
        }

        for (int i = 0; i < SUIT_ORDER.length; i++) {
            Suit suit = SUIT_ORDER[i];
            List<Card> cards = suitCards.get(suit);
            if (cards != null) {
                cards.sort((c1, c2) -> c2.getRank().compareTo(c1.getRank()));
                for (Card card : cards) {
                    sb.append(card.getRank().toLetter());
                }
            }
            if (i < SUIT_ORDER.length - 1) sb.append(".");
        }
        return sb.toString();
    }
}




























































































































































































































































































































































































































































































