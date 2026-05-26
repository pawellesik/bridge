package com.example.bridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCards(List<Card> newCards) {
        List<Card> sortedCard = sortCard(newCards);
        hand.addAll(sortedCard);
    }

    public List<Card> sortCard (List<Card> cards) {
        List<Card> actualHand = cards;

        Collections.sort(actualHand, (c1, c2) -> {
            int s1 = Suit.getSuitPriority(c1.getSuit());
            int s2 = Suit.getSuitPriority(c2.getSuit());
            if (s1 != s2) {
                return s1 - s2;
            }
            return c2.getRank().ordinal() - c1.getRank().ordinal();
        });
        return actualHand;
    };

    public boolean removeCard(Card card) {
        return hand.remove(card);
    }

    public void clearHand() {
        hand.clear();
    }
}