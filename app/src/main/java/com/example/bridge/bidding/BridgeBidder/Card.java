package com.example.bridge.bidding.BridgeBidder;

import java.util.*;
import java.util.stream.Collectors;

public class Card {
    public static final Suit[] SUITS = { Suit.Clubs, Suit.Diamonds, Suit.Hearts, Suit.Spades };

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

    public static Rank parseRank(char rankString) {
        Rank rank = STRING_TO_RANK.get(rankString);
        if (rank != null) {
            return rank;
        }
        throw new IllegalArgumentException("rank " + rankString + " is invalid character");
    }

    @Override
    public int hashCode() {
        return rank.ordinal() * 32 + suit.ordinal();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card card = (Card) obj;
        return card.rank == rank && card.suit == suit;
    }

    public static List<Card> newDeck(boolean shuffle) {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : SUITS) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        if (shuffle) {
            Collections.shuffle(deck);
        }
        return deck;
    }

    public static final Map<Character, Rank> STRING_TO_RANK = new HashMap<>();
    static {
        STRING_TO_RANK.put('2', Rank.Two);
        STRING_TO_RANK.put('3', Rank.Three);
        STRING_TO_RANK.put('4', Rank.Four);
        STRING_TO_RANK.put('5', Rank.Five);
        STRING_TO_RANK.put('6', Rank.Six);
        STRING_TO_RANK.put('7', Rank.Seven);
        STRING_TO_RANK.put('8', Rank.Eight);
        STRING_TO_RANK.put('9', Rank.Nine);
        STRING_TO_RANK.put('T', Rank.Ten);
        STRING_TO_RANK.put('J', Rank.Jack);
        STRING_TO_RANK.put('Q', Rank.Queen);
        STRING_TO_RANK.put('K', Rank.King);
        STRING_TO_RANK.put('A', Rank.Ace);
    }

    @Override
    public String toString() {
        return rank.toLetter() + suit.toLetter();
    }
}
