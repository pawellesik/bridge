package com.example.bridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }

            shuffle();
        }

    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> deal(int count) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < count && !cards.isEmpty(); i++) {
            hand.add(cards.remove(0));
        }
        return hand;
    }
}