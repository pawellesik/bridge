package com.example.bridge.model;

import androidx.annotation.NonNull;

public class Card implements Comparable<Card> {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }

    @Override
    public int compareTo(Card other) {
        if (this.suit != other.suit) {
            return this.suit.priority - other.suit.priority;
        }
        return other.rank.ordinal() - this.rank.ordinal(); // High to low
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return 31 * suit.hashCode() + rank.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return rank.display + " of " + suit;
    }
}
