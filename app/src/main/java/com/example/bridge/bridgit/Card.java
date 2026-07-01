package com.example.bridge.bridgit;

import java.util.*;

public class Card {
    public static final Suit[] Suits = { Suit.Clubs, Suit.Diamonds, Suit.Hearts, Suit.Spades };

    private static final Map<Character, Rank> stringToRank = new HashMap<>();
    static {
        stringToRank.put('2', Rank.Two);
        stringToRank.put('3', Rank.Three);
        stringToRank.put('4', Rank.Four);
        stringToRank.put('5', Rank.Five);
        stringToRank.put('6', Rank.Six);
        stringToRank.put('7', Rank.Seven);
        stringToRank.put('8', Rank.Eight);
        stringToRank.put('9', Rank.Nine);
        stringToRank.put('T', Rank.Ten);
        stringToRank.put('J', Rank.Jack);
        stringToRank.put('Q', Rank.Queen);
        stringToRank.put('K', Rank.King);
        stringToRank.put('A', Rank.Ace);
    }

    public static Rank parseRank(char rankString) {
        Rank rank = stringToRank.get(rankString);
        if (rank != null) {
            return rank;
        }
        throw new IllegalArgumentException("rank " + rankString + " is invalid character");
    }

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public int hashCode() {
        return rank.ordinal() * 32 + suit.ordinal();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card card = (Card) obj;
            return card.rank == rank && card.suit == suit;
        }
        return false;
    }

    public static List<Card> newDeck(boolean shuffle) {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suits) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        if (shuffle) {
            Collections.shuffle(deck);
        }
        return deck;
    }

    @Override
    public String toString() {
        return rank.toLetter() + suit.toLetter();
    }
}
